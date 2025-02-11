package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }

    public static int largest(int[] list) {
        int max = Integer.MIN_VALUE;
        try {
            max = list[0];
        } catch (IndexOutOfBoundsException e) {
            return max;
        }

        for (int elem : list) {
            if (elem > max) {
                max = elem;
            }
        }

        return max;
    }
}
