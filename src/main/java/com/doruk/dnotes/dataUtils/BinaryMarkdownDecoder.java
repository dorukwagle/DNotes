package com.doruk.dnotes.dataUtils;

import java.io.InputStream;

public class BinaryMarkdownDecoder extends BinaryParser {
    public BinaryMarkdownDecoder(String[] codecsName) {
        super(codecsName);
    }

    @Override
    public InputStream apply(InputStream input) {
        return null;
    }
}
