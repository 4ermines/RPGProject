package object;

import entity.NPC;
import entity.Player;
import main.GamePanel;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;

    }

    public void setObject() {
        for (int i = 0; i < gp.obj.length; i++) {
            gp.obj[i] = null;
        }

        switch (gp.mapIndex) {
            //sets objects for roommap1
            case (0):
                gp.sirius = null;
                gp.firedMan = null;
                gp.pinkGirl = null;
                gp.spidermanKid = null;

                gp.obj[0] = new SuperObject("bedbigger", 2*gp.tileSize, 2 * gp.tileSize, true);
                gp.obj[1] = new SuperObject("door1", 12*gp.tileSize, 4 * gp.tileSize, true);
                gp.obj[2] = new SuperObject("door2", 26*gp.tileSize, 6 * gp.tileSize, true);
                gp.obj[3] = new SuperObject("door1", 26*gp.tileSize, 5 * gp.tileSize, true);
                gp.obj[4] = new SuperObject("rug", 4*gp.tileSize, 4 * gp.tileSize, false);
                gp.obj[5] = new SuperObject("backpack1", 2*gp.tileSize, 5 * gp.tileSize, true);
                gp.obj[6] = new SuperObject("dresser", 4*gp.tileSize, 2 * gp.tileSize, true);
                gp.obj[7] = new SuperObject("envelope", 24*gp.tileSize, 5 * gp.tileSize, false);
                gp.obj[8] = new SuperObject("tablebedroom", 7*gp.tileSize, 2 * gp.tileSize, true);
                gp.obj[9] = new SuperObject("chair1", 7*gp.tileSize, 3 * gp.tileSize, true);
                gp.obj[10] = new SuperObject("laptop", 7*gp.tileSize, 2 * gp.tileSize, true);
                gp.obj[11] = new SuperObject("roomitems", 8*gp.tileSize, 2 * gp.tileSize, true);
                break;
            case (1):
                gp.sirius = null;
                gp.firedMan = null;
                gp.pinkGirl = null;
                gp.spidermanKid = null;

                gp.obj[12] = new SuperObject("traindistancedetector", 12.7*gp.tileSize, 2.2 * gp.tileSize, true);
                gp.obj[12].displayWidth = (int) (gp.obj[12].image.getWidth() * 1.5);
                gp.obj[12].displayHeight = (int) (gp.obj[12].image.getHeight() * 1.5);

                gp.obj[13] = new SuperObject("traindoorclosed", 6*gp.tileSize, 1.65 * gp.tileSize, true);
                gp.obj[13].displayWidth = (int) (gp.obj[13].image.getWidth() * 1.5);
                gp.obj[13].displayHeight = (int) (gp.obj[13].image.getHeight() * 1.5);


                break;

            case (2): //9 more
                gp.firedMan = null;
                gp.pinkGirl = null;
                gp.spidermanKid = null;

//                gp.obj[12] = new SuperObject("trainnodoor", 18*gp.tileSize, 16.8 * gp.tileSize, true);
//                gp.obj[12].displayWidth = (int) (gp.obj[12].image.getWidth() * 2.5);
//                gp.obj[12].displayHeight = (int) (gp.obj[12].image.getHeight() * 2.5);

//                gp.obj[13] = new SuperObject("traindoor", 30.3*gp.tileSize, 18.5 * gp.tileSize, false);

                gp.obj[13] = new SuperObject("traindoorclosed", 23*gp.tileSize, 15.5 * gp.tileSize, true);
                gp.obj[13].displayWidth = (int) (gp.obj[13].image.getWidth() * 1.5);
                gp.obj[13].displayHeight = (int) (gp.obj[13].image.getHeight() * 1.5);

                gp.obj[1] = new SuperObject("longerTreesBg", 6 *gp.tileSize, 0 * gp.tileSize, true);
                gp.obj[1].displayWidth = gp.obj[1].image.getWidth() * 2;
                gp.obj[1].displayHeight = gp.obj[1].image.getHeight() * 2;

                //back row
                gp.obj[3] = new SuperObject("forestTree", 5 * gp.tileSize, 11.8 * gp.tileSize, true);
                gp.obj[3].displayWidth = gp.obj[3].image.getWidth() * 2;
                gp.obj[3].displayHeight = gp.obj[3].image.getHeight() * 2;

                gp.obj[4] = new SuperObject("forestTree",4.9 *gp.tileSize, 13.2 * gp.tileSize, true);
                gp.obj[4].displayWidth = gp.obj[4].image.getWidth() * 2;
                gp.obj[4].displayHeight = gp.obj[4].image.getHeight() * 2;


//                gp.obj[14] = new SuperObject("forestTree", 15 * gp.tileSize, 5 * gp.tileSize, true);
//                gp.obj[14].displayWidth = gp.obj[14].image.getWidth() * 2;
//                gp.obj[14].displayHeight = gp.obj[14].image.getHeight() * 2;

                gp.obj[14] = new SuperObject("forestTree",14.4 * gp.tileSize, 14.4 * gp.tileSize, true);
                gp.obj[14].displayWidth = gp.obj[14].image.getWidth() * 2;
                gp.obj[14].displayHeight = gp.obj[14].image.getHeight() * 2;


                gp.obj[15] = new SuperObject("forestTree",13.2 * gp.tileSize, 15.2 * gp.tileSize, true);
                gp.obj[15].displayWidth = gp.obj[15].image.getWidth() * 2;
                gp.obj[15].displayHeight = gp.obj[15].image.getHeight() * 2;

                gp.obj[16] = new SuperObject("forestTree",11.6 * gp.tileSize, 14.7 * gp.tileSize, true);
                gp.obj[16].displayWidth = gp.obj[16].image.getWidth() * 2;
                gp.obj[16].displayHeight = gp.obj[16].image.getHeight() * 2;

                gp.obj[17] = new SuperObject("forestTree",9.8 * gp.tileSize, 14.9 * gp.tileSize, true);
                gp.obj[17].displayWidth = gp.obj[17].image.getWidth() * 2;
                gp.obj[17].displayHeight = gp.obj[17].image.getHeight() * 2;

                gp.obj[19] = new SuperObject("forestTree",5.4 * gp.tileSize,15.1 * gp.tileSize,true);
                gp.obj[19].displayWidth = gp.obj[19].image.getWidth() * 2;
                gp.obj[19].displayHeight = gp.obj[19].image.getHeight() * 2;

                gp.obj[18] = new SuperObject("forestTree",7.5 * gp.tileSize, 15 * gp.tileSize, true);
                gp.obj[18].displayWidth = gp.obj[18].image.getWidth() * 2;
                gp.obj[18].displayHeight = gp.obj[18].image.getHeight() * 2;
//
//                gp.obj[20] = new SuperObject("forestTree",, true);
//                gp.obj[20].displayWidth = gp.obj[20].image.getWidth() * 2;
//                gp.obj[20].displayHeight = gp.obj[20].image.getHeight() * 2;

                //middle row
                gp.obj[21] = new SuperObject("forestTree",14.9 * gp.tileSize, 16.1 * gp.tileSize, true);
                gp.obj[21].displayWidth = gp.obj[21].image.getWidth() * 2;
                gp.obj[21].displayHeight = gp.obj[21].image.getHeight() * 2;

                gp.obj[22] = new SuperObject("forestTree",13.1 * gp.tileSize, 15.8 * gp.tileSize, true);
                gp.obj[22].displayWidth = gp.obj[22].image.getWidth() * 2;
                gp.obj[22].displayHeight = gp.obj[22].image.getHeight() * 2;

                gp.obj[23] = new SuperObject("forestTree",11 * gp.tileSize, 16.3 * gp.tileSize, true);
                gp.obj[23].displayWidth = gp.obj[23].image.getWidth() * 2;
                gp.obj[23].displayHeight = gp.obj[23].image.getHeight() * 2;

                gp.obj[24] = new SuperObject("forestTree",9.3 * gp.tileSize, 16 * gp.tileSize, true);
                gp.obj[24].displayWidth = gp.obj[24].image.getWidth() * 2;
                gp.obj[24].displayHeight = gp.obj[24].image.getHeight() * 2;

                gp.obj[25] = new SuperObject("forestTree",7.4 * gp.tileSize, 15.8 * gp.tileSize, true);
                gp.obj[25].displayWidth = gp.obj[25].image.getWidth() * 2;
                gp.obj[25].displayHeight = gp.obj[25].image.getHeight() * 2;

                gp.obj[26] = new SuperObject("forestTree",5.1 * gp.tileSize, 16.1 * gp.tileSize, true);
                gp.obj[26].displayWidth = gp.obj[26].image.getWidth() * 2;
                gp.obj[26].displayHeight = gp.obj[26].image.getHeight() * 2;

                //front row
                gp.obj[28] = new SuperObject("forestTree",15 * gp.tileSize, 17.3 * gp.tileSize, true);
                gp.obj[28].displayWidth = gp.obj[28].image.getWidth() * 2;
                gp.obj[28].displayHeight = gp.obj[28].image.getHeight() * 2;

                gp.obj[29] = new SuperObject("forestTree",13.2 * gp.tileSize, 16.9 * gp.tileSize, true);
                gp.obj[29].displayWidth = gp.obj[29].image.getWidth() * 2;
                gp.obj[29].displayHeight = gp.obj[29].image.getHeight() * 2;

                gp.obj[30] = new SuperObject("forestTree",11.3 * gp.tileSize, 17 * gp.tileSize, true);
                gp.obj[30].displayWidth = gp.obj[30].image.getWidth() * 2;
                gp.obj[30].displayHeight = gp.obj[30].image.getHeight() * 2;

                gp.obj[31] = new SuperObject("forestTree",9.4 * gp.tileSize, 17.35 * gp.tileSize, true);
                gp.obj[31].displayWidth = gp.obj[31].image.getWidth() * 2;
                gp.obj[31].displayHeight = gp.obj[31].image.getHeight() * 2;

                gp.obj[32] = new SuperObject("forestTree",7.6 * gp.tileSize, 17 * gp.tileSize, true);
                gp.obj[32].displayWidth = gp.obj[32].image.getWidth() * 2;
                gp.obj[32].displayHeight = gp.obj[32].image.getHeight() * 2;

                gp.obj[33] = new SuperObject("forestTree",5.7 * gp.tileSize, 16.8 * gp.tileSize, true);
                gp.obj[33].displayWidth = gp.obj[33].image.getWidth() * 2;
                gp.obj[33].displayHeight = gp.obj[33].image.getHeight() * 2;

                gp.obj[34] = new SuperObject("forestBush1", 1*gp.tileSize, 20 * gp.tileSize, true);

                gp.obj[35] = new SuperObject("forestBush1", 1.2*gp.tileSize, 19.4 * gp.tileSize, true);

                gp.obj[36] = new SuperObject("forestBush2", 5*gp.tileSize, 18.3 * gp.tileSize, true);

                gp.obj[37] = new SuperObject("forestBush1", 12.7*gp.tileSize, 19.2 * gp.tileSize, true);

                gp.obj[38] = new SuperObject("stoutstill", 1.8*gp.tileSize, 13.3 * gp.tileSize, true);

                gp.obj[39] = new SuperObject("forestBush1", 2*gp.tileSize, 13.8 * gp.tileSize, true);

                gp.obj[49] = new SuperObject("forestBush2", 2*gp.tileSize, 12.8 * gp.tileSize, true);

                gp.obj[50] = new SuperObject("forestBush2", 13.3*gp.tileSize, 19.8 * gp.tileSize, true);

                gp.obj[51] = new SuperObject("forestBush2", 6.6*gp.tileSize, 19.3 * gp.tileSize, true);

                gp.obj[52] = new SuperObject("pinkPlants", 1*gp.tileSize, 15 * gp.tileSize, true);

                gp.obj[40] = new SuperObject("forestTree",2 * gp.tileSize, 0 * gp.tileSize, true);
                gp.obj[40].displayWidth = gp.obj[40].image.getWidth() * 2;
                gp.obj[40].displayHeight = gp.obj[40].image.getHeight() * 2;

                gp.obj[41] = new SuperObject("forestTree",3.1 * gp.tileSize, 0.1 * gp.tileSize, true);
                gp.obj[41].displayWidth = gp.obj[41].image.getWidth() * 2;
                gp.obj[41].displayHeight = gp.obj[41].image.getHeight() * 2;

                gp.obj[47] = new SuperObject("forestTree",4.5 * gp.tileSize, 0.45 * gp.tileSize, true);
                gp.obj[47].displayWidth = gp.obj[47].image.getWidth() * 2;
                gp.obj[47].displayHeight = gp.obj[47].image.getHeight() * 2;

                gp.obj[42] = new SuperObject("forestTree",0.9 * gp.tileSize, 0.75 * gp.tileSize, true);
                gp.obj[42].displayWidth = gp.obj[42].image.getWidth() * 2;
                gp.obj[42].displayHeight = gp.obj[42].image.getHeight() * 2;

                gp.obj[43] = new SuperObject("forestTree",0.3 * gp.tileSize, 2.7 * gp.tileSize, true);
                gp.obj[43].displayWidth = gp.obj[43].image.getWidth() * 2;
                gp.obj[43].displayHeight = gp.obj[43].image.getHeight() * 2;

                gp.obj[44] = new SuperObject("forestTree",0.3 * gp.tileSize, 4.5 * gp.tileSize, true);
                gp.obj[44].displayWidth = gp.obj[44].image.getWidth() * 2;
                gp.obj[44].displayHeight = gp.obj[44].image.getHeight() * 2;

                gp.obj[45] = new SuperObject("forestTree",0.4 * gp.tileSize, 6.7 * gp.tileSize, true);
                gp.obj[45].displayWidth = gp.obj[45].image.getWidth() * 2;
                gp.obj[45].displayHeight = gp.obj[45].image.getHeight() * 2;

                gp.obj[46] = new SuperObject("forestTree",0.45 * gp.tileSize, 8.6 * gp.tileSize, true);
                gp.obj[46].displayWidth = gp.obj[46].image.getWidth() * 2;
                gp.obj[46].displayHeight = gp.obj[46].image.getHeight() * 2;


                gp.obj[53] = new SuperObject("forestTree",0.5 * gp.tileSize, 10 * gp.tileSize, true);
                gp.obj[53].displayWidth = gp.obj[53].image.getWidth() * 2;
                gp.obj[53].displayHeight = gp.obj[53].image.getHeight() * 2;


                gp.obj[54] = new SuperObject("forestTree",0 * gp.tileSize, 11 * gp.tileSize, true);
                gp.obj[54].displayWidth = gp.obj[54].image.getWidth() * 2;
                gp.obj[54].displayHeight = gp.obj[54].image.getHeight() * 2;

                gp.obj[55] = new SuperObject("forestTree",0.3 * gp.tileSize, 13 * gp.tileSize, true);
                gp.obj[55].displayWidth = gp.obj[55].image.getWidth() * 2;
                gp.obj[55].displayHeight = gp.obj[55].image.getHeight() * 2;

                gp.obj[56] = new SuperObject("forestTree",0.2 * gp.tileSize, 15 * gp.tileSize, true);
                gp.obj[56].displayWidth = gp.obj[56].image.getWidth() * 2;
                gp.obj[56].displayHeight = gp.obj[56].image.getHeight() * 2;

                gp.obj[57] = new SuperObject("forestTree",0.4 * gp.tileSize, 16.7 * gp.tileSize, true);
                gp.obj[57].displayWidth = gp.obj[57].image.getWidth() * 2;
                gp.obj[57].displayHeight = gp.obj[57].image.getHeight() * 2;

                gp.obj[58] = new SuperObject("forestTree",0.1 * gp.tileSize, 18 * gp.tileSize, true);
                gp.obj[58].displayWidth = gp.obj[58].image.getWidth() * 2;
                gp.obj[58].displayHeight = gp.obj[58].image.getHeight() * 2;

                gp.obj[59] = new SuperObject("forestTree",0 * gp.tileSize, 20 * gp.tileSize, true);
                gp.obj[59].displayWidth = gp.obj[59].image.getWidth() * 2;
                gp.obj[59].displayHeight = gp.obj[59].image.getHeight() * 2;

                gp.obj[60] = new SuperObject("forestBorder", 1 * gp.tileSize, 22 * gp.tileSize, true);
                gp.obj[60].displayWidth = gp.obj[60].image.getWidth() * 2;
                gp.obj[60].displayHeight = gp.obj[60].image.getHeight() * 2;


                break;
            case (3):
                gp.sirius = null;

                gp.firedMan = new NPC(gp, NPC.NPCType.FIREDMAN, 6 * gp.tileSize, (int) (0.8 * gp.tileSize));
                gp.firedMan.currentAnimation = NPC.NPCAnimationType.CRY;

                gp.pinkGirl = new NPC(gp, NPC.NPCType.PINKGIRL, 4 * gp.tileSize, (int) (0.5 * gp.tileSize));

                gp.spidermanKid = new NPC(gp, NPC.NPCType.SPIDERMANKID, (int) (7.8 * gp.tileSize), (int) (1.8 * gp.tileSize));
                gp.spidermanKid.currentAnimation = NPC.NPCAnimationType.EXCITE;

                gp.obj[70] = new SuperObject("trainSeats", 3 * gp.tileSize, 0.5 * gp.tileSize, true);
                gp.obj[70].displayWidth = (int) (gp.obj[70].image.getWidth() * 2.5);
                gp.obj[70].displayHeight = (int) (gp.obj[70].image.getHeight() * 2.5);

                break;

        }

    }

}
