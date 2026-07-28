package training;

import main.GamePanel;

import java.awt.*;

public class TrainingUI {

    GamePanel gp;
    Font monogram;

    public TrainingUI(GamePanel gp)
    {
        this.gp = gp;
        try {
            monogram = Font.createFont(Font.TRUETYPE_FONT,
                            getClass().getResourceAsStream("/fonts/monogram.ttf"))
                    .deriveFont(24f);

        } catch (Exception e) {
            e.printStackTrace();
            monogram = new Font("Arial", Font.PLAIN, 24);
        }
    }

    public void draw(Graphics2D g2)
    {
        if(gp.trainingSystem.phase == TrainingSystem.TrainingPhase.TYPING) {
            drawChallengePrompt(g2);
        }
        else if(gp.trainingSystem.phase == TrainingSystem.TrainingPhase.SHOW_RESULT) {
            drawExamLog(g2);
        }
        else if(gp.trainingSystem.phase == TrainingSystem.TrainingPhase.QUIT_SCREEN) {
            drawQuitScreen(g2);
        }
    }

    private void drawChallengePrompt(Graphics2D g2)
    {
        g2.setColor(new Color(163, 157, 170));
        g2.fillRect(gp.tileSize, (int)(8.5 * gp.tileSize), gp.screenWidth - (2*gp.tileSize), (int)(2.5 * gp.tileSize));
        g2.setColor(Color.BLACK);
        String challengeType = "Challenge type " + gp.trainingSystem.currentChallenge.type;
        g2.setFont(new Font("Courier New", Font.PLAIN, 18));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(challengeType, gp.tileSize + 10, (int)(8.5 * gp.tileSize) + 30);
        g2.drawString(gp.trainingSystem.currentChallenge.prompt, gp.tileSize + 10, (int)(8.5 * gp.tileSize) + fm.getHeight()*2 + 20);

        g2.drawString(gp.handler.getText() + "|", gp.tileSize + 10, (int)(9 * gp.tileSize) + fm.getHeight()*3 + 30);
    }

    private void drawExamLog(Graphics2D g2)
    {
        if(!gp.trainingSystem.trainingLog.isEmpty())
        {
            g2.setColor(new Color(21, 19, 32));
            g2.fillRoundRect(gp.screenWidth / 2 - 3 * gp.tileSize, 6 * gp.tileSize, 6 * gp.tileSize, 2 * gp.tileSize, 35, 35);

            g2.setColor(new Color(108, 102, 138));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(gp.screenWidth / 2 - 3 * gp.tileSize, 6 * gp.tileSize, 6 * gp.tileSize, 2 * gp.tileSize, 35, 35);

            g2.setColor(Color.WHITE);
            g2.setFont(monogram);
            FontMetrics fm = g2.getFontMetrics();
            String[] trainingLogList = gp.trainingSystem.trainingLog.split(" ");
            int textX = gp.screenWidth / 2 - 3 * gp.tileSize + 20;
            int textY = 6 * gp.tileSize + 30 ; // starting Y with padding

            String temp = "";
            for(String word : trainingLogList)
            {
                if(fm.stringWidth(temp + word) < 6 * gp.tileSize - 40)
                {
                    temp += word + " ";
                } else {
                    g2.drawString(temp, textX, textY);
                    temp = word + " ";
                    textY += fm.getHeight();
                }
            }
            g2.drawString(temp, textX, textY);
        }
    }

    public void drawQuitScreen(Graphics2D g2)
    {
        Color normalColor = new Color(32, 30, 51);
        Color boxColor = new Color(68, 66, 110);
        Color highlightColor = new Color(100, 90, 180);
        g2.setFont(monogram);

        int popupWidth = (int) (6 * gp.tileSize);
        int popupHeight = (int) (4 * gp.tileSize);

        int outerX = gp.screenWidth / 2 - popupWidth / 2;
        int outerY = gp.screenHeight / 2 - popupHeight / 2;

        //outer rectangle
        g2.setColor(boxColor);
        g2.fillRoundRect(outerX, outerY, popupWidth, popupHeight, 20, 20);
        g2.setColor(normalColor);
        g2.drawRoundRect(outerX, outerY, popupWidth, popupHeight, 20, 20);

        int buttonWidth = (int) (1.5 * gp.tileSize);
        int buttonHeight = (int) (0.75 * gp.tileSize);
        int buttonY = outerY + 2 * gp.tileSize;

        if (gp.trainingSystem.selectedAction == 0) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect((int) (outerX + (1 * gp.tileSize)), buttonY, buttonWidth, buttonHeight, 10, 10);

        if (gp.trainingSystem.selectedAction == 1) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect((int) (outerX + 3.5 * gp.tileSize), buttonY, buttonWidth, buttonHeight, 10, 10);

        //draw text
        g2.setColor(Color.WHITE);

        FontMetrics fm = g2.getFontMetrics();

        //stolen from battle ui and idk what im even doing for the x and y
        String quitQuestion = "Would you like to quit training now?";
        int quitX = outerX + (popupWidth - fm.stringWidth(quitQuestion)) / 2;
        int quitY = outerY + (popupHeight - fm.getHeight()) / 2 + fm.getAscent() - gp.tileSize;
        g2.drawString(quitQuestion, quitX, quitY);

        g2.drawString("Yes", (int) (outerX + (1 * gp.tileSize)) + 32, buttonY + 30);

        g2.drawString("No", (int) (outerX + (3.5 * gp.tileSize)) + 40, buttonY + 30);


    }

}
