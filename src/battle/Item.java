package battle;

import entity.PartyMember;

import java.awt.image.BufferedImage;

public class Item {

    public enum EffectType
    {
        HEAL
    }

    public String name;
    public EffectType effectType;
    public int power;
    public String description;
    public BufferedImage icon;

    public boolean usableInBattle;

    public Item(String name, EffectType effectType, int power, String description, BufferedImage icon, boolean usableInBattle)
    {
        this.name = name;
        this.effectType = effectType;
        this.power = power;
        this.description = description;
        this.icon = icon;
        this.usableInBattle = usableInBattle;
    }
}
