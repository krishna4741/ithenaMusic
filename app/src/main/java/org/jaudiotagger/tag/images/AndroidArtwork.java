package org.jaudiotagger.tag.images;

import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.reference.PictureTypes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class AndroidArtwork implements Artwork
{
    private byte[]          binaryData;
    private String          mimeType="";
    private String          description="";
    private boolean         isLinked=false;
    private String          imageUrl="";
    private int             pictureType=-1;
    private int             width;
    private int             height;

    public AndroidArtwork()
    {

    }
    public byte[] getBinaryData()
    {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData)
    {
        this.binaryData = binaryData;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }

    public String getDescription()
    {
        return description;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }


    public boolean setImageFromData()
    {
        throw new UnsupportedOperationException();
    }

    public Object getImage() throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public boolean isLinked()
    {
        return isLinked;
    }

    public void setLinked(boolean linked)
    {
        isLinked = linked;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public int getPictureType()
    {
        return pictureType;
    }

    public void setPictureType(int pictureType)
    {
        this.pictureType = pictureType;
    }


    public void setFromFile(File file)  throws IOException
    {
        RandomAccessFile imageFile = new RandomAccessFile(file, "r");
        byte[] imagedata = new byte[(int) imageFile.length()];
        imageFile.read(imagedata);
        imageFile.close();

        setBinaryData(imagedata);
        setMimeType(ImageFormats.getMimeTypeForBinarySignature(imagedata));
        setDescription("");
        setPictureType(PictureTypes.DEFAULT_ID);
    }


    public static AndroidArtwork createArtworkFromFile(File file)  throws IOException
    {
        AndroidArtwork artwork = new AndroidArtwork();
        artwork.setFromFile(file);
        return artwork;
    }


    public static AndroidArtwork createLinkedArtworkFromURL(String url)  throws IOException
    {
        AndroidArtwork artwork = new AndroidArtwork();
        artwork.setLinkedFromURL(url);
        return artwork;
    }


      public void setLinkedFromURL(String url)  throws IOException
      {
          setLinked(true);
          setImageUrl(url);
      }



    public void setFromMetadataBlockDataPicture(MetadataBlockDataPicture coverArt)
    {
        setMimeType(coverArt.getMimeType());
        setDescription(coverArt.getDescription());
        setPictureType(coverArt.getPictureType());       
        if(coverArt.isImageUrl())
        {
            setLinked(coverArt.isImageUrl());
            setImageUrl(coverArt.getImageUrl());
        }
        else
        {
            setBinaryData(coverArt.getImageData());
        }
        setWidth(coverArt.getWidth());
        setHeight(coverArt.getHeight());
    }


    public static AndroidArtwork createArtworkFromMetadataBlockDataPicture(MetadataBlockDataPicture coverArt)
    {
        AndroidArtwork artwork = new AndroidArtwork();
        artwork.setFromMetadataBlockDataPicture(coverArt);
        return artwork;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }
}
