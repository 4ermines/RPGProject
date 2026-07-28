package battle;

import entity.PartyMember;

public class Item {

    public enum EffectType
    {
        HEAL
    }

    public String name;
    public EffectType effectType;
    public int power;
    public String description;

    public Item(String name, EffectType effectType, int power, String description)
    {
        this.name = name;
        this.effectType = effectType;
        this.power = power;
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public int getPower()
    {
        return power;
    }
}
