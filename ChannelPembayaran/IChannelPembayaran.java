package ChannelPembayaran;
public interface IChannelPembayaran {
    double hitungDiskon(double subtotalPlusPajak);
    double hitungBiayaAdmin();
    String getNamaChannel();
}