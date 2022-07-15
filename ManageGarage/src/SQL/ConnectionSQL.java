/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author PC
 */
public class ConnectionSQL { 
    public static Connection getConnection() {
        Connection conn = null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        
        conn = DriverManager.getConnection("jdbc:mysql://localhost/managegarage?"
                                    +"user=root");
    } catch (Exception ex) {
        System.out.println("Noi ket khong thanh cong");
        ex.printStackTrace();
        }  
        return conn;
    }
}
