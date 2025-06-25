import entity.Player;

import java.util.Scanner;

public class Game {
Scanner scanner = new Scanner(System.in);
    public void start() {
        System.out.println("Hello new player! Please enter a name:");
        String name = scanner.nextLine();
        System.out.println("Hi " + name + "!");
        System.out.println("=============================");
        System.out.println("[SCENE: bedroom]");
        System.out.println("You wake up. The sun hits you right in the eye, blinding you. It's a great way to start your day. You think you might get out of bed.");
        System.out.println("Press 1 to get out of bed."); //TODO make it so you walk to the door and living room instead of just a text input!
        String getOutBed = scanner.nextLine();
        if (getOutBed.equalsIgnoreCase("1")) {
            System.out.println("You get out of bed. When you enter the living room, you see a suspicious letter on the floor. You pick it up and open it.");

        } else {
            System.out.println("You stay in bed forever. Never mind. This is totally boring. You get out of bed anyways.");
            System.out.println("When you enter the living room, you see a suspicious letter on the floor. You pick it up.");
        }
        System.out.println("Type 'open letter'");
        String open = scanner.nextLine();
        while (!open.equals("open letter")) {
            System.out.println("Type 'open letter'");
            open = scanner.nextLine();
        }
        System.out.println("You open the letter. It reads:");
        System.out.println("Congratulations!");
        System.out.println("You've caught our eye. You're one of few students to receive this prestigious invitation. We offer you a chance to study at Magicode Academy! This invitation is NOT an admission.");
        System.out.println();
        System.out.println("You jump in joy! You can't believe Magicode wants YOU! Its time to get packing!");
        System.out.println("You pack ur stuff (ill finish this later i promise"); //TODO make this code more interactive, letting the player move around with WASD to pick up the items they need for school
        System.out.println("[SCENE: train]");
        System.out.println("You board a train to whatever country Magicode Academy is in. The ride is long. You take a nap.");
        System.out.println("[SCENE: arrival]");

    }
}
