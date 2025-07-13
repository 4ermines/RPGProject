package entity;

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
    private boolean fadeTriggered = false;


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
            int objIndex = gp.cChecker.checkObject(this, true);

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
                                    "Press 'F' to interact"
                            };
                            gp.obj[i].dialogueShown = true;
                            break;
                        }
                    } else if (distance < 4 * gp.tileSize) {
                        if (gp.obj[i].name.equals("envelope")) {
                            gp.gameState = gp.dialogueState;
                            gp.dialogueLines = new String[]{
                                    "You see a envelope near the front door.",
                                    "You weren't expecting mail. Best to check it out."
                            };
                            gp.obj[i].dialogueShown = true;
                            break;

                        }
                    }
                }
                if (packDone) {
                    gp.gameState = gp.playState;
                    if (distance < gp.tileSize) {
                        if (gp.obj[i].name.equals("door1") && i == 3) {
                            gp.enterTrain();
                        }
                    }
                }
                if (distance < 1.2*gp.tileSize && gp.obj[i] != null) {
                    if (gp.obj[i].name.equals("traindoor") && !fadeTriggered) {
                        fadeTriggered = true;
                        gp.completeFade(
                                new String[]{"You board the train."},
                                () -> {
                                    gp.enterNewTrain();
                                },
                                () -> {
                                    gp.dialogueIndex = 0;
                                    gp.gameState = gp.dialogueState;
                                    gp.dialogueLines = new String[]{
                                            "The station is eerily empty.",
                                            "Did you take the right train?"
                                    };
                                }
                        );
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
                                    "Congratulations! After careful evaluation, we are excited to offer \nyou a place at Magicode Academy!",
                                    "By attending our school, you'll be surrounded by a community of scholars \nand have the chance to sharpen your magic skills."
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
                                            "You can't imagine leaving without your personal items. A phone \nand a notebook is a must!",
                                    };
                                    gp.dialogueIndex = 0;
                                    break;
                                }
                            }
                        }
                        if (gp.obj[5] == null && gp.obj[10] == null && gp.obj[11] == null) {
                            gp.gameState = gp.dialogueState;
                            gp.dialogueLines = new String[]{
                                    "Nice! You've got everything you need. Hurry and catch the train!"
                            };

                            packDone = true;
                        }
                    }
                }
            }
        }
        keyH.interactPressed = false;
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
                        "You should go pack your things ASAP!"
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

        BufferedImage image = null;
        if (holdingLetter && gp.gameState == gp.dialogueState) {
            image = openletter;
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
            }

        }
        g2.drawImage(image, screenX, screenY + animationOffsetY, gp.tileSize, gp.tileSize, null);


    }
}