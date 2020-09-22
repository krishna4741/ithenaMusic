
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractDataTypeList<T extends AbstractDataType> extends AbstractDataType
{

    public AbstractDataTypeList(final String identifier, final AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
        setValue(new ArrayList<T>());
    }


    protected AbstractDataTypeList(final AbstractDataTypeList<T> copy)
    {
        super(copy);
    }

    public List<T> getValue()
    {
        return (List<T>)super.getValue();
    }

    public void setValue(final List<T> list)
    {
        super.setValue(list == null ? new ArrayList<T>() : new ArrayList<T>(list));
    }


    public int getSize()
    {
        int size = 0;
        for (final T t : getValue()) {
            size+=t.getSize();
        }
        return size;
    }


    public void readByteArray(final byte[] buffer, final int offset) throws InvalidDataTypeException
    {
        if (buffer == null)
        {
            throw new NullPointerException("Byte array is null");
        }

        if (offset < 0)
        {
            throw new IndexOutOfBoundsException("Offset to byte array is out of bounds: offset = " + offset + ", array.length = " + buffer.length);
        }

        // no events
        if (offset >= buffer.length)
        {
            getValue().clear();
            return;
        }
        for (int currentOffset = offset; currentOffset<buffer.length;) {
            final T data = createListElement();
            data.readByteArray(buffer, currentOffset);
            data.setBody(frameBody);
            getValue().add(data);
            currentOffset+=data.getSize();
        }
    }


    protected abstract T createListElement();


    public byte[] writeByteArray()
    {
        logger.config("Writing DataTypeList " + this.getIdentifier());
        final byte[] buffer = new byte[getSize()];
        int offset = 0;
        for (final AbstractDataType data : getValue()) {
            final byte[] bytes = data.writeByteArray();
            System.arraycopy(bytes, 0, buffer, offset, bytes.length);
            offset+=bytes.length;
        }

        return buffer;
    }

    @Override
    public int hashCode() {
        return getValue() != null ? getValue().hashCode() : 0;
    }

    @Override
    public String toString() {
        return getValue() != null ? getValue().toString() : "{}";

    }
}
