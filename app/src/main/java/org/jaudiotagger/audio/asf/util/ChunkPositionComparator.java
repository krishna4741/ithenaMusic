
package org.jaudiotagger.audio.asf.util;

import org.jaudiotagger.audio.asf.data.Chunk;

import java.io.Serializable;
import java.util.Comparator;


public final class ChunkPositionComparator implements Comparator<Chunk>,
        Serializable {


    private static final long serialVersionUID = -6337108235272376289L;


    public int compare(final Chunk first, final Chunk second) {
        return Long.valueOf(first.getPosition())
                .compareTo(second.getPosition());
    }
}