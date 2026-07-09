package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NPC extends Entity {
    GamePanel gp;
    private NPCType type;

    public enum NPCType {
        SIRIUS(Map.of("FRONT FACING NEUTRAL", "sirius front facing neutral.png", "FRONT FACING STEP LEFT", "sirius front facing step left.png", "FRONT FACING STEP RIGHT", "sirius front facing step right.png"));

        //map of sprite images for each npc type
        private final Map<String, BufferedImage> spriteMap = new HashMap<>();

        private static BufferedImage getNPCImage(String name) {
            try {
                InputStream s = NPCType.class.getResourceAsStream("/NPC/" + name);
                if (s == null) return null;
                return ImageIO.read(s);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        NPCType(Map<String, String> fileNames) {
            fileNames.forEach((key, fileName) -> {
                spriteMap.put(key, getNPCImage(fileName));
            });
        }

        public BufferedImage getSprite(String key)
        {
            return spriteMap.get(key);
        }
    }

    int phase;
    int animationCounter;
    int animationMax;

    int siriusWalkFrameCounter = 0;
    static final  int SIRIUS_WALK_FRAME_RATE = 10;
    public int siriusWalkFrame = 0;
    boolean siriusGoingForward = true;

    public NPC(GamePanel gp, NPCType type, int worldX, int worldY)
    {
        this.gp = gp;
        this.type = type;
        this.worldX = worldX;
        this.worldY = worldY;
        phase = 0;

        solidArea = new Rectangle(16, 32, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }

    public enum NPCAnimationType
    {
        NONE,
        WALK
    }

    public NPCAnimationType currentAnimation = NPCAnimationType.NONE;

    public void playNPCAnimation()
    {
        if(currentAnimation == NPCAnimationType.NONE)
        {
            return;
        }
        if (currentAnimation == NPCAnimationType.WALK) return;
    }

    // sirius walk only
    public void tickSiriusWalk()
    {
        siriusWalkFrameCounter++;
        if(siriusWalkFrameCounter >= SIRIUS_WALK_FRAME_RATE)
        {
            if(siriusGoingForward)
            {
                siriusWalkFrame++;
                if(siriusWalkFrame >= 2)
                {
                    siriusGoingForward = false;
                }
            } else {
                siriusWalkFrame--;
                if(siriusWalkFrame <= 0)
                {
                    siriusGoingForward = true;
                }
            }
            siriusWalkFrameCounter = 0;
        }
    }



    public void draw(Graphics2D g2)
    {
        double screenX = worldX - gp.player.worldX + gp.player.screenX;
        double screenY = worldY - gp.player.worldY + gp.player.screenY;

        BufferedImage image = null;

        if(type == NPCType.SIRIUS)
        {
            if(currentAnimation == NPCAnimationType.WALK)
            {
                if(siriusWalkFrame == 0)    image = type.getSprite("FRONT FACING STEP LEFT");
                if(siriusWalkFrame == 1)    image = type.getSprite("FRONT FACING NEUTRAL");
                if(siriusWalkFrame == 2)    image = type.getSprite("FRONT FACING STEP RIGHT");
            }
            if(currentAnimation == NPCAnimationType.NONE)
            {
                image = type.getSprite("FRONT FACING NEUTRAL");
            }
        }


        if (image != null) {
            g2.drawImage(image, (int)screenX, (int)screenY, gp.tileSize, gp.tileSize, null);
        }

    }

    public void update()
    {
        playNPCAnimation();

        if(type == NPCType.SIRIUS && currentAnimation == NPCAnimationType.WALK)
        {
            tickSiriusWalk();
        }
    }

}
