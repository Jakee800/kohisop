package Menu;
import Menu.Menu;

public class Minuman extends Menu {
    public Minuman(String kode, String nama, double harga, int stok) {
        super(kode, nama, harga, stok);
    }

    @Override
    public double hitungPajak() {
        
        if (harga < 50) return 0;
        else if (harga <= 55) return harga * 0.08;
        else return harga * 0.11;
        // Pajak minuman: <50 = 0%, 50-55 = 8%, >55 = 11%
    }
}