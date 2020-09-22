package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.*;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;


public class MetadataReader implements ChunkReader {


    private final static GUID[] APPLYING = {
            ContainerType.EXTENDED_CONTENT.getContainerGUID(),
            ContainerType.METADATA_OBJECT.getContainerGUID(),
            ContainerType.METADATA_LIBRARY_OBJECT.getContainerGUID() };


    public boolean canFail() {
        return false;
    }


    public GUID[] getApplyingIds() {
        return APPLYING.clone();
    }


    public Chunk read(final GUID guid, final InputStream stream,
            final long streamPosition) throws IOException {
        final BigInteger chunkLen = Utils.readBig64(stream);

        final MetadataContainer result = new MetadataContainer(guid,
                streamPosition, chunkLen);
        // isExtDesc will be set to true, if a extended content description
        // chunk is read
        // otherwise it is a metadata object, there are only slight differences
        final boolean isExtDesc = result.getContainerType() == ContainerType.EXTENDED_CONTENT;
        final int recordCount = Utils.readUINT16(stream);
        for (int i = 0; i < recordCount; i++) {
            int languageIndex = 0;
            int streamNumber = 0;
            if (!isExtDesc) {

                languageIndex = Utils.readUINT16(stream);
                assert languageIndex >= 0
                        && languageIndex < MetadataDescriptor.MAX_LANG_INDEX;
                assert result.getContainerType() == ContainerType.METADATA_LIBRARY_OBJECT
                        || languageIndex == 0;
                streamNumber = Utils.readUINT16(stream);
                assert streamNumber >= 0
                        && streamNumber <= MetadataDescriptor.MAX_STREAM_NUMBER;
            }
            final int nameLen = Utils.readUINT16(stream);
            String recordName = null;
            if (isExtDesc) {
                recordName = Utils.readFixedSizeUTF16Str(stream, nameLen);
            }
            final int dataType = Utils.readUINT16(stream);
            assert dataType >= 0 && dataType <= 6;
            final long dataLen = isExtDesc ? Utils.readUINT16(stream) : Utils
                    .readUINT32(stream);
            assert dataLen >= 0;
            assert result.getContainerType() == ContainerType.METADATA_LIBRARY_OBJECT
                    || dataLen <= MetadataDescriptor.DWORD_MAXVALUE;
            if (!isExtDesc) {
                recordName = Utils.readFixedSizeUTF16Str(stream, nameLen);
            }
            final MetadataDescriptor descriptor = new MetadataDescriptor(result
                    .getContainerType(), recordName, dataType, streamNumber,
                    languageIndex);
            switch (dataType) {
            case MetadataDescriptor.TYPE_STRING:
                descriptor.setStringValue(Utils.readFixedSizeUTF16Str(stream,
                        (int) dataLen));
                break;
            case MetadataDescriptor.TYPE_BINARY:
                descriptor.setBinaryValue(Utils.readBinary(stream, dataLen));
                break;
            case MetadataDescriptor.TYPE_BOOLEAN:
                assert isExtDesc && dataLen == 4 || !isExtDesc && dataLen == 2;
                descriptor.setBooleanValue(readBoolean(stream, (int) dataLen));
                break;
            case MetadataDescriptor.TYPE_DWORD:
                assert dataLen == 4;
                descriptor.setDWordValue(Utils.readUINT32(stream));
                break;
            case MetadataDescriptor.TYPE_WORD:
                assert dataLen == 2;
                descriptor.setWordValue(Utils.readUINT16(stream));
                break;
            case MetadataDescriptor.TYPE_QWORD:
                assert dataLen == 8;
                descriptor.setQWordValue(Utils.readUINT64(stream));
                break;
            case MetadataDescriptor.TYPE_GUID:
                assert dataLen == GUID.GUID_LENGTH;
                descriptor.setGUIDValue(Utils.readGUID(stream));
                break;
            default:
                // Unknown, hopefully the convention for the size of the
                // value
                // is given, so we could read it binary
                descriptor.setStringValue("Invalid datatype: "
                        + new String(Utils.readBinary(stream, dataLen)));
            }
            result.addDescriptor(descriptor);
        }
        return result;
    }


    private boolean readBoolean(final InputStream stream, final int bytes)
            throws IOException {
        final byte[] tmp = new byte[bytes];
        stream.read(tmp);
        boolean result = false;
        for (int i = 0; i < bytes; i++) {
            if (i == bytes - 1) {
                result = tmp[i] == 1;
                assert tmp[i] == 0 || tmp[i] == 1;
            } else {
                assert tmp[i] == 0;
            }
        }
        return result;
    }

}
