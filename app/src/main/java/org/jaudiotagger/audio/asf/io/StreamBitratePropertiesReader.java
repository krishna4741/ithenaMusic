
package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.data.StreamBitratePropertiesChunk;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;


public class StreamBitratePropertiesReader implements ChunkReader {


    private final static GUID[] APPLYING = { GUID.GUID_STREAM_BITRATE_PROPERTIES };


    protected StreamBitratePropertiesReader() {
        // Nothing to Do
    }


    public boolean canFail() {
        return false;
    }


    public GUID[] getApplyingIds() {
        return APPLYING.clone();
    }


    public Chunk read(final GUID guid, final InputStream stream,
            final long chunkStart) throws IOException {
        final BigInteger chunkLen = Utils.readBig64(stream);
        final StreamBitratePropertiesChunk result = new StreamBitratePropertiesChunk(
                chunkLen);


        final long recordCount = Utils.readUINT16(stream);
        for (int i = 0; i < recordCount; i++) {
            final int flags = Utils.readUINT16(stream);
            final long avgBitrate = Utils.readUINT32(stream);
            result.addBitrateRecord(flags & 0x00FF, avgBitrate);
        }

        result.setPosition(chunkStart);

        return result;
    }

}
