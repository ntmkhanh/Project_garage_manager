/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managegarage;

import Model.Employee;
import SQL.ConnectionSQL;
import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class EmployeeFr extends javax.swing.JFrame {
    DefaultTableModel defaultTableModel;
    CallableStatement cStmt = null;
    Connection conn = ConnectionSQL.getConnection();
    public EmployeeFr() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTableData();
}
    //Them du lieu vao bang nhan vien
    private void setTableData() {
    List<Employee> employees = new ArrayList<Employee>();
    defaultTableModel = new DefaultTableModel() {
            public boolean isCellEdittable(int row, int column) {
                return false;
            }
};
        TableEmployees.setModel(defaultTableModel);
        defaultTableModel.addColumn("Mã Nhân Viên");
        defaultTableModel.addColumn("Họ Tên");
        defaultTableModel.addColumn("Ngày Sinh");
        defaultTableModel.addColumn("Số Điện Thoại");
          
        try {
            String sql = "SELECT * FROM NHANVIEN";  
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            while(rs.next()) {
                Employee employee = new Employee();
                employee.setMaNvien(rs.getString("MaNVien"));
                employee.setHoTen(rs.getString("HoTen"));
                employee.setNgaySinh(rs.getDate("NgaySinh"));
                employee.setSoDT(rs.getString("SoDT"));
                employees.add(employee);
            }
            for (Employee employee : employees) {
            defaultTableModel.addRow(new Object[]{employee.getMaNvien(), employee.getHoTen()
                                        , employee.getNgaySinh(),employee.getSoDT()});
    }
        } catch (SQLException e) {}     
}
    //Ham tai lai du lieu
        private void refreshData() {
        defaultTableModel.setRowCount(0);
        setTableData();
        jTextFieldMaNVien.setText("");
        jTextFieldHoTen.setText("");
        jTextFieldSDT.setText("");
        jTextFieldSearch.setText("");    
    }
    //Ham them nhan vien
        private void addEmp() {
        //Kiểm tra điều kiện mã nhân viên
        if (!jTextFieldMaNVien.getText().trim().equals("")) {
            while (true) {
                if (!jTextFieldMaNVien.getText().trim().matches("^[0-9]{5}$")) {
                    JOptionPane.showMessageDialog(this, "Không đúng định dạng mã ! Ví dụ : xxxxx ");
                    jTextFieldMaNVien.grabFocus();
                    return;
                } else {
                    break;
                }
            }
        }
        // Kiểm tra điều kiện họ tên
         while (true) {
            if (jTextFieldHoTen.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Họ và tên không được để trống !");
                jTextFieldHoTen.grabFocus();
                return;
            } else if (jTextFieldHoTen.getText().trim().length() > 30) {
                JOptionPane.showMessageDialog(this, "Tên không được lớn hơn 50 kí tự ! ");
                jTextFieldHoTen.grabFocus();
                return;
            }
            {
                break;
            }
}
         //Kiểm tra điều kiện SDT 
        if (!jTextFieldSDT.getText().trim().equals("")) {
            while (true) {
                if (!jTextFieldSDT.getText().trim().matches("^[0-9]{3}-[0-9]{7}$")) {
                    JOptionPane.showMessageDialog(this, "Không đúng định dạng SĐT ! Ví dụ : xxx-xxxxxxx ");
                    jTextFieldSDT.grabFocus();
                    return;
                } else {
                    break;
                }
            }
        }
        // Gọi thủ tục thêm nhân viên 
        try {
            cStmt = (CallableStatement) conn.prepareCall("{call them_nv(?, ?, ?, ?)}");
            cStmt.setString(1, jTextFieldMaNVien.getText());
            cStmt.setString(2, jTextFieldHoTen.getText());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(jDateChooser1.getDate());
            cStmt.setString(3, date);
        
            cStmt.setString(4, jTextFieldSDT.getText());
            
            int rs = cStmt.executeUpdate();
            System.out.println(rs);
            defaultTableModel.setRowCount(0);
            setTableData();
            JOptionPane.showMessageDialog(EmployeeFr.this, "Thêm nhân viên thành công");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(EmployeeFr.this, "Chưa nhập thông tin nhân viên");
        }
    }
    //Ham cap nhat nhan vien
        private void editEmp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Connection conn = ConnectionSQL.getConnection();
        if (!jTextFieldMaNVien.getText().trim().equals("")) {
            while (true) {
                if (!jTextFieldMaNVien.getText().trim().matches("^[0-9]{5}$")) {
                    JOptionPane.showMessageDialog(this, "Không đúng định dạng mã ! Ví dụ : xxxxx ");
                    jTextFieldMaNVien.grabFocus();
                    return;
                } else {
                    break;
                }
            }
        }
         while (true) {
            if (jTextFieldHoTen.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Họ và tên không được để trống !");
                jTextFieldHoTen.grabFocus();
                return;
            } else if (jTextFieldHoTen.getText().trim().length() > 50) {
                JOptionPane.showMessageDialog(this, "Tên không được lớn hơn 50 kí tự ! ");
                jTextFieldHoTen.grabFocus();
                return;
            }
            {
                break;
            }
}
        if (!jTextFieldSDT.getText().trim().equals("")) {
            while (true) {
                if (!jTextFieldSDT.getText().trim().matches("^[0-9]{3}-[0-9]{7}$")) {
                    JOptionPane.showMessageDialog(this, "Không đúng định dạng SĐT ! Ví dụ : xxx-xxxxxxx ");
                    jTextFieldSDT.grabFocus();
                    return;
                } else {
                    break;
                }
            }
        }
        try {
            cStmt = (CallableStatement) conn.prepareCall("{call update_nvien(?, ?, ?, ?)}");
            jTextFieldMaNVien.setEnabled(false);
            cStmt.setString(1, jTextFieldMaNVien.getText());
            cStmt.setString(2, jTextFieldHoTen.getText());
            cStmt.setString(4, jTextFieldSDT.getText());
            cStmt.setString(3, sdf.format(jDateChooser1.getDate()));
            int rs = cStmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Lưu thành công ! ");
            defaultTableModel.getDataVector().removeAllElements();
            setTableData();
            refreshData();
            jTextFieldMaNVien.setText("");
            jTextFieldHoTen.setText("");
            jTextFieldSDT.setText("");
            jTextFieldMaNVien.setEnabled(true);
        } catch (SQLException e) {}
}
    //Ham xoa nhan vien
        private void delEmp() {
        int row = TableEmployees.getSelectedRow();
        if ( row == -1 ) {
            JOptionPane.showMessageDialog(EmployeeFr.this, "Vui lòng chọn nhân viên trước", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(EmployeeFr.this, "Bạn chắc chắc muốn xóa không?");
            
            if (confirm == JOptionPane.YES_OPTION) {
                String manvien = String.valueOf(TableEmployees.getValueAt(row, 0));
                try {
                     cStmt = (CallableStatement) conn.prepareCall("{call del_nvien(?)}");
                     cStmt.setString(1, jTextFieldMaNVien.getText());
                     ResultSet rs = cStmt.executeQuery();
                } catch (SQLException e) {}    
                jTextFieldMaNVien.setText("");
                jTextFieldHoTen.setText("");
                jTextFieldSDT.setText("");
                defaultTableModel.setRowCount(0);
                setTableData();
            }
        }
    }
    //Ham tim kiem nhan vien
        private void searchEmp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (jRadioButtonMaNVien.isSelected()) {
            if (!jTextFieldSearch.getText().trim().equals("")) {
                while (true) {
                    if (!jTextFieldSearch.getText().trim().matches("^[0-9]{5}$")) {
                        JOptionPane.showMessageDialog(this, "Không đúng định dạng mã ! Ví dụ : xxxxx ");
                        jTextFieldSearch.grabFocus();
                    return;
                } else {
                    break;
                }
            }
        }
            try {
                cStmt = (CallableStatement) conn.prepareCall("{call FINDID_NHANVIEN(?)}");
                cStmt.setString(1, jTextFieldSearch.getText());
                ResultSet rs = cStmt.executeQuery();
               
                if(rs.next()) {
                     jTextFieldMaNVien.setText(rs.getString("MaNVien"));
                     jTextFieldHoTen.setText(rs.getString("HoTen"));
                     jDateChooser1.setDate(rs.getDate("NgaySinh"));
                     jTextFieldSDT.setText(rs.getString("SoDT"));
                } else {
                    JOptionPane.showMessageDialog(EmployeeFr.this, "Không tìm thấy mã nhân viên", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
        } catch (SQLException e) {}
        } else if (jRadioButtonHoTen.isSelected()) {
            while (true) {
            if (jTextFieldSearch.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Họ và tên không được để trống !");
                jTextFieldSearch.grabFocus();
                return;
            } else if (jTextFieldSearch.getText().trim().length() > 30) {
                JOptionPane.showMessageDialog(this, "Tên không được lớn hơn 50 kí tự ! ");
                jTextFieldSearch.grabFocus();
                return;
            }
            {
                break;
            }
}
            try {
                cStmt = (CallableStatement) conn.prepareCall("{call FINDTen_NHANVIEN(?)}");
                cStmt.setString(1, jTextFieldSearch.getText());
                ResultSet rs = cStmt.executeQuery();
               
                if(rs.next()) {
                     jTextFieldMaNVien.setText(rs.getString("MaNVien"));
                     jTextFieldHoTen.setText(rs.getString("HoTen"));
                     jDateChooser1.setDate(rs.getDate("NgaySinh"));
                     jTextFieldSDT.setText(rs.getString("SoDT"));
                } else {
                    JOptionPane.showMessageDialog(EmployeeFr.this, "Không tìm thấy tên nhân viên", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
        } catch (SQLException e) {}
        } else {
            JOptionPane.showMessageDialog(EmployeeFr.this, "Không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        THONGTINNHANVIEN = new javax.swing.JPanel();
        IDNHANVIEN = new javax.swing.JLabel();
        jTextFieldMaNVien = new javax.swing.JTextField();
        HOTEN = new javax.swing.JLabel();
        jTextFieldHoTen = new javax.swing.JTextField();
        NGAYSINH = new javax.swing.JLabel();
        SODT = new javax.swing.JLabel();
        jTextFieldSDT = new javax.swing.JTextField();
        jButtonThem = new javax.swing.JButton();
        jButtonCapNhat = new javax.swing.JButton();
        jButtonXoa = new javax.swing.JButton();
        jLabelTKNV = new javax.swing.JLabel();
        jRadioButtonMaNVien = new javax.swing.JRadioButton();
        jRadioButtonHoTen = new javax.swing.JRadioButton();
        jTextFieldSearch = new javax.swing.JTextField();
        jButtonTimKiem = new javax.swing.JButton();
        jButtonTaiLai = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableEmployees = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EMPLOYEE");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        THONGTINNHANVIEN.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "THÔNG TIN NHÂN VIÊN        ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(102, 153, 255))); // NOI18N

        IDNHANVIEN.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        IDNHANVIEN.setText("Mã Nhân Viên:");

        HOTEN.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        HOTEN.setText("Họ Tên:");
        HOTEN.setPreferredSize(new java.awt.Dimension(76, 14));

        NGAYSINH.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        NGAYSINH.setText("Ngày Sinh:");

        SODT.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        SODT.setText("Số ĐT:");

        jButtonThem.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonThem.setText("Thêm");
        jButtonThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThemActionPerformed(evt);
            }
        });

        jButtonCapNhat.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonCapNhat.setText("Cập Nhật");
        jButtonCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCapNhatActionPerformed(evt);
            }
        });

        jButtonXoa.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonXoa.setText("Xóa");
        jButtonXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXoaActionPerformed(evt);
            }
        });

        jLabelTKNV.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelTKNV.setText("TÌM KIẾM NHÂN VIÊN");

        buttonGroup1.add(jRadioButtonMaNVien);
        jRadioButtonMaNVien.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jRadioButtonMaNVien.setText("Mã Nhân Viên");

        buttonGroup1.add(jRadioButtonHoTen);
        jRadioButtonHoTen.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jRadioButtonHoTen.setText("Họ Tên");

        jButtonTimKiem.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonTimKiem.setText("Tìm Kiếm");
        jButtonTimKiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonTimKiemMouseClicked(evt);
            }
        });

        jButtonTaiLai.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonTaiLai.setText("Tải lại");
        jButtonTaiLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTaiLaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout THONGTINNHANVIENLayout = new javax.swing.GroupLayout(THONGTINNHANVIEN);
        THONGTINNHANVIEN.setLayout(THONGTINNHANVIENLayout);
        THONGTINNHANVIENLayout.setHorizontalGroup(
            THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(THONGTINNHANVIENLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(THONGTINNHANVIENLayout.createSequentialGroup()
                        .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(HOTEN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(IDNHANVIEN)
                            .addComponent(NGAYSINH)
                            .addComponent(SODT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(THONGTINNHANVIENLayout.createSequentialGroup()
                                .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldHoTen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                    .addComponent(jTextFieldMaNVien, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldSDT))
                                .addGap(135, 135, 135)
                                .addComponent(jLabelTKNV, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(THONGTINNHANVIENLayout.createSequentialGroup()
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(114, 114, 114)
                                .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(THONGTINNHANVIENLayout.createSequentialGroup()
                                        .addComponent(jRadioButtonMaNVien)
                                        .addGap(67, 67, 67)
                                        .addComponent(jRadioButtonHoTen))))))
                    .addGroup(THONGTINNHANVIENLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButtonThem, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, THONGTINNHANVIENLayout.createSequentialGroup()
                                .addGap(418, 418, 418)
                                .addComponent(jButtonTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(136, 136, 136))
                            .addGroup(THONGTINNHANVIENLayout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jButtonCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(jButtonXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(jButtonTaiLai, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        THONGTINNHANVIENLayout.setVerticalGroup(
            THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(THONGTINNHANVIENLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(THONGTINNHANVIENLayout.createSequentialGroup()
                        .addComponent(jLabelTKNV, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButtonMaNVien)
                            .addComponent(jRadioButtonHoTen)))
                    .addGroup(THONGTINNHANVIENLayout.createSequentialGroup()
                        .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(IDNHANVIEN, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldMaNVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(HOTEN, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(THONGTINNHANVIENLayout.createSequentialGroup()
                                .addComponent(NGAYSINH, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(SODT, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButtonTaiLai)
                                .addComponent(jButtonTimKiem))
                            .addGroup(THONGTINNHANVIENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButtonXoa)
                                .addComponent(jButtonCapNhat)
                                .addComponent(jButtonThem)))))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        TableEmployees.setModel(new javax.swing.table.DefaultTableModel(
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
        TableEmployees.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableEmployeesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableEmployees);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 782, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(THONGTINNHANVIEN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1))
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 456, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(THONGTINNHANVIEN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        jScrollPane2.setViewportView(jPanel1);

        getContentPane().add(jScrollPane2);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonThemActionPerformed
        addEmp();
    }//GEN-LAST:event_jButtonThemActionPerformed

    private void jButtonCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCapNhatActionPerformed
        editEmp();
    }//GEN-LAST:event_jButtonCapNhatActionPerformed

    private void jButtonXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXoaActionPerformed
        delEmp();
    }//GEN-LAST:event_jButtonXoaActionPerformed

    private void jButtonTaiLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTaiLaiActionPerformed
        refreshData();
    }//GEN-LAST:event_jButtonTaiLaiActionPerformed

    private void TableEmployeesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableEmployeesMouseClicked
        DefaultTableModel tblModel = (DefaultTableModel)TableEmployees.getModel();
        int index = TableEmployees.getSelectedRow();
        if ( index == -1 ) {
            jTextFieldMaNVien.setText("");
            jTextFieldHoTen.setText("");
            jTextFieldSDT.setText("");
        } else {
        String tblMaNVien = tblModel.getValueAt(index, 0).toString();
        String tblHoTen = tblModel.getValueAt(index, 1).toString();
        // Xử lý thông tin kiểu ngày 
        try {
            String tblNgaySinh = tblModel.getValueAt(index, 2).toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse(tblNgaySinh);
            jDateChooser1.setDate(date);                   
        } catch (ParseException ex) {
            Logger.getLogger(EmployeeFr.class.getName()).log(Level.SEVERE, null, ex);
        }
        String tblSDT = tblModel.getValueAt(index, 3).toString();
        // Đổ thông tin vào các textfield
       jTextFieldMaNVien.setText(tblMaNVien);
       jTextFieldHoTen.setText(tblHoTen);
       jTextFieldSDT.setText(tblSDT);
}
    }//GEN-LAST:event_TableEmployeesMouseClicked

    private void jButtonTimKiemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonTimKiemMouseClicked
        searchEmp();
    }//GEN-LAST:event_jButtonTimKiemMouseClicked

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
            java.util.logging.Logger.getLogger(EmployeeFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmployeeFr().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel HOTEN;
    private javax.swing.JLabel IDNHANVIEN;
    private javax.swing.JLabel NGAYSINH;
    private javax.swing.JLabel SODT;
    private javax.swing.JPanel THONGTINNHANVIEN;
    private javax.swing.JTable TableEmployees;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonCapNhat;
    private javax.swing.JButton jButtonTaiLai;
    private javax.swing.JButton jButtonThem;
    private javax.swing.JButton jButtonTimKiem;
    private javax.swing.JButton jButtonXoa;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabelTKNV;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButtonHoTen;
    private javax.swing.JRadioButton jRadioButtonMaNVien;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldHoTen;
    private javax.swing.JTextField jTextFieldMaNVien;
    private javax.swing.JTextField jTextFieldSDT;
    private javax.swing.JTextField jTextFieldSearch;
    // End of variables declaration//GEN-END:variables
}
