package UncleJohnFarm;

public class Demo {
    // Functia de rulare a aplicatiei
    public static void main(String[] args) {
        TransferEggs eggs = new TransferEggs(); // Transferam ouale
        UncleJohn uncleJohn = new UncleJohn(eggs); // Instantiem pe unchiul John
        SaleEggs business = new SaleEggs(eggs); // Cream afacerea unchiului John , cea de vanzare de oua
        business.createFarm(); // Afacerea se extinde prin crearea de noi ferme
        uncleJohn.start(); // Unchiul John va incepe sa primeasca oua
    }
}
