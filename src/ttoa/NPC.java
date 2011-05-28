package ttoa;

import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author Tucker Lein
 */
public class NPC extends NonPlayable implements CollisionBlock {
    private int numSprites;
    private float x;
    private float y;
    private float scale = 3.0f;
    private float speed = 0.15f;
    private int dir = 1; //0 down, 1 left, 2 up, 3 right
    private Image curImage;
    private Image lastImage;
    private int firstX;
    private int firstY;
    private ArrayList<Image> sprites = new ArrayList<Image>();
    private ArrayList<Animation> animations = new ArrayList<Animation>();
    private BlockMap curMap;
    private Rectangle rect;
    private int yPolyOffset = 1;
    private int xPolyOffset = 1;
    private int animateSpeed = 300;
    private Random rand = new Random();
    private long curTime;

    public NPC(int firstX, int firstY, int numSprites, float x, float y, BlockMap curMap) {
        this.firstX = firstX;
        this.firstY = firstY;
        this.numSprites = numSprites;
        this.x = x;
        this.y = y;
        this.curMap = curMap;
        rect = new Rectangle(x+xPolyOffset, y+yPolyOffset, 46, 46);
    }

    @Override
    public void init() throws SlickException {
        dir = rand.nextInt(11);
        rect.setX(x+xPolyOffset);
        rect.setY(y+yPolyOffset);
        sprites.add(spriteSheet.getSubImage(0, 0, 16, 16));
        int xPos = firstX * 16;
        int yPos = firstY * 16;
        for(int i = 1; i < numSprites; i++) {
            sprites.add(spriteSheet.getSubImage(xPos, yPos, 16, 16));
            xPos += 16;
            if(xPos >= spriteSheet.getWidth()) {
                xPos = 0;
                yPos += 16;
            }
        }
        curImage = sprites.get(1);
        lastImage = sprites.get(1);
        for(int i = 0; i < numSprites; i++) {
            sprites.get(i).setFilter(Image.FILTER_NEAREST);
        }
        animations.add(new Animation(sprites.subList(1, 3).toArray(new Image[0]), animateSpeed)); //down
        animations.add(new Animation(sprites.subList(7, 9).toArray(new Image[0]), animateSpeed)); //left
        animations.add(new Animation(sprites.subList(5, 7).toArray(new Image[0]), animateSpeed)); //up
        animations.add(new Animation(sprites.subList(3, 5).toArray(new Image[0]), animateSpeed)); //right
    }

    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        float tempMovement = (speed * delta);
        if(dir == 7) { //down
            if(curTime == 0) curTime = gc.getTime();
            y += tempMovement;
            lastImage = sprites.get(1);
            curImage = sprites.get(0);
            animations.get(0).start();
            animations.get(1).stop();
            animations.get(2).stop();
            animations.get(3).stop();
            if(entityCollisionWidth() || getNewDirection(gc)) {
                if(entityCollisionWidth()) {
                    while(entityCollisionWidth()) {
                        y -= .05f;
                        rect.setY(y+yPolyOffset);
                    }
                }
                curTime = 0;
                do {
                    dir = rand.nextInt(11);
                } while(dir == 7);
            }
        } else if(dir == 8) { //left
            if(curTime == 0) curTime = gc.getTime();
            x -= tempMovement;
            lastImage = sprites.get(7);
            curImage = sprites.get(0);
            animations.get(0).stop();
            animations.get(1).start();
            animations.get(2).stop();
            animations.get(3).stop();
            if(entityCollisionWidth() || getNewDirection(gc)) {
                if(entityCollisionWidth()) {
                    while(entityCollisionWidth()) {
                        x += 0.5f;
                        rect.setX(x+xPolyOffset);
                    }
                }
                curTime = 0;
                do {
                    dir = rand.nextInt(11);
                } while(dir == 8);
            }
        } else if(dir == 9) { //up
            if(curTime == 0) curTime = gc.getTime();
            y -= tempMovement;
            lastImage = sprites.get(5);
            curImage = sprites.get(0);
            animations.get(0).stop();
            animations.get(1).stop();
            animations.get(2).start();
            animations.get(3).stop();
            if(entityCollisionWidth() || getNewDirection(gc)) {
                if(entityCollisionWidth()) {
                    while(entityCollisionWidth()) {
                        y += .05f;
                        rect.setY(y+yPolyOffset);
                        System.out.println("up");
                    }
                }
                curTime = 0;
                do {
                    dir = rand.nextInt(11);
                } while(dir == 9);
            }
        } else if(dir == 10) { //right
            if(curTime == 0) curTime = gc.getTime();
            x += tempMovement;
            lastImage = sprites.get(3);
            curImage = sprites.get(0);
            animations.get(0).stop();
            animations.get(1).stop();
            animations.get(2).stop();
            animations.get(3).start();
            if(entityCollisionWidth() || getNewDirection(gc)) {
                if(entityCollisionWidth()) {
                    while(entityCollisionWidth()) {
                        x -= .05f;
                        rect.setX(x+xPolyOffset);
                    }
                }
                curTime = 0;
                do {
                    dir = rand.nextInt(11);
                } while(dir == 10);
            }
        } else if(dir < 7) {
            if(curTime == 0) curTime = gc.getTime();
            curImage = lastImage;
            animations.get(0).stop();
            animations.get(1).stop();
            animations.get(2).stop();
            animations.get(3).stop();
            if(getNewDirection(gc)) {
                curTime = 0;
                dir = rand.nextInt(11);
            }
        }
        rect.setX(curMap.getX() + x);
        rect.setY(curMap.getY() + y + yPolyOffset);
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        if(curMap.isUnderPlayer()) curImage.draw(curMap.getX() + x, curMap.getY() + y, scale);
        if(debug) g.draw(rect);
        for(Animation a : animations) {
            if(!a.isStopped() && curMap.isUnderPlayer()) a.draw(curMap.getX() + x, curMap.getY() + y, 48, 48);
        }
    }

    public boolean getNewDirection(GameContainer gc) throws SlickException {
        if(gc.getTime() - curTime > 1000) {
            return true;
        } else return false;
    }

    public boolean entityCollisionWidth() throws SlickException {
        for(int i = 0; i < curMap.entities.size(); i++) {
            CollisionBlock entity1 = (CollisionBlock) curMap.entities.get(i);
            if(!entity1.equals(this)) {
                if(rect.intersects(entity1.getRect())) {
                    return true;
                }
            }
        }
        return false;
    }

    public float getOriginalY() {
        return y;
    }

    public float getOriginalX() {
        return x;
    }

    public Rectangle getRect() {
        return rect;
    }
}
