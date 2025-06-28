package main;

import entity.Player;
import object.SuperObject;
import tiles.TileManager;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.black;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings
    final int originalTileSize = 32; //32x32 tile
    final int scale = 2; //scale by 1

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS
    public int maxWorldCol;
    public int maxWorldRow;
    public int worldWidth = tileSize * maxWorldCol;
    public int worldHeight = tileSize * maxWorldRow;

    public int gameState;
    public final int tileState = 0;
    public final int playState = 1;
    public final int dialogueState = 2;
    public final int transitionState = 3;
    public final int introState = 4;
    public final int fadeState = 5;
    int fadeAlpha = 255;

    public TextHandler textHandler = new TextHandler(this);
    public int dialogueIndex = 0;

    public String objectDialogue = " ";
    public boolean nearObject = false;
    public String[] dialogueLines;



    //FPS
    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[20];


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        gameState = introState;
    }

    public void setupGame() { //object
        aSetter.setObject();
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        //long timer = 0;
        //long drawCount = 0;

        while(gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            //timer += (currentTime - lastTime);

            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            //    drawCount++;
            }

            //if (timer >= 1000000000) {
            //    System.out.println("FPS: " + drawCount);
            //    drawCount = 0;
            //    timer = 0;
            //}

        }

    }

    public void drawIntroText(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.PLAIN, 32));
        String text = "You wake up. The sun shines brightly in your face.";
        int x = 150;
        int y = screenHeight/2;
        g2.drawString(text, x, y);

    }

    int introCounter = 0;
    int fadeCounter = 0;
    public String currentDialogue = "";

    public void update() {
        //fade in intro
        if (gameState == introState) {
            introCounter++;
            if (introCounter > 180) {
                gameState = fadeState;
                fadeAlpha = 255;
            }
        } else if (gameState == fadeState) {
            fadeCounter++;
            fadeAlpha -= 2;
            if (fadeAlpha <= 0) {
                fadeAlpha = 0;
                gameState = dialogueState;
                dialogueIndex = 0;
                dialogueLines = new String[] {
                        "Something tells you it's going to be a good day.",
                        "Use WASD to move."

                };

            }

        }
        else if (gameState == dialogueState && keyH.spacePressed) {
            keyH.spacePressed = false;
            dialogueIndex++;
            if (dialogueIndex >= dialogueLines.length) {
                gameState = playState;
                dialogueIndex = 0;
                currentDialogue = "";
            }
        }
        else if (gameState == playState) {
            player.update();
            player.checkObjectProximity();
            player.checkObjectInteraction();
        }



    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;


        //TILE
        tileM.draw(g2);

        //OBJECT
        for(int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }



        //PLAYER
        player.draw(g2);

        if (gameState == introState) {
            g2.setColor(black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            drawIntroText(g2);

        }


        //intro and fade
         if (gameState == fadeState) {
            g2.setColor(new Color(0, 0, 0, fadeAlpha));
            g2.fillRect(0, 0, screenWidth, screenHeight);


        }
        //dialogue
         if (gameState == dialogueState) {
             if (dialogueIndex < dialogueLines.length) {
                 currentDialogue = dialogueLines[dialogueIndex];
                 textHandler.drawDialogueBox(g2);
             } else {
                 gameState = playState;
                 currentDialogue = "";
             }


         }


        g2.dispose();

    }
}
