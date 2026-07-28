package entity;

import battle.BattleSystem;
import main.GamePanel;
import main.KeyHandler;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public boolean holdingLetter = false;
    public boolean packDone = false;


    public enum AnimationType {
        NONE,
        BOUNCE,
        SPIN,
        //add more
    }
    public AnimationType currentAnimation = AnimationType.NONE;
    private int animationCounter = 0;
    private int animationMax = 30;
    private int animationOffsetY = 0;
    private boolean animationDone = false;
    private boolean triggerLetterReactionDialogue = false;
    private boolean secondTrainFadeTriggered = false;
    private boolean stoutAnimationDone;
    private int stoutAnimationOffsetY = 0;
    private int stoutCounter = 0;
    private int stoutMax = 30;
    public boolean firstBattleLost = false;
    public boolean stoutDefeated = false;
    public boolean exitTrain = false;

    //for save
    public boolean doorOpened = false;

    public Player (GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(16, 32, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues () {
        worldX = gp.tileSize * 4;
        worldY = gp.tileSize * 4;
        speed = 4;
        direction = "down";
        maxHp = 100;
        hp = maxHp;
    }
    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/up facing step left.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/up facing neutral.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/player/up facing step right.png"));

            down1 = ImageIO.read(getClass().getResourceAsStream("/player/front facing step left.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/front facing neutral.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/player/front facing step right.png"));

            left1 = ImageIO.read(getClass().getResourceAsStream("/player/left facing step frontleg.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/left facing neutral.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/player/left facing step backleg.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/player/right facing step frontleg.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/right facing neutral.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/player/right facing step backleg.png"));

            openletter = ImageIO.read(getClass().getResourceAsStream("/player/openletter.png"));

            hurt = ImageIO.read(getClass().getResourceAsStream("/player/up facing hurt.png"));

            icon = ImageIO.read(getClass().getResourceAsStream("/ui/main1BattleIcon.png"));



        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    boolean goingForward = true;

    public void update() {

        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {
            if(keyH.upPressed) {
                direction = "up";
            } else if(keyH.downPressed) {
                direction = "down";
            } else if(keyH.leftPressed) {
                direction = "left";
            } else if(keyH.rightPressed) {
                direction = "right";
            }

            // check tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // check object collision
//            int objIndex = gp.cChecker.checkObject(this, true);
            gp.cChecker.checkObject(this, true);


            //check NPC collision
            gp.cChecker.checkNPC(this);

            // if collision is false, player can move
            if (!collisionOn) {

                switch(direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;

                }
            }

            spriteCounter++;
            if (spriteCounter > 10) {
                if (goingForward) {
                    spriteNum++;
                    if (spriteNum == 3) {
                        goingForward = false;
                    }
                } else {
                    spriteNum--;
                    if (spriteNum == 1) {
                        goingForward = true;
                    }
                }
                spriteCounter = 0;
            }

        }
        playAnimation();

        if(firstBattleLost && !gp.siriusWalkDone)
        {
            if(gp.player.firstBattleLost) {
                gp.sirius = new NPC(gp, NPC.NPCType.SIRIUS, 3 * gp.tileSize, (int) (5.3 * gp.tileSize));
                gp.sirius.currentAnimation = NPC.NPCAnimationType.WALK_DOWNWARD;
                gp.sirius.siriusWalkFrame = 1;
                gp.siriusTargetX = gp.tileSize * 3;
                gp.siriusTargetY = gp.tileSize * 13;
                gp.gameState = gp.siriusWalkState;
            }

        }

    }
    public void checkObjectProximity() {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                double xDistance = Math.abs(worldX - gp.obj[i].worldX);
                double yDistance = Math.abs(worldY - gp.obj[i].worldY);
                double distance = Math.max(xDistance, yDistance);
                // INTRO
                if (!gp.obj[i].dialogueShown) {

                    if (distance < 2 * gp.tileSize) {
                        if (gp.obj[i].name.equals("door1") && i == 1) {
                            gp.gameState = gp.dialogueState;
                            gp.dialogueLines = new String[]{
                                    "Press Space to close this dialogue, then press F to interact with the door."
                            };
                            gp.obj[i].dialogueShown = true;
                            break;
                        }
                    } else if (distance < 4 * gp.tileSize) {
                        if (gp.obj[i].name.equals("envelope")) {
                            gp.gameState = gp.dialogueState;
                            gp.dialogueLines = new String[]{
                                    "You see a envelope near the front door.",
                                    "You weren't expecting mail. Best to check it out.",
                                    "Press F to read the letter when in its range."
                            };
                            gp.obj[i].dialogueShown = true;
                            break;

                        }
                    }
                }
                if (packDone) {
                    if (distance < gp.tileSize) {
                        if (gp.obj[i].name.equals("door1") && i == 3) {
                            gp.completeFade(null,
                                    () -> {
                                        gp.enterTrain();
                                    },

                                    () -> {
                                        gp.gameState = gp.playState;
                                    });
                        }
                    }
                }
                if (distance < 1.75 * gp.tileSize && gp.obj[i] != null) {
                    if (gp.obj[i].name.equals("traindistancedetector") && !gp.triggerTrainAnimation)
                    {
                        gp.triggerTrainAnimation = true;
                    }
                    if (gp.obj[i].name.equals("traindistancedetector") && !gp.firstTrainFadeTriggered && gp.trainAnimationDone) {
                        gp.firstTrainFadeTriggered = true;
                        gp.completeFade(
                                new String[]{"You board the train."},
                                () -> {
                                    gp.enterInteriorTrain();
                                },
                                () -> {
                                    gp.dialogueIndex = 0;
                                    gp.gameState = gp.dialogueState;
                                    gp.dialogueLines = new String[]{
                                            "The trip is quite long. You should do something to pass the time.",
                                            "Press F to interact with your fellow passengers."
                                    };
                                }
                        );
                    }
                }


                if (distance < 2 * gp.tileSize && gp.obj[i] != null) {
                    if (gp.obj[i].name.equals("stoutstill") && !firstBattleLost) {

                        if (stoutAnimationDone) {
                            stoutAnimationDone = false;
                            stoutCounter = 0;
                        }

                        // Only progress the math if actively animating
                        if (!stoutAnimationDone) {
                            stoutCounter++;
                            double t = (double) stoutCounter / stoutMax;

                            // Calculate the directional jumps
                            int xDiff = (int) (worldX - gp.obj[i].worldX);
                            int yDiff = (int) (worldY - gp.obj[i].worldY) - gp.tileSize;

                            gp.obj[i].animationOffsetX = (int) (xDiff * t);
                            gp.obj[i].animationOffsetY = (int) (yDiff * t) + (int) (-4 * t * (1 - t) * 15);

                            if (stoutCounter >= stoutMax) {
                                stoutAnimationDone = true;
                                gp.obj[38] = new SuperObject("stoutstill", 1.8*gp.tileSize + xDiff, 13.3 * gp.tileSize +yDiff, true);

                                gp.obj[i].animationOffsetX = 0;
                                gp.obj[i].animationOffsetY = 0;

                                // start battle
                                gp.battleSystem.isTutorialBattle = true;
                                gp.startBattleTransition(gp.stout);

                                break;
                            }
                        }
                    }
                }

            }

        }
    }
    // else {
    //                    gp.nearObject = false;

    public void checkObjectInteraction() {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                double xDistance = Math.abs(worldX - gp.obj[i].worldX);
                double yDistance = Math.abs(worldY - gp.obj[i].worldY);
                double distance = Math.max(xDistance, yDistance);
                if (gp.obj[i].name.equals("door1") && i == 1) {
                    if (distance < 2 * gp.tileSize) {
                        if (keyH.interactPressed) {
                            gp.obj[1] = new SuperObject("dooropen", 12 * gp.tileSize, 4 * gp.tileSize, false);
                            doorOpened = true;
                            keyH.interactPressed = false;
                            break;
                        }
                    }
                }
                if (gp.obj[i].name.equals("envelope") && i == 7) {
                    if (distance < 1 * gp.tileSize) {
                        if (keyH.interactPressed) {
                            gp.obj[7] = null;
                            i--;
                            holdingLetter = true;
                            gp.gameState = gp.dialogueState;
                            gp.dialogueLines = new String[] {
                                    "Congratulations! After careful evaluation, we are excited to offer you a place at [] Academy!",
                                    "By attending our school, you'll be surrounded by a community of scholars and have the chance to sharpen your magic skills."
                            };
                            gp.dialogueIndex = 0;
                            currentAnimation = AnimationType.BOUNCE;
                            animationMax = 30;
                            gp.startAnimationAfterDialogue = true;
                            triggerLetterReactionDialogue = true;

                            break;
                        }
                    }

                }
                if (gp.obj[i] != null) {

                    if (gp.gameState == gp.packState) {
                        if (gp.obj[i].name.equals("backpack1") && i == 5) {
                            if (distance < 2 * gp.tileSize) {
                                if (keyH.interactPressed) {
                                    gp.obj[5] = null;
                                    gp.gameState = gp.dialogueState;
                                    gp.dialogueLines = new String[]{
                                            "Good idea! You'll definitely need a backpack to pack your things.",
                                    };
                                    gp.dialogueIndex = 0;
                                    break;
                                }
                            }
                        }
                        if (gp.obj[i].name.equals("laptop") && i == 10) {
                            if (distance < gp.tileSize) {
                                if (keyH.interactPressed) {
                                    gp.obj[10] = null;
                                    gp.gameState = gp.dialogueState;
                                    gp.dialogueLines = new String[]{
                                            "Packing a laptop is smart. You'll need it for your studies.",
                                    };
                                    gp.dialogueIndex = 0;
                                    break;
                                }
                            }
                        }
                        if (gp.obj[i].name.equals("roomitems") && i == 11) {
                            if (distance < gp.tileSize) {
                                if (keyH.interactPressed) {
                                    gp.obj[11] = null;
                                    gp.gameState = gp.dialogueState;
                                    gp.dialogueLines = new String[]{
                                            "You can't imagine leaving without your personal items. A phone and a notebook is a must!",
                                    };
                                    gp.dialogueIndex = 0;
                                    break;
                                }
                            }
                        }
                        if (!packDone && gp.obj[5] == null && gp.obj[10] == null && gp.obj[11] == null) {
                            gp.gameState = gp.dialogueState;
                            gp.dialogueLines = new String[]{
                                    "Nice! You've got everything you need. Hurry and catch the train!"
                            };

                            packDone = true;
                            gp.dialogueFinished = false;
                        }
                    }
                }
            }
        }
