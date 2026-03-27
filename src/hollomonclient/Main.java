package hollomonclient;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		Client client = new Client("netsrv.cim.rhul.ac.uk", 1812);
		
		System.out.println("==== Hollomon - Gotta Cache 'Em All! ====");
		
		System.out.print("Username: ");
		String username = scanner.nextLine();
		
		System.out.print("Password: ");
		String password = scanner.nextLine();
		
		boolean running = true;
		
		while (running) {
			System.out.println();
			System.out.println("==== Welcome to Hollomon's Gotta Cache 'Em All!' ====");
			System.out.println("1. Inventory");
			System.out.println("2. Balance ");
			System.out.println("3. Available Cards");
			System.out.println("4. Buy cards");
			System.out.println("5. Sell cards");
			System.out.println("6. Exit");
			System.out.print("Please enter your choice: ");
			
			String choice = scanner.nextLine();
			
			switch (choice) {
				case "1": // Player inventory
					ArrayList<Card> cards = client.getCards(username,  password);
					cards.sort(null); // Sort cards using compareTo method defined in Card.java
					
					System.out.println("\n==== Inventory ====");
					for (Card card : cards) {
						System.out.println(card);
					}
					break;
					
				case "2": // Player credit balance
					int credits = client.getCredits(username,  password);
					System.out.println("\nTotal Credits: " + credits);
					break;
					
				case "3": // Request and display the cards currently available to buy
					ArrayList<Card> offers = client.getOffers(username,  password);
					offers.sort(null);
					
					System.out.println("\n==== Available Cards ====");
					System.out.println("Number of offers: " + offers.size());
					
					
					if (offers.isEmpty()) {
						System.out.println("No cards are currently for sale");
					} else {
						for (Card card : offers) {
							System.out.println(card);
						}
					}
					break;
				
				case "4": // Buy command
					System.out.println("==== Use option 3 first to view available card IDs ====");
					
					System.out.print("Enter the ID of the card you want to buy: ");
					String buyInput = scanner.nextLine();
					
					try {
						long cardId = Long.parseLong(buyInput);
						
						boolean boughtSuccessfully = client.buyCard(username, password, cardId);
						
						if (boughtSuccessfully) {
							System.out.println("Card bought successfully");
						} else {
							System.out.println("Could not buy the card");
						}
					} catch (NumberFormatException e) { // Ensure entered card ID is numeric before sending it to server
						System.out.println("Invalid card ID. Please enter a number");
					}
					break;
					
				case "5": // Sell Command
					System.out.print("Enter the ID of the card you want to sell: ");;
					String idInput = scanner.nextLine();
					
					System.out.print("Enter the price you want to sell it for: ");;
					String priceInput = scanner.nextLine();
					
					try {
						long cardId = Long.parseLong(idInput);
						long price = Long.parseLong(priceInput);
						
						boolean soldSuccessfully = client.sellCard(username, password, cardId, price);
						
						if (soldSuccessfully) {
							System.out.println("Card listed for sale successfully");
						}
					} catch (NumberFormatException e) {
						System.out.println("Invalid input. Please enter numeric values.");
					}
					break;
					
				case "6": // Exit command
					System.out.println("==== Exiting Hollomon client ====");
					running = false;
					break;
					
				default:
					System.out.println("Invalid choice. Please enter a correct numerical option");
			}
		}
		scanner.close();
	}

}
