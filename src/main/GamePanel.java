package main;

import battle.BattleAnimator;
import battle.BattleSystem;
import battle.BattleUI;
import battle.Item;
import entity.Enemy;
import entity.PartyMember;
import entity.Player;
import entity.SiriusMagiosis;
import object.AssetSetter;
import object.SuperObject;
import tiles.TileManager;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.black;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 32; //32x32 tile
    final int scale = 2; //scale by 2
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS
    public int maxWorldCol;
    public int maxWorldRow;

    // ALL THE STATES
    public int gameState;
    public final int playState = 1;
    public final int dialogueState = 2;
    public final int battleState = 3;
    public final int animationState = 6;
    public final int packState = 7;
    public final int trainState = 8;
    public final int fadeOutState = 9;
    public final int fadeInState = 10;
    public final int fadeBlackHoldState = 11;

    // FADE DIALOGUE STUFF
    public String[] fadeDialogueLines;
    public int fadeDialogueIndex = 0;
    public Runnable afterFadeInAction;
    public Runnable afterFadeOutAction;
    public int fadeCounter = 0;
    int fadeAlpha = 255;

    // OTHER DIALOGUE STUFF
    public TextHandler textHandler = new TextHandler(this);
    public int dialogueIndex = 0;
    public String[] dialogueLines;
    public String currentDialogue = "";

    public boolean startAnimationAfterDialogue = false;
    public boolean dialogueFinished = false;

    //FPS
    int FPS = 60;

    //MAPS
    public String[] mapFiles = {
            "/maps/roommap1.txt",
            "/maps/trainmap.txt",
            "/maps/train2map.txt"
    };
    public int mapIndex = 0;

    // INSTANCES
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this, keyH);
    public SuperObject[] obj = new SuperObject[100];
    public BattleSystem battleSystem = new BattleSystem(this);
    public BattleUI battleUI = new BattleUI(this);
    //    public javax.swing.JTextField codeInput = new javax.swing.JTextField();
    public PartyMember siriusMagiosis = new SiriusMagiosis("Sirius Magiosis", 150, 30);

    //  A GamePanel creates a new panel for running the game and its assets
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        startFadeFromBlack(
                new String[]{"You wake up. The sun shines brightly in your face."},
                () -> {
                    dialogueLines = new String[]{
                            "Something tells you it's going to be a good day. Press space to continue.",
                            "Use WASD to move."
                    };
                    dialogueIndex = 0;
                    gameState = dialogueState;
                }
        );
//        battleSystem.partyMembers.add(new PartyMember("Player", 100, 20, player.icon)); //maybe move later
        battleSystem.partyMembers.add(siriusMagiosis); //maybe move later
        battleSystem.partyMembers.add(new PartyMember("Fatty", 100, 10, player.icon));
//        battleSystem.partyMembers.add(new PartyMember("Fatty2", 100, 10, player.icon));

        battleSystem.inventory.add(new Item("Potion", Item.EffectType.HEAL, 5, "Heals a party member 5 HP"));



