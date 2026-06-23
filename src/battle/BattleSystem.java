package battle;

import entity.Enemy;
import entity.PartyMember;
import entity.Stout;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class BattleSystem {

    public enum BattlePhase
    {
        PLAYER_CHOOSE_ACTION,
        PLAYER_ANIMATING,
        PLAYER_TYPING,
        SHOW_RESULT,
        ENEMY_TURN,
        BATTLE_WON,
        BATTLE_LOST,
        PLAYER_CHOOSE_ITEM,
        PLAYER_CHOOSE_SKILL,
        PLAYER_CHOOSE_TARGET
    }

    ArrayList<CodingChallenge> challengePool = new ArrayList<CodingChallenge>();
    CodingChallenge currentChallenge;
    long challengeStartTime;

    GamePanel gp;
    public Enemy currentEnemy;
    public BattlePhase phase;
    public int selectedAction = 0;
    public int selectedSkill = 0;
    public int selectedItem = 0;
    public String battleLog = "";
    public String typedText = "";
    private int resultTimer = 0;
    private int enemyTimer = 0;
    public ArrayList<PartyMember> partyMembers = new ArrayList<>();
    public int activePartyIndex = 0;
    int selectedTargetIndex = 0;
    private Item pendingItem;
    private int turnIndexBeforeTargeting;

    //inv for items
    public ArrayList<Item> inventory = new ArrayList<>();


    public BattleSystem(GamePanel gp)
    {
        this.gp  = gp;
        loadChallenges();
    }

    private void loadChallenges()
    {
        challengePool.add(new CodingChallenge(CodingChallenge.Type.FILL_IN_BLANK, "for (int i = 0; i < 5; ___) {", new String[]{ "i++", "i += 1", "i = i + 1" }, 40, 15));
    }

    public void update()
    {
        if (phase == BattlePhase.PLAYER_CHOOSE_ACTION)
        {
            handleInput();
        } else if (phase == BattlePhase.ENEMY_TURN)
        {
            if(enemyTimer == 0)
                doEnemyAttack();
            enemyTimer++;
            if(enemyTimer > 120)
            {
                enemyTimer = 0;
                battleLog = "";
                phase = BattlePhase.PLAYER_CHOOSE_ACTION;
                activePartyIndex = (activePartyIndex + 1) % partyMembers.size();
                while(partyMembers.get(activePartyIndex).hp <= 0)
                {
                    activePartyIndex = (activePartyIndex + 1) % partyMembers.size();
                }
            }

        } else if (phase == BattlePhase.PLAYER_TYPING)
        {
            if(gp.keyH.enterPressed)
            {
                gp.keyH.enterPressed = false;
                submitAnswer();
            }
            if (gp.keyH.lastTypedChar != 0) {
                if (gp.keyH.lastTypedChar == '\b' && typedText.length() > 0) {
                    typedText = typedText.substring(0, typedText.length() - 1);
                } else if (gp.keyH.lastTypedChar != '\b' && gp.keyH.lastTypedChar >= 32) {
                    typedText += gp.keyH.lastTypedChar;
                }
                gp.keyH.lastTypedChar = 0;
            }
            if (gp.keyH.escapePressed) {
                gp.keyH.escapePressed = false;
                typedText = "";
                phase = BattlePhase.PLAYER_CHOOSE_ACTION;
            }
        }
        else if (phase == BattlePhase.SHOW_RESULT) {
            if (gp.battleUI.animator.isAnimDone()) {
                resultTimer++;
                if (resultTimer > 120) {
                    resultTimer = 0;
                    checkBattleEnd(); // moved here
                    if (phase != BattlePhase.BATTLE_WON && phase != BattlePhase.BATTLE_LOST) {
                        phase = BattlePhase.ENEMY_TURN;
                    }
                }
            }
        }
        else if (phase == BattlePhase.PLAYER_CHOOSE_SKILL)
        {
            handleSkillInput();
        } else if (phase == BattlePhase.PLAYER_CHOOSE_TARGET)
        {
            handleTargetInput();
        } else if (phase == BattlePhase.PLAYER_CHOOSE_ITEM)
        {
            handleItemInput();
        } else if (phase == BattlePhase.BATTLE_WON || phase == BattlePhase.BATTLE_LOST)
        {
            if(gp.keyH.spacePressed)
            {
                gp.gameState = gp.playState;
            }
        }

    }

    public void submitAnswer()
    {
        String input = typedText;
        int damage = ChallengeEvaluator.evaluate(currentChallenge, input, System.currentTimeMillis() - challengeStartTime, partyMembers.get(activePartyIndex).attack);
        typedText = "";
        if (damage > 0)
        {
            gp.battleUI.animator.playOnce(BattleAnimator.EnemyAnim.DAMAGED);
        }
        currentEnemy.hp -= damage;
        battleLog = damage > 0 ? "Hit for " + damage + " damage!" : "Wrong answer! Miss!";
        phase = BattlePhase.SHOW_RESULT;

    }

    private void handleInput()
    {
        // move around the battle menu with WASD
        if(selectedAction == 0)
        {
            if(gp.keyH.downPressed)
            {
                selectedAction = 2;
                gp.keyH.downPressed = false;
            }
            if(gp.keyH.rightPressed)
            {
                selectedAction = 1;
                gp.keyH.rightPressed = false;
            }
        }
        else if(selectedAction == 1)
        {
            if(gp.keyH.downPressed)
            {
                selectedAction = 3;
                gp.keyH.downPressed = false;
            }
            if(gp.keyH.leftPressed)
            {
                selectedAction = 0;
                gp.keyH.leftPressed = false;
            }
        }
        else if(selectedAction == 2)
        {
            if(gp.keyH.upPressed)
            {
                selectedAction = 0;
                gp.keyH.upPressed = false;
            }
            if(gp.keyH.rightPressed)
            {
                selectedAction = 3;
                gp.keyH.rightPressed = false;
            }
        }
        else if(selectedAction == 3)
        {
            if(gp.keyH.upPressed)
            {
                selectedAction = 1;
                gp.keyH.upPressed = false;
            }
            if(gp.keyH.leftPressed)
            {
                selectedAction = 2;
                gp.keyH.leftPressed = false;
            }
        }

        if(gp.keyH.spacePressed)
        {
            switch(selectedAction)
            {
                case 0:
                    startCodeChallenge();
                    break;
                case 1:
                    //TODO: implement skills
                    selectedSkill = 0;
                    phase = BattlePhase.PLAYER_CHOOSE_SKILL;
                    break;
                case 2:
                    //TODO: implement items
                    selectedItem = 0;
                    phase = BattlePhase.PLAYER_CHOOSE_ITEM;
                    break;
                case 3:
                    //TODO: implement defend
                    partyMembers.get(activePartyIndex).isDefending = true;
                    battleLog = partyMembers.get(activePartyIndex).name + " defends against enemy attack!";
                    phase = BattlePhase.SHOW_RESULT;
                    break;
            }
            gp.keyH.spacePressed = false;
        }

    }

    private void handleSkillInput()
    {
        if(selectedSkill == 0 && partyMembers.get(activePartyIndex).skills.size() > 0)
        {
            if(gp.keyH.downPressed && partyMembers.get(activePartyIndex).skills.size() > 2)
            {
                selectedSkill = 2;
                gp.keyH.downPressed = false;
            }
            if(gp.keyH.rightPressed && partyMembers.get(activePartyIndex).skills.size() > 1)
            {
                selectedSkill = 1;
                gp.keyH.rightPressed = false;
            }
        }
        else if(selectedSkill == 1 && partyMembers.get(activePartyIndex).skills.size() > 1)
        {
            if(gp.keyH.downPressed && partyMembers.get(activePartyIndex).skills.size() > 3)
            {
                selectedSkill = 3;
                gp.keyH.downPressed = false;
            }
            if(gp.keyH.leftPressed && partyMembers.get(activePartyIndex).skills.size() > 0)
            {
                selectedSkill = 0;
                gp.keyH.leftPressed = false;
            }
        }
        else if(selectedSkill == 2 && partyMembers.get(activePartyIndex).skills.size() > 2)
        {
            if(gp.keyH.upPressed && partyMembers.get(activePartyIndex).skills.size() > 0)
            {
                selectedSkill = 0;
                gp.keyH.upPressed = false;
            }
            if(gp.keyH.rightPressed && partyMembers.get(activePartyIndex).skills.size() > 3)
            {
                selectedSkill = 3;
                gp.keyH.rightPressed = false;
            }
        }
        else if(selectedSkill == 3 && partyMembers.get(activePartyIndex).skills.size() > 3)
        {
            if(gp.keyH.upPressed && partyMembers.get(activePartyIndex).skills.size() > 1)
            {
                selectedSkill = 1;
                gp.keyH.upPressed = false;
            }
            if(gp.keyH.leftPressed && partyMembers.get(activePartyIndex).skills.size() > 2)
            {
                selectedSkill = 2;
                gp.keyH.leftPressed = false;
            }
        }

        if (gp.keyH.escapePressed) {
            gp.keyH.escapePressed = false;
            phase = BattlePhase.PLAYER_CHOOSE_ACTION;
        }

        if(gp.keyH.spacePressed)
        {
            gp.keyH.spacePressed = false;
            PartyMember activeMember = partyMembers.get(activePartyIndex);
            if(selectedSkill < activeMember.skills.size())
            {
                Skill chosen = activeMember.skills.get(selectedSkill);
                applySkill(chosen, activeMember);
            }
        }
    }

    private void applySkill(Skill skill, PartyMember user)
    {
        switch(skill.effectType)
        {
            case DAMAGE:
                currentEnemy.hp -= skill.power;
                battleLog = user.name + " used " + skill.name + "!";
                break;
            case HEAL:
                user.hp += skill.power;
                battleLog = user.name + " healed for " + skill.power + "!";
                break;
            case BUFF_ATTACK:
                user.attack += skill.power;
                battleLog = user.name + "'s attack rose!";
                break;
            case BUFF_DEFENSE:
                break;
        }
        phase = BattlePhase.SHOW_RESULT;

    }

    private void handleItemInput()
    {
        if(selectedItem == 0 && inventory.size() > 0)
        {
            if(gp.keyH.downPressed && inventory.size() > 2)
            {
                selectedItem = 2;
                gp.keyH.downPressed = false;
            }
            if(gp.keyH.rightPressed && inventory.size() > 1)
            {
                selectedItem = 1;
                gp.keyH.rightPressed = false;
            }
        }
        else if(selectedItem == 1 && inventory.size() > 1)
        {
            if(gp.keyH.downPressed && inventory.size() > 3)
            {
                selectedItem = 3;
                gp.keyH.downPressed = false;
            }
            if(gp.keyH.leftPressed && inventory.size() > 0)
            {
                selectedItem = 0;
                gp.keyH.leftPressed = false;
            }
        }
        else if(selectedItem == 2 && inventory.size() > 2)
        {
            if(gp.keyH.upPressed && inventory.size() > 0)
            {
                selectedItem = 0;
                gp.keyH.upPressed = false;
            }
            if(gp.keyH.rightPressed && inventory.size() > 3)
            {
                selectedItem = 3;
                gp.keyH.rightPressed = false;
            }
        }
        else if(selectedItem == 3 && inventory.size() > 3)
        {
            if(gp.keyH.upPressed && inventory.size() > 1)
            {
                selectedItem = 1;
                gp.keyH.upPressed = false;
            }
            if(gp.keyH.leftPressed && inventory.size() > 2)
            {
                selectedItem = 2;
                gp.keyH.leftPressed = false;
            }
        }

        if (gp.keyH.escapePressed) {
            gp.keyH.escapePressed = false;
            phase = BattlePhase.PLAYER_CHOOSE_ACTION;
        }

        if(gp.keyH.spacePressed) {
            gp.keyH.spacePressed = false;
            turnIndexBeforeTargeting = activePartyIndex;
            selectedTargetIndex = turnIndexBeforeTargeting;
            phase = BattlePhase.PLAYER_CHOOSE_TARGET;
            pendingItem = inventory.get(selectedItem);
        }
    }

    private void handleTargetInput()
    {
        battleLog = "Who would you like to use this item on? Press enter to confirm.";
        if(gp.keyH.downPressed)
        {
            gp.keyH.downPressed = false;
            if(selectedTargetIndex + 1 < partyMembers.size())
            {
                selectedTargetIndex++;
                activePartyIndex++;
            }
        } else if(gp.keyH.upPressed)
        {
            gp.keyH.upPressed = false;
            if(selectedTargetIndex - 1 >= 0)
            {
                selectedTargetIndex--;
                activePartyIndex--;
            }
        }
        if(gp.keyH.enterPressed)
        {
            gp.keyH.enterPressed = false;
            activePartyIndex = turnIndexBeforeTargeting; //reset because its just for the highlight
            useItem(pendingItem, partyMembers.get(selectedTargetIndex));
            phase = BattlePhase.SHOW_RESULT;
            selectedTargetIndex = 0;
        }
        if (gp.keyH.escapePressed) {
            gp.keyH.escapePressed = false;
            battleLog = "";
            activePartyIndex = turnIndexBeforeTargeting;
            phase = BattlePhase.PLAYER_CHOOSE_ITEM;
        }

    }

    private void useItem(Item item, PartyMember target)
    {
        switch(item.effectType)
        {
            case HEAL:
                target.hp += item.power;
                battleLog = target.name + " healed for " + item.power + "!";
                break;
        }
    }

    private void doEnemyAttack()
    {
        int dmg = currentEnemy.attack;
        PartyMember target = partyMembers.get(activePartyIndex);

        gp.battleUI.animator.playOnce(BattleAnimator.EnemyAnim.ATTACK);

        if(target.isDefending)
        {
            dmg /= 2;
            target.isDefending = false; //reset after one turn
        }

        target.hp -= dmg;
        battleLog = currentEnemy.name + " attacks for " + dmg + " damage!";

    }

    public void startCodeChallenge() {

        int randomIndex = (int)(Math.random() * challengePool.size());

        currentChallenge = challengePool.get(randomIndex);
        challengeStartTime = System.currentTimeMillis();
//        gp.codeInput.setText("");
//        gp.codeInput.setVisible(true);
//        gp.codeInput.requestFocus();
        phase = BattlePhase.PLAYER_TYPING;
    }

    public void startBattle(Enemy enemy)
    {
        gp.gameState = gp.battleState;
        currentEnemy = enemy;
        phase = BattlePhase.PLAYER_CHOOSE_ACTION;
    }

    private void checkBattleEnd()
    {
        if(currentEnemy.hp <= 0)
        {
            phase = BattlePhase.BATTLE_WON;
            return;
        }

        boolean allDead = true;
        for (PartyMember member : partyMembers)
        {
            if(member.hp > 0)
            {
                allDead = false;
                break;
            }
        }

        if(allDead)
        {
            phase = BattlePhase.BATTLE_LOST;
        }
    }

}
