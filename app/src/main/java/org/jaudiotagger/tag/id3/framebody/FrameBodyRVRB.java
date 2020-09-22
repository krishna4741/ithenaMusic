
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyRVRB extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyRVRB()
    {
        //        this.setObject("Reverb Left", new Short((short) 0));
        //        this.setObject("Reverb Right", new Short((short) 0));
        //        this.setObject("Reverb Bounces Left", new Byte((byte) 0));
        //        this.setObject("Reverb Bounces Right", new Byte((byte) 0));
        //        this.setObject("Reverb Feedback Left To Left", new Byte((byte) 0));
        //        this.setObject("Reverb Feedback Left To Right", new Byte((byte) 0));
        //        this.setObject("Reverb Feedback Right To Right", new Byte((byte) 0));
        //        this.setObject("Reverb Feedback Right to Left", new Byte((byte) 0));
        //        this.setObject("Premix Left To Right", new Byte((byte) 0));
        //        this.setObject("Premix Right To Left", new Byte((byte) 0));
    }

    public FrameBodyRVRB(FrameBodyRVRB body)
    {
        super(body);
    }


    public FrameBodyRVRB(short reverbLeft, short reverbRight, byte reverbBouncesLeft, byte reverbBouncesRight, byte reverbFeedbackLeftToLeft, byte reverbFeedbackLeftToRight, byte reverbFeedbackRightToRight, byte reverbFeedbackRightToLeft, byte premixLeftToRight, byte premixRightToLeft)
    {
        this.setObjectValue(DataTypes.OBJ_REVERB_LEFT, reverbLeft);
        this.setObjectValue(DataTypes.OBJ_REVERB_RIGHT, reverbRight);
        this.setObjectValue(DataTypes.OBJ_REVERB_BOUNCE_LEFT, reverbBouncesLeft);
        this.setObjectValue(DataTypes.OBJ_REVERB_BOUNCE_RIGHT, reverbBouncesRight);
        this.setObjectValue(DataTypes.OBJ_REVERB_FEEDBACK_LEFT_TO_LEFT, reverbFeedbackLeftToLeft);
        this.setObjectValue(DataTypes.OBJ_REVERB_FEEDBACK_LEFT_TO_RIGHT, reverbFeedbackLeftToRight);
        this.setObjectValue(DataTypes.OBJ_REVERB_FEEDBACK_RIGHT_TO_RIGHT, reverbFeedbackRightToRight);
        this.setObjectValue(DataTypes.OBJ_REVERB_FEEDBACK_RIGHT_TO_LEFT, reverbFeedbackRightToLeft);
        this.setObjectValue(DataTypes.OBJ_PREMIX_LEFT_TO_RIGHT, premixLeftToRight);
        this.setObjectValue(DataTypes.OBJ_PREMIX_RIGHT_TO_LEFT, premixRightToLeft);
    }


    public FrameBodyRVRB(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_REVERB;
    }



    protected void setupObjectList()
    {
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_LEFT, this, 2));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_RIGHT, this, 2));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_BOUNCE_LEFT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_BOUNCE_RIGHT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_FEEDBACK_LEFT_TO_LEFT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_FEEDBACK_LEFT_TO_RIGHT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_FEEDBACK_RIGHT_TO_RIGHT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_FEEDBACK_RIGHT_TO_LEFT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_PREMIX_LEFT_TO_RIGHT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_PREMIX_RIGHT_TO_LEFT, this, 1));
    }
}
