package entity;

import battle.Skill;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PartyMember extends Entity {

    public int attack;
    public BufferedImage icon;
    public ArrayList<Skill> skills = new ArrayList<>();
    public boolean isDefending;

    public PartyMember(String name, int maxHp, int attack, BufferedImage icon)
    {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.icon = icon;
    }


}
