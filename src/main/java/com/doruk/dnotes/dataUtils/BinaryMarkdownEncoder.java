package com.doruk.dnotes.dataUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.SegmentNode;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.MarkdownEncoder;

public class BinaryMarkdownEncoder extends BinaryParser implements MarkdownEncoder {
    public BinaryMarkdownEncoder(String[] codecsName) {
        super(codecsName);
    }

    private byte[] toContinuationBytes(int value) {
        var bytes = new ArrayList<Byte>(10);
        int shift = 0;

        while (true) {
            byte shifted = (byte) (value >> shift);
            byte byteValue = (byte) (shifted & 0x7F); // extract 7 bits

            if ((shifted & 0x80) == 0) { // check the MSB
                bytes.add(byteValue);
                break;
            }

            bytes.add((byte) (byteValue | 0x80));  // add continuation bit
            shift += 7;
        }
        var cursor = 0;
        var arr = new byte[bytes.size()];
        for (byte b : bytes)
            arr[cursor++] = b;
        return arr;
    }

    // returns the bytes length as int
    private void encodeStatelessStyles(Set<ToolName> styles, ByteArrayOutputStream stream) throws IOException {
        var lengthBytes = toContinuationBytes(styles.size());
        stream.write(lengthBytes);
        
        for (ToolName style : styles)
            stream.write(codecsByteMap.get(style.name()));
    }

    // either Object = either ParagraphModifiers or ToolName, either way, contains to Name
    private int encodeStatefulStyles(Map<Enum, Integer> states) {
        return 0;
    }

    private ByteArrayOutputStream encodeParagraph(ParagraphNode node) {
        var stream = new ByteArrayOutputStream();

        // first mark the start of the paragraph
        stream.write(Markers.PARAGRAPH_START);
        // write globals start
        stream.write(Markers.GLOBALS_START);

        // now write globals length;
        node.getGlobalStyles();
        node.getModifiers();
        node.getSegments();
        for (SegmentNode segments : node.getSegments()) {
            segments.getStyles();
            segments.getStateValues();
        }
        
        return stream;
    }

    /**
     * ORDER OF ENCODING: SKELETON FORMAT OF MARKDOWN
     * 1. PARAGRAPH START MARKER
     * 2. GLOBALS START MARKER
     * 3. GLOBALS LENGTH
     * 4. GLOBALS
     * 5. MODIFIERS START MARKER
     * 6. MODIFIERS LENGTH
     * 7. MODIFIERS
     * 8. SEGMENT START MARKER
     * 9. SEGMENT STYLES MARKER
     * 10. SEGMENT STYLES LENGTH
     * 11. SEGMENT STYLES
     * 12. SEGMENT STATE VALUE MARKERS
     * 13. SEGMENT STATE VALUES LENGTH
     * 14. SEGMENT STATE VALUES
     * 15. SEGMENT TEXT START MARKER
     * 16. SEGMENT TEXT LENGTH
     * 17. SEGMENT TEXT
     * 
     * # ALL LENGTH ARE REPRESENTED IN 7 BITs, WHERE 1 EXTRA BIT IS CONTINUATION BIT
     * # EVERY KEY VALUES LIKE: MODIFIERS, SEGMENT STATE VALUES, HAVE FOLLOWING LENGTH BYTES.
     * 
     * @param nodes
     * @param output
     * @return
     */
    @Override
    public void encode(Stream<ParagraphNode> nodes, OutputStream output) {
        nodes.forEach(node -> {
            try {
                this.encodeParagraph(node).writeTo(output);
            } catch (Exception e) {
                throw new ProcessingStageException(e.getMessage(), e);
            }
        });
    }
}
