/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;

/**
 *
 * @author PC
 */
public class Bill {
    private String MaTTOAN;
    private String MaNVIEN;
    private String MaKHANG;
    private String MaDVU;
    private String MaHDON;
    private Date NgayTT;
    public String getMaTTOAN() {
        return MaTTOAN;
    }

    public void setMaTTOAN(String MaTTOAN) {
        this.MaTTOAN = MaTTOAN;
    }

    public String getMaNVIEN() {
        return MaNVIEN;
    }

    public void setMaNVIEN(String MaNVIEN) {
        this.MaNVIEN = MaNVIEN;
    }

    public String getMaKHANG() {
        return MaKHANG;
    }

    public void setMaKHANG(String MaKHANG) {
        this.MaKHANG = MaKHANG;
    }

    public String getMaDVU() {
        return MaDVU;
    }

    public void setMaDVU(String MaDVU) {
        this.MaDVU = MaDVU;
    }

    public String getMaHDON() {
        return MaHDON;
    }

    public void setMaHDON(String MaHDON) {
        this.MaHDON = MaHDON;
    }

    public Date getNgayTT() {
        return NgayTT;
    }

    public void setNgayTT(Date NgayTT) {
        this.NgayTT = NgayTT;
    }
}
