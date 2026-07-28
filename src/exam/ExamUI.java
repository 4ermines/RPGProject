package exam;

import main.GamePanel;

import java.awt.*;

public class ExamUI {

    GamePanel gp;
    Font monogram;

    public ExamUI(GamePanel gp)
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
        if(gp.examSystem.phase == ExamSystem.ExamPhase.TYPING) {
            drawChallengePrompt(g2);
        }
        else if(gp.examSystem.phase == ExamSystem.ExamPhase.SHOW_RESULT) {
            drawExamLog(g2);
        }
        else if(gp.examSystem.phase == ExamSystem.ExamPhase.EXAM_FAILED || gp.examSystem.phase == ExamSystem.ExamPhase.EXAM_PASSED) {
            drawResultsScreen(g2);
        }
    }

    private void drawChallengePrompt(Graphics2D g2)
    {
        long timeElapsed = System.currentTimeMillis() - gp.examSystem.challengeStartTime;
        long timeLeft = (gp.examSystem.currentChallenge.timeLimit * 1000) - timeElapsed;

        g2.setColor(new Color(163, 157, 170));
        g2.fillRect(gp.tileSize, (int)(8.5 * gp.tileSize), gp.screenWidth - (2*gp.tileSize), (int)(2.5 * gp.tileSize));
        g2.setColor(Color.BLACK);
        String challengeType = "Challenge type " + gp.examSystem.currentChallenge.type;
        g2.setFont(new Font("Courier New", Font.PLAIN, 18));
        FontMetrics fm = g2.getFontMetrics();
        if(gp.examSystem.currentChallenge != null)
            g2.drawString("Time remaining: " + timeLeft/1000, 11 * gp.tileSize + 10, (int)(8.5 * gp.tileSize) + 30);
        g2.drawString("Question " + gp.examSystem.totalTally + "/" + gp.examSystem.totalQuestions, gp.tileSize + 10, (int)(8.5 * gp.tileSize) + 30);
        g2.drawString(challengeType, gp.tileSize + 10, (int)(9 * gp.tileSize) + 30);
        g2.drawString(gp.examSystem.currentChallenge.prompt, gp.tileSize + 10, (int)(9 * gp.tileSize) + fm.getHeight()*2 + 20);

        g2.drawString(gp.handler.getText() + "|", gp.tileSize + 10, (int)(9 * gp.tileSize) + fm.getHeight()*3 + 30);
    }

    private void drawExamLog(Graphics2D g2)
    {
        if(!gp.examSystem.examLog.isEmpty())
        {
            g2.setColor(new Color(21, 19, 32));
            g2.fillRoundRect(gp.screenWidth / 2 - 3 * gp.tileSize, 6 * gp.tileSize, 6 * gp.tileSize, 2 * gp.tileSize, 35, 35);

            g2.setColor(new Color(108, 102, 138));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(gp.screenWidth / 2 - 3 * gp.tileSize, 6 * gp.tileSize, 6 * gp.tileSize, 2 * gp.tileSize, 35, 35);

            g2.setColor(Color.WHITE);
            g2.setFont(monogram);
            FontMetrics fm = g2.getFontMetrics();
            String[] examLogList = gp.examSystem.examLog.split(" ");
            int textX = gp.screenWidth / 2 - 3 * gp.tileSize + 20;
            int textY = 6 * gp.tileSize + 30 ; // starting Y with padding

            String temp = "";
            for(String word : examLogList)
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

    public void drawResultsScreen(Graphics2D g2)
    {
        Color normalColor = new Color(32, 30, 51);
        g2.setColor(normalColor);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.WHITE);
        Font largeMonogram = monogram.deriveFont(96.0f);
        g2.setFont(largeMonogram);
        FontMetrics fm = g2.getFontMetrics();
        String text = (gp.examSystem.phase == ExamSystem.ExamPhase.EXAM_PASSED) ? "You passed!" : "You failed...";
        int x = gp.screenWidth/2 - fm.stringWidth(text)/2;
        int y = gp.screenHeight/2 - 20;

        g2.drawString(text, x, y);

        Font mediumMonogram = monogram.deriveFont(48.0f);
        g2.setFont(mediumMonogram);
        String spaceToCont = "Press space to continue";
        fm = g2.getFontMetrics();

        g2.drawString(spaceToCont, gp.screenWidth/2 - fm.stringWidth(spaceToCont)/2, gp.screenHeight/2 + 40);

    }



}
