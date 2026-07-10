package battle;

import entity.PartyMember;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BattleUI {
    GamePanel gp;
    Font monogram;
    public BattleAnimator animator = new BattleAnimator();
    public BufferedImage background;



    public BattleUI(GamePanel gp)
    {
        this.gp = gp;
        try {
            monogram = Font.createFont(Font.TRUETYPE_FONT,
                            getClass().getResourceAsStream("/fonts/monogram.ttf"))
                    .deriveFont(24f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            monogram = new Font("Arial", Font.PLAIN, 24);
        }
    }

    public void draw(Graphics2D g2)
    {
        drawBackground(g2);
        animator.update(gp.battleSystem.currentEnemy);
        BufferedImage frame = animator.getCurrentFrame(gp.battleSystem.currentEnemy);
        if(frame != null) {
            int x = gp.screenWidth/2 - frame.getWidth()/2;
            int y = gp.screenHeight/7;
            g2.drawImage(frame, x, y, null);
        }

        drawBossHPBar(g2);
//        drawPlayerHPBar(g2);
        drawBattleLog(g2);
        drawInstructionsMenu(g2);

        if (gp.battleSystem.phase == BattleSystem.BattlePhase.PLAYER_TYPING) {
            drawChallengePrompt(g2);
        } else if (gp.battleSystem.phase == BattleSystem.BattlePhase.PLAYER_CHOOSE_SKILL) {
            drawSkillsMenu(g2);
            drawPartyPanel(g2);
        } else if (gp.battleSystem.phase == BattleSystem.BattlePhase.PLAYER_CHOOSE_ITEM)
        {
            drawItemsMenu(g2);
            drawPartyPanel(g2);
        } else if (gp.battleSystem.phase == BattleSystem.BattlePhase.BATTLE_WON || gp.battleSystem.phase == BattleSystem.BattlePhase.BATTLE_LOST )
        {
            drawResultsScreen(g2);
        }
        else {
            drawActionMenu(g2);
            drawPartyPanel(g2);
        }

        if(gp.battleSystem.isSiriusTip)
        {
            Color transparentBlack = new Color(0, 0, 0, 50);
            g2.setColor(transparentBlack);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

//            g2.drawImage(siriusBattleTip1, 0, 0, null);
            g2.drawImage(gp.siriusBattleTips[gp.battleSystem.battleDialogueIndex], 0, 0, null);

            g2.setColor(Color.BLACK);
            Font largeMonogram = monogram.deriveFont(36.0f);
            g2.setFont(largeMonogram);

            FontMetrics fm = g2.getFontMetrics();
            String[] battleDialogueList = gp.battleSystem.battleCurrentDialogue.split(" ");
            int textX = (int) (2.8*gp.tileSize);
            int textY = (int) (5.5*gp.tileSize);

            String temp = "";
            for(String word : battleDialogueList)
            {
                if(fm.stringWidth(temp + word) < 6.8 * gp.tileSize - 40)
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

    private void drawHPBar(Graphics2D g2, int x, int y, int w, int current, int max, Color fill) {
        g2.setColor(new Color(38, 33, 92));
        g2.fillRoundRect(x, y, w, 8, 4, 4);
        int filled = (int)((double) current / max * w);
        g2.setColor(fill);
        g2.fillRoundRect(x, y, filled, 8, 4, 4);
    }

    private void drawBackground(Graphics2D g2)
    {
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        try {
            background = ImageIO.read(getClass().getResourceAsStream("/battle/backgrounds/battleBG1.png"));

        }catch(IOException e) {
            e.printStackTrace();
        }
        if(background != null)
            g2.drawImage(background, 0, 0, null);
    }

    private void drawBossHPBar(Graphics2D g2)
    {
        drawHPBar(g2, gp.screenWidth/2 - 3 * gp.tileSize, 1 * gp.tileSize, 6 * gp.tileSize, gp.battleSystem.currentEnemy.hp, gp.battleSystem.currentEnemy.maxHp, Color.RED);
    }

    private void drawPlayerHPBar(Graphics2D g2)
    {
        drawHPBar(g2, gp.screenWidth - (gp.screenWidth - (int)(2.9 * gp.tileSize)), 9 * gp.tileSize, 4 * gp.tileSize, gp.player.hp, gp.player.maxHp, Color.GREEN);
    }

    private void drawInstructionsMenu(Graphics2D g2)
    {
        Color normalColor = new Color(32, 30, 51);

        g2.setFont(monogram);

        g2.setColor(normalColor);
        g2.fillRect(10, 10, (int) (gp.tileSize * 2.5), (int) (gp.tileSize * 2.6));
        g2.setColor(Color.WHITE);
        g2.drawString("BATTLE CONTROLS", 18, 30);
        g2.drawString("WASD - Select", 18, 80);
        g2.drawString("SPACE - Select", 18, 110);
        g2.drawString("ESC - Back", 18, 140);
        g2.drawString("ENTER - Submit", 18, 170);



    }

    private void drawPartyPanel(Graphics2D g2)
    {
        int panelHeight = gp.battleSystem.partyMembers.size() * gp.tileSize;
        g2.setFont(monogram);
        Color normalColor = new Color(32, 30, 51);
        Color highlightColor = new Color(100, 90, 180);
        g2.setColor(normalColor);
        if(gp.battleSystem.partyMembers.size() <= 2) {
            g2.fillRoundRect(gp.tileSize, (int) (8.5 * gp.tileSize), (int) (6.5 * gp.tileSize), panelHeight, 2, 2);
            for(int i = 0; i < gp.battleSystem.partyMembers.size(); i++)
            {
                if(i == gp.battleSystem.activePartyIndex) {
                    g2.setColor(highlightColor);
                    g2.fillRoundRect(gp.tileSize, (int) (8.5 * gp.tileSize) + (i*panelHeight/2), (int) (6.5 * gp.tileSize), panelHeight / gp.battleSystem.partyMembers.size(), 2, 2); // same x/width as the panel, just that row's y and height
                }

                drawHPBar(g2, gp.screenWidth - (gp.screenWidth - (int)(2 * gp.tileSize)), (9+i) * gp.tileSize, 4 * gp.tileSize, gp.battleSystem.partyMembers.get(i).hp, gp.battleSystem.partyMembers.get(i).maxHp, Color.GREEN);
                g2.setColor(Color.WHITE);
                g2.drawString(gp.battleSystem.partyMembers.get(i).name, gp.screenWidth - (gp.screenWidth - (int)(2 * gp.tileSize)), (int)((8.95+i) * gp.tileSize));
                g2.drawImage(gp.battleSystem.partyMembers.get(i).icon, gp.screenWidth - (gp.screenWidth - (int)(1 * gp.tileSize)), (int)((8.6+i) * gp.tileSize), gp.tileSize, gp.tileSize, null);
            }
        } else if(gp.battleSystem.partyMembers.size() == 3) {
            g2.fillRoundRect(gp.tileSize, (int) (8 * gp.tileSize), (int) (6.5 * gp.tileSize), panelHeight, 2, 2);
            for(int i = 0; i < gp.battleSystem.partyMembers.size(); i++)
            {
                if(i == gp.battleSystem.activePartyIndex) {
                    g2.setColor(highlightColor);
                    g2.fillRoundRect(gp.tileSize, (int) (8 * gp.tileSize) + (i*panelHeight/3), (int) (6.5 * gp.tileSize), panelHeight / gp.battleSystem.partyMembers.size(), 2, 2); // same x/width as the panel, just that row's y and height
                }

                drawHPBar(g2, gp.screenWidth - (gp.screenWidth - (int)(2 * gp.tileSize)), (int)((8.5+i) * gp.tileSize), 4 * gp.tileSize, gp.battleSystem.partyMembers.get(i).hp, gp.battleSystem.partyMembers.get(i).maxHp, Color.GREEN);
                g2.setColor(Color.WHITE);
                g2.drawString(gp.battleSystem.partyMembers.get(i).name, gp.screenWidth - (gp.screenWidth - (int)(2 * gp.tileSize)), (int)((8.45+i) * gp.tileSize));
                g2.drawImage(gp.battleSystem.partyMembers.get(i).icon, gp.screenWidth - (gp.screenWidth - (int)(1 * gp.tileSize)), (int)((8.1+i) * gp.tileSize), gp.tileSize, gp.tileSize, null);
            }
        } else {
            g2.fillRoundRect(gp.tileSize, (int) (7 * gp.tileSize), (int) (6.5 * gp.tileSize), panelHeight, 2, 2);
            for(int i = 0; i < gp.battleSystem.partyMembers.size(); i++)
            {
                if(i == gp.battleSystem.activePartyIndex) {
                    g2.setColor(highlightColor);
                    g2.fillRoundRect(gp.tileSize, (int) (7 * gp.tileSize) + (i*panelHeight/4), (int) (6.5 * gp.tileSize), panelHeight / gp.battleSystem.partyMembers.size(), 2, 2); // same x/width as the panel, just that row's y and height
                }

                drawHPBar(g2, gp.screenWidth - (gp.screenWidth - (int)(2 * gp.tileSize)), (int)((7.5+i) * gp.tileSize), 4 * gp.tileSize, gp.battleSystem.partyMembers.get(i).hp, gp.battleSystem.partyMembers.get(i).maxHp, Color.GREEN);
                g2.setColor(Color.WHITE);
                g2.drawString(gp.battleSystem.partyMembers.get(i).name, gp.screenWidth - (gp.screenWidth - (int)(2 * gp.tileSize)), (int)((7.45+i) * gp.tileSize));
                g2.drawImage(gp.battleSystem.partyMembers.get(i).icon, gp.screenWidth - (gp.screenWidth - (int)(1 * gp.tileSize)), (int)((7.1+i) * gp.tileSize), gp.tileSize, gp.tileSize, null);
            }
        }
    }

    private void drawActionMenu(Graphics2D g2)
    {
        Color normalColor = new Color(32, 30, 51);
        Color highlightColor = new Color(100, 90, 180);
        g2.setFont(monogram);

        //drawing the boxes
        if (gp.battleSystem.selectedAction == 0) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(gp.screenWidth - ((int)(7.5 * gp.tileSize)), (int)(8.5 * gp.tileSize), 3 * gp.tileSize, gp.tileSize, 2, 2);

        if (gp.battleSystem.selectedAction == 1) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(gp.screenWidth - ((int)(4 * gp.tileSize)), (int)(8.5 * gp.tileSize), 3 * gp.tileSize, gp.tileSize, 2, 2);

        if (gp.battleSystem.selectedAction == 2) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(gp.screenWidth - ((int)(7.5 * gp.tileSize)), 10 * gp.tileSize, 3 * gp.tileSize, gp.tileSize, 2, 2);

        if (gp.battleSystem.selectedAction == 3) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(gp.screenWidth - ((int)(4 * gp.tileSize)), 10 * gp.tileSize, 3 * gp.tileSize, gp.tileSize, 2, 2);

        //drawing the text
        g2.setColor(Color.WHITE);

        FontMetrics fm = g2.getFontMetrics();

        String attack = "Attack";
        int attackX = gp.screenWidth - ((int)(7.5 * gp.tileSize)) + (3 * gp.tileSize - fm.stringWidth(attack)) / 2;
        int attackY = (int)(8.5 * gp.tileSize) + (gp.tileSize - fm.getHeight()) / 2 + fm.getAscent();;
        g2.drawString(attack, attackX, attackY);

        String skills = "Skills";
        int skillsX = gp.screenWidth - ((int)(4 * gp.tileSize)) + (3 * gp.tileSize - fm.stringWidth(skills)) / 2;
        int skillsY = (int)(8.5 * gp.tileSize) + (gp.tileSize - fm.getHeight()) / 2 + fm.getAscent();;
        g2.drawString(skills, skillsX, skillsY);

        String items = "Items";
        int itemsX = gp.screenWidth - ((int)(7.5 * gp.tileSize)) + (3 * gp.tileSize - fm.stringWidth(items)) / 2;
        int itemsY = 10 * gp.tileSize + (gp.tileSize - fm.getHeight()) / 2 + fm.getAscent();;
        g2.drawString(items, itemsX, itemsY);

        String defend = "Defend";
        int defendX = gp.screenWidth - ((int)(4 * gp.tileSize)) + (3 * gp.tileSize - fm.stringWidth(defend)) / 2;
        int defendY = 10 * gp.tileSize + (gp.tileSize - fm.getHeight()) / 2 + fm.getAscent();;
        g2.drawString(defend, defendX, defendY);

    }

    private void drawBattleLog(Graphics2D g2)
    {
        if(!gp.battleSystem.battleLog.isEmpty() && animator.isAnimDone())
        {
            g2.setColor(new Color(21, 19, 32));
            g2.fillRoundRect(gp.screenWidth / 2 - 3 * gp.tileSize, 6 * gp.tileSize, 6 * gp.tileSize, 2 * gp.tileSize, 35, 35);

            g2.setColor(new Color(108, 102, 138));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(gp.screenWidth / 2 - 3 * gp.tileSize, 6 * gp.tileSize, 6 * gp.tileSize, 2 * gp.tileSize, 35, 35);

            g2.setColor(Color.WHITE);
            g2.setFont(monogram);
            FontMetrics fm = g2.getFontMetrics();
            String[] battleLogList = gp.battleSystem.battleLog.split(" ");
            int textX = gp.screenWidth / 2 - 3 * gp.tileSize + 20;
            int textY = 6 * gp.tileSize + 30 ; // starting Y with padding

            String temp = "";
            for(String word : battleLogList)
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

    private void drawChallengePrompt(Graphics2D g2)
    {
        g2.setColor(new Color(163, 157, 170));
        g2.fillRect(gp.tileSize, (int)(8.5 * gp.tileSize), gp.screenWidth - (2*gp.tileSize), (int)(2.5 * gp.tileSize));
        g2.setColor(Color.BLACK);
        String challengeType = "Challenge type " + gp.battleSystem.currentChallenge.type;
        g2.setFont(new Font("Courier New", Font.PLAIN, 18));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(challengeType, gp.tileSize + 10, (int)(8.5 * gp.tileSize) + 30);
        g2.drawString(gp.battleSystem.currentChallenge.prompt, gp.tileSize + 10, (int)(8.5 * gp.tileSize) + fm.getHeight()*2 + 20);

        g2.drawString(gp.battleSystem.typedText + "|", gp.tileSize , (int)(8.5 * gp.tileSize) + fm.getHeight()*3 + 30);
    }

    public void drawSkillsMenu(Graphics2D g2)
    {
        Color normalColor = new Color(32, 30, 51);
        Color highlightColor = new Color(100, 90, 180);
        Color borderColor = new Color(21, 20, 35);
        g2.setFont(monogram);
        g2.setColor(normalColor);
        g2.fillRoundRect(8 * gp.tileSize, (int)(8.5 * gp.tileSize), (int)(7 * gp.tileSize), (int)(2.5 * gp.tileSize),2, 2);
        g2.setColor(borderColor);
        g2.fillRoundRect(8 * gp.tileSize, (int)(8.5 * gp.tileSize), (int)(7 * gp.tileSize), gp.tileSize/2, 2, 2);
        g2.setColor(Color.WHITE);
        g2.drawString("SKILLS MENU", (int) (8.1*gp.tileSize), (int) (8.8*gp.tileSize));
        PartyMember activeMember = gp.battleSystem.partyMembers.get(gp.battleSystem.activePartyIndex);

        //drawing the boxes
        if (gp.battleSystem.selectedSkill == 0) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(((int) (8.1 * gp.tileSize)), (int) (9.2 * gp.tileSize), 3 * gp.tileSize, gp.tileSize / 2, 2, 2);


        if (gp.battleSystem.selectedSkill == 1) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(((int) (11.9 * gp.tileSize)), (int) (9.2 * gp.tileSize), 3 * gp.tileSize, gp.tileSize / 2, 2, 2);


        if (gp.battleSystem.selectedSkill == 2) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(((int) (8.1 * gp.tileSize)), (int) (10.2 * gp.tileSize), 3 * gp.tileSize, gp.tileSize / 2, 2, 2);


        if (gp.battleSystem.selectedSkill == 3) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(((int) (11.9 * gp.tileSize)), (int) (10.2 * gp.tileSize), 3 * gp.tileSize, gp.tileSize / 2, 2, 2);


        //drawing text
        g2.setColor(Color.WHITE);
        int i = 0;
            for(Skill skill : activeMember.skills) {
                int col = i % 2; // 0 = left, 1 = right
                int row = i / 2; // 0 = top, 1 = bottom
                int x = (col == 0) ? (int)(8.2 * gp.tileSize) : (int)(12 * gp.tileSize);
                int y = (int)(9.5 * gp.tileSize) + row * gp.tileSize;
                g2.drawString(skill.name, x, y);
                i++;
            }

    }

    public void drawItemsMenu(Graphics2D g2)
    {
        Color normalColor = new Color(32, 30, 51);
        Color highlightColor = new Color(100, 90, 180);
        Color borderColor = new Color(21, 20, 35);
        g2.setFont(monogram);
        g2.setColor(normalColor);
        g2.fillRoundRect(8 * gp.tileSize, (int)(8.5 * gp.tileSize), (int)(7 * gp.tileSize), (int)(2.5 * gp.tileSize),2, 2);
        g2.setColor(borderColor);
        g2.fillRoundRect(8 * gp.tileSize, (int)(8.5 * gp.tileSize), (int)(7 * gp.tileSize), gp.tileSize/2, 2, 2);
        g2.setColor(Color.WHITE);
        g2.drawString("ITEMS MENU", (int) (8.1*gp.tileSize), (int) (8.8*gp.tileSize));
        PartyMember activeMember = gp.battleSystem.partyMembers.get(gp.battleSystem.activePartyIndex);

        //drawing the boxes
        if (gp.battleSystem.selectedItem == 0) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(((int) (8.1 * gp.tileSize)), (int) (9.2 * gp.tileSize), 3 * gp.tileSize, gp.tileSize / 2, 2, 2);


        if (gp.battleSystem.selectedItem == 1) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(((int) (11.9 * gp.tileSize)), (int) (9.2 * gp.tileSize), 3 * gp.tileSize, gp.tileSize / 2, 2, 2);


        if (gp.battleSystem.selectedItem == 2) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(((int) (8.1 * gp.tileSize)), (int) (10.2 * gp.tileSize), 3 * gp.tileSize, gp.tileSize / 2, 2, 2);


        if (gp.battleSystem.selectedItem == 3) {
            g2.setColor(highlightColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(((int) (11.9 * gp.tileSize)), (int) (10.2 * gp.tileSize), 3 * gp.tileSize, gp.tileSize / 2, 2, 2);


        //drawing text
        g2.setColor(Color.WHITE);
        int i = 0;
        for(Item item : gp.battleSystem.inventory) {
            int col = i % 2; // 0 = left, 1 = right
            int row = i / 2; // 0 = top, 1 = bottom
            int x = (col == 0) ? (int)(8.2 * gp.tileSize) : (int)(12 * gp.tileSize);
            int y = (int)(9.5 * gp.tileSize) + row * gp.tileSize;
            g2.drawString(item.name, x, y);
            i++;
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
        String text = (gp.battleSystem.phase == BattleSystem.BattlePhase.BATTLE_WON) ? "Victory!" : "Defeat...";
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
