package Menu;
public abstract class Menu {
    protected String kode;
    protected String nama;
    protected double harga;
    protected int stok;

    public Menu(String kode, String nama, double harga, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
    public void setHarga(double harga) { this.harga = harga; }
    public void setStok(int stok) { this.stok = stok; }

    public abstract double hitungPajak();

    @Override
    public String toString() {
        return String.format("%-10s %-35s Rp%,12.2f %10d", kode, nama, harga, stok);
    }
}