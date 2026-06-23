package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, rightPressed, leftPressed;
    public boolean spacePressed;
    public boolean interactPressed;
    public boolean testPressed;
    public boolean testBattlePressed;
    public boolean enterPressed;
    public boolean escapePressed;

    public char lastTypedChar = 0;

    @Override
    public void keyTyped(KeyEvent e) {
        lastTypedChar = e.getKeyChar();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) { //if W is pressed
            upPressed = true;
        }

        if (code == KeyEvent.VK_A) { //if A is pressed
            leftPressed = true;
        }

        if (code == KeyEvent.VK_S) { //if S is pressed
            downPressed = true;
        }

        if (code == KeyEvent.VK_D) { //if D is pressed
            rightPressed = true;
        }

        if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
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

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) { //if W is pressed
            upPressed = false;
        }

        if (code == KeyEvent.VK_A) { //if A is pressed
            leftPressed = false;
        }

        if (code == KeyEvent.VK_S) { //if S is pressed
            downPressed = false;
        }

        if (code == KeyEvent.VK_D) { //if D is pressed
            rightPressed = false;
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