//        keyH.interactPressed = false;
    }

    public void checkNPCProximity()
    {
        NPC[] npcs = {gp.firedMan, gp.pinkGirl, gp.spidermanKid};

        for(NPC npc : npcs) {
            if (npc == null) continue;

            double xDistance = Math.abs(worldX - npc.worldX);
            double yDistance = Math.abs(worldY - npc.worldY);
            double distance = Math.max(xDistance, yDistance);
        }
    }

    public void checkNPCInteraction()
    {
        NPC[] npcs = {gp.firedMan, gp.pinkGirl, gp.spidermanKid};

        for(NPC npc : npcs) {
            if (npc == null) continue;

            double xDistance = Math.abs(worldX - npc.worldX);
            double yDistance = Math.abs(worldY - npc.worldY);
            double distance = Math.max(xDistance, yDistance);

            if (distance < 1 * gp.tileSize && npc == gp.firedMan) {
                if (keyH.interactPressed && !gp.firedManTalkDone) {
                    gp.gameState = gp.dialogueState;
                    gp.dialogueLines = new String[]{
                            "A suited man cries, holding a cardboard box full of office supplies.",
                            "This is the worst day of my life...",
                            "Twenty years with those scumbags, all gone to sh*t just like that.",
                            "Excuse me, sir, are you alright? What happened?",
                            "*Sniff* I was the best man on their team. The BEST! Thrown out on the road like trash!",
                            "It's over for me. IT'S OVERRRR!!! WAAAAAAAAAHHHHH!!!!!!!!!!!!!!!!!!",
                            "Maybe it's best to leave him alone..."
                    };
                    gp.dialogueSpeakers = new String[]{
                            "",
                            "Tearful Man",
                            "Tearful Man",
                            "Player",
                            "Tearful Man",
                            "Tearful Man",
                            ""
                    };
                    gp.dialogueIndex = 0;
                    gp.firedManTalkDone = true;
                }
            }
                if (distance < 1 * gp.tileSize && npc == gp.pinkGirl) {
                    if(keyH.interactPressed && !gp.pinkGirlTalkDone) {
                        gp.gameState = gp.dialogueState;
                        gp.dialogueLines = new String[]{
                                "You see a fashionable girl with pink hair.",
                                "Hey, I love your outfit. It's very... pink.",
                                "Thanks! Gotta look good for my first day at the office!",
                                "Wow, good luck! What do you work with?",
                                "Oh, I'm a software engineer.",
                                "That's awesome! I just got into a new school and I'll need to pick up some programming skills as well.",
                                "Then let me give you a tip...",
                                "Whatever you do, don't forget to add the SEMICOLON at the end of each code statement!"
                        };
                        gp.dialogueSpeakers = new String[]{
                                "",
                                "Player",
                                "Pink Girl",
                                "Pink Girl",
                                "Player",
                                "Pink Girl",
                                "Player",
                                "Pink Girl",
                                "Pink Girl"
                        };
                        gp.dialogueIndex = 0;
                        gp.pinkGirlTalkDone = true;
                    }
                }

                if (distance < 1 * gp.tileSize && npc == gp.spidermanKid) {
                    if(keyH.interactPressed && !gp.spidermanKidTalkDone) {
                        gp.gameState = gp.dialogueState;
                        gp.dialogueLines = new String[]{
                                "A kid dressed as Spiderman excitedly flaps his arms around.",
                                "What are you doing here alone?",
                                "The city needs me!",
                                "Kid, where are your parents?",
                                "They were taken by the Green Goblin!",
                                "Are you... here alone?",
                                "Please don't tell my mom...",
                                "...",
                                "Look, I made this potion infused with my spider DNA that'll protect you from bad guys.",
                                "I'll give it to you if you promise not to tell?",
                                "This kid is totally lost in fantasy."
                        };
                        gp.dialogueSpeakers = new String[]{
                                "",
                                "Player",
                                "Kid",
                                "Player",
                                "Kid",
                                "Player",
                                "Kid",
                                "Kid",
                                "Kid",
                                "Kid",
                                ""
                        };
                        gp.dialogueIndex = 0;
                        gp.spidermanKidTalkDone = true;
                    }

                }
            }
        keyH.interactPressed = false;
        if(!exitTrain)
        {
            if(gp.firedManTalkDone && gp.pinkGirlTalkDone && gp.spidermanKidTalkDone && gp.spiderKidPotionGiven && gp.gameState != gp.dialogueState && gp.gameState != gp.itemPopupState)
            {
                gp.completeFade(
                        new String[]{"You leave the train."},
                        () -> {
                            gp.enterNewTrain();
                        },
                        () -> {
                            gp.gameState = gp.dialogueState;
                            gp.dialogueLines = new String[]{
                                    "The station is eerily empty.",
                                    "Was this the right stop?"
                            };
                            gp.dialogueSpeakers = new String[]{
                                    "",
                                    ""
                            };
                            exitTrain = true;
                        }
                );

            }
        }

    }

    public void playAnimation() {
        if (currentAnimation == AnimationType.NONE) {
            return;
        }

        animationCounter++;

        switch (currentAnimation) {
            case BOUNCE:
                animationDone = false;
                double t = (double) animationCounter / animationMax;
                animationOffsetY = (int)(-4 * t * (1-t) * 10);
                break;
            case SPIN:
                //do something eventually?

        }
        if (animationCounter >= animationMax) {
            animationCounter = 0;
            animationOffsetY = 0;
            currentAnimation = AnimationType.NONE;
            gp.gameState = gp.dialogueState;
            animationDone = true;
            if (triggerLetterReactionDialogue) {
                gp.dialogueLines = new String[] {
                        "You can't believe you made it!",
                        "You should go pack your things ASAP!",
                        "Walk up to the important items in your room and press F to pack them."
                };
                gp.dialogueIndex = 0;
                triggerLetterReactionDialogue = false;
                gp.dialogueFinished = true;
            }
            holdingLetter = false;


        }
    }


    public void draw(Graphics2D g2) {

         //    g2.setColor(Color.white);
        //    g2.fillRect(x, y, gp.tileSize, gp.tileSize);
        int screenX = worldX - gp.camWorldX + gp.player.screenX;
        int screenY = worldY - gp.camWorldY + gp.player.screenY;


        BufferedImage image = null;
        if (holdingLetter && gp.gameState == gp.dialogueState) {
            image = openletter;
        }else if(firstBattleLost && !stoutDefeated) {
            if(!gp.siriusMet)
                image = hurt;
            else
                image = up2;
        } else if (currentAnimation == AnimationType.BOUNCE) {
            image = down2;
        } else {
            switch (direction) {
                case "up":
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                    if (spriteNum == 3) {
                        image = up3;
                    }

                    if(!gp.keyH.upPressed && !gp.keyH.downPressed && !gp.keyH.rightPressed && !gp.keyH.leftPressed)
                    {
                        image = up2;
                    } else if(gp.gameState == gp.dialogueState)
                    {
                        image = up2;
                    }

                    break;
                case "down":
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                    if (spriteNum == 3) {
                        image = down3;
                    }

                    if(!gp.keyH.upPressed && !gp.keyH.downPressed && !gp.keyH.rightPressed && !gp.keyH.leftPressed)
                    {
                        image = down2;
                    } else if(gp.gameState == gp.dialogueState)
                    {
                        image = down2;
                    }


                    break;
                case "left":
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                    if (spriteNum == 3) {
                        image = left3;
                    }

                    if(!gp.keyH.upPressed && !gp.keyH.downPressed && !gp.keyH.rightPressed && !gp.keyH.leftPressed)
                    {
                        image = left2;
                    } else if(gp.gameState == gp.dialogueState)
                    {
                        image = left2;
                    }


                    break;
                case "right":
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                    if (spriteNum == 3) {
                        image = right3;
                    }

                    if(!gp.keyH.upPressed && !gp.keyH.downPressed && !gp.keyH.rightPressed && !gp.keyH.leftPressed)
                    {
                        image = right2;
                    } else if(gp.gameState == gp.dialogueState)
                    {
                        image = right2;
                    }

            }

        }
        g2.drawImage(image, screenX, screenY + animationOffsetY, gp.tileSize, gp.tileSize, null);

//        if(!stoutAnimationDone && 0 < stoutCounter && stoutCounter <= stoutMax)
//        {
//            g2.drawImage(image, screenX + 10, screenY + stoutAnimationOffsetY, gp.tileSize, gp.tileSize, null);
//        }


    }
}