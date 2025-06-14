package MataUang;
public class USD implements IMataUang {
    public double konversiDariIDR(double jumlahIDR) { return jumlahIDR / 15.0; }
    public String getKode() { return "USD"; }
}