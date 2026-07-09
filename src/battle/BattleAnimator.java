package battle;

import entity.Enemy;

import java.awt.image.BufferedImage;

public class BattleAnimator {

    BufferedImage[] idleFrames;
    int animFrame = 0;
    int animCounter = 0;
    int animSpeed = 10;

    public enum EnemyAnim {
        IDLE,
        ATTACK,
        DAMAGED
    }

    public EnemyAnim currentAnim = EnemyAnim.IDLE;

    public void update(Enemy enemy)
    {
        animCounter++;
        if(animCounter >= animSpeed) {
            animCounter = 0;
            animFrame++;

            int frameCount;
            switch (currentAnim)
            {
                case IDLE:
                    frameCount = enemy.idleFrames.length;
                    if(animFrame >= frameCount) animFrame = 0;
                    break;
                case ATTACK:
                    frameCount = enemy.attackFrames.length;
                    if(animFrame >= frameCount) {
                        animFrame = 0;
                        currentAnim = EnemyAnim.IDLE;
                    }
                    break;
                case DAMAGED:
                    frameCount = enemy.damagedFrames.length;
                    if(animFrame >= frameCount) {
                        animFrame = 0;
                        currentAnim = EnemyAnim.IDLE;
                    }
                    break;
            }
        }


//        System.out.println(animFrame);


    }

    public void playOnce(EnemyAnim anim)
    {
        currentAnim = anim;
        animFrame = 0;
    }

    public BufferedImage getCurrentFrame(Enemy enemy) {
        switch(currentAnim) {
            case IDLE:
                return enemy.idleFrames[animFrame];
            case ATTACK:
                if (enemy.attackFrames == null || enemy.attackFrames.length == 0)
                    return enemy.idleFrames[0]; // fallback to idle
                return enemy.attackFrames[animFrame];

            case DAMAGED:
                return enemy.damagedFrames[animFrame];
        }
        return null;
    }

    public boolean isAnimDone()
    {
        return currentAnim == EnemyAnim.IDLE;
    }

}

