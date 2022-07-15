/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author PC
 */
public class HistoryCustomer {

    public String getMaHDon() {
        return MaHDon;
    }

    public void setMaHDon(String MaHDon) {
        this.MaHDon = MaHDon;
    }

    public String gettenDvu() {
        return tenDvu;
    }

    public void settenDvu(String tenDvu) {
        this.tenDvu = tenDvu;
    }

    public Integer getGia() {
        return Gia;
    }

    public void setGia(Integer Gia) {
        this.Gia = Gia;
    }
    public void setNgaySD(Date NgaySD) {
        this.NgaySD = NgaySD;
    }
    public Date getNgaySD() {
        return NgaySD;
    }
    private String MaHDon;
    private String tenDvu;
    private Integer Gia;
    private Date NgaySD;
}
