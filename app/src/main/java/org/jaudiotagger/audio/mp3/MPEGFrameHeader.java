
package org.jaudiotagger.audio.mp3;

import org.jaudiotagger.FileConstants;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.logging.AbstractTagDisplayFormatter;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;



@SuppressWarnings({"PointlessArithmeticExpression"})
public class MPEGFrameHeader
{

    private static final int BYTE_1 = 0;
    private static final int BYTE_2 = 1;
    private static final int BYTE_3 = 2;
    private static final int BYTE_4 = 3;
    public static final int HEADER_SIZE = 4;


    public static final int SYNC_SIZE = 2;

    public static final int SYNC_BYTE1 = 0xFF;
    public static final int SYNC_BYTE2 = 0xE0;
    public static final int SYNC_BIT_ANDSAMPING_BYTE3 = 0xFC;

    private static final byte[] header = new byte[HEADER_SIZE];



    public static final Map<Integer, String> mpegVersionMap = new HashMap<Integer, String>();
    public final static int VERSION_2_5 = 0;
    public final static int VERSION_2 = 2;
    public final static int VERSION_1 = 3;

    static
    {
        mpegVersionMap.put(VERSION_2_5, "MPEG-2.5");
        mpegVersionMap.put(VERSION_2, "MPEG-2");
        mpegVersionMap.put(VERSION_1, "MPEG-1");
    }


    public static final Map<Integer, String> mpegLayerMap = new HashMap<Integer, String>();
    public final static int LAYER_I = 3;
    public final static int LAYER_II = 2;
    public final static int LAYER_III = 1;

    static
    {
        mpegLayerMap.put(LAYER_I, "Layer 1");
        mpegLayerMap.put(LAYER_II, "Layer 2");
        mpegLayerMap.put(LAYER_III, "Layer 3");
    }


    public final static int LAYER_I_SLOT_SIZE = 4;
    public final static int LAYER_II_SLOT_SIZE = 1;
    public final static int LAYER_III_SLOT_SIZE = 1;


    private static final Map<Integer, Integer> bitrateMap = new HashMap<Integer, Integer>();

    static
    {
        // MPEG-1, Layer I (E)
        bitrateMap.put(0x1E, 32);
        bitrateMap.put(0x2E, 64);
        bitrateMap.put(0x3E, 96);
        bitrateMap.put(0x4E, 128);
        bitrateMap.put(0x5E, 160);
        bitrateMap.put(0x6E, 192);
        bitrateMap.put(0x7E, 224);
        bitrateMap.put(0x8E, 256);
        bitrateMap.put(0x9E, 288);
        bitrateMap.put(0xAE, 320);
        bitrateMap.put(0xBE, 352);
        bitrateMap.put(0xCE, 384);
        bitrateMap.put(0xDE, 416);
        bitrateMap.put(0xEE, 448);
        // MPEG-1, Layer II (C)
        bitrateMap.put(0x1C, 32);
        bitrateMap.put(0x2C, 48);
        bitrateMap.put(0x3C, 56);
        bitrateMap.put(0x4C, 64);
        bitrateMap.put(0x5C, 80);
        bitrateMap.put(0x6C, 96);
        bitrateMap.put(0x7C, 112);
        bitrateMap.put(0x8C, 128);
        bitrateMap.put(0x9C, 160);
        bitrateMap.put(0xAC, 192);
        bitrateMap.put(0xBC, 224);
        bitrateMap.put(0xCC, 256);
        bitrateMap.put(0xDC, 320);
        bitrateMap.put(0xEC, 384);
        // MPEG-1, Layer III (A)
        bitrateMap.put(0x1A, 32);
        bitrateMap.put(0x2A, 40);
        bitrateMap.put(0x3A, 48);
        bitrateMap.put(0x4A, 56);
        bitrateMap.put(0x5A, 64);
        bitrateMap.put(0x6A, 80);
        bitrateMap.put(0x7A, 96);
        bitrateMap.put(0x8A, 112);
        bitrateMap.put(0x9A, 128);
        bitrateMap.put(0xAA, 160);
        bitrateMap.put(0xBA, 192);
        bitrateMap.put(0xCA, 224);
        bitrateMap.put(0xDA, 256);
        bitrateMap.put(0xEA, 320);
        // MPEG-2, Layer I (6)
        bitrateMap.put(0x16, 32);
        bitrateMap.put(0x26, 48);
        bitrateMap.put(0x36, 56);
        bitrateMap.put(0x46, 64);
        bitrateMap.put(0x56, 80);
        bitrateMap.put(0x66, 96);
        bitrateMap.put(0x76, 112);
        bitrateMap.put(0x86, 128);
        bitrateMap.put(0x96, 144);
        bitrateMap.put(0xA6, 160);
        bitrateMap.put(0xB6, 176);
        bitrateMap.put(0xC6, 192);
        bitrateMap.put(0xD6, 224);
        bitrateMap.put(0xE6, 256);
        // MPEG-2, Layer II (4)
        bitrateMap.put(0x14, 8);
        bitrateMap.put(0x24, 16);
        bitrateMap.put(0x34, 24);
        bitrateMap.put(0x44, 32);
        bitrateMap.put(0x54, 40);
        bitrateMap.put(0x64, 48);
        bitrateMap.put(0x74, 56);
        bitrateMap.put(0x84, 64);
        bitrateMap.put(0x94, 80);
        bitrateMap.put(0xA4, 96);
        bitrateMap.put(0xB4, 112);
        bitrateMap.put(0xC4, 128);
        bitrateMap.put(0xD4, 144);
        bitrateMap.put(0xE4, 160);
        // MPEG-2, Layer III (2)
        bitrateMap.put(0x12, 8);
        bitrateMap.put(0x22, 16);
        bitrateMap.put(0x32, 24);
        bitrateMap.put(0x42, 32);
        bitrateMap.put(0x52, 40);
        bitrateMap.put(0x62, 48);
        bitrateMap.put(0x72, 56);
        bitrateMap.put(0x82, 64);
        bitrateMap.put(0x92, 80);
        bitrateMap.put(0xA2, 96);
        bitrateMap.put(0xB2, 112);
        bitrateMap.put(0xC2, 128);
        bitrateMap.put(0xD2, 144);
        bitrateMap.put(0xE2, 160);
    }


