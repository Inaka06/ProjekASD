public class Node {
    MataKuliah mk;
    ListString prasyarat;
    Node next;
    Node nextDiTabel;

    //Bikin constructor buat Node... udah gitu aja. Daftar prasayaratnya masih kosong.
    public Node (MataKuliah mk) {
        this.mk = mk;
        this.prasyarat = new ListString ();
        this.next = null;
        this.nextDiTabel = null;
    }
}
