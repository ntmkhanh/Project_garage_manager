package managegarage;

import Model.Customer;
import SQL.ConnectionSQL;
import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 *
 * @NguyenDuyKhangCTU PC
 */
public class CustomerFr extends javax.swing.JFrame {
    DefaultTableModel defaultTableModel;
    CallableStatement cStmt = null;
    Connection conn = ConnectionSQL.getConnection();

    public CustomerFr() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setTableData();
};
    private void setTableData() {
        defaultTableModel = new DefaultTableModel() {
            public boolean isCellEdittable(int row, int column) {
                return false;
            }
    };
        TableCustomer.setModel(defaultTableModel);
        defaultTableModel.addColumn("Ma Khach Hang");
        defaultTableModel.addColumn("Ho Ten Khach Hang");
        defaultTableModel.addColumn("Bien So Xe");
        defaultTableModel.addColumn("So Dien Thoai");
        try {
        String sql = "SELECT * from KHACHHANG";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery(sql);
        List<Customer> customer = new ArrayList<Customer>();
        while (rs.next()) {
            Customer customers = new Customer();
            customers.setMaKHANG(rs.getString("MaKHANG"));
            customers.setTenKH(rs.getString("TENKH"));
            customers.setBienSo(rs.getString("Bienso"));
            customers.setSoDTKH(rs.getString("SoDTKH"));
            customer.add(customers);
            }
        for (Customer customers : customer) {
            defaultTableModel.addRow(new Object[]{customers.getMaKHANG(), customers.getTenKH()
                                        , customers.getBienSo(),customers.getSoDTKH()});
    }
        } catch (SQLException e) {
        }
}    
    //Ham tai lai frame
    private void refreshData() {
        defaultTableModel.setRowCount(0);
        setTableData();
        jTextFieldMaKH.setText("");
        jTextFieldTenKH.setText("");
        jTextFieldSDT.setText("");
        jTextFieldBienSo.setText("");
    }
    //Ham them khach hang
    private void AddCustomer() {
        //Kiem tra dieu kien ma nhan vien
        if (!jTextFieldMaKH.getText().trim().equals("")) {
            while (true) {
                if (!jTextFieldMaKH.getText().trim().matches("^[0-9]{5}$")) {
                    JOptionPane.showMessageDialog(this, "Không đúng định dạng mã ! Ví dụ : xxxxx ");
                    jTextFieldMaKH.grabFocus();
                    return;
                } else {
                    break;
                }
            }
        }
        //Kiem tra dieu kien ho ten
         while (true) {
            if (jTextFieldTenKH.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Họ và tên không được để trống !");
                jTextFieldTenKH.grabFocus();
                return;
            } else if (jTextFieldTenKH.getText().trim().length() > 50) {
                JOptionPane.showMessageDialog(this, "Tên không được lớn hơn 50 kí tự ! ");
                jTextFieldTenKH.grabFocus();
                return;
            }
            {
                break;
            }
}
        //Kiem tra dieu kien SDT
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
        //Kiem tra dieu kien bien so xe
        if (!jTextFieldBienSo.getText().trim().equals("")) {
            while (true) {
                if (!jTextFieldBienSo.getText().trim().matches("^[0-9]{2}[ABCDE]{1}-[0-9]{5}$")) {
                    JOptionPane.showMessageDialog(this, "Không đúng định dạng biển số ! Ví dụ : 64B2-56789 ");
                    jTextFieldBienSo.grabFocus();
                    return;
                } else {
                    break;
                }
            }
        }
        try {
            cStmt = (CallableStatement) conn.prepareCall("{call them_kh(?, ?, ?, ?)}");
            cStmt.setString(1, jTextFieldMaKH.getText());
            cStmt.setString(2, jTextFieldTenKH.getText());
            cStmt.setString(3, jTextFieldBienSo.getText());
            cStmt.setString(4, jTextFieldSDT.getText());
            
            int rs = cStmt.executeUpdate();
            System.out.println(rs);
            defaultTableModel.setRowCount(0);
            setTableData();
            JOptionPane.showMessageDialog(CustomerFr.this, "Thêm khách hàng thành công");
            jTextFieldMaKH.setText("");
            jTextFieldTenKH.setText("");
            jTextFieldBienSo.setText("");
            jTextFieldSDT.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(CustomerFr.this, "Chưa nhập thông tin khách hàng");
        }
}
    //Ham sua khach hang
    private void EditCustomer() {
        try {
            cStmt = (CallableStatement) conn.prepareCall("{call update_khang(?, ?, ?, ?)}");
            jTextFieldMaKH.setEnabled(false);
            cStmt.setString(1, jTextFieldMaKH.getText());
            cStmt.setString(2, jTextFieldTenKH.getText());
            cStmt.setString(4, jTextFieldSDT.getText());
            cStmt.setString(3, jTextFieldBienSo.getText());            
            int rs = cStmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Lưu thành công ! ");
            defaultTableModel.getDataVector().removeAllElements();
            setTableData();
            refreshData();
            //jButtonRefreshActionPerformed(evt);
            jTextFieldBienSo.setText("");
            jTextFieldTenKH.setText("");
            jTextFieldSDT.setText("");
            jTextFieldMaKH.setEnabled(true);
        } catch (SQLException e) {}
    }
    //Ham xoa khach hang
    private void DelCustomer() {
        int row = TableCustomer.getSelectedRow();
        if ( row == -1 ) {
            JOptionPane.showMessageDialog(CustomerFr.this, "Vui lòng chọn khách hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(CustomerFr.this, "Bạn chắc chắc muốn xóa không?");
            
            if (confirm == JOptionPane.YES_OPTION) {
                String makhang = String.valueOf(TableCustomer.getValueAt(row, 0));
                try {
                     cStmt = (CallableStatement) conn.prepareCall("{call del_khang(?)}");
                     cStmt.setString(1, jTextFieldMaKH.getText());
                     ResultSet rs = cStmt.executeQuery();
                } catch (SQLException e) {}       
                jTextFieldMaKH.setText("");
                jTextFieldTenKH.setText("");
                jTextFieldSDT.setText("");
                jTextFieldBienSo.setText("");
                defaultTableModel.setRowCount(0);
                setTableData();
            }
        }
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
        THONGTINKHACHHANG = new javax.swing.JPanel();
        MAKHACHHANG = new javax.swing.JLabel();
        TENKHACHHANG = new javax.swing.JLabel();
        MAXE = new javax.swing.JLabel();
        NGAYNHANXE = new javax.swing.JLabel();
        jTextFieldMaKH = new javax.swing.JTextField();
        jTextFieldTenKH = new javax.swing.JTextField();
        jTextFieldBienSo = new javax.swing.JTextField();
        jTextFieldSDT = new javax.swing.JTextField();
        jButtonAdd = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableCustomer = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CUSTOMER");
        getContentPane().setLayout(new java.awt.CardLayout());

        THONGTINKHACHHANG.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "THÔNG TIN KHÁCH HÀNG    ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(102, 153, 255))); // NOI18N

        MAKHACHHANG.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        MAKHACHHANG.setText("Mã Khách Hàng:");

        TENKHACHHANG.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        TENKHACHHANG.setText("Tên Khách Hàng:");

        MAXE.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        MAXE.setText("Biển Số Xe:");

        NGAYNHANXE.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        NGAYNHANXE.setText("Số Điện Thoại:");

        jButtonAdd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonAdd.setText("Thêm");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonEdit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonEdit.setText("Sửa");
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });

        jButtonDelete.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonDelete.setText("Xóa");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonRefresh.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonRefresh.setText("Tải Lại");
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout THONGTINKHACHHANGLayout = new javax.swing.GroupLayout(THONGTINKHACHHANG);
        THONGTINKHACHHANG.setLayout(THONGTINKHACHHANGLayout);
        THONGTINKHACHHANGLayout.setHorizontalGroup(
            THONGTINKHACHHANGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(THONGTINKHACHHANGLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(THONGTINKHACHHANGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(MAKHACHHANG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TENKHACHHANG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MAXE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NGAYNHANXE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(THONGTINKHACHHANGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldMaKH)
                    .addComponent(jTextFieldTenKH)
                    .addComponent(jTextFieldBienSo)
                    .addComponent(jTextFieldSDT))
                .addContainerGap())
            .addGroup(THONGTINKHACHHANGLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jButtonEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(101, Short.MAX_VALUE))
        );
        THONGTINKHACHHANGLayout.setVerticalGroup(
            THONGTINKHACHHANGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(THONGTINKHACHHANGLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(THONGTINKHACHHANGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MAKHACHHANG, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(THONGTINKHACHHANGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TENKHACHHANG, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(THONGTINKHACHHANGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MAXE, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldBienSo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(THONGTINKHACHHANGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NGAYNHANXE, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(THONGTINKHACHHANGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAdd)
                    .addComponent(jButtonEdit)
                    .addComponent(jButtonDelete)
                    .addComponent(jButtonRefresh))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        TableCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "1", "2", "3", "4"
            }
        ));
        TableCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableCustomerMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableCustomer);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 597, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(THONGTINKHACHHANG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1))
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 357, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(THONGTINKHACHHANG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jScrollPane2.setViewportView(jPanel1);

        getContentPane().add(jScrollPane2, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        AddCustomer();
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditActionPerformed
        EditCustomer();
    }//GEN-LAST:event_jButtonEditActionPerformed

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshActionPerformed
        refreshData();
    }//GEN-LAST:event_jButtonRefreshActionPerformed

    private void TableCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableCustomerMouseClicked
        DefaultTableModel tblModel = (DefaultTableModel)TableCustomer.getModel();
        String tblMaKHANG = tblModel.getValueAt(TableCustomer.getSelectedRow(), 0).toString();
        String tblTenKH = tblModel.getValueAt(TableCustomer.getSelectedRow(), 1).toString();
        String tblBienSo = tblModel.getValueAt(TableCustomer.getSelectedRow(), 2).toString();
        String tblSDT = tblModel.getValueAt(TableCustomer.getSelectedRow(), 3).toString();
        //Do du lieu vao cac TextField
        jTextFieldMaKH.setText(tblMaKHANG);
        jTextFieldTenKH.setText(tblTenKH);
        jTextFieldBienSo.setText(tblBienSo);
        jTextFieldSDT.setText(tblSDT); 
    }//GEN-LAST:event_TableCustomerMouseClicked

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        DelCustomer();
    }//GEN-LAST:event_jButtonDeleteActionPerformed

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
            java.util.logging.Logger.getLogger(CustomerFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomerFr().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel MAKHACHHANG;
    private javax.swing.JLabel MAXE;
    private javax.swing.JLabel NGAYNHANXE;
    private javax.swing.JLabel TENKHACHHANG;
    private javax.swing.JPanel THONGTINKHACHHANG;
    private javax.swing.JTable TableCustomer;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldBienSo;
    private javax.swing.JTextField jTextFieldMaKH;
    private javax.swing.JTextField jTextFieldSDT;
    private javax.swing.JTextField jTextFieldTenKH;
    // End of variables declaration//GEN-END:variables
}
