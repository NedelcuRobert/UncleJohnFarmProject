package UncleJohnFarm;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Farm extends Thread {

    private final int number; // Numarul fermei curente
    private final int size; // Dimensiunea matricei " a fermei "
    private final boolean[][] farm;
    private final ReentrantLock farmLock = new ReentrantLock(); // Zavor pentru ferma
    private final ReentrantLock hensListLock = new ReentrantLock(); // Zavor pentru gaini
    private final ReentrantLock eggsLock = new ReentrantLock(); // Zavor pentru oua
    private final ArrayList<HenPosition> henArrayList = new ArrayList<>(); // O lista in care vom memora pozitia gainilor
    private final ArrayList<Integer> eggs = new ArrayList<>(); // Lista de oua
    private final Semaphore employeeSemaphore = new Semaphore(10); // Semafor pentru numarul maxim permis de angajati care poti citi
    private int count_egg = 1; // Contor pentru al catelea ou scoate gaina respectiva, acesta va porni de la 1 (fiind primul ou din afacerea lui John)

    // Constructor
    public Farm(int size, int number) {
        this.farm = new boolean[size][size];
        this.number = number;
        this.size = size;
    }

    // Metoda de run
    public void run() {
        do {
            // Ferma  = fir de executie
            positionOfHen(); //  Aici vom avea pozitia gainilor
            /* Sleeping for 3000 milliseconds */
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (true);
    }

    // Functie pentru a adauga o gaina in ferma
    public boolean addHen(HenPosition henPosition) {
        var result = false;
        hensListLock.lock(); // Blocam lista de gaini

        var X = henPosition.getXposition();
        var Y = henPosition.getYposition();
        // Daca pozitia acesteia nu este deja ocupata , o adaugam in lista

        if (!farm[X][Y]) {
            farm[X][Y] = true;
            henArrayList.add(henPosition); // Adaugam gaina
            henPosition.start();
            henPosition.reportHenPosition(); //  Raportam pozitia actuala a gainii
            hensListLock.unlock(); // Deblocam lista de gaini
            result = true;
        }
        return result; // Returnam rezultatul
    }

    // Functie pentru a raporta pozitia si schimba pozitia gainii , iar atunci cand o schimba in una din directiile sus, jos, dreapta,stanga
    // Gaina va crea ou
    private void positionOfHen() {
        try {
            Random rand = new Random();
            // Blocam listele
            farmLock.lock();
            hensListLock.lock();
            eggsLock.lock();
            Iterator<HenPosition> iterator = henArrayList.iterator();
            if (iterator.hasNext()) {
                do {
                    HenPosition hen = iterator.next();
                    hen.reportHenPosition(); // Raportam pozitia gainii
                    var X = rand.nextInt(size); // Generam random coordonata X pentru ca gaina se poate muta dupa ce raporteaza pozitia
                    var Y = rand.nextInt(size); // Generam random coordonata Y pentru ca gaina se poate muta dupa ce raporteaza pozitia
                    hen.changeHenPosition(X, Y); // Schimbam pozitia gainii , deoarece dupa ce isi raporteaza pozitia ea isi poate schimba pozitia initiala
                    HenDirection hen_direction = new HenDirection(size); // Instantiem directia acesteia dupa ce am adaugat noile coordonate dupa schimbare
                    // Verificam daca gaina se muta in jos,atunci va crea ou
                    if(hen_direction.moveDown(hen.getXposition(), hen.getYposition())){
                        createEgg(hen.getNumber(), count_egg);
                    }
                    // Verificam daca gaina se muta in stanga,atunci va crea ou
                    else if(hen_direction.moveLeft(hen.getXposition(), hen.getYposition())){
                        createEgg(hen.getNumber(), count_egg);
                    }
                    // Verificam daca gaina se muta in dreapta,atunci va crea ou
                    else if(hen_direction.moveRight(hen.getXposition(), hen.getYposition())){
                        createEgg(hen.getNumber(), count_egg);
                    }
                    // Verificam daca gaina se muta in sus,atunci va crea ou
                    else if(hen_direction.moveUp(hen.getXposition(), hen.getYposition())){
                        createEgg(hen.getNumber(), count_egg);
                    }
                    // Altfel gaina nu va crea ou
                } while (iterator.hasNext());
            }
        } catch (Exception e) {
            System.out.println(MessageFormat.format("Exceptie din ferma: {0}", e.getMessage()));
            e.printStackTrace();
        } finally {
            // Dupa raportarea pozitiei vom debloca listele.
            hensListLock.unlock();
            farmLock.unlock();
            eggsLock.unlock();

        }
    }

    // Functia pentru a crea un ou de catre gaina
    private void createEgg(int hen_number, int egg) {
        try {
            eggsLock.lock(); // Blocam lista de oua
            eggs.add(egg); // Adaugam oul
            count_egg++; // Daca oul s-a creat inseamna ca oul viitor scos de aceeasi gaina o sa fie incrementat cu 1
            System.out.println(MessageFormat.format("Gaina cu numarul: {0} a scos oul: {1}", hen_number, egg));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eggsLock.unlock(); // Deblocam lista de oua
        }
    }

    // Functie de get a unui ou
    public int getEgg() throws Exception {
        int egg;
        try {
            employeeSemaphore.acquire();
            // Maxim 10 angajati ai fermei pot citi simultan din sistemul de monitorizare
            eggsLock.lock(); // Blocam lista de oua
            egg = eggs.get(eggs.size() - 1); // Luam oul
            eggs.remove(eggs.size() - 1); // Apoi decrementam deoarece am luat un ou , deci numarul de ou va fi mai mic cu 1
        } finally {
            eggsLock.unlock(); // Deblocheaza lista de oua
            employeeSemaphore.release();
        }
        return egg; // Returnam oul
    }

    // Functii de get ale variabilelor
    public int getSize() {
        return size;
    }
    public int getNumber() {
        return number;
    }
    public ReentrantLock getFarmLock() {
        return farmLock;
    }
    public int getNumberOfHens() {
        return henArrayList.size();
    }
}
