package common;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logs helpful messages to both a file and the console.
 */
public class Logger implements LoggerInterface {
  private FileWriter fileWriter;
  private static final String DATE_FORMAT = "MM-dd-yyyy HH:mm:ss.SSS";

  /**
   * Constructs a logger that writes log messages to the log file.
   *
   * @param logFileName The name of the log file.
   */
  public Logger(String logFileName) {
    try {
      this.fileWriter = new FileWriter(logFileName, true); // Open in append mode
    } catch (IOException e) {
      System.err.println("Logger Initialization Failed: " + e.getMessage());
    }
  }

  /**
   * Logs a message to the log file and console.
   *
   * @param level The log level (e.g., INFO, ERROR).
   * @param message The message to be logged.
   */
  @Override
  public void log(String level, String message) {
    String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
    String logMessage = String.format("%s [%s] - %s%n", timestamp, level, message);

    // Write the log message to the file
    try {
      if (fileWriter != null) {
        fileWriter.write(logMessage);
        fileWriter.flush(); // Ensure immediate writing to file
      }
    } catch (IOException e) {
      System.err.println("Failed to log message: " + e.getMessage());
    }

    // Also print the log message to the console
    System.out.print(logMessage);
  }

  /**
   * Closes the logger.
   */
  @Override
  public void close() {
    try {
      if (fileWriter != null) {
        fileWriter.close(); // Close file resources
      }
    } catch (IOException e) {
      System.err.println("Failed to close logger: " + e.getMessage());
    }
  }
}
