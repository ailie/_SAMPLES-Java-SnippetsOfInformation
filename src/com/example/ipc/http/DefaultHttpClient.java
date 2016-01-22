package com.example.ipc.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class DefaultHttpClient {

    public static void main(String[] args) throws IOException {
        String result;

        result = get("https://httpbin.org/get");
        result = post("https://httpbin.org/post");

        System.out.println(result);
    }

    private static String get(String link) throws MalformedURLException, IOException {
        return getResponseBody((HttpURLConnection) new URL(link).openConnection(), 999999);
    }

    private static String post(String link) throws MalformedURLException, IOException {

        HttpURLConnection conn = (HttpURLConnection) new URL(link).openConnection();

        conn.setInstanceFollowRedirects(false);
        conn.setUseCaches(false);

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String encoding = "UTF-8";

        conn.setRequestProperty("Content-Type",
                                "application/x-www-form-urlencoded; charset=" + encoding);

        byte[] entityPayload = buildEntityPayloaFrom(encoding,
                                                     "username: VIP",
                                                     "password: SECRET");

        conn.setRequestProperty("Content-Length",
                                Integer.toString(entityPayload.length));

        conn.getOutputStream().write(entityPayload);

        return getResponseBody(conn, 999999);
    }

    /**
     * @param encoding
     * @param entityHeaderFields
     *            an array of fields like: "username: VIP", "password: SECRET"
     */
    private static byte[] buildEntityPayloaFrom(String encoding, String... entityHeaderFields)
            throws UnsupportedEncodingException {

        StringBuilder result = new StringBuilder();

        for (String i : entityHeaderFields) {
            String[] pair = i.split(":", 2);
            String key = URLEncoder.encode(pair[0].trim(), encoding);
            String val = URLEncoder.encode(pair[1].trim(), encoding);
            result.append(String.format("%s=%s&", key, val));
        }
        return result.toString().replaceFirst("&$", "").getBytes(encoding);
    }

    private static String getResponseBody(HttpURLConnection conn, int maxStringLen)
            throws IOException {

        int status = conn.getResponseCode();

        if (status >= 200 && status < 300) {
            if (conn.getInputStream() != null) {
                String encoding = getEncoding(conn, "UTF-8");
                String result = buildStringFrom(conn.getInputStream(),
                                                maxStringLen,
                                                Charset.forName(encoding));
                conn.getInputStream().close();
                return result;
            } else
                return null;
        } else
            throw new IllegalStateException("Unexpected response status: " + status);

    }

    /** Decodes a sequence of bytes into a list of characters. */
    public static String buildStringFrom(InputStream is, int maxStringLen, Charset mapping)
            throws IOException, UnsupportedEncodingException {

        char[] buffer = new char[maxStringLen];
        int count = 0, countTotal = 0;

        while ((count = new InputStreamReader(is, mapping)
                .read(buffer, countTotal, maxStringLen - countTotal)) != /* EOF */ -1)
            countTotal += count;

        return new String(buffer, 0, countTotal);
    }

    private static String getEncoding(HttpURLConnection conn, String defEncoding) {

        if (conn.getContentEncoding() != null)
            return conn.getContentEncoding();

        else if (conn.getContentType() != null) {

            String regex = "^.*;\\s*charset=([^;]+).*$";
            if (conn.getContentType().matches(regex)) {
                String serverDeclaredEncoding = conn.getContentType()
                                                    .replaceFirst(regex, "$1");
                if (serverDeclaredEncoding.length() > 0)
                    return serverDeclaredEncoding;
            }
        }

        return "UTF-8";
    }
}
