# Algoritma Rekomendasi Mata Kuliah

Program ini digunakan untuk cek mata kuliah (MK) yang bisa diambil untuk semester depan berdasarkan jadwal penawaran dan MK yang sudah lulus. Kode pengecekannya ada di `Rekomendasi.hitung()`. Hasilnya disimpan di kode `Hasil`, lalu hasilkan outputnya lewat kode `Printer`.

## Langkah algoritma

1. Ambil semua kode MK lewat `k.semuaKode()`, lalu cek satu per satu sesuai urutan katalog.
2. Lewati MK yang sudah lulus atau tidak ditawarkan semester depan. MK dengan periode `Keduanya` tersedia pada semester Ganjil maupun Genap.
3. Sedangkan Skripsi (`SKR`), hitung MK lain yang belum lulus. Kalau semua MK lulus, Skripsi bisa diambil. Kalau belum, catat jumlah MK yang masih kurang.
4. Untuk MK lainnya, cek prasyarat langsungnya. Kalau semua sudah lulus, simpan kode MK ke daftar `bisa`. Kalau tanpa prasyarat juga boleh diambil selama ditawarkan.
5. Kalau ada prasyarat langsung yang belum lulus, ambil semua rantainya lewat `k.semuaPrasyarat()`. Method ini memakai DFS untuk menelusuri prasyarat. Susun alasan dari syarat paling awal sampai syarat langsung, ambil yang belum lulus saja.
6. Simpan alasannya ke daftar `tidakBisa`. Setelah semua dicek, `Printer.buatTeks()` ambil nama dan SKS lewat `k.getMK()`, lalu menyusun dua daftar. Daftar kosong ditulis `(tidak ada)`.

MK yang masuk rekomendasi tetap belum dianggap lulus saat cek MK selanjutnya. Jadi, MK lanjutan belum otomatis bisa diambil bersama prasyarat yang baru direkomendasikan.

## Contoh untuk mhs_01

Sesuai yang ada di data tersebut, semester berikutnya adalah Genap. Mahasiswa sudah lulus RPL, PDAS, PKK, AOK, KAL, STP, dan AGM. Dari 37 MK di katalog, ada **11 MK yang bisa diambil**, yaitu BD, PLAN, SO, MD, AL, EP, BING, PANC, KWU, KWN, dan CAP.

| MK yang belum bisa | Alasan |
| --- | --- |
| PAW | Belum lulus SO, IMK |
| DAA | Belum lulus PLAN, ASD |
| KI | Belum lulus SO, JK |
| KALJ | Belum lulus AL, KA |
| SKR | Belum lulus semua MK wajib, masih kurang 29 MK |

SO bisa diambil karena AOK sudah lulus. PAW belum bisa karena IMK belum lulus. Rantai prasyaratnya juga memuat SO yang belum lulus, sehingga keduanya ditulis sebagai alasan. CAP ikut muncul karena ditawarkan Genap dan tidak punya prasyarat di CSV.

## Kompleksitas

Misalkan V adalah jumlah MK, E jumlah hubungan prasyarat, L jumlah kode di daftar lulus, dan T panjang seluruh teks hasil. Panjang kode MK dianggap tetap.

- Pada `ListString`, `get(i)` mencakup node dari awal, jadi paling lama O(V). Loop yang ambil semua elemen dengan `get(i)` bisa membutuhkan O(V²). Untuk `lulus.contains()` butuh O(L).
- Kalau satu MK punya p prasyarat langsung, pengecekannya membutuhkan O(p² + pL), karena ada penelusuran linked list dan pencarian dalam daftar lulus.
- DFS juga pakai linked list untuk menandai kode yang sudah dikunjungi. Satu penelusuran punya batas atas O(V(V + E)). Karena bisa diulang untuk banyak MK, ditambah pengecekan kelulusan, batas atas waktu `Rekomendasi.hitung()` adalah **O(V²(V + E + L))**.
- Penyusunan hasil pada `Printer.buatTeks()` punya batas atas **O(V² + T²)**. Bagian O(T²) berasal dari penggabungan teks berulang dengan `+=`, yang menyalin kembali teks sebelumnya.
