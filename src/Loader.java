import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Loader {

    // Biar Main tinggal manggil satu method ini buat dapet graph Kurikulum yang udah jadi.
    public static Kurikulum bacaKurikulum(String pathMK, String pathPrasyarat) throws Exception {
        Kurikulum k = new Kurikulum();

        MataKuliah[] daftarMK = muatMataKuliah(pathMK);
        for (int i = 0; i < daftarMK.length; i++) {
            try {
                k.tambahMK(daftarMK[i]);
            } catch (IllegalArgumentException e) { // ada kode MK yang dobel
                throw formatSalah(pathMK);
            }
        }

        String[][] daftarPrasyarat = muatPrasyarat(pathPrasyarat);
        for (int i = 0; i < daftarPrasyarat.length; i++) {
            try {
                k.tambahPrasyarat(daftarPrasyarat[i][0], daftarPrasyarat[i][1]);
            } catch (IllegalArgumentException e) { // kode MK-nya gak ada di matakuliah.csv
                throw formatSalah(pathPrasyarat);
            }
        }

        return k;
    }

    // Apa pun salahnya, pesannya disamain aja biar gak ribet.
    private static Exception formatSalah(String path) {
        return new Exception("Format file tidak sesuai: " + path);
    }

    private static BufferedReader buka(String path) throws Exception {
        if (!new File(path).isFile()) {
            throw new Exception("File tidak ditemukan: " + path);
        }
        return new BufferedReader(new FileReader(path));
    }

    public static int hitungBarisData(String path, boolean adaHeader) throws Exception {
        BufferedReader br = buka(path);
        int jumlah = 0;
        String line;
        boolean barisPertama = true;

        while ((line = br.readLine()) != null) {
            if (barisPertama && adaHeader) {
                barisPertama = false;
                continue;
            }
            barisPertama = false;

            if (line.trim().length() > 0) {
                jumlah++;
            }
        }

        br.close();
        return jumlah;
    }

    public static MataKuliah[] muatMataKuliah(String path) throws Exception {
        int jumlah = hitungBarisData(path, true);
        MataKuliah[] hasil = new MataKuliah[jumlah];

        BufferedReader br = buka(path);
        String line;
        boolean barisPertama = true;
        int idx = 0;

        try {
            while ((line = br.readLine()) != null) {
                if (barisPertama) {
                    barisPertama = false;
                    continue;
                }
                String bersih = line.trim();
                if (bersih.length() == 0) {
                    continue;
                }

                String[] bagian = bersih.split(";");
                if (bagian.length != 5) {
                    throw formatSalah(path);
                }
                String periode = bagian[4].trim();
                if (!(periode.equalsIgnoreCase("Ganjil") || periode.equalsIgnoreCase("Genap")
                        || periode.equalsIgnoreCase("Keduanya"))) {
                    throw formatSalah(path);
                }
                String kode = bagian[0].trim().toUpperCase();
                String nama = bagian[1].trim();
                int sks = Integer.parseInt(bagian[2].trim());
                int semester = Integer.parseInt(bagian[3].trim());

                hasil[idx] = new MataKuliah(kode, nama, sks, semester, periode);
                idx++;
            }
        } catch (RuntimeException e) { // misal sks/semester-nya bukan angka
            throw formatSalah(path);
        } finally {
            br.close();
        }
        return hasil;
    }

    public static DataMahasiswa muatMahasiswa(String path) throws Exception {
        BufferedReader br = buka(path);
        String line;
        String semesterBerikutnya = null;
        ListString lulus = null;

        while ((line = br.readLine()) != null) {
            String bersih = line.trim();
            if (bersih.length() == 0) {
                continue;
            }

            if (bersih.indexOf("semester_berikutnya=") == 0) {
                semesterBerikutnya = bersih.substring("semester_berikutnya=".length()).trim();
            } else if (bersih.indexOf("lulus=") == 0) {
                lulus = new ListString();
                String[] raw = bersih.substring("lulus=".length()).split(",");
                for (int i = 0; i < raw.length; i++) {
                    String kodeMk = raw[i].trim().toUpperCase();
                    if (kodeMk.length() > 0) {
                        lulus.add(kodeMk);
                    }
                }
            }
        }
        br.close();

        if (semesterBerikutnya == null || lulus == null
                || !(semesterBerikutnya.equalsIgnoreCase("ganjil") || semesterBerikutnya.equalsIgnoreCase("genap"))) {
            throw formatSalah(path);
        }
        return new DataMahasiswa(semesterBerikutnya, lulus);
    }

    public static String[][] muatPrasyarat(String path) throws Exception {
        int jumlah = hitungBarisData(path, true);
        String[][] hasil = new String[jumlah][2];

        BufferedReader br = buka(path);
        String line;
        boolean barisPertama = true;
        int idx = 0;

        while ((line = br.readLine()) != null) {
            if (barisPertama) {
                barisPertama = false;
                continue;
            }
            String bersih = line.trim();
            if (bersih.length() == 0) {
                continue;
            }

            String[] bagian = bersih.split(";");
            if (bagian.length != 2) {
                br.close();
                throw formatSalah(path);
            }
            hasil[idx][0] = bagian[0].trim().toUpperCase();
            hasil[idx][1] = bagian[1].trim().toUpperCase();
            idx++;
        }

        br.close();
        return hasil;
    }
}
