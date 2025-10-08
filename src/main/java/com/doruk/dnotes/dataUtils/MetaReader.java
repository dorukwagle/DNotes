package com.doruk.dnotes.dataUtils;

import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.store.GlobalConstants;
import com.doruk.dnotes.utils.NumberUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class MetaReader {
    private final FileInputStream stream;
    private boolean isEncrypted = false;
    private final byte[] obfuscationSeed = new byte[GlobalConstants.OBFUSCATION_SEED_LENGTH];
    private String sharedBy = "";
    private Date createdAt = null;
    private byte fileType = 0;

    // temporary bytes stream
    private ByteArrayInputStream tempStream;

    public MetaReader(FileInputStream stream) {
        this.stream = stream;
    }

    private void parseMeta() throws ProcessingStageException {
        byte[] marker = new byte[1];
        byte[] tmpBuff = new byte[1];
        try {
            while (tempStream.read(marker) != -1) {
                switch (marker[0]) {
                    case Markers.Keys.FILE_TYPE_KEY -> {
                        if (tempStream.read(tmpBuff) < 1) throw new ProcessingStageException("Unable to read file type.");
                        this.fileType = tmpBuff[0];
                    }
                    case Markers.Keys.DATA_TRANSFORM_KEY -> {
                        if (tempStream.read(tmpBuff) < 1) throw new ProcessingStageException("Unable to read file transformation");
                        this.isEncrypted = tmpBuff[0] == Markers.Transforms.ENCRYPTED;
                    }
                    case Markers.Keys.OBFUSCATION_SEED_KEY -> {
                        var len = NumberUtils.continuousBytesToLong(tempStream);
                        if (len < 1) throw new ProcessingStageException("Invalid seed length byte");

                        var seedLen = tempStream.read(obfuscationSeed);
                        if (seedLen < GlobalConstants.OBFUSCATION_SEED_LENGTH)
                            throw new ProcessingStageException("Unable to read seed data");
                    }
                    case Markers.Keys.SHARED_BY_KEY -> {
                        var len = (int)NumberUtils.continuousBytesToLong(tempStream);
                        if (len < 1) throw new ProcessingStageException("Invalid length for user name");

                        var name = new byte[len];
                        var nameLen = tempStream.read(name);
                        if (nameLen < len) throw new ProcessingStageException("Unable to read user name");

                        this.sharedBy = new String(name, StandardCharsets.UTF_8);
                    }
                    case Markers.Keys.CREATED_AT_KEY -> {
                        var dt = NumberUtils.continuousBytesToLong(tempStream);
                        this.createdAt = new Date(dt);
                    }
                    default -> throw new ProcessingStageException("Invalid key identifier byte");
                }
            }
        } catch (IOException e) {
            throw new ProcessingStageException(e.getMessage(), e);
        }
    }

    /**
     * Consumes all the metadata bytes
     * Makes the stream point to the byte following meta end byte
     * Parses the metadata
     */
    public void parse() throws ProcessingStageException {
        try {
            byte[] marker = new byte[1];
            if (stream.read(marker) == -1 || marker[0] != Markers.META_START)
                throw new ProcessingStageException("Invalid byte found while reading meta start");

            // read the length of metadata
            var metaLength = (int)NumberUtils.continuousBytesToLong(stream);
            byte[] metaBytes = new byte[metaLength];
            stream.read(metaBytes);

            // parse the results
            this.tempStream = new ByteArrayInputStream(metaBytes);
            this.parseMeta();

            // read the end of meta
            stream.read(marker);
            if (marker[0] != Markers.META_END)
                throw new ProcessingStageException("Invalid byte found while reading meta end");

        } catch (IOException e) {
            throw new ProcessingStageException(e.getMessage(), e);
        }
    }

    public boolean isDocEncrypted() {
        return this.isEncrypted;
    }

    public byte[] getObfuscationSeed() {
        return obfuscationSeed;
    }

    public String getSharedBy() {
        return sharedBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public byte getFileType() {
        return fileType;
    }
}
