package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.lang.Thread.sleep;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, rightPressed, leftPressed;
    public boolean spacePressed;
    public boolean interactPressed;
    public boolean testPressed;
    public boolean testBattlePressed;
    public boolean enterPressed;
    public boolean escapePressed;
    public boolean spaceIsActive = true;
    public boolean cursorUpActive = true;
    public boolean cursorDownActive = true;
    public boolean cursorLeftActive = true;
    public boolean cursorRightActive = true;

    GamePanel gp;

    public char lastTypedChar = 0;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        lastTypedChar = e.getKeyChar();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) { //if W is pressed
            upPressed = true;
            if (cursorUpActive && gp.gameState == gp.inventoryState && gp.inventory.slotRow != 0) {
                gp.inventory.slotRow--;
                cursorUpActive = false;
            }
        }

        if (code == KeyEvent.VK_A) { //if A is pressed
            leftPressed = true;
            if (cursorLeftActive && gp.gameState == gp.inventoryState && gp.inventory.slotCol != 0) {
                gp.inventory.slotCol--;
                cursorLeftActive = false;
            }
        }

        if (code == KeyEvent.VK_S) { //if S is pressed
            downPressed = true;
            if (cursorDownActive && gp.gameState == gp.inventoryState && gp.inventory.slotRow != 5) {
                gp.inventory.slotRow++;
                cursorDownActive = false;
            }
        }

        if (code == KeyEvent.VK_D) { //if D is pressed
            rightPressed = true;
            if (cursorRightActive && gp.gameState == gp.inventoryState && gp.inventory.slotCol != 4) {
                gp.inventory.slotCol++;
                cursorRightActive = false;
            }

        }

        if ((code == KeyEvent.VK_SPACE) && (spaceIsActive)) {
            spacePressed = true;
            spaceIsActive = false;
        }

        if (code == KeyEvent.VK_F) {
            interactPressed = true;
        }

        if (code == KeyEvent.VK_T) {
            testPressed = true;
        }

        if(code == KeyEvent.VK_Y)
        {
            testBattlePressed = true;
        }

        if(code == KeyEvent.VK_ENTER)
        {
            enterPressed = true;
        }

        if(code == KeyEvent.VK_ESCAPE)
        {
            escapePressed = true;
        }


    }
    public void activeSpace() {
            spaceIsActive = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) { //if W is pressed
            upPressed = false;
            cursorUpActive = true;
        }

        if (code == KeyEvent.VK_A) { //if A is pressed
            leftPressed = false;
            cursorLeftActive = true;
        }

        if (code == KeyEvent.VK_S) { //if S is pressed
            downPressed = false;
            cursorDownActive = true;
        }

        if (code == KeyEvent.VK_D) { //if D is pressed
            rightPressed = false;
            cursorRightActive = true;
        }

        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }

        if (code == KeyEvent.VK_F) {
            interactPressed = false;
        }

        if (code == KeyEvent.VK_T) {
            testPressed = false;
        }

        if(code == KeyEvent.VK_Y)
        {
            testBattlePressed = false;
        }

        if(code == KeyEvent.VK_ENTER)
        {
            enterPressed = false;
        }

        if(code == KeyEvent.VK_ESCAPE)
        {
            escapePressed = false;
        }

    }

}
