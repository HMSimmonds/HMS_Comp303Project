package ca.mcgill.cs.comp303.rummy.model;

import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import java.io.Serializable;

import ca.mcgill.cs.comp303.rummy.model.Card.Rank;
import ca.mcgill.cs.comp303.rummy.model.Card.Suit;

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
		matched.clear();
		unmatched.clear();
	}
	
	/**
	 * @return A copy of the set of matched sets
	 */
	public Set<ICardSet> getMatchedCards()
	{
		return Collections.unmodifiableSet(matched);
	}
	
	/**
	 * @return A copy of the set of unmatched cards.
	 */
	public Set<Card> getUnmatchedCards()
	{
		return Collections.unmodifiableSet(unmatched);
	}
	
	/**
	 * @return The number of cards in the hand.
	 */
	public int size()
	{
		return getNumberOfMatchedCards() + unmatched.size();
	}
	
	private int getNumberOfMatchedCards()
	{
		int num = 0;
		for (ICardSet s: matched)
			num += s.size();
		
		return num;
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
		for (ICardSet c : matched)
		{
			if (c.contains(pCard)) return true;
		}
		
		return unmatched.contains(pCard);
	}
	
	/**
	 * @return The total point value of the unmatched cards in this hand.
	 */
	public int score()
	{
		int score = 0;
		
		for (Card c : unmatched)
			score += c.getScore();
		
		return score;
	}
	
	/**
	 * Creates a group of cards of the same rank.
	 * @param pCards The cards to groups
	 * @pre pCards != null
	 * @throws HandException If the cards in pCard are not all unmatched
	 * cards of the hand or if the group is not a valid group.
	 */
	public CardSet createGroup( Set<Card> pCards )
	{
		if (pCards.size() < 3) throw new HandException("Group does not have at least 3 cards");
		
		int rank = -1;
		for (Card c : pCards)
		{
			if (rank == -1) 
				rank = c.getRank().ordinal();
			
			else if (rank != c.getRank().ordinal()) 
				throw new HandException("Ranks are not the same");
			
			if (!unmatched.contains(c))
				throw new HandException("Cards are not all unmatched");
		}
		
		CardSet cardSet = new CardSet(true, pCards);
		
		return cardSet;
	}
	
	/**
	 * Creates a run of cards of the same suit.
	 * @param pCards The cards to group in a run
	 * @pre pCards != null
	 * @throws HandException If the cards in pCard are not all unmatched
	 * cards of the hand or if the group is not a valid group.
	 */
	public CardSet createRun( Set<Card> pCards )
	{
		if (pCards.size() < 3) throw new HandException("Run does not contain at least 3 cards");
		
		int suit = -1;
		for (Card c : pCards)
		{
			if (suit == -1) suit = c.getSuit().ordinal();
			else if (suit != c.getSuit().ordinal())
				throw new HandException("Suits are not similar");
			if (!unmatched.contains(c))
				throw new HandException("Cards are not unmatched");
		}
		
		CardSet cardSet = new CardSet(false, pCards);
		return cardSet;
	}
	
	/**
	 * Calculates the matching of cards into groups and runs that
	 * results in the lowest amount of points for unmatched cards.
	 */
	public void autoMatch()
	{
		/* Put the matched cards back into the unmatched set */
		for (ICardSet set : matched)
			for (Card card : set)
				unmatched.add(card);
		
		/* First compute the groups */
		LinkedList<CardSet> groups = calculateGroups();
		
		/* Second, compute the runs */
		LinkedList<CardSet> runs = calculateRuns();
		
		/* Select the best groups/runs, without conflict */
		/* So you have two lists, groups and runs containing
		 * the possible groups and runs.
		 */
		matched = getBestMatch(groups, runs);
	}
	
	private LinkedList<CardSet> calculateGroups()
	{
		int[] map = new int[NUM_SUIT_CARDS];
		
		//get the number of each rank and place into map
		for (Card c : unmatched)
			map[c.getRank().ordinal()] += 1;
		
		LinkedList<CardSet> groups = new LinkedList<CardSet>();
		
		for (int i = 0; i < NUM_SUIT_CARDS; i++)
		{
			if (map[i] >= 3)
			{
				//must be a group
				Set<Card> set = new HashSet<Card>();
				
				for (Card c : unmatched)
					if (c.getRank().ordinal() == i) set.add(c);
				
				groups.add(createGroup(set));
			}
		}
		
		return groups;
	}
	
	private LinkedList<CardSet> calculateRuns()
	{
		LinkedList<Card> spades = new LinkedList<Card>();
		LinkedList<Card> hearts = new LinkedList<Card>();
		LinkedList<Card> diamonds = new LinkedList<Card>();
		LinkedList<Card> clubs = new LinkedList<Card>();
		
		LinkedList<CardSet> runs = new LinkedList<CardSet>();
		
		for (Card c: unmatched)
		{
			Suit suit = c.getSuit();
			
			if (suit == Suit.CLUBS) clubs.add(c);
			else if (suit == Suit.DIAMONDS) diamonds.add(c);
			else if (suit == Suit.HEARTS) hearts.add(c);
			else spades.add(c);
		}
		
		Collections.sort(spades);
		Collections.sort(hearts);
		Collections.sort(diamonds);
		Collections.sort(clubs);
		
		LinkedList<LinkedList<Card>> lists = new LinkedList<LinkedList<Card>>();
		lists.add(clubs);
		lists.add(spades);
		lists.add(diamonds);
		lists.add(hearts);
		
		//get runs
		for (LinkedList<Card> lc : lists)
		{
			Set<Card> cur = new HashSet<Card>();
			Rank rank = null;
			int runsCounter = 0;
			
			
			for (Card c : lc)
			{
				if (rank == null) 
				{
					rank = c.getRank();
					runsCounter = 1;
				}
				
				else if (rank.ordinal() + runsCounter == c.getRank().ordinal())
				{
					if (runsCounter == 1) cur.add(new Card(rank, c.getSuit()));
					cur.add(c);
					runsCounter++;
				}
				else
				{
					if (runsCounter > 2) runs.add(createRun(cur));
					
					cur.clear();
					rank = null;
					runsCounter = 0;
				}
			}
			
			if (runsCounter > 2) runs.add(createRun(cur));
		}
		return runs;
	}
	
	private Set<ICardSet> getBestMatch(LinkedList<CardSet> groups, LinkedList<CardSet> runs)
	{
		List<CardSet> merge = new LinkedList<CardSet>();
		Set<ICardSet> ret = new HashSet<ICardSet>();
		
		// Sort groups and runs
		merge.addAll(groups);
		merge.addAll(runs);
		Collections.sort(merge, new Comparator<CardSet>() {
			public int compare(CardSet pCardSet1, CardSet pCardSet2)
			{
				return pCardSet2.getScore() - pCardSet1.getScore();
			} 
		});
		
		for (CardSet cardSet : merge)
		{
			boolean hasOverlap = false;
			
			for (Card card : cardSet)
			{
				for (ICardSet matchedCardSet : ret)
				{
					if (matchedCardSet.contains(card))
					{
						hasOverlap = true;
					}
				}
			}
			
			// If no card has an overlap, add the card set
			if (!hasOverlap) ret.add(cardSet);
			
			// and then remove the cards from the unmatched set
			for (Card c : cardSet)
			{
				unmatched.remove(c);
			}
		}
		
		return ret;	
	}
	
	public Set<Card> tryComplete(Set<ICardSet> pSet)
	{
		Set<Card> ret = new HashSet<Card>();
		
		for (Card c : unmatched)
		{
			for (ICardSet set : pSet)
			{
				if (set.isGroup())
				{
					// Group elements have the same rank
					if (c.getRank().ordinal() == set.iterator().next().getRank().ordinal())
						ret.add(c);
				}
				else
				{
					// Run elements have the same suit
					if (c.getSuit().ordinal() == set.iterator().next().getSuit().ordinal())
					{
						List<Card> l = new ArrayList<Card>();
						
						// Transforms ICardSet into a list
						for (Card card : set) l.add(card);
						
						Collections.sort(l);
						
						if (c.getRank().ordinal() == l.get(0).getRank().ordinal() - 1
						 || c.getRank().ordinal() == l.get(l.size()-1).getRank().ordinal() - 1)
							ret.add(c);
					}
				}
			}
		}
		
		return ret;
	}
	
		private int numberOfMatchedCards()
		{
			int size = 0;
			
			for (ICardSet s : matched)
			{
				size += s.size();
			}
			
			return size;
		}
		
		public Card getDrawnCard()
		{
			return pickedCard;
		}
		
		public void setDrawnCard(Card pCard)
		{
			pickedCard = pCard;
		}
}


