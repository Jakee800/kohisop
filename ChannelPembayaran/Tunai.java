package ChannelPembayaran;
public class Tunai implements IChannelPembayaran {
    public double hitungDiskon(double subtotalPlusPajak) { return 0; }
    public double hitungBiayaAdmin() { return 0; }
    public String getNamaChannel() { return "Tunai"; }
}