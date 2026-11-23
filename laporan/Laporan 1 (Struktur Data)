# Struktur Data Kurikulum

Program menyimpan 37 mata kuliah (MK) dan 19 hubungan prasyarat dalam graf berarah. Setiap `Node` berisi satu `MataKuliah` dan daftar kode prasyarat langsungnya. Misalnya, untuk mengecek syarat Keamanan Informasi (KI), program cukup membuka daftar milik KI.

## Daftar prasyarat dan urutan katalog

Setiap MK punya `ListString`, yaitu daftar berantai satu arah buatan sendiri. Satu `NodeString` menyimpan satu kode dan penunjuk ke node berikutnya. Berikut cuplikan daftar prasyarat sesuai data kelompok.

```text
MK     Prasyarat langsung
KI     [JK]
JK     [SO]
SO     [AOK]
AOK    []
```

Untuk mencari semua prasyarat KI, `semuaPrasyarat` memakai DFS dan mengikuti syarat satu per satu sampai AOK. Saat kembali dari penelusuran, program mencatat AOK, lalu SO, lalu JK. Urutan ini menunjukkan MK yang perlu diselesaikan lebih dulu. Setiap kode ditandai saat pertama dikunjungi supaya jalur yang bertemu tidak menghasilkan kode ganda.

## Pencarian kode mata kuliah

Di `Kurikulum`, setiap node MK punya dua penghubung. `next` mengikuti urutan katalog, sehingga `semuaKode()` bisa membaca MK sesuai urutan CSV. `nextDiTabel` menghubungkan MK yang mendapat slot yang sama di tabel hash berukuran 100 slot. Fungsi hash membaca kode per karakter, mengalikan nilai sebelumnya dengan 128, menambahkan nilai karakter, lalu mengambil sisa bagi 100.

```text
Urutan katalog     head → RPL → PDAS → PKK → AOK → …
Isi tabel[96]      PKL → AL → KAL
```

KAL, AL, dan PKL masuk ke slot 96 dalam urutan tersebut. Karena node baru diletakkan di depan rantai, pencarian KAL dimulai dari PKL, melewati AL, lalu menemukan KAL.

## Kompleksitas

Pada `ListString`, menambah data di akhir lewat `tail` dan membaca `size()` memerlukan O(1). Untuk daftar berisi n kode, `get(i)` dan `contains()` paling lama O(n) karena pencarian dimulai dari depan. Untuk kode pendek dan rantai hash yang pendek, `getMK()` rata-rata O(1). Jika semua V mata kuliah masuk satu slot, pencarian bisa mencapai O(V).

Untuk satu DFS, batas atas waktunya O(V(V + E)), dengan E jumlah hubungan prasyarat. Penyebabnya, program berulang kali membaca daftar melalui indeks dan mencari kode yang sudah dikunjungi di dalam linked list.
