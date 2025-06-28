package main;

import object.SuperObject;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;

    }

    public void setObject() {

        gp.obj[0] = new SuperObject("bedbigger", 2*gp.tileSize, 2 * gp.tileSize, true);
        gp.obj[1] = new SuperObject("door1", 12*gp.tileSize, 4 * gp.tileSize, true);
        gp.obj[2] = new SuperObject("door2", 26*gp.tileSize, 6 * gp.tileSize, true);
        gp.obj[3] = new SuperObject("door1", 26*gp.tileSize, 5 * gp.tileSize, true);
        gp.obj[4] = new SuperObject("rug", 4*gp.tileSize, 4 * gp.tileSize, false);
        gp.obj[5] = new SuperObject("backpack1", 2*gp.tileSize, 5 * gp.tileSize, false);
        gp.obj[6] = new SuperObject("dresser", 4*gp.tileSize, 2 * gp.tileSize, true);
        gp.obj[7] = new SuperObject("envelope", 24*gp.tileSize, 5 * gp.tileSize, false);





    }
}
