import java.util.List;
import Menu.Menu;

public class SortingHelper {

        public static void sortMenuByKode(List<Menu> list) {
        for (int i = 1; i < list.size(); i++) {
            Menu key = list.get(i);
            int j = i - 1;

            // Membandingkan berdasarkan kode menu
            while (j >= 0 && list.get(j).getKode().compareTo(key.getKode()) > 0) {
                list.set(j + 1, list.get(j));
                j = j - 1;
            }
            list.set(j + 1, key);
        }
    }

    public static void sortItemTransaksiByHarga(List<ItemTransaksi> list) {
        for (int i = 1; i < list.size(); i++) {
            ItemTransaksi key = list.get(i);
            int j = i - 1;

            // Membandingkan berdasarkan harga item
            while (j >= 0 && list.get(j).getMenu().getHarga() > key.getMenu().getHarga()) {
                list.set(j + 1, list.get(j));
                j = j - 1;
            }
            list.set(j + 1, key);
        }
    }

 
    public static String padRight(String s, int n) {
        if (s.length() >= n) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < n) {
            sb.append(' ');
        }
        return sb.toString();
    }

        public static String padLeft(String s, int n) {
        if (s.length() >= n) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < n - s.length()) {
            sb.append(' ');
        }
        sb.append(s);
        return sb.toString();
    }
}