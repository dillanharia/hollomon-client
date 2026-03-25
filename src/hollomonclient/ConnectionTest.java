package hollomonclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionTest {

    public static void main(String[] args) {
        connectAndLogin();
    }
    	
    	
    private static void connectAndLogin() {
    	String serverHost = "netsrv.cim.rhul.ac.uk";
        int serverPort = 1812;

        
        String username = "evidence".toLowerCase();
        String password = "internationaltodiscover";

        try (
            Socket socket = new Socket(serverHost, serverPort);
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("====Connected to server====");

            outputWriter.println(username);
            outputWriter.println(password);

            String serverResponse;
            
            // Continuously read lines sent by server
            while ((serverResponse = inputReader.readLine()) != null) {

                if (serverResponse.equals("CARD")) {

                    int id = Integer.parseInt(inputReader.readLine());
                    String name = inputReader.readLine();
                    Rarity rarity = Rarity.fromString(inputReader.readLine());
                    int price = Integer.parseInt(inputReader.readLine());

                    // Create a Card object from the received server data
                    Card card = new Card(id, name, rarity, price);

                    // Print the parsed card
                    System.out.println("Parsed card: " + card);
                }
                else if (serverResponse.equals("Server has finished sending data")) {
                    break;
                }
                else {
                    System.out.println(serverResponse);
                }
            }

            System.out.println("====Connection closed====");
        } catch (IOException e) {
            System.out.println("----Could not connect to server----");
            e.printStackTrace();
        }
    }
}