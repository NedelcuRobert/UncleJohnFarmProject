package UncleJohnFarm;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class SaleEggs {
    public static int number_farms; // Numar total de ferme pe care le detine unchiul John
    private Farm[] farms; // Toate fermele existente
    private HensGenerator[] hens_generators; // Generatorul pentru gaini
    public static int number_total_hens = 1; // Numarul total de gaini
    private final TransferEggs transferQueueEggs; // Transfer al oualor printr-o coada
    private static final ReentrantLock hensCounterLock = new ReentrantLock(); // Zavor pentru numarul de gaini existente
    private static final Semaphore henRestSemaphore = new Semaphore(0); // Semafor pentru odihnirea gainilor
    private Employee[] employees; // Toti angajatii unchiului John

    // Constructor
    public SaleEggs(TransferEggs transferQueueEggs) {
        this.transferQueueEggs = transferQueueEggs;
    }

    //Functie de get pentru blocarea contorului pentru gaini
    public static ReentrantLock getHensCounterLock() {
        return hensCounterLock;
    }

    // Functie de get pentru Semaforul de odihnire a gainilor
    public static Semaphore getHenRestSemaphore() {
        return henRestSemaphore;
    }

    // Functie pentru a crea fermele cu ce contine ea
    public void createFarm() {
        Random rand = new Random();
        number_farms = rand.nextInt(4) + 2; // Numar random intre 2 si 5
        int number_employees = rand.nextInt(10) + 8; // Numar random , dar se stie ca sunt mai multi de 8 de aceea vom adauga 8
        farms = new Farm[number_farms]; // Cream fermele
        hens_generators = new HensGenerator[number_farms]; // Cream gainile
        employees = new Employee[number_employees]; // Cream angajatii

        List<String> asList = Arrays.asList(MessageFormat.format("Au fost create {0} ferme", number_farms),
                MessageFormat.format("Au fost creati {0} angajati", number_employees));
        for (String s : asList) {
            System.out.println(s);
        }
        // Aici se incepe rularea lor
        IntStream.range(0, number_farms).forEachOrdered(i -> {
            int number = rand.nextInt(500) + 100;
            farms[i] = new Farm(number, i + 1);
            hens_generators[i] = new HensGenerator(farms[i]);
            System.out.println(MessageFormat.format("Ferma {0} are {1} gaini",
                    i + 1,
                    number));
        });
        IntStream.range(0, number_employees).forEachOrdered(i -> {
            employees[i] = new Employee(farms, i + 1, transferQueueEggs);
        });
        IntStream.range(0, number_farms).forEachOrdered(i -> {
            hens_generators[i].start();
            farms[i].start();
        });

    }
}

