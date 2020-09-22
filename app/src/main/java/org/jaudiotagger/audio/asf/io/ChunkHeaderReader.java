
package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;


final class ChunkHeaderReader implements ChunkReader {


    private final static GUID[] APPLYING = { GUID.GUID_UNSPECIFIED };


    private static final ChunkHeaderReader INSTANCE = new ChunkHeaderReader();


    public static ChunkHeaderReader getInstance() {
        return INSTANCE;
    }


    private ChunkHeaderReader() {
        // Hidden
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
        stream.skip(chunkLen.longValue() - 24);
        return new Chunk(guid, chunkStart, chunkLen);
    }

}