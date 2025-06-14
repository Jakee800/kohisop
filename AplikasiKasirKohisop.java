import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.LinkedList;
import java.time.LocalDateTime;
import java.util.Comparator; 
import java.util.PriorityQueue;

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

public class AplikasiKasirKohisop {
    private List<Menu> daftarMenu;
    private List<Member> daftarMember;
    private Scanner scanner;
    private int customerCount; 
    private List<Transaksi> completedTransactions; 

    private PriorityQueue<ItemTransaksi> foodQueue;
    private LinkedList<ItemTransaksi> drinkStack;

    public AplikasiKasirKohisop() {
        daftarMenu = new ArrayList<>();
        daftarMember = new ArrayList<>();
        scanner = new Scanner(System.in);
        customerCount = 0; 
        completedTransactions = new ArrayList<>(); 

        foodQueue = new PriorityQueue<>(new Comparator<ItemTransaksi>() {
            @Override
            public int compare(ItemTransaksi item1, ItemTransaksi item2) {
             
                return Double.compare(item2.getMenu().getHarga(), item1.getMenu().getHarga());
            }
        });
        drinkStack = new LinkedList<>(); 

        inisialisasiMenuAwal();
        inisialisasiMemberAwal();
    }

    private void inisialisasiMenuAwal() {
        daftarMenu.add(new Minuman("A1", "Caffe Latte", 46, 50));
        daftarMenu.add(new Minuman("A2", "Cappuccino", 46, 40));
        daftarMenu.add(new Minuman("E1", "Caffe Americano", 37, 60));
        daftarMenu.add(new Minuman("E2", "Caffe Mocha", 55, 35));
        daftarMenu.add(new Minuman("E3", "Caramel Macchiato", 59, 30));
        daftarMenu.add(new Minuman("E4", "Asian Dolce Latte", 55, 25));
        daftarMenu.add(new Minuman("E5", "Double Shots Iced Shaken Espresso", 50, 45));
        daftarMenu.add(new Minuman("B1", "Freshly Brewed Coffee", 23, 55));
        daftarMenu.add(new Minuman("B2", "Vanilla Sweet Cream Cold Brew", 50, 30));
        daftarMenu.add(new Minuman("B3", "Cold Brew", 44, 40));

        daftarMenu.add(new Makanan("M1", "Petemania Pizza", 112, 20));
        daftarMenu.add(new Makanan("M2", "Mie Rebus Super Mario", 35, 60));
        daftarMenu.add(new Makanan("M3", "Ayam Bakar Goreng Rebus Spesial", 72, 25));
        daftarMenu.add(new Makanan("M4", "Soto Kambing Iga Guling", 124, 15));
        daftarMenu.add(new Makanan("S1", "Singkong Bakar A La Carte", 37, 50));
        daftarMenu.add(new Makanan("S2", "Ubi Cilembu Bakar Arang", 58, 40));
        daftarMenu.add(new Makanan("S3", "Tempe Mendoan", 18, 80));
        daftarMenu.add(new Makanan("S4", "Tahu Bakso Extra Telur", 28, 70));
    }

    private void inisialisasiMemberAwal() {
        Member member1 = new Member("A23FB9", "Budi", 50);
        Member member2 = new Member("CDE123", "Citra", 20);
        daftarMember.add(member1);
        daftarMember.add(member2);
    }

    public void tampilkanMenu() {
        S.clear();
        S.move(1, 1);
        System.out.println("--- Aplikasi Kasir KohiSop ---");
        S.move(1, 2);
        System.out.println("1. Manajemen Menu");
        S.move(1, 3);
        System.out.println("2. Mulai Transaksi Baru");
        S.move(1, 4);
        System.out.println("3. Manajemen Member");
        S.move(1, 5);
        System.out.println("0. Keluar");
        S.move(1, 6);
        System.out.print("Pilih opsi: ");
    }

    public void tampilkanMenuManajemenProduk() {
        S.clear();
        S.move(1, 1);
        System.out.println("--- Manajemen Menu ---");
        S.move(1, 2);
        System.out.println("1. Tambah Menu Baru");
        S.move(1, 3);
        System.out.println("2. Lihat Daftar Menu");
        S.move(1, 4);
        System.out.println("3. Update Menu (Harga/Stok)");
        S.move(1, 5);
        System.out.println("4. Hapus Menu");
        S.move(1, 6);
        System.out.println("0. Kembali ke Menu Utama");
        S.move(1, 7);
        System.out.print("Pilih opsi: ");
    }

    public void tampilkanMenuManajemenMember() {
        S.clear();
        S.move(1, 1);
        System.out.println("--- Manajemen Member ---");
        S.move(1, 2);
        System.out.println("1. Lihat Daftar Member");
        S.move(1, 3);
        System.out.println("2. Cari Member");
        S.move(1, 4);
        System.out.println("0. Kembali ke Menu Utama");
        S.move(1, 5);
        System.out.print("Pilih opsi: ");
    }

