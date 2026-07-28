package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ItemPopup {
    GamePanel gp;

    private boolean active = false;
    private int timer = 0;
    private int displayDuration = 180;

    private String itemName;
    private BufferedImage itemIcon;

    public ItemPopup(GamePanel gp) {
        this.gp = gp;
    }

    public void trigger(String itemName, BufferedImage itemIcon) {
        this.itemName = itemName;
        this.itemIcon = itemIcon;
        this.timer = 0;
        this.active = true;

        gp.gameState = gp.itemPopupState;
    }

    public void update() {
        if (!active)
            return;

        timer++;
        if (timer > displayDuration)
            active = false;
        gp.gameState = gp.playState;
    }

    Font monogram;

    public void draw(Graphics2D g2) {

        try {
            monogram = Font.createFont(Font.TRUETYPE_FONT,
                            getClass().getResourceAsStream("/fonts/monogram.ttf"))
                    .deriveFont(48f);

        } catch (Exception e) {
            e.printStackTrace();
            monogram = new Font("Arial", Font.PLAIN, 24);
        }

        if (!active)
            return;

        int popupWidth = (int) (2.8 * gp.tileSize);
        int popupHeight = (int) (3.3 * gp.tileSize);

        int x = gp.screenWidth / 2 - popupWidth / 2;
        int y = gp.screenHeight / 2 - popupHeight / 2;

        int outerPadding = 20;

        // outer box
        g2.setColor(new Color(151, 124, 101));
        g2.fillRect(x, y, popupWidth, popupHeight);

//        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        g2.setFont(monogram);
        FontMetrics fm = g2.getFontMetrics();

        // header band
        g2.setColor(new Color(241, 228, 213));
        String header = "You got a";
        g2.drawString(header, x + popupWidth / 2 - fm.stringWidth(header) / 2, y + 30);

        // icon band
        int iconSize = gp.tileSize;
        int iconPanelX = x + outerPadding;
        int iconPanelY = y + 55;
        int iconPanelWidth = popupWidth - (outerPadding * 2);
        int iconPanelHeight = iconSize + 20; // just enough to frame the icon
        g2.fillRect(iconPanelX, iconPanelY, iconPanelWidth, iconPanelHeight);

        int iconX = iconPanelX + (iconPanelWidth - iconSize) / 2;
        int iconY = iconPanelY + 10;
        g2.drawImage(itemIcon, iconX, iconY, iconSize, iconSize, null);

        // name band
        g2.setColor(new Color(241, 228, 213));
        int nameY = iconPanelY + iconPanelHeight + 40;
        g2.drawString(itemName, x + popupWidth/2 - fm.stringWidth(itemName)/2, nameY);

    }
}
