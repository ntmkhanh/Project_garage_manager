/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managegarage;

import Model.Services;
import SQL.ConnectionSQL;
import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class ServicesFr extends javax.swing.JFrame {
    DefaultTableModel defaultTableModel;
    CallableStatement cStmt = null;
    Connection conn = ConnectionSQL.getConnection();
    /**
     * Creates new form ServicesFr
     */
    public ServicesFr() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTableData();
}
    private void setTableData() {
        List<Services> service = new ArrayList<Services>();
        //Connection conn = ConnectionSQL.getConnection();
        defaultTableModel = new DefaultTableModel() {
            public boolean isCellEdittable(int row, int column) {
                return false;
            }
};
        TableServices.setModel(defaultTableModel);
        defaultTableModel.addColumn("Mã Dịch Vụ");
        //TableServices.getColumn(1).setPreferredWidth(25); 
        defaultTableModel.addColumn("Tên Dịch Vụ");
        defaultTableModel.addColumn("Giá");    
        try {
            String sql = "SELECT * FROM DICHVU";  
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            while(rs.next()) {
                Services services = new Services();
                services.setMaDvu(rs.getString("MaDVU"));
                services.setTenDvu(rs.getString("TENDV"));
                services.setGia(rs.getInt("GIA"));
                service.add(services);
            }
            for (Services services : service) {
            defaultTableModel.addRow(new Object[]{services.getMaDvu(), services.getTenDvu()
                                        , services.getGia()});
    }
        } catch (SQLException e) {
        }     
};
    public void refresh() {
        defaultTableModel.setRowCount(0);
        setTableData();
        jTextFieldMDV.setText("");
        jTextFieldTDV.setText("");
        jTextFieldGia.setText("");
    }
    //Service Mouse Click
    public void serviceClick() {
         DefaultTableModel tblModel = (DefaultTableModel)TableServices.getModel();
        int index = TableServices.getSelectedRow();
        if ( index == -1 ) {
            jTextFieldMDV.setText("");
            jTextFieldTDV.setText("");
            jTextFieldGia.setText("");
        } else {
        String tblMaDVu = tblModel.getValueAt(index, 0).toString();
        String tblTenDV = tblModel.getValueAt(index, 1).toString();
        String tblGia = tblModel.getValueAt(index, 2).toString();
        // Đổ thông tin vào các textfield
       jTextFieldMDV.setText(tblMaDVu);
       jTextFieldTDV.setText(tblTenDV);
       jTextFieldGia.setText(tblGia);
        }
    }
    //Edit Price
    public void editPrice() {
        try {
            cStmt = (CallableStatement) conn.prepareCall("{call update_DICHVU(?, ?)}");
            jTextFieldMDV.setEnabled(false);
            jTextFieldTDV.setEnabled(false);
            cStmt.setString(1, jTextFieldMDV.getText());
            cStmt.setString(2, jTextFieldGia.getText());            
            int rs = cStmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Lưu thành công ! ");
            defaultTableModel.getDataVector().removeAllElements();
            setTableData();
            refresh();
            jTextFieldMDV.setText("");
            jTextFieldTDV.setText("");
            jTextFieldGia.setText("");
            jTextFieldMDV.setEnabled(true);
            jTextFieldTDV.setEnabled(true);
        } catch (SQLException e) {
        }
    }
    //Delete Service
    public void delService() {
        int row = TableServices.getSelectedRow();
        if ( row == -1 ) {
            JOptionPane.showMessageDialog(ServicesFr.this, "Vui lòng chọn thanh toan cần xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(ServicesFr.this, "Bạn chắc chắc muốn xóa không?");
            
            if (confirm == JOptionPane.YES_OPTION) {
                String MaDVU = String.valueOf(TableServices.getValueAt(row, 0));
                try {
                     cStmt = (CallableStatement) conn.prepareCall("{call del_dichvu(?)}");
                     cStmt.setString(1, MaDVU);
                     ResultSet rs = cStmt.executeQuery();
                } catch (SQLException e) {
                }    
                defaultTableModel.setRowCount(0);
                setTableData();
            }
        }
    }
    //Add Service
    public void addService() {
        try {
            cStmt = (CallableStatement) conn.prepareCall("{call them_dv(?, ?, ?)}");
            cStmt.setString(1, jTextFieldMDV.getText());
            cStmt.setString(2, jTextFieldTDV.getText());
            cStmt.setString(3, jTextFieldGia.getText());
            
            int rs = cStmt.executeUpdate();
            System.out.println(rs);
            defaultTableModel.setRowCount(0);
            setTableData();
            JOptionPane.showMessageDialog(ServicesFr.this, "Thêm dịch vụ thành công");
            jTextFieldMDV.setText("");
            jTextFieldTDV.setText("");
            jTextFieldGia.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(ServicesFr.this, "Chưa nhập thông tin dịch vụ hoặc mã dịch vụ đã tồn tại");
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
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        MADICHVU = new javax.swing.JLabel();
        TENDICHVU = new javax.swing.JLabel();
        GIADICHVU = new javax.swing.JLabel();
        jTextFieldMDV = new javax.swing.JTextField();
        jTextFieldTDV = new javax.swing.JTextField();
        jTextFieldGia = new javax.swing.JTextField();
        jButtonThem = new javax.swing.JButton();
        jButtonChinhGia = new javax.swing.JButton();
        jButtonXoa = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableServices = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SERVICES");
        setBackground(new java.awt.Color(255, 51, 255));
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DỊCH VỤ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(102, 153, 255))); // NOI18N

        MADICHVU.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        MADICHVU.setText("Mã Dịch Vụ:");

        TENDICHVU.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        TENDICHVU.setText("Tên Dịch Vụ");

        GIADICHVU.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        GIADICHVU.setText("Giá Dịch Vụ:");

        jButtonThem.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonThem.setText("Thêm");
        jButtonThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThemActionPerformed(evt);
            }
        });

        jButtonChinhGia.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonChinhGia.setText("Chỉnh giá");
        jButtonChinhGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChinhGiaActionPerformed(evt);
            }
        });

        jButtonXoa.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonXoa.setText("Xóa");
        jButtonXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(GIADICHVU)
                            .addComponent(MADICHVU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TENDICHVU, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldMDV, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButtonChinhGia, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldGia, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldTDV, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonThem, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MADICHVU, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMDV, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TENDICHVU, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTDV, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(GIADICHVU, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldGia, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonThem, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonChinhGia, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );

        TableServices.setModel(new javax.swing.table.DefaultTableModel(
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
        TableServices.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        TableServices.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableServicesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableServices);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 579, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 351, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jScrollPane2.setViewportView(jPanel2);

        getContentPane().add(jScrollPane2, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonThemActionPerformed
        addService();
    }//GEN-LAST:event_jButtonThemActionPerformed

    private void jButtonXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXoaActionPerformed
        delService();
    }//GEN-LAST:event_jButtonXoaActionPerformed

    private void TableServicesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableServicesMouseClicked
        serviceClick();
    }//GEN-LAST:event_TableServicesMouseClicked

    private void jButtonChinhGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChinhGiaActionPerformed
        editPrice();
    }//GEN-LAST:event_jButtonChinhGiaActionPerformed

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
            java.util.logging.Logger.getLogger(ServicesFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServicesFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServicesFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServicesFr.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServicesFr().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel GIADICHVU;
    private javax.swing.JLabel MADICHVU;
    private javax.swing.JLabel TENDICHVU;
    private javax.swing.JTable TableServices;
    private javax.swing.JButton jButtonChinhGia;
    private javax.swing.JButton jButtonThem;
    private javax.swing.JButton jButtonXoa;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldGia;
    private javax.swing.JTextField jTextFieldMDV;
    private javax.swing.JTextField jTextFieldTDV;
    // End of variables declaration//GEN-END:variables
}
