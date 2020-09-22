
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.EventTimingTimestampTypes;

import java.nio.ByteBuffer;
import java.util.*;


public class FrameBodySYTC extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{
    public static final int MPEG_FRAMES = 1;
    public static final int MILLISECONDS = 2;


    public FrameBodySYTC()
    {
        setObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT, MILLISECONDS);
    }


    public FrameBodySYTC(final int timestampFormat, final byte[] tempo)
    {
        setObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT, timestampFormat);
        setObjectValue(DataTypes.OBJ_SYNCHRONISED_TEMPO_LIST, tempo);
    }


    public FrameBodySYTC(final ByteBuffer byteBuffer, final int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public FrameBodySYTC(final FrameBodySYTC body)
    {
        super(body);
    }


    public int getTimestampFormat()
    {
        return ((Number) getObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT)).intValue();
    }


    public void setTimestampFormat(final int timestampFormat)
    {
        if (EventTimingTimestampTypes.getInstanceOf().getValueForId(timestampFormat) == null)
        {
            throw new IllegalArgumentException("Timestamp format must be 1 or 2 (ID3v2.4, 4.7): " + timestampFormat);
        }
        setObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT, timestampFormat);
    }


    public Map<Long, Integer> getTempi()
    {
        final Map<Long, Integer> map = new LinkedHashMap<Long, Integer>();
        final List<SynchronisedTempoCode> codes = (List<SynchronisedTempoCode>)getObjectValue(DataTypes.OBJ_SYNCHRONISED_TEMPO_LIST);
        for (final SynchronisedTempoCode code : codes)
        {
            map.put(code.getTimestamp(), code.getTempo());
        }
        return Collections.unmodifiableMap(map);
    }


    public List<Long> getTimestamps()
    {
        final List<Long> list = new ArrayList<Long>();
        final List<SynchronisedTempoCode> codes = (List<SynchronisedTempoCode>)getObjectValue(DataTypes.OBJ_SYNCHRONISED_TEMPO_LIST);
        for (final SynchronisedTempoCode code : codes)
        {
            list.add(code.getTimestamp());
        }
        return Collections.unmodifiableList(list);
    }


    public void addTempo(final long timestamp, final int tempo)
    {
        // make sure we don'timer have two tempi at the same time
        removeTempo(timestamp);
        final List<SynchronisedTempoCode> codes = (List<SynchronisedTempoCode>)getObjectValue(DataTypes.OBJ_SYNCHRONISED_TEMPO_LIST);
        int insertIndex = 0;
        if (!codes.isEmpty() && codes.get(0).getTimestamp() <= timestamp)
        {
            for (final SynchronisedTempoCode code : codes)
            {
                final long translatedTimestamp = code.getTimestamp();
                if (timestamp < translatedTimestamp)
                {
                    break;
                }
                insertIndex++;
            }
        }
        codes.add(insertIndex, new SynchronisedTempoCode(DataTypes.OBJ_SYNCHRONISED_TEMPO, this, tempo, timestamp));
    }


    public boolean removeTempo(final long timestamp)
    {
        final List<SynchronisedTempoCode> codes = (List<SynchronisedTempoCode>)getObjectValue(DataTypes.OBJ_SYNCHRONISED_TEMPO_LIST);
        boolean removed = false;
        for (final ListIterator<SynchronisedTempoCode> iterator = codes.listIterator(); iterator.hasNext(); )
        {
            final SynchronisedTempoCode code = iterator.next();
            if (timestamp == code.getTimestamp())
            {
                iterator.remove();
                removed = true;
            }
            if (timestamp > code.getTimestamp())
            {
                break;
            }
        }
        return removed;
    }


    public void clearTempi()
    {
        ((List<EventTimingCode>)getObjectValue(DataTypes.OBJ_SYNCHRONISED_TEMPO_LIST)).clear();
    }

    @Override
    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_SYNC_TEMPO;
    }

    @Override
    public void read(final ByteBuffer byteBuffer) throws InvalidTagException
    {
        super.read(byteBuffer);

        // validate input
        final List<SynchronisedTempoCode> codes = (List<SynchronisedTempoCode>)getObjectValue(DataTypes.OBJ_SYNCHRONISED_TEMPO_LIST);
        long lastTimestamp = 0;
        for (final SynchronisedTempoCode code : codes)
        {
            if (code.getTimestamp() < lastTimestamp)
            {
                logger.warning("Synchronised tempo codes are not in chronological order. " + lastTimestamp + " is followed by " + code.getTimestamp() + ".");
                // throw exception???
            }
            lastTimestamp = code.getTimestamp();
        }
    }

    @Override
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TIME_STAMP_FORMAT, this, EventTimingTimestampTypes.TIMESTAMP_KEY_FIELD_SIZE));
        objectList.add(new SynchronisedTempoCodeList(this));
    }
}