    public void run() {
        int pilihan;
        do {
            tampilkanMenu();
            try {
                pilihan = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                S.move(1, 7);
                System.out.println("Input tidak valid. Silakan masukkan angka.");
                S.delay(1000);
                scanner.nextLine();
                pilihan = -1;
                continue;
            }

            switch (pilihan) {
                case 1:
                    menuManajemenProduk();
                    break;
                case 2:
                    mulaiTransaksiBaru();
                    customerCount++;
                    if (customerCount % 3 == 0) {
                        prosesPesananDapur();
                    }
                    break;
                case 3:
                    menuManajemenMember();
                    break;
                case 0:
                    S.move(1, 7);
                    System.out.println("Terima kasih telah menggunakan aplikasi KohiSop!");
                    S.delay(1000);
                    break;
                default:
                    S.move(1, 7);
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                    S.delay(1000);
            }
        } while (pilihan != 0);
    }

    private void menuManajemenProduk() {
        int pilihan;
        do {
            tampilkanMenuManajemenProduk();
            try {
                pilihan = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                S.move(1, 8);
                System.out.println("Input tidak valid. Silakan masukkan angka.");
                S.delay(1000);
                scanner.nextLine();
                pilihan = -1;
                continue;
            }

            switch (pilihan) {
                case 1:
                    tambahMenu();
                    break;
                case 2:
                    lihatDaftarMenu();
                    break;
                case 3:
                    updateMenu();
                    break;
                case 4:
                    hapusMenu();
                    break;
                case 0:
                    S.move(1, 8);
                    System.out.println("Kembali ke menu utama...");
                    S.delay(1000);
                    break;
                default:
                    S.move(1, 8);
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                    S.delay(1000);
            }
        } while (pilihan != 0);
    }

