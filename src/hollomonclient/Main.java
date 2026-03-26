package hollomonclient;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		Client client = new Client("netsrv.cim.rhul.ac.uk", 1812);
		
		System.out.println("---- Hollomon - Gotta Cache 'Em All! ----");
		
		System.out.print("Username: ");
		String username = scanner.nextLine();
		
		System.out.print("Password: ");
		String password = scanner.nextLine();
		
		boolean running = true;
		
		while (running) {
			System.out.println();
			System.out.println("1. Inventory");
			System.out.println("2. Balance ");
			System.out.println("3. Available Cards");
			System.out.println("4. Exit");
			System.out.print("Please enter your choice: ");
			
			String choice = scanner.nextLine();
			
			switch (choice) {
				case "1": // Player inventory
					ArrayList<Card> cards = client.getCards(username,  password);
					cards.sort(null); // Sort cards using compareTo method defined in Card.java
					
					System.out.println("\n--- Inventory ---");
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
					
					System.out.println("\n--- Available Cards ---");
					
					if (offers.isEmpty()) {
						System.out.println("No cards are currently for sale");
					} else {
						for (Card card : offers) {
							System.out.println(card);
						}
					}
					break;
					
				case "4":
					System.out.println("Exiting Hollomon client");
					running = false;
					break;
					
				default:
					System.out.println("Invalid choice. Please enter a correct numerical option");
					
			}
		
		}
		
		scanner.close();
	}

}
