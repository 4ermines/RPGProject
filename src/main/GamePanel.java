package main;

import battle.BattleSystem;
import battle.BattleUI;
import battle.Item;
import entity.*;
import object.AssetSetter;
import object.SuperObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
    public final int siriusWalkState = 12;
    public final int battleDialogueState = 13;

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
    public String currentSpeaker = "";
    public String[] dialogueSpeakers;


    public boolean startAnimationAfterDialogue = false;
    public boolean dialogueFinished = false;

    //FPS
    int FPS = 60;

    //MAPS
    public String[] mapFiles = {
            "/maps/roommap1.txt",
            "/maps/trainmap.txt",
            "/maps/train2map.txt",
            "/maps/interiortrainmap.txt",
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
    public PartyMember siriusMagiosis = new SiriusMagiosis("Sirius Magiosis", 150, 30);
    public Enemy stout = new Stout();

    //SIRIUS EXTRAS
    public NPC sirius;
    public int siriusTargetX, siriusTargetY;
    public boolean siriusWalkDone = false;
    public boolean siriusMet = false;
    public boolean siriusWalkDialogueFinished = false;
    public BufferedImage[] siriusBattleTips;
    public int siriusBattleTipIndex = 0;
    public BufferedImage siriusBattleTip1, siriusBattleTip2, siriusBattleTip3;

    {
        try {
            siriusBattleTip1 = ImageIO.read(getClass().getResourceAsStream("/ui/siriusBattleTip1.png"));
            siriusBattleTip2 = ImageIO.read(getClass().getResourceAsStream("/ui/siriusBattleTip2.png"));
            siriusBattleTip3 = ImageIO.read(getClass().getResourceAsStream("/ui/siriusBattleTip3.png"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


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
        battleSystem.partyMembers.add(new PartyMember("Player", player.hp, 20, player.icon)); //maybe move later
//        battleSystem.partyMembers.add(siriusMagiosis); //maybe move later
//        battleSystem.partyMembers.add(new PartyMember("Fatty", 100, 10, player.icon));
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

    // Changes the map when train station is entered
    public void enterTrain() {
        gameState = trainState;
        mapIndex = 1;
        aSetter.setObject();
        tileM.loadMap(mapFiles[1]);
        player.worldX = 1 * tileSize;
        player.worldY = (int) (8.5 * tileSize);
    }

    // Changes the map when first train is entered
    public void enterInteriorTrain() {
        gameState = trainState;
        mapIndex = 3;
        aSetter.setObject();
        tileM.loadMap(mapFiles[3]);
        player.worldX = 2 * tileSize;
        player.worldY = (int) (2 * tileSize);
    }


    public void enterNewTrain() {
        gameState = trainState;
        mapIndex = 2;
        aSetter.setObject();
        tileM.loadMap(mapFiles[2]);
        player.worldX = 30 * tileSize;
        player.worldY = 18 * tileSize;
    }



    public void update() {

        if (sirius != null) sirius.update();


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

                if(siriusWalkDone)
                {
                    if(siriusWalkDialogueFinished && !siriusMet)
                    {
                        siriusMet = true;
                        gameState = dialogueState;
                        currentSpeaker = "Purple Wizard";
                        dialogueLines = new String[]{
                                "Dude, you need any help?",
                                "What? Never seen a wizard before?",
                                "I'll lend you my wand for now."
                        };
                    } else if(!player.stoutDefeated) {
                        if(siriusMet)
                        {
                            gameState = battleState;
                            battleSystem.startBattle(new Stout());
                            battleSystem.isSiriusTip = true;
                            battleSystem.battleDialogueLines = new String[]{
                                    "Don't fret! With a powerful wizard like me on your hands, you'll easily achieve victory!",
                                    "Oh, right... you don't know how spells work...",
                                    "Basically, if you write the correct JAVA code, you cast a spell that damages your enemy.",
                                    "This enemy is called a STOUT. It's weak to print statement spells.",
                                    "Print statements tell the computer to output or display some sort of text on your screen.",
                                    "Hit the attack button to give it a try!"
                            };
                            siriusBattleTips = new BufferedImage[]{
                                    siriusBattleTip1,
                                    siriusBattleTip3,
                                    siriusBattleTip2,
                                    siriusBattleTip1,
                                    siriusBattleTip1,
                                    siriusBattleTip2
                            };
                            battleSystem.battleDialogueIndex = 0;
                            gameState = battleDialogueState;
                            battleSystem.isFirstTurnTutorial = true;
                        }

                    }
                }


                dialogueIndex = 0;
                currentDialogue = "";
                dialogueSpeakers = null;

            } else {
                // UPDATE THE SPEAKER FOR THE NEXT LINE
                if (dialogueSpeakers != null && dialogueIndex < dialogueSpeakers.length) {
                    currentSpeaker = dialogueSpeakers[dialogueIndex];
                }
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
        } else if(gameState == battleState) {
            battleSystem.update();
        } else if(gameState == siriusWalkState)
        {
            if(sirius != null)
            {
                int siriusSpeed = 2;

                if(Math.abs(sirius.worldX - siriusTargetX) > siriusSpeed)
                {
                    sirius.worldX += (sirius.worldX < siriusTargetX) ? siriusSpeed : -siriusSpeed;
                } else if (Math.abs(sirius.worldY - siriusTargetY) > siriusSpeed) {
                    sirius.worldY += (sirius.worldY < siriusTargetY) ? siriusSpeed : -siriusSpeed;
                } else {
                    sirius.worldX = siriusTargetX;
                    sirius.worldY = siriusTargetY;
                    sirius.currentAnimation = NPC.NPCAnimationType.NONE; // stop walking
                    siriusWalkDone = true;
                    gameState = dialogueState;
                    currentSpeaker = "Purple Wizard";
                    dialogueLines = new String[]{
                            "Yo."
                    };
                    dialogueIndex = 0;
                    siriusWalkDialogueFinished = true;
                }
            }

        } else if (gameState == battleDialogueState && keyH.spacePressed)
        {
            keyH.spacePressed = false;
            battleSystem.battleDialogueIndex++;
            siriusBattleTipIndex++;

            if (battleSystem.battleDialogueIndex >= battleSystem.battleDialogueLines.length) {
                gameState = battleState;
                if(battleSystem.isFirstTurnTutorial)
                    battleSystem.phase = BattleSystem.BattlePhase.PLAYER_CHOOSE_ACTION;
                else {
                    battleSystem.phase = BattleSystem.BattlePhase.PLAYER_TYPING;
                    battleSystem.battleLog = "According to that wizard, the structure of a print statement is System.out.print(\"[text to print]\")... Use that information to cast a proper spell.";
                }

                battleSystem.battleDialogueIndex = 0;
                battleSystem.battleCurrentDialogue = "";
                battleSystem.isSiriusTip = false;
                siriusBattleTipIndex = 0;
            }

        }

        // TESTING
        if (keyH.testPressed && gameState == playState) {
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

        if (mapIndex == 2) {
            int cx = player.screenX + tileSize / 2;
            int cy = player.screenY + tileSize / 2;
            float radius = screenWidth * 0.6f; // adjust size to taste

            RadialGradientPaint vignette = new RadialGradientPaint(
                    cx, cy, radius,
                    new float[]{0.0f, 1.0f},
                    new Color[]{new Color(0,0,0,0), new Color(0,0,0,200)}
            );

            g2.setPaint(vignette);
            g2.fillRect(0, 0, screenWidth, screenHeight);

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

        if (gameState == battleDialogueState) {
            if (battleSystem.battleDialogueIndex < battleSystem.battleDialogueLines.length) {
                battleSystem.battleCurrentDialogue = battleSystem.battleDialogueLines[battleSystem.battleDialogueIndex];

            } else {
                gameState = battleState;
                battleSystem.battleCurrentDialogue = "";
            }
        }


        if (sirius != null) sirius.draw(g2);


        if(gameState == battleState || gameState == battleDialogueState)
         {
             battleUI.draw(g2);

         }



        g2.dispose();

    }
}
