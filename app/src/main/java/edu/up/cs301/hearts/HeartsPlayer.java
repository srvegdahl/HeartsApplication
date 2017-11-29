package edu.up.cs301.hearts;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GamePlayer;


/**
 * Created by emmasoriano on 10/19/17.
 */

public class HeartsPlayer {

    public CardDeck hand;
    public CardDeck collection;
    public Card[] myPass = new Card[3];
    public boolean myTurn = false;
    public boolean isWinner = false;
    public boolean hasTwoOfClubs = false;
    public int score = 0;
    public String name;
    public HeartsGameState currentGameState;



    //Constructor
    public HeartsPlayer(String playerName, HeartsGameState GS){

        currentGameState = GS;
        //set players name
        name = playerName;

        //determines if player has the starting card
        //Clubs, spades, diamonds, hearts
        Card twoOfClubs = new Card(Rank.TWO, Suit.Club);
        hasTwoOfClubs = checkIfCardinHand(twoOfClubs);
    }


    public Card[] getMyPass(){
        return myPass;
    }


    public Card[] getHand(){
        return (Card[]) hand.cards.toArray();
    }

    public String getName(){
        return name;
    }

    public int getScore(){
        return score;
    }

    public boolean isMyTurn(){
        return myTurn;
    }

    public void setMyPass(Card[] cards){
        myPass = cards;
    }
    public void setIsWinner(boolean initWinner){
        isWinner= initWinner;
    }

    /**
     * Set hand to given list of cards
     * @param initHand - shouldn't be more then
     */
    public void setHand(CardDeck initHand){
        int i;
        for (Card c: initHand.cards){
            c = initHand.peekAtTopCard();
            hand.add(c);
            initHand.removeTopCard();
        }
    }

    public void setName(String initName){
        name = initName;
    }

    public void setScore(int initScore){
        score = score + initScore;
    }

    public void setMyTurn(boolean initMyTurn){
        myTurn = initMyTurn;
    }

    public void threeCardPass(CardDeck pass, GamePlayer p){
        //pass cards to appropriate player
        for(Card c: pass.cards){
            p.hand.cards.add(c);
        }
        CardDeck copyPass = pass;
        //remove cards passed to another player from hand
        for(Card c: hand.cards){
            for(int i=0; i<pass.size(); i++){
                if(c.equals(copyPass.peekAtTopCard())){
                    hand.cards.remove(copyPass.peekAtTopCard());
                    copyPass.removeTopCard();
                }
            }
        }
    }

    public boolean checkIfCardinHand(Card card){
        for(Card c: hand.cards){
            if(c.equals(card)){
                return true;
            }
        }
        return false;
    }

    public boolean checkIfFaceInHand(Rank face){
        for(Card c: hand.cards){
            if(c.getRank().equals(face)){
                return true;
            }
        }
        return false;
    }

}
