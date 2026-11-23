# Proyek 2 ASD: Rekomendasi Pengambilan Mata Kuliah

Program Java yang membaca peta mata kuliah (`matakuliah.csv`), daftar prasyarat (`prasyarat.csv`), dan daftar MK yang sudah lulus (`mhs_xx.txt`). Hasilnya daftar MK yang bisa diambil semester depan, plus MK yang belum bisa diambil beserta alasannya.

## Anggota

| Bagian | Anggota | File |
|---|---|---|
| O1 | Ahmad Fathi Al Ghifari | `NodeString`, `ListString`, `Node`, `Kurikulum` |
| O2 | @BadGems | `Loader`, `MataKuliah`, `DataMahasiswa` |
| O3 | @ravinanatasha-gif | `Rekomendasi`, `Hasil`, `Printer`, `laporan/2-algoritma.md` |
| O4 | @Inaka06 | `Main`, `Tes`, data uji, `expected/`, README |

## Cara menjalankan

Jalankan `Main` tanpa argumen untuk menjalankan semua contoh di `data/`, atau `Tes` untuk menjalankan semua tes.

Lewat terminal:

```
javac -d out src/*.java
java -cp out Main                                                         # semua contoh
java -cp out Main data/matakuliah.csv data/prasyarat.csv data/mhs_01.txt  # satu file
java -cp out Tes                                                          # semua tes
```

## Contoh output (`mhs_01.txt`)

```
Daftar MK yang bisa diambil:
 1. BD    Basis Data          3 SKS
 2. PLAN  Pemrograman Lanjut  5 SKS
 3. SO    Sistem Operasi      5 SKS
 4. MD    Matematika Diskret  2 SKS
 5. AL    Aljabar Linear      2 SKS
 6. EP    Etika Profesi       2 SKS
 7. BING  Bahasa Inggris      2 SKS
 8. PANC  Pancasila           2 SKS
 9. KWU   Kewirausahaan       2 SKS
10. KWN   Kewarganegaraan     2 SKS
11. CAP   Capstone Project    3 SKS

Daftar MK yang belum bisa diambil semester depan:
PAW: belum lulus SO, IMK
DAA: belum lulus PLAN, ASD
KI: belum lulus SO, JK
KALJ: belum lulus AL, KA
SKR: belum lulus semua MK wajib (kurang 29 MK)
```

## Data uji

| File | Isi |
|---|---|
| `mhs_01`–`mhs_03` | contoh dari soal |
| `mhs_04` | lulus semua kecuali Skripsi, jadi Skripsi bisa diambil |
| `mhs_05` | belum lulus apa pun |
| `mhs_06` | ada kode tidak dikenal (`XYZ`), cuma dikasih peringatan |
| `mhs_rusak` | `semester_berikutnya=pendek`, harus keluar `Format file tidak sesuai` |

Kunci jawaban output `mhs_01`–`mhs_06` ada di folder `expected/`, dan `Tes` membandingkan output program dengan isi folder itu.

## Struktur data

- **Singly linked list** (`NodeString`, `ListString`) untuk daftar kode MK.
- **Graph berarah** (`Node`, `Kurikulum`): setiap MK satu node, dan setiap node menyimpan list prasyarat langsungnya (adjacency list). Rantai prasyarat dicari pakai DFS.
- **Hash table** (`Kurikulum`, array `Node[100]` dengan separate chaining) supaya MK bisa dicari dari kodenya dengan cepat.
