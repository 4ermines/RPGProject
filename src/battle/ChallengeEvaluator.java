package battle;

public class ChallengeEvaluator {

    public static int evaluate(CodingChallenge challenge, String playerInput, long timeElapsed, int attackStat)
    {
        String cleaned = playerInput.trim().replaceAll("\\s+", " ");

        boolean correct = false;
        for(String answer : challenge.validAnswers)
        {
            if(cleaned.equalsIgnoreCase(answer.trim()))
            {
                correct = true;
                break;
            }
        }

        if(!correct)
        {
            return 0;
        }

        //time bonus
        double timeRatio = 1.0 - ((double) timeElapsed / (challenge.timeLimit * 1000));
        double multiplier = 0.5 + (0.5 * Math.max(0, timeRatio));
        return (int)(challenge.baseDamage * multiplier) + attackStat;
    }
}
