package com.doruk.dnotes.dataUtils;

import com.doruk.dnotes.store.GlobalConstants;
import com.doruk.dnotes.utils.NumberUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class MetaWriter {
    private final OutputStream stream;
    private final byte[] buff = new byte[300];
    private int cursor = 0;

    /**
     * Note: Always pass the FileOutputStream
     * @param stream
     * @throws IOException
     *
     */
    public MetaWriter(OutputStream stream) throws IOException {
        this.stream = stream;
        writeVersion();
    }

    private void writeVersion() throws IOException {
        // write version
        buff[cursor++] = Markers.Keys.DNT_VERSION_KEY;
        var bytes = NumberUtils.toContinuationBytes(GlobalConstants.APP_VERSION_CODE);
        for (byte b : bytes)
            buff[cursor++] = b;
    }

    // write metadata
    private void writeMeta(byte fileType, Date createdAt, byte transform, byte[] obfuscationSeed) {
        //file type
        buff[cursor++] = Markers.Keys.FILE_TYPE_KEY;
        buff[cursor++] = fileType;

        // transformation
        buff[cursor++] = Markers.Keys.DATA_TRANSFORM_KEY;
        buff[cursor++] = transform;

        // created at
        buff[cursor++] = Markers.Keys.CREATED_AT_KEY;
        var createdAtBytes = NumberUtils.toContinuationBytes(createdAt.getTime());
        for (byte b : createdAtBytes)
            buff[cursor++] = b;

        // obfuscation seed
        buff[cursor++] = Markers.Keys.OBFUSCATION_SEED_KEY;
        // write length of seed, always 32
        buff[cursor++] = GlobalConstants.OBFUSCATION_SEED_LENGTH;
        for (byte b : obfuscationSeed)
            buff[cursor++] = b;
    }

    // default metadata, while saving a note
    public void writeDefaultsMeta(Date createdAt, byte[] obfuscationSeed) {
        this.writeMeta(Markers.FileType.NORMAL, createdAt, Markers.Transforms.OBFUSCATED, obfuscationSeed);
    }

    // metadata for backup files
    public void writeBackupMeta(byte[] obfuscationSeed, boolean isEncrypted, Date createdAt) {
        this.writeMeta(Markers.FileType.BACKUP, createdAt,
                isEncrypted ? Markers.Transforms.ENCRYPTED : Markers.Transforms.OBFUSCATED, obfuscationSeed);

        // write backup specific metadata
    }

    // metadata for shared files
    public void writeSharedMeta(byte[] obfuscationSeed, String sharedBy, Date createdAt) {
        this.writeMeta(Markers.FileType.SHARED, createdAt, Markers.Transforms.OBFUSCATED, obfuscationSeed);

        // write shared specific metadata
        var nameBytes = sharedBy.getBytes(StandardCharsets.UTF_8);
        // write marker and length
        buff[cursor++] = Markers.Keys.SHARED_BY_KEY;
        var nameLengthBytes = NumberUtils.toContinuationBytes(nameBytes.length);
        for (byte b : nameLengthBytes)
            buff[cursor++] = b;

        // write name
        for (byte b : nameBytes)
            buff[cursor++] = b;
    }

    public void commit() throws IOException {
        // write meta start
        stream.write(Markers.META_START);
        // write meta length
        var lengthBytes = NumberUtils.toContinuationBytes(cursor);
        for (byte b : lengthBytes)
            stream.write(b);

        // finally end of meta, and file starts
        buff[cursor++] = Markers.META_END;
        stream.write(buff, 0, cursor);
        stream.flush();
    }
}