    private void tambahMenu() {
        S.clear();
        S.move(1, 1);
        System.out.print("Masukkan kode menu: ");
        String kode = scanner.nextLine().toUpperCase();
        for (Menu m : daftarMenu) {
            if (m.getKode().equals(kode)) {
                S.move(1, 2);
                System.out.println("Kode menu sudah ada. Gunakan fungsi update jika ingin mengubah.");
                S.delay(1500);
                return;
            }
        }
        S.move(1, 2);
        System.out.print("Masukkan nama menu: ");
        String nama = scanner.nextLine();
        double harga;
        int currentLine = 3;
        while (true) {
            S.move(1, currentLine);
            System.out.print("Masukkan harga menu: ");
            try {
                harga = Double.parseDouble(scanner.nextLine());
                if (harga < 0) {
                    S.move(1, ++currentLine);
                    System.out.println("Harga tidak boleh negatif.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                S.move(1, ++currentLine);
                System.out.println("Input harga tidak valid. Masukkan angka.");
            }
        }
        int stok;
        S.move(1, ++currentLine);
        while (true) {
            System.out.print("Masukkan stok menu: ");
            try {
                stok = Integer.parseInt(scanner.nextLine());
                if (stok < 0) {
                    S.move(1, ++currentLine);
                    System.out.println("Stok tidak boleh negatif.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                S.move(1, ++currentLine);
                System.out.println("Input stok tidak valid. Masukkan angka.");
            }
        }

        S.move(1, ++currentLine);
        System.out.print("Jenis menu (Makanan/Minuman): ");
        String jenis = scanner.nextLine().toLowerCase();
        S.move(1, ++currentLine);
        if (jenis.equals("makanan")) {
            daftarMenu.add(new Makanan(kode, nama, harga, stok));
            System.out.println("Makanan berhasil ditambahkan!");
        } else if (jenis.equals("minuman")) {
            daftarMenu.add(new Minuman(kode, nama, harga, stok));
            System.out.println("Minuman berhasil ditambahkan!");
        } else {
            System.out.println("Jenis menu tidak valid. Menu tidak ditambahkan.");
        }
        S.delay(1500);
    }

    private void lihatDaftarMenu() {
        S.clear();
        if (daftarMenu.isEmpty()) {
            S.move(1, 1);
            System.out.println("Belum ada menu terdaftar.");
            S.delay(1500);
            return;
        }

        int currentLine = 1;
        int kodeCol = 1;
        int namaCol = 12;
        int hargaCol = 50;
        int stokCol = 70;

        S.move(1, currentLine++);
        System.out.println("--- Daftar Menu KohiSop ---");
        S.move(kodeCol, currentLine);
        System.out.print(SortingHelper.padRight("Kode", 10));
        S.move(namaCol, currentLine);
        System.out.print(SortingHelper.padRight("Nama", 35)); 
        S.move(hargaCol, currentLine);
        System.out.print(SortingHelper.padRight("Harga (Rp)", 12)); 
        S.move(stokCol, currentLine++);
        System.out.println(SortingHelper.padRight("Stok", 12)); 
        S.move(1, currentLine++);
        System.out.println("------------------------------------------------------------------------------");

        List<Menu> makananList = new ArrayList<>();
        List<Menu> minumanList = new ArrayList<>();
        for (Menu menu : daftarMenu) {
            if (menu instanceof Makanan) {
                makananList.add(menu);
            } else if (menu instanceof Minuman) {
                minumanList.add(menu);
            }
        }

        SortingHelper.sortMenuByKode(makananList); 
        SortingHelper.sortMenuByKode(minumanList); 

        S.move(1, currentLine++);
        System.out.println("--- Menu Makanan ---");
        for (Menu menu : makananList) {
            S.move(kodeCol, currentLine);
            System.out.print(SortingHelper.padRight(menu.getKode(), 10)); 
            S.move(namaCol, currentLine);
            System.out.print(SortingHelper.padRight(menu.getNama(), 35));
            S.move(hargaCol, currentLine);
            System.out.print("Rp" + String.format("%,.2f", menu.getHarga())); 
            S.move(stokCol, currentLine++);
            System.out.println(String.valueOf(menu.getStok()));
        }
        S.move(1, currentLine++);
        System.out.println("--- Menu Minuman ---");
        for (Menu menu : minumanList) {
            S.move(kodeCol, currentLine);
            System.out.print(SortingHelper.padRight(menu.getKode(), 10)); 
            S.move(namaCol, currentLine);
            System.out.print(SortingHelper.padRight(menu.getNama(), 35)); 
            S.move(hargaCol, currentLine);
            System.out.print("Rp" + String.format("%,.2f", menu.getHarga())); 
            S.move(stokCol, currentLine++);
            System.out.println(String.valueOf(menu.getStok()));
        }
        S.move(1, currentLine++);
        System.out.println("------------------------------------------------------------------------------");
        S.move(1, currentLine++);
        System.out.print("Tekan Enter untuk melanjutkan...");
        scanner.nextLine();
    }

    private void updateMenu() {
        System.out.print("Masukkan kode menu yang ingin diupdate: ");
        String kode = scanner.nextLine().toUpperCase();
        Menu menuToUpdate = getMenuByKode(kode);
        
        if (menuToUpdate == null) {
            System.out.println("Menu tidak ditemukan.");
            return;
        }

        System.out.println("Menu yang ditemukan: " + menuToUpdate);
        System.out.print("Masukkan harga baru (kosongkan jika tidak berubah): ");
        String hargaStr = scanner.nextLine();
        if (!hargaStr.isEmpty()) {
            try {
                double hargaBaru = Double.parseDouble(hargaStr);
                if (hargaBaru >= 0) {
                    menuToUpdate.setHarga(hargaBaru);
                } else {
                    System.out.println("Harga tidak boleh negatif. Harga tidak diubah.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input harga tidak valid. Harga tidak diubah.");
            }
        }

        System.out.print("Masukkan stok baru (kosongkan jika tidak berubah): ");
        String stokStr = scanner.nextLine();
        if (!stokStr.isEmpty()) {
            try {
                int stokBaru = Integer.parseInt(stokStr);
                if (stokBaru >= 0) {
                    menuToUpdate.setStok(stokBaru);
                } else {
                    System.out.println("Stok tidak boleh negatif. Stok tidak diubah.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input stok tidak valid. Stok tidak diubah.");
            }
        }
        System.out.println("Menu berhasil diupdate!");
    }

    private void hapusMenu() {
        System.out.print("Masukkan kode menu yang ingin dihapus: ");
        String kode = scanner.nextLine().toUpperCase();
        
        boolean removed = daftarMenu.removeIf(m -> m.getKode().equals(kode));
        if (removed) {
            System.out.println("Menu berhasil dihapus!");
        } else {
            System.out.println("Menu tidak ditemukan.");
        }
    }


    private Menu getMenuByKode(String kode) {
        for (Menu m : daftarMenu) {
            if (m.getKode().equals(kode)) {
                return m;
            }
        }
        return null;
    }

    private Member getMemberByNama(String nama) {
        for (Member m : daftarMember) {
            if (m.getNamaMember().equalsIgnoreCase(nama)) {
                return m;
            }
        }
        return null;
    }

    private void mulaiTransaksiBaru() {
        Transaksi transaksi = new Transaksi();
        String kodeMenu;

        lihatDaftarMenu();

        S.clear();
        int currentLine = 1;
        S.move(1, currentLine++);
        System.out.println("--- Memulai Pemesanan Item ---");
        S.move(1, currentLine++);
        System.out.println("Masukkan 'CC' untuk membatalkan pesanan.");
        S.move(1, currentLine++);
        System.out.println("Masukkan 'selesai' untuk mengakhiri pemilihan item.");
        S.move(1, currentLine++);
        System.out.println("Masukkan 'S' atau '0' untuk membatalkan item saat input kuantitas.");

        while (true) {
            S.move(1, currentLine++);
            System.out.print("Masukkan kode menu (atau 'selesai' / 'CC'): ");
            kodeMenu = scanner.nextLine().toUpperCase();

            if (kodeMenu.equalsIgnoreCase("CC")) {
                S.move(1, currentLine++);
                System.out.println("Pesanan dibatalkan.");
                S.delay(1500);
                for (ItemTransaksi item : transaksi.getKeranjangBelanja()) {
                    Menu menuDibeli = getMenuByKode(item.getMenu().getKode());
                    if (menuDibeli != null) {
                        menuDibeli.setStok(menuDibeli.getStok() + item.getKuantitas());
                    }
                }
                return;
            }

            if (kodeMenu.equalsIgnoreCase("selesai")) {
                break;
            }

            Menu menuYangDipilih = getMenuByKode(kodeMenu);
            if (menuYangDipilih == null) {
                S.move(1, currentLine++);
                System.out.println("Kode tidak valid. Silakan masukkan kode lainnya yang benar.");
                S.delay(1500);
                continue;
            }

            if (menuYangDipilih.getStok() <= 0) {
                S.move(1, currentLine++);
                System.out.println("Stok " + menuYangDipilih.getNama() + " habis!");
                S.delay(1500);
                continue;
            }

            boolean isMinuman = (menuYangDipilih instanceof Minuman);
            boolean isMakanan = (menuYangDipilih instanceof Makanan);

            if (isMinuman && transaksi.getUniqueMinumanCount() >= 5 && !transaksi.keranjangBelanjaContains(kodeMenu)) {
                S.move(1, currentLine++);
                System.out.println("Maksimal 5 jenis minuman berbeda dalam satu pesanan.");
                S.delay(1500);
                continue;
            }
            if (isMakanan && transaksi.getUniqueMakananCount() >= 5 && !transaksi.keranjangBelanjaContains(kodeMenu)) {
                System.out.println("Maksimal 5 jenis makanan berbeda dalam satu pesanan.");
                S.delay(1500);
                continue;
            }

            S.move(1, currentLine++);
            System.out.print("Masukkan kuantitas (default 1, 'S' atau '0' untuk batal): ");
            String kuantitasStr = scanner.nextLine();
            int kuantitas;

            if (kuantitasStr.equalsIgnoreCase("S") || kuantitasStr.equals("0")) {
                S.move(1, currentLine++);
                System.out.println("Item " + menuYangDipilih.getNama() + " dibatalkan.");
                S.delay(1000);
                continue;
            } else if (kuantitasStr.isEmpty()) {
                kuantitas = 1;
            } else {
                try {
                    kuantitas = Integer.parseInt(kuantitasStr);
                } catch (NumberFormatException e) {
                    S.move(1, currentLine++);
                    System.out.println("Input kuantitas tidak valid. Masukkan angka.");
                    S.delay(1000);
                    continue;
                }
            }

            if (kuantitas <= 0) {
                S.move(1, currentLine++);
                System.out.println("Kuantitas harus lebih dari 0.");
                S.delay(1000);
                continue;
            }

            int maxKuantitasPerJenis = isMinuman ? 3 : 2;

            int currentKuantitasInCart = 0;
            for (ItemTransaksi item : transaksi.getKeranjangBelanja()) {
                if (item.getMenu().getKode().equals(menuYangDipilih.getKode())) {
                    currentKuantitasInCart = item.getKuantitas();
                    break;
                }
            }

            if (currentKuantitasInCart + kuantitas > maxKuantitasPerJenis) {
                S.move(1, currentLine++);
                System.out.println("Kuantitas maksimal untuk " + (isMinuman ? "minuman" : "makanan") + " ini adalah " + maxKuantitasPerJenis + " porsi.");
                S.delay(1500);
                continue;
            }
            
            if (kuantitas > menuYangDipilih.getStok()) {
                S.move(1, currentLine++);
                System.out.println("Stok " + menuYangDipilih.getNama() + " tidak mencukupi. Stok tersedia: " + menuYangDipilih.getStok());
                S.delay(1500);
                continue;
            }

            transaksi.tambahItem(menuYangDipilih, kuantitas);
            menuYangDipilih.setStok(menuYangDipilih.getStok() - kuantitas);

            transaksi.tampilkanKeranjang();
            S.move(1, currentLine++);
            System.out.println("Item ditambahkan. Total saat ini: Rp" + String.format("%,.2f", transaksi.getTotalBelanjaSebelumPajakDanDiskon()));
        }

        if (transaksi.getKeranjangBelanja().isEmpty()) {
            S.clear();
            S.move(1, 1);
            System.out.println("Transaksi dibatalkan karena tidak ada item.");
            S.delay(1500);
            return;
        }

        String namaPelanggan;
        Member currentMember = null;
        int initialPoin = 0;

        S.clear();
        int currentLineForInput = 1;
        S.move(1, currentLineForInput++);
        System.out.print("Apakah pelanggan sudah menjadi member? (ya/tidak): ");
        String isMember = scanner.nextLine().toLowerCase();

        if (isMember.equals("ya")) {
            S.move(1, currentLineForInput++);
            System.out.print("Masukkan nama member: ");
            namaPelanggan = scanner.nextLine().toLowerCase();
            currentMember = getMemberByNama(namaPelanggan);
            if (currentMember == null) {
                S.move(1, currentLineForInput++);
                System.out.println("Member tidak ditemukan. Pelanggan akan didaftarkan sebagai member baru setelah transaksi jika memenuhi syarat.");
                namaPelanggan = namaPelanggan.substring(0, 1).toUpperCase() + namaPelanggan.substring(1);
            } else {
                initialPoin = currentMember.getJumlahPoin();
                S.move(1, currentLineForInput++);
                System.out.println("Selamat datang kembali, " + currentMember.getNamaMember() + "!");
                S.move(1, currentLineForInput++);
                System.out.println("Poin Anda saat ini: " + initialPoin);
            }
        } else {
            S.move(1, currentLineForInput++);
            System.out.print("Apakah Anda ingin menjadi member? (ya/tidak): ");
            String inginMenjadiMember = scanner.nextLine().toLowerCase();

            if (inginMenjadiMember.equals("ya")) {
                S.move(1, currentLineForInput++);
                System.out.print("Masukkan nama Anda (untuk daftar member baru): ");
                namaPelanggan = scanner.nextLine();
                S.move(1, currentLineForInput++);
                System.out.println("Anda akan didaftarkan sebagai member baru setelah transaksi jika total belanja Anda mencapai Rp10 atau lebih.");
            } else {
                S.move(1, currentLineForInput++);
                System.out.print("Masukkan nama pelanggan (non-member): ");
                namaPelanggan = scanner.nextLine();
            }
        }

        transaksi.setInitialMemberPoin(initialPoin);
        transaksi.setMember(currentMember);

        transaksi.hitungPajakDiskonDanTotalAkhir();

        completedTransactions.add(transaksi);


        S.clear();
        int currentLineForDetails = 1;
        S.move(1, currentLineForDetails++);
        System.out.println("--- Detail Pesanan KohiSop ---");
        S.move(1, currentLineForDetails++);
        System.out.println("ID Transaksi: " + transaksi.getIdTransaksi());
        S.move(1, currentLineForDetails++);
        System.out.println("Waktu: " + transaksi.getWaktuTransaksi().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        S.move(1, currentLineForDetails++);
        System.out.println("-----------------------------------------------------------------------");

        List<ItemTransaksi> makananDipesan = new ArrayList<>();
        List<ItemTransaksi> minumanDipesan = new ArrayList<>();
        for (ItemTransaksi item : transaksi.getKeranjangBelanja()) {
            if (item.getMenu() instanceof Makanan) {
                makananDipesan.add(item);
            } else if (item.getMenu() instanceof Minuman) {
                minumanDipesan.add(item);
            }
        }

        SortingHelper.sortItemTransaksiByHarga(makananDipesan);
        SortingHelper.sortItemTransaksiByHarga(minumanDipesan);

        S.move(1, currentLineForDetails++);
        System.out.println("------------------------------- Makanan -------------------------------");
        for (ItemTransaksi item : makananDipesan) {
            S.move(1, currentLineForDetails);
            System.out.print(item.getMenu().getKode() + "   " + item.getMenu().getNama());
            S.move(45, currentLineForDetails);
            System.out.print("Rp" + String.format("%,.2f", item.getMenu().getHarga()));
            S.move(60, currentLineForDetails);
            System.out.print("x " + item.getKuantitas());
            S.move(70, currentLineForDetails++);
            System.out.println("= Rp" + String.format("%,.2f", item.getTotalHargaItem()));
        }
        S.move(1, currentLineForDetails++);
        System.out.println("------------------------------- Minuman -------------------------------");
        for (ItemTransaksi item : minumanDipesan) {
            S.move(1, currentLineForDetails);
            System.out.print(item.getMenu().getKode() + "   " + item.getMenu().getNama());
            S.move(45, currentLineForDetails);
            System.out.print("Rp" + String.format("%,.2f", item.getMenu().getHarga()));
            S.move(60, currentLineForDetails);
            System.out.print("x " + item.getKuantitas());
            S.move(70, currentLineForDetails++);
            System.out.println("= Rp" + String.format("%,.2f", item.getTotalHargaItem()));
        }

        S.move(1, currentLineForDetails++);
        System.out.println("-----------------------------------------------------------------------");
        S.move(1, currentLineForDetails++);
        System.out.println("Total Belanja Awal:       Rp" + String.format("%,.2f", transaksi.getTotalBelanjaSebelumPajakDanDiskon()));
        S.move(1, currentLineForDetails++);
        System.out.println("Total Pajak:              Rp" + String.format("%,.2f", transaksi.getTotalPajak()));
        S.move(1, currentLineForDetails++);
        System.out.println("Diskon Channel Pembayaran: Rp" + String.format("%,.2f", transaksi.getTotalDiskon()));
        if (transaksi.getPaymentChannelChoice() == 3) {
            S.move(1, currentLineForDetails++);
            System.out.println("Biaya Admin eMoney:       Rp" + String.format("%,.2f", transaksi.getBiayaAdmin()));
        }
        S.move(1, currentLineForDetails++);
        System.out.println("Total Tagihan Sebelum Poin: Rp" + String.format("%,.2f", transaksi.getTotalBelanjaFinal()));
        S.move(1, currentLineForDetails++);
        System.out.println("-----------------------------------------------------------------------\n");

        S.move(1, currentLineForDetails++);
        System.out.println("--- Pilihan Mata Uang Pembayaran ---");
        S.move(1, currentLineForDetails++);
        System.out.println("1. IDR");
        S.move(1, currentLineForDetails++);
        System.out.println("2. USD (1 USD = 15 IDR)");
        S.move(1, currentLineForDetails++);
        System.out.println("3. JPY (10 JPY = 1 IDR)");
        S.move(1, currentLineForDetails++);
        System.out.println("4. MYR (1 MYR = 4 IDR)");
        S.move(1, currentLineForDetails++);
        System.out.println("5. EUR (1 EUR = 14 IDR)");
        S.move(1, currentLineForDetails++);
        System.out.print("Pilih mata uang: ");
        int currencyChoice;
        try {
            currencyChoice = scanner.nextInt();
            scanner.nextLine();
        } catch (java.util.InputMismatchException e) {
            S.move(1, currentLineForDetails++);
            System.out.println("Pilihan mata uang tidak valid. Menggunakan IDR secara default.");
            S.delay(1000);
            scanner.nextLine();
            currencyChoice = 1;
        }
        IMataUang mataUang;
        switch (currencyChoice) {
            case 2: mataUang = new USD(); break;
            case 3: mataUang = new JPY(); break;
            case 4: mataUang = new MYR(); break;
            case 5: mataUang = new EUR(); break;
            default: mataUang = new IDR(); break;
        }
        transaksi.setMataUang(mataUang);

        S.move(1, currentLineForDetails++);
        System.out.println("--- Pilihan Channel Pembayaran ---");
        S.move(1, currentLineForDetails++);
        System.out.println("1. Tunai (Tidak ada diskon)");
        S.move(1, currentLineForDetails++);
        System.out.println("2. QRIS (Diskon 5%)");
        S.move(1, currentLineForDetails++);
        System.out.println("3. eMoney (Diskon 7%, Biaya Admin 20 IDR)");
        S.move(1, currentLineForDetails++);
        System.out.print("Pilih channel pembayaran: ");
        int channelChoice;
        try {
            channelChoice = scanner.nextInt();
            scanner.nextLine();
        } catch (java.util.InputMismatchException e) {
            S.move(1, currentLineForDetails++);
            System.out.println("Pilihan channel pembayaran tidak valid. Menggunakan Tunai secara default.");
            S.delay(1000);
            scanner.nextLine();
            channelChoice = 1;
        }
        IChannelPembayaran channelPembayaran;
        switch (channelChoice) {
            case 2: channelPembayaran = new QRIS(); break;
            case 3: channelPembayaran = new EMoney(); break;
            default: channelPembayaran = new Tunai(); break;
        }
        transaksi.setChannelPembayaran(channelPembayaran);

        transaksi.hitungPajakDiskonDanTotalAkhir();

        int poinDigunakanSaatIni = 0;
        double totalTagihanDenganPajakDiskon = transaksi.getTotalBelanjaFinal();
        if (transaksi.getMember() != null && mataUang.getKode().equals("IDR")) {
            S.move(1, currentLineForDetails++);
            System.out.print("Gunakan poin untuk pembayaran? (ya/tidak): ");
            String usePoin = scanner.nextLine().toLowerCase();
            if (usePoin.equals("ya")) {
                int maxPoinBisaDigunakan = (int) Math.floor(totalTagihanDenganPajakDiskon / 2.0);
                poinDigunakanSaatIni = Math.min(transaksi.getMember().getJumlahPoin(), maxPoinBisaDigunakan);
                transaksi.setPoinDigunakan(poinDigunakanSaatIni);
            }
        }

        double totalYangHarusDibayarDalamIDR = totalTagihanDenganPajakDiskon - (poinDigunakanSaatIni * 2.0);
        double totalYangHarusDibayarDalamMataUangPilihan = mataUang.konversiDariIDR(totalYangHarusDibayarDalamIDR);

        S.move(1, currentLineForDetails++);
        System.out.println("Total yang harus dibayar (" + mataUang.getKode() + "): " + String.format("%,.2f", totalYangHarusDibayarDalamMataUangPilihan));
        S.move(1, currentLineForDetails++);
        System.out.print("Jumlah uang dibayar: ");
        double bayar;
        try {
            bayar = scanner.nextDouble();
            scanner.nextLine();
        } catch (java.util.InputMismatchException e) {
            S.move(1, currentLineForDetails++);
            System.out.println("Input pembayaran tidak valid. Transaksi dibatalkan.");
            S.delay(1500);
            scanner.nextLine();
            for (ItemTransaksi item : transaksi.getKeranjangBelanja()) {
                Menu menuDibeli = getMenuByKode(item.getMenu().getKode());
                if (menuDibeli != null) {
                    menuDibeli.setStok(menuDibeli.getStok() + item.getKuantitas());
                }
            }
            return;
        }

        double jumlahBayarDalamIDR = bayar * (mataUang instanceof IDR ? 1.0 :
            (mataUang instanceof USD ? 15.0 :
            (mataUang instanceof JPY ? 0.1 :
            (mataUang instanceof MYR ? 4.0 :
            (mataUang instanceof EUR ? 14.0 : 1.0)))));


        if ((channelChoice == 2 || channelChoice == 3) && jumlahBayarDalamIDR < totalYangHarusDibayarDalamIDR) {
            S.move(1, currentLineForDetails++);
            System.out.println("Saldo tidak mencukupi untuk pembayaran ini. Transaksi dibatalkan.");
            S.delay(1500);
            for (ItemTransaksi item : transaksi.getKeranjangBelanja()) {
                Menu menuDibeli = getMenuByKode(item.getMenu().getKode());
                if (menuDibeli != null) {
                    menuDibeli.setStok(menuDibeli.getStok() + item.getKuantitas());
                }
            }
            return;
        }

        transaksi.prosesPembayaran(bayar);

        if (transaksi.getMember() == null && totalYangHarusDibayarDalamIDR >= 10) { 
            Member newMember = new Member(namaPelanggan);
            daftarMember.add(newMember);
            S.move(1, currentLineForDetails++);
            System.out.println("Pelanggan " + newMember.getNamaMember() + " telah terdaftar sebagai member baru dengan kode " + newMember.getKodeMember() + ".");
            transaksi.setMember(newMember);
            transaksi.setInitialMemberPoin(0);
        }

        if (transaksi.getMember() != null) {
            transaksi.getMember().kurangiPoin(poinDigunakanSaatIni);
            int poinDiperoleh = (int) (totalYangHarusDibayarDalamIDR / 10);
            if (transaksi.getMember().doesKodeMemberContainA()) {
                poinDiperoleh *= 2;
            }
            S.move(1, currentLineForDetails++);
            System.out.println("Poin Diperoleh: " + poinDiperoleh);
            transaksi.getMember().tambahPoin(poinDiperoleh);
            transaksi.setFinalMemberPoin(transaksi.getMember().getJumlahPoin());
        }

        transaksi.cetakStruk();
        S.delay(3000);
    }

    private void menuManajemenMember() {
        int pilihan;
        do {
            tampilkanMenuManajemenMember();
            try {
                pilihan = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                S.move(1, 6);
                System.out.println("Input tidak valid. Silakan masukkan angka.");
                S.delay(1000);
                scanner.nextLine();
                pilihan = -1;
                continue;
            }

            switch (pilihan) {
                case 1:
                    lihatDaftarMember();
                    break;
                case 2:
                    cariMember();
                    break;
                case 0:
                    S.move(1, 6);
                    System.out.println("Kembali ke menu utama...");
                    S.delay(1000);
                    break;
                default:
                    S.move(1, 6);
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                    S.delay(1000);
            }
        } while (pilihan != 0);
    }

    private void lihatDaftarMember() {
        S.clear();
        if (daftarMember.isEmpty()) {
            System.out.println("Belum ada member terdaftar.");
            return;
        }
        int currentLine = 1;
        S.move(1, currentLine++);
        System.out.println("--- Daftar Member ---");
        S.move(1, currentLine);
        System.out.print(SortingHelper.padRight("Kode", 10)); 
        S.move(12, currentLine); 
        System.out.print(SortingHelper.padRight("Nama", 20)); 
        S.move(35, currentLine++); 
        System.out.println(SortingHelper.padRight("Poin", 10)); 
        S.move(1, currentLine++);
        System.out.println("----------------------------------------------");
        for (Member member : daftarMember) {
            S.move(1, currentLine);
            System.out.print(SortingHelper.padRight(member.getKodeMember(), 10)); 
            S.move(12, currentLine); 
            System.out.print(SortingHelper.padRight(member.getNamaMember(), 20)); 
            S.move(35, currentLine++); 
            System.out.println(String.valueOf(member.getJumlahPoin()));
        }
        S.move(1, currentLine++);
        System.out.println("----------------------------------------------");
        S.move(1, currentLine++);
        System.out.print("Tekan Enter untuk melanjutkan...");
        scanner.nextLine();
    }

    private void cariMember() {
        S.clear();
        S.move(1, 1);
        System.out.print("Masukkan nama member yang ingin dicari: ");
        String namaInput = scanner.nextLine().toLowerCase();
        Member foundMember = getMemberByNama(namaInput);
        int currentLine = 2;
        if (foundMember != null) {
            S.move(1, currentLine++);
            System.out.println("--- Detail Member ---");
            S.move(1, currentLine++);
            System.out.println("Kode Member: " + foundMember.getKodeMember());
            S.move(1, currentLine++);
            System.out.println("Nama Member: " + foundMember.getNamaMember());
            S.move(1, currentLine++);
            System.out.println("Jumlah Poin: " + foundMember.getJumlahPoin());
        } else {
            S.move(1, currentLine++);
            System.out.println("Member dengan nama '" + namaInput + "' tidak ditemukan.");
        }
        S.delay(1500);
    }

    private void prosesPesananDapur() {
        S.clear();
        int currentLine = 1;
        int startCol = 1;

        S.move(startCol, currentLine++);
        System.out.println("--- Proses Pesanan Tim Dapur ---");
        S.move(startCol, currentLine++);
        System.out.println("Pesanan sedang diproses untuk " + customerCount + " pelanggan terakhir.");
        S.move(startCol, currentLine++);
        System.out.println("--------------------------------------------------");

        for (Transaksi transaksi : completedTransactions) {
            List<ItemTransaksi> foodItemsInTx = new ArrayList<>();
            List<ItemTransaksi> drinkItemsInTx = new ArrayList<>();

            // Pisahkan item makanan dan minuman dari transaksi
            for (ItemTransaksi item : transaksi.getKeranjangBelanja()) {
                if (item.getMenu() instanceof Makanan) {
                    foodItemsInTx.add(item);
                } else if (item.getMenu() instanceof Minuman) {
                    drinkItemsInTx.add(item);
                }
            }
            for (ItemTransaksi item : foodItemsInTx) {
                foodQueue.offer(item);
            }

            for (ItemTransaksi item : drinkItemsInTx) {
                drinkStack.push(item); 
            }
        }
        completedTransactions.clear();


       
        S.move(startCol, currentLine++);
        System.out.println("--- Dapur Makanan (Prioritas Harga Tertinggi) ---");
        if (foodQueue.isEmpty()) {
            S.move(startCol, currentLine++);
            System.out.println("Tidak ada pesanan makanan yang menunggu.");
        } else {
            // Header untuk daftar makanan
            S.move(startCol, currentLine);
            System.out.print(SortingHelper.padRight("Kode - Nama", 30));
            S.move(startCol + 35, currentLine);
            System.out.print(SortingHelper.padRight("Qty", 5));
            S.move(startCol + 45, currentLine++);
            System.out.println(SortingHelper.padRight("Harga Satuan", 15));
            currentLine++; // Spasi
            while (!foodQueue.isEmpty()) {
                ItemTransaksi item = foodQueue.poll(); 
                S.move(startCol, currentLine);
                String itemDesc = item.getMenu().getKode() + " - " + item.getMenu().getNama();
                String hargaUnit = String.format("%,.2f", item.getMenu().getHarga());

                System.out.print(SortingHelper.padRight(itemDesc, 30));
                S.move(startCol + 35, currentLine);
                System.out.print(SortingHelper.padRight(String.valueOf(item.getKuantitas()), 5));
                S.move(startCol + 45, currentLine++);
                System.out.println("Rp" + SortingHelper.padLeft(hargaUnit, 12));
            }
        }
        currentLine++; 

        S.move(startCol, currentLine++);
        System.out.println("--- Dapur Minuman (Terakhir Masuk, Pertama Keluar) ---");
        if (drinkStack.isEmpty()) {
            S.move(startCol, currentLine++);
            System.out.println("Tidak ada pesanan minuman yang menunggu.");
        } else {
          
            S.move(startCol, currentLine);
            System.out.print(SortingHelper.padRight("Kode - Nama", 30));
            S.move(startCol + 35, currentLine);
            System.out.print(SortingHelper.padRight("Qty", 5));
            S.move(startCol + 45, currentLine++);
            System.out.println(SortingHelper.padRight("Harga Satuan", 15));
            currentLine++; // Spasi
            while (!drinkStack.isEmpty()) {
                ItemTransaksi item = drinkStack.pop(); 
                S.move(startCol, currentLine);
                String itemDesc = item.getMenu().getKode() + " - " + item.getMenu().getNama();
                String hargaUnit = String.format("%,.2f", item.getMenu().getHarga());

                System.out.print(SortingHelper.padRight(itemDesc, 30));
                S.move(startCol + 35, currentLine);
                System.out.print(SortingHelper.padRight(String.valueOf(item.getKuantitas()), 5));
                S.move(startCol + 45, currentLine++);
                System.out.println("Rp" + SortingHelper.padLeft(hargaUnit, 12));
            }
        }
        currentLine++; 

        S.move(startCol, currentLine++);
        System.out.println("--------------------------------------------------");
        S.move(startCol, currentLine++);
        System.out.print("Tekan Enter untuk melanjutkan...");
        scanner.nextLine();
    }

    public static void main(String[] args) {
        AplikasiKasirKohisop app = new AplikasiKasirKohisop();
        app.run();
    }
}