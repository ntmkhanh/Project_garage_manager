/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.List;

/**
 *
 * @author PC
 */
public class Services {
    private String MaDvu;
    private String TenDvu;
    private Integer Gia;
    
    public String getMaDvu() {
        return MaDvu;
    }

    public void setMaDvu(String MaDvu) {
        this.MaDvu = MaDvu;
    }

    public String getTenDvu() {
        return TenDvu;
    }

    public void setTenDvu(String TenDvu) {
        this.TenDvu = TenDvu;
    }

    public Integer getGia() {
        return Gia;
    }

    public void setGia(Integer Gia) {
        this.Gia = Gia;
    }
}
