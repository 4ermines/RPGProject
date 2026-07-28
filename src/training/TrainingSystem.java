package training;

import battle.ChallengeEvaluator;
import battle.CodingChallenge;
import main.GamePanel;

import java.util.ArrayList;

public class TrainingSystem {
    public enum TrainingPhase
    {
        TYPING,
        SHOW_RESULT,
        QUIT_SCREEN
    }

    ArrayList<CodingChallenge> challengePool = new ArrayList<CodingChallenge>();
    CodingChallenge currentChallenge;
    long challengeStartTime;
    private boolean codeChallengeStarted = false;
    public String trainingLog = "";
    private int resultTimer = 0;
    public int selectedAction = 0;

    boolean isPreExam = true;

//    private int confirmContinuation = 0;

    GamePanel gp;
    public TrainingSystem.TrainingPhase phase;

    public TrainingSystem(GamePanel gp)
    {
        this.gp = gp;
        loadChallenges();
    }

    public void startTraining() {
        codeChallengeStarted = true;
        gp.gameState = gp.trainingState;
        startCodeChallenge();
    }

    //Loads training questions
    private void loadChallenges() {
        challengePool.add(new CodingChallenge(CodingChallenge.Type.FILL_IN_BLANK, "What is 1 + 1", new String[]{"2"}, 40, 30));
        //TODO: add actual questions...
    }

    public void startCodeChallenge(){
        int randomIndex = (int)(Math.random() * challengePool.size());

        currentChallenge = challengePool.get(randomIndex);
        challengeStartTime = System.currentTimeMillis();
        phase = TrainingSystem.TrainingPhase.TYPING;

    }

    //Submit answer for training challenge
    public void submitAnswer(){
        String input = gp.handler.getText();;
        codeChallengeStarted = false;
        boolean isCorrect = ChallengeEvaluator.evaluate(currentChallenge, input);

        if(isCorrect){
            trainingLog = "Correct!";
        } else {
            trainingLog = "Incorrect!";
        }

//        confirmContinuation++;
        gp.handler.clearText();
        phase = TrainingSystem.TrainingPhase.SHOW_RESULT;
    }

    public void handleQuitInput() {
        //would you like to quit training?
        if(selectedAction == 0) { //yes
            if(gp.keyH.rightPressed)
            {
                selectedAction = 1;
                gp.keyH.rightPressed = false;
            }

            if(gp.keyH.spacePressed) {
                gp.keyH.spacePressed = false;

                if(isPreExam) {
                    isPreExam = false;
                    gp.gameState = gp.dialogueState;
                    gp.dialogueSpeakers = new String[]{
                            "Sirius Magiosis",
                    };

                    gp.dialogueLines = new String[]{
                            "Ready for the exam?",
                            "You're gonna do great!"
                    };
                } else {
                    gp.gameState = gp.dialogueState;
                    gp.dialogueSpeakers = new String[]{
                            "Sirius Magiosis",
                    };

                    gp.dialogueLines = new String[]{
                            "See you later!",
                    };

                }
            }
        } else if (selectedAction == 1) { //no
            if(gp.keyH.leftPressed)
            {
                selectedAction = 0;
                gp.keyH.leftPressed = false;
            }

            if(gp.keyH.spacePressed) {
                gp.keyH.spacePressed = false;
                startTraining(); //idk
                selectedAction = 0;
            }
        }
    }

    public void update() {

        if(phase == TrainingPhase.TYPING)
        {
            trainingLog = "";

            if(gp.keyH.escapePressed) {
                gp.keyH.escapePressed = false;
                phase = TrainingPhase.QUIT_SCREEN;
            }

            if(!codeChallengeStarted && phase == TrainingPhase.TYPING) {
                startCodeChallenge();
                codeChallengeStarted = true;
            }

            if(gp.keyH.enterPressed && phase == TrainingPhase.TYPING)
            {
                gp.keyH.enterPressed = false;
                submitAnswer();
            }

            gp.handler.updateTypedText();

        }
        else if(phase == TrainingPhase.SHOW_RESULT)
        {
            resultTimer++;

            if(gp.keyH.escapePressed) {
                gp.keyH.escapePressed = false;
                phase = TrainingPhase.QUIT_SCREEN;
            }

            if(resultTimer > 120 && phase == TrainingPhase.SHOW_RESULT)
            {
                resultTimer = 0;

                phase = TrainingPhase.TYPING;
            }
        }
        else if(phase == TrainingPhase.QUIT_SCREEN)
        {
            handleQuitInput();
        }
    }


}
