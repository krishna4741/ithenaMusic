
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.datatype.NumberVariableLength;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyASPI extends AbstractID3v2FrameBody implements ID3v24FrameBody
{
    private static final int DATA_START_FIELD_SIZE = 4;
    private static final int DATA_LENGTH_FIELD_SIZE = 4;
    private static final int NO_OF_INDEX_POINTS_FIELD_SIZE = 2;
    private static final int BITS_PER_INDEX_POINTS_FIELD_SIZE = 1;
    private static final int FRACTION_AT_INDEX_MINIMUM_FIELD_SIZE = 1;
    private static final String INDEXED_DATA_START = "IndexedDataStart";
    private static final String INDEXED_DATA_LENGTH = "IndexedDataLength";
    private static final String NUMBER_OF_INDEX_POINTS = "NumberOfIndexPoints";
    private static final String BITS_PER_INDEX_POINT = "BitsPerIndexPoint";
    private static final String FRACTION_AT_INDEX = "FractionAtIndex";


    public FrameBodyASPI()
    {
    }


    public FrameBodyASPI(FrameBodyASPI copyObject)
    {
        super(copyObject);
    }


    public FrameBodyASPI(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_AUDIO_SEEK_POINT_INDEX;
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberFixedLength(INDEXED_DATA_START, this, DATA_START_FIELD_SIZE));
        objectList.add(new NumberFixedLength(INDEXED_DATA_LENGTH, this, DATA_LENGTH_FIELD_SIZE));
        objectList.add(new NumberFixedLength(NUMBER_OF_INDEX_POINTS, this, NO_OF_INDEX_POINTS_FIELD_SIZE));
        objectList.add(new NumberFixedLength(BITS_PER_INDEX_POINT, this, BITS_PER_INDEX_POINTS_FIELD_SIZE));
        objectList.add(new NumberVariableLength(FRACTION_AT_INDEX, this, FRACTION_AT_INDEX_MINIMUM_FIELD_SIZE));
    }
}
