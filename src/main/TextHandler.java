package main;

import java.awt.*;

public class TextHandler {

    GamePanel gp;

    public TextHandler(GamePanel gp) {
        this.gp = gp;
    }

    public void drawDialogueBox(Graphics2D g2) {

        int x = gp.tileSize;
        int y = gp.screenHeight - gp.tileSize * 2;
        int width = gp.screenWidth - 100;
        int height = 100;
        int lineHeight = 40;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, width, height, 35, 35);


        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, width, height, 35, 35);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        String fullText = gp.currentDialogue;
        String[] lines = fullText.split("\n");
        for (int i = 0; i < lines.length; i++) {
            g2.drawString(lines[i], x + 30, y + 40+(i * lineHeight));
        }


        g2.setColor(Color.white);

    }
}
