package common;

/**
 * Interface for logging messages.
 */
public interface LoggerInterface {

  /**
   * Logs a message.
   *
   * @param level The log level (e.g., INFO, ERROR).
   * @param message The message to log.
   */
  void log(String level, String message);

  /**
   * Closes the logger.
   */
  void close();
}