    public static final Map<Integer, String> modeMap = new HashMap<Integer, String>();
    public final static int MODE_STEREO = 0;
    public final static int MODE_JOINT_STEREO = 1;
    public final static int MODE_DUAL_CHANNEL = 2;
    public final static int MODE_MONO = 3;

    static
    {
        modeMap.put(MODE_STEREO, "Stereo");
        modeMap.put(MODE_JOINT_STEREO, "Joint Stereo");
        modeMap.put(MODE_DUAL_CHANNEL, "Dual");
        modeMap.put(MODE_MONO, "Mono");
    }


    private static final Map<Integer, String> emphasisMap = new HashMap<Integer, String>();
    public final static int EMPHASIS_NONE = 0;
    public final static int EMPHASIS_5015MS = 1;
    public final static int EMPHASIS_RESERVED = 2;
    public final static int EMPHASIS_CCITT = 3;

    static
    {
        emphasisMap.put(EMPHASIS_NONE, "None");
        emphasisMap.put(EMPHASIS_5015MS, "5015MS");
        emphasisMap.put(EMPHASIS_RESERVED, "Reserved");
        emphasisMap.put(EMPHASIS_CCITT, "CCITT");
    }


    private static final Map<Integer, String> modeExtensionMap = new HashMap<Integer, String>();
    private final static int MODE_EXTENSION_NONE = 0;
    private final static int MODE_EXTENSION_ONE = 1;
    private final static int MODE_EXTENSION_TWO = 2;
    private final static int MODE_EXTENSION_THREE = 3;

    private static final Map<Integer, String> modeExtensionLayerIIIMap = new HashMap<Integer, String>();
    private final static int MODE_EXTENSION_OFF_OFF = 0;
    private final static int MODE_EXTENSION_ON_OFF = 1;
    private final static int MODE_EXTENSION_OFF_ON = 2;
    private final static int MODE_EXTENSION_ON_ON = 3;

    static
    {
        modeExtensionMap.put(MODE_EXTENSION_NONE, "4-31");
        modeExtensionMap.put(MODE_EXTENSION_ONE, "8-31");
        modeExtensionMap.put(MODE_EXTENSION_TWO, "12-31");
        modeExtensionMap.put(MODE_EXTENSION_THREE, "16-31");

        modeExtensionLayerIIIMap.put(MODE_EXTENSION_OFF_OFF, "off-off");
        modeExtensionLayerIIIMap.put(MODE_EXTENSION_ON_OFF, "on-off");
        modeExtensionLayerIIIMap.put(MODE_EXTENSION_OFF_ON, "off-on");
        modeExtensionLayerIIIMap.put(MODE_EXTENSION_ON_ON, "on-on");
    }


    private static final Map<Integer, Map<Integer, Integer>> samplingRateMap = new HashMap<Integer, Map<Integer, Integer>>();
    private static final Map<Integer, Integer> samplingV1Map = new HashMap<Integer, Integer>();
    private static final Map<Integer, Integer> samplingV2Map = new HashMap<Integer, Integer>();
    private static final Map<Integer, Integer> samplingV25Map = new HashMap<Integer, Integer>();

