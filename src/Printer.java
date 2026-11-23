public class Printer {
    /** Susun teks hasil rekomendasi biar bisa diprintout oleh Tes. */
    public static String buatTeks(Kurikulum k, Hasil h) {
        int lebarNama = 0;

        // Cari panjang nama MK terpanjang untuk rapikan kolom.
        for (int i = 0; i < h.bisa.size(); i++) {
            MataKuliah mk = k.getMK(h.bisa.get(i));

            if (mk.nama.length() > lebarNama) {
                lebarNama = mk.nama.length();
            }
        }

        String teks = "Daftar MK yang bisa diambil:\n";

        if (h.bisa.size() == 0) {
            teks += "(tidak ada)\n";
        } else {
            for (int i = 0; i < h.bisa.size(); i++) {
                MataKuliah mk = k.getMK(h.bisa.get(i));

                String nomor = "" + (i + 1);
                while (nomor.length() < 2) {
                    nomor = " " + nomor;
                }

                String kode = mk.kode;
                while (kode.length() < 5) {
                    kode += " ";
                }

                String nama = mk.nama;
                while (nama.length() < lebarNama) {
                    nama += " ";
                }

                teks += nomor + ". " + kode + " " + nama + "  " + mk.sks + " SKS\n";
            }
        }

        teks += "\nDaftar MK yang belum bisa diambil semester depan:\n";

        if (h.tidakBisa.size() == 0) {
            teks += "(tidak ada)\n";
        } else {
            for (int i = 0; i < h.tidakBisa.size(); i++) {
                teks += h.tidakBisa.get(i) + "\n";
            }
        }

        return teks;
    }

    /** Tampilin hasil rekomendasi di outputnya. */
    public static void cetak(Kurikulum k, Hasil h) {
        System.out.print(buatTeks(k, h));
    }
}
