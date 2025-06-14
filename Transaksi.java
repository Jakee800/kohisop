import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import ChannelPembayaran.IChannelPembayaran;
import ChannelPembayaran.EMoney;
import ChannelPembayaran.QRIS;
import ChannelPembayaran.Tunai;
import MataUang.EUR;
import MataUang.IDR;
import MataUang.JPY;
import MataUang.MYR;
import MataUang.IMataUang;
import MataUang.USD;
import Menu.Makanan;
import Menu.Menu;
import Menu.Minuman;

public class Transaksi {
    private String idTransaksi;
    private LinkedList<ItemTransaksi> keranjangBelanja;
    private LocalDateTime waktuTransaksi;
    private double totalBelanjaSebelumPajakDanDiskon;
    private double totalPajak;
    private double totalDiskon;
    private double biayaAdmin;
    private double totalBelanjaFinal;
    private double bayar;
    private double kembalian;

    private Member member;
    private int initialMemberPoin;
    private int finalMemberPoin;
    private int poinDigunakan;

    private IChannelPembayaran channelPembayaran;
    private IMataUang mataUang;

    public Transaksi() {
        this.idTransaksi = UUID.randomUUID().toString().substring(0, 8);
        this.keranjangBelanja = new LinkedList<>();
        this.waktuTransaksi = LocalDateTime.now();
        this.totalBelanjaSebelumPajakDanDiskon = 0.0;
        this.totalPajak = 0.0;
        this.totalDiskon = 0.0;
        this.biayaAdmin = 0.0;
        this.totalBelanjaFinal = 0.0;
        this.bayar = 0.0;
        this.kembalian = 0.0;
        this.poinDigunakan = 0;
        this.channelPembayaran = null;
        this.mataUang = null;
    }

    // Setters
    public void setMember(Member member) { this.member = member; }
    public void setInitialMemberPoin(int poin) { this.initialMemberPoin = poin; }
    public void setFinalMemberPoin(int poin) { this.finalMemberPoin = poin; }
    public void setPoinDigunakan(int poin) { this.poinDigunakan = poin; }
    public void setChannelPembayaran(IChannelPembayaran channelPembayaran) { this.channelPembayaran = channelPembayaran; }
    public void setMataUang(IMataUang mataUang) { this.mataUang = mataUang; }

    // Getters
    public String getIdTransaksi() { return idTransaksi; }
    public LinkedList<ItemTransaksi> getKeranjangBelanja() { return keranjangBelanja; }
    public LocalDateTime getWaktuTransaksi() { return waktuTransaksi; }
    public Member getMember() { return member; }
    public int getInitialMemberPoin() { return initialMemberPoin; }
    public int getFinalMemberPoin() { return finalMemberPoin; }
    public int getPoinDigunakan() { return poinDigunakan; }
    public double getTotalBelanjaSebelumPajakDanDiskon() { return totalBelanjaSebelumPajakDanDiskon; }
    public double getTotalPajak() { return totalPajak; }
    public double getTotalDiskon() { return totalDiskon; }
    public double getBiayaAdmin() { return biayaAdmin; }
    public double getTotalBelanjaFinal() { return totalBelanjaFinal; }
    public double getTotalBelanjaFinalDalamMataUang() {
        if (mataUang != null) {
            return mataUang.konversiDariIDR(totalBelanjaFinal - (poinDigunakan * 2.0));
        }
        return totalBelanjaFinal - (poinDigunakan * 2.0);
    }
    public double getBayar() { return bayar; }
    public double getKembalian() { return kembalian; }
    public IChannelPembayaran getChannelPembayaran() { return channelPembayaran; }
    public IMataUang getMataUang() { return mataUang; }

    // Metode untuk mengelola keranjang belanja
    public void tambahItem(Menu menu, int kuantitas) {
        for (ItemTransaksi item : keranjangBelanja) {
            if (item.getMenu().getKode().equals(menu.getKode())) {
                item.setKuantitas(item.getKuantitas() + kuantitas);
                hitungTotalBelanja();
                return;
            }
        }
        keranjangBelanja.add(new ItemTransaksi(menu, kuantitas));
        hitungTotalBelanja();
    }

