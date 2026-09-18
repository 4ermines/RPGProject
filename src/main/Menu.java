package main;

import java.awt.*;

public class Menu {

    GamePanel gp;
    int selectedButton = 0;

    public Menu(GamePanel gp) {
        this.gp = gp;
    }

    public void drawMenu(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int width = gp.tileSize * 3;
        int x = gp.screenWidth/2 - width/2;
        int height = (int) (gp.tileSize * 1);
        int padding = gp.tileSize + 20;

        int continueButtonY = gp.screenHeight/2 - height/2 - padding;
        int saveButtonY = gp.screenHeight/2 - height/2;
        int quitButtonY = gp.screenHeight/2 - height/2 + padding;

        drawButton(x, continueButtonY, width, height, "Continue", g2);
        drawButton(x, saveButtonY, width, height, "Save", g2);
        drawButton(x, quitButtonY, width, height, "Quit", g2);

        if(gp.mouseH.mousePressed) {
            if (gp.mouseH.mouseX >= x && gp.mouseH.mouseX <= x + width && gp.mouseH.mouseY >= continueButtonY && gp.mouseH.mouseY <= continueButtonY + height) {
                gp.gameState = gp.playState;
                gp.mouseH.mousePressed = false;
            }
            if (gp.mouseH.mouseX >= x && gp.mouseH.mouseX <= x + width && gp.mouseH.mouseY >= saveButtonY && gp.mouseH.mouseY <= saveButtonY + height) {
                SaveLoadManager.save(gp, 0);
                gp.mouseH.mousePressed = false;
            }
            if (gp.mouseH.mouseX >= x && gp.mouseH.mouseX <= x + width && gp.mouseH.mouseY >= quitButtonY && gp.mouseH.mouseY <= quitButtonY + height) {
                System.exit(0);
                gp.mouseH.mousePressed = false;
            }
        }
    }

    private void drawButton(int x, int y, int width, int height, String text, Graphics2D g2) {
        boolean hovered = gp.mouseH.mouseX >= x && gp.mouseH.mouseX <= x + width
                && gp.mouseH.mouseY >= y && gp.mouseH.mouseY <= y + height;

        Color normalColor = new Color(32, 30, 51, 255);
        Color highlightColor = new Color(52, 50, 83, 255);

        if (hovered) {
            g2.setColor(highlightColor);
        }
        else {
            g2.setColor(normalColor);
        }

        g2.fillRoundRect(x, y, width, height, 20, 20);

        Color border = new Color(137, 131, 175);
        g2.setColor(border);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 10,10);

        g2.setFont(new Font("Arial", Font.BOLD, 16));
        Color c = new Color(255, 255, 255);
        g2.setColor(c);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, x + width/2 - fm.stringWidth(text)/2, y + (height - (fm.getAscent() + fm.getDescent())) / 2 + fm.getAscent());
    }

    private void handleInput() {
    }

    public void update() {
        handleInput();
    }
}
