package edu.up.cs301.hearts;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

/**
 * Updated by S. Seydlitz on 11/17/17
 */

public class EasyAI extends GameComputerPlayer implements Animator {

    //variables
    CardDeck currentHand;
    Table table = new Table();
    Suit baseSuit = table.getSuitIndex();
    //boolean heartsPlayed = false;
    Card chosenCard;
    Card[] collection;
    Card[] myPass = new Card[3];
    boolean myTurn = false;
    boolean isWinner = false;
    boolean hasTwoOfClubs = false;
    int score = 0;
    String name;
    // our game state
    protected HeartsGameState state;

    // our activity
    private Activity myActivity;

    // the amination surface
    private AnimationSurface surface;


    public EasyAI(String playerName) {
        super(playerName);
    }

    public void setAsGui(GameMainActivity activity) {

        // remember the activity
        myActivity = activity;

        // Load the layout resource for the new configuration
        activity.setContentView(R.layout.sj_human_player);// change to hearts

        // link the animator (this object) to the animation surface
        surface = (AnimationSurface) myActivity
                .findViewById(R.id.animation_surface);
        surface.setAnimator(this);

        // read in the card images
        edu.up.cs301.card.Card.initImages(activity);

        // if the state is not null, simulate having just received the state so that
        // any state-related processing is done
        if (state != null) {
            receiveInfo(state);
        }

    }

    public void receiveInfo(GameInfo info) {
        Log.i("HeartsComputerPlayer", "receiving updated state ("+info.getClass()+")");
        if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
            // if we had an out-of-turn or illegal move, flash the screen
            surface.flash(Color.RED, 50);
        }
        else if (!(info instanceof HeartsGameState)) {
            // otherwise, if it's not a game-state message, ignore
            return;
        }
        else {
            // it's a game-state object: update the state. Since we have an animation
            // going, there is no need to explicitly display anything. That will happen
            // at the next animation-tick, which should occur within 1/20 of a second
            this.state = (HeartsGameState)info;
            Log.i("computer player", "receiving");
        }

        int ind = state.CurrentPlayerIndex;
        //I need the current player so I can call their hand!!!!!!
        currentHand = new CardDeck(state.piles[ind]);
    }

    public void strategy() {

        Rank[] ranks = new Rank[13];
        Suit[] suits = new Suit[4];
        ranks[0].fromChar('2');
        ranks[1].fromChar('3');
        ranks[2].fromChar('4');
        ranks[3].fromChar('5');
        ranks[4].fromChar('6');
        ranks[5].fromChar('7');
        ranks[6].fromChar('8');
        ranks[7].fromChar('9');
        ranks[8].fromChar('T');
        ranks[9].fromChar('J');
        ranks[10].fromChar('Q');
        ranks[11].fromChar('K');
        ranks[12].fromChar('A');
        suits[0].fromChar('C');
        suits[1].fromChar('S');
        suits[2].fromChar('D');
        suits[3].fromChar('H');

        //pick a card at random from EasyAI's card deck
        //Remember, there are three different AI's, so there are three different decks to keep track of
        Random rand = new Random();
        //x is rank, y is suit
        int x = rand.nextInt(14 - 0) + 0;
        int y = rand.nextInt(5 - 0 + 0);

        if(baseSuit==null){
            chosenCard = new Card(ranks[x],suits[y]);
        }//our card will be first

        //check and see if the AI player has a card of the suit that was originally played. If not, play any card
        else if(checkIfFaceInHand(baseSuit)==true){
            chosenCard = new Card(ranks[x], baseSuit);
        }
        else{
            chosenCard = new Card(ranks[x],suits[y]);
        }

    }

    public Card playCard() {
        if(checkIfCardinHand(chosenCard)==true){
            //if they have this card, then take it away from the AI player's hand!
            //to do this we need to make sure that we can get the array of cards in the player's hand!
            currentHand.remove(chosenCard);
            sleep(1000);
            return chosenCard;
        } else {
            strategy();
        }
        return null;
    }

//Added methods from HeartsPlayer class

    public Card[] getMyPass(){
        return myPass;
    }

    public Card[] getHand(){
        return (Card[]) currentHand.cards.toArray();
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

    /*
    /**
     * Set hand to given list of cards
     * @param initHand - shouldn't be more then


    public void setHand(Card[] initHand){
        int i;
        for (i = 0; i < initHand.length; i++){
            hand.add(initHand[i]);
        }
        collection= (Card[]) currentHand.cards.toArray();
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

    /*
    public void threeCardPass(Card[] pass, HeartsPlayer p){
        //pass cards to appropriate player
        p.setHand(pass);

        //remove cards passed to another player from hand
        for(Card c: hand){
            for(int i=0; i<pass.length; i++){
                if(c.equals(pass[i])){
                    hand.cards.remove(pass[i]);
                }
            }
        }
    }


    */

    /**
     * @return
     * 		the amimation interval, in milliseconds
     */
    public int interval() {
        // 1/20 of a second
        return 50;
    }

    /**
     * @return
     * 		the background color
     */
    public int backgroundColor() {
        return Color.GREEN;
    }

    /**
     * @return
     * 		whether the animation should be paused
     */
    public boolean doPause() {
        return false;
    }

    /**
     * @return
     * 		whether the animation should be terminated
     */
    public boolean doQuit() {
        return false;
    }

    public boolean checkIfCardinHand(Card card){
        for(Card c: currentHand.cards){
            if(c.equals(card)){
                return true;
            }
        }
        return false;
    }

    public boolean checkIfFaceInHand(Suit face){
        for(Card c: currentHand.cards){
            if(c.getSuit().equals(face)){
                return true;
            }
        }
        return false;
    }

    public void tick(Canvas g) {
        // ignore if we have not yet received the game state
        if (state == null) return;
        //getCanvas(g);
    }

    public void onTouch(MotionEvent me){
    }

}