    static
    {
        samplingV1Map.put(0, 44100);
        samplingV1Map.put(1, 48000);
        samplingV1Map.put(2, 32000);

        samplingV2Map.put(0, 22050);
        samplingV2Map.put(1, 24000);
        samplingV2Map.put(2, 16000);

        samplingV25Map.put(0, 11025);
        samplingV25Map.put(1, 12000);
        samplingV25Map.put(2, 8000);

        samplingRateMap.put(VERSION_1, samplingV1Map);
        samplingRateMap.put(VERSION_2, samplingV2Map);
        samplingRateMap.put(VERSION_2_5, samplingV25Map);
    }


    private static final Map<Integer, Map<Integer, Integer>> samplesPerFrameMap = new HashMap<Integer, Map<Integer, Integer>>();
    private static final Map<Integer, Integer> samplesPerFrameV1Map = new HashMap<Integer, Integer>();
    private static final Map<Integer, Integer> samplesPerFrameV2Map = new HashMap<Integer, Integer>();
    private static final Map<Integer, Integer> samplesPerFrameV25Map = new HashMap<Integer, Integer>();

    static
    {
        samplesPerFrameV1Map.put(LAYER_I, 384);
        samplesPerFrameV1Map.put(LAYER_II, 1152);
        samplesPerFrameV1Map.put(LAYER_III, 1152);

        samplesPerFrameV2Map.put(LAYER_I, 384);
        samplesPerFrameV2Map.put(LAYER_II, 1152);
        samplesPerFrameV2Map.put(LAYER_III, 1152);

        samplesPerFrameV25Map.put(LAYER_I, 384);
        samplesPerFrameV25Map.put(LAYER_II, 1152);
        samplesPerFrameV25Map.put(LAYER_III, 1152);

        samplesPerFrameMap.put(VERSION_1, samplesPerFrameV1Map);
        samplesPerFrameMap.put(VERSION_2, samplesPerFrameV2Map);
        samplesPerFrameMap.put(VERSION_2_5, samplesPerFrameV25Map);

    }


    private static final int SCALE_BY_THOUSAND = 1000;
    private static final int LAYER_I_FRAME_SIZE_COEFFICIENT = 12;
    private static final int LAYER_II_FRAME_SIZE_COEFFICIENT = 144;
    private static final int LAYER_III_FRAME_SIZE_COEFFICIENT = 144;


    private static final int MASK_MP3_ID = FileConstants.BIT3;


    private static final int MASK_MP3_VERSION = FileConstants.BIT4 | FileConstants.BIT3;


    private static final int MASK_MP3_LAYER = FileConstants.BIT2 | FileConstants.BIT1;


    private static final int MASK_MP3_PROTECTION = FileConstants.BIT0;


    private static final int MASK_MP3_BITRATE = FileConstants.BIT7 | FileConstants.BIT6 | FileConstants.BIT5 | FileConstants.BIT4;


    private static final int MASK_MP3_FREQUENCY = FileConstants.BIT3 + FileConstants.BIT2;


    private static final int MASK_MP3_PADDING = FileConstants.BIT1;


    private static final int MASK_MP3_PRIVACY = FileConstants.BIT0;


    private static final int MASK_MP3_MODE = FileConstants.BIT7 | FileConstants.BIT6;


    private static final int MASK_MP3_MODE_EXTENSION = FileConstants.BIT5 | FileConstants.BIT4;


    private static final int MASK_MP3_COPY = FileConstants.BIT3;


    private static final int MASK_MP3_HOME = FileConstants.BIT2;


    private static final int MASK_MP3_EMPHASIS = FileConstants.BIT1 | FileConstants.BIT0;


    private byte[] mpegBytes;


    private int version;

    private String versionAsString;


    private int layer;

    private String layerAsString;

    private Integer bitRate;


    private int channelMode;


    private String channelModeAsString;


    private int emphasis;


    private String emphasisAsString;


    private String modeExtension;


    private boolean isPadding;


    private boolean isCopyrighted;


    private boolean isOriginal;


    private boolean isProtected;



    private boolean isPrivate;

    private Integer samplingRate;



    public int getLayer()
    {
        return layer;
    }

    public String getLayerAsString()
    {
        return layerAsString;
    }


    private void setCopyrighted()
    {
        isCopyrighted = (mpegBytes[BYTE_4] & MASK_MP3_COPY) != 0;
    }



