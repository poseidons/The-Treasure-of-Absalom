package ttoa;

import java.util.ArrayList;
import java.util.Stack;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

/**
 * Player class that controls the main player in the game.
 * Implements CollisionBlock because it is a rectangle that 
 * other CollisionBlocks can interact with. Extends Playable
 * so it can use the playable sprite sheet defined in 
 * Playable.
 *
 * @author Tucker Lein
 */
public class Player extends Playable implements CollisionBlock{
    private int numSprites = 13; //number of sprites the Player will consist of
    private float playerX; //x position of the sprite
    private float playerY; //y position of the sprite
    private float playerScale = 3.0f; //scale of the sprite
    private float speed = 0.2f; //speed of movement
    private int dir = 0; //0 down, 1 left, 2 up, 3 right
    private boolean[] dirBool = { false, false, false, false }; //0 down, 1 left, 2 up, 3 right
    private ArrayList<Image> sprites = new ArrayList<Image>(); //ArrayList of all sprites
    private ArrayList<Animation> animations = new ArrayList<Animation>(); //ArrayList of all animations
    private Image curImage = null; //the current image the sprite uses; will be set of sprites.get(0) (blank) when an animation is playing
    private Rectangle rect; //rectangle used for collisions
    private int animateSpeed = 250; //speed at which the animations animate at
    private BlockMap curMap; //the current BlockMap the player is on
    private boolean move = false; //boolean to see if the player is supposed to be moving or the map is (used in scrolling, true = player moving, false = map is moving)
    private int yPolyOffset = 26; //y offset of the rectangle to the 0,0 coordinate on the sprite
    private int xPolyOffset = 1; //x offset of the rectangle to the 0,0 corrdinate on the sprite
    private Stack<Integer> inputStack = new Stack<Integer>(); //Stack that contains all movement input given, top = current direction
    private Stack<Animation> animationStack = new Stack<Animation>(); //Stack that contains the current directional animation, top = current animation

    /**
     * Constructor, defines the BlockMap the player is on and adds the player to that maps collision entities.
     * 
     *
     * @param curMap
     */
    public Player(BlockMap curMap) {
        this.curMap = curMap;
        curMap.entities.add(this);
        playerX = (800/2); //temp init placement x
        playerY = (600/2); //temp init placement y
        rect = new Rectangle(playerX+xPolyOffset, playerY+yPolyOffset, 46, 23);
    }

    /**
     * initializes all elements and gathers all data concerning sprites and animations.
     *
     * @throws SlickException
     */
    @Override
    public void init() throws SlickException {
        rect.setX(playerX+xPolyOffset);
        rect.setY(playerY+yPolyOffset);
        int xPos = 0;
        int yPos = 0;
        int size = 16;
        for(int i = 0; i < numSprites; i++) {
            sprites.add(spriteSheet.getSubImage(xPos, yPos, size, size));
            xPos += 16;
            if(xPos >= spriteSheet.getWidth()) {
                xPos = 0;
                yPos += 16;
            }
        }
        curImage = sprites.get(0);
        animations.add(new Animation(sprites.subList(10, 13).toArray(new Image[0]), animateSpeed));
        animations.add(new Animation(sprites.subList(4, 7).toArray(new Image[0]), animateSpeed));
        animations.add(new Animation(sprites.subList(7, 10).toArray(new Image[0]), animateSpeed));
        animations.add(new Animation(sprites.subList(1, 4).toArray(new Image[0]), animateSpeed));
        for(int i = 0; i < animations.size(); i++) {
            for(int k = 0; k < animations.get(i).getFrameCount(); k++) {
                animations.get(i).getImage(k).setFilter(animations.get(i).getImage(k).FILTER_NEAREST); //nearest neighbor type of resizing
            }
        }
        curImage.setFilter(curImage.FILTER_NEAREST); //nearest neighbor type of resizing
    }

