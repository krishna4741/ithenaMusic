
package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.EncryptionChunk;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;


class EncryptionChunkReader implements ChunkReader {


    private final static GUID[] APPLYING = { GUID.GUID_CONTENT_ENCRYPTION };


    protected EncryptionChunkReader() {
        // NOTHING toDo
    }


    public boolean canFail() {
        return false;
    }


    public GUID[] getApplyingIds() {
        return APPLYING.clone();
    }


    public Chunk read(final GUID guid, final InputStream stream,
            final long chunkStart) throws IOException {
        EncryptionChunk result;
        final BigInteger chunkLen = Utils.readBig64(stream);
        result = new EncryptionChunk(chunkLen);

        // Can'timer be interpreted

        byte[] secretData;
        byte[] protectionType;
        byte[] keyID;
        byte[] licenseURL;

        // Secret Data length
        int fieldLength;
        fieldLength = (int) Utils.readUINT32(stream);
        // Secret Data
        secretData = new byte[fieldLength + 1];
        stream.read(secretData, 0, fieldLength);
        secretData[fieldLength] = 0;

        // Protection type Length
        fieldLength = 0;
        fieldLength = (int) Utils.readUINT32(stream);
        // Protection Data Length
        protectionType = new byte[fieldLength + 1];
        stream.read(protectionType, 0, fieldLength);
        protectionType[fieldLength] = 0;

        // Key ID length
        fieldLength = 0;
        fieldLength = (int) Utils.readUINT32(stream);
        // Key ID
        keyID = new byte[fieldLength + 1];
        stream.read(keyID, 0, fieldLength);
        keyID[fieldLength] = 0;

        // License URL length
        fieldLength = 0;
        fieldLength = (int) Utils.readUINT32(stream);
        // License URL
        licenseURL = new byte[fieldLength + 1];
        stream.read(licenseURL, 0, fieldLength);
        licenseURL[fieldLength] = 0;

        result.setSecretData(new String(secretData));
        result.setProtectionType(new String(protectionType));
        result.setKeyID(new String(keyID));
        result.setLicenseURL(new String(licenseURL));

        result.setPosition(chunkStart);

        return result;
    }

}