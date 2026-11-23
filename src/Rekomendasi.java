public class Rekomendasi {
    /** Cek MK yang bisa diambil dan jelasin kenapa MK yang belum bisa diambil. */
    public static Hasil hitung(Kurikulum k, DataMahasiswa m) {
        Hasil hasil = new Hasil();
        ListString semuaKode = k.semuaKode();

        for (int i = 0; i < semuaKode.size(); i++) {
            String kode = semuaKode.get(i);
            MataKuliah mk = k.getMK(kode);

            // MK yang sudah lulus gak bakal muncul lagi.
            if (m.lulus.contains(kode)) {
                continue;
            }

            // Cek apa MK ditawarkan atau nggak untuk semester depan.
            if (!mk.periode.equalsIgnoreCase(m.semesterBerikutnya)
                    && !mk.periode.equalsIgnoreCase("Keduanya")) {
                continue;
            }

            // Skripsi ada prasyarat tersendiri, kalau MK lulus semua berati bisa ambil Skripsi/SK 
            if (kode.equals("SKR")) {
                int sisa = hitungSisaWajib(semuaKode, m.lulus);

                if (sisa == 0) {
                    hasil.bisa.add(kode);
                } else {
                    hasil.tidakBisa.add(
                        "SKR: belum lulus semua MK wajib (kurang " + sisa + " MK)"
                    );
                }
                continue;
            }

            ListString syarat = k.prasyarat(kode);
            if (sudahLulusSemua(syarat, m.lulus)) {
                hasil.bisa.add(kode);
            } else {
                ListString semuaSyarat = k.semuaPrasyarat(kode);
                String alasan = buatAlasan(semuaSyarat, m.lulus);
                hasil.tidakBisa.add(kode + ": belum lulus " + alasan);
            }
        }

        return hasil;
    }

    private static boolean sudahLulusSemua(ListString syarat, ListString lulus) {
        for (int i = 0; i < syarat.size(); i++) {
            if (!lulus.contains(syarat.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static String buatAlasan(ListString semuaSyarat, ListString lulus) {
        String alasan = "";

        // Terus kurikulum udah tangani untuk urutan dan penghapusan duplikatnya 
        for (int i = 0; i < semuaSyarat.size(); i++) {
            String kode = semuaSyarat.get(i);

            if (!lulus.contains(kode)) {
                if (!alasan.equals("")) {
                    alasan += ", ";
                }
                alasan += kode;
            }
        }

        return alasan;
    }

    private static int hitungSisaWajib(ListString semuaKode, ListString lulus) {
        int sisa = 0;

        for (int i = 0; i < semuaKode.size(); i++) {
            String kode = semuaKode.get(i);

            if (!kode.equals("SKR") && !lulus.contains(kode)) {
                sisa++;
            }
        }

        return sisa;
    }
}
