package client;

import common.Logger;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * TCP client that connects to a server, performs key-value operations, and logs interactions.
 */
public class TCPClient implements ClientInterface {
  private final String serverAddress;
  private final int port;
  private final Logger logger;
  private Socket socket;

  /**
   * Constructs a TCP client with the given server address and port.
   *
   * @param serverAddress The server's IP address.
   * @param port The server's port number.
   */
  public TCPClient(String serverAddress, int port) {
    this.serverAddress = serverAddress;
    this.port = port;
    this.logger = new Logger("TCPClientLog.log");
  }

  /**
   * Executes the client, connects to the server, and handles user input for key-value operations.
   */
  @Override
  public void execute() {
    try {
      socket = new Socket(serverAddress, port);
      socket.setSoTimeout(5000); // Set timeout of 5 seconds

      logger.log("INFO", "Connected to TCP server at " + serverAddress + ":" + port);
      System.out.println("Connected to TCP server.");

      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

      // Pre-populate key-value store
      prePopulate(out, in);

      // Execute the 15 required commands
      performFifteenOperations(out, in);

      String request;
      while (true) {
        System.out.print("Enter command (PUT/GET/DELETE or 'quit' to exit): ");
        request = userInput.readLine();
        if (request.equalsIgnoreCase("quit")) {
          break;
        }

        sendAndReceive(request, out, in);
      }

    } catch (IOException e) {
      logger.log("ERROR", "Client error: " + e.getMessage());
    } finally {
      shutdown();
    }
  }

  /**
   * Pre-populates the key-value store with predefined data.
   *
   * @param out PrintWriter for sending commands to the server.
   * @param in BufferedReader for receiving responses from the server.
   * @throws IOException If an I/O error occurs.
   */
  private void prePopulate(PrintWriter out, BufferedReader in) throws IOException {
    String[] prePopulatedData = {
        "PUT apple 100", "PUT banana 200", "PUT cherry 300",
        "PUT mango 400", "PUT orange 500"
    };

    System.out.println("Pre-populating key-value store...");
    for (String command : prePopulatedData) {
      sendAndReceive(command, out, in);
    }
    System.out.println("Pre-population completed.");
  }

  /**
   * Executes 15 operations (5 PUTs, 5 GETs, 5 DELETEs) on the key-value store.
   *
   * @param out PrintWriter for sending commands to the server.
   * @param in BufferedReader for receiving responses from the server.
   * @throws IOException If an I/O error occurs.
   */
  private void performFifteenOperations(PrintWriter out, BufferedReader in) throws IOException {
    String[] putCommands = {
        "PUT kiwi 600", "PUT grape 700", "PUT watermelon 800",
        "PUT strawberry 900", "PUT blueberry 1000"
    };

    String[] getCommands = {
        "GET apple", "GET banana", "GET cherry", "GET mango", "GET orange"
    };

    String[] deleteCommands = {
        "DELETE apple", "DELETE banana", "DELETE cherry", "DELETE mango", "DELETE orange"
    };

    // Execute 5 PUTs
    System.out.println("Performing 5 additional PUT commands...");
    for (String command : putCommands) {
      sendAndReceive(command, out, in);
    }

    // Execute 5 GETs
    System.out.println("Performing 5 GET commands...");
    for (String command : getCommands) {
      sendAndReceive(command, out, in);
    }

    // Execute 5 DELETEs
    System.out.println("Performing 5 DELETE commands...");
    for (String command : deleteCommands) {
      sendAndReceive(command, out, in);
    }
  }

  /**
   * Helper method to send a request to the server and receive a response.
   *
   * @param request The command to send to the server.
   * @param out PrintWriter for sending commands to the server.
   * @param in BufferedReader for receiving responses from the server.
   * @throws IOException If an I/O error occurs.
   */
  private void sendAndReceive(String request, PrintWriter out, BufferedReader in) throws IOException {
    out.println(request);
    try {
      String response = in.readLine();
      if (response == null) {
        logger.log("ERROR", "Server closed the connection.");
        return;
      }
      System.out.println("Response: " + response);
      logger.log("INFO", "Sent request: " + request + " | Received response: " + response);
    } catch (SocketTimeoutException e) {
      logger.log("WARNING", "Server response timed out for request: " + request);
      System.out.println("No response from server. Moving to next command.");
    }
  }

  /**
   * Shuts down the client and closes the connection to the server.
   */
  @Override
  public void shutdown() {
    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
      }
      logger.close();
    } catch (IOException e) {
      logger.log("ERROR", "Error closing client socket: " + e.getMessage());
    }
    System.out.println("TCP Client closed.");
  }
}
