package com.doruk.dnotes.dataUtils.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Consumer;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.codecs.enums.ParagraphModifiers;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.MarkdownDecoder;

public class BinaryMarkdownDecoder extends BinaryParser implements MarkdownDecoder {
    public BinaryMarkdownDecoder(Enum<?>[] codecsName) {
        super(codecsName);
    }

    private int continuousBytesToInteger(InputStream stream) throws IOException {
        byte[] ref = new byte[1];
        int value = 0;
        int shift = 0;

        while (stream.read(ref) != -1) {
            value |= (ref[0] & 0x7F) << shift;

            if ((ref[0] & 0x80) == 0)
                break;

            shift += 7;
        }

        return value;
    }

    private void readGlobalStyles(InputStream stream, ParagraphNode node) throws IOException {
        byte[] marker = new byte[1];

        if (stream.read(marker) == -1 ||marker[0] != Markers.GLOBALS_START)
            throw new ProcessingStageException("Invalid byte found while reading global styles");

        int length = continuousBytesToInteger(stream);
        byte[] globalBytes = new byte[length];

        if (stream.read(globalBytes) == -1)
            return;

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

        int length = continuousBytesToInteger(stream);
        byte[] globalBytes = new byte[length];

        if (stream.read(globalBytes) == -1)
            return;

        var modifierStream = new ByteArrayInputStream(globalBytes);
        byte[] modifier = new byte[1];

        while (modifierStream.read(modifier) != -1) {
            var style = bytesCodecMap.get(modifier[0]);
            if (style == null)
                throw new ProcessingStageException("Unrecognized global modifier byte");

            int value = continuousBytesToInteger(modifierStream);
            node.addModifier((ParagraphModifiers) style, value);
        }
    }

    private void readSegmentStyles(InputStream stream, ParagraphNode node) {

    }

    private void readSegmentStateValues(InputStream stream, ParagraphNode node) {

    }

    private void readSegmentText(InputStream stream, ParagraphNode node) {

    }

    private ParagraphNode readParagraphNode(InputStream input) throws IOException {
        // read  start byte
        byte[] b = new byte[1];
        var read = input.read(b);

        if (b[0] != Markers.PARAGRAPH_START)
            throw new ProcessingStageException("Invalid paragraph start byte");

        return  null;
    }

    @Override
    public void decode(InputStream input, Consumer<ParagraphNode> consumer) {
        try {
            byte[] marker = new byte[1];

            while (input.read(marker) != -1 && marker[0] == Markers.PARAGRAPH_START)
                consumer.accept(readParagraphNode(input));

        } catch (Exception e) {
            throw new ProcessingStageException(e.getMessage(), e);
        }

    }
}
