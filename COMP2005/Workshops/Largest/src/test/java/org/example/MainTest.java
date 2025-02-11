package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LargestTest {

    @Test
    void testValid() {
        assertEquals(9, Main.largest(new int[]{5, 9, 6}));
    }

    @Test
    void testOrders() {
        assertEquals(9, Main.largest(new int[]{7, 8, 9}));
        assertEquals(9, Main.largest(new int[]{9, 7, 8}));
        assertEquals(9, Main.largest(new int[]{8, 9, 7}));
    }

    @Test
    void testInvalid() {
        assertNotEquals(5, Main.largest(new int[]{5, 9, 6}));
    }

    @Test
    void testSingle() {
        assertEquals(5, Main.largest(new int[]{5}));
    }

    @Test
    void testZero() {
        assertEquals(0, Main.largest(new int[]{0}));
    }

    @Test
    void testNegative() {
        assertEquals(-7, Main.largest(new int[]{-9, -8, -7}));
    }

    @Test
    void testMixedPolarity() {
        assertEquals(3, Main.largest(new int[]{-9, -6, 0, 3}));
    }

    @Test
    void testDuplicatesOther() {
        assertEquals(9 , Main.largest(new int[]{8, 8, 9}));
    }

    @Test
    void testDuplicatesTarget() {
        assertEquals(9, Main.largest(new int[]{8, 9, 9}));
    }

    @Test
    void testEmpty() {
        assertEquals(Integer.MIN_VALUE, Main.largest(new int[]{}));
    }

//    @Test(expected=IllegalArgumentException.class)
//    void testIllegalArgument() {
//        Main.largest(9);
//    }
}
