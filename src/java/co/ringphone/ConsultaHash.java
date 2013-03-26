/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ringphone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luis
 */


public class ConsultaHash {
// database URL
//    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/autodialer?user=autodialer&password=aut0d14l3rs3rv1c3s";
    
    int idclave;
    String hash="";
            static final String DATABASE_URL = "jdbc:mysql://localhost:3306/ringpa_user";

// launch the application
    public ConsultaHash(int idclave) {
        this.idclave=idclave;
    }
    
    public ConsultaHash(String hash) {
//        this.idclave=idclave;
        this.hash=hash;
    }
    public String consultar(){

        
        Connection conn = null; // manages connection
        PreparedStatement statement = null; // query statement
        ResultSet resultSet = null; // manages results
        String idsos=null;
// connect to database books and query database
        try {
            try {
                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                } catch (InstantiationException ex) {
                    Logger.getLogger(ConsultaHash.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(ConsultaHash.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConsultaHash.class.getName()).log(Level.SEVERE, null, ex);
            }

// establish connection to database
            // establish connection to database
                conn = DriverManager.getConnection(
                        DATABASE_URL, "RINGPA_DB", "IeF5Poh1*1&");
//            connection = DriverManager.getConnection(
//                    DATABASE_URL);
// create Statement for querying database
            if(this.hash.equals("")){
            statement = conn.prepareStatement("SELECT `extension_original`FROM `usuario` WHERE `id_usuario`=?" );
            statement.setInt(1, this.idclave);
            }
//            {
//            resultSet = statement.executeQuery(
//             "SELECT `extension_original`FROM `usuario` WHERE `id_usuario`="+ this.idclave );
//            "SELECT id_sosing FROM  `llamada_clave200` WHERE((`id_clave`  ="+ idclave +"))" );
//            }
            else{
//                statement = connection.prepareStatement("SELECT `extension_original`FROM `usuario` WHERE `id_usuario`=?" );
            
                statement = conn.prepareStatement("SELECT `extension_original`FROM `usuario` WHERE `extension_md5`=?" );
                statement.setString(1, this.hash);
//                statement.setString(2, );
            }
            resultSet = statement.executeQuery();

// process query results
ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
//            System.out.println("Authors Table of Books Database:\n");
//            for (int i = 1; i <= numberOfColumns; i++) {
//                System.out.printf("%-8s\t", metaData.getColumnName(i));
//            }
//            System.out.println();
            
            while (resultSet.next()) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    idsos=resultSet.getObject(i).toString();
//                    System.out.printf("%-8s\t", resultSet.getObject(i));
                }
                
//                System.out.println();
            } // end while
//            System.out.println(idsos);
        } // end try
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } // end catch
        finally // ensure resultSet, statement and connection are closed
        {
            try {
                resultSet.close();
                statement.close();
                conn.close();
            } // end try
            catch (Exception exception) {
                exception.printStackTrace();
            } // end catch
        } // end finally
        return idsos;
    } // end main
    
          public static void main(String[] args) {

        ConsultaHash con = new ConsultaHash("c9e1074f5b3f9fc8ea15d152add07294");
//        ConsultaHash con = new ConsultaHash(4);
        String idsos=con.consultar();
        System.out.println(idsos);
        if(   idsos == null) {
                  System.out.println("es nulo");
              }
    }
} // end class DisplayAuthors
