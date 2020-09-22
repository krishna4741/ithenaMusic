package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.ChunkPositionComparator;
import org.jaudiotagger.audio.asf.util.Utils;

import java.math.BigInteger;
import java.util.*;


public class ChunkContainer extends Chunk {


    private final static Set<GUID> MULTI_CHUNKS;

    static {
        MULTI_CHUNKS = new HashSet<GUID>();
        MULTI_CHUNKS.add(GUID.GUID_STREAM);
    }


    protected static boolean chunkstartsUnique(final ChunkContainer container) {
        boolean result = true;
        final Set<Long> chunkStarts = new HashSet<Long>();
        final Collection<Chunk> chunks = container.getChunks();
        for (final Chunk curr : chunks) {
            result &= chunkStarts.add(curr.getPosition());
        }
        return result;
    }


    private final Map<GUID, List<Chunk>> chunkTable;


    public ChunkContainer(final GUID chunkGUID, final long pos,
            final BigInteger length) {
        super(chunkGUID, pos, length);
        this.chunkTable = new Hashtable<GUID, List<Chunk>>();
    }


    public void addChunk(final Chunk toAdd) {
        final List<Chunk> list = assertChunkList(toAdd.getGuid());
        if (!list.isEmpty() && !MULTI_CHUNKS.contains(toAdd.getGuid())) {
            throw new IllegalArgumentException(
                    "The GUID of the given chunk indicates, that there is no more instance allowed."); //$NON-NLS-1$
        }
        list.add(toAdd);
        assert chunkstartsUnique(this) : "Chunk has equal start position like an already inserted one."; //$NON-NLS-1$
    }


    protected List<Chunk> assertChunkList(final GUID lookFor) {
        List<Chunk> result = this.chunkTable.get(lookFor);
        if (result == null) {
            result = new ArrayList<Chunk>();
            this.chunkTable.put(lookFor, result);
        }
        return result;
    }


    public Collection<Chunk> getChunks() {
        final List<Chunk> result = new ArrayList<Chunk>();
        for (final List<Chunk> curr : this.chunkTable.values()) {
            result.addAll(curr);
        }
        return result;
    }


    protected Chunk getFirst(final GUID lookFor,
            final Class<? extends Chunk> instanceOf) {
        Chunk result = null;
        final List<Chunk> list = this.chunkTable.get(lookFor);
        if (list != null && !list.isEmpty()) {
            final Chunk chunk = list.get(0);
            if (instanceOf.isAssignableFrom(chunk.getClass())) {
                result = chunk;
            }
        }
        return result;
    }


    public boolean hasChunkByGUID(final GUID lookFor) {
        return this.chunkTable.containsKey(lookFor);
    }


    @Override
    public String prettyPrint(final String prefix) {
        return prettyPrint(prefix, "");
    }


    public String prettyPrint(final String prefix, final String containerInfo) {
        final StringBuilder result = new StringBuilder(super.prettyPrint(prefix));
        result.append(containerInfo);
        result.append(prefix).append("  |").append(Utils.LINE_SEPARATOR);
        final ArrayList<Chunk> list = new ArrayList<Chunk>(getChunks());
        Collections.sort(list, new ChunkPositionComparator());

        for (Chunk curr : list) {
            result.append(curr.prettyPrint(prefix + "  |"));
            result.append(prefix).append("  |").append(Utils.LINE_SEPARATOR);
        }
        return result.toString();
    }
}
