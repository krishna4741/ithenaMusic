package org.jaudiotagger.audio.aiff;


public class ExtDouble {

        byte[] _rawData;
        

        public ExtDouble(byte[] rawData) 
        {
            _rawData = rawData;
        }



        public double toDouble ()
        {
            int sign;
            int exponent;
            long mantissa = 0;
            
            // Extract the sign bit.
            sign = _rawData[0] >> 7;
            
            // Extract the exponent.  It's stored with a
            // bias of 16383, so subtract that off.
            // Also, the mantissa is between 1 and 2 (i.e.,
            // all but 1 digits are to the right of the binary point, so
            // we take 62 (not 63: see below) off the exponent for that.
            exponent = (_rawData[0] << 8) | _rawData[1];
            exponent &= 0X7FFF;          // strip off sign bit
            exponent -= (16383 + 62);    // 1 is added to the "real" exponent
            
            // Extract the mantissa.  It's 64 bits of unsigned
            // data, but a long is a signed number, so we have to
            // discard the LSB.  We'll lose more than that converting
            // to double anyway.  This division by 2 is the reason for
            // adding an extra 1 to the exponent above.
            int shifter = 55;
            for (int i = 2; i < 9; i++) {
                mantissa |= ((long) _rawData[i] & 0XFFL) << shifter;
                shifter -= 8;
            }
            mantissa |= _rawData[9] >>> 1;
            
            // Now put it together in a floating point number.
            double val = Math.pow (2, exponent);
            val *= mantissa;
            if (sign != 0) {
                val = -val;
            }
            return val;
        }
}

