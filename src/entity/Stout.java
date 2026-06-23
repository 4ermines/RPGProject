package entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Stout extends Enemy{

    //animation
    int idleFrameCount = 8;
    int attackFrameCount = 0;
    int damagedFrameCount = 15;

    public Stout()
    {
        super("Stout", 100, 10);
        loadEnemyAnimations();
    }

    public void loadEnemyAnimations() {
        idleFrames = new BufferedImage[idleFrameCount];
        attackFrames = new BufferedImage[attackFrameCount];
        damagedFrames = new BufferedImage[damagedFrameCount];

        for (int i = 0; i < idleFrameCount; i++) {
            try {
                idleFrames[i] = ImageIO.read(getClass().getResourceAsStream(
                        "/battle/enemies/idle_" + String.format("%03d", i + 1) + ".png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < attackFrameCount; i++) {
            try {
                attackFrames[i] = ImageIO.read(getClass().getResourceAsStream(
                        "/battle/enemies/attack_" + String.format("%03d", i + 1) + ".png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < damagedFrameCount; i++) {
            try {
                damagedFrames[i] = ImageIO.read(getClass().getResourceAsStream(
                        "/battle/enemies/damage_" + String.format("%03d", i + 1) + ".png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
