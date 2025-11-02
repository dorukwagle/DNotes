package com.doruk.dnotes.MarkdownEditor.codecs;

import java.util.stream.Stream;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.MutableParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.MutableTextStyle;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.SegmentNode;
import com.doruk.dnotes.MarkdownEditor.codecs.enums.ParagraphModifiers;
import com.doruk.dnotes.MarkdownEditor.codecs.interfaces.Codec.CodecType;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ICodecManager;
import com.doruk.dnotes.MarkdownEditor.utils.ParagraphStyleHelper;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;

public class Coder implements ICodecManager {
    @SuppressWarnings("unchecked")
    @Override
    public Stream<ParagraphNode> dumpEditorDocument(FXTextEditor editor) {
        var codecsList = Factory.createCodecs();

        // return the stream of encoded paragraphs
        return editor.getArea()
                .getParagraphs()
                .stream()
                .map(paragraph -> {
                    var paragraphNode = new ParagraphNode();

                    // first scan the paragraph for all the styles it has, using paragraph codecs
                    // Codec<ParagraphNode, ParagraphStyle>
                    codecsList.stream()
                            .filter(codec -> codec.getCodecType() == CodecType.ParagraphCodec)
                            .forEach(codec -> codec.encode(paragraphNode, paragraph.getParagraphStyle()));

                    // then scan the paragraph for all the styles it has, using text codecs
                    paragraph.getStyledSegments()
                            .forEach(segment -> {
                                var segmentNode = new SegmentNode(segment.getSegment());

                                // iterate over each segment/text codecs
                                // Codec<SegmentNode, TextStyle>
                                codecsList.stream()
                                        .filter(codec -> codec.getCodecType() == CodecType.TextCodec)
                                        .forEach(codec -> codec.encode(segmentNode, segment.getStyle()));

                                paragraphNode.addSegment(segmentNode);
                            });

                    return paragraphNode;
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadEditorDocument(FXTextEditor editor, ParagraphNode node) {
        var area = editor.getArea();
        var codecsList = Factory.createCodecs();

        var dataLen = area.getLength();

        // insert a new paragraph
        if (dataLen > 0)
            area.insertText(dataLen++, "\n"); // creates a paragraph

        // if the node is empty, add whitespace
        if (dataLen == 0 && isEmptyNode(node)) {
            area.insertText(dataLen, "\u200B");
            return;
        }

        // create mutable paragraph style, then populate it
        var paragraphStyle = new MutableParagraphStyle();

        codecsList.stream()
                .filter(codec -> codec.getCodecType() == CodecType.ParagraphCodec)
                .forEach(codec -> codec.decode(node, paragraphStyle));

        // iterate each segments and decode them
        for (var segment : node.getSegments()) {
            var textStyle = new MutableTextStyle();

            codecsList.stream()
                    .filter(codec -> codec.getCodecType() == CodecType.TextCodec)
                    .forEach(codec -> codec.decode(segment, textStyle));

            // apply the segment style
            area.setTextInsertionStyle(StyleHelper.convertToTextStyle(textStyle));

            // add the text to the document, then increment the data length
            area.insertText(dataLen, segment.getText());
            dataLen += segment.getText().length();
        }

        // set paragraph style
        var parIndex = editor.getParagraphIndexAtPos(dataLen);
        area.setParagraphStyle(
                parIndex,
                ParagraphStyleHelper.convertToParagraphStyle(paragraphStyle)
        );
    }

    @Override
    public Enum<?>[] getCodecsValues() {
        // manually add codecs avoiding loops, to maintain consistency and same order
        // always
        return new Enum[] {
                ParagraphModifiers.IsItemChecked,
                ParagraphModifiers.Level,
                ParagraphModifiers.LineCount,
                ParagraphModifiers.NumberListId,
                ParagraphModifiers.Offset,
                ToolName.Underline,
                ToolName.AlignLeft,
                ToolName.CheckList,
                ToolName.H1,
                ToolName.H2,
                ToolName.H3,
                ToolName.BulletList,
                ToolName.NumberList,
                ToolName.H4,
                ToolName.Bold,
                ToolName.Italic,
                ToolName.Blockquote,
                ToolName.AlignCenter,
                ToolName.Strikethrough,
                ToolName.FontColor,
                ToolName.FontBG,
                ToolName.Font,
        };
    }

    private boolean isEmptyNode(ParagraphNode node) {
        var segments = node.getSegments();
        if (segments.isEmpty())
            return true;

        return segments.getFirst().getText().isEmpty();
    }
}
