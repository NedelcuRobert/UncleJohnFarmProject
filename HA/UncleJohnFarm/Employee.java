package UncleJohnFarm;

import java.text.MessageFormat;
import java.util.Random;

public class Employee {

    private final int number; // Numarul angajatului curent
    private final Farm[] farms; // Fermele existente ale unchiului John
    private final TransferEggs eggs; // O coada in care vom adauga ouale

    // Constructor
    public Employee(Farm[] farms, int number, TransferEggs eggs) {

        this.farms = farms;
        this.number = number;
        this.eggs = eggs;
    }

    // Metoda Run
    public void run() throws Exception {

        while (true) {
            // Primirea oului de la ferma
            int egg = transferEggFromFarm();

            switch (egg) {
                case 0:
                    break;
                default:
                    System.out.println(MessageFormat.format("Angajatul cu numarul {0} a luat oul cu numarul {1} ", number, egg));
                    transferEggToUncleJohn(egg);
                    break;
            }
            // Sleeping between 10 and 30 milliseconds
            Random rand = new Random();
            long millis = rand.nextInt(30) + 10;
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Functie care ne ajuta sa transferam un ou catre unchiul John
    private void transferEggToUncleJohn(int egg) {
        // Dam oul
        eggs.giveEgg(egg);
        System.out.println(MessageFormat.format("Angajatul cu numarul {0} i-a trimis lui unchiul John oul cu numarul {1} ", number, egg));
    }

    // Functie care ne ajuta sa luam un ou de la ferma
    private int transferEggFromFarm() throws Exception {
        Random rand = new Random();
        int farm = rand.nextInt(SaleEggs.number_farms);
        // Luam oul
        return farms[farm].getEgg();
    }
}

