import java.io.BufferedReader;
import java.io.FileReader;

// Semua tes ada di sini. Kalau ringkasannya 0 gagal berarti aman.
public class Tes {
    static int lulus = 0;
    static int gagal = 0;

    public static void main(String[] args) throws Exception {
        tesListString();
        tesKurikulum();
        tesRekomendasi();
        tesOutput();
        tesFileRusak();
        System.out.println("\nRingkasan: " + lulus + " lulus, " + gagal + " gagal.");
    }

    static void tesListString() {
        System.out.println("--- ListString");
        ListString list = new ListString();
        cek("list baru kosong", list.size() == 0);
        list.add("A");
        list.add("B");
        list.add("C");
        cek("size setelah 3 add", list.size() == 3);
        cek("get(0) = A dan get(2) = C", list.get(0).equals("A") && list.get(2).equals("C"));
        cek("contains B, tidak contains Z", list.contains("B") && !list.contains("Z"));
        try {
            list.get(3);
            cek("get di luar list harus error", false);
        } catch (IndexOutOfBoundsException e) {
            cek("get di luar list harus error", true);
        }
    }

    static void tesKurikulum() {
        System.out.println("--- Kurikulum (graph dan hash table)");
        Kurikulum k = new Kurikulum();
        String[] kode = {"AOK", "SO", "JK", "KI", "KAL", "AL", "KA", "KALJ", "JST", "PDAS"};
        for (int i = 0; i < kode.length; i++) {
            k.tambahMK(new MataKuliah(kode[i], "Nama " + kode[i], 2, 1, "Ganjil"));
        }
        k.tambahPrasyarat("AOK", "SO");
        k.tambahPrasyarat("SO", "JK");
        k.tambahPrasyarat("JK", "KI");
        k.tambahPrasyarat("KAL", "AL");
        k.tambahPrasyarat("AL", "KA");
        k.tambahPrasyarat("KA", "KALJ");
        k.tambahPrasyarat("KALJ", "JST");

        cek("semuaKode urut sesuai tambah", gabung(k.semuaKode()).startsWith("AOK, SO, JK"));
        cek("getMK(SO) ditemukan, getMK(XYZ) null", k.getMK("SO") != null && k.getMK("XYZ") == null);
        cek("prasyarat(SO) = AOK", gabung(k.prasyarat("SO")).equals("AOK"));
        cek("DFS semuaPrasyarat(KI) = AOK, SO, JK", gabung(k.semuaPrasyarat("KI")).equals("AOK, SO, JK"));
        cek("DFS semuaPrasyarat(JST) = KAL, AL, KA, KALJ", gabung(k.semuaPrasyarat("JST")).equals("KAL, AL, KA, KALJ"));
        cek("semuaPrasyarat(PDAS) kosong", k.semuaPrasyarat("PDAS").size() == 0);

        try {
            k.tambahMK(new MataKuliah("SO", "Duplikat", 2, 1, "Ganjil"));
            cek("kode MK duplikat harus error", false);
        } catch (IllegalArgumentException e) {
            cek("kode MK duplikat harus error", true);
        }

        // Tabelnya cuma 100 slot, jadi 200 MK pasti ada yang tabrakan. Semuanya harus tetap ketemu.
        Kurikulum besar = new Kurikulum();
        for (int i = 0; i < 200; i++) {
            besar.tambahMK(new MataKuliah("MK" + i, "Nama " + i, 2, 1, "Ganjil"));
        }
        boolean semuaKetemu = true;
        for (int i = 0; i < 200; i++) {
            MataKuliah mk = besar.getMK("MK" + i);
            if (mk == null || !mk.nama.equals("Nama " + i)) {
                semuaKetemu = false;
            }
        }
        cek("hash table: 200 MK semuanya ditemukan walau ada tabrakan", semuaKetemu);
    }

