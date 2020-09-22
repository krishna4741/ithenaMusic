
package org.jaudiotagger.audio.asf.util;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.logging.ErrorMessage;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Utils {

    public static final long DIFF_BETWEEN_ASF_DATE_AND_JAVA_DATE = 11644470000000l;

    public final static String LINE_SEPARATOR = System
            .getProperty("line.separator"); //$NON-NLS-1$

    private static final int MAXIMUM_STRING_LENGTH_ALLOWED = 32766;


    public static void checkStringLengthNullSafe(String value)
            throws IllegalArgumentException {
        if (value != null) {
            if (value.length() > MAXIMUM_STRING_LENGTH_ALLOWED) {
                throw new IllegalArgumentException(
                        ErrorMessage.WMA_LENGTH_OF_STRING_IS_TOO_LARGE
                                .getMsg((value.length() * 2)));
            }
        }
    }


    public static boolean isStringLengthValidNullSafe(String value) {
        if (value != null) {
            if (value.length() > MAXIMUM_STRING_LENGTH_ALLOWED) {
                return false;
            }
        }
        return true;
    }


    public static void copy(InputStream source, OutputStream dest, long amount)
            throws IOException {
        byte[] buf = new byte[8192];
        long copied = 0;
        while (copied < amount) {
            int toRead = 8192;
            if ((amount - copied) < 8192) {
                toRead = (int) (amount - copied);
            }
            int read = source.read(buf, 0, toRead);
            if (read == -1) {
                throw new IOException(
                        "Inputstream has to continue for another "
                                + (amount - copied) + " bytes.");
            }
            dest.write(buf, 0, read);
            copied += read;
        }
    }


    public static void flush(final InputStream source, final OutputStream dest)
            throws IOException {
        final byte[] buf = new byte[8192];
        int read;
        while ((read = source.read(buf)) != -1) {
            dest.write(buf, 0, read);
        }
    }


    public static byte[] getBytes(final long value, final int byteCount) {
        byte[] result = new byte[byteCount];
        for (int i = 0; i < result.length; i ++) {
            result[i] = (byte) ((value >>> (i*8)) & 0xFF);
        }
        return result;
    }


    public static byte[] getBytes(final String source, final Charset charset) {
        assert charset != null;
        assert source != null;
        final ByteBuffer encoded = charset.encode(source);
        final byte[] result = new byte[encoded.limit()];
        encoded.rewind();
        encoded.get(result);
        return result;
    }





    public static GregorianCalendar getDateOf(final BigInteger fileTime) {
        final GregorianCalendar result = new GregorianCalendar();

        // Divide by 10 to convert from -4 to -3 (millisecs)
        BigInteger time = fileTime.divide(new BigInteger("10"));
        // Construct Date taking into the diff between 1601 and 1970
        Date date = new Date(time.longValue() - DIFF_BETWEEN_ASF_DATE_AND_JAVA_DATE);
        result.setTime(date);
        return result;
    }


    public static boolean isBlank(String toTest) {
        if (toTest == null)
        {
            return true;
        }

        for (int i = 0; i < toTest.length(); i++)
        {
            if(!Character.isWhitespace(toTest.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }


    public static BigInteger readBig64(InputStream stream) throws IOException {
        byte[] bytes = new byte[8];
        byte[] oa = new byte[8];
        int read = stream.read(bytes);
        if (read != 8) {
            // 8 bytes mandatory.
            throw new EOFException();
        }
        for (int i = 0; i < bytes.length; i++) {
            oa[7 - i] = bytes[i];
        }
        return new BigInteger(oa);
    }


    public static byte[] readBinary(InputStream stream, long size)
            throws IOException {
        byte[] result = new byte[(int) size];
        stream.read(result);
        return result;
    }


    public static String readCharacterSizedString(InputStream stream)
            throws IOException {
        StringBuilder result = new StringBuilder();
        int strLen = readUINT16(stream);
        int character = stream.read();
        character |= stream.read() << 8;
        do {
            if (character != 0) {
                result.append((char) character);
                character = stream.read();
                character |= stream.read() << 8;
            }
        } while (character != 0 || (result.length() + 1) > strLen);
        if (strLen != (result.length() + 1)) {
            throw new IllegalStateException(
                    "Invalid Data for current interpretation"); //$NON-NLS-1$
        }
        return result.toString();
    }


    public static String readFixedSizeUTF16Str(InputStream stream, int strLen)
            throws IOException {
        byte[] strBytes = new byte[strLen];
        int read = stream.read(strBytes);
        if (read == strBytes.length) {
            if (strBytes.length >= 2) {

                if (strBytes[strBytes.length - 1] == 0
                        && strBytes[strBytes.length - 2] == 0) {
                    byte[] copy = new byte[strBytes.length - 2];
                    System.arraycopy(strBytes, 0, copy, 0, strBytes.length - 2);
                    strBytes = copy;
                }
            }
            return new String(strBytes, "UTF-16LE");
        }
        throw new IllegalStateException(
                "Couldn'timer read the necessary amount of bytes.");
    }


    public static GUID readGUID(InputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("Argument must not be null"); //$NON-NLS-1$
        }
        int[] binaryGuid = new int[GUID.GUID_LENGTH];
        for (int i = 0; i < binaryGuid.length; i++) {
            binaryGuid[i] = stream.read();
        }
        return new GUID(binaryGuid);
    }


    public static int readUINT16(InputStream stream) throws IOException {
        int result = stream.read();
        result |= stream.read() << 8;
        return result;
    }


    public static long readUINT32(InputStream stream) throws IOException {
        long result = 0;
        for (int i = 0; i <= 24; i += 8) {
            // Warning, always cast to long here. Otherwise it will be
            // shifted as int, which may produce a negative value, which will
            // then be extended to long and assign the long variable a negative
            // value.
            result |= (long) stream.read() << i;
        }
        return result;
    }


    public static long readUINT64(InputStream stream) throws IOException {
        long result = 0;
        for (int i = 0; i <= 56; i += 8) {
            // Warning, always cast to long here. Otherwise it will be
            // shifted as int, which may produce a negative value, which will
            // then be extended to long and assign the long variable a negative
            // value.
            result |= (long) stream.read() << i;
        }
        return result;
    }


    public static String readUTF16LEStr(InputStream stream) throws IOException {
        int strLen = readUINT16(stream);
        byte[] buf = new byte[strLen];
        int read = stream.read(buf);
        if (read == strLen || (strLen == 0 && read == -1)) {

            if (buf.length >= 2) {
                if (buf[buf.length - 1] == 0 && buf[buf.length - 2] == 0) {
                    byte[] copy = new byte[buf.length - 2];
                    System.arraycopy(buf, 0, copy, 0, buf.length - 2);
                    buf = copy;
                }
            }
            return new String(buf, AsfHeader.ASF_CHARSET.name());
        }
        throw new IllegalStateException(
                "Invalid Data for current interpretation"); //$NON-NLS-1$
    }


    public static void writeUINT16(int number, OutputStream out)
            throws IOException {
        if (number < 0) {
            throw new IllegalArgumentException("positive value expected."); //$NON-NLS-1$
        }
        byte[] toWrite = new byte[2];
        for (int i = 0; i <= 8; i += 8) {
            toWrite[i / 8] = (byte) ((number >> i) & 0xFF);
        }
        out.write(toWrite);
    }


    public static void writeUINT32(long number, OutputStream out)
            throws IOException {
        if (number < 0) {
            throw new IllegalArgumentException("positive value expected."); //$NON-NLS-1$
        }
        byte[] toWrite = new byte[4];
        for (int i = 0; i <= 24; i += 8) {
            toWrite[i / 8] = (byte) ((number >> i) & 0xFF);
        }
        out.write(toWrite);
    }


    public static void writeUINT64(long number, OutputStream out)
            throws IOException {
        if (number < 0) {
            throw new IllegalArgumentException("positive value expected."); //$NON-NLS-1$
        }
        byte[] toWrite = new byte[8];
        for (int i = 0; i <= 56; i += 8) {
            toWrite[i / 8] = (byte) ((number >> i) & 0xFF);
        }
        out.write(toWrite);
    }

}