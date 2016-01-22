package com.example.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class IOUtils {

    private static final int EOF                 = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 64;

    /** Decodes a sequence of bytes into a list of characters. */
    public static String buildStringFrom(InputStream is, int maxStringLen, Charset mapping)
            throws IOException, UnsupportedEncodingException {

        char[] buffer = new char[maxStringLen];
        int count = 0, countTotal = 0;

        while ((count = new InputStreamReader(is, mapping)
                .read(buffer, countTotal, maxStringLen - countTotal)) != EOF)
            countTotal += count;

        return new String(buffer, 0, countTotal);
    }

    /**
     * Copies all bytes from an InputStream to an OutputStream. This method uses an
     * internal buffer, so there is no need to use a BufferedInputStream. Does not close
     * or flush either stream.
     *
     * @return the number of bytes copied
     */
    public static long copy(InputStream src, OutputStream dst) throws IOException {
        return copy(src, dst, DEFAULT_BUFFER_SIZE);
    }

    public static long copyThenCloseSrc(InputStream src, OutputStream dst)
            throws IOException {
        long countTotal = copy(src, dst, DEFAULT_BUFFER_SIZE);
        src.close();
        return countTotal;
    }

    public static long copyThenCloseBoth(InputStream src, OutputStream dst)
            throws IOException {
        long countTotal = copy(src, dst, DEFAULT_BUFFER_SIZE);
        src.close();
        dst.close();
        return countTotal;
    }

    public static long copy(InputStream src, OutputStream dst, int bufferSize)
            throws IOException {

        byte[] buffer = new byte[bufferSize];
        int count = 0;
        long countTotal = 0;

        while ((count = src.read(buffer)) != EOF) {
            dst.write(buffer, 0, count);
            countTotal += count;
        }
        return countTotal;
    }
}
