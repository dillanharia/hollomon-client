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

            	// If a card block's received,parse it and store it as a Card obj
                if (serverResponse.equals("CARD")) {

                    long id = Long.parseLong(inputReader.readLine());
                    String name = inputReader.readLine();
                    Rarity rarity = Rarity.fromString(inputReader.readLine());
                    long price = Long.parseLong(inputReader.readLine());

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
    
    // method for receiving and displaying credits upon login
    public int getCredits(String username, String password) {

        int credits = 0;
        
        
        try (
            Socket socket = new Socket(serverHost, serverPort);
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))
        ) {
            // logs in
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
    
 // method to request & return the player's current card collection
    public ArrayList<Card> getCards(String username, String password) {

        ArrayList<Card> cards = new ArrayList<>();

        try (
            Socket socket = new Socket(serverHost, serverPort);
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))
        ) {

            // login first
            outputWriter.println(username.toLowerCase());
            outputWriter.println(password);

            String serverResponse;

            // skip initial login response and starter cards
            while ((serverResponse = inputReader.readLine()) != null) {
                if (serverResponse.equals("OK")) {
                    break;
                }
            }

            // send CARDS command
            outputWriter.println("CARDS");

            // read and parse returned cards
            while ((serverResponse = inputReader.readLine()) != null) {

                if (serverResponse.equals("CARD")) {

                    long id = Long.parseLong(inputReader.readLine());
                    String name = inputReader.readLine();
                    Rarity rarity = Rarity.fromString(inputReader.readLine());
                    long price = Long.parseLong(inputReader.readLine());

                    Card card = new Card(id, name, rarity, price);
                    cards.add(card);
                }
                else if (serverResponse.equals("OK")) {
                    break;
                }
            }

        } catch (IOException e) { // fail safe in case cards aren't received
            System.out.println("Error getting cards from server");
            e.printStackTrace();
        }

        return cards;
    }
    
    // Method to send the OFFERS cmd to request cards currently for sale
    public ArrayList<Card> getOffers(String username, String password) {
    	
    	ArrayList<Card> offers = new ArrayList<>();

        try (
            Socket socket = new Socket(serverHost, serverPort);
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))
        ) {
        	
        	// Login first
        	outputWriter.println(username.toLowerCase());
        	outputWriter.println(password);
        	
        	String serverResponse;
        	
        	// Skip initial login responses/starter cards
        	while ((serverResponse = inputReader.readLine()) != null) {
        		if (serverResponse.equals("OK")) {
        			break;
        		}
        	}
        	
        	// Request cards currently on sale
        	outputWriter.println("OFFERS");
        	
        	// Read/parse returned cards
        	while ((serverResponse = inputReader.readLine()) != null) {
        		
        		if (serverResponse.equals("CARD")) {
        			long id = Long.parseLong(inputReader.readLine());
        			String name = inputReader.readLine();
        			Rarity rarity = Rarity.fromString(inputReader.readLine());
        			long price = Long.parseLong(inputReader.readLine());
        			
        			Card card = new Card(id, name, rarity, price); // Store each returned offer as a Card obj
        			offers.add(card);
        		}
        		else if (serverResponse.equals("OK")) {
        			break;
        		}
        	}
        } catch (IOException e) {
        	System.out.println("Error getting offers from server");
        	e.printStackTrace();
        }
        
        return offers;
    }
    
    // Send BUY cmd for a selected card ID
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
    		
    		// Skip initial login response and starter cards
    		while ((serverResponse = inputReader.readLine()) != null) {
    			if (serverResponse.equals("OK")) {
    				break;
    			}
    		}
    		
    		// Send BUY cmd with chosen card ID
    		outputWriter.println(" BUY " + cardId);
    		
    		// Read server result
    		serverResponse = inputReader.readLine();
    		
    		return serverResponse != null && serverResponse.equals("OK");
    	
    	} catch (IOException e) {
    		System.out.println("Error buying card from server");
    		e.printStackTrace();
    		return false;
    	}
    }
 
}