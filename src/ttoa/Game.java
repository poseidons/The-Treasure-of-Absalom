package ttoa;

import org.newdawn.slick.*;
import java.util.ArrayList;

/**
 * Creates all values used in the game and calls upon each
 * init, update, and render method for all GameObject.
 * This and Driver are the only classes that do not
 * derive from GameObject.
 *
 * @author Tucker Lein
 */
public class Game extends BasicGame {
    ArrayList<GameObject> elements = new ArrayList<GameObject>(); //holds all top game elements, such as maps, and the player.
    private Harbor harbor = null; //harbor map - not yet worked into the elements ArrayList and managed accordingly.
    private static int windowWidth = 800; //width of the window
    private static int windowHeight = 600; //height of the window
    private NonPlayable nonPlayable = new NonPlayable(); //creates an instance of the NonPlayable class, used for its static variables.
    private Playable playable = new Playable(); //creates an instance of the Playable class, used for its static variables.

    /**
     * Constructor, calls the constructor of BasicGame which creates the window.
     */
    public Game() {
        super("The Treasure of Absalom");
    }

    /**
     * Initializes all game elements and calls all init methods throughout the game.
     *
     * @param gc GameContainer object
     * @throws SlickException
     */
    @Override
    public void init(GameContainer gc) throws SlickException {
        nonPlayable.init();
        playable.init();
        harbor = new Harbor(new BlockMap("data/maps/harbor.tmx"));
        harbor.init();
        elements.add(new Player(harbor.getMap()));
        gc.setTargetFrameRate(60); //Forces the game to try and run at 60 frames per second
        gc.setShowFPS(false);
        for(GameObject o : elements) {
            o.init();
        }
    }

    /**
     * Updates all game elements and calls all update methods throughout the game.
     *
     * @param gc GameContainer object
     * @param delta The amount of time thats passed since last update in milliseconds
     * @throws SlickException
     */
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        Input input = gc.getInput();
        for(GameObject o : elements) {
            o.update(gc, delta);
            if(input.isKeyPressed(input.KEY_D)) {
                if(o.debug == true) o.debug = false;
                else o.debug = true;
            }
        }
        harbor.update(gc, delta);
    }

    /**
     * Renders all game elements and calls all render methods throughout the game.
     * Maps have to be rendered using the render() method of the TiledMap object which
     * can't use floats for the position, this results in a slight loss of precision
     * on the display. This only effects graphics though, not mechanics.
     *
     * @param gc GameContainer object
     * @param g Graphics object
     * @throws SlickException
     */
    public void render(GameContainer gc, Graphics g) throws SlickException {
        int mapX = (int) harbor.getMap().getX();
        int mapY = (int) harbor.getMap().getY();
        for(int i = 0; i < harbor.getMap().getTmap().getLayerCount(); i++) {
            if(harbor.getMap().getTmap().getLayerProperty(i, "underPlayer", "-1").equals("1")) { //renders all layers that are meant to be under the player
                harbor.getMap().setUnderPlayer(true); //map is currently rendering under the player
                if(harbor.getMap().getTmap().getLayerProperty(i, "transparent", "-1").equals("1"))
                    harbor.getMap().findTransparencies(g, i); //do not render layers that are transparent, instead call findTransparencies()
                else
                    harbor.getMap().getTmap().render(mapX, mapY, i); //render layer
                harbor.setLayer(i); //sets the layer variable, which just shows which layer is currently being rendered.
                harbor.render(gc, g); //render the harbor object, this renders all objects associated with the harbor NOT the map.
            }
        }
        for(GameObject o : elements) { //render all elements that are meant to be displayed above the previous layers, but below the next set of layers
            o.render(gc, g);
        }
        for(int i = 0; i < harbor.getMap().getTmap().getLayerCount(); i++) {
            if(harbor.getMap().getTmap().getLayerProperty(i, "underPlayer", "-1").equals("0")) {
                harbor.getMap().setUnderPlayer(false); //not under player
                if(harbor.getMap().getTmap().getLayerProperty(i, "transparent", "-1").equals("1"))
                    harbor.getMap().findTransparencies(g, i); //same as above, don't render transparent layer
                else harbor.getMap().getTmap().render(mapX, mapY, i);
                harbor.setLayer(i); //sets the layer
                harbor.render(gc, g); //render the harbor object again, this is to render all animations and NPCs that should be above the player.
            }
        }
        harbor.getMap().render(gc, g); //render the BlockMap asscoiated with the harbor
    }

    /**
     * Getter for windowHeight
     *
     * @return windowHeight
     */
    public static int getWindowHeight() {
        return windowHeight;
    }

    /**
     * Getter for windowWidth
     *
     * @return windowWidth
     */
    public static int getWindowWidth() {
        return windowWidth;
    }
}