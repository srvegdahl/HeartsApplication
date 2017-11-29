package edu.up.cs301.hearts;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;
//import edu.up.cs301.slapjack.SJComputerPlayer;

import android.graphics.Color;

/**
 * this is the primary activity for Hearts game
 *
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class HeartsMainActivity extends GameMainActivity {

    public static final int PORT_NUMBER = 7752;

    /** a hearts game for two players. The default is human vs. computer */
    @Override
    public GameConfig createDefaultConfig() {

        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        playerTypes.add(new GamePlayerType("human player (green)") {
            public GamePlayer createPlayer(String name) {
                return new HeartsHumanPlayer(name);
            }});
        playerTypes.add(new GamePlayerType("human player (yellow)") {
            public GamePlayer createPlayer(String name) {
                return new HeartsHumanPlayer(name);
            }
        });
        playerTypes.add(new GamePlayerType("computer player (easy)") {
            public GamePlayer createPlayer(String name) {
                return new EasyAI(name);
            }
        });
        //playerTypes.add(new GamePlayerType("computer player (slow)") {
           // public GamePlayer createPlayer(String name) {
               // return new HardAI(name);
           // }
      //  });

        // Create a game configuration class for Hearts
        GameConfig defaultConfig = new GameConfig(playerTypes, 4, 4, "Hearts", PORT_NUMBER);

        // Add the default players
        defaultConfig.addPlayer("Human", 0);
        defaultConfig.addPlayer("Computer1", 2);
        defaultConfig.addPlayer("Computer2", 2);
        defaultConfig.addPlayer("Computer3", 2);


        // Set the initial information for the remote player
        defaultConfig.setRemoteData("Guest", "", 1);

        //done!
        return defaultConfig;
    }//createDefaultConfig

    @Override
    public LocalGame createLocalGame() {
        return new HeartsLocalGame();
    }
}
