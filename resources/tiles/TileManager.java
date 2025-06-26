package tiles;

import main.GamePanel;

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

        tile = new Tile[10]; //10 kinds of tiles
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/roommap1.txt");
    }

    public void getTileImage() {

        try {

            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/floortile1.png"));
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/walltile1.png"));
            tile[1].collision = true;
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/blacktile.png"));

            //add more later

        }catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void loadMap(String mapPath) {
        try {
            InputStream is = getClass().getResourceAsStream(mapPath); //import text file
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //read the content of the text file

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

                String line = br.readLine(); //read a single line of text

                while (col < gp.maxWorldCol) {
                    String numbers[] = line.split(" "); //get the numbers one by one and put them into an array

                    int num = Integer.parseInt(numbers[col]); //changing from string to int

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol); {
                    col = 0;
                    row++;
                }

            }
            br.close();

        } catch (Exception e) {

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