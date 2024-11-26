package server;

/**
 * Server application that runs a TCP or UDP server based on user input.
 */
public class ServerApp {

  /**
   * Main method to start the server.
   *
   * @param args Command-line arguments: <Port#> <TCP/UDP>
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: java ServerApp <Port#> <TCP/UDP>");
      System.exit(1);
    }

    String protocol = args[1].toUpperCase();
    int port = Integer.parseInt(args[0]);

    ServerInterface server;

    // Choose the protocol (TCP or UDP) based on the input
    switch (protocol) {
      case "UDP":
        server = new UDPServer(port);
        break;
      case "TCP":
        server = new TCPServer(port);
        break;
      default:
        System.err.println("Invalid protocol. Please enter either 'TCP' or 'UDP'.");
        return;
    }

    // Add shutdown hook for graceful server shutdown
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      server.shutdown();
      System.out.println("Server shutting down gracefully.");
    }));

    server.execute();
  }
}
