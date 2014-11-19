package ca.mcgill.cs.comp303.rummy.model;

import java.util.Set;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import java.io.Serializable;

/**
 * Models a hand of 10 cards. The hand is not sorted. Not threadsafe.
 * The hand is a set: adding the same card twice will not add duplicates
 * of the card.
 * @inv size() > 0
 * @inv size() <= HAND_SIZE
 */
public class Hand implements Serializable
{
	
	private final int NUM_SUIT_CARDS = 13;
	private final int HAND_SIZE = 10;
	
	private Set<Card> unmatched;
	private Set<ICardSet> matched;
	
	//buffer variable to remember players picked up card
	private Card pickedCard;
	
	
	/**
	 * Creates a new, empty hand.
	 */
	public Hand()
	{
		matched = new HashSet<ICardSet>();
		unmatched = new HashSet<Card>();
	}
	
	/**
	 * Adds pCard to the list of unmatched cards.
	 * If the card is already in the hand, it is not added.
	 * @param pCard The card to add.
	 * @throws HandException if the hand is complete.
	 * @throws HandException if the card is already in the hand.
	 * @pre pCard != null
	 */
	public void add( Card pCard )
	{	
		if (isComplete())
		{
			throw new HandException("Hand is full .. sorry!");
		}
		else if (contains(pCard))
		{
			throw new HandException("Card already exists ?");
		}
		
		else unmatched.add(pCard);
	}
	
	/**
	 * Remove pCard from the hand and break any matched set
	 * that the card is part of. Does nothing if
	 * pCard is not in the hand.
	 * @param pCard The card to remove.
	 * @pre pCard != null
	 */
	public void remove( Card pCard )
	{
		for (Iterator<ICardSet> it = matched.iterator(); it.hasNext();)
		{
			ICardSet cur = it.next();
			if (cur.contains(pCard))
			{
				//add cards from cardSet to unmatched
				for (Card c : cur)
					unmatched.add(c);
				
				it.remove(); //remove cardSet
			}
		}
		
		//now remove from unmatched
		unmatched.remove(pCard);
	}
	
	public void remove(Set<Card> pSet)
	{
		for (Card c: pSet)
			remove(c);
	}
	
	/**
	 * @return True if the hand is complete.
	 */
	public boolean isComplete()
	{
		return HAND_SIZE == size();
	}
	
	/**
	 * Removes all the cards from the hand.
	 */
	public void clear()
	{
		// TODO
	}
	
	/**
	 * @return A copy of the set of matched sets
	 */
	public Set<ICardSet> getMatchedSets()
	{
		return null; // TODO
	}
	
	/**
	 * @return A copy of the set of unmatched cards.
	 */
	public Set<Card> getUnmatchedCards()
	{
		return null; // TODO
	}
	
	/**
	 * @return The number of cards in the hand.
	 */
	public int size()
	{
		return Integer.MAX_VALUE; // TODO
	}
	
	/**
	 * Determines if pCard is already in the hand, either as an
	 * unmatched card or as part of a set.
	 * @param pCard The card to check.
	 * @return true if the card is already in the hand.
	 * @pre pCard != null
	 */
	public boolean contains( Card pCard )
	{
		return false; // TODO
	}
	
	/**
	 * @return The total point value of the unmatched cards in this hand.
	 */
	public int score()
	{
		return Integer.MAX_VALUE; // TODO
	}
	
	/**
	 * Creates a group of cards of the same rank.
	 * @param pCards The cards to groups
	 * @pre pCards != null
	 * @throws HandException If the cards in pCard are not all unmatched
	 * cards of the hand or if the group is not a valid group.
	 */
	public void createGroup( Set<Card> pCards )
	{
		// TODO
	}
	
	/**
	 * Creates a run of cards of the same suit.
	 * @param pCards The cards to group in a run
	 * @pre pCards != null
	 * @throws HandException If the cards in pCard are not all unmatched
	 * cards of the hand or if the group is not a valid group.
	 */
	public void createRun( Set<Card> pCards )
	{
		// TODO
	}
	
	/**
	 * Calculates the matching of cards into groups and runs that
	 * results in the lowest amount of points for unmatched cards.
	 */
	public void autoMatch()
	{
	}
}