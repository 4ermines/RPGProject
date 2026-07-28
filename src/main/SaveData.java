package main;

import battle.Item;
import entity.PartyMember;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    // Position
    public int mapIndex;
    public int worldX;
    public int worldY;

    // Player stats
    public int playerHp;
    public int playerMaxHp;

    // Quest / dialogue flags
    public boolean siriusMet;
    public boolean siriusWalkDone;
    public boolean firedManTalkDone;
    public boolean pinkGirlTalkDone;
    public boolean spidermanKidTalkDone;
    public boolean spiderKidPotionGiven;
    public boolean castleFadeTriggered;
    public boolean stoutDefeated;

    public boolean doorOpened;
    public boolean holdingLetter;
    public boolean packDone;
    public boolean firstBattleLost;
    public boolean exitTrain;


    // Battle system state
    public List<String> inventoryNames = new ArrayList<>();
    public List<Integer> inventoryAmounts = new ArrayList<>(); // if items stack
    public List<String> partyNames = new ArrayList<>();
    public List<Integer> partyHp = new ArrayList<>();
    public List<Integer> partyMaxHp = new ArrayList<>();
    public List<Integer> removedObjectIndices = new ArrayList<>();

    public SaveData(GamePanel gp) {
        mapIndex = gp.mapIndex;
        worldX = gp.player.worldX;
        worldY = gp.player.worldY;

        playerHp = gp.player.hp;
        playerMaxHp = gp.player.maxHp; // adjust to your actual field names

        siriusMet = gp.siriusMet;
        siriusWalkDone = gp.siriusWalkDone;
        firedManTalkDone = gp.firedManTalkDone;
        pinkGirlTalkDone = gp.pinkGirlTalkDone;
        spidermanKidTalkDone = gp.spidermanKidTalkDone;
        spiderKidPotionGiven = gp.spiderKidPotionGiven;
        castleFadeTriggered = gp.castleFadeTriggered;
        stoutDefeated = gp.player.stoutDefeated;

        doorOpened = gp.player.doorOpened;
        holdingLetter = gp.player.holdingLetter;
        packDone = gp.player.packDone;
        firstBattleLost = gp.player.firstBattleLost;
        exitTrain = gp.player.exitTrain;

        for (Item item : gp.battleSystem.inventory) {
            inventoryNames.add(item.name); // adjust to your actual Item fields
        }
        for (PartyMember pm : gp.battleSystem.partyMembers) {
            partyNames.add(pm.name);
            partyHp.add(pm.hp);
            partyMaxHp.add(pm.maxHp);
        }
        for (int i = 0; i < gp.obj.length; i++) {
            if(gp.obj[i] == null)
            {
                removedObjectIndices.add(i);
            }
        }
    }
}