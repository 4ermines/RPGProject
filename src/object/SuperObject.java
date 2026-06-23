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


    public SuperObject(String name, double worldX, double worldY, boolean collision) {
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
        this.collision = collision;

        loadImage(name);

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

    public void draw(Graphics2D g2, GamePanel gp) {
        double screenX = worldX - gp.player.worldX + gp.player.screenX;
        double screenY = worldY - gp.player.worldY + gp.player.screenY;
        double imageWidth = displayWidth;
        double imageHeight = displayHeight;

        // Only draw if the object is visible on the screen
        if (worldX + imageWidth > gp.player.worldX - gp.player.screenX &&
                worldX < gp.player.worldX + gp.player.screenX + gp.tileSize &&
                worldY + imageHeight > gp.player.worldY - gp.player.screenY &&
                worldY < gp.player.worldY + gp.player.screenY + gp.tileSize) {

            // FIX: Added the animation offsets directly inside your existing conditional draw statement
            g2.drawImage(image,
                    (int) (screenX + animationOffsetX),
                    (int) (screenY + animationOffsetY),
                    (int) imageWidth,
                    (int) imageHeight,
                    null);
        }
    }



}

