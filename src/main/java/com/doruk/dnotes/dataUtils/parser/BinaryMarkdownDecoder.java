package com.doruk.dnotes.dataUtils.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.SegmentNode;
import com.doruk.dnotes.MarkdownEditor.codecs.enums.ParagraphModifiers;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.MarkdownDecoder;
import com.doruk.dnotes.utils.NumberUtils;

import static com.doruk.dnotes.dataUtils.StreamDataUtil.readFully;

public class BinaryMarkdownDecoder extends BinaryParser implements MarkdownDecoder {
    public BinaryMarkdownDecoder(Enum<?>[] codecsName) {
        super(codecsName);
    }

    private void readGlobalStyles(InputStream stream, ParagraphNode node) throws IOException {
        byte[] marker = new byte[1];

        if (stream.read(marker) == -1 ||marker[0] != Markers.GLOBALS_START)
            throw new ProcessingStageException("Invalid byte found while reading global styles");

        int length = (int)NumberUtils.continuousBytesToLong(stream);
        byte[] globalBytes = new byte[length];

        readFully(stream, globalBytes);

        for (byte b : globalBytes) {
            var style = bytesCodecMap.get(b);
            if (style == null)
                throw new ProcessingStageException("Unrecognized global style byte");
            node.addGlobalStyle((ToolName) style);
        }
    }

    private void readGlobalModifiers(InputStream stream, ParagraphNode node) throws IOException {
        byte[] marker = new byte[1];
        if (stream.read(marker) == -1 || marker[0] != Markers.GLOBALS_STATE_VALUES)
            throw new ProcessingStageException("Invalid byte found while reading global modifiers");

        int length = (int)NumberUtils.continuousBytesToLong(stream);
        byte[] globalBytes = new byte[length];

        readFully(stream, globalBytes);

        var modifierStream = new ByteArrayInputStream(globalBytes);
        byte[] modifier = new byte[1];

        while (modifierStream.read(modifier) != -1) {
            var style = bytesCodecMap.get(modifier[0]);
            if (style == null)
                throw new ProcessingStageException("Unrecognized global modifier byte");

            int value = (int)NumberUtils.continuousBytesToLong(modifierStream);
            node.addModifier((ParagraphModifiers) style, value);
        }
    }

    private void readSegmentStyles(InputStream stream, SegmentNode node) throws IOException {
        byte[] marker = new byte[1];
        if (stream.read(marker) == -1 || marker[0] != Markers.SEGMENT_STYLES)
            throw new ProcessingStageException("Invalid byte found while reading segment styles");

        int length = (int)NumberUtils.continuousBytesToLong(stream);
        byte[] segmentBytes = new byte[length];

        readFully(stream, segmentBytes);

        for (byte b : segmentBytes) {
            var style = bytesCodecMap.get(b);
            if (style == null)
                throw new ProcessingStageException("Unrecognized segment style byte");
            node.addStyle((ToolName) style);
        }
    }

    private void readSegmentStateValues(InputStream stream, SegmentNode node) throws IOException {
        byte[] marker = new byte[1];
        if (stream.read(marker) == -1 || marker[0] != Markers.SEGMENT_STATE_VALUES)
            throw new ProcessingStageException("Invalid byte found while reading segment state values");

        int length = (int)NumberUtils.continuousBytesToLong(stream);
        byte[] segmentBytes = new byte[length];

        readFully(stream, segmentBytes);

        var stateStream = new ByteArrayInputStream(segmentBytes);
        byte[] modifier = new byte[1];

        while (stateStream.read(modifier) != -1) {
            var style = bytesCodecMap.get(modifier[0]);
            if (style == null)
                throw new ProcessingStageException("Unrecognized segment state value byte");

            int value = (int)NumberUtils.continuousBytesToLong(stateStream);
            node.addStateValue((ToolName) style, value);
        }
    }

    private void readSegmentText(InputStream stream, SegmentNode node) throws IOException {
        byte[] marker = new byte[1];
        if (stream.read(marker) == -1 || marker[0] != Markers.SEGMENT_TEXT)
            throw new ProcessingStageException("Invalid byte found while reading segment text");

        int length = (int)NumberUtils.continuousBytesToLong(stream);
        byte[] textBytes = new byte[length];

        readFully(stream, textBytes);

        node.setText(new String(textBytes, StandardCharsets.UTF_8));
    }

    private ParagraphNode readParagraphNode(InputStream input) throws IOException {
        // read  start byte
        byte[] marker = new byte[1];
        if (input.read(marker) == -1)
            return null;

        if (marker[0] != Markers.PARAGRAPH_START)
            throw new ProcessingStageException("Invalid paragraph start byte");

        var paragraphNode = new ParagraphNode();

        // read global styles
        readGlobalStyles(input, paragraphNode);

        // read global modifiers
        readGlobalModifiers(input, paragraphNode);

        byte[] sb = new byte[1];
        while (input.read(sb) != -1 && sb[0] != Markers.PARAGRAPH_END) {
            if (sb[0] != Markers.SEGMENT_START)
                throw new ProcessingStageException("Invalid segment start byte");

            SegmentNode segment = new SegmentNode();
            readSegmentStyles(input, segment);
            readSegmentStateValues(input, segment);
            readSegmentText(input, segment);
            paragraphNode.addSegment(segment);
        }

        return  paragraphNode;
    }

    @Override
    public void decode(InputStream input, Consumer<ParagraphNode> consumer) {
        try {
            ParagraphNode node;
            while ((node = readParagraphNode(input)) != null)
                consumer.accept(node);
        } catch (Exception e) {
            throw new ProcessingStageException(e.getMessage(), e);
        }
    }
}
