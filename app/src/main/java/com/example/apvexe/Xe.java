package com.example.apvexe;

public class Xe {
    String id, anh, ten, tuyenchay, diemden, ngaykhoihanh, giokhoihanh, sdt;
    int giave, soluongve;

    public Xe() {

    }

    public Xe(String id, String anh, String ten, String tuyenchay, String diemden, String ngaykhoihanh, String giokhoihanh, String sdt, int giave, int soluongve) {
        this.id = id;
        this.anh = anh;
        this.ten = ten;
        this.tuyenchay = tuyenchay;
        this.diemden = diemden;
        this.ngaykhoihanh = ngaykhoihanh;
        this.giokhoihanh = giokhoihanh;
        this.sdt = sdt;
        this.giave = giave;
        this.soluongve = soluongve;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getTuyenchay() {
        return tuyenchay;
    }

    public void setTuyenchay(String tuyenchay) {
        this.tuyenchay = tuyenchay;
    }

    public String getDiemden() {
        return diemden;
    }

    public void setDiemden(String diemden) {
        this.diemden = diemden;
    }

    public String getNgaykhoihanh() {
        return ngaykhoihanh;
    }

    public void setNgaykhoihanh(String ngaykhoihanh) {
        this.ngaykhoihanh = ngaykhoihanh;
    }

    public String getGiokhoihanh() {
        return giokhoihanh;
    }

    public void setGiokhoihanh(String giokhoihanh) {
        this.giokhoihanh = giokhoihanh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getGiave() {
        return giave;
    }

    public void setGiave(int giave) {
        this.giave = giave;
    }

    public int getSoluongve() {
        return soluongve;
    }

    public void setSoluongve(int soluongve) {
        this.soluongve = soluongve;
    }
}