    public void hapusItem(String kodeMenu) {
        keranjangBelanja.removeIf(item -> item.getMenu().getKode().equals(kodeMenu));
        hitungTotalBelanja();
    }

    private void hitungTotalBelanja() {
       
        double sum = 0.0;
        for (ItemTransaksi item : keranjangBelanja) {
            sum += item.getTotalHargaItem();
        }
        this.totalBelanjaSebelumPajakDanDiskon = sum;
        hitungPajakDiskonDanTotalAkhir();
    }

        public void hitungPajakDiskonDanTotalAkhir() {
        this.totalPajak = 0.0;
        this.totalDiskon = 0.0;
        this.biayaAdmin = 0.0;

        boolean isMemberWithA = (member != null && member.doesKodeMemberContainA());

        for (ItemTransaksi item : keranjangBelanja) {
            Menu menuDipesan = item.getMenu();
            int kuantitas = item.getKuantitas();
            
            if (isMemberWithA) {
            } else {
                this.totalPajak += menuDipesan.hitungPajak() * kuantitas;
            }
        }

        double subtotalAfterPajak = totalBelanjaSebelumPajakDanDiskon + totalPajak;

        if (channelPembayaran != null) {
            this.totalDiskon = channelPembayaran.hitungDiskon(subtotalAfterPajak);
            this.biayaAdmin = channelPembayaran.hitungBiayaAdmin();
        }

        this.totalBelanjaFinal = subtotalAfterPajak - totalDiskon + biayaAdmin;
    }

    public void prosesPembayaran(double jumlahBayar) {
        this.bayar = jumlahBayar;
        double totalHarusDibayarIDR = totalBelanjaFinal - (poinDigunakan * 2.0);
        
        this.kembalian = jumlahBayar - (mataUang != null
            ? mataUang.konversiDariIDR(totalHarusDibayarIDR)
            : totalHarusDibayarIDR);
    }
    
