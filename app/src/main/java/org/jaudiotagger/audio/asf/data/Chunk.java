
package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;

import java.math.BigInteger;


public class Chunk {


    protected final BigInteger chunkLength;


    protected final GUID guid;


    protected long position;


    public Chunk(final GUID headerGuid, final BigInteger chunkLen) {
        if (headerGuid == null) {
            throw new IllegalArgumentException("GUID must not be null.");
        }
        if (chunkLen == null || chunkLen.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "chunkLen must not be null nor negative.");
        }
        this.guid = headerGuid;
        this.chunkLength = chunkLen;
    }


    public Chunk(final GUID headerGuid, final long pos,
            final BigInteger chunkLen) {
        if (headerGuid == null) {
            throw new IllegalArgumentException("GUID must not be null");
        }
        if (pos < 0) {
            throw new IllegalArgumentException(
                    "Position of header can'timer be negative.");
        }
        if (chunkLen == null || chunkLen.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "chunkLen must not be null nor negative.");
        }
        this.guid = headerGuid;
        this.position = pos;
        this.chunkLength = chunkLen;
    }


    @Deprecated
    public long getChunckEnd() {
        return this.position + this.chunkLength.longValue();
    }


    public long getChunkEnd() {
        return this.position + this.chunkLength.longValue();
    }


    public BigInteger getChunkLength() {
        return this.chunkLength;
    }


    public GUID getGuid() {
        return this.guid;
    }


    public long getPosition() {
        return this.position;
    }


    public String prettyPrint(final String prefix) {
        final StringBuilder result = new StringBuilder();
        result.append(prefix).append("-> GUID: ").append(
                GUID.getGuidDescription(this.guid))
                .append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  | : Starts at position: ").append(
                getPosition()).append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  | : Last byte at: ").append(
                getChunkEnd() - 1).append(Utils.LINE_SEPARATOR);
        return result.toString();
    }


    public void setPosition(final long pos) {
        this.position = pos;
    }


    @Override
    public String toString() {
        return prettyPrint("");
    }

}