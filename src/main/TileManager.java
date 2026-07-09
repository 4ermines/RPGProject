package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {

        this.gp = gp;

        tile = new Tile[100]; //100 kinds of tiles
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap(gp.mapFiles[0]);
        gp.mapIndex = 0;
    }

    public void getTileImage() {

        try {

            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/roomtiles.png"));
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/walltile1.png"));
            tile[1].collision = true;
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/blacktile.png"));
            tile[2].collision = true;
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/walltilebottom.png"));
            tile[3].collision = true;
            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/walltileleft.png"));
            tile[4].collision = true;
            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/walltileright.png"));
            tile[5].collision = true;
            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/tiles/walltileinterior.png"));
            tile[6].collision = true;
            tile[7] = new Tile();
            tile[7].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainfloortile.png"));
            tile[7].collision = false;
            tile[8] = new Tile();
            tile[8].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainstairtoptile.png"));
            tile[8].collision = false;
            tile[9] = new Tile();
            tile[9].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainstairbottomtile.png"));
            tile[9].collision = false;
            tile[10] = new Tile();
            tile[10].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainwalltile.png"));
            tile[10].collision = true;

            tile[11] = new Tile();
            tile[11].image = ImageIO.read(getClass().getResourceAsStream("/tiles/traindoortileleft.png"));
            tile[11].collision = true;
            tile[12] = new Tile();
            tile[12].image = ImageIO.read(getClass().getResourceAsStream("/tiles/traindoortileright.png"));
            tile[12].collision = true;

            tile[13] = new Tile();
            tile[13].image = ImageIO.read(getClass().getResourceAsStream("/tiles/darkTile.png"));
            tile[13].collision = false;
            tile[14] = new Tile();
            tile[14].image = ImageIO.read(getClass().getResourceAsStream("/tiles/topDarkStair.png"));
            tile[14].collision = false;
            tile[15] = new Tile();
            tile[15].image = ImageIO.read(getClass().getResourceAsStream("/tiles/bottomDarkStair.png"));
            tile[15].collision = false;
            tile[16] = new Tile();
            tile[16].image = ImageIO.read(getClass().getResourceAsStream("/tiles/darkwalltile.png"));
            tile[16].collision = true;
            tile[17] = new Tile();
            tile[17].image = ImageIO.read(getClass().getResourceAsStream("/tiles/darkTileGrassy1.png"));
            tile[17].collision = false;
            tile[18] = new Tile();
            tile[18].image = ImageIO.read(getClass().getResourceAsStream("/tiles/darkTileGrassy2.png"));
            tile[18].collision = false;
            tile[19] = new Tile();
            tile[19].image = ImageIO.read(getClass().getResourceAsStream("/tiles/darkGrassBland.png"));
            tile[19].collision = false;
            tile[20] = new Tile();
            tile[20].image = ImageIO.read(getClass().getResourceAsStream("/tiles/darkGrassPurpleFlower1.png"));
            tile[20].collision = false;
            tile[21] = new Tile();
            tile[21].image = ImageIO.read(getClass().getResourceAsStream("/tiles/darkGrassPurpleFlower2.png"));
            tile[21].collision = false;
            tile[22] = new Tile();
            tile[22].image = ImageIO.read(getClass().getResourceAsStream("/tiles/actualmiddledarkstair.png"));
            tile[22].collision = false;
            tile[23] = new Tile();
            tile[23].image = ImageIO.read(getClass().getResourceAsStream("/tiles/darkGrassRock.png"));
            tile[23].collision = false;
            tile[24] = new Tile();
            tile[24].image = ImageIO.read(getClass().getResourceAsStream("/tiles/darkGrassWhiteFlower.png"));
            tile[24].collision = false;

            tile[25] = new Tile();
            tile[25].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainFloorTile1.png"));
            tile[25].collision = false;

            tile[26] = new Tile();
            tile[26].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainFloorTile2.png"));
            tile[26].collision = false;

            tile[27] = new Tile();
            tile[27].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainWallTile1.png"));
            tile[27].collision = true;

            tile[28] = new Tile();
            tile[28].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainWallTile2.png"));
            tile[28].collision = true;

            tile[29] = new Tile();
            tile[29].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainWallTileRight.png"));
            tile[29].collision = true;

            tile[30] = new Tile();
            tile[30].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainWallTileLeft.png"));
            tile[30].collision = true;

            tile[31] = new Tile();
            tile[31].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainYellowTile1.png"));
            tile[31].collision = false;

            tile[32] = new Tile();
            tile[32].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainYellowTile2.png"));
            tile[32].collision = false;

            tile[33] = new Tile();
            tile[33].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainrailtile.png"));
            tile[33].collision = true;

            tile[34] = new Tile();
            tile[34].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainWallTileTopLCorner.png"));
            tile[34].collision = true;

            //more corners can be added later if necessary

            tile[37] = new Tile();
            tile[37].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainInteriorFloor.png"));
            tile[37].collision = false;

            tile[38] = new Tile();
            tile[38].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainInteriorWall1.png"));
            tile[38].collision = true;

            tile[39] = new Tile();
            tile[39].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainInteriorWall2.png"));
            tile[39].collision = true;

            tile[40] = new Tile();
            tile[40].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainInteriorWall3.png"));
            tile[40].collision = true;

            tile[41] = new Tile();
            tile[41].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainInteriorWall4.png"));
            tile[41].collision = true;

            tile[42] = new Tile();
            tile[42].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainInteriorDoor.png"));
            tile[42].collision = true;

            tile[43] = new Tile();
            tile[43].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainInteriorWindow1.png"));
            tile[43].collision = true;

            tile[44] = new Tile();
            tile[44].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trainInteriorWindow2.png"));
            tile[44].collision = true;

            //add more later

        }catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void loadMap(String mapPath) {
        try {
            InputStream is = getClass().getResourceAsStream(mapPath); //import text file
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //read the content of the text file

            java.util.List<String> lines = new java.util.ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
            gp.maxWorldRow = lines.size();
            if (gp.maxWorldRow > 0) {
                gp.maxWorldCol = lines.get(0).split(" ").length;
            } else {
                gp.maxWorldCol = 0;
            }
            mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

            // Fill the mapTileNum array
            for (int row = 0; row < gp.maxWorldRow; row++) {
                String[] numbers = lines.get(row).split(" ");
                for (int col = 0; col < gp.maxWorldCol; col++) {
                    mapTileNum[col][row] = Integer.parseInt(numbers[col]);
                }
            }
            } catch (Exception e) {
                e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow]; //extract a tile number which is stored in map tile num

            int worldX = worldCol * gp.tileSize; //camera
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                    && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            } //improve game performance
            worldCol++;
            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }


    }

}
//*       g2.drawImage(tile[0].image, 0, 0, gp.tileSize, gp.tileSize, null);
//        g2.drawImage(tile[0].image, 64, 0, gp.tileSize, gp.tileSize, null);
//        g2.drawImage(tile[0].image, 128, 0, gp.tileSize, gp.tileSize, null);
//        g2.drawImage(tile[0].image, 192, 0, gp.tileSize, gp.tileSize, null);
//        g2.drawImage(tile[0].image, 256, 0, gp.tileSize, gp.tileSize, null);
//        g2.drawImage(tile[0].image, 320, 0, gp.tileSize, gp.tileSize, null);
//        g2.drawImage(tile[0].image, 384, 0, gp.tileSize, gp.tileSize, null);