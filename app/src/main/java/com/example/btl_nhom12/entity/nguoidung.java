package com.example.btl_nhom12.entity;

public class nguoidung {
    private String ten;
    private String sdt;
    private String email;
    private String matkhau;
    private String ngaysinh;


    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
    public nguoidung() {
    }

    public nguoidung( String ten, String ngaysinh, String email, String sdt, String matkhau) {
        this.ngaysinh = ngaysinh;
        this.matkhau = matkhau;
        this.ten = ten;
        this.email = email;
        this.sdt = sdt;
    }
}
