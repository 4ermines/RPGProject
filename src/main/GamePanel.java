package main;

import battle.BattleSystem;
import battle.BattleUI;
import battle.Item;
import entity.*;
import exam.ExamSystem;
import exam.ExamUI;
import object.AssetSetter;
import object.SuperObject;
import training.TrainingSystem;
import training.TrainingUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.awt.Color.black;
import static java.awt.Color.pink;

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
    public final int siriusFollowState = 14;
    public final int battleTransitionState = 15;
    public final int examState = 16;
    public final int trainingState = 17;
    public final int panState = 18;
    public final int titleState = 19;
    public final int saveSelectState = 20;
    public final int itemPopupState = 21;

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
            "/maps/castleentrance.txt"
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
    public ItemPopup itemPopup = new ItemPopup(this);
    public ExamSystem examSystem = new ExamSystem(this);
    public ExamUI examUI = new ExamUI(this);
    public TypedInputHandler handler = new TypedInputHandler(this);
    public TrainingSystem trainingSystem = new TrainingSystem(this);
    public TrainingUI trainingUI = new TrainingUI(this);
    public MouseHandler mouseH = new MouseHandler();
    public TitleScreen titleScreen = new TitleScreen(this, mouseH);

    //CAM
    public int camWorldX, camWorldY;
    public int castlePanTargetY, castlePanTargetX;

    //PAN
    public final int panHoldDuration = 60;
    public Runnable afterPanAction;
    public int panTimer = 0;
    public PanPhase panPhase;
    public enum PanPhase {
        PANNING_UP,
        HOLDING,
        PANNING_DOWN
    }


    //SIRIUS EXTRAS
    public NPC sirius;
    public int siriusTargetX, siriusTargetY;
    public boolean siriusWalkDone = false;
    public boolean siriusMet = false;
    public boolean siriusWalkDialogueFinished = false;
    public BufferedImage[] siriusBattleTips;
    public int siriusBattleTipIndex = 0;
    public BufferedImage siriusBattleTip1, siriusBattleTip2, siriusBattleTip3;

    //NPCs
    public NPC firedMan;
    public NPC pinkGirl;
    public NPC spidermanKid;

    //TRAIN
    private int lastDoorFrame = -1;
    public boolean firstTrainFadeTriggered = false;
    public boolean triggerTrainAnimation = false;
    public boolean trainAnimationDone;
    public int trainAnimationCounter = 0;
    public int trainAnimationMax = 30;
    public int trainAnimationIndex = 0;

    double scrollX;
    BufferedImage trainScrollImage;

    {
        try {
            trainScrollImage = ImageIO.read(getClass().getResourceAsStream("/tiles/trainWindowBg.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean firedManTalkDone = false;
    public boolean pinkGirlTalkDone = false;
    public boolean spidermanKidTalkDone = false;
    public boolean spiderKidPotionGiven = false;

    public boolean castleFadeTriggered = false;
    public boolean doNotStartBattle;

    //BATTLE TRANSITIONS
    public enum TransitionPhase {
        CLOSING,
        HOLDING,
        OPENING
    }
    public TransitionPhase transitionPhase;
    public int transitionTimer = 0;
    public final int transitionCloseDuration = 18;
    public final int transitionHoldDuration = 8;
    public final int transitionOpenDuration = 22;
    public double transitionMaxRadius;
    public Enemy pendingBattleEnemy;
    public boolean transitionSceneSwitched = false;
    public int transitionTargetState;

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
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        this.setFocusable(true);

        gameState = titleState;

        //OLD GAME INTRO, NEEDS TO BE REPLACED WITH TITLE SCREEN AND ONLY PLAY AFTER SELECTING A NEW GAME
//        startFadeFromBlack(
//                new String[]{"You wake up. The sun shines brightly in your face."},
//                () -> {
//                    dialogueLines = new String[]{
//                            "Something tells you it's going to be a good day. Press space to continue.",
//                            "Use WASD to move."
//                    };
//                    dialogueIndex = 0;
//                    gameState = dialogueState;
//                }
//        );



        battleSystem.partyMembers.add(new PartyMember("Player", player.hp, 20, player.icon)); //maybe move later
//        battleSystem.partyMembers.add(siriusMagiosis); //maybe move later

        battleSystem.inventory.add(new Item("Potion", Item.EffectType.HEAL, 5, "Heals a party member 5 HP"));
//



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
        double spaceDelta = 0;
        double spaceDelay = 75;
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
                spaceDelta++;
                if (spaceDelta >= spaceDelay) {
                    keyH.activeSpace();
                    spaceDelta = 0;
                }
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

    public void startPan(int targetX, int targetY, Runnable afterPanAction) {
        gameState = panState;
        panTimer = 0;
        panPhase = PanPhase.PANNING_UP;
        castlePanTargetX = targetX;
        castlePanTargetY = targetY;
        this.afterPanAction = afterPanAction;
    }

    private void moveCamTowards(int targetX, int targetY, int speed) {
        if (Math.abs(camWorldX - targetX) > speed) {
            camWorldX += (camWorldX < targetX) ? speed : -speed;
        } else if (Math.abs(camWorldY - targetY) > speed) {
            camWorldY += (camWorldY < targetY) ? speed : -speed;
        } else {
            camWorldX = targetX;
            camWorldY = targetY;
        }
    }

    public void updatePan() {
        int panSpeed = 3;

        switch (panPhase) {
            case PANNING_UP:
                moveCamTowards(castlePanTargetX, castlePanTargetY, panSpeed);
                if(camWorldY == castlePanTargetY && camWorldX == castlePanTargetX)
                {
                    panPhase = PanPhase.HOLDING;
                }
                break;
            case HOLDING:
                panTimer++;
                if(panTimer >= panHoldDuration)
                {
                    panPhase = PanPhase.PANNING_DOWN;
                }
                break;
            case PANNING_DOWN:
                moveCamTowards(player.worldX, player.worldY, panSpeed);
                if(camWorldY == player.worldY && camWorldX == player.worldX)
                {
                    afterPanAction.run();
                }
                break;
        }
    }

    public void startBattleTransition(Enemy enemy) {
        startBattleTransition(enemy, battleState);
    }

    public void startBattleTransition(Enemy enemy, int targetGameStateAfter) {
        pendingBattleEnemy = enemy;
        transitionTargetState = targetGameStateAfter;
        transitionPhase = TransitionPhase.CLOSING;
        transitionTimer = 0;
        transitionSceneSwitched = false;
        transitionMaxRadius = Math.sqrt(Math.pow(screenWidth / 2.0, 2) + Math.pow(screenHeight / 2.0, 2));
        gameState = battleTransitionState;
    }

    private void updateBattleTransition(){
        transitionTimer++;
        switch (transitionPhase){
            case CLOSING:
                if(transitionTimer >= transitionCloseDuration)
                {
                    if(!transitionSceneSwitched)
                    {
                        battleSystem.startBattle(pendingBattleEnemy);
                        gameState = battleTransitionState;
                        transitionSceneSwitched = true;
                    }
                    transitionPhase = TransitionPhase.HOLDING;
                    transitionTimer = 0;
                }
                break;
            case HOLDING:
                if(transitionTimer >= transitionHoldDuration)
                {
                    transitionPhase = TransitionPhase.OPENING;
                    transitionTimer = 0;
                }
                break;
            case OPENING:
                if(transitionTimer >= transitionOpenDuration)
                {
                    gameState = transitionTargetState;
                }
                break;

        }
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
        player.worldY = (int) (1.8 * tileSize);
    }


    public void enterNewTrain() {
        gameState = trainState;
        mapIndex = 2;
        aSetter.setObject();
        tileM.loadMap(mapFiles[2]);
        player.worldX = 30 * tileSize;
        player.worldY = 18 * tileSize;
    }

    public void checkCastleMapEntrance()
    {
        double xDistance = Math.abs(player.worldX - sirius.worldX);
        double yDistance = Math.abs(player.worldY - sirius.worldY);
        double distance = Math.max(xDistance, yDistance);

        if(distance < 2 * tileSize)
        {
            if(sirius.worldY == siriusTargetY && !castleFadeTriggered)
            {
                completeFade(null,
                        () -> {
                            mapIndex = 4;
                            aSetter.setObject();
                            tileM.loadMap(mapFiles[4]);
                            player.worldX = 3 * tileSize;
                            player.worldY = (int) (2.5 * tileSize);
                            sirius.worldX = 3 * tileSize;
                            sirius.worldY = (int) (3.5 * tileSize);
                            castleFadeTriggered = true;
                        },
                        () -> {
                            gameState = siriusFollowState;
                            siriusTargetY = (int) (0.5 * tileSize);
                        }

                );
            }
        }


    }

    public void animateTrainDoors()
    {
        trainAnimationCounter++;
        if(trainAnimationCounter >= trainAnimationMax && trainAnimationIndex < 3)
        {
            trainAnimationIndex++;
            trainAnimationCounter = 0;
        }

    }

    private double easeInOutQuad(double t)
    {
        return t < 0.5 ? 2 * t * t : -1 + (4 - 2 * t) * t;
        //takes progress value t from 0.0 to 1.0, returning an eased progress value from 0 to 1, but remapped so the motion feels natural instead of robotic
        //if you were to animate using t, the progress value alone, it would animate at a constant speed
        //basically what a graph does in an editing software
    }

    private void drawBattleTransitionOverlay(Graphics2D g2)
    {
        double radius;
        switch (transitionPhase)
        {
            case CLOSING:
                double tc = easeInOutQuad((double) transitionTimer / transitionCloseDuration);
                radius = transitionMaxRadius * (1 - tc);
                break;
            case OPENING:
                double to = easeInOutQuad((double) transitionTimer / transitionOpenDuration);
                radius = transitionMaxRadius * to;
                break;
            default:
                radius = 0;
        }

        int cx = screenWidth / 2;
        int cy = screenHeight / 2;

        //white flash at pinch point for impact
        if (transitionPhase == TransitionPhase.HOLDING && transitionTimer < 3)
        {
            g2.setColor(Color.white);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            return;
        }

        java.awt.geom.Area mask = new java.awt.geom.Area(new Rectangle(0, 0, screenWidth,screenHeight));
        if (radius > 0)
        {
            java.awt.geom.Ellipse2D hole = new java.awt.geom.Ellipse2D.Double(cx - radius, cy - radius, radius * 2, radius * 2);
            mask.subtract(new java.awt.geom.Area(hole));
        }
        g2.setColor(Color.black);
        g2.fill(mask);

        //ring around iris edge
        if (radius > 0 && radius < transitionMaxRadius)
        {
            g2.setColor(new Color(255, 255, 255, 180));
            g2.setStroke(new BasicStroke(4f));
            g2.draw(new java.awt.geom.Ellipse2D.Double(cx - radius, cy - radius, radius * 2, radius * 2));
        }
    }

    public void loadGame(SaveData data) {
        mapIndex = data.mapIndex;
        aSetter.setObject();
        for(int index : data.removedObjectIndices) {
            obj[index] = null;
        }
        if(data.doorOpened && mapIndex == 0) {
            obj[1] = new SuperObject("dooropen", 12 * tileSize, 4 * tileSize, false);
        }
        tileM.loadMap(mapFiles[mapIndex]);

        player.worldX = data.worldX;
        player.worldY = data.worldY;

        siriusMet = data.siriusMet;
        siriusWalkDone = data.siriusWalkDone;
        firedManTalkDone = data.firedManTalkDone;
        pinkGirlTalkDone = data.pinkGirlTalkDone;
        spidermanKidTalkDone = data.spidermanKidTalkDone;
        spiderKidPotionGiven = data.spiderKidPotionGiven;
        castleFadeTriggered = data.castleFadeTriggered;
        player.stoutDefeated = data.stoutDefeated;

        player.holdingLetter = data.holdingLetter;
        player.packDone = data.packDone;
        player.firstBattleLost = data.firstBattleLost;
        player.exitTrain = data.exitTrain;

        player.hp = data.playerHp;
        player.maxHp = data.playerMaxHp;

        battleSystem.partyMembers.clear();

        for(int i = 0; i < data.partyNames.size(); i++)
        {
            String partyName = data.partyNames.get(i);
            int lastHp = data.partyHp.get(i);
//            int maxHp = data.partyMaxHp.get(i);

            switch(partyName) {
                case "Player":
                    PartyMember savedPlayerMember = new PartyMember("Player", player.maxHp, 20, player.icon);
                    battleSystem.partyMembers.add(savedPlayerMember);
                    savedPlayerMember.hp = lastHp;
                    break;
                case "Sirius Magiosis":
                    battleSystem.partyMembers.add(siriusMagiosis);
                    siriusMagiosis.hp = lastHp;
                    break;
            }
        }

        gameState = playState;

    }

    public void update() {

        if (sirius != null) sirius.update();

        if(firedMan != null) firedMan.update();

        if(pinkGirl != null) pinkGirl.update();

        if(spidermanKid != null) spidermanKid.update();

        if (itemPopup != null) itemPopup.update();

        if(spidermanKidTalkDone && !spiderKidPotionGiven && gameState == playState)
        {
            try {
                itemPopup.trigger("Potion", ImageIO.read(getClass().getResourceAsStream("/ui/potionItem.png")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            spiderKidPotionGiven = true;
        }

        if(triggerTrainAnimation && !firstTrainFadeTriggered)
        {
            animateTrainDoors();
            if(trainAnimationIndex != lastDoorFrame) {
                lastDoorFrame = trainAnimationIndex; //ensure only 3 objs are made, not 180
                switch (trainAnimationIndex) {
                    case 1:
                        obj[13] = new SuperObject("traindoorhalfopen", 6 * tileSize, 1.65 * tileSize, true);
                        obj[13].displayWidth = (int) (obj[13].image.getWidth() * 1.5);
                        obj[13].displayHeight = (int) (obj[13].image.getHeight() * 1.5);
                        break;
                    case 2:
                        obj[13] = new SuperObject("traindoormostlyopen", 6 * tileSize, 1.65 * tileSize, true);
                        obj[13].displayWidth = (int) (obj[13].image.getWidth() * 1.5);
                        obj[13].displayHeight = (int) (obj[13].image.getHeight() * 1.5);
                        break;
                    case 3:
                        obj[13] = new SuperObject("traindooropen", 6 * tileSize, 1.65 * tileSize, true);
                        obj[13].displayWidth = (int) (obj[13].image.getWidth() * 1.5);
                        obj[13].displayHeight = (int) (obj[13].image.getHeight() * 1.5);
                        trainAnimationDone = true;
                        break;
                }
            }
        }

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
                } else if (battleSystem.siriusRescued) {
                    battleSystem.siriusRescued = false;
                    doNotStartBattle = true;
                    keyH.spacePressed = false;
                    startBattleTransition(new Stout());
                }
                else {
                    gameState = playState;
                }

                if(siriusWalkDone && !doNotStartBattle)
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
//                            battleSystem.startBattle(new Stout());
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
//                            gameState = battleDialogueState;
                            startBattleTransition(new Stout(), battleDialogueState);
                            battleSystem.isFirstTurnTutorial = true;
                        }

                    } else {
                        if(player.stoutDefeated)
                        {
                            sirius.currentAnimation = NPC.NPCAnimationType.WALK_UPWARD;
                            siriusTargetX = sirius.worldX;
                            siriusTargetY = 2 * tileSize;
                            gameState = siriusFollowState;
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
            player.checkNPCProximity();
            player.checkNPCInteraction();

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
            player.checkNPCProximity();
            player.checkNPCInteraction();
        } else if(gameState == battleState) {
            battleSystem.update();
        }
        else if(gameState == siriusWalkState)
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

        } else if(gameState == siriusFollowState)
        {
            player.update();
            checkCastleMapEntrance();

            double xDistance = Math.abs(player.worldX - sirius.worldX);
            double yDistance = Math.abs(player.worldY - sirius.worldY);
            double distance = Math.max(xDistance, yDistance);

            if(distance < 3 * tileSize && sirius.worldY != siriusTargetY)
            {
                sirius.currentAnimation = NPC.NPCAnimationType.WALK_UPWARD;
                int siriusSpeed = 2;

                if(Math.abs(sirius.worldX - siriusTargetX) > siriusSpeed)
                {
                    sirius.worldX += (sirius.worldX < siriusTargetX) ? siriusSpeed : -siriusSpeed;
                } else if (Math.abs(sirius.worldY - siriusTargetY) > siriusSpeed) {
                    sirius.worldY += (sirius.worldY < siriusTargetY) ? siriusSpeed : -siriusSpeed;
                } else {
                    sirius.worldX = siriusTargetX;
                    sirius.worldY = siriusTargetY;
                }

            } else {
                sirius.currentAnimation = NPC.NPCAnimationType.SIRIUS_WAIT;
            }

        }
        else if (gameState == battleDialogueState && keyH.spacePressed)
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

        } else if (gameState == battleTransitionState)
        {
            updateBattleTransition();
        }

        else if (gameState == examState)
        {
            examSystem.update();
        }

        else if (gameState == trainingState)
        {
            trainingSystem.update();
        }

        else if (gameState == panState)
        {
            int panSpeed = 3;

            gameState = panState;
            updatePan();

        }

        // TESTING
        if (keyH.testPressed && gameState == playState) {
            keyH.testPressed = false;
//            trainingSystem.startTraining();
//            startPan(player.worldX, 0,
//                    () -> {
//                        gameState = playState;
//                    }
//            );
            SaveLoadManager.save(this, 0);
        }

//        if(keyH.testBattlePressed)
//        {
//            keyH.testBattlePressed = false;
//            Enemy testBoss = new Enemy("Test Boss", 100, 10);
//            battleSystem.startBattle(testBoss);
//            System.out.println(testBoss.hp);
//        }

        //scroll image in train
        if (mapIndex == 3)
        {
            scrollX++;
        }

        if(gameState != panState) {
            camWorldX = player.worldX;
            camWorldY = player.worldY;
        }


    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        super.paintComponent(g);


        //scroll bg train interior
        if (mapIndex == 3) {
            double imageWidth = trainScrollImage.getWidth();
            double offset = scrollX % imageWidth;

            int windowScreenY = 0 - player.worldY + player.screenY + (int) (0.35 * tileSize);

            // room's actual left/right edges in screen space
            int roomLeft = tileSize - player.worldX + player.screenX;
            int roomRight = maxWorldCol * tileSize - player.worldX + player.screenX - tileSize;

            Shape oldClip = g2.getClip();
            g2.setClip(roomLeft, windowScreenY, roomRight - roomLeft, tileSize); // adjust height as needed

            int startX = (int) (-offset - imageWidth);
            for (int x = startX; x < screenWidth + imageWidth; x += imageWidth) {
                g2.drawImage(trainScrollImage, x, windowScreenY, null);
            }

            g2.setClip(oldClip);
        }
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

        //NPCs
        if (sirius != null) sirius.draw(g2);
        if(firedMan != null) firedMan.draw(g2);
        if(pinkGirl != null) pinkGirl.draw(g2);
        if(spidermanKid != null) spidermanKid.draw(g2);


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

//        if (gameState == battleDialogueState) {
//            if (battleSystem.battleDialogueIndex < battleSystem.battleDialogueLines.length) {
//                battleSystem.battleCurrentDialogue = battleSystem.battleDialogueLines[battleSystem.battleDialogueIndex];
//
//            } else {
//                gameState = battleState;
//                battleSystem.battleCurrentDialogue = "";
//            }
//        }

        if(gameState == battleTransitionState)
        {
            if(transitionSceneSwitched)
                battleUI.draw(g2);
            drawBattleTransitionOverlay(g2);
        }
        else if(gameState == battleState || gameState == battleDialogueState)
         {
             battleUI.draw(g2);
         }

        if(gameState == examState)
        {
            examUI.draw(g2);
        }

        if(gameState == trainingState)
        {
            trainingUI.draw(g2);
        }

        if (itemPopup != null) itemPopup.draw(g2);

        if (gameState == titleState)
        {
            titleScreen.drawTitleScreen(g2);
        }

        if (gameState == saveSelectState)
        {
            titleScreen.drawSaveSelectScreen(g2);
        }



        g2.dispose();

    }
}
