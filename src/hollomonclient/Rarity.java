package hollomonclient;

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
