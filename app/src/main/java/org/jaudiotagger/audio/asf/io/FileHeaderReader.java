
package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.FileHeader;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;


public class FileHeaderReader implements ChunkReader {


    private final static GUID[] APPLYING = { GUID.GUID_FILE };


    protected FileHeaderReader() {
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
        // Skip client GUID.
        stream.skip(16);
        final BigInteger fileSize = Utils.readBig64(stream);
        // fileTime in 100 ns since midnight of 1st january 1601 GMT
        final BigInteger fileTime = Utils.readBig64(stream);

        final BigInteger packageCount = Utils.readBig64(stream);

        final BigInteger timeEndPos = Utils.readBig64(stream);
        final BigInteger duration = Utils.readBig64(stream);
        final BigInteger timeStartPos = Utils.readBig64(stream);

        final long flags = Utils.readUINT32(stream);

        final long minPkgSize = Utils.readUINT32(stream);
        final long maxPkgSize = Utils.readUINT32(stream);
        final long uncompressedFrameSize = Utils.readUINT32(stream);

        final FileHeader result = new FileHeader(chunkLen, fileSize, fileTime,
                packageCount, duration, timeStartPos, timeEndPos, flags,
                minPkgSize, maxPkgSize, uncompressedFrameSize);
        result.setPosition(chunkStart);
        return result;
    }

}