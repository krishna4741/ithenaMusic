package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;
import org.jaudiotagger.logging.ErrorMessage;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;


public enum ContainerType
{


    CONTENT_BRANDING(GUID.GUID_CONTENT_BRANDING, 32, false, false, false, false),


    CONTENT_DESCRIPTION(GUID.GUID_CONTENTDESCRIPTION, 16, false, false, false,
            false),

    EXTENDED_CONTENT(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, 16, false, false,
            false, false),

    METADATA_LIBRARY_OBJECT(GUID.GUID_METADATA_LIBRARY, 32, true, true, true,
            true),

    METADATA_OBJECT(GUID.GUID_METADATA, 16, false, true, false, true);


    public static boolean areInCorrectOrder(final ContainerType low,
            final ContainerType high) {
        final List<ContainerType> asList = Arrays.asList(getOrdered());
        return asList.indexOf(low) <= asList.indexOf(high);
    }


    public static ContainerType[] getOrdered() {
        return new ContainerType[] {CONTENT_DESCRIPTION, CONTENT_BRANDING, EXTENDED_CONTENT, METADATA_OBJECT, METADATA_LIBRARY_OBJECT};
    }


    private final GUID containerGUID;


    private final boolean guidEnabled;


    private final boolean languageEnabled;


    private final BigInteger maximumDataLength;


    private final boolean multiValued;


    private final long perfMaxDataLen;


    private final boolean streamEnabled;


    private ContainerType(final GUID guid, final int maxDataLenBits,
            final boolean guidAllowed, final boolean stream,
            final boolean language, final boolean multiValue) {
        this.containerGUID = guid;
        this.maximumDataLength = BigInteger.valueOf(2).pow(maxDataLenBits)
                .subtract(BigInteger.ONE);
        if (this.maximumDataLength
                .compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0) {
            this.perfMaxDataLen = this.maximumDataLength.longValue();
        } else {
            this.perfMaxDataLen = -1;
        }
        this.guidEnabled = guidAllowed;
        this.streamEnabled = stream;
        this.languageEnabled = language;
        this.multiValued = multiValue;
    }


    public void assertConstraints(final String name, final byte[] data,
            final int type, final int stream, final int language) {
        final RuntimeException result = checkConstraints(name, data, type,
                stream, language);
        if (result != null) {
            throw result;
        }
    }


    public RuntimeException checkConstraints(final String name,
            final byte[] data, final int type, final int stream,
            final int language) {
        RuntimeException result = null;
        // TODO generate tests
        if (name == null || data == null) {
            result = new IllegalArgumentException("Arguments must not be null.");
        } else {
            if (!Utils.isStringLengthValidNullSafe(name)) {
                result = new IllegalArgumentException(
                        ErrorMessage.WMA_LENGTH_OF_STRING_IS_TOO_LARGE
                                .getMsg(name.length()));
            }
        }
        if (result == null && !isWithinValueRange(data.length)) {
            result = new IllegalArgumentException(
                    ErrorMessage.WMA_LENGTH_OF_DATA_IS_TOO_LARGE.getMsg(
                            data.length, getMaximumDataLength(),
                            getContainerGUID().getDescription()));
        }
        if (result == null
                && (stream < 0 || stream > MetadataDescriptor.MAX_STREAM_NUMBER || (!isStreamNumberEnabled() && stream != 0))) {
            final String streamAllowed = isStreamNumberEnabled() ? "0 to 127"
                    : "0";
            result = new IllegalArgumentException(
                    ErrorMessage.WMA_INVALID_STREAM_REFERNCE.getMsg(stream,
                            streamAllowed, getContainerGUID().getDescription()));
        }
        if (result == null && type == MetadataDescriptor.TYPE_GUID
                && !isGuidEnabled()) {
            result = new IllegalArgumentException(
                    ErrorMessage.WMA_INVALID_GUID_USE.getMsg(getContainerGUID()
                            .getDescription()));
        }
        if (result == null
                && ((language != 0 && !isLanguageEnabled()) || (language < 0 || language >= MetadataDescriptor.MAX_LANG_INDEX))) {
            final String langAllowed = isStreamNumberEnabled() ? "0 to 126"
                    : "0";
            result = new IllegalArgumentException(
                    ErrorMessage.WMA_INVALID_LANGUAGE_USE.getMsg(language,
                            getContainerGUID().getDescription(), langAllowed));
        }
        if (result == null && this == CONTENT_DESCRIPTION
                && type != MetadataDescriptor.TYPE_STRING) {
            result = new IllegalArgumentException(
                    ErrorMessage.WMA_ONLY_STRING_IN_CD.getMsg());
        }
        return result;
    }


    public GUID getContainerGUID() {
        return this.containerGUID;
    }


    public BigInteger getMaximumDataLength() {
        return this.maximumDataLength;
    }


    public boolean isGuidEnabled() {
        return this.guidEnabled;
    }


    public boolean isLanguageEnabled() {
        return this.languageEnabled;
    }


    public boolean isWithinValueRange(final long value) {
        return (this.perfMaxDataLen == -1 || this.perfMaxDataLen >= value)
                && value >= 0;
    }


    public boolean isMultiValued() {
        return this.multiValued;
    }


    public boolean isStreamNumberEnabled() {
        return this.streamEnabled;
    }
}