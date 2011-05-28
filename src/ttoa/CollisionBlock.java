package ttoa;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author Tucker Lein
 */
public interface CollisionBlock {
    public void render(GameContainer gc, Graphics g) throws SlickException;

    public float getOriginalY();

    public float getOriginalX();

    public Rectangle getRect();
}
