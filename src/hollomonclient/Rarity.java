package hollomonclient;


// This file defines the possible rarity levels for cards
// Rarities are also used to categorise cards and determine their ordering when they're being sorted

public enum Rarity {
	// types of rarities
    COMMON,
    UNCOMMON,
    RARE,
    UNIQUE;

    public static Rarity fromString(String rarityText) {
        return Rarity.valueOf(rarityText.toUpperCase());
    }
}
