package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.ChunkContainer;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Logger;


abstract class ChunkContainerReader<ChunkType extends ChunkContainer>
        implements ChunkReader {


    protected static final Logger LOGGER = Logger
            .getLogger("org.jaudiotabgger.audio"); //$NON-NLS-1$


    public final static int READ_LIMIT = 8192;


    protected final boolean eachChunkOnce;


    protected boolean hasFailingReaders = false;


    protected final Map<GUID, ChunkReader> readerMap = new HashMap<GUID, ChunkReader>();


    protected ChunkContainerReader(
            final List<Class<? extends ChunkReader>> toRegister,
            final boolean readChunkOnce) {
        this.eachChunkOnce = readChunkOnce;
        for (final Class<? extends ChunkReader> curr : toRegister) {
            register(curr);
        }
    }


    protected void checkStream(final InputStream stream)
            throws IllegalArgumentException {
        if (this.hasFailingReaders && !stream.markSupported()) {
            throw new IllegalArgumentException(
                    "Stream has to support mark/reset.");
        }
    }


    abstract protected ChunkType createContainer(long streamPosition,
            BigInteger chunkLength, InputStream stream) throws IOException;


    protected ChunkReader getReader(final GUID guid) {
        return this.readerMap.get(guid);
    }


    protected boolean isReaderAvailable(final GUID guid) {
        return this.readerMap.containsKey(guid);
    }


    public ChunkType read(final GUID guid, final InputStream stream,
            final long chunkStart) throws IOException, IllegalArgumentException {
        checkStream(stream);
        final CountingInputStream cis = new CountingInputStream(stream);
        if (!Arrays.asList(getApplyingIds()).contains(guid)) {
            throw new IllegalArgumentException(
                    "provided GUID is not supported by this reader.");
        }
        // For Know the file pointer pointed to an ASF header chunk.
        final BigInteger chunkLen = Utils.readBig64(cis);

        final ChunkType result = createContainer(chunkStart, chunkLen, cis);
        // 16 bytes have already been for providing the GUID
        long currentPosition = chunkStart + cis.getReadCount() + 16;

        final HashSet<GUID> alreadyRead = new HashSet<GUID>();

        while (currentPosition < result.getChunkEnd()) {
            final GUID currentGUID = Utils.readGUID(cis);
            final boolean skip = this.eachChunkOnce
                    && (!isReaderAvailable(currentGUID) || !alreadyRead
                            .add(currentGUID));
            Chunk chunk;

            if (!skip && isReaderAvailable(currentGUID)) {
                final ChunkReader reader = getReader(currentGUID);
                if (reader.canFail()) {
                    cis.mark(READ_LIMIT);
                }
                chunk = getReader(currentGUID).read(currentGUID, cis,
                        currentPosition);
            } else {
                chunk = ChunkHeaderReader.getInstance().read(currentGUID, cis,
                        currentPosition);
            }
            if (chunk == null) {

                cis.reset();
            } else {
                if (!skip) {
                    result.addChunk(chunk);
                }
                currentPosition = chunk.getChunkEnd();
                // Always take into account, that 16 bytes have been read prior
                // to calling this method
                assert cis.getReadCount() + chunkStart + 16 == currentPosition;
            }
        }

        return result;
    }


    private <T extends ChunkReader> void register(final Class<T> toRegister) {
        try {
            final T reader = toRegister.newInstance();
            for (final GUID curr : reader.getApplyingIds()) {
                this.readerMap.put(curr, reader);
            }
        } catch (InstantiationException e) {
            LOGGER.severe(e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.severe(e.getMessage());
        }
    }

}
