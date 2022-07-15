/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;

/**
 *
 * NDKhang
 */
public class InvoiceDetails {
    private String Mahdon;
    private String MaTToan;
    private String TenNVien;
    private String TenKH;
    private String TenDV;
    private int Gia;

    public Date getNgay() {
        return Ngay;
    }

    public void setNgay(Date Ngay) {
        this.Ngay = Ngay;
    }
    private Date Ngay;

    public String getMahdon() {
        return Mahdon;
    }

    public void setMahdon(String Mahdon) {
        this.Mahdon = Mahdon;
    }

    public String getMaTToan() {
        return MaTToan;
    }

    public void setMaTToan(String MaTToan) {
        this.MaTToan = MaTToan;
    }

    public String getTenNVien() {
        return TenNVien;
    }

    public void setTenNVien(String TenNVien) {
        this.TenNVien = TenNVien;
    }

    public String getTenKH() {
        return TenKH;
    }

    public void setTenKH(String TenKH) {
        this.TenKH = TenKH;
    }

    public String getTenDV() {
        return TenDV;
    }

    public void setTenDV(String TenDV) {
        this.TenDV = TenDV;
    }

    public int getGia() {
        return Gia;
    }

    public void setGia(int Gia) {
        this.Gia = Gia;
    }
}
