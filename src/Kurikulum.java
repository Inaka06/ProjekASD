public class Kurikulum {
    private Node head;
    private Node tail;
    private Node[] tabel = new Node[100]; //Dibikin 100 biar genap, suka aja sih.

    //Buat ngitung nomor slot di tabel dari huruf-huruf pada kode matkul yg ada di MK.
    private int hash (String key) {
        int hashVal = 0;
        for (int i = 0; i < key.length (); i++) {
            hashVal = (hashVal * 128 + key.charAt (i)) % tabel.length;
        }
        return hashVal % tabel.length;
    }

    //Buat nyari node dari kode MK. Kalau satu slot isinya beberapa node, cek satu satu.
    private Node cariSimpul (String kode) {
        Node temp = tabel[hash (kode)];
        while (temp != null) {
            if (temp.mk.kode.equals (kode)) {
                return temp;
            }
            temp = temp.nextDiTabel;
        }
        return null;
    }

    //Nambahin matkul ke akhir daftar dan masukin node yang sama ke tabel supaya gampang dicari.
    public void tambahMK (MataKuliah mk) {
        if (cariSimpul (mk.kode) != null) {
            throw new IllegalArgumentException ("Kode MK duplikat: " + mk.kode);
        }

        Node temp = new Node (mk);
        if (head == null) {
            head = temp;
        } else {
            tail.next = temp;
        }
        tail = temp;

        int i = hash (mk.kode);
        temp.nextDiTabel = tabel[i];
        tabel[i] = temp;
    }

    //Ngambil data matkul base on code-nya. Kalau kodenya belum ada nanti outputnya bakalan null.
    public MataKuliah getMK (String kode) {
        Node temp = cariSimpul (kode);
        if (temp == null) {
            return null;
        }
        return temp.mk;
    }

    //Ngambil semua kode MK sesuai urutan masuknya.
    public ListString semuaKode () {
        ListString hasil = new ListString ();
        Node temp = head;
        while (temp != null) {
            hasil.add (temp.mk.kode);
            temp = temp.next;
        }
        return hasil;
    }

    //Buat nyatat kodePrasyarat sebagai syarat untuk mengambil kodeMK. Kalau ada yg sama, dicatat sekali doang.
    public void tambahPrasyarat (String kodePrasyarat, String kodeMK) {
        if (cariSimpul (kodePrasyarat) == null) {
            throw new IllegalArgumentException ("Kode MK tidak dikenal: " + kodePrasyarat);
        }
        Node temp = cariSimpul (kodeMK);
        if (temp == null) {
            throw new IllegalArgumentException ("Kode MK tidak dikenal: " + kodeMK);
        }
        if (!temp.prasyarat.contains (kodePrasyarat)) {
            temp.prasyarat.add (kodePrasyarat);
        }
    }

    //Buat nyalin daftar prasyarat langsung supaya perubahan pada hasilnya ga ngubah data MK.
    public ListString prasyarat (String kodeMK) {
        Node temp = cariSimpul (kodeMK);
        if (temp == null) {
            throw new IllegalArgumentException ("Kode MK tidak dikenal: " + kodeMK);
        }
        ListString hasil = new ListString ();
        for (int i = 0; i < temp.prasyarat.size (); i++) {
            hasil.add (temp.prasyarat.get (i));
        }
        return hasil;
    }

    //Ngambil semua prasyarat matkul, sama prasyarat tiap MK yang jadi syaratnya.
    public ListString semuaPrasyarat (String kodeMK) {
        ListString hasil = new ListString ();
        ListString dikunjungi = new ListString ();
        dikunjungi.add (kodeMK);
        dfs (kodeMK, dikunjungi, hasil);
        return hasil;
    }

    //Ngecek syarat tiap MK sebelum nambahin MK itu ke hasil dan bakalan skip kode yang udah dicek.
    private void dfs (String kodeMK, ListString dikunjungi, ListString hasil) {
        ListString langsung = prasyarat (kodeMK);
        for (int i = 0; i < langsung.size (); i++) {
            String kode = langsung.get (i);
            if (!dikunjungi.contains (kode)) {
                dikunjungi.add (kode);
                dfs (kode, dikunjungi, hasil);
                hasil.add (kode);
            }
        }
    }
}
