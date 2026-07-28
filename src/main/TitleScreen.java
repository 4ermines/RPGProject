package main;

import java.awt.*;

public class TitleScreen {

    public int selectedOption;
    String[] options = {"New Game", "Quit"};
    GamePanel gp;
    MouseHandler mouseH;

    boolean isError = false;
    int errorTimer = 0;
    int errorMax = 200;

    public TitleScreen(GamePanel gp, MouseHandler mouseH) {
        this.gp = gp;
        this.mouseH = mouseH;
    }

    public void drawTitleScreen(Graphics2D g2) {
        g2.setColor(new Color(8, 5, 15));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Georgia", Font.BOLD, 64));
        String title = "Unnamed RPG Project";
        int titleX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(title) / 2;
        int titleY = gp.screenHeight / 2 - 60;
        g2.drawString(title, titleX, titleY);

        // Tagline
        g2.setFont(new Font("Georgia", Font.ITALIC, 22));
        g2.setColor(new Color(108, 102, 138));
        String sub = "An educational, story-driven RPG.";
        int subX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(sub) / 2;
        g2.drawString(sub, subX, titleY + 45);

        // Play button
        int btnW = 180;
        int btnH = 52;
        int btnX = gp.screenWidth / 2 - btnW / 2;
        int btnY = gp.screenHeight / 2 + 60;
        drawButton(g2, "PLAY", btnX, btnY, btnW, btnH);

        if (mouseH.mousePressed) {
            if (mouseH.mouseX >= btnX && mouseH.mouseX <= btnX + btnW
                    && mouseH.mouseY >= btnY && mouseH.mouseY <= btnY + btnH) {
                gp.gameState = gp.saveSelectState;
                mouseH.mousePressed = false;
            }
        }
    }

    public void drawSaveSelectScreen(Graphics2D g2)
    {

        g2.setColor(new Color(8, 5, 15));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Georgia", Font.BOLD, 64));
        String title = "Save Select";
        int titleX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(title) / 2;
        int titleY = gp.screenHeight / 2 - 60;
        g2.drawString(title, titleX, titleY);

        // New Game button
        int newbtnW = 180;
        int newbtnH = 52;
        int newbtnX = gp.screenWidth / 2 - newbtnW / 2 - 120;
        int newbtnY = gp.screenHeight / 2;
        drawButton(g2, "NEW GAME", newbtnX, newbtnY, newbtnW, newbtnH);

        // Continue button
        int continuebtnW = 180;
        int continuebtnH = 52;
        int continuebtnX = gp.screenWidth / 2 - continuebtnW / 2 + 120;
        int continuebtnY = gp.screenHeight / 2;
        drawButton(g2, "CONTINUE", continuebtnX, continuebtnY, continuebtnW, continuebtnH);

        if (mouseH.mousePressed) {

            if (mouseH.mouseX >= newbtnX && mouseH.mouseX <= newbtnX + newbtnW
                    && mouseH.mouseY >= newbtnY && mouseH.mouseY <= newbtnY + newbtnH) {
                //todo: start a new game
                //gp.gameState = gp.playState;
                gp.startFadeFromBlack(
                    new String[]{"You wake up. The sun shines brightly in your face."},
                    () -> {
                         gp.dialogueLines = new String[]{
                                  "Something tells you it's going to be a good day. Press space to continue.",
                                   "Use WASD to move."
                         };
                         gp.dialogueIndex = 0;
                         gp.gameState = gp.dialogueState;
                }
        );

                mouseH.mousePressed = false;
            }

            if (mouseH.mouseX >= continuebtnX && mouseH.mouseX <= continuebtnX + continuebtnW
                    && mouseH.mouseY >= continuebtnY && mouseH.mouseY <= continuebtnY + continuebtnH) {
                SaveData data = SaveLoadManager.load(0);
                if(data != null)
                {
                    gp.loadGame(data);
                } else {
                    isError = true;
                }
                mouseH.mousePressed = false;
            }
        }

        if(isError)
        {
            errorTimer++;
            if(errorTimer >= errorMax)
            {
                isError = false;
                errorTimer = 0;
            }
        }

        if(isError)
        {
            String error = "No save file found!";
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(new Color(137, 131, 175));
            g2.drawString(error, gp.screenWidth / 2 - fm.stringWidth(error) / 2, continuebtnY + 160);

        }

    }

    //HELPER
    private void drawButton(Graphics2D g2, String label, int x, int y, int w, int h) {
        boolean hovered = mouseH.mouseX >= x && mouseH.mouseX <= x + w
                && mouseH.mouseY >= y && mouseH.mouseY <= y + h;

        g2.setColor(hovered ? new Color(137, 131, 175) : new Color(108, 102, 138));
        g2.fillRoundRect(x, y, w, h, 12, 12);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Georgia", Font.BOLD, 20));
        int labelX = x + w / 2 - g2.getFontMetrics().stringWidth(label) / 2;
        int labelY = y + h / 2 + 7;
        g2.drawString(label, labelX, labelY);
    }



}
