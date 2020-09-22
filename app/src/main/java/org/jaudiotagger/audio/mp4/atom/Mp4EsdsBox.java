package org.jaudiotagger.audio.mp4.atom;

import org.jaudiotagger.audio.generic.Utils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


public class Mp4EsdsBox extends AbstractMp4Box
{
    public static final int VERSION_FLAG_LENGTH = 1;
    public static final int OTHER_FLAG_LENGTH = 3;
    public static final int DESCRIPTOR_TYPE_LENGTH = 1;
    public static final int ES_ID_LENGTH = 2;
    public static final int STREAM_PRIORITY_LENGTH = 1;
    public static final int CONFIG_TYPE_LENGTH = 1;
    public static final int OBJECT_TYPE_LENGTH = 1;
    public static final int STREAM_TYPE_LENGTH = 1;
    public static final int BUFFER_SIZE_LENGTH = 3;
    public static final int MAX_BITRATE_LENGTH = 4;
    public static final int AVERAGE_BITRATE_LENGTH = 4;
    public static final int DESCRIPTOR_OBJECT_TYPE_LENGTH = 1;
    public static final int CHANNEL_FLAGS_LENGTH = 1;

    //Data we are tring to extract
    private Kind kind;
    private AudioProfile audioProfile;
    private int numberOfChannels;
    private int maxBitrate;
    private int avgBitrate;

    //Section indentifiers
    private static final int SECTION_THREE = 0x03;
    private static final int SECTION_FOUR = 0x04;
    private static final int SECTION_FIVE = 0x05;
    private static final int SECTION_SIX = 0x06;

    //Possible Section Filler values
    private static final int FILLER_START = 0x80;
    private static final int FILLER_OTHER = 0x81;
    private static final int FILLER_END = 0xFE;

    private static Map<Integer, Kind> kindMap;
    private static Map<Integer, AudioProfile> audioProfileMap;


    static
    {
        //Create maps to speed up lookup from raw value to enum
        kindMap = new HashMap<Integer, Kind>();
        for (Kind next : Kind.values())
        {
            kindMap.put(next.getId(), next);
        }

        audioProfileMap = new HashMap<Integer, AudioProfile>();
        for (AudioProfile next : AudioProfile.values())
        {
            audioProfileMap.put(next.getId(), next);
        }
    }


    public Mp4EsdsBox(Mp4BoxHeader header, ByteBuffer dataBuffer)
    {
        this.header = header;

        //Not currently used, as lengths can extend over more than one section i think
        int sectionThreeLength;
        int sectionFourLength;
        int sectionFiveLength;
        int sectionSixLength;

        //As explained earlier the length of this atom is not fixed so processing is a bit more difficult
        //Process Flags
        dataBuffer.position(dataBuffer.position() + VERSION_FLAG_LENGTH + OTHER_FLAG_LENGTH);

        //Process Section 3 if exists
        if (dataBuffer.get() == SECTION_THREE)
        {
            sectionThreeLength = processSectionHeader(dataBuffer);
            //Skip Other Section 3 data
            dataBuffer.position(dataBuffer.position() + ES_ID_LENGTH + STREAM_PRIORITY_LENGTH);
        }

        //Process Section 4 (to getFields type and bitrate)
        if (dataBuffer.get() == SECTION_FOUR)
        {
            sectionFourLength = processSectionHeader(dataBuffer);

            //kind (in iTunes)
            kind = kindMap.get((int) dataBuffer.get());

            //Skip Other Section 4 data
            dataBuffer.position(dataBuffer.position() + STREAM_TYPE_LENGTH + BUFFER_SIZE_LENGTH);

            //Bit rates
            this.maxBitrate = Utils.getIntBE(dataBuffer, dataBuffer.position(), (dataBuffer.position() + MAX_BITRATE_LENGTH - 1));
            dataBuffer.position(dataBuffer.position() + MAX_BITRATE_LENGTH);

            this.avgBitrate = Utils.getIntBE(dataBuffer, dataBuffer.position(), (dataBuffer.position() + AVERAGE_BITRATE_LENGTH - 1));
            dataBuffer.position(dataBuffer.position() + AVERAGE_BITRATE_LENGTH);


        }
        //Process Section 5,(to getFields no of channels and audioprofile(profile in itunes))
        if (dataBuffer.get() == SECTION_FIVE)
        {
            sectionFiveLength = processSectionHeader(dataBuffer);

            //Audio Profile
            audioProfile = audioProfileMap.get((dataBuffer.get() >> 3));

            //Channels
            byte channelByte = dataBuffer.get();
            numberOfChannels = (channelByte << 1) >> 4;
        }

        //Process Section 6, not needed ...


    }

