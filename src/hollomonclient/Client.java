package hollomonclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

	// Stores server address and port number for Hollomon connection
    private String serverHost;
    private int serverPort;

    // Makes a client for communicating with the hollomon Server
    public Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }
    
    // Connects to the server, sends login details, returns any cards received
    public ArrayList<Card> login(String username, String password) {
        ArrayList<Card> cards = new ArrayList<>(); // List used to store card objs received from server

        try (
            Socket socket = new Socket(serverHost, serverPort);
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("====Connected to server====");

            // Sends the username/pass to server
            outputWriter.println(username.toLowerCase());
            outputWriter.println(password);

            String serverResponse;

            // Continuously read lines sent by the Hollomon server
            while ((serverResponse = inputReader.readLine()) != null) {

            	// If a card block's received, parse it and store it as a Card obj
                if (serverResponse.equals("CARD")) {

                    int id = Integer.parseInt(inputReader.readLine());
                    String name = inputReader.readLine();
                    Rarity rarity = Rarity.fromString(inputReader.readLine());
                    int price = Integer.parseInt(inputReader.readLine());

                    // Create a Card object from the received server data
                    Card card = new Card(id, name, rarity, price);
                    cards.add(card);

                    System.out.println("Stored card: " + card);
                }
                else if (serverResponse.equals("OK")) {
                    break;
                }
                else {
                    System.out.println(serverResponse);
                }
            }

            System.out.println("--- Connection closed ---");
        } catch (IOException e) {
            System.out.println("--- Could not connect to server ---");
            e.printStackTrace();
        }

        return cards;
    }
    
    // Method for receiving and dispaying credits upon login
    public int getCredits(String username, String password) {

        int credits = 0;
        
        
        try (
            Socket socket = new Socket(serverHost, serverPort);
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))
        ) {
            // Logs in
            outputWriter.println(username.toLowerCase());
            outputWriter.println(password);

            String serverResponse;

            // skip login response + card data
            while ((serverResponse = inputReader.readLine()) != null) {
                if (serverResponse.equals("OK")) {
                    break;
                }
            }

            // send the 'CREDITS' command
            outputWriter.println("CREDITS");

            // read the credit value by parsing
            String creditLine = inputReader.readLine();
            credits = Integer.parseInt(creditLine);

            // Read final OK
            inputReader.readLine();

        } catch (IOException e) {
            System.out.println("Error getting credits");
            e.printStackTrace();
        }

        return credits; // credits value
    }
}