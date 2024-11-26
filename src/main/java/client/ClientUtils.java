package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utility class for handling client-related operations.
 */
public class ClientUtils {

  /**
   * Reads user input from the console.
   *
   * @return The user input as a string.
   * @throws IOException If an I/O error occurs.
   */
  public static String getUserInput() throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    return reader.readLine();
  }
}
