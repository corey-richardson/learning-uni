package com.example.demo_sb;

import com.example.demosb.Message;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    @Test
    void testMessage() {
        String expected = "Hello there!!!";
        String actual = Message.getMessage();
        assertEquals(expected, actual);
    }
}
