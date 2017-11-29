package edu.up.cs301.hearts;

import java.util.Random;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Updated by S. Seydlitz on 11/19/17
 */

public class HardAI extends GameComputerPlayer {

    public HardAI(String playerName) {
        super(playerName);
    }

    protected void receiveInfo(GameInfo info) {

    }
/*
    int clubs = 0;
    int spades = 1;
    int diamonds = 2;
    int hearts = 4;
    String thisGuy = this.getName();
    Card[] currentHand = thisGuy.getHand();
    HeartsGameState thisTime = new HeartsGameState(0,thisGuy);
    int baseSuit = thisTime.getCurrentSuit();
    boolean heartsPlayed = false;
    Card chosenCard;
    String baseN;
    public String[] faceValues = {"Two", "Three", "Four", "Five", "Six", "Seven",
            "Eight", "Nine", "Ten", "Jack", "Queen","King","Ace"};

    public void strategy() {
        //Remember, there are three different AI's, so there are three different decks to keep track of

        //THIS WILL BE DIFFERENT FROM THE EASYAI'S CODE
        Random ran = new Random();
        //x is rank, y is suit
        int x = ran.nextInt(14);
        int y = ran.nextInt(5);
        int w = ran.nextInt(52);
        if(baseSuit==clubs){
            baseN = "Clubs";
        }
        if(baseSuit==spades){
            baseN = "Spades";
        }
        if(baseSuit==diamonds){
            baseN = "Diamonds";
        }
        if(baseSuit==hearts){
            baseN = "Hearts";
        }
        if(baseSuit==-1){
            chosenCard = new Card(x,y);
        }//our card will be first, so just choose a random one, doesn't matter if it's a heart

        //check and see if the AI player has a card of the suit that was originally played. If not, play any card

        /////check and see if the player has the originally played suit in their hand. If they don't, play any other suit
        if(checkIfFaceInHand(baseN)==true){
            //since they have a card of the original face in their hand, they must play a card of that face.
            chosenCard = new Card(x, baseSuit);
        }
        else{
            //since they don't have a card of the original face, play the highest cards, preferably hearts cards
            chosenCard = new Card(x,y);
        }

    }

    public Card playCard() {
        if(checkIfCardinHand(chosenCard)==true){
            //if they have this card, then take it away from the AI player's hand!
            //to do this we need to make sure that we can get the array of cards in the player's hand!
            return chosenCard;
        }
        else{
            strategy();
        }
    }
    */
}
