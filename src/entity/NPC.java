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

    int exciteFrameCounter = 0;
    int exciteFrame = 0;
    private static final int EXCITE_FRAME_RATE = 20;

    int cryFrameCounter = 0;
    int cryFrame = 0;
    private static final int CRY_FRAME_RATE = 30;

    public enum NPCType {
        SIRIUS(Map.of("FRONT FACING NEUTRAL", "sirius front facing neutral.png",
                "FRONT FACING STEP LEFT", "sirius front facing step left.png",
                "FRONT FACING STEP RIGHT", "sirius front facing step right.png",
                "UP FACING NEUTRAL", "sirius up facing neutral.png",
                "UP FACING STEP LEFT", "sirius up facing step left.png",
                "UP FACING STEP RIGHT", "sirius up facing step right.png")),
        FIREDMAN(Map.of("CRY1", "firedManCry1.png", "CRY2", "firedManCry2.png")),
        PINKGIRL(Map.of("IDLE", "pinkGirl.png")),
        SPIDERMANKID(Map.of("IDLE1", "spidermanKid1.png", "IDLE2", "spidermanKid2.png", "TURN", "spidermanKidTurn.png"));

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
        WALK_DOWNWARD,
        WALK_UPWARD,
        SIRIUS_WAIT,
        CRY,
        EXCITE
    }

    public NPCAnimationType currentAnimation = NPCAnimationType.NONE;

    public void playNPCAnimation()
    {
        if(currentAnimation == NPCAnimationType.NONE)
        {
            return;
        }
        if (currentAnimation == NPCAnimationType.WALK_DOWNWARD)
            return;
//        if(currentAnimation == NPCAnimationType.CRY && type == NPCType.FIREDMAN)
//        {
//
//        }
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
        double screenX = worldX - gp.camWorldX + gp.player.screenX;
        double screenY = worldY - gp.camWorldY + gp.player.screenY;

        BufferedImage image = null;

        if(type == NPCType.SIRIUS)
        {
            if(currentAnimation == NPCAnimationType.WALK_DOWNWARD)
            {
                if(siriusWalkFrame == 0)    image = type.getSprite("FRONT FACING STEP LEFT");
                if(siriusWalkFrame == 1)    image = type.getSprite("FRONT FACING NEUTRAL");
                if(siriusWalkFrame == 2)    image = type.getSprite("FRONT FACING STEP RIGHT");
            }
            if(currentAnimation == NPCAnimationType.WALK_UPWARD)
            {
                if(siriusWalkFrame == 0)    image = type.getSprite("UP FACING STEP LEFT");
                if(siriusWalkFrame == 1)    image = type.getSprite("UP FACING NEUTRAL");
                if(siriusWalkFrame == 2)    image = type.getSprite("UP FACING STEP RIGHT");
            }
            if(currentAnimation == NPCAnimationType.SIRIUS_WAIT)
            {
                image = type.getSprite("FRONT FACING NEUTRAL");
            }
            if(currentAnimation == NPCAnimationType.NONE)
            {
                image = type.getSprite("FRONT FACING NEUTRAL");
            }
        }

        if(type == NPCType.FIREDMAN)
        {
            if(currentAnimation == NPCAnimationType.CRY)
            {
                image = (cryFrame == 0)
                        ? type.getSprite("CRY1")
                        : type.getSprite("CRY2");
            }
        }

        if(type == NPCType.SPIDERMANKID)
        {
            if(currentAnimation == NPCAnimationType.EXCITE)
            {
                image = (exciteFrame == 0)
                        ? type.getSprite("IDLE1")
                        : type.getSprite("IDLE2");
            }
            if(gp.gameState == gp.dialogueState && gp.spidermanKidTalkDone)
            {
                image = type.getSprite("TURN");
            }
        }

        if(type == NPCType.PINKGIRL)
        {
            image = type.getSprite("IDLE");
        }


        if (image != null) {
            g2.drawImage(image, (int)screenX, (int)screenY, gp.tileSize, gp.tileSize, null);
        }

    }

    public void update()
    {
        playNPCAnimation();

        if(type == NPCType.SIRIUS && currentAnimation == NPCAnimationType.WALK_DOWNWARD)
        {
            tickSiriusWalk();
        }

        if(type == NPCType.SIRIUS && currentAnimation == NPCAnimationType.WALK_UPWARD)
        {
            tickSiriusWalk();
        }

        if(type == NPCType.FIREDMAN && currentAnimation == NPCAnimationType.CRY)
        {
            cryFrameCounter++;
            if(cryFrameCounter >= CRY_FRAME_RATE)
            {
                cryFrame = (cryFrame == 0) ? 1 : 0;
                cryFrameCounter = 0;
            }
        }

        if(type == NPCType.SPIDERMANKID && currentAnimation == NPCAnimationType.EXCITE)
        {
            exciteFrameCounter++;
            if(exciteFrameCounter >= EXCITE_FRAME_RATE)
            {
                exciteFrame = (exciteFrame == 0) ? 1 : 0;
                exciteFrameCounter = 0;
            }
        }
    }

}