    static void tesRekomendasi() {
        System.out.println("--- Rekomendasi");
        Kurikulum k = new Kurikulum();
        k.tambahMK(new MataKuliah("AOK", "Arsitektur", 2, 1, "Ganjil"));
        k.tambahMK(new MataKuliah("SO", "Sistem Operasi", 5, 2, "Genap"));
        k.tambahMK(new MataKuliah("JK", "Jaringan", 5, 3, "Ganjil"));
        k.tambahMK(new MataKuliah("KI", "Keamanan", 5, 4, "Genap"));
        k.tambahMK(new MataKuliah("X", "Tanpa Prasyarat", 2, 2, "Genap"));
        k.tambahMK(new MataKuliah("SKR", "Skripsi", 6, 8, "Keduanya"));
        k.tambahPrasyarat("AOK", "SO");
        k.tambahPrasyarat("SO", "JK");
        k.tambahPrasyarat("JK", "KI");

        ListString sudahLulus = new ListString();
        sudahLulus.add("AOK");
        Hasil h = Rekomendasi.hitung(k, new DataMahasiswa("genap", sudahLulus));
        cek("bisa diambil = SO, X", gabung(h.bisa).equals("SO, X"));
        cek("alasan KI = belum lulus SO, JK", h.tidakBisa.get(0).equals("KI: belum lulus SO, JK"));
        cek("alasan SKR = kurang 4 MK", h.tidakBisa.get(1).equals("SKR: belum lulus semua MK wajib (kurang 4 MK)"));

        ListString hampirSemua = new ListString();
        String[] kode = {"AOK", "SO", "JK", "KI", "X"};
        for (int i = 0; i < kode.length; i++) {
            hampirSemua.add(kode[i]);
        }
        Hasil h2 = Rekomendasi.hitung(k, new DataMahasiswa("ganjil", hampirSemua));
        cek("SKR bisa diambil kalau semua MK lain lulus", gabung(h2.bisa).equals("SKR"));
    }

    static void tesOutput() throws Exception {
        System.out.println("--- Output dibandingkan dengan expected/");
        String folder = Main.folderProyek();
        Kurikulum kurikulum = Loader.bacaKurikulum(folder + "data/matakuliah.csv", folder + "data/prasyarat.csv");
        cek("matakuliah.csv berisi 37 MK", kurikulum.semuaKode().size() == 37);

        String[] kasus = {"mhs_01", "mhs_02", "mhs_03", "mhs_04", "mhs_05", "mhs_06"};
        for (int i = 0; i < kasus.length; i++) {
            DataMahasiswa mahasiswa = Loader.muatMahasiswa(folder + "data/" + kasus[i] + ".txt");
            String didapat = Printer.buatTeks(kurikulum, Rekomendasi.hitung(kurikulum, mahasiswa));
            String seharusnya = bacaFile(folder + "expected/" + kasus[i] + ".txt");
            cek(kasus[i], didapat.equals(seharusnya));
            if (!didapat.equals(seharusnya)) {
                System.out.println("--- seharusnya:\n" + seharusnya + "--- didapat:\n" + didapat);
            }
        }
    }

    static void tesFileRusak() {
        System.out.println("--- File rusak");
        String folder = Main.folderProyek();
        try {
            Loader.muatMahasiswa(folder + "data/mhs_rusak.txt");
            cek("semester_berikutnya tidak valid harus ditolak", false);
        } catch (Exception e) {
            cek("semester_berikutnya tidak valid harus ditolak", e.getMessage().startsWith("Format file tidak sesuai"));
        }
        try {
            Loader.muatMahasiswa(folder + "data/tidak_ada.txt");
            cek("file tidak ada harus ditolak", false);
        } catch (Exception e) {
            cek("file tidak ada harus ditolak", e.getMessage().startsWith("File tidak ditemukan"));
        }
    }

    static void cek(String nama, boolean benar) {
        if (benar) {
            lulus++;
            System.out.println("[PASS] " + nama);
        } else {
            gagal++;
            System.out.println("[FAIL] " + nama);
        }
    }

    // Biar isi list gampang dibandingin, contoh hasilnya "AOK, SO, JK".
    static String gabung(ListString list) {
        String teks = "";
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                teks += ", ";
            }
            teks += list.get(i);
        }
        return teks;
    }

    // Dibaca per baris terus ditambah "\n", jadi file Windows (CRLF) juga aman.
    static String bacaFile(String path) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String isi = "";
        String baris = br.readLine();
        while (baris != null) {
            isi += baris + "\n";
            baris = br.readLine();
        }
        br.close();
        return isi;
    }
}
