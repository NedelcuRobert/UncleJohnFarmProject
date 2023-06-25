package UncleJohnFarm;

public class HenDirection {

    private final boolean[][] farm;
    private final int count;

    // Constructor
    // Gainile fac oua mutandu-se intr-una din directiile stanga, dreapta, sus, jos
    public HenDirection(int count) {

        this.farm = new boolean[count][count];
        this.count = count;

    }
    // Functie care verifica daca gaina s-a mutat in stanga
    public boolean moveLeft(int X, int Y) {
        return Y - 1 > 0 && !farm[X][Y - 1];
    }

    // Functie care verifica daca gaina s-a mutat in jos
    public boolean moveDown(int X, int Y) {
        return X + 1 < count && !farm[X + 1][Y];
    }

    // Functie care verifica daca gaina s-a mutat in sus
    public boolean moveUp(int X, int Y) {
        return X - 1 > 0 && !farm[X - 1][Y];
    }

    // Functie care verifica daca gaina s-a mutat in dreapta
    public boolean moveRight(int X, int Y) {
        return Y + 1 < count && !farm[X][Y + 1];
    }

}

