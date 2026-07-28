package main;

import entity.Entity;
import entity.NPC;
import object.SuperObject;

import java.awt.*;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int tileNum1, tileNum2;

        switch(entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) {

        int index = 999;

        for(int i = 0; i < gp.obj.length; i++) {
            if(gp.obj[i] != null) {
                SuperObject obj = gp.obj[i];

                Rectangle entityArea = new Rectangle(entity.worldX + entity.solidArea.x,  entity.worldY + entity.solidArea.y,
                        entity.solidArea.width, entity.solidArea.height);

                Rectangle objectArea = new Rectangle((int) (obj.worldX + obj.solidArea.x), (int) (obj.worldY + obj.solidArea.y),
                        obj.solidArea.width, obj.solidArea.height);

                //get entity's solid area position
                //entity.solidArea.x = entity.worldX + entity.solidArea.x;
                //entity.solidArea.y = entity.worldY + entity.solidArea.y;

                //get object's solid area position
                //gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                //gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch(entity.direction) {
                    case "up":
                        entityArea.y -= entity.speed;
                        if (entityArea.intersects(objectArea)) {
                            if(gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                        }
                        break;
                    case "down":
                        entityArea.y += entity.speed;
                        if (entityArea.intersects(objectArea)) {
                            if(gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                        }
                        break;
                    case "left":
                        entityArea.x -= entity.speed;
                        if (entityArea.intersects(objectArea)) {
                            if(gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                        }
                        break;
                    case "right":
                        entityArea.x += entity.speed;
                        if (entityArea.intersects(objectArea)) {
                            if(gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                        }
                        break;

                }
                //entity.solidArea.x = entity.solidAreaDefaultX;
                //entity.solidArea.y = entity.solidAreaDefaultY;
                //gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                //gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }



        return index;
    }

    public void checkNPC(Entity entity) {
        NPC[] npcs = {gp.firedMan, gp.pinkGirl, gp.spidermanKid};
        for (NPC npc : npcs) {
            if (npc == null) continue;

            Rectangle entityArea = new Rectangle(
                    entity.worldX + entity.solidArea.x,
                    entity.worldY + entity.solidArea.y,
                    entity.solidArea.width, entity.solidArea.height
            );
            Rectangle npcArea = new Rectangle(
                    npc.worldX + npc.solidArea.x,
                    npc.worldY + npc.solidArea.y + 20,
                    npc.solidArea.width, npc.solidArea.height
            );

            switch (entity.direction) {
                case "up": entityArea.y -= entity.speed; break;
                case "down": entityArea.y += entity.speed; break;
                case "left": entityArea.x -= entity.speed; break;
                case "right": entityArea.x += entity.speed; break;
            }

            if (entityArea.intersects(npcArea)) {
                entity.collisionOn = true;
            }
        }
    }
}
