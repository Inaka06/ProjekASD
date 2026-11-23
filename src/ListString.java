public class ListString {
    private NodeString head;
    private NodeString tail;
    private int jumlah;

    //Buat nambahin data baru di akhir daftar.
    public void add (String data) {
        if (head == null) {
            head = new NodeString (data);
            tail = head;
        } else {
            tail.next = new NodeString (data);
            tail = tail.next;
        }
        jumlah++;
    }

    //Nanti bakalan ngambil data di posisi yang diminta, start-nya dari node pertama yaitu 0.
    public String get (int index) {
        if (index < 0 || index >= jumlah) {
            throw new IndexOutOfBoundsException ();
        }
        NodeString temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp.data;
    }

    //Buat balikin jumlah data yang ada di daftar.
    public int size () {
        return jumlah;
    }

    //Ngecek satu satu ada data yang dicari atau ngga di daftar.
    public boolean contains (String data) {
        NodeString temp = head;
        while (temp != null) {
            if (data == null) {
                if (temp.data == null) {
                    return true;
                }
            } else if (data.equals (temp.data)) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }
}
