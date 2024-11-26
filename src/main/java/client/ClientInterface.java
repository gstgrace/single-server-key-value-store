package client;

/**
 * Defines the basic operations for a client, including execute and shutdown.
 */
public interface ClientInterface {

  /**
   * Starts the client.
   */
  void execute();

  /**
   * Gracefully shuts down the client.
   */
  void shutdown();
}
