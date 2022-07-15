/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author PC
 */
public class Customer {
    private String MaKHANG;
    private String TenKH;
    private String SoDTKH;
    private String BienSo;
    public String getBienSo() {
        return BienSo;
    }
    public void setBienSo(String BienSo) {
        this.BienSo = BienSo;
    }
    public String getMaKHANG() {
        return MaKHANG;
    }

    public void setMaKHANG(String MaKHANG) {
        this.MaKHANG = MaKHANG;
    }

    public String getTenKH() {
        return TenKH;
    }

    public void setTenKH(String TenKH) {
        this.TenKH = TenKH;
    }

    public String getSoDTKH() {
        return SoDTKH;
    }

    public void setSoDTKH(String SoDTKH) {
        this.SoDTKH = SoDTKH;
    }
}
