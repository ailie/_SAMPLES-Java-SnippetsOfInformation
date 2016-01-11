package com.example.net;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPRequests {

    public static void main(String[] args) throws Exception {
        try (BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(
                                new URL("http://www.google.com").openStream(),
                                        StandardCharsets.ISO_8859_1))) {
            while (in.ready()) {
                System.out.println(in.readLine());
            }
        }
    }
}
