package entity;

import java.awt.image.BufferedImage;

public class Enemy extends Entity {
    public int maxHp;
    public int attack;
    public BufferedImage[] idleFrames;
    public BufferedImage[] attackFrames;
    public BufferedImage[] damagedFrames;

    
    public Enemy(String name, int maxHp, int attack)
    {
        this.name = name;
        this.maxHp = maxHp;
        this.attack = attack;
        this.hp = maxHp;
    }
}
