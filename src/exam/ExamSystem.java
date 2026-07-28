package exam;

import battle.ChallengeEvaluator;
import battle.CodingChallenge;
import main.GamePanel;

import java.util.ArrayList;

public class ExamSystem {

    public enum ExamPhase
    {
        TYPING,
        SHOW_RESULT,
        EXAM_PASSED,
        EXAM_FAILED
    }

    ArrayList<CodingChallenge> challengePool = new ArrayList<CodingChallenge>();
    CodingChallenge currentChallenge;
    long challengeStartTime;
    int totalQuestions = 15;
    int totalTally;
    int correctTally;
    private boolean codeChallengeStarted = false;
    public String examLog = "";
    private int resultTimer = 0;

    GamePanel gp;
    public ExamPhase phase;

    public ExamSystem(GamePanel gp)
    {
        this.gp = gp;
        loadChallenges();
    }

    public void startExam() {
        codeChallengeStarted = true;
        gp.gameState = gp.examState;
        totalTally = 1;
        correctTally = 0;
        startCodeChallenge();
    }

    //Loads exam questions
    private void loadChallenges() {
        challengePool.add(new CodingChallenge(CodingChallenge.Type.FILL_IN_BLANK, "What is 1 + 1", new String[]{"2"}, 40, 30));
        //TODO: add actual questions...
    }

    //Submit answer for exam challenge
    public void submitAnswer(){
        String input = gp.handler.getText();;
        long timeElapsed = System.currentTimeMillis() - challengeStartTime;
        codeChallengeStarted = false;
        boolean isCorrect = false;
        boolean outOfTime = false;

        if(ChallengeEvaluator.evaluate(currentChallenge, input))
        {
            if(timeElapsed <= currentChallenge.timeLimit * 1000)
            {
                isCorrect = true;
                correctTally++;
            } else {
                outOfTime = true;
            }
        } else {
            //incorrect
        }

        if(isCorrect){
            examLog = "Correct!";
        } else if(outOfTime){
            examLog = "Too slow!";
        } else{
            examLog = "Incorrect!";
        }

        gp.handler.clearText();
        phase = ExamPhase.SHOW_RESULT;
        totalTally++;
    }

    //Selects random code challenge question
    public void startCodeChallenge(){
        int randomIndex = (int)(Math.random() * challengePool.size());

        currentChallenge = challengePool.get(randomIndex);
        challengeStartTime = System.currentTimeMillis();
        phase = ExamSystem.ExamPhase.TYPING;

    }

    private void checkExamEnd(){
        if(totalTally > totalQuestions)
        {
            if(correctTally >= 10)
            {
                phase = ExamPhase.EXAM_PASSED;
            } else {
                phase = ExamPhase.EXAM_FAILED;
            }
        }
    }

    private void forceExamTimeOut(){
        examLog = "Too slow!";
        phase = ExamPhase.SHOW_RESULT;
        totalTally++;
        codeChallengeStarted = false;
        gp.handler.clearText();
    }

    public void update() {
        if(phase == ExamPhase.TYPING){

            examLog = "";

            if(!codeChallengeStarted) {
                startCodeChallenge();
                codeChallengeStarted = true;
            }

            long timeElapsed = System.currentTimeMillis() - challengeStartTime;

            if(timeElapsed >= currentChallenge.timeLimit * 1000)
            {
                forceExamTimeOut();
            }

            if(gp.keyH.enterPressed)
            {
                gp.keyH.enterPressed = false;
                submitAnswer();
            }

            gp.handler.updateTypedText();
        }
        else if(phase == ExamPhase.SHOW_RESULT){

            resultTimer++;

            if(resultTimer > 120)
            {
                resultTimer = 0;
                checkExamEnd();

                if(totalTally <= totalQuestions) {
                    phase = ExamPhase.TYPING;
                }

            }
        }
        //TODO: implement actual dialogue lines
        else if(phase == ExamPhase.EXAM_PASSED){

            if(gp.keyH.spacePressed)
            {
                gp.keyH.spacePressed = false;
                gp.gameState = gp.dialogueState;
                gp.dialogueSpeakers = new String[]{
                        "uhhh",
                };

                gp.dialogueLines = new String[]{
                        "Not bad.",
                };
            }
        }
        else if(phase == ExamPhase.EXAM_FAILED){

            if(gp.keyH.spacePressed)
            {
                gp.keyH.spacePressed = false;
                gp.gameState = gp.dialogueState;
                gp.dialogueSpeakers = new String[]{
                        "uhhh",
                };

                gp.dialogueLines = new String[]{
                        "Not quite my tempo.",
                };

            }

        }
    }

}
