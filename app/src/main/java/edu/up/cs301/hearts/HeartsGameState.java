package edu.up.cs301.hearts;


import java.util.ArrayList;
import java.util.Arrays;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.Game;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Created by emmasoriano on 10/23/17.
 */

//package edu.up.cs301.slapjack;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Contains the state of a Slapjack game.  Sent by the game when
 * a player wants to enquire about the state of the game.  (E.g., to display
 * it, or to help figure out its next move.)
 *
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class HeartsGameState extends GameState {

    ///////////////////////////////////////////////////
    // ************** instance variables ************
    ///////////////////////////////////////////////////

    private static final long serialVersionUID = -8269749892027578792L;

    /*
    /**
     * HeartsGameState Constructor
     * @param d
     * @param user
     */

    public GamePlayer[] players = new GamePlayer[4];
    public Table table;
    private int[] Scores = new int[4];
    public boolean GameOver = false;
    public int Trick = 0;

    public GamePlayer CurrentPlayer;
    public int CurrentPlayerIndex;



    // Chosen difficulty
    //  - 0: EasyAIxxx
    //  - 1: HardAI
    protected int Difficulty;

    // the three piles of cards:
    //  - 0: pile for player 0
    //  - 1: pile for player 1
    //  - 2: pile for player 2
    //  - 3: pile for player 3
    //  - 4: the "up" pile, where the top card
    // Note that when players receive the state, all but the top card in all piles
    // are passed as null.
    public CardDeck[] piles;

    // whose turn is it to turn a card?
    public int toPlay;

    /**
     * Constructor for objects of class HeartsGameState. Initializes for the beginning of the
     * game, with a random player as the first to turn card
     */
    public HeartsGameState() {
        // randomly pick the player who starts
        //toPlay = (int)(2*Math.random());
        toPlay = hasTwoOfClubs();

        // initialize the decks as follows:
        // - each player deck (#0 and #1) gets half the cards, randomly
        //   selected
        // - the middle deck (#2) is empty
        piles = new CardDeck[4];
        piles[0] = new CardDeck(); // create empty deck
        piles[1] = new CardDeck(); // create empty deck
        piles[2] = new CardDeck(); // create empty deck
        piles[3] = new CardDeck(); // create empty deck
        piles[toPlay].add52(); // give all cards to player whose turn it is, in order
        piles[toPlay].shuffle(); // shuffle the cards
        // move cards to opponent, until to piles have ~same size
        int counter = 0;
        int i=0;
        do{
            if(counter==13){
                counter=0;
                i++;
            }
            piles[toPlay].moveTopCardTo(piles[i]);
            counter++;
        }while(counter<=4);
    }

    /**
     * Copy constructor for objects of class HeartsGameState. Makes a copy of the given state
     *
     * @param orig the state to be copied
     */
    public HeartsGameState(HeartsGameState orig) {
        // set index of player whose turn it is
        toPlay = orig.toPlay;
        // create new deck array, making copy of each deck
        piles = new CardDeck[4];
        piles[0] = new CardDeck(orig.piles[0]);
        piles[1] = new CardDeck(orig.piles[1]);
        piles[2] = new CardDeck(orig.piles[2]);
        piles[3] = new CardDeck(orig.piles[3]);

    }

    /**
     * Set AI difficulty
     * @param difficulty
     *  - 0 : EasyAIxxx
     *  - 1 : HardAI
     */
    public void setDifficulty(int difficulty){
        if((difficulty == 0)||(difficulty == 1)){
            this.Difficulty = difficulty;
        }
        else{
            return;
        }
    }

    /**
     * Set Current Player
     * @param index
     */
    public void setCurrentPlayer(int index){
        if((index >= 0)&&(index <= 3)){
            CurrentPlayer = players[index];
            CurrentPlayerIndex = index;

        }
    }

    /**
     * Set Game Over
     * @param b
     */
    public void setGameOver(boolean b){
        GameOver = b;
    }

    /**
     * Set Trick
     */
    public void setTrick(int trick){
        Trick = trick;
    }

    public int getDifficulty(){return Difficulty;}

    public int[] getScores(){
        return Scores;
    }

    public Table getTable(){
        return table;
    }

    /**
     * Changes the Current Player to the next player.
     */
    public void NextTurn(){
        if(CurrentPlayerIndex == 3){
            CurrentPlayer = players[0];
            CurrentPlayerIndex = 0;
        }
        else {
            CurrentPlayerIndex++;
            CurrentPlayer = players[CurrentPlayerIndex];
        }
    }

    /**
     * Gives the given deck.
     *
     * @return the deck for the given player, or the middle deck if the
     * index is 2
     */
    public CardDeck getDeck(int num) {
        if (num < 0 || num > 3) return null;
        return piles[num];
    }

    /**
     * Tells which player's turn it is.
     *09
     * @return the index (0 or 1) of the player whose turn it is.
     */
    public int toPlay() {
        return toPlay;
    }

    /**
     *
     * @return
     */
    public int hasTwoOfClubs(){
        Card twoClubs = new Card(Rank.TWO, Suit.Club);
        int num = 0;
        for(int i = 0; i<players.length;i++){
            //if(players[i].checkIfCardInHand(twoClubs)){
                //num= i;
            //}
        }
        return num;
    }


    /**
     * change whose move it is
     *
     * @param idx the index of the player whose move it now is
     */
    public void setToPlay(int idx) {
        toPlay = idx;
    }


}

    /**
     * Replaces all cards with null, except for the top card of deck 2
     */


