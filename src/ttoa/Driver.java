package ttoa;

import org.newdawn.slick.*;

/**
 * Driver class, creates a Game Object and mounts it in the pre-built
 * AppGameContainer which handles basic game loops.
 *
 * @author Tucker Lein
 */
public class Driver {

    /**
     * Main class, runs game.
     *
     * @param args
     * @throws SlickException
     */
    public static void main(String[] args) throws SlickException {
         Game g = new Game(); //creates Game object
         AppGameContainer app = new AppGameContainer(g); //creates AppGameContainer and pass it the Game object
         app.setIcons( new String[] {"res/32x32.png", "res/24x24.png", "res/16x16.png"} );  //load icons
         app.setDisplayMode(g.getWindowWidth(), g.getWindowHeight(), false); //Sets the width, height, and fullscreen option of window
         app.start(); //start the game loop
    }
}