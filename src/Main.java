import java.io.File;

public class Main {

    public static void main(String[] args) {
        if (args.length == 3) {
            jalankan(args[0], args[1], args[2]);
            return;
        }

        // Kalau gak ada argumen, jalanin semua contoh di data/ sekaligus.
        String folder = folderProyek();
        String[] contoh = {"mhs_01", "mhs_02", "mhs_03", "mhs_04", "mhs_05", "mhs_06", "mhs_rusak"};
        for (int i = 0; i < contoh.length; i++) {
            System.out.println("========== " + contoh[i] + ".txt ==========");
            jalankan(folder + "data/matakuliah.csv", folder + "data/prasyarat.csv",
                    folder + "data/" + contoh[i] + ".txt");
            System.out.println();
        }
    }

    static void jalankan(String pathMK, String pathPrasyarat, String pathMahasiswa) {
        try {
            Kurikulum kurikulum = Loader.bacaKurikulum(pathMK, pathPrasyarat);
            DataMahasiswa mahasiswa = Loader.muatMahasiswa(pathMahasiswa);

            // Kode lulus yang gak ada di kurikulum cukup dikasih peringatan, programnya tetap jalan.
            for (int i = 0; i < mahasiswa.lulus.size(); i++) {
                String kode = mahasiswa.lulus.get(i);
                if (kurikulum.getMK(kode) == null) {
                    System.out.println("Peringatan: kode MK '" + kode + "' tidak dikenal, diabaikan");
                }
            }

            Printer.cetak(kurikulum, Rekomendasi.hitung(kurikulum, mahasiswa));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Nyari folder data/ biar program tetap jalan walau di-run dari folder di atas folder proyek.
    static String folderProyek() {
        if (new File("data/matakuliah.csv").isFile()) {
            return "";
        }
        File[] isi = new File(".").listFiles();
        if (isi != null) {
            for (int i = 0; i < isi.length; i++) {
                if (new File(isi[i], "data/matakuliah.csv").isFile()) {
                    return isi[i].getName() + "/";
                }
            }
        }
        return "";
    }
}
