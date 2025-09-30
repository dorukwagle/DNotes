package com.doruk.dnotes.MarkdownEditor.codecs;

import java.util.stream.Stream;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.SegmentNode;
import com.doruk.dnotes.MarkdownEditor.codecs.enums.ParagraphModifiers;
import com.doruk.dnotes.MarkdownEditor.codecs.interfaces.Codec.CodecType;
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

    @Override
    public void loadEditorDocument(FXTextEditor editor, ParagraphNode node) {

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
}
