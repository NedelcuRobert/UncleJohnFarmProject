package UncleJohnFarm;

public class TransferEggs {
    private final int[] eggsCount = new int[10]; // Un contor pentru numarul de oua care urmeaza sa fie primite de catre unchiul John
    // Pentru a transfera ouale ne vom folosi e o coada , in care avem capul find Head si coada fiind Tail
    private volatile int head = 0;
    private volatile int tail = 0;

    // Functie pentru ca un angajat sa trimita un ou catre unchiul John
    public synchronized void giveEgg(int egg) {
        if (tail - head == eggsCount.length) {
            do {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (tail - head == eggsCount.length);
        }
        // Se trimite oul deci se adauga la contor
        eggsCount[tail % eggsCount.length] = egg;
        tail += 1;
        notifyAll(); // Dam de stire ca oul s-a trimis cu succes
    }

    // Functie pentru ca unchiul John sa primeasca oua de la angajatii lui
    public synchronized int receiveEggs() {
        if (tail == head) {
            do {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (tail == head);
        }
        // Se primeste oul deci se adauga la contor
        int egg = eggsCount[head % eggsCount.length];
        head += 1;
        notifyAll(); // Dam de stire ca oul s-a primit cu succes
        return egg; // Returnam oul
    }

}