/*
public class HeartsGameState extends GameState {

    // Declare Instance Variables
    public String userName;
    public GamePlayer[] players = new GamePlayer[4];
    public GamePlayer currentPlayer;
    public GamePlayer nextPlayer;
    public CardDeck deck;
    public int playerIndex;
    public int difficulty;
    public int[] currentScores;
    public int currentSuit;
    public int round;
    Table table;


    /**
     * HeartsGameState Constructor
     * @param d
     * @param user
     */
/*
    public HeartsGameState(int d, String user){
        //initialize variables
        difficulty = d;
        userName = user;
        setPlayers();
        setCurrentPlayer(players[0]);
        playerIndex = 0;
        currentScores[0] = 0;
        currentScores[1] = 0;
        currentScores[2] = 0;
        currentScores[3] = 0;
        currentSuit = 1;
        round = 0;
        table = new Table();
        //create a deck of cards
        deck = new CardDeck();


    }


    /**
     * Sets players to
     */
/*
    public void setPlayers(){
        int i;
        players[0] = new HeartsHumanPlayer(userName);
        for(i = 1; i <= 3; i++){
            if(difficulty == 0){
                EasyAIxxx newAI = new EasyAIxxx("Easy AI " + i);
                players[i] = newAI;
            }
            else{
                HardAI newAI = new HardAI("Hard AI " + i);
                players[i] = newAI;
            }
        }
    }


    /**
     * deals the players hands
     */
/*
    public void dealHands(){
        CardDeck[] hand = new CardDeck[4];
        int counter=0;
        deck.shuffle();
        CardDeck copyDeck = deck;
        for(int i= 0; i<deck.size();i++){
            if(i%13==0 && i!=0){
                counter++;
            }
            copyDeck.moveTopCardTo(hand[counter]);
        }

        for(int j = 0; j<players.length; j++){
            players[j].setHand(hand[j]);
        }

    }
//determine who starts
    /**
     * set a given player for who's turn it is
     * @param player
     */

/*

    /**
     * set given player's score
     * @param player
     * @param addScore
     */
   /*
    public void setPlayersScore(GamePlayer player, int addScore){
        //setScore(addScore);
    }


    /**
     * get round number
     * @return
     */
   /*
    public int getRound(){
        return round;
    }

}
*/