    /**
     * Updates the player based on input, this determines scrolling, collisions, and directional input
     *
     * @param gc GameContainer object
     * @param delta The amount of time thats passed since last update in milliseconds
     * @throws SlickException
     */
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        Input input = gc.getInput(); //get the current Input object
        checkInput(input); //collects the current keyboard inputs
        dir = -1; //resets dir to a nonvalid value
        move = false; //resets move
        float tempMovement = (speed * delta); //calculates the current amount of movement
        if(!inputStack.isEmpty()) { //if there is input
            if(inputStack.peek() == 1) { //left
                if(curMap.getX() >= 0 || playerX >= gc.getWidth()/2) { //Determines when the player should be moving instead of the screen
                    move = true; //player is to be moving.
                }
                playerX -= tempMovement; //set the movement
                rect.setX(playerX+xPolyOffset); //set the new rect position
                if(entityCollisionWidth() || !move) { //if the player is colliding with something OR the screen is scrolling
                    if(!entityCollisionWidth()) { //basically if(!move), if(not colliding with something)
                        curMap.setX((curMap.getX() + tempMovement)); //move the map the opposite direction the player went
                        for(int i = 0; i < curMap.entities.size(); i++) { //loop through all the map's entities
                            curMap.entities.get(i).getRect().setX((curMap.getX() + curMap.entities.get(i).getOriginalX()));
                        }
                        playerX += tempMovement; //reset the player position back
                    } else { //else if(colliding with something)
                        while(entityCollisionWidth()) { //while still colliding with something
                            playerX += 0.05f; //reposition away from the collision
                            rect.setX(playerX+xPolyOffset); //set the rect's new position
                        }
                    }
                    rect.setX(playerX+xPolyOffset); //set the rect's new position, just for good measure
                }
                curImage = sprites.get(0); //set the current image (not animation) to blank
                dir = 1; //direction is now 1 (left)
            }
            checkInput(input); //check input again
            if(inputStack.peek() == 3) { //right, repeat all above steps.
                if((curMap.getX()*-1 + gc.getWidth()) >= curMap.getMapWidth() || playerX <= gc.getWidth()/2) {
                    move = true;
                }
                playerX += tempMovement;
                rect.setX(playerX+xPolyOffset);
                if(entityCollisionWidth() || !move) {
                    if(!entityCollisionWidth()) {
                        curMap.setX((curMap.getX() - tempMovement));
                        for(int i = 0; i < curMap.entities.size(); i++) {
                            curMap.entities.get(i).getRect().setX((curMap.getX()+curMap.entities.get(i).getOriginalX()));
                        }
                        playerX -= tempMovement;
                    } else {
                        while(entityCollisionWidth()) {
                            playerX -= 0.05f;
                            rect.setX(playerX+xPolyOffset);
                        }
                    }
                    rect.setX(playerX+xPolyOffset);
                }
                curImage = sprites.get(0);
                dir = 3;
            }
            move = false;
            checkInput(input);
            if(inputStack.peek() == 2) { //up
                if(curMap.getY() >= 0 || playerY >= gc.getHeight()/2) {
                    move = true;
                }
                playerY -= tempMovement;
                rect.setY(playerY+yPolyOffset);
                if(entityCollisionWidth() || !move) {
                    if(!entityCollisionWidth()) {
                        curMap.setY((curMap.getY() + tempMovement));
                        for(int i = 0; i < curMap.entities.size(); i++) {
                            curMap.entities.get(i).getRect().setY((curMap.getY()+curMap.entities.get(i).getOriginalY()));
                        }
                        playerY += tempMovement;
                    } else {
                        while(entityCollisionWidth()) {
                            playerY += 0.05f;
                            rect.setY(playerY + yPolyOffset);
                        }
                    }
                    rect.setY(playerY+yPolyOffset);
                }
                curImage = sprites.get(0);
                dir = 2;
            }
            checkInput(input);
            if(inputStack.peek() == 0) { //down
                if((curMap.getY()*-1 + gc.getHeight()) >= curMap.getMapHeight() || playerY <= gc.getHeight()/2) {
                    move = true;
                }
                playerY += tempMovement;
                rect.setY(playerY+yPolyOffset);
                if(entityCollisionWidth() || !move) {
                    if(!entityCollisionWidth()) {
                        curMap.setY((curMap.getY() - tempMovement));
                        for(int i = 0; i < curMap.entities.size(); i++) {
                            curMap.entities.get(i).getRect().setY((curMap.getY()+curMap.entities.get(i).getOriginalY()));
                        }
                        playerY -= tempMovement;
                    } else {
                        while(entityCollisionWidth()) {
                            playerY -= 0.05f;
                            rect.setY(playerY+yPolyOffset);
                        }
                    }
                    rect.setY(playerY+yPolyOffset);
                }
                curImage = sprites.get(0);
                dir = 0;
            }
            if(!animationStack.isEmpty()) { //if the animation stack is not empty there is an animation to be played
                animationStack.peek().start(); //start the top most animation
            }
        }
    }

    /**
     * Renders the current image and the current animation
     *
     * @param gc GameContainer object
     * @param g Graphics object
     * @throws SlickException
     */
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        if(curMap.isUnderPlayer() && animationStack.isEmpty()) {
            curImage.draw(playerX, playerY, playerScale);
        }
        if(curMap.isUnderPlayer() && !animationStack.isEmpty()) animationStack.peek().draw(playerX, playerY, 48, 48);
        if(debug) g.draw(rect);
    }

    
    public boolean entityCollisionWidth() throws SlickException {
        for(int i = 0; i < curMap.entities.size(); i++) {
            CollisionBlock entity1 = (CollisionBlock) curMap.entities.get(i);
            if(!entity1.equals(this)) {
                if((rect.intersects(entity1.getRect()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void checkInput(Input input) {
        if(input.isKeyPressed(Input.KEY_LEFT)) {
            inputStack.add(1);
            animationStack.add(animations.get(0));
        }
        if(input.isKeyPressed(Input.KEY_RIGHT)) {
            inputStack.add(3);
            animationStack.add(animations.get(1));
        }
        if(input.isKeyPressed(Input.KEY_UP)) {
            inputStack.add(2);
            animationStack.add(animations.get(2));
        }
        if(input.isKeyPressed(Input.KEY_DOWN)) {
            inputStack.add(0);
            animationStack.add(animations.get(3));
        }
        if(!input.isKeyDown(Input.KEY_LEFT)) {
            inputStack.removeElement((Object)1);
            animationStack.removeElement(animations.get(0));
            dirBool[1] = false;
            if(curImage.equals(sprites.get(0)) && dir == 1) curImage = sprites.get(10);
        }
        if(!input.isKeyDown(Input.KEY_RIGHT)) {
            inputStack.remove((Object)3);
            animationStack.removeElement(animations.get(1));
            dirBool[3] = false;
            if(curImage.equals(sprites.get(0)) && dir == 3) curImage = sprites.get(4);
        }
        if(!input.isKeyDown(Input.KEY_UP)) {
            inputStack.remove((Object)2);
            animationStack.removeElement(animations.get(2));
            dirBool[2] = false;
            if(curImage.equals(sprites.get(0)) && dir == 2) curImage = sprites.get(7);
        }
        if(!input.isKeyDown(Input.KEY_DOWN)) {
            inputStack.remove((Object)0);
            animationStack.removeElement(animations.get(3));
            dirBool[0] = false;
            if(curImage.equals(sprites.get(0)) && dir == 0) curImage = sprites.get(1);
        }
    }

    public void setCurImage(Image curImage) {
        this.curImage = curImage;
    }

    public float getPlayerX() {
        return playerX;
    }

    public float getPlayerY() {
        return playerY;
    }

    public float getOriginalY() {
        return playerY;
    }

    public float getOriginalX() {
        return playerX;
    }

    public Rectangle getRect() {
        return rect;
    }
}
