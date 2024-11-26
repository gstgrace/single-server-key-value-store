package client;

/**
 * Entry point for the client application.
 * Determines whether to use TCP or UDP based on the provided arguments.
 */
public class ClientApp {

  /**
   * Main method that initializes the client based on user input.
   *
   * @param args Command-line arguments: <ServerIP> <Port#> <TCP/UDP>
   */
  public static void main(String[] args) {
    if (args.length != 3) {
      System.err.println("Usage: java ClientApp <ServerIP> <Port#> <TCP/UDP>");
      System.exit(1);
    }

    String serverAddress = args[0];
    int port = Integer.parseInt(args[1]);
    String protocol = args[2].toUpperCase();

    ClientInterface client;

    // Switch between TCP and UDP based on user input
    switch (protocol) {
      case "UDP":
        client = new UDPClient(serverAddress, port);
        break;
      case "TCP":
        client = new TCPClient(serverAddress, port);
        break;
      default:
        System.err.println("Invalid protocol. Please enter either 'TCP' or 'UDP'.");
        return;
    }

    client.execute();
  }
}
