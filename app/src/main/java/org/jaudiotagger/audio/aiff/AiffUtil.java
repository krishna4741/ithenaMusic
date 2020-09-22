package org.jaudiotagger.audio.aiff;

//import java.io.EOFException;
import java.io.IOException;
//import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AiffUtil {

    private final static SimpleDateFormat dateFmt =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    
    private final static Charset LATIN1 = Charset.availableCharsets().get("ISO-8859-1");
    

    public static long readUINT32(RandomAccessFile raf) throws IOException {
        long result = 0;
        for (int i = 0; i < 4; i++) {
            // Warning, always cast to long here. Otherwise it will be
            // shifted as int, which may produce a negative value, which will
            // then be extended to long and assign the long variable a negative
            // value.
            result <<= 8;
            result |= (long) raf.read();
        }
        return result;
    }
    

    public static String read4Chars(RandomAccessFile raf) throws IOException 
    {
        StringBuffer sbuf = new StringBuffer(4);
        for (int i = 0; i < 4; i++) {
            char ch = (char) raf.read();
            sbuf.append(ch);
        }
        return sbuf.toString();
    }
    
    public static double read80BitDouble (RandomAccessFile raf)
                throws IOException
    {
        byte[] buf = new byte[10];
        raf.readFully(buf);
        ExtDouble xd = new ExtDouble (buf);
        return xd.toDouble();
    }


    public static Date timestampToDate (long timestamp)
    {
        Calendar cal = Calendar.getInstance ();
        cal.set (1904, 0, 1, 0, 0, 0);
        
        // If we add the seconds directly, we'll truncate the long
        // value when converting to int.  So convert to hours plus
        // residual seconds.
        int hours = (int) (timestamp / 3600);
        int seconds = (int) (timestamp - (long) hours * 3600L);
        cal.add (Calendar.HOUR_OF_DAY, hours);
        cal.add (Calendar.SECOND, seconds);
        Date dat = cal.getTime ();
        return dat;
    }
    

    public static String formatDate (Date dat) {
        return dateFmt.format (dat);
    }
    

    public static String bytesToPascalString (byte[] data) {
        int len = (int) data[0];
        return new String(data, 0, len, LATIN1);
    }
    

    public static String readPascalString(RandomAccessFile raf) throws IOException {
        int len = raf.read ();
        byte[] buf = new byte[len + 1];
        raf.read (buf, 1, len);
        buf[0] = (byte) len;
        return bytesToPascalString(buf);
    }
}
