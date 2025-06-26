package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public Player (GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(16, 32, 48, 48);

        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues () {
        worldX = gp.tileSize * 10;
        worldY = gp.tileSize * 8;
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


    }
    public void draw(Graphics2D g2) {

         //    g2.setColor(Color.white);
        //    g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

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

        }   g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

    }
}