package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;


public final class ContentBranding extends MetadataContainer {


    public final static Set<String> ALLOWED;


    public final static String KEY_BANNER_IMAGE = "BANNER_IMAGE";


    public final static String KEY_BANNER_TYPE = "BANNER_IMAGE_TYPE";


    public final static String KEY_BANNER_URL = "BANNER_IMAGE_URL";


    public final static String KEY_COPYRIGHT_URL = "COPYRIGHT_URL";

    static {
        ALLOWED = new HashSet<String>();
        ALLOWED.add(KEY_BANNER_IMAGE);
        ALLOWED.add(KEY_BANNER_TYPE);
        ALLOWED.add(KEY_BANNER_URL);
        ALLOWED.add(KEY_COPYRIGHT_URL);
    }


    public ContentBranding() {
        this(0, BigInteger.ZERO);
    }


    public ContentBranding(final long pos, final BigInteger size) {
        super(ContainerType.CONTENT_BRANDING, pos, size);
    }


    public String getBannerImageURL() {
        return getValueFor(KEY_BANNER_URL);
    }


    public String getCopyRightURL() {
        return getValueFor(KEY_COPYRIGHT_URL);
    }


    @Override
    public long getCurrentAsfChunkSize() {
        // GUID, size, image type, image data size, image url data size,
        // copyright data size
        long result = 40;
        result += assertDescriptor(KEY_BANNER_IMAGE,
                MetadataDescriptor.TYPE_BINARY).getRawDataSize();
        result += getBannerImageURL().length();
        result += getCopyRightURL().length();
        return result;
    }


    public byte[] getImageData() {
        return assertDescriptor(KEY_BANNER_IMAGE,
                MetadataDescriptor.TYPE_BINARY).getRawData();
    }


    public long getImageType() {
        if (!hasDescriptor(KEY_BANNER_TYPE)) {
            final MetadataDescriptor descriptor = new MetadataDescriptor(
                    ContainerType.CONTENT_BRANDING, KEY_BANNER_TYPE,
                    MetadataDescriptor.TYPE_DWORD);
            descriptor.setDWordValue(0);
            addDescriptor(descriptor);
        }
        return assertDescriptor(KEY_BANNER_TYPE).getNumber();
    }


    @Override
    public boolean isAddSupported(final MetadataDescriptor descriptor) {
        return ALLOWED.contains(descriptor.getName())
                && super.isAddSupported(descriptor);
    }


    public void setBannerImageURL(final String imageURL) {
        if (Utils.isBlank(imageURL)) {
            removeDescriptorsByName(KEY_BANNER_URL);
        } else {
            assertDescriptor(KEY_BANNER_URL).setStringValue(imageURL);
        }
    }


    public void setCopyRightURL(final String copyRight) {
        if (Utils.isBlank(copyRight)) {
            removeDescriptorsByName(KEY_COPYRIGHT_URL);
        } else {
            assertDescriptor(KEY_COPYRIGHT_URL).setStringValue(copyRight);
        }
    }


    public void setImage(final long imageType, final byte[] imageData) {
        assert imageType >= 0 && imageType <= 3;
        assert imageType > 0 || imageData.length == 0;
        assertDescriptor(KEY_BANNER_TYPE, MetadataDescriptor.TYPE_DWORD)
                .setDWordValue(imageType);
        assertDescriptor(KEY_BANNER_IMAGE, MetadataDescriptor.TYPE_BINARY)
                .setBinaryValue(imageData);
    }


    @Override
    public long writeInto(final OutputStream out) throws IOException {
        final long chunkSize = getCurrentAsfChunkSize();
        out.write(getGuid().getBytes());
        Utils.writeUINT64(chunkSize, out);
        Utils.writeUINT32(getImageType(), out);
        assert getImageType() >= 0 && getImageType() <= 3;
        final byte[] imageData = getImageData();
        assert getImageType() > 0 || imageData.length == 0;
        Utils.writeUINT32(imageData.length, out);
        out.write(imageData);
        Utils.writeUINT32(getBannerImageURL().length(), out);
        out.write(getBannerImageURL().getBytes("ASCII"));
        Utils.writeUINT32(getCopyRightURL().length(), out);
        out.write(getCopyRightURL().getBytes("ASCII"));
        return chunkSize;
    }

}
