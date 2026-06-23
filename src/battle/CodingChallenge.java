package battle;

public class CodingChallenge {
    public enum Type{
        FILL_IN_BLANK,
        WRITE_EXPRESSION
    }

    public Type type;
    public String prompt; //shown to player
    public String[] validAnswers;
    public int baseDamage;
    public int timeLimit;

    public CodingChallenge(Type type, String prompt, String[] validAnswers, int baseDamage, int timeLimit)
    {
        this.type = type;
        this.prompt = prompt;
        this.validAnswers = validAnswers;
        this.baseDamage = baseDamage;
        this.timeLimit = timeLimit;
    }
}
