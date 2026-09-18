package object;

import entity.Player;
import entity.Stout;
import main.GamePanel;
import org.w3c.dom.css.Rect;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SuperObject {

    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public double worldX;
    public double worldY;
    public Rectangle solidArea = new Rectangle();
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public boolean dialogueShown = false;
    public int displayWidth;
    public int displayHeight;

    public int animationOffsetY = 0;
    public int animationOffsetX = 0;

    public int flameSpriteNum = 0;
    public boolean flameGoingForward = true;
    public int flameSpriteCounter = 0;
    public BufferedImage[] flameFrames;


    public SuperObject(String name, double worldX, double worldY, boolean collision) {
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
        this.collision = collision;

        loadImage(name);

        loadFlameFrames();

        if (image != null) {
            switch (name) {
                case "bedbigger":
                    this.solidArea = new Rectangle(10, 10, image.getWidth()-20, image.getHeight() - 10);
                    break;
                case "forestTree":
                    this.solidArea = new Rectangle(10, 10, (int)(image.getWidth()*1.6), (int)(image.getHeight()*1.6));
                    break;
                case "forestBorder":
                    this.solidArea = new Rectangle(10, 10, (int)(image.getWidth()*1.6), (int)(image.getHeight()*1.6));
                    break;
                case "vendingMachine":
                    this.solidArea = new Rectangle(0, 0, (int)(image.getWidth()*3), (int)(image.getHeight()*2));
                    break;
                case "vendingMachineBackFull":
                    this.solidArea = new Rectangle(0, 0, (int)(image.getWidth()*3), (int)(image.getHeight()*3));
                    break;
                case "bench":
                    this.solidArea = new Rectangle(0, 0, (int)(image.getWidth()*3), (int)(image.getHeight()*2));
                    break;
                case "trashCan":
                    this.solidArea = new Rectangle(0, 0, (int)(image.getWidth()*2), (int)(image.getHeight()*2));
                    break;
                default:
                    this.solidArea = new Rectangle(0, 0, image.getWidth(), image.getHeight());
            }
        }

        this.solidAreaDefaultX = solidArea.x;
        this.solidAreaDefaultY = solidArea.y;

    }

    private void loadImage(String name) {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/" + name + ".png"));
            displayWidth = image.getWidth();
            displayHeight = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFlameFrames() {
        flameFrames = new BufferedImage[3];
        flameFrames[0] = image; // already loaded via loadImage(name) in constructor
        try {
            flameFrames[1] = ImageIO.read(getClass().getResourceAsStream("/objects/castleFlame2.png"));
            flameFrames[2] = ImageIO.read(getClass().getResourceAsStream("/objects/castleFlame3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2, GamePanel gp) {

        if (name.equals("castleFlame1")) {
            image = flameFrames[flameSpriteNum];
        }


        double screenX = worldX - gp.camWorldX + gp.player.screenX;
        double screenY = worldY - gp.camWorldY + gp.player.screenY;
        double imageWidth = displayWidth;
        double imageHeight = displayHeight;


        // Only draw if the object is visible on the screen
        if (worldX + imageWidth > gp.camWorldX - gp.player.screenX &&
                worldX < gp.camWorldX + gp.player.screenX + gp.tileSize &&
                worldY + imageHeight > gp.camWorldY - gp.player.screenY &&
                worldY < gp.camWorldY + gp.player.screenY + gp.tileSize) {


            // Animation offsets
            g2.drawImage(image,
                    (int) (screenX + animationOffsetX),
                    (int) (screenY + animationOffsetY),
                    (int) imageWidth,
                    (int) imageHeight,
                    null);
        }

    }

    public void update() {
        if(flameSpriteCounter < 30) {
            flameSpriteCounter++;
        } else {
            flameSpriteCounter = 0;
            if(flameGoingForward)
            {
                if(flameSpriteNum < 2) {
                    flameSpriteNum++;
                } else {
                    flameGoingForward = false;
                    flameSpriteNum--;
                }
            } else {
                if(flameSpriteNum > 0) {
                    flameSpriteNum--;
                } else {
                    flameGoingForward = true;
                    flameSpriteNum++;
                }
            }
        }
    }



}

