package edu.up.cs301.hearts;

import android.util.Log;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;


/**
 * This class checks legality of players moves and finds the winner of the round.
 * Created by emmasoriano on 10/19/17.
 */

public class HeartsLocalGame extends LocalGame {
    HeartsGameState currentGame;

    private final static int WIN_TARGET = 100;

    /**
     * Constructor for the HeartsLocalGame.
     */
    public HeartsLocalGame(){
        Log.i("HeartsLocalGame", "creating game");
        // create the state for the beginning of the game
        currentGame = new HeartsGameState();//1,"human");

    }

    /**
     *
     * validCard method checks if the card played by currentPlayer is legal
     *
     */
    public boolean validCard(Card card){

        boolean valid = false;

        //check if they played a card of the same suit as first card played,
        if(card.getSuit().equals(currentGame.table.cardsPlayed[0].getSuit())){
            valid = true;
        }
        // if not, check if they have a card of that suit,
        else{
            for (int i = 0; i < currentGame.piles[currentGame.toPlay].size(); i++) {
                Card c = currentGame.piles[currentGame.toPlay].cardAt(i);
               if(c.getSuit().equals(currentGame.table.cardsPlayed[0].getSuit())){
                   valid = false;
               }
           }
        }

        return valid;
    }

    /**
     *  validTurn method checks if its legal for a player to play a card
     *
     */
    public boolean validTurn(GamePlayer player){
        int idx = this.getPlayerIdx(player);
        if(idx == currentGame.toPlay) {
            return true;
        }
        else{
            return false;
        }
    }

//    /**
//     * checks who's turn it is to play a card
//     */
//    public GamePlayer checkTurn(){
//        return currentGame.players[currentGame.toPlay];
//    }

    /**
     *   The winRound method determines which player won the round
     *   and sets their isWinner boolean to true.
     */
    public void winRound(){
        //find suit of first card played
        Suit suit = currentGame.table.cardsPlayed[0].getSuit();
        Rank highestFace = currentGame.table.cardsPlayed[0].getRank();
/*
        //find highest card of suit played
        for(int i=0; i<currentGame.table.cardsPlayed.length; i++){
            if(currentGame.table.cardsPlayed[i].getCardValue()>highestFace){
             highestFace = currentGame.table.cardsPlayed[i].getCardValue();
            }
        }
        */

        //find which player played that card
        Card winningCard = new Card(highestFace,suit);
        for (int i = 0; i < players.length; i++) {
            if (currentGame.piles[i].containsCard(winningCard)) {
                currentGame.toPlay = i;
            }
        }
//        for(GamePlayer p: currentGame.players){
//
//            if(p.checkIfCardInHand(winningCard)){
//                p.setIsWinner(true);
//            }
//        }

    }

    public void CardPass(){

        /*

        //ask user to select three cards then hit "pass" button
        // TODO Fix this
        if(false){
            //have a round int that tells us which way to pass
            if(currentGame.round == 0) {
                //pass right
                currentGame.players[0].threeCardPass(currentGame.players[0].getMyPass(),currentGame.players[1]);
                currentGame.players[1].threeCardPass(currentGame.players[1].getMyPass(),currentGame.players[2]);
                currentGame.players[2].threeCardPass(currentGame.players[2].getMyPass(),currentGame.players[3]);
                currentGame.players[3].threeCardPass(currentGame.players[3].getMyPass(),currentGame.players[0]);

            }

            if(currentGame.round == 1) {
                //pass right
                currentGame.players[0].threeCardPass(currentGame.players[0].getMyPass(),currentGame.players[3]);
                currentGame.players[1].threeCardPass(currentGame.players[1].getMyPass(),currentGame.players[0]);
                currentGame.players[2].threeCardPass(currentGame.players[2].getMyPass(),currentGame.players[1]);
                currentGame.players[3].threeCardPass(currentGame.players[3].getMyPass(),currentGame.players[2]);

            }

            if(currentGame.round == 2) {
                //pass across table
                currentGame.players[0].threeCardPass(currentGame.players[0].getMyPass(),currentGame.players[2]);
                currentGame.players[1].threeCardPass(currentGame.players[1].getMyPass(),currentGame.players[3]);
                currentGame.players[2].threeCardPass(currentGame.players[2].getMyPass(),currentGame.players[0]);
                currentGame.players[3].threeCardPass(currentGame.players[3].getMyPass(),currentGame.players[1]);

            }

        }
        */

    }

    public int calculatePoints(){
        int points = 0;

        for(Card c: currentGame.table.cardsPlayed){
            //add one point each time a heart is on the table
            if(c.getSuit().equals(Suit.Heart)){
             points++;
            }
            //add 13 points if the queen of spades is on the table
            else if(c.getSuit().equals(Suit.Spade) && c.getRank().equals(Rank.QUEEN)){
                points=+13;
            }
        }

        return points;
    }

/*
    public void updateScore(){
        int points = calculatePoints();
        for(int i = 0; i<players.length; i++){
            //if(currentGame.players[i].isWinner == true){
            //    currentGame.players[i].setScore(points);
            //}
        }
    }

*/
    protected void sendUpdatedStateTo(GamePlayer p) {
        int thisPlayerNum = this.getPlayerIdx(p);
        HeartsGameState tempState = new HeartsGameState(currentGame);
        // need to null out some stuff in state
        p.sendInfo(tempState);
    }

    protected boolean canMove(int playerIdx) {
        return false;
    }

    protected String checkIfGameOver() {
        return null;
    }

    protected boolean makeMove(GameAction action) {
        return false;
    }
}
