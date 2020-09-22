package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.GUID;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


final class ModificationResult {


    private final long byteDifference;


    private final int chunkDifference;


    private final Set<GUID> occuredGUIDs = new HashSet<GUID>();


    public ModificationResult(final int chunkCountDiff, final long bytesDiffer,
            final GUID... occurred) {
        assert occurred != null && occurred.length > 0;
        this.chunkDifference = chunkCountDiff;
        this.byteDifference = bytesDiffer;
        this.occuredGUIDs.addAll(Arrays.asList(occurred));
    }


    public ModificationResult(final int chunkCountDiff, final long bytesDiffer,
            final Set<GUID> occurred) {
        this.chunkDifference = chunkCountDiff;
        this.byteDifference = bytesDiffer;
        this.occuredGUIDs.addAll(occurred);
    }


    public long getByteDifference() {
        return this.byteDifference;
    }


    public int getChunkCountDifference() {
        return this.chunkDifference;
    }


    public Set<GUID> getOccuredGUIDs() {
        return new HashSet<GUID>(this.occuredGUIDs);
    }

}
