package com.doruk.dnotes.MarkdownEditor.codecs;

import java.util.stream.Stream;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.codecs.codec.H1Codec;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.SegmentNode;
import com.doruk.dnotes.MarkdownEditor.codecs.enums.ParagraphModifiers;
import com.doruk.dnotes.MarkdownEditor.codecs.interfaces.Codec;
import com.doruk.dnotes.MarkdownEditor.codecs.interfaces.Codec.CodecType;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ICodecManager;

public class CodecManager implements ICodecManager {
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
                    codecsList.stream()
                            .filter(codec -> codec.getCodecType() == CodecType.ParagraphCodec)
                            .map(codec -> (Codec<ParagraphNode, ParagraphStyle>) codec)
                            .forEach(codec -> codec.encode(paragraphNode, paragraph.getParagraphStyle()));

                    // then scan the paragraph for all the styles it has, using text codecs
                    paragraph.getStyledSegments()
                            .stream()
                            .forEach(segment -> {
                                var segmentNode = new SegmentNode(segment.getSegment());

                                // iterate over each segment/text codecs
                                codecsList.stream()
                                        .filter(codec -> codec.getCodecType() == CodecType.TextCodec)
                                        .map(codec -> (Codec<SegmentNode, TextStyle>) codec)
                                        .forEach(codec -> codec.encode(segmentNode, segment.getStyle()));

                                paragraphNode.addSegment(segmentNode);
                            });

                    return paragraphNode;
                });
    }

    @Override
    public void loadEditorDocument(FXTextEditor editor) {

    }

    @Override
    public String[] getCodecsValues() {
        // manually add codecs avoiding loops, to maintain consistency and same order
        // always
        return new String[] {
                ParagraphModifiers.IsItemChecked.name(),
                ParagraphModifiers.Level.name(),
                ParagraphModifiers.LineCount.name(),
                ParagraphModifiers.NumberListId.name(),
                ParagraphModifiers.Offset.name(),
                ToolName.Underline.name(),
                ToolName.AlignLeft.name(),
                ToolName.CheckList.name(),
                ToolName.H1.name(),
                ToolName.H2.name(),
                ToolName.H3.name(),
                ToolName.BulletList.name(),
                ToolName.NumberList.name(),
                ToolName.H4.name(),
                ToolName.Bold.name(),
                ToolName.Italic.name(),
                ToolName.Blockquote.name(),
                ToolName.AlignCenter.name(),
                ToolName.Strikethrough.name(),
                ToolName.FontColor.name(),
                ToolName.FontBG.name(),
                ToolName.Font.name()
        };
    }
}
