package hollomonclient;

public class Card implements Comparable<Card> {

    // the card's data: id, name of card, rarity of card and price of card
    private long cardId;
    private String cardName;
    private Rarity rarity;
    private long price;
    
    // constructor to Initialise a card with all required attributes
    public Card(long cardId, String cardName, Rarity rarity, long price) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.rarity = rarity;
        this.price = price;
    }

    // ---- getters ---- 
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
        return Long.compare(this.cardId, other.cardId);
    }

    // card formatting
    @Override
    public String toString() { 
        return "[" + cardId + "] " + cardName + " (" + rarity + ") - " + price + " credits";
    }
}