
package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.TagOptionSingleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.logging.Logger;


public class MetadataDescriptor implements Comparable<MetadataDescriptor>,
        Cloneable {


    public static final long DWORD_MAXVALUE = new BigInteger("FFFFFFFF", 16)
            .longValue();


    private static final Logger LOGGER = Logger
            .getLogger("org.jaudiotagger.audio.asf.data");


    public static final int MAX_LANG_INDEX = 127;


    public static final int MAX_STREAM_NUMBER = 127;


    public static final BigInteger QWORD_MAXVALUE = new BigInteger(
            "FFFFFFFFFFFFFFFF", 16);


    public final static int TYPE_BINARY = 1;


    public final static int TYPE_BOOLEAN = 2;


    public final static int TYPE_DWORD = 3;


    public final static int TYPE_GUID = 6;


    public final static int TYPE_QWORD = 4;


    public final static int TYPE_STRING = 0;


    public final static int TYPE_WORD = 5;


    public static final int WORD_MAXVALUE = 65535;


    private final ContainerType containerType;



    private byte[] content = new byte[0];


    private int descriptorType;


    private int languageIndex = 0;


    private final String name;


    private int streamNumber = 0;


    public MetadataDescriptor(final ContainerType type, final String propName,
            final int propType) {
        this(type, propName, propType, 0, 0);
    }


    public MetadataDescriptor(final ContainerType type, final String propName,
            final int propType, final int stream, final int language) {
        assert type != null;
        type.assertConstraints(propName, new byte[0], propType, stream,
                language);
        this.containerType = type;
        this.name = propName;
        this.descriptorType = propType;
        this.streamNumber = stream;
        this.languageIndex = language;
    }


    public MetadataDescriptor(final String propName) {
        this(propName, TYPE_STRING);
    }


    public MetadataDescriptor(final String propName, final int propType) {
        this(ContainerType.METADATA_LIBRARY_OBJECT, propName, propType, 0, 0);
    }


    public BigInteger asNumber() {
        BigInteger result = null;
        switch (this.descriptorType) {
        case TYPE_BOOLEAN:
        case TYPE_WORD:
        case TYPE_DWORD:
        case TYPE_QWORD:
        case TYPE_BINARY:
            if (this.content.length > 8) {
                throw new NumberFormatException(
                        "Binary data would exceed QWORD");
            }
            break;
        case TYPE_GUID:
            throw new NumberFormatException(
                    "GUID cannot be converted to a number.");
        case TYPE_STRING:
            result = new BigInteger(getString(), 10);
            break;
        default:
            throw new IllegalStateException();
        }
        if (result == null) {
            final byte[] copy = new byte[this.content.length];
            for (int i = 0; i < copy.length; i++) {
                copy[i] = this.content[this.content.length - (i + 1)];
            }
            result = new BigInteger(1, copy);
        }
        return result;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    public int compareTo(final MetadataDescriptor other) {
        return getName().compareTo(other.getName());
    }


    public MetadataDescriptor createCopy() {
        final MetadataDescriptor result = new MetadataDescriptor(
                this.containerType, this.name, this.descriptorType,
                this.streamNumber, this.languageIndex);
        result.content = getRawData();
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (obj instanceof MetadataDescriptor) {
            if (obj == this) {
                result = true;
            } else {
                final MetadataDescriptor other = (MetadataDescriptor) obj;
                result = other.getName().equals(getName())
                        && other.descriptorType == this.descriptorType
                        && other.languageIndex == this.languageIndex
                        && other.streamNumber == this.streamNumber
                        && Arrays.equals(this.content, other.content);
            }
        }
        return result;
    }


    public boolean getBoolean() {
        return this.content.length > 0 && this.content[0] != 0;
    }


    @Deprecated
    public byte[] getBytes() {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            writeInto(result, this.containerType);
        } catch (final IOException e) {
            LOGGER.warning(e.getMessage());
        }
        return result.toByteArray();
    }


    public ContainerType getContainerType() {
        return this.containerType;
    }


    public int getCurrentAsfSize(final ContainerType type) {

        int result = 8;

        if (type != ContainerType.EXTENDED_CONTENT) {
            // Stream number and language index (respectively reserved field).
            // And +2 bytes, because data type is 32 bit, not 16
            result += 6;
        }
        result += getName().length() * 2;

        if (this.getType() == TYPE_BOOLEAN) {
            result += 2;
            if (type == ContainerType.EXTENDED_CONTENT) {
                // Extended content description boolean values are stored with
                // 32-bit
                result += 2;
            }
        } else {

            result += this.content.length;
            if (TYPE_STRING == this.getType()) {
                result += 2; // zero term of content string.
            }
        }
        return result;
    }


    public GUID getGuid() {
        GUID result = null;
        if (getType() == TYPE_GUID && this.content.length == GUID.GUID_LENGTH) {
            result = new GUID(this.content);
        }
        return result;
    }


    public int getLanguageIndex() {
        return this.languageIndex;
    }


    public String getName() {
        return this.name;
    }


    public long getNumber() {
        int bytesNeeded;
        switch (getType()) {
        case TYPE_BOOLEAN:
            bytesNeeded = 1;
            break;
        case TYPE_DWORD:
            bytesNeeded = 4;
            break;
        case TYPE_QWORD:
            bytesNeeded = 8;
            break;
        case TYPE_WORD:
            bytesNeeded = 2;
            break;
        default:
            throw new UnsupportedOperationException(
                    "The current type doesn'timer allow an interpretation as a number. ("
                            + getType() + ")");
        }
        if (bytesNeeded > this.content.length) {
            throw new IllegalStateException(
                    "The stored data cannot represent the type of current object.");
        }
        long result = 0;
        for (int i = 0; i < bytesNeeded; i++) {
            result |= (((long) this.content[i] & 0xFF) << (i * 8));
        }
        return result;
    }


    public byte[] getRawData() {
        final byte[] copy = new byte[this.content.length];
        System.arraycopy(this.content, 0, copy, 0, this.content.length);
        return copy;
    }


    public int getRawDataSize() {
        return this.content.length;
    }


    public int getStreamNumber() {
        return this.streamNumber;
    }


    public String getString() {
        String result = null;
        switch (getType()) {
        case TYPE_BINARY:
            result = "binary data";
            break;
        case TYPE_BOOLEAN:
            result = String.valueOf(getBoolean());
            break;
        case TYPE_GUID:
            result = getGuid() == null ? "Invalid GUID" : getGuid().toString();
            break;
        case TYPE_QWORD:
        case TYPE_DWORD:
        case TYPE_WORD:
            result = String.valueOf(getNumber());
            break;
        case TYPE_STRING:
            try {
                result = new String(this.content, "UTF-16LE");
            } catch (final UnsupportedEncodingException e) {
                LOGGER.warning(e.getMessage());
            }
            break;
        default:
            throw new IllegalStateException("Current type is not known.");
        }
        return result;
    }


    public int getType() {
        return this.descriptorType;
    }


    @Override
    public int hashCode() {
        return this.name.hashCode();
    }


    public boolean isEmpty() {
        return this.content.length == 0;
    }


    public void setBinaryValue(final byte[] data)
            throws IllegalArgumentException {
        this.containerType.assertConstraints(this.name, data,
                this.descriptorType, this.streamNumber, this.languageIndex);
        this.content = data.clone();
        this.descriptorType = TYPE_BINARY;
    }


    public void setBooleanValue(final boolean value) {
        this.content = new byte[] { value ? (byte) 1 : 0 };
        this.descriptorType = TYPE_BOOLEAN;
    }


    public void setDWordValue(final long value) {
        if (value < 0 || value > DWORD_MAXVALUE) {
            throw new IllegalArgumentException("value out of range (0-"
                    + DWORD_MAXVALUE + ")");
        }
        this.content = Utils.getBytes(value, 4);
        this.descriptorType = TYPE_DWORD;
    }


    public void setGUIDValue(final GUID value) {
        this.containerType.assertConstraints(this.name, value.getBytes(),
                TYPE_GUID, this.streamNumber, this.languageIndex);
        this.content = value.getBytes();
        this.descriptorType = TYPE_GUID;
    }


    public void setLanguageIndex(final int language) {
        this.containerType.assertConstraints(this.name, this.content,
                this.descriptorType, this.streamNumber, language);
        this.languageIndex = language;
    }


    public void setQWordValue(final BigInteger value)
            throws IllegalArgumentException {
        if (value == null) {
            throw new NumberFormatException("null");
        }
        if (BigInteger.ZERO.compareTo(value) > 0) {
            throw new IllegalArgumentException(
                    "Only unsigned values allowed (no negative)");
        }
        if (MetadataDescriptor.QWORD_MAXVALUE.compareTo(value) < 0) {
            throw new IllegalArgumentException(
                    "Value exceeds QWORD (64 bit unsigned)");
        }
        this.content = new byte[8];
        final byte[] valuesBytes = value.toByteArray();
        if (valuesBytes.length <= 8) {
            for (int i = valuesBytes.length - 1; i >= 0; i--) {
                this.content[valuesBytes.length - (i + 1)] = valuesBytes[i];
            }
        } else {

            Arrays.fill(this.content, (byte) 0xFF);
        }
        this.descriptorType = TYPE_QWORD;
    }


    public void setQWordValue(final long value) {
        if (value < 0) {
            throw new IllegalArgumentException("value out of range (0-"
                    + MetadataDescriptor.QWORD_MAXVALUE.toString() + ")");
        }
        this.content = Utils.getBytes(value, 8);
        this.descriptorType = TYPE_QWORD;
    }


    public void setStreamNumber(final int stream) {
        this.containerType.assertConstraints(this.name, this.content,
                this.descriptorType, stream, this.languageIndex);
        this.streamNumber = stream;
    }


    public void setString(final String value)
            throws IllegalArgumentException {
        try {
            switch (getType()) {
            case TYPE_BINARY:
                throw new IllegalArgumentException(
                        "Cannot interpret binary as string.");
            case TYPE_BOOLEAN:
                setBooleanValue(Boolean.parseBoolean(value));
                break;
            case TYPE_DWORD:
                setDWordValue(Long.parseLong(value));
                break;
            case TYPE_QWORD:
                setQWordValue(new BigInteger(value, 10));
                break;
            case TYPE_WORD:
                setWordValue(Integer.parseInt(value));
                break;
            case TYPE_GUID:
                setGUIDValue(GUID.parseGUID(value));
                break;
            case TYPE_STRING:
                setStringValue(value);
                break;
            default:
                // new Type added but not handled.
                throw new IllegalStateException();
            }
        } catch (final NumberFormatException nfe) {
            throw new IllegalArgumentException(
                    "Value cannot be parsed as Number or is out of range (\""
                            + value + "\")", nfe);
        }
    }


    // TODO Test
    public void setStringValue(final String value)
            throws IllegalArgumentException {
        if (value == null) {
            this.content = new byte[0];
        } else {
            final byte[] tmp = Utils.getBytes(value, AsfHeader.ASF_CHARSET);
            if (getContainerType().isWithinValueRange(tmp.length)) {
                // Everything is fine here, data can be stored.
                this.content = tmp;
            } else {
                // Normally a size violation, check if JAudiotagger my truncate
                // the string
                if (TagOptionSingleton.getInstance()
                        .isTruncateTextWithoutErrors()) {
                    // truncate the string
                    final int copyBytes = (int) getContainerType()
                            .getMaximumDataLength().longValue();
                    this.content = new byte[copyBytes % 2 == 0 ? copyBytes
                            : copyBytes - 1];
                    System.arraycopy(tmp, 0, this.content, 0,
                            this.content.length);
                } else {
                    // We may not truncate, so its an error
                    throw new IllegalArgumentException(
                            ErrorMessage.WMA_LENGTH_OF_DATA_IS_TOO_LARGE
                                    .getMsg(tmp.length, getContainerType()
                                            .getMaximumDataLength(),
                                            getContainerType()
                                                    .getContainerGUID()
                                                    .getDescription()));
                }
            }
        }
        this.descriptorType = TYPE_STRING;
    }


    public void setWordValue(final int value)
            throws IllegalArgumentException {
        if (value < 0 || value > WORD_MAXVALUE) {
            throw new IllegalArgumentException("value out of range (0-"
                    + WORD_MAXVALUE + ")");
        }
        this.content = Utils.getBytes(value, 2);
        this.descriptorType = TYPE_WORD;
    }


    @Override
    public String toString() {
        return getName()
                + " : "
                + new String[] { "String: ", "Binary: ", "Boolean: ",
                        "DWORD: ", "QWORD:", "WORD:", "GUID:" }[this.descriptorType]
                + getString() + " (language: " + this.languageIndex
                + " / stream: " + this.streamNumber + ")";
    }


    public int writeInto(final OutputStream out,
            final ContainerType contType) throws IOException {
        final int size = getCurrentAsfSize(contType);

        byte[] binaryData;
        if (this.descriptorType == TYPE_BOOLEAN) {
            binaryData = new byte[contType == ContainerType.EXTENDED_CONTENT ? 4
                    : 2];
            binaryData[0] = (byte) (getBoolean() ? 1 : 0);
        } else {
            binaryData = this.content;
        }
        // for Metadata objects the stream number and language index
        if (contType != ContainerType.EXTENDED_CONTENT) {
            Utils.writeUINT16(getLanguageIndex(), out);
            Utils.writeUINT16(getStreamNumber(), out);
        }
        Utils.writeUINT16(getName().length() * 2 + 2, out);

        // The name for the metadata objects come later
        if (contType == ContainerType.EXTENDED_CONTENT) {
            out.write(Utils.getBytes(getName(), AsfHeader.ASF_CHARSET));
            out.write(AsfHeader.ZERO_TERM);
        }

        // type and content len follow up are identical
        final int type = getType();
        Utils.writeUINT16(type, out);
        int contentLen = binaryData.length;
        if (TYPE_STRING == type) {
            contentLen += 2; // Zero Term
        }

        if (contType == ContainerType.EXTENDED_CONTENT) {
            Utils.writeUINT16(contentLen, out);
        } else {
            Utils.writeUINT32(contentLen, out);
        }

        // Metadata objects now write their descriptor name
        if (contType != ContainerType.EXTENDED_CONTENT) {
            out.write(Utils.getBytes(getName(), AsfHeader.ASF_CHARSET));
            out.write(AsfHeader.ZERO_TERM);
        }

        // The content.
        out.write(binaryData);
        if (TYPE_STRING == type) {
            out.write(AsfHeader.ZERO_TERM);
        }
        return size;
    }
}
