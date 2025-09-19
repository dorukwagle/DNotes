package com.doruk.dnotes.dataUtils;

import java.io.InputStream;

public class BinaryMarkdownEncoder extends BinaryParser {
    public BinaryMarkdownEncoder(String[] codecsName) {
        super(codecsName);
    }

    @Override
    public InputStream apply(InputStream input) {
        return null;
    }
}
