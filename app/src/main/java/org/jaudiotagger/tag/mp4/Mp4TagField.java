
package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.mp4.atom.Mp4DataBox;
import org.jaudiotagger.tag.mp4.field.Mp4FieldType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;


public abstract class Mp4TagField implements TagField
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.tag.mp4");


    protected String id;

    //Just used by reverese dns class, so it knows the size of its aprent so it can detect end correctly
    protected Mp4BoxHeader parentHeader;

    protected Mp4TagField(String id)
    {
        this.id = id;
    }


    protected Mp4TagField(ByteBuffer data) throws UnsupportedEncodingException
    {
        build(data);
    }


    protected Mp4TagField(Mp4BoxHeader parentHeader, ByteBuffer data) throws UnsupportedEncodingException
    {
        this.parentHeader = parentHeader;
        build(data);
    }

    protected Mp4TagField(String id, ByteBuffer data) throws UnsupportedEncodingException
    {
        this(id);
        build(data);
    }


    public String getId()
    {
        return id;
    }

    public void isBinary(boolean b)
    {

    }

    public boolean isCommon()
    {
        return id.equals(Mp4FieldKey.ARTIST.getFieldName()) || id.equals(Mp4FieldKey.ALBUM.getFieldName()) || id.equals(Mp4FieldKey.TITLE.getFieldName()) || id.equals(Mp4FieldKey.TRACK.getFieldName()) || id.equals(Mp4FieldKey.DAY.getFieldName()) || id.equals(Mp4FieldKey.COMMENT.getFieldName()) || id.equals(Mp4FieldKey.GENRE.getFieldName());
    }


    protected byte[] getIdBytes()
    {
        return Utils.getDefaultBytes(getId(), "ISO-8859-1");
    }


    protected abstract byte[] getDataBytes() throws UnsupportedEncodingException;



    public abstract Mp4FieldType getFieldType();


    protected abstract void build(ByteBuffer data) throws UnsupportedEncodingException;


    public byte[] getRawContent() throws UnsupportedEncodingException
    {
        logger.fine("Getting Raw data for:" + getId());
        try
        {
            //Create Data Box
            byte[] databox = getRawContentDataOnly();

            //Wrap in Parent box
            ByteArrayOutputStream outerbaos = new ByteArrayOutputStream();
            outerbaos.write(Utils.getSizeBEInt32(Mp4BoxHeader.HEADER_LENGTH + databox.length));
            outerbaos.write(Utils.getDefaultBytes(getId(), "ISO-8859-1"));
            outerbaos.write(databox);
            return outerbaos.toByteArray();
        }
        catch (IOException ioe)
        {
            //This should never happen as were not actually writing to/from a file
            throw new RuntimeException(ioe);
        }
    }


    public byte[] getRawContentDataOnly() throws UnsupportedEncodingException
    {
        logger.fine("Getting Raw data for:" + getId());
        try
        {
            //Create Data Box
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = getDataBytes();
            baos.write(Utils.getSizeBEInt32(Mp4DataBox.DATA_HEADER_LENGTH + data.length));
            baos.write(Utils.getDefaultBytes(Mp4DataBox.IDENTIFIER, "ISO-8859-1"));
            baos.write(new byte[]{0});
            baos.write(new byte[]{0, 0, (byte) getFieldType().getFileClassId()});
            baos.write(new byte[]{0, 0, 0, 0});
            baos.write(data);
            return baos.toByteArray();
        }
        catch (IOException ioe)
        {
            //This should never happen as were not actually writing to/from a file
            throw new RuntimeException(ioe);
        }
    }
}
