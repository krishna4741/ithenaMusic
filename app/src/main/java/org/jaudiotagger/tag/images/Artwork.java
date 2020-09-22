package org.jaudiotagger.tag.images;

import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;

import java.io.File;
import java.io.IOException;


public interface Artwork
{
    public byte[] getBinaryData();


    public void setBinaryData(byte[] binaryData);

    public String getMimeType();

    public void setMimeType(String mimeType);

    public String getDescription();

    public int getHeight();

    public int getWidth();

    public void setDescription(String description);


    public boolean setImageFromData();

    public Object getImage() throws IOException;

    public boolean isLinked();

    public void setLinked(boolean linked);

    public String getImageUrl();

    public void setImageUrl(String imageUrl);

    public int getPictureType();

    public void setPictureType(int pictureType);


    public void setFromFile(File file)  throws IOException;


    public void setFromMetadataBlockDataPicture(MetadataBlockDataPicture coverArt);


    public void setWidth(int width);

    public void setHeight(int height);
}
