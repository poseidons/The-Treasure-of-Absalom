package ttoa;

import java.util.ArrayList;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Tucker Lein
 */
public class Harbor extends GameObject {
    private BlockMap map = null;
    private ArrayList<Animation> animations = new ArrayList<Animation>();
    private int layer = 0;
    private ArrayList<NPC> npcs = new ArrayList<NPC>();

    public Harbor(BlockMap map) {
        this.map = map;
        animations = map.getAnimations();
        npcs.add(new NPC(1, 0, 9, 400, 400, map));
        for(NPC npc : npcs) {
            map.entities.add((CollisionBlock)npc);
        }
    }

    public BlockMap getMap() {
        return map;
    }

    @Override
    public void init() throws SlickException {
        for(Animation a : animations) {
            a.start();
        }
        for(NPC npc : npcs) {
            npc.init();
        }
    }

    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        for(NPC npc : npcs) {
            npc.update(gc, delta);
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        for(int i = 0; i < animations.size(); i++) {
            if(layer == map.getAnimationLayer().get(i))
                animations.get(i).draw((int)map.getX() + map.getAnimationXPositions().get(i), (int)map.getY() + map.getAnimationYPositions().get(i));
        }
        if(map.isUnderPlayer()) {
            for(NPC npc : npcs) {
                npc.render(gc, g);
            }
        }
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
