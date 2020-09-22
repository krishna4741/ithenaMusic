
package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;

import java.math.BigInteger;
import java.util.Date;


public class FileHeader extends Chunk {


    private final BigInteger duration;


    private final Date fileCreationTime;


    private final BigInteger fileSize;


    private final long flags;


    private final long maxPackageSize;


    private final long minPackageSize;


    private final BigInteger packageCount;


    private final BigInteger timeEndPos;


    private final BigInteger timeStartPos;


    private final long uncompressedFrameSize;


    public FileHeader(final BigInteger chunckLen, final BigInteger size,
            final BigInteger fileTime, final BigInteger pkgCount,
            final BigInteger dur, final BigInteger timestampStart,
            final BigInteger timestampEnd, final long headerFlags,
            final long minPkgSize, final long maxPkgSize,
            final long uncmpVideoFrameSize) {
        super(GUID.GUID_FILE, chunckLen);
        this.fileSize = size;
        this.packageCount = pkgCount;
        this.duration = dur;
        this.timeStartPos = timestampStart;
        this.timeEndPos = timestampEnd;
        this.flags = headerFlags;
        this.minPackageSize = minPkgSize;
        this.maxPackageSize = maxPkgSize;
        this.uncompressedFrameSize = uncmpVideoFrameSize;
        this.fileCreationTime = Utils.getDateOf(fileTime).getTime();
    }


    public BigInteger getDuration() {
        return this.duration;
    }


    public int getDurationInSeconds() {
        return this.duration.divide(new BigInteger("10000000")).intValue();
    }


    public Date getFileCreationTime() {
        return new Date(this.fileCreationTime.getTime());
    }


    public BigInteger getFileSize() {
        return this.fileSize;
    }


    public long getFlags() {
        return this.flags;
    }


    public long getMaxPackageSize() {
        return this.maxPackageSize;
    }


    public long getMinPackageSize() {
        return this.minPackageSize;
    }


    public BigInteger getPackageCount() {
        return this.packageCount;
    }


    public float getPreciseDuration() {
        return (float) (getDuration().doubleValue() / 10000000d);
    }


    public BigInteger getTimeEndPos() {
        return this.timeEndPos;
    }


    public BigInteger getTimeStartPos() {
        return this.timeStartPos;
    }


    public long getUncompressedFrameSize() {
        return this.uncompressedFrameSize;
    }


    @Override
    public String prettyPrint(final String prefix) {
        final StringBuilder result = new StringBuilder(super.prettyPrint(prefix));
        result.append(prefix).append("  |-> Filesize      = ").append(
                getFileSize().toString()).append(" Bytes").append(
                Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |-> Media duration= ").append(
                getDuration().divide(new BigInteger("10000")).toString())
                .append(" ms").append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |-> Created at    = ").append(
                getFileCreationTime()).append(Utils.LINE_SEPARATOR);
        return result.toString();
    }
}