    public int getNumberOfChannels()
    {
        return numberOfChannels;
    }


    public int getMaxBitrate()
    {
        return maxBitrate;
    }


    public int getAvgBitrate()
    {
        return avgBitrate;
    }


    public int processSectionHeader(ByteBuffer dataBuffer)
    {
        int datalength;
        byte nextByte = dataBuffer.get();
        if (((nextByte & 0xFF) == FILLER_START) || ((nextByte & 0xFF) == FILLER_OTHER) || ((nextByte & 0xFF) == FILLER_END))
        {
            dataBuffer.get();
            dataBuffer.get();
            datalength = dataBuffer.get();
        }
        else
        {
            datalength = nextByte;
        }
        return datalength;
    }


    public Kind getKind()
    {
        return kind;
    }


    public AudioProfile getAudioProfile()
    {
        return audioProfile;
    }


    public static enum Kind
    {
        V1(1),
        V2(2),
        MPEG4_VIDEO(32),
        MPEG4_AVC_SPS(33),
        MPEG4_AVC_PPS(34),
        MPEG4_AUDIO(64),
        MPEG2_SIMPLE_VIDEO(96),
        MPEG2_MAIN_VIDEO(97),
        MPEG2_SNR_VIDEO(98),
        MPEG2_SPATIAL_VIDEO(99),
        MPEG2_HIGH_VIDEO(100),
        MPEG2_422_VIDEO(101),
        MPEG4_ADTS_MAIN(102),
        MPEG4_ADTS_LOW_COMPLEXITY(103),
        MPEG4_ADTS_SCALEABLE_SAMPLING(104),
        MPEG2_ADTS_MAIN(105),
        MPEG1_VIDEO(106),
        MPEG1_ADTS(107),
        JPEG_VIDEO(108),
        PRIVATE_AUDIO(192),
        PRIVATE_VIDEO(208),
        PCM_LITTLE_ENDIAN_AUDIO(224),
        VORBIS_AUDIO(225),
        DOLBY_V3_AUDIO(226),
        ALAW_AUDIO(227),
        MULAW_AUDIO(228),
        ADPCM_AUDIO(229),
        PCM_BIG_ENDIAN_AUDIO(230),
        YV12_VIDEO(240),
        H264_VIDEO(241),
        H263_VIDEO(242),
        H261_VIDEO(243);

        private int id;

        Kind(int id)
        {
            this.id = id;
        }

        public int getId()
        {
            return id;
        }
    }


    public static enum AudioProfile
    {
        MAIN(1, "Main"),
        LOW_COMPLEXITY(2, "Low Complexity"),
        SCALEABLE(3, "Scaleable Sample rate"),
        T_F(4, "T/F"),
        T_F_MAIN(5, "T/F Main"),
        T_F_LC(6, "T/F LC"),
        TWIN_VQ(7, "TWIN"),
        CELP(8, "CELP"),
        HVXC(9, "HVXC"),
        HILN(10, "HILN"),
        TTSI(11, "TTSI"),
        MAIN_SYNTHESIS(12, "MAIN_SYNTHESIS"),
        WAVETABLE(13, "WAVETABLE"),;

        private int id;
        private String description;


        AudioProfile(int id, String description)
        {
            this.id = id;
            this.description = description;
        }

        public int getId()
        {
            return id;
        }

        public String getDescription()
        {
            return description;
        }
    }
}