    private void setVersion() throws InvalidAudioFrameException
    {
        //MPEG Version
        version = (byte) ((mpegBytes[BYTE_2] & MASK_MP3_VERSION) >> 3);
        versionAsString = mpegVersionMap.get(version);
        if (versionAsString == null)
        {
            throw new InvalidAudioFrameException("Invalid mpeg version");
        }
    }


    private void setOriginal()
    {
        isOriginal = (mpegBytes[BYTE_4] & MASK_MP3_HOME) != 0;
    }


    private void setProtected()
    {
        isProtected = (mpegBytes[BYTE_2] & MASK_MP3_PROTECTION) == 0x00;
    }


    private void setPrivate()
    {
        isPrivate = (mpegBytes[BYTE_3] & MASK_MP3_PRIVACY) != 0;
    }


    private void setBitrate() throws InvalidAudioFrameException
    {

        int bitRateIndex = mpegBytes[BYTE_3] & MASK_MP3_BITRATE | mpegBytes[BYTE_2] & MASK_MP3_ID | mpegBytes[BYTE_2] & MASK_MP3_LAYER;

        bitRate = bitrateMap.get(bitRateIndex);
        if (bitRate == null)
        {
            throw new InvalidAudioFrameException("Invalid bitrate");
        }
    }



    private void setChannelMode() throws InvalidAudioFrameException
    {
        channelMode = (mpegBytes[BYTE_4] & MASK_MP3_MODE) >>> 6;
        channelModeAsString = modeMap.get(channelMode);
        if (channelModeAsString == null)
        {
            throw new InvalidAudioFrameException("Invalid channel mode");
        }
    }


    private void setEmphasis() throws InvalidAudioFrameException
    {
        emphasis = mpegBytes[BYTE_4] & MASK_MP3_EMPHASIS;
        emphasisAsString = emphasisMap.get(emphasis);
        if (getEmphasisAsString() == null)
        {
            throw new InvalidAudioFrameException("Invalid emphasis");
        }
    }



    private void setPadding()
    {
        isPadding = (mpegBytes[BYTE_3] & MASK_MP3_PADDING) != 0;
    }



    private void setLayer() throws InvalidAudioFrameException
    {
        layer = (mpegBytes[BYTE_2] & MASK_MP3_LAYER) >>> 1;
        layerAsString = mpegLayerMap.get(layer);
        if (layerAsString == null)
        {
            throw new InvalidAudioFrameException("Invalid Layer");
        }
    }



    private void setModeExtension() throws InvalidAudioFrameException
    {
        int index = (mpegBytes[BYTE_4] & MASK_MP3_MODE_EXTENSION) >> 4;
        if (layer == LAYER_III)
        {
            modeExtension = modeExtensionLayerIIIMap.get(index);
            if (getModeExtension() == null)
            {
                throw new InvalidAudioFrameException("Invalid Mode Extension");
            }
        }
        else
        {
            modeExtension = modeExtensionMap.get(index);
            if (getModeExtension() == null)
            {
                throw new InvalidAudioFrameException("Invalid Mode Extension");
            }
        }
    }


    private void setSamplingRate() throws InvalidAudioFrameException
    {
        //Frequency
        int index = (mpegBytes[BYTE_3] & MASK_MP3_FREQUENCY) >>> 2;
        Map<Integer, Integer> samplingRateMapForVersion = samplingRateMap.get(version);
        if (samplingRateMapForVersion == null)
        {
            throw new InvalidAudioFrameException("Invalid version");
        }
        samplingRate = samplingRateMapForVersion.get(index);
        if (samplingRate == null)
        {
            throw new InvalidAudioFrameException("Invalid sampling rate");
        }
    }


    public int getNumberOfChannels()
    {
        switch (channelMode)
        {
            case MODE_DUAL_CHANNEL:
                return 2;
            case MODE_JOINT_STEREO:
                return 2;
            case MODE_MONO:
                return 1;
            case MODE_STEREO:
                return 2;
            default:
                return 0;
        }
    }

    public int getChannelMode()
    {
        return channelMode;
    }

    public String getChannelModeAsString()
    {
        return channelModeAsString;
    }


    public int getVersion()
    {
        return version;
    }

    public String getVersionAsString()
    {
        return versionAsString;
    }


