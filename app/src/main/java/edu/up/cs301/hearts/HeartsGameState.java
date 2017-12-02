package edu.up.cs301.hearts;


import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.infoMsg.GameState;


/**
 * Contains the state of a Hearts game.  Sent by the game when
 * a player wants to enquire about the state of the game.  (E.g., to display
 * it, or to help figure out its next move.)
 *
 * @author Steven R. Vegdahl
 * @contributor Emma Soriano, Chris Lytle
 * @version October 2017
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
    public boolean GameOver = false;


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
    public CardDeck[] piles;

//TODO create scoring method and keep track/update trick
    private int[] Scores = new int[4];
    public int Trick = 0;


    /**
     * Constructor for objects of class HeartsGameState. Initializes for the beginning of the
     * game, with a random player as the first to turn card
     */
    public HeartsGameState() {
        CardDeck defaultDeck=new CardDeck();
        defaultDeck.add52();
        defaultDeck.shuffle();

        // initialize the decks as follows:
        piles = new CardDeck[4];
        piles[0] = new CardDeck(); // create empty deck
        piles[1] = new CardDeck(); // create empty deck
        piles[2] = new CardDeck(); // create empty deck
        piles[3] = new CardDeck(); // create empty deck

        int counter = 0;
        for(int i=0; i<defaultDeck.cards.size();i++){
            piles[counter].add(defaultDeck.get(i));
            if(i%14==0&&counter!=3){counter++;}
        }

        //initialize CurrentPlayerIndex
        hasTwoOfClubs();

    }

    /**
     * Copy constructor for objects of class HeartsGameState. Makes a copy of the given state
     *
     * @param orig the state to be copied
     */
    public HeartsGameState(HeartsGameState orig) {
        // set index of player whose turn it is
        CurrentPlayerIndex = orig.CurrentPlayerIndex;
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
        //TODO fix main menu so that it asks user to select difficulty level
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
        if (num < 0 || num >= 4) return null;
        return piles[num];
    }

    /**
     * Tells which player's turn it is.
     *09
     * @return the index (0 or 1) of the player whose turn it is.
     */
    public int getCurrentPlayerIndex() {
        return CurrentPlayerIndex;
    }

    /**
     *
     * @return
     */
    public void hasTwoOfClubs(){
        Card twoClubs = new Card(Rank.TWO, Suit.Club);
        int num = 0;
        for(int i = 0; i<players.length;i++){
            if(piles[i].equals(twoClubs)){
                num= i;
                setCurrentPlayer(i);
            }
        }
    }


    public int getPlayerIndex(GamePlayer p){
        for(int i =0; i<players.length; i++){
            if(players[i].equals(p)){
                return i;
            }
        }
        return -1;
    }

}