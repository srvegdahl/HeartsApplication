package edu.up.cs301.hearts;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;

/**
 * This class calculates points earned by players and updates their current scores
 * Created by emmasoriano on 10/19/17.
 */

public class Scoring {
    int score = 0;
    CardDeck gameDeck;
    //constructor
    public Scoring(Table playedCards){
        //Calculates the score of the round
        Card queen = new Card(Rank.QUEEN, Suit.Spade);
        //iterate through cards on table
        for(int i=0; i<playedCards.cardsPlayed.length;i++) {
            //check if each card is the queen of spades
            if (playedCards.cardsPlayed[i] == queen){
                score = score + 13;
            }
            //check if each card is a heart
            for (int j = 0; j < 13; j++) {
                if (playedCards.cardsPlayed[i].getSuit().equals(Suit.Heart)) {
                    score++;
                }
            }
        }

    }


    public void updateScores(HeartsPlayer p1, HeartsPlayer p2, HeartsPlayer p3, HeartsPlayer p4){
        //gets the player who won the round and adds the points won from the round to their score
        if(p1.isWinner == true){
            p1.score = p1.score + score;
            p1.isWinner = false;
        }
        else if(p2.isWinner == true){
            p2.score = p2.score + score;
            p2.isWinner = false;
        }
        else if(p3.isWinner == true){
            p3.score = p3.score + score;
            p3.isWinner = false;
        }
        else if(p4.isWinner == true){
            p4.score = p4.score + score;
            p4.isWinner = false;
        }


        //if advanced update GUI with scores
        //else keep GUI same
    }


}
