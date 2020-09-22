package org.jaudiotagger.tag.id3.reference;


public class ITunesRating extends ID3Rating
{
    private static ID3Rating rating=null;
    private ITunesRating()
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
                newValue=20;
                break;

            case 2:
                newValue=40;
                break;

            case 3:
                newValue=60;
                break;

            case 4:
                newValue=80;
                break;

            case 5:
                newValue=100;
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
        else if (value<=20)
        {
             newValue = 1;
        }
        else if (value<=40)
        {
             newValue = 2;
        }
        else if (value<=60)
        {
             newValue = 3;
        }
        else if (value<=80)
        {
             newValue = 4;
        }
        else if (value<=100)
        {
             newValue = 5;
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
            rating=new ITunesRating();
        }
        return rating;
    }
}
