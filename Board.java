import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;


public class Board {

	Parser parser;
	
	public static final int numOfSpaces = 40;
	public static final int maxNumHouses = 4;
	public Space[] allSpaces = new Space[40];
	public ArrayList<Card> allCards = new ArrayList<Card>();
	public ArrayList<Card> communityCards = new ArrayList<Card>();
	public ArrayList<Card> chanceCards = new ArrayList<Card>();
	public Property[][] groups; //Row corresponds to which group the properties in that row are in.
	public Random generator = new Random();
	
	public Board(String filename) throws NullBoardException {
		parser = new Parser(filename, this);
		setupGroups();
	}
	
	public void setSpaces(int space, Space n) {
		allSpaces[space] = n;
	}
	
	public void addCard(Card card) {
		allCards.add(card);
		if (card.isCommunity()) { communityCards.add(card); }
		else { chanceCards.add(card); }
	}
	
	/**
	 * @param cardType True for Community, false for Chance.
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
	 * @param pile True for Community, false for Chance.
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

	public Space getSpace(int onSpace) {
		return allSpaces[onSpace];
	}
	
	/**
	 * Sets up the groups to determine if a player has all the properties.
	 * ***TODO: Make this into a HashTable...
	 */
	public void setupGroups() {
		HashSet<Integer> numberOfDifferentGroups = new HashSet<Integer>();
		Property currentProperty;
		for (int i = 0; i < allSpaces.length; i = i + 1) {
			if (!(getSpace(i) instanceof Property)) { continue; }
			currentProperty = (Property) getSpace(i);
			numberOfDifferentGroups.add(currentProperty.getGroup());
		}
		groups = new Property[numberOfDifferentGroups.size()][];
		System.out.println("There are " + numberOfDifferentGroups.size() + " groups.");
		for (int i = 0; i < groups.length; i = i + 1) { //Which group
			int j = 0; //Which column in row we are on
			for (int k = 0; i < allSpaces.length; i = i + 1) {
				if (!(getSpace(k) instanceof Property)) { continue; }
				currentProperty = (Property) getSpace(k);
				if (currentProperty.getGroup() != i) { continue; }
				groups[i][j] = currentProperty; j = j + 1;
			}
		}
		
	}
	
	public Property[] getGroup(int group) {
		System.out.println("the size is: " + groups[group].length);
		return groups[group];
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
