package main;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Inventory {

    GamePanel gp;
    public int slotCol = 0;
    public int slotRow = 0;

    public Inventory(GamePanel gp)
    {
        this.gp = gp;
    }


    public void drawInventory(Graphics2D g2) {

        // BACKGROUND SQUARE
        int bgX = gp.tileSize * 2;
        int bgY = (int) (gp.tileSize * 1.5);
        int bgWidth = gp.tileSize * 12;
        int bgHeight = gp.tileSize * 9;

        drawSubWindow(bgX, bgY, bgWidth, bgHeight, g2);

        // ITEM SELECTION FRAME
        int frameX = gp.tileSize * 7;
        int frameY = (int) (gp.tileSize * 2.5);
        int frameWidth = (int) (gp.tileSize * 6);
        int frameHeight = gp.tileSize * 7;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight, g2);

        int buttonX = gp.tileSize * 2;
        int buttonY = (int) (gp.tileSize * 1.5);
        int buttonWidth = (int) (gp.tileSize * 0.8);
        int buttonHeight = (int) (gp.tileSize * 0.5);

        Color c = new Color(200, 36, 36);
        g2.setColor(c);
        g2.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 10, 10);

        c = new Color(117, 23, 23);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(buttonX+2, buttonY+2, buttonWidth-5, buttonHeight-5, 2,2);

        g2.setFont(new Font("Arial", Font.BOLD, 16));
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.drawString("ESC", buttonX+8, buttonY+22);

        // SLOT
        final int slotXstart = frameX + 32;
        final int slotYstart = frameY + 32;
        int slotX = slotXstart;
        int slotY = slotYstart;

        // DRAW PLAYER'S ITEMS
        for(int i = 0; i < gp.player.inventory.size(); i++) {
            g2.drawImage(gp.player.inventory.get(i).icon, slotX, slotY, gp.tileSize, gp.tileSize, null);

            slotX += gp.tileSize;

            if(i == 4 || i == 9 || i == 14 || i == 19 || i == 24) {
                slotX = slotXstart;
                slotY += gp.tileSize;
            }
        }

        // CURSOR
        int cursorX = slotXstart + (gp.tileSize * slotCol);
        int cursorY = slotYstart + (gp.tileSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;
        // DRAW CURSOR
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

    }

    public void drawSubWindow(int x, int y, int width, int height, Graphics2D g2) {
        Color c = new Color(0, 0, 0,220);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25,25);
    }

}
