package ttoa;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Tucker Lein
 */
public class NonPlayable extends GameObject {
    protected static Image spriteSheet = null;

    @Override
    public void init() throws SlickException {
        spriteSheet = new Image("res/sprite_sheets/NonPlayable/sheet.png");
    }
}