    public int getUniqueMinumanCount() {
        int uniqueCount = 0;
        List<String> uniqueCodes = new ArrayList<>();
        for (ItemTransaksi item : keranjangBelanja) {
            if (item.getMenu() instanceof Minuman) {
                boolean found = false;
                for(String code : uniqueCodes) {
                    if (code.equals(item.getMenu().getKode())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    uniqueCodes.add(item.getMenu().getKode());
                    uniqueCount++;
                }
            }
        }
        return uniqueCount;
    }

    public int getUniqueMakananCount() {
        int uniqueCount = 0;
        List<String> uniqueCodes = new ArrayList<>();
        for (ItemTransaksi item : keranjangBelanja) {
            if (item.getMenu() instanceof Makanan) {
                boolean found = false;
                for(String code : uniqueCodes) {
                    if (code.equals(item.getMenu().getKode())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    uniqueCodes.add(item.getMenu().getKode());
                    uniqueCount++;
                }
            }
        }
        return uniqueCount;
    }

    public boolean keranjangBelanjaContains(String kodeMenu) {
        for (ItemTransaksi item : keranjangBelanja) {
            if (item.getMenu().getKode().equals(kodeMenu)) {
                return true;
            }
        }
        return false;
    }

    public void cetakStruk() {
        S.clear();
        int currentLine = 1;
        int startCol = 15; 
        int itemDetailNameCol = startCol;
        int itemDetailPriceCol = startCol + 40; 
        int itemDetailQtyCol = startCol + 55;   
        int itemDetailTotalCol = startCol + 65; 
        
        S.move(startCol, currentLine++);
        System.out.println("------------------------------------------------------------------------------------");
        S.move(startCol + 10, currentLine++);
        System.out.println("KohiSop Coffee Shop");
        S.move(startCol + 5, currentLine++);
        System.out.println("Jl. Contoh No. 123, Malang");
        S.move(startCol + 10, currentLine++);
        System.out.println("(0341) 123-456");
        S.move(startCol, currentLine++);
        System.out.println("------------------------------------------------------------------------------------");
        currentLine++; 

        S.move(startCol, currentLine++);
        System.out.println("ID Transaksi: " + idTransaksi);
        S.move(startCol, currentLine++);
        System.out.println("Waktu: " + waktuTransaksi.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        currentLine++; 

        if (member != null) {
            S.move(startCol, currentLine++);
            System.out.println("Member: " + member.getNamaMember() + " (Kode: " + member.getKodeMember() + ")");
            S.move(startCol, currentLine++);
            System.out.println("Poin Sebelum: " + initialMemberPoin + " poin");
            S.move(startCol, currentLine++);
            System.out.println("Poin Setelah: " + finalMemberPoin + " poin");
        } else {
            S.move(startCol, currentLine++);
            System.out.println("Pelanggan: Non-Member");
        }
        currentLine++; 
        S.move(startCol, currentLine++);
        System.out.println("------------------------------------------------------------------------------------");

        S.move(startCol, currentLine++);
        System.out.println("----------------- Daftar Pesanan -----------------");
        S.move(itemDetailNameCol, currentLine);
        System.out.print(SortingHelper.padRight("Kode - Menu", 35)); 
        S.move(itemDetailPriceCol, currentLine);
        System.out.print(SortingHelper.padRight("Harga", 10)); 
        S.move(itemDetailQtyCol, currentLine);
        System.out.print(SortingHelper.padRight("Qty", 5)); 
        S.move(itemDetailTotalCol, currentLine++);
        System.out.println(SortingHelper.padRight("Total", 10)); 
        S.move(startCol, currentLine++);
        System.out.println("------------------------------------------------------------------------------------");

        List<ItemTransaksi> makananDipesan = new ArrayList<>();
        List<ItemTransaksi> minumanDipesan = new ArrayList<>();
        for (ItemTransaksi item : keranjangBelanja) {
            if (item.getMenu() instanceof Makanan) {
                makananDipesan.add(item);
            } else if (item.getMenu() instanceof Minuman) {
                minumanDipesan.add(item);
            }
        }

        SortingHelper.sortItemTransaksiByHarga(makananDipesan);
        SortingHelper.sortItemTransaksiByHarga(minumanDipesan); 

        boolean hasPrintedSectionHeader = false;

        if (!makananDipesan.isEmpty()) {
            S.move(startCol, currentLine++);
            System.out.println("--- Makanan ---");
            hasPrintedSectionHeader = true;
            for (ItemTransaksi item : makananDipesan) {
                String kodeNama = item.getMenu().getKode() + " - " + item.getMenu().getNama();
                String hargaItem = String.format("%,.2f", item.getMenu().getHarga());
                String kuantitas = String.valueOf(item.getKuantitas());
                String totalPerItem = String.format("%,.2f", item.getTotalHargaItem());

                S.move(itemDetailNameCol, currentLine);
                System.out.print(SortingHelper.padRight(kodeNama, itemDetailPriceCol - itemDetailNameCol -1));
                S.move(itemDetailPriceCol, currentLine);
                System.out.print("Rp" + SortingHelper.padLeft(hargaItem, 10)); 
                S.move(itemDetailQtyCol, currentLine);
                System.out.print(SortingHelper.padLeft(kuantitas, 5));
                S.move(itemDetailTotalCol, currentLine++);
                System.out.println("Rp" + SortingHelper.padLeft(totalPerItem, 10));
            }
        }

        if (!minumanDipesan.isEmpty()) {
            if (hasPrintedSectionHeader) {
                currentLine++;
            }
            S.move(startCol, currentLine++);
            System.out.println("--- Minuman ---");
            for (ItemTransaksi item : minumanDipesan) {
                String kodeNama = item.getMenu().getKode() + " - " + item.getMenu().getNama();
                String hargaItem = String.format("%,.2f", item.getMenu().getHarga());
                String kuantitas = String.valueOf(item.getKuantitas());
                String totalPerItem = String.format("%,.2f", item.getTotalHargaItem());

                S.move(itemDetailNameCol, currentLine);
                System.out.print(SortingHelper.padRight(kodeNama, itemDetailPriceCol - itemDetailNameCol -1));
                S.move(itemDetailPriceCol, currentLine);
                System.out.print("Rp" + SortingHelper.padLeft(hargaItem, 10));
                S.move(itemDetailQtyCol, currentLine);
                System.out.print(SortingHelper.padLeft(kuantitas, 5));
                S.move(itemDetailTotalCol, currentLine++);
                System.out.println("Rp" + SortingHelper.padLeft(totalPerItem, 10));
            }
        }
        S.move(startCol, currentLine++);
        System.out.println("------------------------------------------------------------------------------------");

        // Ringkasan Pembayaran
        S.move(startCol, currentLine++);
        System.out.println("Subtotal:               Rp" + String.format("%,.2f", totalBelanjaSebelumPajakDanDiskon));
        S.move(startCol, currentLine++);
        System.out.println("Pajak (10%):            Rp" + String.format("%,.2f", totalPajak));
        S.move(startCol, currentLine++);
        System.out.println("Diskon Channel:         Rp" + String.format("%,.2f", totalDiskon));
        if (channelPembayaran != null && channelPembayaran.getNamaChannel().equalsIgnoreCase("eMoney")) {
            S.move(startCol, currentLine++);
            System.out.println("Biaya Admin eMoney:     Rp" + String.format("%,.2f", biayaAdmin));
        }
        if (member != null && poinDigunakan > 0) {
            S.move(startCol, currentLine++);
            System.out.println("Poin Digunakan (" + poinDigunakan + "):      -Rp" + String.format("%,.2f", poinDigunakan * 2.0));
        }
        S.move(startCol, currentLine++);
        System.out.println("------------------------------------------------------------------------------------");
        S.move(startCol, currentLine++);
        System.out.println("TOTAL:                  " + (mataUang != null ? mataUang.getKode() : "IDR") + " " + String.format("%,.2f", getTotalBelanjaFinalDalamMataUang()));
        S.move(startCol, currentLine++);
        System.out.println("Dibayar:                " + (mataUang != null ? mataUang.getKode() : "IDR") + " " + String.format("%,.2f", bayar));
        S.move(startCol, currentLine++);
        System.out.println("Kembalian:              " + (mataUang != null ? mataUang.getKode() : "IDR") + " " + String.format("%,.2f", kembalian));
        S.move(startCol, currentLine++);
        System.out.println("------------------------------------------------------------------------------------");

        
        currentLine++;
        S.move(startCol, currentLine++);
        System.out.println("   Terima kasih telah berbelanja!  ");
        S.move(startCol, currentLine++);
        System.out.println("      Sampai jumpa kembali!      ");
        S.move(startCol, currentLine++);
        System.out.println("------------------------------------------------------------------------------------");
    }

    public void tampilkanKeranjang() {
        S.clear();
        int currentLine = 1;
        int mainCol = 1;
        int kodeMenuCol = 1;
        int hargaPorsiCol = 30; 
        int kuantitasCol = 45; 
        int subtotalCol = 55;  

        S.move(mainCol, currentLine++);
        System.out.println("--- Daftar Pesanan Anda ---");
        
        
        S.move(kodeMenuCol, currentLine);
        System.out.print(SortingHelper.padRight("Kode - Menu", 28)); 
        S.move(hargaPorsiCol, currentLine);
        System.out.print(SortingHelper.padRight("Harga/porsi", 14)); 
        S.move(kuantitasCol, currentLine);
        System.out.print(SortingHelper.padRight("Kuantitas", 14)); 
        S.move(subtotalCol, currentLine++);
        System.out.println(SortingHelper.padRight("Subtotal", 15)); 

        S.move(mainCol, currentLine++);
        System.out.println("----------------------------------------------------------------------------");

        
        List<ItemTransaksi> foodItems = new ArrayList<>();
        for (ItemTransaksi item : keranjangBelanja) {
            if (item.getMenu() instanceof Makanan) {
                foodItems.add(item);
            }
        }
        SortingHelper.sortItemTransaksiByHarga(foodItems); 

        
        List<ItemTransaksi> drinkItems = new ArrayList<>();
        for (ItemTransaksi item : keranjangBelanja) {
            if (item.getMenu() instanceof Minuman) {
                drinkItems.add(item);
            }
        }
        SortingHelper.sortItemTransaksiByHarga(drinkItems); 

        if (!foodItems.isEmpty()) {
            S.move(mainCol, currentLine++);
            System.out.println("--- Makanan ---");
            for (ItemTransaksi item : foodItems) {
                String kodeNama = item.getMenu().getKode() + " - " + item.getMenu().getNama();
                String hargaStr = String.format("%,.2f", item.getMenu().getHarga());
                String kuantitasStr = String.valueOf(item.getKuantitas());
                String subtotalStr = String.format("%,.2f", item.getTotalHargaItem());

                S.move(kodeMenuCol, currentLine);
                System.out.print(SortingHelper.padRight(kodeNama, hargaPorsiCol - kodeMenuCol - 1));
                S.move(hargaPorsiCol, currentLine);
                System.out.print(SortingHelper.padLeft(hargaStr, kuantitasCol - hargaPorsiCol - 1));
                S.move(kuantitasCol, currentLine);
                System.out.print(SortingHelper.padLeft(kuantitasStr, subtotalCol - kuantitasCol - 1));
                S.move(subtotalCol, currentLine++);
                System.out.println(SortingHelper.padLeft(subtotalStr, 15));
            }
        }

        if (!drinkItems.isEmpty()) {
            if (!foodItems.isEmpty()) {
                currentLine++; 
            }
            S.move(mainCol, currentLine++);
            System.out.println("--- Minuman ---");
            for (ItemTransaksi item : drinkItems) {
                String kodeNama = item.getMenu().getKode() + " - " + item.getMenu().getNama();
                String hargaStr = String.format("%,.2f", item.getMenu().getHarga());
                String kuantitasStr = String.valueOf(item.getKuantitas());
                String subtotalStr = String.format("%,.2f", item.getTotalHargaItem());

                S.move(kodeMenuCol, currentLine);
                System.out.print(SortingHelper.padRight(kodeNama, hargaPorsiCol - kodeMenuCol - 1));
                S.move(hargaPorsiCol, currentLine);
                System.out.print(SortingHelper.padLeft(hargaStr, kuantitasCol - hargaPorsiCol - 1));
                S.move(kuantitasCol, currentLine);
                System.out.print(SortingHelper.padLeft(kuantitasStr, subtotalCol - kuantitasCol - 1));
                S.move(subtotalCol, currentLine++);
                System.out.println(SortingHelper.padLeft(subtotalStr, 15));
            }
        }
        
        S.move(mainCol, currentLine++);
        System.out.println("---------------------------------------------------------------------------------"); 
        S.move(mainCol, currentLine++);
        System.out.println("Total Belanja Awal: Rp" + String.format("%,.2f", totalBelanjaSebelumPajakDanDiskon));
        S.move(mainCol, currentLine++);
        System.out.println("---------------------------------------------------------------------------------"); 
    }

    public int getPaymentChannelChoice() {
        if (channelPembayaran instanceof Tunai) return 1;
        if (channelPembayaran instanceof QRIS) return 2;
        if (channelPembayaran instanceof EMoney) return 3;
        return 0; 
    }

    public String getCurrencyCode() {
        if (mataUang != null) {
            return mataUang.getKode();
        }
        return "IDR";
    }
}