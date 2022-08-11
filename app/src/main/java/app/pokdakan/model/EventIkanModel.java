package app.pokdakan.model;

public class EventIkanModel {

    String id, event_id, produk, jumlah, satuan;

    public  EventIkanModel(String id, String event_id, String produk, String jumlah, String satuan){
        this.id = id;
        this.event_id = event_id;
        this.produk = produk;
        this.jumlah = jumlah;
        this.satuan = satuan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getProduk() {
        return produk;
    }

    public void setProduk(String produk) {
        this.produk = produk;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}
