package ChannelPembayaran;
public class EMoney implements IChannelPembayaran {
    public double hitungDiskon(double subtotalPlusPajak) { return subtotalPlusPajak * 0.07; }
    public double hitungBiayaAdmin() { return 20; }
    public String getNamaChannel() { return "eMoney"; }
}