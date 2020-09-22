
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.EventTimingTimestampTypes;

import java.nio.ByteBuffer;
import java.util.*;


public class FrameBodyETCO extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    public static final int MPEG_FRAMES = 1;
    public static final int MILLISECONDS = 2;


    public FrameBodyETCO()
    {
        setObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT, MILLISECONDS);
    }

    public FrameBodyETCO(final FrameBodyETCO body)
    {
        super(body);
    }


    public FrameBodyETCO(final ByteBuffer byteBuffer, final int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public int getTimestampFormat()
    {
        return ((Number) getObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT)).intValue();
    }


    public void setTimestampFormat(final int timestampFormat)
    {
        if (EventTimingTimestampTypes.getInstanceOf().getValueForId(timestampFormat) == null)
        {
            throw new IllegalArgumentException("Timestamp format must be 1 or 2 (ID3v2.4, 4.5): " + timestampFormat);
        }
        setObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT, timestampFormat);
    }


    public Map<Long, int[]> getTimingCodes()
    {
        final Map<Long, int[]> map = new LinkedHashMap<Long, int[]>();
        final List<EventTimingCode> codes = (List<EventTimingCode>)getObjectValue(DataTypes.OBJ_TIMED_EVENT_LIST);
        long lastTimestamp = 0;
        for (final EventTimingCode code : codes)
        {
            final long translatedTimestamp = code.getTimestamp() == 0 ? lastTimestamp : code.getTimestamp();
            final int[] types = map.get(translatedTimestamp);
            if (types == null) {
                map.put(translatedTimestamp, new int[]{code.getType()});
            } else {
                final int[] newTypes = new int[types.length + 1];
                System.arraycopy(types, 0, newTypes, 0, types.length);
                newTypes[newTypes.length-1] = code.getType();
                map.put(translatedTimestamp, newTypes);
            }
            lastTimestamp = translatedTimestamp;
        }
        return Collections.unmodifiableMap(map);
    }


    public List<Long> getTimestamps(final int... type)
    {
        final Set<Integer> typeSet = toSet(type);
        final List<Long> list = new ArrayList<Long>();
        final List<EventTimingCode> codes = (List<EventTimingCode>)getObjectValue(DataTypes.OBJ_TIMED_EVENT_LIST);
        long lastTimestamp = 0;
        for (final EventTimingCode code : codes)
        {
            final long translatedTimestamp = code.getTimestamp() == 0 ? lastTimestamp : code.getTimestamp();
            if (typeSet.contains(code.getType()))
            {
                list.add(translatedTimestamp);
            }
            lastTimestamp = translatedTimestamp;
        }
        return Collections.unmodifiableList(list);
    }


    public void addTimingCode(final long timestamp, final int... types)
    {
        final List<EventTimingCode> codes = (List<EventTimingCode>)getObjectValue(DataTypes.OBJ_TIMED_EVENT_LIST);
        long lastTimestamp = 0;
        int insertIndex = 0;
        if (!codes.isEmpty() && codes.get(0).getTimestamp() <= timestamp)
        {
            for (final EventTimingCode code : codes)
            {
                final long translatedTimestamp = code.getTimestamp() == 0 ? lastTimestamp : code.getTimestamp();
                if (timestamp < translatedTimestamp)
                {
                    break;
                }
                insertIndex++;
                lastTimestamp = translatedTimestamp;
            }
        }
        for (final int type : types) {
            codes.add(insertIndex, new EventTimingCode(DataTypes.OBJ_TIMED_EVENT, this, type, timestamp));
            insertIndex++; // preserve order of types
        }
    }


    public boolean removeTimingCode(final long timestamp, final int... types)
    {
        // before we can remove anything, we have to resolve relative 0-timestamps
        // otherwise we might remove the anchor a relative timestamp relies on
        resolveRelativeTimestamps();
        final Set<Integer> typeSet = toSet(types);
        final List<EventTimingCode> codes = (List<EventTimingCode>)getObjectValue(DataTypes.OBJ_TIMED_EVENT_LIST);
        boolean removed = false;
        for (final ListIterator<EventTimingCode> iterator = codes.listIterator(); iterator.hasNext(); )
        {
            final EventTimingCode code = iterator.next();
            if (timestamp == code.getTimestamp() && typeSet.contains(code.getType()))
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


    public void clearTimingCodes()
    {
        ((List<EventTimingCode>)getObjectValue(DataTypes.OBJ_TIMED_EVENT_LIST)).clear();
    }


    private void resolveRelativeTimestamps()
    {
        final List<EventTimingCode> codes = (List<EventTimingCode>)getObjectValue(DataTypes.OBJ_TIMED_EVENT_LIST);
        long lastTimestamp = 0;
        for (final EventTimingCode code : codes)
        {
            final long translatedTimestamp = code.getTimestamp() == 0 ? lastTimestamp : code.getTimestamp();
            code.setTimestamp(translatedTimestamp);
            lastTimestamp = translatedTimestamp;
        }
    }

    @Override
    public void read(final ByteBuffer byteBuffer) throws InvalidTagException
    {
        super.read(byteBuffer);

        // validate input
        final List<EventTimingCode> codes = (List<EventTimingCode>)getObjectValue(DataTypes.OBJ_TIMED_EVENT_LIST);
        long lastTimestamp = 0;
        for (final EventTimingCode code : codes)
        {
            final long translatedTimestamp = code.getTimestamp() == 0 ? lastTimestamp : code.getTimestamp();
            if (code.getTimestamp() < lastTimestamp)
            {
                logger.warning("Event codes are not in chronological order. " + lastTimestamp + " is followed by " + code.getTimestamp() + ".");
                // throw exception???
            }
            lastTimestamp = translatedTimestamp;
        }
    }


    @Override
    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_EVENT_TIMING_CODES;
    }


    @Override
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TIME_STAMP_FORMAT, this, EventTimingTimestampTypes.TIMESTAMP_KEY_FIELD_SIZE));
        objectList.add(new EventTimingCodeList(this));
    }

    private static Set<Integer> toSet(final int... types)
    {
        final Set<Integer> typeSet = new HashSet<Integer>();
        for (final int type : types)
        {
            typeSet.add(type);
        }
        return typeSet;
    }


}
