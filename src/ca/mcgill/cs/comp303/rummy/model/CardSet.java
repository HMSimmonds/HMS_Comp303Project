package ca.mcgill.cs.comp303.rummy.model;

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Collections;

public class CardSet extends HashSet<Card> implements ICardSet
{
	private boolean isGroup;
	private Set<Card> aCards;
	
	public CardSet(boolean pGroup, Set<Card> pSet)
	{
		aCards = new HashSet<Card>(pSet);
		isGroup = pGroup;
	}
	
	public CardSet(boolean pGroup)
	{
		aCards = new HashSet<Card>();
		isGroup = pGroup;
	}
	
	public Set<Card> getSet()
	{
		return Collections.unmodifiableSet(aCards);
	}
	
	public boolean contains(Card pCard) 
	{
		return aCards.contains(pCard);
	}
	
	public int size()
	{
		return aCards.size();
	}
	
	public boolean isRun()
	{
		return !isGroup;
	}
	
	public boolean isGroup()
	{
		return isGroup;
	}
	
	

}
