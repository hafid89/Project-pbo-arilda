Ringkasan Penerapan 5 Pilar OOP dan Multithreading di GeometryProject

Dokumen ini merangkum lokasi implementasi OOP (enkapsulasi, abstraksi, pewarisan, polimorfisme, interface/kontrak) serta mekanisme multithreading di proyek.

- **Enkapsulasi**: atribut private dan akses melalui getter/setter
  - `src/geometry/BendaGeometri.java` (atribut `nama`, `warna`, metode `getNama()`/`setNama()`): [src/geometry/BendaGeometri.java](src/geometry/BendaGeometri.java#L17-L55)
  - `src/geometry/Elips.java` (atribut `sumbuPanjang`, `sumbuPendek`, validasi pada setter): [src/geometry/Elips.java](src/geometry/Elips.java#L40-L49)

- **Abstraksi**: kelas abstrak yang mendefinisikan kontrak umum
  - `src/geometry/BendaGeometri.java` (abstract base class): [src/geometry/BendaGeometri.java](src/geometry/BendaGeometri.java#L14-L21)
  - `src/geometry/Benda2Dimensi.java`, `src/geometry/Benda3Dimensi.java`

- **Pewarisan**: subclass mewarisi perilaku dan atribut
  - `Elips` extends `Benda2Dimensi`: [src/geometry/Elips.java](src/geometry/Elips.java#L12-L12)
  - `BolaElips` extends `Benda3Dimensi`: [src/geometry/BolaElips.java](src/geometry/BolaElips.java#L1-L1)

- **Polimorfisme**: overriding dan pemanggilan lewat tipe umum
  - Override `hitungLuas()`/`hitungKeliling()` di `Elips`: [src/geometry/Elips.java](src/geometry/Elips.java#L67-L74)
  - Penggunaan `instanceof` dan pemanggilan berbeda di runtime (`BendaGeometri.run()`): [src/geometry/BendaGeometri.java](src/geometry/BendaGeometri.java#L81-L89)
  - Contoh gaya `Parent p = new Child();` ada di GUI melalui `calculateShape(BendaGeometri shape, ...)`.
    - `calculateElips()` membuat `Elips elips = new Elips(a, b);`
    - `calculateTabungElips()` membuat `TabungElips tabung = new TabungElips(alas, t);`
    - `calculateBolaElips()` membuat `BolaElips bola = new BolaElips(a, b, c);`
    Semua objek tersebut diperlakukan sebagai `BendaGeometri` ketika dikirim ke `calculateShape()`.
  - Ada demo khusus `ui.PolymorphismDemo` yang menampilkan contoh `EkspresiWajah objGembira = new WajahGembira();` dan mencetak hasilnya ke terminal.

- **Interface / Kontrak**: `VolumeCalculable`
  - `src/geometry/VolumeCalculable.java` dan implementasi di `Benda3Dimensi`/kelas 3D: [src/geometry/VolumeCalculable.java](src/geometry/VolumeCalculable.java#L1-L5)

- **Multithreading**: beberapa pola concurrency digunakan
  - `BendaGeometri` implements `Runnable` dan menyediakan API asinkron:
    - `calculateAsync()` menggunakan shared `ExecutorService` (`newCachedThreadPool()`)
    - `calculateWithFuture()` mengembalikan `Future<Double>` sehingga caller dapat menunggu hasil atau membatalkan tugas.
    Referensi: [src/geometry/BendaGeometri.java](src/geometry/BendaGeometri.java#L14-L21) & [src/geometry/BendaGeometri.java](src/geometry/BendaGeometri.java#L95-L103)
  - `MultithreadingGeometryDemo` menunjukkan:
    - pembuatan thread pool dengan `Executors.newFixedThreadPool(cores)`
    - penggunaan `Future` untuk menunggu hasil
    - pembatalan/interrupt dengan `future.cancel(true)`
    - perbandingan performa sekuensial vs paralel.
    Referensi: [src/geometry/MultithreadingGeometryDemo.java](src/geometry/MultithreadingGeometryDemo.java#L1-L11)
  - `GeometryCalculator` menunjukkan cara aman menjalankan perhitungan di background thread:
    - menggunakan `Thread` terpisah untuk kalkulasi UI-friendly
    - `volatile boolean isCalculating` dan `synchronized(lock)` untuk state thread-safe
    - menangani `InterruptedException` untuk pembatalan
    - menggunakan `SwingUtilities.invokeLater` untuk mengupdate UI dari thread non-UI.
    Referensi: [src/calculator/GeometryCalculator.java](src/calculator/GeometryCalculator.java#L60-L100)

Jika Anda ingin versi lebih panjang (halaman per pilar dengan kutipan kode dan penjelasan baris demi baris), saya akan memperluas tiap bagian menjadi halaman terpisah atau menambahkan lebih banyak kutipan kode di file ini.