    public int getPaddingLength()
    {
        if (isPadding())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public Integer getBitRate()
    {
        return bitRate;
    }

    public Integer getSamplingRate()
    {
        return samplingRate;
    }


    public int getFrameLength()
    {
        switch (version)
        {
            case VERSION_2:
            case VERSION_2_5:
                switch (layer)
                {
                    case LAYER_I:
                        return (LAYER_I_FRAME_SIZE_COEFFICIENT * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength()) * LAYER_I_SLOT_SIZE;

                    case LAYER_II:
                        return (LAYER_II_FRAME_SIZE_COEFFICIENT ) * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength() * LAYER_II_SLOT_SIZE;

                    case LAYER_III:
                        if (this.getChannelMode() == MODE_MONO)
                        {
                            return (LAYER_III_FRAME_SIZE_COEFFICIENT / 2 ) * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength() * LAYER_III_SLOT_SIZE;
                        }
                        else
                        {
                            return (LAYER_III_FRAME_SIZE_COEFFICIENT) * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength() * LAYER_III_SLOT_SIZE;
                        }


                    default:
                        throw new RuntimeException("Mp3 Unknown Layer:" + layer);

                }


            case VERSION_1:
                switch (layer)
                {
                    case LAYER_I:
                        return (LAYER_I_FRAME_SIZE_COEFFICIENT * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength()) * LAYER_I_SLOT_SIZE;

                    case LAYER_II:
                        return LAYER_II_FRAME_SIZE_COEFFICIENT * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength() * LAYER_II_SLOT_SIZE;

                    case LAYER_III:
                        return LAYER_III_FRAME_SIZE_COEFFICIENT * (getBitRate() * SCALE_BY_THOUSAND) / getSamplingRate() + getPaddingLength() * LAYER_III_SLOT_SIZE;

                    default:
                        throw new RuntimeException("Mp3 Unknown Layer:" + layer);

                }

            default:
                throw new RuntimeException("Mp3 Unknown Version:" + version);

        }
    }


    public int getNoOfSamples()
    {
        Integer noOfSamples = samplesPerFrameMap.get(version).get(layer);
        return noOfSamples;
    }


    public boolean isPadding()
    {
        return isPadding;
    }

    public boolean isCopyrighted()
    {
        return isCopyrighted;
    }

    public boolean isOriginal()
    {
        return isOriginal;
    }

    public boolean isProtected()
    {
        return isProtected;
    }

    public boolean isPrivate()
    {
        return isPrivate;
    }

    public boolean isVariableBitRate()
    {
        return false;
    }

    public int getEmphasis()
    {
        return emphasis;
    }

    public String getEmphasisAsString()
    {
        return emphasisAsString;
    }

    public String getModeExtension()
    {
        return modeExtension;
    }



    private MPEGFrameHeader() throws InvalidAudioFrameException
    {

    }


    private MPEGFrameHeader(byte[] b) throws InvalidAudioFrameException
    {
        mpegBytes = b;
        setBitrate();
        setVersion();
        setLayer();
        setProtected();
        setSamplingRate();
        setPadding();
        setPrivate();
        setChannelMode();
        setModeExtension();
        setCopyrighted();
        setOriginal();
        setEmphasis();
    }


    public static MPEGFrameHeader parseMPEGHeader(ByteBuffer bb) throws InvalidAudioFrameException
    {
        int position = bb.position();
        bb.get(header, 0, HEADER_SIZE);
        bb.position(position);
        MPEGFrameHeader frameHeader = new MPEGFrameHeader(header);

        return frameHeader;
    }


    public static boolean isMPEGFrame(ByteBuffer bb)
    {
        int position = bb.position();
        return (((bb.get(position) & SYNC_BYTE1) == SYNC_BYTE1)
                && ((bb.get(position + 1) & SYNC_BYTE2) == SYNC_BYTE2)
                && ((bb.get(position + 2) & SYNC_BIT_ANDSAMPING_BYTE3) != SYNC_BIT_ANDSAMPING_BYTE3));
    }


    public String toString()
    {
        return " mpeg frameheader:" + " frame length:" + getFrameLength() + " version:" + versionAsString + " layer:" + layerAsString + " channelMode:" + channelModeAsString + " noOfSamples:" + getNoOfSamples() + " samplingRate:" + samplingRate + " isPadding:" + isPadding + " isProtected:" + isProtected + " isPrivate:" + isPrivate + " isCopyrighted:" + isCopyrighted + " isOriginal:" + isCopyrighted + " isVariableBitRate" + this.isVariableBitRate() + " header as binary:" + AbstractTagDisplayFormatter.displayAsBinary(mpegBytes[BYTE_1]) + " " + AbstractTagDisplayFormatter.displayAsBinary(mpegBytes[BYTE_2]) + " " + AbstractTagDisplayFormatter.displayAsBinary(mpegBytes[BYTE_3]) + " " + AbstractTagDisplayFormatter.displayAsBinary(mpegBytes[BYTE_4]);
    }
}

