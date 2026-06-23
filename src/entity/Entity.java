package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    public String name;
    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3, openletter;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    public int hp;
    public int maxHp;

    public BufferedImage icon;
}
