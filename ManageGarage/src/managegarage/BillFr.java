/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managegarage;

import Model.Bill;
import Model.HistoryCustomer;
import Model.InvoiceDetails;
import javax.swing.JFrame;
import SQL.ConnectionSQL;
import com.mysql.cj.jdbc.CallableStatement;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author PC
 */
public class BillFr extends javax.swing.JFrame {
    DefaultTableModel defaultTableModel;
    Bill bill;
    CallableStatement cStmt = null;
    Connection conn = ConnectionSQL.getConnection();
    public BillFr() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateComboDV();
        updateComboNV();
        updateComboKH();
        setTableData();
};
    //Ham them du lieu vao bang hoa don
        public void setTableData() {
        defaultTableModel = new DefaultTableModel() {
            public boolean isCellEdittable(int row, int column) {
                return false;
            }
    };
        TableBill.setModel(defaultTableModel);
        defaultTableModel.addColumn("Mã Thanh Toán");
        defaultTableModel.addColumn("Mã Nhân Viên");
        defaultTableModel.addColumn("Mã Khách Hàng");
        defaultTableModel.addColumn("Mã Dịch Vụ");
        defaultTableModel.addColumn("Định Danh Hóa Đơn");
        defaultTableModel.addColumn("Ngày Thanh Toán");
        try {
        String sql = "SELECT * from THANHTOAN";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery(sql);
        List<Bill> bills = new ArrayList<Bill>();
        while (rs.next()) {
            Bill bill = new Bill();
            bill.setMaTTOAN(rs.getString("MaTToan"));
            bill.setMaNVIEN(rs.getString("MaNVien"));
            bill.setMaKHANG(rs.getString("MaKHANG"));
            bill.setMaDVU(rs.getString("MaDVU"));
            bill.setMaHDON(rs.getString("MaHDON"));
            bill.setNgayTT(rs.getDate("NgayTT"));
            bills.add(bill);
            }
        for (Bill bill : bills) {
            defaultTableModel.addRow(new Object[]{bill.getMaTTOAN(), bill.getMaNVIEN(), bill.getMaKHANG()
                                        , bill.getMaDVU(),bill.getMaHDON(), bill.getNgayTT()});
    }
        } catch (SQLException e) {}
}    
    //Ham them du lieu vao combobox dichvu 
        private void updateComboDV() {
        try {
            String sql = "SELECT TENDV from DICHVU";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString("TenDV");
                jComboBoxDV.addItem(name);
                }
        } catch (SQLException e) {}
}
    //Ham them ten nhan vien vao combobox
        private void updateComboNV() {
        try {
            String sql = "SELECT * from NHANVIEN";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);
            while (rs.next()) {
                String HoTen = rs.getString("HoTen");
                jComboBoxNV.addItem(HoTen);
                }
        } catch (SQLException e) {}
}
    //Ham them ten khach hang vao combobox
        private void updateComboKH() {
        try {
            String sql = "SELECT * from KHACHHANG";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString("TenKH");
                jComboBoxKH.addItem(name);
                }
        } catch (SQLException e) {}
}
    //Thong ke hoa don theo ngay
        public void thongkehoadon() {  
        defaultTableModel = new DefaultTableModel() {
        public boolean isCellEdittable(int row, int column) {
            return false;
        }
    };
        TableCTHD.setModel(defaultTableModel);
        defaultTableModel.addColumn("Ngày TT");
        defaultTableModel.addColumn("Định Danh Hóa Đơn");
        defaultTableModel.addColumn("Mã Thanh Toán");
        defaultTableModel.addColumn("Tên Nhân Viên");
        defaultTableModel.addColumn("Tên Khách Hàng");
        defaultTableModel.addColumn("Tên Dịch Vụ");
        defaultTableModel.addColumn("Giá Dịch Vụ");
        try {
        cStmt = (CallableStatement) conn.prepareCall("{call thongke(?)}");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(jDateChooserTK.getDate());
        cStmt.setString(1, date);
        ResultSet rs = cStmt.executeQuery();
        List<InvoiceDetails> idetail = new ArrayList<InvoiceDetails>();
        while (rs.next()) {
            InvoiceDetails idetails = new InvoiceDetails();
            idetails.setNgay(rs.getDate("ngay"));
            idetails.setMahdon(rs.getString("mahdon"));
            idetails.setMaTToan(rs.getString("mattoan"));
            idetails.setTenNVien(rs.getString("tennvien"));
            idetails.setTenKH(rs.getString("tenkh"));
            idetails.setTenDV(rs.getString("tendv"));
            idetails.setGia(rs.getInt("gia"));
            //idetail.setGia(rs.getDate("gia"));
            idetail.add(idetails);
            }
        for (InvoiceDetails idetails : idetail) {
            defaultTableModel.addRow(new Object[]{idetails.getNgay(), idetails.getMahdon(), idetails.getMaTToan(), idetails.getTenNVien()
                                        , idetails.getTenKH(),idetails.getTenDV(), idetails.getGia()});
        }
    } catch (SQLException e) {}   
}
    //Xem chi tiet hoa don 
        public void ViewBillDetail() {
        KetQua.setText("");
        defaultTableModel = new DefaultTableModel() {
            public boolean isCellEdittable(int row, int column) {
                return false;
            }
    };  
        if (jTextFieldNhap.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(BillFr.this, "Vui lòng nhập định danh hóa đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
        TableCTHD.setModel(defaultTableModel);
        defaultTableModel.addColumn("Định Danh Hóa Đơn");
        defaultTableModel.addColumn("Mã Thanh Toán");
        defaultTableModel.addColumn("Tên Nhân Viên");
        defaultTableModel.addColumn("Tên Khách Hàng");
        defaultTableModel.addColumn("Tên Dịch Vụ");
        defaultTableModel.addColumn("Giá Dịch Vụ");
        try {
        cStmt = (CallableStatement) conn.prepareCall("{call view_bill(?)}");
        cStmt.setString(1, jTextFieldNhap.getText());
        ResultSet rs = cStmt.executeQuery();
        List<InvoiceDetails> idetail = new ArrayList<InvoiceDetails>();
        while (rs.next()) {
            InvoiceDetails idetails = new InvoiceDetails();
            idetails.setMahdon(rs.getString("mahdon"));
            idetails.setMaTToan(rs.getString("mattoan"));
            idetails.setTenNVien(rs.getString("tennvien"));
            idetails.setTenKH(rs.getString("tenkh"));
            idetails.setTenDV(rs.getString("tendv"));
            idetails.setGia(rs.getInt("gia"));
            idetail.add(idetails);
            }
        for (InvoiceDetails idetails : idetail) {
            defaultTableModel.addRow(new Object[]{idetails.getMahdon(), idetails.getMaTToan(), idetails.getTenNVien()
                                        , idetails.getTenKH(),idetails.getTenDV(), idetails.getGia()});
   }
        } catch (SQLException e) {}   
   }
}
    //Ham them hoa don
        public void AddBill() {
            // Kiem tra nhap id
        while (true) {
            if (jTextFieldID.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Không được để trống !");
                jTextFieldID.grabFocus();
                return;
            } else if (!jTextFieldID.getText().trim().matches("^[0-9]{5}$")) {
                    JOptionPane.showMessageDialog(this, "Không đúng định dạng mã ! Ví dụ : xxxxx ");
                    jTextFieldID.grabFocus();
                    return;
            }
            {
                break;
            }
}
            // Kiem tra ma dinh danh hoa don 
        while (true) {
            if (jTextFieldMaHD.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Không được để trống !");
                jTextFieldMaHD.grabFocus();
                return;
            } else if (!jTextFieldMaHD.getText().trim().matches("^[0-9]{5}$")) {
                    JOptionPane.showMessageDialog(this, "Không đúng định dạng mã định danh ! Ví dụ : xxxxx ");
                    jTextFieldMaHD.grabFocus();
                    return;
            }
            {
                break;
            }
}
        try {
            cStmt = (CallableStatement) conn.prepareCall("{call them_tt(?, ?, ?, ?, ?, ?)}");
            cStmt.setString(1, jTextFieldID.getText());
            cStmt.setString(2, jTextFieldMNV.getText());
            cStmt.setString(3, jTextFieldMKH.getText());
            cStmt.setString(4, jTextFieldMDV.getText());
            cStmt.setString(5, jTextFieldMaHD.getText());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(jDateChooserNTT.getDate());
            cStmt.setString(6, date);
     
            int rs = cStmt.executeUpdate();
            System.out.println(rs);
            defaultTableModel.setRowCount(0);
            setTableData();
            JOptionPane.showMessageDialog(BillFr.this, "Thêm thanh toán thành công");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(BillFr.this, "Chưa nhập thông tin thanh toán");
        }
}
    //Ham giam gia hoa don 
        public void SaleBill() {
        Font font1 = new Font("SansSerif", Font.BOLD, 20);
        if ( jTextFieldNhap.getText().trim().equals("") ) {
            jTextFieldNhap.setBackground(Color.BLUE);
            JOptionPane.showMessageDialog(BillFr.this, "Vui lòng nhập định danh hóa đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
            jTextFieldNhap.setBackground(Color.WHITE);
        } else {
        try {
            cStmt =  (CallableStatement) conn.prepareCall("select sale_bill(?) as sale");
            cStmt.setString(1, jTextFieldNhap.getText());
            ResultSet rs = cStmt.executeQuery();
            if (rs.next()) {
                int so = rs.getInt("sale");
                KetQua.setText("" + so + " VND");
            }
            KetQua.setFont(font1);
            KetQua.setForeground(Color.BLUE);
        } catch (SQLException e) {}
    }
}
    //Ham tim ID khach hang
        public void FindIDKH() {
        try {
            cStmt = (CallableStatement) conn.prepareCall("{call FINDTen_KHANG(?)}");
            cStmt.setString(1, (String)jComboBoxKH.getSelectedItem());
            ResultSet rs = cStmt.executeQuery();
            //Lay du lieu tu combobox
            if(rs.next()) {
                jTextFieldMKH.setText(rs.getString("MaKHANG"));
            }
       } catch (SQLException e) {}
    }
    //Ham tai lai du lieu
        public void refreshData() {
        setTableData();
        DefaultTableModel Table = (DefaultTableModel) TableCTHD.getModel();
        Table.setRowCount(0);
        jTextFieldNhap.setText("");
        jTextFieldID.setText("");
        jTextFieldMaHD.setText("");
        ThongKe.setText("");
        KetQua.setText("");
    }
    //Ham tinh tong hoa don
        public void SumBill() {
        Font font1 = new Font("SansSerif", Font.BOLD, 20);
        if ( jTextFieldNhap.getText().trim().equals("") ) {
            JOptionPane.showMessageDialog(BillFr.this, "Vui lòng nhập định danh hóa đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
        try {
            cStmt = (CallableStatement) conn.prepareCall("{call sum_bill(?)}");
            cStmt.setString(1, jTextFieldNhap.getText());
            ResultSet rs = cStmt.executeQuery();
            //Lay du lieu tu combobox
            if(rs.next()) {
                KetQua.setText(rs.getString("GiaDV")+ " VND");
            }
            KetQua.setFont(font1);
            KetQua.setForeground(Color.BLUE);
       } catch (SQLException e) {}
    }
}
    //Ham tim ID Nhan Vien
        public void FindIDNV() {
        try {
            cStmt = (CallableStatement) conn.prepareCall("{call FINDTen_NHANVIEN(?)}");
            cStmt.setString(1, (String)jComboBoxNV.getSelectedItem());
            ResultSet rs = cStmt.executeQuery();
            //Lay du lieu tu combobox
            if(rs.next()) {
                jTextFieldMNV.setText(rs.getString("MaNVien"));
            }
       } catch (SQLException e) {}
}
    //Ham tim ID Dich Vu
        public void FindIDDV() {
        try {
            cStmt = (CallableStatement) conn.prepareCall("{call FINDTen_DICHVU(?)}");
            cStmt.setString(1, (String)jComboBoxDV.getSelectedItem());
            ResultSet rs = cStmt.executeQuery();
            //Lay id tu combobox
            if(rs.next()) {
                jTextFieldMDV.setText(rs.getString("MaDVu"));
            }
       } catch (SQLException e) {}
}
    //Ham lich su khach hang 
        public void HistoryCustomer() {
        KetQua.setText("");
        defaultTableModel = new DefaultTableModel() {
        public boolean isCellEdittable(int row, int column) {
                return false;
            }
    };
         if ( jTextFieldNhap.getText().trim().equals("") ) {
            jTextFieldNhap.setBackground(Color.BLUE);
            JOptionPane.showMessageDialog(BillFr.this, "Vui lòng nhập tên khách hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            jTextFieldNhap.setBackground(Color.WHITE);
         } else {
        TableCTHD.setModel(defaultTableModel);
        defaultTableModel.addColumn("Mã Hóa Đơn");
        defaultTableModel.addColumn("Tên Dịch Vụ");
        defaultTableModel.addColumn("Giá");
        defaultTableModel.addColumn("Ngày Sử Dụng");
        try {
            cStmt = (CallableStatement) conn.prepareCall("{call history_customer(?)}");
            cStmt.setString(1, jTextFieldNhap.getText());
            ResultSet rs = cStmt.executeQuery();
            List<HistoryCustomer> historycustomer = new ArrayList<HistoryCustomer>();
        while (rs.next()) {
            HistoryCustomer historycustomers = new HistoryCustomer();
            historycustomers.setMaHDon(rs.getString("mhd"));
            historycustomers.settenDvu(rs.getString("dv"));
            historycustomers.setGia(rs.getInt("g"));
            historycustomers.setNgaySD(rs.getDate("ngay"));
            historycustomer.add(historycustomers);
            }
        for (HistoryCustomer historycustomers : historycustomer) {
            defaultTableModel.addRow(new Object[]{historycustomers.getMaHDon(),historycustomers.gettenDvu(), 
                                                  historycustomers.getGia(), historycustomers.getNgaySD()
            });
    }
        jTextFieldNhap.setText("");
        } catch (SQLException e) {}
    }
}
    //Ham xoa thanh toan 
        public void DelBill() {
        int row = TableBill.getSelectedRow();
        if ( row == -1 ) {
            JOptionPane.showMessageDialog(BillFr.this, "Vui lòng chọn thanh toán cần xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(BillFr.this, "Bạn chắc chắc muốn xóa không?");
            
            if (confirm == JOptionPane.YES_OPTION) {
                String mattoan = String.valueOf(TableBill.getValueAt(row, 0));
                try {
                     cStmt = (CallableStatement) conn.prepareCall("{call del_ttoan(?)}");
                     cStmt.setString(1, mattoan);
                     ResultSet rs = cStmt.executeQuery();
                } catch (SQLException e) {
                }    
                defaultTableModel.setRowCount(0);
                setTableData();
        }
    }
}
    //Ham thong ke hoa don
        public void StatisBill() {
        thongkehoadon();
        Font font1 = new Font("SansSerif", Font.BOLD, 20);
        try {
            cStmt = (CallableStatement) conn.prepareCall("{call sum_thongke(?)}");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(jDateChooserTK.getDate());
            cStmt.setString(1, date);
            ResultSet rs = cStmt.executeQuery();
            //Lay du lieu tu combobox
            if(rs.next()) {
                ThongKe.setText(rs.getString("thongke")+ " VND");
            }
            ThongKe.setFont(font1);
            ThongKe.setForeground(Color.BLUE);
       } catch (SQLException e) {}
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        madichvu = new javax.swing.JLabel();
        jButtonAdd = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableBill = new javax.swing.JTable();
        ButtonTongHD = new javax.swing.JButton();
        tongtien = new javax.swing.JLabel();
        ngaythanhtoan = new javax.swing.JLabel();
        banghoadon = new javax.swing.JLabel();
        jTextFieldID = new javax.swing.JTextField();
        bangcthoadon = new javax.swing.JLabel();
        mahoadon = new javax.swing.JLabel();
        manhanvien = new javax.swing.JLabel();
        jTextFieldMaHD = new javax.swing.JTextField();
        makhachhang = new javax.swing.JLabel();
        jComboBoxDV = new javax.swing.JComboBox<>();
        jComboBoxNV = new javax.swing.JComboBox<>();
        jComboBoxKH = new javax.swing.JComboBox<>();
        TaiLai = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableCTHD = new javax.swing.JTable();
        ThongKe = new javax.swing.JTextField();
        jDateChooserNTT = new com.toedter.calendar.JDateChooser();
        xoa1 = new javax.swing.JButton();
        jTextFieldMDV = new javax.swing.JTextField();
        jTextFieldMKH = new javax.swing.JTextField();
        jTextFieldMNV = new javax.swing.JTextField();
        ButtonLSKH = new javax.swing.JButton();
        ButtonCTHD = new javax.swing.JButton();
        ngaythanhtoan1 = new javax.swing.JLabel();
        ngaythanhtoan2 = new javax.swing.JLabel();
        jTextFieldNhap = new javax.swing.JTextField();
        KetQua = new javax.swing.JTextField();
        jDateChooserTK = new com.toedter.calendar.JDateChooser();
        jButtonThongKe = new javax.swing.JButton();
        ButtonSale = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BILL");
        getContentPane().setLayout(new java.awt.CardLayout());

        madichvu.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        madichvu.setText("Dịch Vụ:");

        jButtonAdd.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonAdd.setText("Thêm");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        TableBill.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "T1", "T2", "T3", "T4", "T5"
            }
        ));
        jScrollPane1.setViewportView(TableBill);

        ButtonTongHD.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        ButtonTongHD.setText("Tổng Hóa Đơn");
        ButtonTongHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonTongHDActionPerformed(evt);
            }
        });

        tongtien.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        tongtien.setText("Định Danh Hóa Đơn:");

        ngaythanhtoan.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        ngaythanhtoan.setText("Ngày Thanh Toán:");

        banghoadon.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        banghoadon.setForeground(new java.awt.Color(255, 0, 0));
        banghoadon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        banghoadon.setText("BẢNG HÓA ĐƠN");

        bangcthoadon.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        bangcthoadon.setForeground(new java.awt.Color(255, 0, 0));
        bangcthoadon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bangcthoadon.setText("CHI TIẾT");

        mahoadon.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        mahoadon.setText("Mã Thanh Toán:");

        manhanvien.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        manhanvien.setText("Nhân Viên:");

        makhachhang.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        makhachhang.setText("Khách Hàng:");

        jComboBoxDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDVActionPerformed(evt);
            }
        });

        jComboBoxNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNVActionPerformed(evt);
            }
        });

        jComboBoxKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxKHActionPerformed(evt);
            }
        });

        TaiLai.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        TaiLai.setText("Tải Lại");
        TaiLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaiLaiActionPerformed(evt);
            }
        });

        TableCTHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TableCTHD.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane3.setViewportView(TableCTHD);

        xoa1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        xoa1.setText("Xóa");
        xoa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xoa1ActionPerformed(evt);
            }
        });

        ButtonLSKH.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        ButtonLSKH.setText("Lịch Sử Khách Hàng");
        ButtonLSKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonLSKHActionPerformed(evt);
            }
        });

        ButtonCTHD.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        ButtonCTHD.setText("Chi Tiết Hóa Đơn");
        ButtonCTHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ButtonCTHDMouseClicked(evt);
            }
        });

        ngaythanhtoan1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        ngaythanhtoan1.setText("Chọn Ngày Xem:");

        ngaythanhtoan2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        ngaythanhtoan2.setText("Nhập Tại Đây:");

        jButtonThongKe.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonThongKe.setText("Thống Kê");
        jButtonThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThongKeActionPerformed(evt);
            }
        });

        ButtonSale.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        ButtonSale.setText("Giảm Giá Hóa Đơn");
        ButtonSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSaleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1234, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
                        .addComponent(jScrollPane1)
                        .addComponent(banghoadon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bangcthoadon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(xoa1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(makhachhang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(madichvu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tongtien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addGap(2, 2, 2)
                                    .addComponent(manhanvien, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(ngaythanhtoan, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(ngaythanhtoan2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(mahoadon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ngaythanhtoan1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextFieldMaHD, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jComboBoxKH, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextFieldMKH, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTextFieldMNV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jComboBoxNV, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TaiLai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldID, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jComboBoxDV, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldMDV, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jDateChooserNTT, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jDateChooserTK, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(ButtonCTHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ButtonTongHD, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(ButtonLSKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ButtonSale, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(ThongKe)
                                .addComponent(KetQua, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 576, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(7, 7, 7)
                            .addComponent(TaiLai)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(mahoadon, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(manhanvien, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldMNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(makhachhang, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldMKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(madichvu, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldMDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tongtien, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(ngaythanhtoan, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jDateChooserNTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButtonAdd)
                                .addComponent(xoa1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(banghoadon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(22, 22, 22)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(ngaythanhtoan1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jDateChooserTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextFieldNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ngaythanhtoan2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(8, 8, 8)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ButtonCTHD)
                                .addComponent(ButtonLSKH))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ButtonTongHD)
                                .addComponent(ButtonSale))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(KetQua, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(bangcthoadon, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap()))
        );

        jScrollPane2.setViewportView(jPanel1);

        getContentPane().add(jScrollPane2, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        AddBill();
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jComboBoxKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxKHActionPerformed
        FindIDKH();
    }//GEN-LAST:event_jComboBoxKHActionPerformed

    private void TaiLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaiLaiActionPerformed
        refreshData();
    }//GEN-LAST:event_TaiLaiActionPerformed

    private void ButtonTongHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonTongHDActionPerformed
        SumBill();
    }//GEN-LAST:event_ButtonTongHDActionPerformed

    private void jComboBoxNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxNVActionPerformed
        FindIDNV();
    }//GEN-LAST:event_jComboBoxNVActionPerformed

    private void jComboBoxDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDVActionPerformed
        FindIDDV();
    }//GEN-LAST:event_jComboBoxDVActionPerformed

    private void ButtonLSKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonLSKHActionPerformed
        HistoryCustomer();
    }//GEN-LAST:event_ButtonLSKHActionPerformed

    private void xoa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xoa1ActionPerformed
        DelBill();
    }//GEN-LAST:event_xoa1ActionPerformed

    private void ButtonSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSaleActionPerformed
        SaleBill();
    }//GEN-LAST:event_ButtonSaleActionPerformed

    private void ButtonCTHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonCTHDMouseClicked
        ViewBillDetail();
    }//GEN-LAST:event_ButtonCTHDMouseClicked

    private void jButtonThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonThongKeActionPerformed
        StatisBill();
    }//GEN-LAST:event_jButtonThongKeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BillFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BillFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BillFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BillFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BillFr().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonCTHD;
    private javax.swing.JButton ButtonLSKH;
    private javax.swing.JButton ButtonSale;
    private javax.swing.JButton ButtonTongHD;
    private javax.swing.JTextField KetQua;
    private javax.swing.JTable TableBill;
    private javax.swing.JTable TableCTHD;
    private javax.swing.JButton TaiLai;
    private javax.swing.JTextField ThongKe;
    private javax.swing.JLabel bangcthoadon;
    private javax.swing.JLabel banghoadon;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonThongKe;
    private javax.swing.JComboBox<String> jComboBoxDV;
    private javax.swing.JComboBox<String> jComboBoxKH;
    private javax.swing.JComboBox<String> jComboBoxNV;
    private com.toedter.calendar.JDateChooser jDateChooserNTT;
    private com.toedter.calendar.JDateChooser jDateChooserTK;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextFieldID;
    private javax.swing.JTextField jTextFieldMDV;
    private javax.swing.JTextField jTextFieldMKH;
    private javax.swing.JTextField jTextFieldMNV;
    private javax.swing.JTextField jTextFieldMaHD;
    private javax.swing.JTextField jTextFieldNhap;
    private javax.swing.JLabel madichvu;
    private javax.swing.JLabel mahoadon;
    private javax.swing.JLabel makhachhang;
    private javax.swing.JLabel manhanvien;
    private javax.swing.JLabel ngaythanhtoan;
    private javax.swing.JLabel ngaythanhtoan1;
    private javax.swing.JLabel ngaythanhtoan2;
    private javax.swing.JLabel tongtien;
    private javax.swing.JButton xoa1;
    // End of variables declaration//GEN-END:variables
}
