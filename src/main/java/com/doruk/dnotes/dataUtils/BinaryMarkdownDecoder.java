package com.doruk.dnotes.dataUtils;

import java.io.InputStream;

import com.doruk.dnotes.interfaces.ProcessingInputStage;

public class BinaryMarkdownDecoder extends BinaryParser implements ProcessingInputStage {
    public BinaryMarkdownDecoder(String[] codecsName) {
        super(codecsName);
    }

    @Override
    public InputStream apply(InputStream input) {
        return null;
    }
}
