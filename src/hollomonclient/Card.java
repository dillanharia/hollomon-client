package hollomonclient;


// This file defines a single card in the game, storing its ID, name, rarity and price
// This class also illustrates how all cards are compared and sorted when displayed to the user via the terminal

public class Card implements Comparable<Card> {

    // The card's data: id, name of card, rarity of card and price of card
    private long cardId;
    private String cardName;
    private Rarity rarity;
    private long price;
    
    // Constructor to Initialise a card with all required attributes
    public Card(long cardId, String cardName, Rarity rarity, long price) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.rarity = rarity;
        this.price = price;
    }

    // ---- Getters ---- 
    public long getCardId() {
        return cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public long getPrice() {
        return price;
    }

    // Comparison method defining how cards are to be ordered
    @Override
    public int compareTo(Card other) { 

    	// Rarity Order
        int rarityCompare = other.rarity.ordinal() - this.rarity.ordinal();
        if (rarityCompare != 0) {
            return rarityCompare;
        }

        // Alphabetical Order
        int nameCompare = this.cardName.compareToIgnoreCase(other.cardName);
        if (nameCompare != 0) {
            return nameCompare;
        }


        return Long.compare(this.cardId, other.cardId);
    }

    // Card Formmatting
    @Override
    public String toString() { 
        return "[" + cardId + "] " + cardName + " (" + rarity + ") - " + price + " credits";
    }
}