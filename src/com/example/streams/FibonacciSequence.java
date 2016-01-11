package com.example.streams;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Processing Data with Java SE 8 Streams, Part 1
 * http://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html
 * */
public class FibonacciSequence {

    public static void main(String[] args) throws InterruptedException {

        long maxSize = 100000,startTime;

        startTime = System.nanoTime();
        IntStream limit1 = genFibonacciSequence_m1().limit(maxSize);
        System.out.println(limit1.count() +" : "+ (System.nanoTime() - startTime) / 1E6);  // elapsedTimeMS

        startTime = System.nanoTime();
        IntStream limit2 = genFibonacciSequence_m2().limit(maxSize);
        System.out.println(limit2.count() +" : "+ (System.nanoTime() - startTime) / 1E6);  // elapsedTimeMS
    }

    private static IntStream genFibonacciSequence_m1() { // faster - uses array objects for storage
        return Stream
                .iterate(new int[] { 0, 1 }, i -> new int[] { i[1], i[0] + i[1] })
                .mapToInt(i -> i[0]);
    }

    private static IntStream genFibonacciSequence_m2() {

        class Pair { // uses this custom kind of objects for storage

            int a, b;

            public Pair(int a, int b) {
                super();
                this.a = a;
                this.b = b;
            }
        }

        return Stream
                .iterate(new Pair(0, 1), n -> new Pair(n.b, n.a + n.b))
                .mapToInt(n -> n.a);
    }
}
