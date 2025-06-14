import java.util.Random;

public class Member {
    private String kodeMember;
    private String namaMember;
    private int jumlahPoin;

    
    public Member(String namaMember) {
        this.namaMember = namaMember;
        this.jumlahPoin = 0; 
        this.kodeMember = generateKodeMember();
    }

    
    public Member(String kodeMember, String namaMember, int jumlahPoin) {
        this.kodeMember = kodeMember;
        this.namaMember = namaMember;
        this.jumlahPoin = jumlahPoin;
    }

    
    private String generateKodeMember() {
        String chars = "0123456789ABCDEF"; 
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) { 
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    
    public String getKodeMember() {
        return kodeMember;
    }

    public String getNamaMember() {
        return namaMember;
    }

    public int getJumlahPoin() {
        return jumlahPoin;
    }

    
    public void setNamaMember(String namaMember) { 
        this.namaMember = namaMember;
    }

    
    public void tambahPoin(int poin) {
        this.jumlahPoin += poin;
    }

    
    public void kurangiPoin(int poin) {
        this.jumlahPoin -= poin;
        if (this.jumlahPoin < 0) { 
            this.jumlahPoin = 0;
        }
    }

    public boolean doesKodeMemberContainA() {
        return this.kodeMember.contains("A");
    }

    @Override
    public String toString() {
        return String.format("Kode: %s, Nama: %s, Poin: %d", kodeMember, namaMember, jumlahPoin);
    }
}