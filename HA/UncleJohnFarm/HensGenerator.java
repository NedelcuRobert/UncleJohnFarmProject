package UncleJohnFarm;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class HensGenerator extends Thread {

    private final Farm farm; // Ferma pentru care firul va genera gainile

    // Constructor
    public HensGenerator(Farm farm) {
        this.farm = farm;
    }

    // Functie pentru a crea gaini cu ajutorul random
    public void createHen() {
        Random rand = new Random();
        ReentrantLock farmLock = farm.getFarmLock();
        // Gainile nu se pot misca in timp ce in ferma este adugata o noua gaina
        farmLock.lock(); // Vom bloca ferma
        var farmsCount = farm.getSize();
        if (farm.getNumberOfHens() == (farmsCount / 2)) {
            farmLock.unlock(); // Deblocam ferma
        } else {
            var X = rand.nextInt(farmsCount); // Generam random coordonata X pentru pozitie gainii noi
            var Y = rand.nextInt(farmsCount); // Generam random coordonata Y pentru pozitia gainii noi
            ReentrantLock hensCounterLock = SaleEggs.getHensCounterLock(); // Niciun alt fir nu poate accesa numarul de gaini din ferma
            hensCounterLock.lock();
            // Cream noua gaina
            HenPosition hen = new HenPosition(SaleEggs.number_total_hens, X, Y, farm); // Generam random si pozitia acesteia
            // Adaugam gaina in ferma noastra
            if (farm.addHen(hen)) {
                SaleEggs.number_total_hens += 1; // Numarul total de gaini va i incrementat cu 1
                System.out.println(MessageFormat.format("Gaina cu numarul {0} a fost adaugata in ferma {1} cu succes ", hen.getNumber(),
                        farm.getNumber()));
                hensCounterLock.unlock(); // Deblocam counterLock-ul
            } else {
                hensCounterLock.unlock(); // Deblocam counterLock-ul
            }
            farmLock.unlock(); // Deblocam ferma
        }
    }

    // Metoda de run
    public void run() {
        do {
            Random rand = new Random();
            long millis = rand.nextInt(1000) + 500;
            // Sleeping a random time between 500 and 1000 milliseconds
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Crearea unei gaini noi
            createHen();
        } while (true);
    }
}