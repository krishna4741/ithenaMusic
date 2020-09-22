package org.jaudiotagger.tag.id3.reference;


public class MediaMonkeyPlayerRating  extends ID3Rating
{
    private static ID3Rating rating=null;
    private MediaMonkeyPlayerRating()
    {
    }

    public int convertRatingFromFiveStarScale(int value)
    {
        if(value < 0 || value > 5)
        {
            throw new IllegalArgumentException("convert Ratings from Five Star Scale accepts values from 0 to 5 not:"+value);
        }
        int newValue=0;

        switch(value)
        {
            case 0:
                break;

            case 1:
                newValue=1;
                break;

            case 2:
                newValue=64;
                break;

            case 3:
                newValue=128;
                break;

            case 4:
                newValue=196;
                break;

            case 5:
                newValue=255;
                break;

        }
        return newValue;
    }

    public int convertRatingToFiveStarScale(int value)
    {
        int newValue=0;
        if (value<=0)
        {
             newValue = 0;
        }
        else if (value<=1)
        {
             newValue = 1;
        }
        else if (value<=8)
        {
             newValue = 0;
        }
        else if (value<=18)
        {
             newValue = 1;
        }
        else if (value<=28)
        {
             newValue = 1;
        }
        else if (value<=28)
        {
             newValue = 1;
        }
        else if (value<=28)
        {
             newValue = 1;
        }
        else if (value<=28)
        {
             newValue = 1;
        }
        else if (value<=29)
        {
             newValue = 2;
        }
        else if (value<=39)
        {
             newValue = 1;
        }
        else if (value<=49)
        {
             newValue = 1;
        }
        else if (value<=113)
        {
             newValue = 2;
        }
        else if (value<=167)
        {
             newValue = 3;
        }
        else if (value<=218)
        {
             newValue = 4;
        }
        else
        {
             newValue = 5;
        }
        return  newValue;
    }

     public static ID3Rating getInstance()
    {
        if(rating==null)
        {
            rating=new MediaMonkeyPlayerRating();
        }
        return rating;
    }
}
