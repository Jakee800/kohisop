package Menu;
public class Makanan extends Menu {
    public Makanan(String kode, String nama, double harga, int stok) {
        super(kode, nama, harga, stok);
    }

    @Override
    public double hitungPajak() {
        
        if (harga > 50) {
            return harga * 0.08;
        } else {
            return harga * 0.11;
        }
        // Pajak makanan: 8% jika harga > 50, 11% jika harga <= 50
    }
}