package ca.mcgill.cs.comp303.rummy.model;

import java.util.List;

public abstract class Player
{
	private Hand aHand;
	private String aName;
	
	public Player()
	{
		aHand = new Hand();
	}
	//ABSTRACT METHODS
	
	//draws card from stock or discard pile
	protected abstract boolean draw(Card pTopStock, Card pTopDiscard);
	
	//true if player wants to knock
	protected abstract boolean knock();
	
	//player discards
	protected abstract Card discard();
	
	protected abstract boolean takeFirstCard(Card pTopStock, Card pTopDiscard);
	
	//determines if player can knock
	public boolean canKnock()
	{
		getHand().autoMatch();
		
		return ((getHand().score() <= 10) && (!getHand().getMatchedCards().isEmpty())); 
	}
	
	public void endTurn()
	{
		aHand.add(aHand.getDrawnCard());
	}
	
	
	
	public Hand getHand()
	{
		return aHand;
	}
	
	public void setHand(List<Card> pCards)
	{
		for (Card c : pCards) aHand.add(c);
	}
	
	public Card getDrawnCard()
	{
		return aHand.getDrawnCard();
	}
	
	public void setDrawnCard(Card pCard)
	{
		aHand.setDrawnCard(pCard);
	}
	
	public void setName(String pName)
	{
		aName = pName;
	}
	
	public int getScore()
	{
		return getHand().score();
	}
	
	public String toString()
	{
		return aName;
	}
	
	
}
