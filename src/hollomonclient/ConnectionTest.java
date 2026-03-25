package hollomonclient;

import java.util.ArrayList;

public class ConnectionTest {

    public static void main(String[] args) {
        Client client = new Client("netsrv.cim.rhul.ac.uk", 1812);

        ArrayList<Card> cards = client.login("evidence", "internationaltodiscover");

        
        System.out.println("\n--- All Cards Have Been Received ---");
        
        
        for (Card card : cards) {
            System.out.println(card);
        }
        
        int credits = client.getCredits("evidence", "internationaltodiscover"); // Requests credits from my user using pass

        System.out.println("\nTotal Credits: " + credits); // Prints total credits
    }
}