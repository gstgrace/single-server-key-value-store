package client;

import common.Logger;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * UDP client that connects to a server, performs key-value operations, and logs interactions.
 */
public class UDPClient implements ClientInterface {
  private final String serverAddress;
  private final int port;
  private final Logger logger;
  private DatagramSocket socket;

  /**
   * Constructs a UDP client with the given server address and port.
   *
   * @param serverAddress The server's IP address.
   * @param port The server's port number.
   */
  public UDPClient(String serverAddress, int port) {
    this.serverAddress = serverAddress;
    this.port = port;
    this.logger = new Logger("UDPClientLog.log");
  }

  /**
   * Executes the client, connects to the server, and handles user input for key-value operations.
   */
  @Override
  public void execute() {
    try {
      socket = new DatagramSocket();
      socket.setSoTimeout(5000); // Set timeout of 5 seconds
      InetAddress address = InetAddress.getByName(serverAddress);

      System.out.println("Connected to UDP server.");
      logger.log("INFO", "Connected to UDP server at " + serverAddress + ":" + port);

      // Pre-populate key-value store
      prePopulate(socket, address);

      // Execute the 15 required commands
      performFifteenOperations(socket, address);

      while (true) {
        System.out.print("Enter command (PUT/GET/DELETE or 'quit' to exit): ");
        String request = ClientUtils.getUserInput();
        if (request.equalsIgnoreCase("quit")) {
          break;
        }

        sendAndReceive(request, socket, address);
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
   * @param socket DatagramSocket used for communication.
   * @param address InetAddress of the server.
   * @throws IOException If an I/O error occurs.
   */
  private void prePopulate(DatagramSocket socket, InetAddress address) throws IOException {
    String[] prePopulatedData = {
        "PUT lion 101", "PUT tiger 202", "PUT elephant 303",
        "PUT giraffe 404", "PUT zebra 505"
    };

    System.out.println("Pre-populating key-value store...");
    for (String command : prePopulatedData) {
      sendAndReceive(command, socket, address);
    }
    System.out.println("Pre-population completed.");
  }

  /**
   * Executes 15 operations (5 PUTs, 5 GETs, 5 DELETEs) on the key-value store.
   *
   * @param socket DatagramSocket used for communication.
   * @param address InetAddress of the server.
   * @throws IOException If an I/O error occurs.
   */
  private void performFifteenOperations(DatagramSocket socket, InetAddress address) throws IOException {
    String[] putCommands = {
        "PUT kangaroo 606", "PUT panda 707", "PUT cheetah 808",
        "PUT dolphin 909", "PUT penguin 1010"
    };

    String[] getCommands = {
        "GET lion", "GET tiger", "GET elephant", "GET giraffe", "GET zebra"
    };

    String[] deleteCommands = {
        "DELETE lion", "DELETE tiger", "DELETE elephant", "DELETE giraffe", "DELETE zebra"
    };

    // Execute 5 PUTs
    System.out.println("Performing 5 additional PUT commands...");
    for (String command : putCommands) {
      sendAndReceive(command, socket, address);
    }

    // Execute 5 GETs
    System.out.println("Performing 5 GET commands...");
    for (String command : getCommands) {
      sendAndReceive(command, socket, address);
    }

    // Execute 5 DELETEs
    System.out.println("Performing 5 DELETE commands...");
    for (String command : deleteCommands) {
      sendAndReceive(command, socket, address);
    }
  }

  /**
   * Sends a request to the server and receives a response using UDP.
   *
   * @param request The command to send to the server.
   * @param socket DatagramSocket used for communication.
   * @param address InetAddress of the server.
   * @throws IOException If an I/O error occurs.
   */
  private void sendAndReceive(String request, DatagramSocket socket, InetAddress address) throws IOException {
    byte[] buffer = request.getBytes(StandardCharsets.UTF_8);
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
    socket.send(packet);

    buffer = new byte[1024];
    DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
    try {
      socket.receive(responsePacket);

      String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
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
    if (socket != null && !socket.isClosed()) {
      socket.close();
    }
    logger.close();
    System.out.println("UDP Client closed.");
  }
}
