package edu.sjsu.cs158a;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class WikiTest {

    @Test
    void testMain() {
        // Redirect System.out to a ByteArrayOutputStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Create command line arguments to test with
        String[] args = {"Linux"};

        // Call the main method with the command line arguments
        Wiki.main(args);

        // Retrieve the output of the main method from the ByteArrayOutputStream
        String expectedOutput = "Searching: Linux - Wikipedia\n" +
                "Checking children:\n" +
                "Found in: Bell Labs - Wikipedia";
        String actualOutput = outContent.toString().trim();

        // Use assertions to check that the output of the main method is what you expect it to be
        assertEquals(expectedOutput, actualOutput);
    }
}