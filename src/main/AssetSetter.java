package main;

import object.SuperObject;

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
            case (0):
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
                gp.obj[12] = new SuperObject("trainnodoor", 6*gp.tileSize, 0.5 * gp.tileSize, true);
                gp.obj[13] = new SuperObject("traindoor", 15.3*gp.tileSize, 0.5 * gp.tileSize, false);

                break;

            case (2):
                gp.obj[12] = new SuperObject("trainnodoor", 21*gp.tileSize, 8.5 * gp.tileSize, true);
                gp.obj[13] = new SuperObject("traindoor", 30.3*gp.tileSize, 8.5 * gp.tileSize, false);
                break;
        }

    }

}
