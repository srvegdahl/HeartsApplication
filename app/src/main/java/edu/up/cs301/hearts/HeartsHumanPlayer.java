package edu.up.cs301.hearts;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.*;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;
import edu.up.cs301.slapjack.Deck;
import edu.up.cs301.slapjack.SJPlayAction;
import edu.up.cs301.slapjack.SJSlapAction;
import edu.up.cs301.slapjack.SJState;

import static edu.up.cs301.game.R.drawable.card_2c;

/**
 * Created by emmasoriano on 11/6/17.
 */



/**
 *
 *
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class HeartsHumanPlayer extends GameHumanPlayer implements Animator {



    private  boolean [] cardLocationBool;

    boolean drawMe=false;
    boolean doubleTap=false;
    Card cardToPlay;
    boolean singleTap=false;
    String currentPlayer="empty";
    boolean AI1HasPlayed= false;
    boolean AI2HasPlayed= false;
    boolean AI3HasPlayed= false;
    boolean HumanHasPlayed= false;

    //instance variables added from HeartsPlayer class
    CardDeck hand;
    Card[] collection;
    Card[] myPass = new Card[2];
    boolean myTurn = false;

    boolean isWinner = false;
    boolean hasTwoOfClubs = false;
    int score = 0;
    String name;

    int count=0;
    Canvas g=new Canvas();
    Card AI1ToPlay;


    private  Card [] humanCards;

    private  RectF [] cardLocationY;

    private Card c;
    private Card selectedCard;


    private  RectF [] cardLocation;

    TextView turnId;

    // sizes and locations of card decks and cards, expressed as percentages
    // of the screen height and width
    private final static float CARD_HEIGHT_PERCENT = 40; // height of a card
    private final static float CARD_WIDTH_PERCENT = 17; // width of a card
    private final static float LEFT_BORDER_PERCENT = 4; // width of left border
    private final static float RIGHT_BORDER_PERCENT = 20; // width of right border
    private final static float VERTICAL_BORDER_PERCENT = 4; // width of top/bottom borders

    // our game state
    protected HeartsGameState state;

    // our activity
    private Activity myActivity;

    // the amination surface
    private AnimationSurface surface;

    // the background color
    private int backgroundColor;

    public boolean checkIfCardinHand(Card card){
        if((card != null)&&(hand != null)){
            for(Card c: hand.cards){
                if(c.equals(card)){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * constructor
     *
     * @param name
     *        the player's name
     */
    public HeartsHumanPlayer(String name){
        super(name);

        //determines if player has the starting card
        Card twoOfClubs = new Card(Rank.TWO, Suit.Club);
        if (checkIfCardinHand(twoOfClubs)){
            hasTwoOfClubs= true;
        }
    }

    /**
     * callback method: we have received a message from the game
     *
     * @param info
     *        the message we have received from the game
     */
    @Override
    public void receiveInfo(GameInfo info) {
        Log.i("HeartsHumanPlayer", "receiving updated state ("+info.getClass()+")");
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
            Log.i("human player", "receiving");
        }
    }

    /**
     * call-back method: called whenever the GUI has changed (e.g., at the beginning
     * of the game, or when the screen orientation changes).
     *
     * @param activity
     *        the current activity
     */
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
        if (state != null) {receiveInfo(state);}

    }

    /**
     * @return the top GUI view
     */
    @Override
    public View getTopView() {

        return myActivity.findViewById(R.id.top_gui_layout);
    }

    /**
     * @return
     *        the amimation interval, in milliseconds
     */
    public int interval() {
        // 1/20 of a second
        return 50;
    }

    /**
     * @return
     *        the background color
     */
    public int backgroundColor() {
        return Color.GREEN;
    }

    /**
     * @return
     *        whether the animation should be paused
     */
    public boolean doPause() {
        return false;
    }

    /**
     * @return
     *        whether the animation should be terminated
     */
    public boolean doQuit() {
        return false;
    }


    public void addCards(RectF midTopLocation){


        for (int n=0; n<=13; n++) {

            if (cardLocation[n].contains(midTopLocation)) {

                cardLocation[n] = midTopLocation;
                //System.out.println("added mid top loc to array  " + midTopLocation);
            }
        }
    }

    /**
     * callback-method: we have gotten an animation "tick"; redraw the screen image:
     * - the middle deck, with the top card face-up, others face-down
     * - the two players' decks, with all cards face-down
     * - a red bar to indicate whose turn it is
     *
     * @param g
     *        the canvas on which we are to draw
     */

    public void tick(Canvas g) {

        // ignore if we have not yet received the game state
        if (state == null) return;


        CardDeck myDeck = state.getDeck(0);
        CardDeck AI1Deck = state.getDeck(1);

        if (cardLocation==null&&cardLocationY==null&&humanCards==null) {
            c = state.getDeck(0).peekAtPlayerCard();// currently one of your own cards
            AI1ToPlay = state.getDeck(1).peekAtPlayerCard();// currently one of your own cards
            Card AI2ToPlay = state.getDeck(2).peekAtPlayerCard();// currently one of your own cards
            Card AI3ToPlay = state.getDeck(3).peekAtPlayerCard();// currently one of your own cards

            cardLocation = new RectF[14];
            cardLocationY = new RectF[14];
            humanCards=new Card[14];
            cardLocationBool = new boolean[14];

            //System.out.println("touch squares havent been initalized  ");

            // get the height and width of the animation surface
            int height = surface.getHeight();
            int width = surface.getWidth();


            /***********************************************************************/
            // draw the middle card-pile

            for (int row = 1; row < 8; row++) {
                for (int col = 1; col < 3; col++) {
                    humanCards[count]=myDeck.get(count);
                    float rectRight = 210;
                    float rectTop = 1000;
                    float rectBottom = 1300;
                    float rectLeft = 60;

                    rectLeft = rectLeft + ((row - 1) * 200);
                    rectRight = rectRight + ((row - 1) * 200);
                    rectTop = rectTop + ((col - 1) * 325);
                    rectBottom = rectBottom + ((col - 1) * 325);

                    cardLocation[count] = new RectF(rectLeft, rectTop, rectRight, rectBottom);

                    float rectRightY = 230;
                    float rectTopY = 990;
                    float rectBottomY = 1320;
                    float rectLeftY = 50;

                    rectLeftY = rectLeftY + ((row - 1) * 200);
                    rectRightY = rectRightY + ((row - 1) * 200);
                    rectTopY = rectTopY + ((col - 1) * 325);
                    rectBottomY = rectBottomY + ((col - 1) * 325);

                    cardLocationY[count] = new RectF(rectLeftY, rectTopY, rectRightY, rectBottomY);
                    count++;

                }
            }
        }


        Paint p = new Paint();
        p.setColor(Color.YELLOW);

        if ((count==cardLocation.length)) {
            //g.drawRect(cardLocationY[count - 1], p);
        }else{
            g.drawRect(cardLocationY[count], p);
            //drawCard(g, cardLocation[count], selectedCard);
            drawCard(g, cardLocation[count], humanCards[count]);
        }


        //Draw the card the human played


        float HrectLeft = 650;
        float HrectRight = 800;
        float HrectTop = 500;
        float HrectBottom = 700;


        c = state.getDeck(0).peekAtPlayerCard();// currently one of your own cards

        RectF HcardPile = new RectF(HrectLeft, HrectTop, HrectRight, HrectBottom);
        if (HumanHasPlayed)drawCard(g, HcardPile, c);
        else drawCardBacks(g, HcardPile, 0, 0, 1);


        // draw the First AI's played card

        float AI1rectLeft = 250;
        float AI1rectRight = 400;
        float AI1rectTop = 300;
        float AI1rectBottom = 500;

        EasyAI AIofTheEasyKind= new EasyAI("AI1");// this is naughty, we need to properly declare the Easy AI
        AI1HasPlayed=true;
        AI1ToPlay = state.getDeck(1).peekAtPlayerCard();// currently one of your own cards

        RectF aI1cardPile = new RectF(AI1rectLeft, AI1rectTop, AI1rectRight, AI1rectBottom);
        if (AI1HasPlayed==true)drawCard(g, aI1cardPile, AIofTheEasyKind.playCard());//AI1ToPlay);
        else drawCardBacks(g, aI1cardPile, 0, 0, 1);


        //drawCard(g, aI1cardPile, c);


        // draw the  second AI's played card
        float AI2rectLeft = 1050;
        float AI2rectRight = 1200;
        float AI2rectTop = 300;
        float AI2rectBottom = 500;

        AI2HasPlayed=true;

        c = state.getDeck(2).peekAtPlayerCard();// currently one of your own cards

        RectF aI2cardPile = new RectF(AI2rectLeft, AI2rectTop, AI2rectRight, AI2rectBottom);
        if (AI2HasPlayed)drawCard(g, aI2cardPile, c);
        else drawCardBacks(g, aI2cardPile, 0, 0, 1);



        // draw the third AI's played card

        float AI3rectLeft = 650;
        float AI3rectRight = 800;
        float AI3rectTop = 100;
        float AI3rectBottom = 300;


        AI3HasPlayed=true;

        c = state.getDeck(3).peekAtPlayerCard();// currently one of your own cards

        RectF aI3cardPile = new RectF(AI3rectLeft, AI3rectTop, AI3rectRight, AI3rectBottom);
        if (AI3HasPlayed)drawCard(g, aI3cardPile, c);
        else drawCardBacks(g, aI3cardPile, 0, 0, 1);



        for(int i=0; i<Math.min(13,myDeck.size());i++) {
            //Log.i(" drawing card ",""+i);
            drawCard(g, cardLocation[i],humanCards[i] );//myDeck.get(i));


            //drawCardBacks(g,checkCardRect,checkCardRect,checkCardRect,3);

            //Log.i(" finished card draw ",""+i);
        }
        if (doubleTap==true){
            //draw selected card in played human spot
            drawCard(g, HcardPile, selectedCard);
            p = new Paint();
            p.setColor(Color.YELLOW);
            RectF temp = cardLocationY[count];

            g.drawRect(cardLocationY[count], p);

            cardLocationY[count]=temp;
            cardLocationBool[count]=true;


            //humanCards[count]=null;
            drawMe=true;

            // String currentPlayer="human";

            // turnId.setText("It is "+currentPlayer+"'s turn.");

        }
        if (count==14||count==12) {

            p = new Paint();
            p.setColor(Color.GREEN);
            g.drawRect(cardLocationY[12], p);
            drawCard(g, cardLocation[12],humanCards[12] );
        }
        for(int i=0; i<Math.min(13,myDeck.size());i++) {
            if (cardLocationBool[i] == true) {
                p = new Paint();
                p.setColor(Color.GREEN);
                g.drawRect(cardLocationY[i], p);
            }
        }
/*
      reset();
      if (drawMe==true){
         g.drawRect(cardLocationY[count], p);
      }
*/
    }



    /**
     * @return
     *        the rectangle that represents the location on the drawing
     *        surface where the top card in the opponent's deck is to
     *        be drawn
     */
    private RectF opponentTopCardLocation() {
        // near the left-bottom of the drawing surface, based on the height
        // and width, and the percentages defined above
        int width = surface.getWidth();
        int height = surface.getHeight();
        return new RectF(LEFT_BORDER_PERCENT*width/100f,
                (100-VERTICAL_BORDER_PERCENT-CARD_HEIGHT_PERCENT)*height/100f,
                (LEFT_BORDER_PERCENT+CARD_WIDTH_PERCENT)*width/100f,
                (100-VERTICAL_BORDER_PERCENT)*height/100f);
    }

    /**
     * @return
     *        the rectangle that represents the location on the drawing
     *        surface where the top card in the current player's deck is to
     *        be drawn
     */
    private RectF thisPlayerTopCardLocation() {
        // near the right-bottom of the drawing surface, based on the height
        // and width, and the percentages defined above
        int width = surface.getWidth();
        int height = surface.getHeight();
        return new RectF((100-RIGHT_BORDER_PERCENT-CARD_WIDTH_PERCENT)*width/100f,
                (100-VERTICAL_BORDER_PERCENT-CARD_HEIGHT_PERCENT)*height/100f,
                (100-RIGHT_BORDER_PERCENT)*width/100f,
                (100-VERTICAL_BORDER_PERCENT)*height/100f);
    }

    /**
     * @return
     *        the rectangle that represents the location on the drawing
     *        surface where the top card in the middle pile is to
     *        be drawn
     */
    private RectF middlePileTopCardLocation() {
        // near the middle-bottom of the drawing surface, based on the height
        // and width, and the percentages defined above
        int height = surface.getHeight();
        int width = surface.getWidth();
        float rectLeft = (100-CARD_WIDTH_PERCENT+LEFT_BORDER_PERCENT-RIGHT_BORDER_PERCENT)*width/200;
        float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
        float rectTop = (100-VERTICAL_BORDER_PERCENT-CARD_HEIGHT_PERCENT)*height/100f;
        float rectBottom = (100-VERTICAL_BORDER_PERCENT)*height/100f;
        return new RectF(rectLeft, rectTop, rectRight, rectBottom);
    }

    /**
     * draws a sequence of card-backs, each offset a bit from the previous one, so that all can be
     * seen to some extent
     *
     * @param g
     *        the canvas to draw on
     * @param topRect
     *        the rectangle that defines the location of the top card (and the size of all
     *        the cards
     * @param deltaX
     *        the horizontal change between the drawing position of two consecutive cards
     * @param deltaY
     *        the vertical change between the drawing position of two consecutive cards
     * @param numCards
     *        the number of card-backs to draw
     */
    private void drawCardBacks(Canvas g, RectF topRect, float deltaX, float deltaY,
                               int numCards) {
        // loop through from back to front, drawing a card-back in each location
        for (int i = numCards-1; i >= 0; i--) {
            // determine theh position of this card's top/left corner
            float left = topRect.left + i*deltaX;
            float top = topRect.top + i*deltaY;
            // draw a card-back (hence null) into the appropriate rectangle
            drawCard(g,
                    new RectF(left, top, left + topRect.width(), top + topRect.height()),
                    null);
        }
    }

    /**
     * callback method: we have received a touch on the animation surface
     *
     * @param event
     *        the motion-event
     */
    public void onTouch(MotionEvent event) {
        boolean dontFlash= false;
        // ignore everything except down-touch events
        if (event.getAction() != MotionEvent.ACTION_DOWN) return;

        // get the location of the touch on the surface
        float x = (int) event.getX();
        float y = (int) event.getY();

        // determine whether the touch occurred on the top-card of either
        // the player's pile or the middle pile
        RectF myTopCardLoc = thisPlayerTopCardLocation();
        RectF middleTopCardLoc = middlePileTopCardLocation();
        if(cardLocation != null){
            for (int n=0; n<=13; n++) {
                if (cardLocation[n].contains(x, y)) {
                    dontFlash=true;
                    selectedCard=humanCards[n];
                    if (singleTap==false){
                        selectedCard=humanCards[n];
                        singleTap=true;
                        doubleTap=false;
                    }else if (cardToPlay==selectedCard){
                        //draw card to play in human table spot
                        doubleTap=true;
                        cardLocationBool[n]=true;
                        // isLegalMove(cardToPlay);
                        singleTap=false;
                    }
                    else{

                        singleTap=true;
                        selectedCard=humanCards[n];
                        doubleTap=false;
                    }


                    cardToPlay=humanCards[n];


                    //System.out.println("I've been touched!   " + cardLocation[n]);
                    RectF touched = cardLocation[n];
                    count =n;
                    //highlightCard(touched, count);


                }
            }
        }

        if (dontFlash==false){ surface.flash(Color.RED, 50);}
    }

    public void highlightCard(RectF initcard, int count){

        Paint p = new Paint();
        p.setColor(Color.YELLOW);
        g.drawRect(cardLocationY[count],p);

    }

    /**
     * draws a card on the canvas; if the card is null, draw a card-back
     *
     * @param g
     *        the canvas object
     * @param rect
     *        a rectangle defining the location to draw the card
     * @param c
     *        the card to draw; if null, a card-back is drawn
     */
    private static void drawCard(Canvas g, RectF rect, edu.up.cs301.card.Card c) {
        if (c == null) {
            // null: draw a card-back, consisting of a blue card
            // with a white line near the border. We implement this
            // by drawing 3 concentric rectangles:
            // - blue, full-size
            // - white, slightly smaller
            // - blue, even slightly smaller
            Paint white = new Paint();
            white.setColor(Color.WHITE);
            Paint blue = new Paint();
            blue.setColor(Color.BLUE);
            RectF inner1 = scaledBy(rect, 0.96f); // scaled by 96%
            RectF inner2 = scaledBy(rect, 0.98f); // scaled by 98%
            g.drawRect(rect, blue); // outer rectangle: blue
            g.drawRect(inner2, white); // middle rectangle: white
            g.drawRect(inner1, blue); // inner rectangle: blue
        }
        else {
            // just draw the card
            c.drawOn(g, rect);
        }
    }

    /**
     * scales a rectangle, moving all edges with respect to its center
     *
     * @param rect
     *        the original rectangle
     * @param factor
     *        the scaling factor
     * @return
     *        the scaled rectangle
     */
    private static RectF scaledBy(RectF rect, float factor) {
        // compute the edge locations of the original rectangle, but with
        // the middle of the rectangle moved to the origin
        float midX = (rect.left+rect.right)/2;
        float midY = (rect.top+rect.bottom)/2;
        float left = rect.left-midX;
        float right = rect.right-midX;
        float top = rect.top-midY;
        float bottom = rect.bottom-midY;

        // scale each side; move back so that center is in original location
        left = left*factor + midX;
        right = right*factor + midX;
        top = top*factor + midY;
        bottom = bottom*factor + midY;

        // create/return the new rectangle
        return new RectF(left, top, right, bottom);
    }



//Added methods from HeartsPlayer class

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
            hand.cards.add(c);
        }
        collection= (Card[]) hand.cards.toArray();
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

    public void threeCardPass(CardDeck pass, GamePlayer p) {
        //pass cards to appropriate player
        for (Card c : pass.cards) {
            p.hand.cards.add(c);
        }

        //remove cards passed to another player from hand
        for (Card c : hand.cards) {
            for (Card i : pass.cards) {
                if (c.equals(i)) {
                    hand.cards.remove(i);
                }
            }
        }
    }

    public boolean checkIfCardInHand(Card card){
        for(Card c: hand.cards){
            if(c.equals(card)){
                return true;
            }
        }
        return false;
    }




}