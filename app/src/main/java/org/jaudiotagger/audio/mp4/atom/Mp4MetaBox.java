package org.jaudiotagger.audio.mp4.atom;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.mp4.Mp4AtomIdentifier;
import org.jaudiotagger.logging.ErrorMessage;

import java.nio.ByteBuffer;


public class Mp4MetaBox extends AbstractMp4Box
{
    public static final int FLAGS_LENGTH = 4;


    public Mp4MetaBox(Mp4BoxHeader header, ByteBuffer dataBuffer)
    {
        this.header = header;
        this.dataBuffer = dataBuffer;
    }

    public void processData() throws CannotReadException
    {
        //4-skip the meta flags and check they are the meta flags
        byte[] b = new byte[FLAGS_LENGTH];
        dataBuffer.get(b);
        if (b[0] != 0)
        {
            throw new CannotReadException(ErrorMessage.MP4_FILE_META_ATOM_CHILD_DATA_NOT_NULL.getMsg());
        }
    }


    public static Mp4MetaBox createiTunesStyleMetaBox(int childrenSize)
    {
        Mp4BoxHeader metaHeader = new Mp4BoxHeader(Mp4AtomIdentifier.META.getFieldName());
        metaHeader.setLength(Mp4BoxHeader.HEADER_LENGTH + Mp4MetaBox.FLAGS_LENGTH + childrenSize);
        ByteBuffer metaData = ByteBuffer.allocate(Mp4MetaBox.FLAGS_LENGTH);     
        Mp4MetaBox metaBox = new Mp4MetaBox(metaHeader,metaData);
        return metaBox;
    }
}
