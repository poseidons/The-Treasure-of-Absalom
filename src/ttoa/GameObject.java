package ttoa;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Base class for all other classes to be used in the game (except for Game and Driver).
 * All other classes will inherit from it eventually and use the init, update, and render
 * methods plus the debug flag.
 *
 * @author Tucker Lein
 */
public class GameObject {
    public static boolean debug = false; //determines if debug settings are on or off

    /**
     * Base init method, currently empty
     *
     * @throws SlickException
     */
    public void init() throws SlickException {
    }

    /**
     * Base update method, currently empty
     *
     * @param gc GameContainer object
     * @param delta The amount of time thats passed since last update in milliseconds
     * @throws SlickException
     */
    public void update(GameContainer gc, int delta) throws SlickException {
    }

    /**
     * Base render method, currently empty
     *
     * @param gc GameContainer object
     * @param g Graphics object
     * @throws SlickException
     */
    public void render(GameContainer gc, Graphics g) throws SlickException {
    }
}
