public class DataMahasiswa {
    public String semesterBerikutnya;
    public ListString lulus;

    public DataMahasiswa() {
        this.semesterBerikutnya = "";
        this.lulus = new ListString();
    }

    public DataMahasiswa(String semesterBerikutnya, ListString lulus) {
        if (semesterBerikutnya != null) {
            this.semesterBerikutnya = semesterBerikutnya.trim();
        } else {
            this.semesterBerikutnya = "";
        }

        if (lulus != null) {
            this.lulus = lulus;
        } else {
            this.lulus = new ListString();
        }
    }
}