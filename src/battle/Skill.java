package battle;

public class Skill {
    public enum EffectType
    {
        DAMAGE,
        HEAL,
        BUFF_ATTACK,
        BUFF_DEFENSE
    }

    public String name;
    public EffectType effectType;
    public int power;
    public String description;

    public Skill(String name, EffectType effectType, int power, String description)
    {
        this.name = name;
        this.effectType = effectType;
        this.power = power;
        this.description = description;
    }



}
