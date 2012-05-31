import java.util.ArrayList;
import java.util.Random;


public class Board {

	Parser parser;
	
	public static final int numOfSpaces = 40;
	public Space[] allSpaces = new Space[40];
	public ArrayList<Card> allCards = new ArrayList<Card>();
	public ArrayList<Card> communityCards = new ArrayList<Card>();
	public ArrayList<Card> chanceCards = new ArrayList<Card>();
	public Random generator = new Random();
	
	public void setSpaces(int space, Space n) {
		allSpaces[space] = n;
	}
	
	public void addCard(Card card) {
		allCards.add(card);
		if (card.isCommunity()) { communityCards.add(card); }
		else { chanceCards.add(card); }
	}
	
	/**
	 * @param cardType
	 * @return Card that is drawn from the appropriate pile.
	 * Returns the card which was drawn from the appropriate pile and then removes it from the pile.
	 */
	public Card removeCard(boolean cardType) {
		int toRemove;
		if (cardType) { toRemove = generator.nextInt(communityCards.size()); }
		else { toRemove = generator.nextInt(chanceCards.size()); }
		Card originalCard = (cardType ? communityCards.get(toRemove) : chanceCards.get(toRemove));
		Card toReturn = originalCard.createShadowCard();
		if (cardType) { communityCards.remove(toRemove); } else { chanceCards.remove(toRemove); }
		return toReturn;
	}
		
	/**
	 * @param pile True for Community, false for Chance
	 * Used when all the cards in a pile are gone and reshuffles the appropriate pile
	 */
	public void reshuffleCards(boolean pile) {
		for (int i = 0; i < allCards.size(); i = i + 1) {
			Card currentCard = allCards.get(i);
			if (currentCard.isCommunity() == pile) {
				if (pile) { communityCards.add(currentCard); } else { chanceCards.add(currentCard); }
			}
		}
			
	}

	public Board(String filename) throws NullBoardException {
		parser = new Parser(filename, this);
	}

	public Space getSpace(int onSpace) {
		return allSpaces[onSpace];
	}
	
	public int numberOfCommunityCards() {
		return communityCards.size();
	}
	
	public int numberOfChanceCards() {
		return chanceCards.size();
	}
		
	public void getInformationOfSpace(int spaceNumber) {
		System.out.println(allSpaces[spaceNumber].toString());
	}
	
}
