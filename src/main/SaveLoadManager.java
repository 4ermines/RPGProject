package main;

import java.io.*;

public class SaveLoadManager {
    private static String saveDir() {
        return System.getProperty("user.home") + File.separator + ".RPGproject" + File.separator + "saves";
    }

    public static void save(GamePanel gp, int slot) {
        File dir = new File(saveDir());
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, "slot" + slot + ".sav");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(new SaveData(gp));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SaveData load(int slot) {
        File file = new File(saveDir(), "slot" + slot + ".sav");
        if(!file.exists()) return null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (SaveData) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
