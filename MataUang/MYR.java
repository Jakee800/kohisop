package MataUang;
public class MYR implements IMataUang {
    public double konversiDariIDR(double jumlahIDR) { return jumlahIDR / 4.0; }
    public String getKode() { return "MYR"; }
}