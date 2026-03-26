package hollomonclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


// This Client.java file handles all communication with the Hollomon server, including login and all of the game's commands

public class Client {

	// Stores server address and port number for Hollomon connection
    private String serverHost;
    private int serverPort;

    // Makes a client for communicating with the hollomon Server
    public Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }
    
    // Login Method: connects to the server, performs login and retrieves the initial set of cards
    public ArrayList<Card> login(String username, String password) {

        try (
            Socket socket = new Socket(serverHost, serverPort);
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("====Connected to server====");

            // sends the username/pass to server
            outputWriter.println(username.toLowerCase());
            outputWriter.println(password);

            ArrayList<Card> cards = new ArrayList<>();
            String serverResponse;

            // read login response and initial cards
            while ((serverResponse = inputReader.readLine()) != null) {

                if (serverResponse.equals("CARD")) {
                    Card card = readCard(inputReader);
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
            return cards;

        } catch (IOException e) {
            System.out.println("--- Could not connect to server ---");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // Balance Method: requests and returns the Player's current credit balance from the server
    public int getCredits(String username, String password) {

        int credits = 0;
        
        try (
            Socket socket = new Socket(serverHost, serverPort);
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))
        ) {

            outputWriter.println(username.toLowerCase());
            outputWriter.println(password);

            String serverResponse;

            while ((serverResponse = inputReader.readLine()) != null) {
                if (serverResponse.equals("OK")) {
                    break;
                }
            }

            // Send CREDITS command to the Hollomon server
            outputWriter.println("CREDITS");

            String creditLine = inputReader.readLine();
            
            if (credits == 0) {
            	System.out.println("Oh No!");
            } else {
            credits = Integer.parseInt(creditLine);
            
            inputReader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error getting credits");
            e.printStackTrace();
        }

        return credits; // credits value
    }
    
 // Cards inventory method: Requests and returns the player's current card collection
    public ArrayList<Card> getCards(String username, String password) {

        try (
            Socket socket = new Socket(serverHost, serverPort);
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))
        ) {

            // Login first
            outputWriter.println(username.toLowerCase());
            outputWriter.println(password);

            // Skip initial login response and starter cards
            skipUntilOk(inputReader);

            // Send CARDS command
            outputWriter.println("CARDS");

            // Read and return the card list
            return readCardsUntilOk(inputReader);

        } catch (IOException e) {
            System.out.println("Error getting cards from server");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // Offers command method: Requests and returns the list of cards currently available for purchase
    public ArrayList<Card> getOffers(String username, String password) {

        try (
            Socket socket = new Socket(serverHost, serverPort);
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))
        ) {

            // Login first
            outputWriter.println(username.toLowerCase());
            outputWriter.println(password);

            // Skip initial login response and starter cards
            skipUntilOk(inputReader);

            // Request cards currently on sale
            outputWriter.println("OFFERS");

            // Read and return the offer list
            return readCardsUntilOk(inputReader);

        } catch (IOException e) {
            System.out.println("Error getting offers from server");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // BUY Command: Attempts to buy a card with a given ID and returns whether the purchase suceeded
    public boolean buyCard(String username, String password, long cardId) {
    	
    	try (
    		Socket socket = new Socket(serverHost, serverPort);
    			PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
    			BufferedReader inputReader = new BufferedReader(
    					new InputStreamReader(socket.getInputStream()))
    	) {
    	
    		// Login
    		outputWriter.println(username.toLowerCase());
    		outputWriter.println(password);
    		
    		String serverResponse;
    		
    		while ((serverResponse = inputReader.readLine()) != null) {
    			if (serverResponse.equals("OK")) {
    				break;
    			}
    		}
    		
    		// Send BUY cmd to Hollomon server
    		outputWriter.println("BUY " + cardId);
    		
    		serverResponse = inputReader.readLine();
    		
    		return serverResponse != null && serverResponse.equals("OK");
    	
    	} catch (IOException e) {
    		System.out.println("Error buying card from server");
    		e.printStackTrace();
    		return false;
    	}
    }
    
    // SELL command: attempts to list a card for sale with a specified price and returns if it succeeded
    public boolean sellCard(String username, String password, long cardId, long price) {
    	
    	try (
    		Socket socket = new Socket(serverHost, serverPort);
    			PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
    			BufferedReader inputReader = new BufferedReader(
    					new InputStreamReader(socket.getInputStream()))
    	) {
    		// Login
    		outputWriter.println(username.toLowerCase());
    		outputWriter.println(password);
    		
    		String serverResponse;
    		
    		while ((serverResponse = inputReader.readLine()) != null) {
    			if (serverResponse.equals("OK")) {
    				break;
    			}
    		}
    		// Send SELL command
    		outputWriter.println("SELL " + cardId + " " + price);
    		
    		serverResponse = inputReader.readLine();
    		
    		return serverResponse != null && serverResponse.equals("OK");
    	} catch (IOException e) {
    		System.out.println("Error selling card");
    		e.printStackTrace();
    		return false;
    	}
    }
    // Reads a single card from the server response and converts it into a Card object
    private Card readCard(BufferedReader inputReader) throws IOException {
        long id = Long.parseLong(inputReader.readLine());
        String name = inputReader.readLine();
        Rarity rarity = Rarity.fromString(inputReader.readLine());
        long price = Long.parseLong(inputReader.readLine());

        return new Card(id, name, rarity, price);
    }

    // Skips all server responses until "OK" marker's reached
    private void skipUntilOk(BufferedReader inputReader) throws IOException {
        String serverResponse;

        while ((serverResponse = inputReader.readLine()) != null) {
            if (serverResponse.equals("OK")) {
                break;
            }
        }
    }
    // Reads multiple card entries from the server until "OK" is received
    private ArrayList<Card> readCardsUntilOk(BufferedReader inputReader) throws IOException {
        ArrayList<Card> cards = new ArrayList<>();
        String serverResponse;

        while ((serverResponse = inputReader.readLine()) != null) {
            if (serverResponse.equals("CARD")) {
                cards.add(readCard(inputReader));
            }
            else if (serverResponse.equals("OK")) {
                break;
            }
        }

        return cards;
    }
    
 
}