
package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public final class AsfHeader extends ChunkContainer {

    public final static Charset ASF_CHARSET = Charset.forName("UTF-16LE"); //$NON-NLS-1$


    public final static byte[] ZERO_TERM = { 0, 0 };

    static {
        Set<GUID> MULTI_CHUNKS = new HashSet<GUID>();
        MULTI_CHUNKS.add(GUID.GUID_STREAM);
    }


    private final long chunkCount;


    public AsfHeader(final long pos, final BigInteger chunkLen,
            final long chunkCnt) {
        super(GUID.GUID_HEADER, pos, chunkLen);
        this.chunkCount = chunkCnt;
    }


    public ContentDescription findContentDescription() {
        ContentDescription result = getContentDescription();
        if (result == null && getExtendedHeader() != null) {
            result = getExtendedHeader().getContentDescription();
        }
        return result;
    }


    public MetadataContainer findExtendedContentDescription() {
        MetadataContainer result = getExtendedContentDescription();
        if (result == null && getExtendedHeader() != null) {
            result = getExtendedHeader().getExtendedContentDescription();
        }
        return result;
    }


    public MetadataContainer findMetadataContainer(final ContainerType type) {
        MetadataContainer result = (MetadataContainer) getFirst(type
                .getContainerGUID(), MetadataContainer.class);
        if (result == null) {
            result = (MetadataContainer) getExtendedHeader().getFirst(
                    type.getContainerGUID(), MetadataContainer.class);
        }
        return result;
    }


    public AudioStreamChunk getAudioStreamChunk() {
        AudioStreamChunk result = null;
        final List<Chunk> streamChunks = assertChunkList(GUID.GUID_STREAM);
        for (int i = 0; i < streamChunks.size() && result == null; i++) {
            if (streamChunks.get(i) instanceof AudioStreamChunk) {
                result = (AudioStreamChunk) streamChunks.get(i);
            }
        }
        return result;
    }


    public long getChunkCount() {
        return this.chunkCount;
    }


    public ContentDescription getContentDescription() {
        return (ContentDescription) getFirst(GUID.GUID_CONTENTDESCRIPTION,
                ContentDescription.class);
    }


    public EncodingChunk getEncodingChunk() {
        return (EncodingChunk) getFirst(GUID.GUID_ENCODING, EncodingChunk.class);
    }


    public EncryptionChunk getEncryptionChunk() {
        return (EncryptionChunk) getFirst(GUID.GUID_CONTENT_ENCRYPTION,
                EncryptionChunk.class);
    }


    public MetadataContainer getExtendedContentDescription() {
        return (MetadataContainer) getFirst(
                GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, MetadataContainer.class);
    }


    public AsfExtendedHeader getExtendedHeader() {
        return (AsfExtendedHeader) getFirst(GUID.GUID_HEADER_EXTENSION,
                AsfExtendedHeader.class);
    }


    public FileHeader getFileHeader() {
        return (FileHeader) getFirst(GUID.GUID_FILE, FileHeader.class);
    }


    public StreamBitratePropertiesChunk getStreamBitratePropertiesChunk() {
        return (StreamBitratePropertiesChunk) getFirst(
                GUID.GUID_STREAM_BITRATE_PROPERTIES,
                StreamBitratePropertiesChunk.class);
    }


    @Override
    public String prettyPrint(final String prefix) {
        final StringBuilder result = new StringBuilder(super.prettyPrint(prefix,
                prefix + "  | : Contains: \"" + getChunkCount() + "\" chunks"
                        + Utils.LINE_SEPARATOR));
        return result.toString();
    }
}