package ca.mcgill.cs.comp303.rummy.model;

/**
 * An immutable description of a playing card.
 */
public final class Card implements Comparable<Card>
{
	/**
	 * Represents the rank of the card.
	 */
	public enum Rank 
	{ ACE, TWO, THREE, FOUR, FIVE, SIX,
		SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING }
	
	/**
	 * Represents the suit of the card.
	 */
	public enum Suit 
	{ CLUBS, DIAMONDS, HEARTS, SPADES }
	
	private final Rank aRank;
	
	private final Suit aSuit;
	
	/**
	 * Create a new card object. 
	 * @param pRank The rank of the card.
	 * @param pSuit The suit of the card.
	 */
	public Card(Rank pRank, Suit pSuit )
	{
		aRank = pRank;
		aSuit = pSuit;
	}
	
	/**
	 * Obtain the rank of the card.
	 * @return An object representing the rank of the card.
	 */
	public Rank getRank()
	{
		return aRank;
	}
	
	/**
	 * Obtain the suit of the card.
	 * @return An object representing the suit of the card 
	 */
	public Suit getSuit()
	{
		return aSuit;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 * @return See above.
	 */
	public String toString()
	{
		return aRank + " of " + aSuit;
	}

	/**
	 * Compares two cards according to gin rules (ace is low, suits 
	 * run as Spade, Hearts, Diamonds, Clubs (high to low)).
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @param pCard The card to compare to
	 * @return Returns a negative integer, zero, or a positive integer 
	 * as this object is less than, equal to, or greater than pCard
	 */
	public int compareTo(Card pCard)
	{
		return getSuit().ordinal() != pCard.getSuit().ordinal() ? 
				getSuit().ordinal() - pCard.getSuit().ordinal() :
				(getRank().ordinal() == pCard.getRank().ordinal()) ?
						0 :
						getRank().ordinal() - pCard.getRank().ordinal();
	}

	/**
	 * Two cards are equal if they have the same suit and rank.
	 * @param pCard The card to test.
	 * @return true if the two cards are equal
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object pCard ) 
	{
		if (pCard == null) return false;
		else if (pCard == this) return true; //same reference
		else if (pCard.getClass() != getClass()) return false;
		else {
			boolean suit = aSuit == ((Card)pCard).getSuit();
			boolean rank = aRank == ((Card)pCard).getRank();
			
			return (rank && suit);
		}
	}

	/** 
	 * The hashcode for a card is the suit*13 + that of the rank (perfect hash).
	 * @return the hashcode
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() 
	{
		final int prime = 13; //pick any prime to create code
		return (aSuit.ordinal() * prime) + aRank.hashCode();
	}
	
	public int getScore()
	{
		final int HIGH_CARD_SCORE = 10;
		return aRank.ordinal() < Rank.TEN.ordinal() ? aRank.ordinal() + 1 : HIGH_CARD_SCORE;
	}
}
