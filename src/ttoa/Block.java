/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ttoa;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author Tucker Lein
 */
public class Block extends GameObject implements CollisionBlock {
    private Rectangle rect;
    private float x;
    private float y;
    private float originalX;
    private float originalY;
    private int[] test;

    public Block(float x, float y, int test[], String type) {
        this.x = x;
        this.y = y;
        originalX = x;
        originalY = y;
        this.test = test;
        rect = new Rectangle(x, y, 24, 24);

    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        if(debug) g.draw(rect);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getOriginalX() {
        return originalX;
    }

    public float getOriginalY() {
        return originalY;
    }

    public Rectangle getRect() {
        return rect;
    }
}
