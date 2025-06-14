package ChannelPembayaran;
public class QRIS implements IChannelPembayaran {
    public double hitungDiskon(double subtotalPlusPajak) { return subtotalPlusPajak * 0.05; }
    public double hitungBiayaAdmin() { return 0; }
    public String getNamaChannel() { return "QRIS"; }
}