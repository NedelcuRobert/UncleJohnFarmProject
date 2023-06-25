package UncleJohnFarm;

import java.text.MessageFormat;

public class UncleJohn extends Thread {

    private final TransferEggs received_eggs; // Ouale pe care le primeste unchiul John

    // Constructor
    public UncleJohn(TransferEggs eggs) {
        this.received_eggs = eggs;
    }

    // Metoda de run
    public void run() {
        do {
            // Unchiul John primeste oua de la angajatii lui
            int eggs = received_eggs.receiveEggs();
            System.out.println(MessageFormat.format("Unchiul John a primit oul cu numarul {0}", eggs));
        } while (true);
    }

}
