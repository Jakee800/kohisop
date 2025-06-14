package MataUang;
public class EUR implements IMataUang {
    public double konversiDariIDR(double jumlahIDR) { return jumlahIDR / 14.0; }
    public String getKode() { return "EUR"; }
}