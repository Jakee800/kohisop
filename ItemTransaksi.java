import Menu.Menu;

public class ItemTransaksi {
    private Menu menu; 
    private int kuantitas;

    public ItemTransaksi(Menu menu, int kuantitas) { 
        this.menu = menu;
        this.kuantitas = kuantitas;
    }

    // Getters
    public Menu getMenu() { return menu; } 
    public int getKuantitas() { return kuantitas; }

    // Setter
    public void setKuantitas(int kuantitas) { this.kuantitas = kuantitas; }

    public double getTotalHargaItem() {
        return menu.getHarga() * kuantitas; 
    }

    @Override
    public String toString() {
        return String.format("%-20s x %d = Rp%,.2f", menu.getNama(), kuantitas, getTotalHargaItem()); 
    }
}