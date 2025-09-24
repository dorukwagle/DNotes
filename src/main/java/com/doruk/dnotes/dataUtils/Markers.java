package com.doruk.dnotes.dataUtils;

public final class Markers {
    public final class Transforms {
        public static final byte COMPRESSED = 11;
        public static final byte COMPRESSED_ENCRYPTED = 12;
        public static final byte NORMAL = 13;
    }
    
    public final class FileType {
        public static final byte BACKUP = 21;
        public static final byte SHARED = 22;
    }
    
    // keys to store data, they contain their corresponding value
    public final class Keys {
        public static final byte DNT_VERSION_KEY = 31;
        public static final byte DATA_TRANSFORM_KEY = 32;
        public static final byte FILE_TYPE_KEY = 33;
        public static final byte DOC_NAME_KEY = 34;
        public static final byte SHARED_BY_KEY = 35;
        public static final byte CREATED_AT_KEY = 36;
        public static final byte ORIGIN_AT_KEY = 37;
    }

    public static final byte FILE_START = 1;

    public static final byte META_START = 2;

    public static final byte MARKDOWN_START = 3;

    public static final byte DATABASE_START = 4;

    public static final byte DOC_DATA_START = 5;

    public static final byte PROTECTED_DOC_START = 6;

    public static final byte TERMINATOR = 0;
}
