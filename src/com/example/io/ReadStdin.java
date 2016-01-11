package com.example.io;
import java.util.Scanner;

public class ReadStdin {

    public static void main(String[] args) {

        // set some sys prop
        System.setProperty(PROPERTY_KEY, "5.6.7.8");

        // make a pause - read smth
        try (Scanner sc = new Scanner(System.in)) { // also see System.console().readLine()
            System.out.print("> ");
            System.out.format("You entered: '%s'\n", sc.nextLine());
        }

        // check if the sys prop was persisted in memory
        System.out.println(System.getProperty(PROPERTY_KEY));
    }

    private static final String PROPERTY_KEY = "http.proxyHost";
}
