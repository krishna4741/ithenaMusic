
package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public final class ContentDescription extends MetadataContainer {

    public final static Set<String> ALLOWED;


    public final static String KEY_AUTHOR = "AUTHOR";


    public final static String KEY_COPYRIGHT = "COPYRIGHT";


    public final static String KEY_DESCRIPTION = "DESCRIPTION";


    public final static String KEY_RATING = "RATING";


    public final static String KEY_TITLE = "TITLE";

    static {
        ALLOWED = new HashSet<String>(Arrays.asList(KEY_AUTHOR,
                KEY_COPYRIGHT, KEY_DESCRIPTION, KEY_RATING, KEY_TITLE));
    }


    public ContentDescription() {
        this(0, BigInteger.ZERO);
    }


    public ContentDescription(final long pos,final BigInteger chunkLen) {
        super(ContainerType.CONTENT_DESCRIPTION, pos, chunkLen);
    }


    public String getAuthor() {
        return getValueFor(KEY_AUTHOR);
    }


    public String getComment() {
        return getValueFor(KEY_DESCRIPTION);
    }


    public String getCopyRight() {
        return getValueFor(KEY_COPYRIGHT);
    }


    @Override
    public long getCurrentAsfChunkSize() {
        long result = 44; // GUID + UINT64 for size + 5 times string length
        // (each
        // 2 bytes) + 5 times zero term char (2 bytes each).
        result += getAuthor().length() * 2; // UTF-16LE
        result += getComment().length() * 2;
        result += getRating().length() * 2;
        result += getTitle().length() * 2;
        result += getCopyRight().length() * 2;
        return result;
    }


    public String getRating() {
        return getValueFor(KEY_RATING);
    }


    public String getTitle() {
        return getValueFor(KEY_TITLE);
    }


    @Override
    public boolean isAddSupported(final MetadataDescriptor descriptor) {
        return ALLOWED.contains(descriptor.getName())
                && super.isAddSupported(descriptor);
    }


    @Override
    public String prettyPrint(final String prefix) {
        final StringBuilder result = new StringBuilder(super.prettyPrint(prefix));
        result.append(prefix).append("  |->Title      : ").append(getTitle())
                .append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |->Author     : ").append(getAuthor())
                .append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |->Copyright  : ").append(
                getCopyRight()).append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |->Description: ").append(getComment())
                .append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("  |->Rating     :").append(getRating())
                .append(Utils.LINE_SEPARATOR);
        return result.toString();
    }


    public void setAuthor(final String fileAuthor) throws IllegalArgumentException {
        setStringValue(KEY_AUTHOR, fileAuthor);
    }


    public void setComment(final String tagComment) throws IllegalArgumentException {
        setStringValue(KEY_DESCRIPTION, tagComment);
    }


    public void setCopyright(final String cpright) throws IllegalArgumentException {
        setStringValue(KEY_COPYRIGHT, cpright);
    }


    public void setRating(final String ratingText) throws IllegalArgumentException {
        setStringValue(KEY_RATING, ratingText);
    }


    public void setTitle(final String songTitle) throws IllegalArgumentException {
        setStringValue(KEY_TITLE, songTitle);
    }


    @Override
    public long writeInto(final OutputStream out) throws IOException {
        final long chunkSize = getCurrentAsfChunkSize();

        out.write(this.getGuid().getBytes());
        Utils.writeUINT64(getCurrentAsfChunkSize(), out);
        // write the sizes of the string representations plus 2 bytes zero term
        // character
        Utils.writeUINT16(getTitle().length() * 2 + 2, out);
        Utils.writeUINT16(getAuthor().length() * 2 + 2, out);
        Utils.writeUINT16(getCopyRight().length() * 2 + 2, out);
        Utils.writeUINT16(getComment().length() * 2 + 2, out);
        Utils.writeUINT16(getRating().length() * 2 + 2, out);
        // write the Strings
        out.write(Utils.getBytes(getTitle(), AsfHeader.ASF_CHARSET));
        out.write(AsfHeader.ZERO_TERM);
        out.write(Utils.getBytes(getAuthor(), AsfHeader.ASF_CHARSET));
        out.write(AsfHeader.ZERO_TERM);
        out.write(Utils.getBytes(getCopyRight(), AsfHeader.ASF_CHARSET));
        out.write(AsfHeader.ZERO_TERM);
        out.write(Utils.getBytes(getComment(), AsfHeader.ASF_CHARSET));
        out.write(AsfHeader.ZERO_TERM);
        out.write(Utils.getBytes(getRating(), AsfHeader.ASF_CHARSET));
        out.write(AsfHeader.ZERO_TERM);
        return chunkSize;
    }
}