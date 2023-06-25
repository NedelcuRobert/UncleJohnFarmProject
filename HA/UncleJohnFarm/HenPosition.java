package UncleJohnFarm;

import java.text.MessageFormat;

public class HenPosition extends Thread {
    private final int number; // Numarul gainii curente
    private int x; // Coordonata x a gainii in ferma
    private int y; // Coordonata y a gainii in ferma
    private final Farm henFarm; // Ferma care contine gaina respectiva

    // Constructor
    public HenPosition(int number, int x, int y, Farm henFarm) {

        this.number = number;
        this.x = x;
        this.y = y;
        this.henFarm = henFarm;
    }

    // Functie care ne genereaza pozitia curenta a fiecarei gaini in ferma
    public void reportHenPosition() {
        System.out.println(MessageFormat.format("Gaina cu numarul {0} se afla la ({1},{2}) in ferma {3}",
                number, x, y, henFarm.getNumber()));
    }

    // Functie pentru a schimba in caz ca ne dorim pozitia unei gaini
    public void changeHenPosition(int newX, int newY) {
        x = newX;
        y = newY;
        // Dupa ce am schimbat pozitia gainii anuntam si directia
        new HenDirection(x);
        new HenDirection(y);
    }

    // Metoda de run
    public void run() {
        do {
            // Semafor pentru odihnirea gainilor
            SaleEggs.getHenRestSemaphore().release();
            // Sleeping 30 milliseconds
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    // Functii pentru gen ale variabilelor

    public int getXposition() {
        return x;
    }

    public int getYposition() {
        return y;
    }

    public int getNumber() {
        return number;
    }

}

