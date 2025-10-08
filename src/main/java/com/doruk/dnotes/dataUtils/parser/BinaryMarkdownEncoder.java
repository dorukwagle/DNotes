package com.doruk.dnotes.dataUtils.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.SegmentNode;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.MarkdownEncoder;
import com.doruk.dnotes.utils.NumberUtils;

public class BinaryMarkdownEncoder extends BinaryParser implements MarkdownEncoder {
    public BinaryMarkdownEncoder(Enum<?>[] codecsName) {
        super(codecsName);
    }

    // returns the bytes length as int
    private void encodeStatelessStyles(Set<ToolName> styles, OutputStream stream) throws IOException {
        var lengthBytes = NumberUtils.toContinuationBytes(styles.size());
        for (byte b : lengthBytes)
            stream.write(b);
        
        for (ToolName style : styles)
            stream.write(codecsByteMap.get(style));
    }

    // either Object = either ParagraphModifiers or ToolName, either way, contains name()
    private void encodeStatefulStyles(Map<? extends Enum<?>, Integer> states, OutputStream stream) throws IOException {
        var totalBytesLength = new AtomicInteger(states.size()); // each key = 1 byte

        Map<Byte, List<Byte>> encodings = states.entrySet().stream()
            .map(entry -> {
                var contBytes = NumberUtils.toContinuationBytes(entry.getValue());
                totalBytesLength.addAndGet(contBytes.size());
               
               return Map.entry(codecsByteMap.get(entry.getKey()), contBytes);
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        
        // write the length bytes
        var lengthBytes = NumberUtils.toContinuationBytes(totalBytesLength.get());
        for (byte b: lengthBytes)
            stream.write(b);
        
        // write the encodings
        for (Map.Entry<Byte, List<Byte>> entry : encodings.entrySet()) {
            stream.write(entry.getKey());
            for (byte b: entry.getValue())
                stream.write(b);
        }
    }

    private void encodeParagraph(ParagraphNode node, OutputStream stream) throws IOException {
        // first mark the start of the paragraph
        stream.write(Markers.PARAGRAPH_START);

        // write globals start
        stream.write(Markers.GLOBALS_START);
        // write globals
        encodeStatelessStyles(node.getGlobalStyles(), stream);

        // write modifiers start
        stream.write(Markers.GLOBALS_STATE_VALUES);
        // write paragraph modifiers
        encodeStatefulStyles(node.getModifiers(), stream);

        // write segments
        for (SegmentNode segment : node.getSegments()) {
            // write segments start
            stream.write(Markers.SEGMENT_START);
            // write segment style start
            stream.write(Markers.SEGMENT_STYLES);
            // write segment styles
            encodeStatelessStyles(segment.getStyles(), stream);

            // write segment state values start
            stream.write(Markers.SEGMENT_STATE_VALUES);
            // write segment state values
            encodeStatefulStyles(segment.getStateValues(), stream);

            // write segment text start
            stream.write(Markers.SEGMENT_TEXT);
            byte[] textBytes = segment.getText().getBytes(StandardCharsets.UTF_8);
            // write segment text length
            var lengthBytes = NumberUtils.toContinuationBytes(textBytes.length);
            for (byte b: lengthBytes)
                stream.write(b);
            // write all text as bytes
            stream.write(textBytes);
        }

        // mark the end of the paragraph
        stream.write(Markers.PARAGRAPH_END);
    }

    @Override
    public void encode(Stream<ParagraphNode> nodes, OutputStream output) {
        nodes.forEach(node -> {
            try {
                this.encodeParagraph(node, output);
            } catch (Exception e) {
                throw new ProcessingStageException(e.getMessage(), e);
            }
        });
    }
}
