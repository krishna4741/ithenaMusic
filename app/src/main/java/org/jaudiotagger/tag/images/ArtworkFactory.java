package org.jaudiotagger.tag.images;

import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;

import java.io.File;
import java.io.IOException;


public class ArtworkFactory
{


    public static Artwork getNew()
    {
        return new AndroidArtwork();
    }


    public static Artwork createArtworkFromMetadataBlockDataPicture(MetadataBlockDataPicture coverArt)
    {
        return AndroidArtwork.createArtworkFromMetadataBlockDataPicture(coverArt);
    }


    public static Artwork createArtworkFromFile(File file) throws IOException
    {
        return AndroidArtwork.createArtworkFromFile(file);
    }


    public static Artwork createLinkedArtworkFromURL(String link) throws IOException
    {
        return AndroidArtwork.createLinkedArtworkFromURL(link);
    }
}
