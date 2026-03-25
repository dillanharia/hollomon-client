package hollomonclient;

public class Card implements Comparable<Card> {

    // the card's data: id, name of card, rarity of card and price of card
    private int cardId;
    private String cardName;
    private Rarity rarity;
    private int price;
    
    // constructor to Initialise a card with all required attributes
    public Card(int cardId, String cardName, Rarity rarity, int price) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.rarity = rarity;
        this.price = price;
    }

    // ---- getters ---- 
    public int getCardId() {
        return cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getPrice() {
        return price;
    }

    // comparison method defining how cards are to be ordered
    @Override
    public int compareTo(Card other) { 

    	// compare the rarity first (higher rarity comes first)
        int rarityCompare = other.rarity.ordinal() - this.rarity.ordinal();
        if (rarityCompare != 0) {
            return rarityCompare;
        }

        // if card rarities ==, then compare alphabetically by name
        int nameCompare = this.cardName.compareToIgnoreCase(other.cardName);
        if (nameCompare != 0) {
            return nameCompare;
        }

        // if name same, compare by ID
        return Integer.compare(this.cardId, other.cardId);
    }

    // card formatting
    @Override
    public String toString() { 
        return "[" + cardId + "] " + cardName + " (" + rarity + ") - " + price + " credits";
    }
}