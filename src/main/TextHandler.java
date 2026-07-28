package main;

import java.awt.*;
import java.util.Objects;

public class TextHandler {

    main.GamePanel gp;

    public TextHandler(main.GamePanel gp) {
        this.gp = gp;
    }

    public void drawDialogueBox(Graphics2D g2) {

        int x = gp.tileSize;
        int y = gp.screenHeight - gp.tileSize * 2;
        int width = gp.screenWidth - 100;
        int height = 100;
        int lineHeight = 40;

        if(!gp.currentSpeaker.isEmpty())
        {
            g2.setFont(new Font("Arial", Font.PLAIN, 24));
            g2.setColor(new Color(100, 75, 30));
            g2.fillRoundRect(x, y - 35, g2.getFontMetrics().stringWidth(gp.currentSpeaker) + 20, 35, 10, 10 );

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(x, y - 35, g2.getFontMetrics().stringWidth(gp.currentSpeaker) + 20, 35, 10, 10);
            g2.setColor(Color.WHITE);

            g2.drawString(gp.currentSpeaker, x + 10, y - 12);

        }


        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, width, height, 35, 35);




        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, width, height, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 24));

        FontMetrics fm = g2.getFontMetrics();
        int maxTextWidth = width - 60;

        String[] words = gp.currentDialogue.split(" ");
        String currentLine = "";
        int lineIndex = 0;

        for (String word : words) {
            String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
            if(fm.stringWidth(testLine) < maxTextWidth)
            {
                currentLine = testLine;
            } else {
                g2.drawString(currentLine, x + 30, y + 40 + (lineIndex * lineHeight));
                lineIndex++;
                currentLine = word;
            }

        }

        g2.drawString(currentLine, x + 30, y + 40 + (lineIndex * lineHeight));

        g2.setColor(Color.white);

    }
}
