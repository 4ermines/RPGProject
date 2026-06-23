package entity;

import battle.Skill;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SiriusMagiosis extends PartyMember {

    public SiriusMagiosis(String name, int maxHp, int attack)
    {
        super(name, maxHp, attack, null);
        getSiriusImage();
        skills.add(new Skill("Something", Skill.EffectType.BUFF_ATTACK, 10, "buffs attack or something"));
        skills.add(new Skill("Something else", Skill.EffectType.BUFF_ATTACK, 10, "does something else"));
        skills.add(new Skill("Something else", Skill.EffectType.BUFF_ATTACK, 10, "does something else"));


    }

    public void getSiriusImage()
    {
        try {
            icon = ImageIO.read(getClass().getResourceAsStream("/ui/siriusMagiosisBattleIcon.png"));

        }catch(IOException e) {
            e.printStackTrace();
        }

    }

}
