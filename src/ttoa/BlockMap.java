package ttoa;

import java.util.ArrayList;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author Tucker Lein
 */
public class BlockMap extends GameObject{
    private TiledMap tmap;
    private int mapWidth;
    private int mapHeight;
    private float x = 0;
    private float y = 0;
    private int square[] = {0,0,
                            23,0,
                            23,23,
                            0,23}; //upper left, upper right, lower right, lower left
    public ArrayList<CollisionBlock> entities;
    private ArrayList<Float> animationXPositions = new ArrayList<Float>();
    private ArrayList<Float> animationYPositions = new ArrayList<Float>();
    private ArrayList<Integer> animationLayer = new ArrayList<Integer>();
    private ArrayList<Animation> animations = new ArrayList<Animation>();
    private boolean underPlayer = false;

    public BlockMap(String ref) throws SlickException {
        entities = new ArrayList<CollisionBlock>();
        tmap = new TiledMap(ref, "data/maps");
        findAnimations();
        mapWidth = tmap.getWidth() * tmap.getTileWidth();
        mapHeight = tmap.getHeight() * tmap.getTileHeight();

        for(int x = 0; x < tmap.getWidth(); x++) {
            for(int y = 0; y < tmap.getHeight(); y++) {
                int tileID = tmap.getTileId(x, y, 0);
                if(tileID == 1) {
                    entities.add((CollisionBlock) new Block(x*24, y*24, square, "square"));
                }
            }
        }
    }

    public void findTransparencies(Graphics g, int layer) {
        for(int x = 0; x < getTmap().getWidth(); x++) {
            for(int y = 0; y < getTmap().getHeight(); y++) {
                int tileID = getTmap().getTileId(x, y, layer);
                if(getTmap().getTileProperty(tileID, "transparent", "-1").equals("1")) {
                    Image img = getTmap().getTileImage(x, y, layer);
                    img.setAlpha(0.6f);
                    g.drawImage(img, (int)this.x + (x * getTmap().getTileWidth()), (int)this.y + (y * getTmap().getTileWidth()));
                }
            }
        }
    }

    public ArrayList<Animation> findAnimations() throws SlickException{
        for(int x =0; x < getTmap().getWidth(); x++) {
            for(int y = 0; y < getTmap().getHeight(); y++) {
                for(int i = 0 ; i < getTmap().getLayerCount(); i++) {
                    int tileID = getTmap().getTileId(x, y, i);
                    int numFrames = Integer.valueOf(getTmap().getTileProperty(tileID, "numFrames", "-1"));
                    if(getTmap().getTileProperty(tileID, "animate", "-1").equals("1")) {
                        animationXPositions.add(x * (float)getTmap().getTileWidth());
                        animationYPositions.add(y * (float)getTmap().getTileHeight());
                        animationLayer.add(i);
                        Image[] frames = new Image[numFrames];
                        getTmap().getTileImage(x, y, numFrames);
                        int sheetX = Integer.valueOf(getTmap().getTileProperty(tileID, "sheetX", "-1"));
                        int sheetY = Integer.valueOf(getTmap().getTileProperty(tileID, "sheetY", "-1"));
                        for(int k = 0; k < numFrames; k++) {
                            frames[k] = getTmap().getTileSet(0).tiles.getSubImage(sheetX + Integer.valueOf(getTmap().getTileProperty(tileID, "frame" + String.valueOf(k+1), "-1")), sheetY);
                            if(getTmap().getTileProperty(tileID, "transparent", "-1").equals("1")) frames[k].setAlpha(0.6f);
                        }
                        animations.add(new Animation(frames, Integer.valueOf(getTmap().getTileProperty(tileID, "duration", "-1"))));
                    }
                }
            }
        }
        return animations;
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        for(CollisionBlock b : entities) {
            b.render(gc, g);
        }
    }

    public TiledMap getTmap() {
        return tmap;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public ArrayList<Float> getAnimationXPositions() {
        return animationXPositions;
    }

    public ArrayList<Float> getAnimationYPositions() {
        return animationYPositions;
    }

    public ArrayList<Integer> getAnimationLayer() {
        return animationLayer;
    }

    public ArrayList<Animation> getAnimations() {
        return animations;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setUnderPlayer(boolean underPlayer) {
        this.underPlayer = underPlayer;
    }

    public boolean isUnderPlayer() {
        return underPlayer;
    }
}