//        codeInput.setVisible(false);
//        codeInput.setFont(new Font("Monospaced", Font.PLAIN, 16));
//        codeInput.setBounds(
//                tileSize,
//                (int)(9.5 * tileSize),
//                screenWidth - 2 * tileSize,
//                tileSize
//        );
//        this.setLayout(null);
//        this.add(codeInput);
    }

    //  Set up game assets (objects)
    public void setupGame() {
        aSetter.setObject();
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Game timer
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

//    public void drawIntroText(Graphics2D g2) {
//        g2.setColor(Color.white);
//        g2.setFont(new Font("Arial", Font.PLAIN, 32));
//        String text = "You wake up. The sun shines brightly in your face.";
//        int x = 150;
//        int y = screenHeight/2;
//        g2.drawString(text, x, y);
//
//    }

    // FADE MECHANICS

    public void startFadeFromBlack(String[] centeredText, Runnable afterFadeInAction) {
        this.fadeDialogueLines = centeredText;
        this.fadeDialogueIndex = 0;
        this.afterFadeInAction = afterFadeInAction;
        this.fadeAlpha = 255; // start fully black
        this.fadeCounter = 0;
        this.gameState = fadeBlackHoldState;
    }

    public void completeFade(String[] centeredText, Runnable afterFadeOutAction, Runnable afterFadeInAction) {
        this.fadeDialogueLines = centeredText;
        this.fadeDialogueIndex = 0;
        this.afterFadeInAction = afterFadeInAction;
        this.afterFadeOutAction = afterFadeOutAction;
        this.fadeAlpha = 0; // start not black
        this.fadeCounter = 0;
        this.gameState = fadeOutState;
    }

    // Changes the map when first train is entered
    public void enterTrain() {
        gameState = trainState;
        mapIndex = 1;
        aSetter.setObject();
        tileM.loadMap(mapFiles[1]);
        player.worldX = 2 * tileSize;
        player.worldY = 2 * tileSize;
    }

    public void enterNewTrain() {
        gameState = trainState;
        mapIndex = 2;
        aSetter.setObject();
        tileM.loadMap(mapFiles[2]);
        player.worldX = 30 * tileSize;
        player.worldY = 11 * tileSize;
    }



    public void update() {
        // fade in intro
        int fadePauseCounter = 0;
        if (gameState == fadeOutState) {
            fadeAlpha += 5;
            if (fadeAlpha >= 255) {
                fadeAlpha = 255;
                if (afterFadeOutAction != null) {
                    afterFadeOutAction.run();
                    afterFadeOutAction = null;
                }
                gameState = fadeBlackHoldState;
            }
        } else if (gameState == fadeBlackHoldState) {
            fadeCounter++;
            if (fadeCounter > 180) {
                gameState = fadeInState;
            }
        } else if (gameState == fadeInState) {
            fadeAlpha -= 5;
            if (fadeAlpha <= 0) {
                fadeAlpha = 0;
                if (afterFadeInAction != null) {
                    afterFadeInAction.run(); // run custom thing
                    afterFadeInAction = null;
                }
            }
        } else if (gameState == dialogueState && keyH.spacePressed) {
            keyH.spacePressed = false;
            dialogueIndex++;
            if (dialogueIndex >= dialogueLines.length) {
                if (startAnimationAfterDialogue) {
                    gameState = animationState;
                    startAnimationAfterDialogue = false;
                } else if (dialogueFinished) {
                    gameState = packState;

                } else {
                    gameState = playState;
                }
                dialogueIndex = 0;
                currentDialogue = "";
            }
        } else if (gameState == playState) {
            player.update();
            player.checkObjectProximity();
            player.checkObjectInteraction();
        } else if (gameState == packState) {
            player.update();
            player.checkObjectProximity();
            player.checkObjectInteraction();
        } else if (gameState == animationState) {
            player.playAnimation();
        } else if (gameState == trainState) {
            player.update();
            player.checkObjectProximity();
            player.checkObjectInteraction();
        } else if(gameState == battleState)
        {
            battleSystem.update();
        }
        // TESTING
        if (keyH.testPressed) {
            keyH.testPressed = false;
            enterTrain();
        }
        if(keyH.testBattlePressed)
        {
            keyH.testBattlePressed = false;
            Enemy testBoss = new Enemy("Test Boss", 100, 10);
            battleSystem.startBattle(testBoss);
            System.out.println(testBoss.hp);
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

//        if (gameState == introState) {
//            g2.setColor(black);
//            g2.fillRect(0, 0, screenWidth, screenHeight);
//            drawIntroText(g2);
//
//        }

        if (gameState == fadeBlackHoldState || gameState == fadeInState || gameState == fadeOutState) {
            g2.setColor(new Color(0, 0, 0, fadeAlpha));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            if (gameState == fadeBlackHoldState && fadeDialogueLines != null) {
                g2.setColor(Color.white);
                g2.setFont(new Font("Arial", Font.PLAIN, 24));
                String text = fadeDialogueLines[fadeDialogueIndex];
                int x = screenWidth / 2 - g2.getFontMetrics().stringWidth(text) / 2;
                int y = screenHeight / 2;
                g2.drawString(text, x, y);
            }
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

         if(gameState == battleState)
         {
             battleUI.draw(g2);

         }



        g2.dispose();

    }
}
