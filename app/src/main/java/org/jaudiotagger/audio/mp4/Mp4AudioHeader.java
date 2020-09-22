package org.jaudiotagger.audio.mp4;

import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.mp4.atom.Mp4EsdsBox;


public class Mp4AudioHeader extends GenericAudioHeader
{

    public final static String FIELD_KIND = "KIND";


    public final static String FIELD_PROFILE = "PROFILE";



    public final static String FIELD_BRAND = "BRAND";

    public void setKind(Mp4EsdsBox.Kind kind)
    {
        content.put(FIELD_KIND, kind);
    }


    public Mp4EsdsBox.Kind getKind()
    {
        return (Mp4EsdsBox.Kind) content.get(FIELD_KIND);
    }


    public void setProfile(Mp4EsdsBox.AudioProfile profile)
    {
        content.put(FIELD_PROFILE, profile);
    }


    public Mp4EsdsBox.AudioProfile getProfile()
    {
        return (Mp4EsdsBox.AudioProfile) content.get(FIELD_PROFILE);
    }


    public void setBrand(String brand)
    {
        content.put(FIELD_BRAND, brand);
    }



    public String getBrand()
    {
        return (String) content.get(FIELD_BRAND);
    }


}
