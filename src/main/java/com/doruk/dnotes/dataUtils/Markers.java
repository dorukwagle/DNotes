package com.doruk.dnotes.dataUtils;

public final class Markers {
    public static final class Transforms {
        public static final byte OBFUSCATED = 11;
        public static final byte ENCRYPTED = 12;
    }
    
    public static final class FileType {
        public static final byte BACKUP = 21;
        public static final byte SHARED = 22;
        public static final byte NORMAL = 23;
    }
    
    // keys to store data, they contain their corresponding value
    public static final class Keys {
        public static final byte DNT_VERSION_KEY = 31;
        public static final byte DATA_TRANSFORM_KEY = 32;
        public static final byte FILE_TYPE_KEY = 33;
        public static final byte DOC_NAME_KEY = 34;
        public static final byte SHARED_BY_KEY = 35;
        public static final byte CREATED_AT_KEY = 36;
        public static final byte OBFUSCATION_SEED_KEY = 38;
        public static final byte DOC_INTERNAL_PASSWORD = 39;
    }

    public static final byte META_START = 2;

    public static final byte DOC_START = 3;

    public static final byte DATABASE_START = 4;

    public static final byte DOC_DATA_START = 5;

    public static final byte META_END = 6;
}
