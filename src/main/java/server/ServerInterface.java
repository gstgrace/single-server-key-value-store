package server;

/**
 * Interface for server operations.
 */
public interface ServerInterface {

  /**
   * Starts the server.
   */
  void execute();

  /**
   * Shuts down the server.
   */
  void shutdown();
}
