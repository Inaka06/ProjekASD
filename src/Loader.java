import java.io.BufferedReader;
import java.io.FileReader;

public class Loader {

    public static int hitungBarisData(String path, boolean adaHeader) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(path));
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

        BufferedReader br = new BufferedReader(new FileReader(path));
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
            if (bagian.length >= 5) {
                String kode = bagian[0].trim();
                String nama = bagian[1].trim();
                int sks = Integer.parseInt(bagian[2].trim());
                int semester = Integer.parseInt(bagian[3].trim());
                String periode = bagian[4].trim();

                hasil[idx] = new MataKuliah(kode, nama, sks, semester, periode);
                idx++;
            }
        }

        br.close();
        return hasil;
    }

    public static DataMahasiswa muatMahasiswa(String path) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        String semesterBerikutnya = "";
        ListString lulus = new ListString();

        while ((line = br.readLine()) != null) {
            String bersih = line.trim();
            if (bersih.length() == 0) {
                continue;
            }

            if (bersih.indexOf("semester_berikutnya=") == 0) {
                semesterBerikutnya = bersih.substring("semester_berikutnya=".length()).trim();
            } else if (bersih.indexOf("lulus=") == 0) {
                String isiLulus = bersih.substring("lulus=".length()).trim();
                if (isiLulus.length() > 0) {
                    String[] raw = isiLulus.split(",");
                    for (int i = 0; i < raw.length; i++) {
                        String kodeMk = raw[i].trim();
                        if (kodeMk.length() > 0) {
                            lulus.add(kodeMk);
                        }
                    }
                }
            }
        }

        br.close();
        return new DataMahasiswa(semesterBerikutnya, lulus);
    }

    public static String[][] muatPrasyarat(String path) throws Exception {
        int jumlah = hitungBarisData(path, true);
        String[][] hasil = new String[jumlah][2];

        BufferedReader br = new BufferedReader(new FileReader(path));
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
            if (bagian.length >= 2) {
                hasil[idx][0] = bagian[0].trim();
                hasil[idx][1] = bagian[1].trim();
                idx++;
            }
        }

        br.close();
        return hasil;
    }
}