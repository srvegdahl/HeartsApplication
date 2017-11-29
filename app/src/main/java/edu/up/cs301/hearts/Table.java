package edu.up.cs301.hearts;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GamePlayer;

/**
 * Created by emmasoriano on 10/19/17.
 */

public class Table {

    public Card[] cardsPlayed = new Card[4];
    public Suit suit;

    //Constructor
    public Table(){
        //initialize "empty" table
        for(int i=0; i<4; i++){
            cardsPlayed[i]=null;
        }

    }

    public void addCard(Card card, GamePlayer GP){
        for(int i=0; i<4; i++){
            if(cardsPlayed[i] == null){
                cardsPlayed[i]=card;
                if(i == 0){
                    suit = card.getSuit();
                }
                break;
            }
        }
    }

    public Card[] getTable(){
        return cardsPlayed;
    }

    public Suit getSuitIndex(){
        return suit;
    }

    /**
     * Finds Highest Card and returns it
     * @return
     */
    public Card getHighestCard() {
        Card rtrnCard = cardsPlayed[0];
        int i;
        int tempInt;
        int currentHighestRank = cardsPlayed[0].getIntValue();
        for(i = 0; i < cardsPlayed.length; i++){
            if(cardsPlayed[i].getSuit().equals(suit)){
                 tempInt = cardsPlayed[i].getIntValue();
                if((currentHighestRank < tempInt)&&(!(cardsPlayed[i].equals(rtrnCard)))){
                    rtrnCard = cardsPlayed[i];
                    currentHighestRank = cardsPlayed[i].getIntValue();
                }
            }
        }
        return rtrnCard;
    }

    public void clearTable(){
        for(int i=0; i<4; i++){
            cardsPlayed[i]=null;
        }

    }
}
