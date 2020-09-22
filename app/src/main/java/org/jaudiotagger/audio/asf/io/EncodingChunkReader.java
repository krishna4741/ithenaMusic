
package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.EncodingChunk;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;


class EncodingChunkReader implements ChunkReader {

    private final static GUID[] APPLYING = { GUID.GUID_ENCODING };


    protected EncodingChunkReader() {
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
        final BigInteger chunkLen = Utils.readBig64(stream);
        final EncodingChunk result = new EncodingChunk(chunkLen);
        int readBytes = 24;
        // Can'timer be interpreted

        stream.skip(20);
        readBytes += 20;


        final int stringCount = Utils.readUINT16(stream);
        readBytes += 2;


        for (int i = 0; i < stringCount; i++) {
            final String curr = Utils.readCharacterSizedString(stream);
            result.addString(curr);
            readBytes += 4 + 2 * curr.length();
        }
        stream.skip(chunkLen.longValue() - readBytes);
        result.setPosition(chunkStart);
        return result;
    }

}