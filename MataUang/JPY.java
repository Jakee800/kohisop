package MataUang;
public class JPY implements IMataUang {
    public double konversiDariIDR(double jumlahIDR) { return jumlahIDR * 10.0; }
    public String getKode() { return "JPY"; }
}