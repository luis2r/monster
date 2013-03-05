/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ringphone;

/**
 *
 * @author luis
 */
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

public class ConsultaIdSOS {
// database URL
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/autodialer?user=autodialer&password=aut0d14l3rs3rv1c3s";
    String idclave;
// launch the application
    public ConsultaIdSOS(String idclave) {
        this.idclave=idclave;


    }
    public String consultar(){
        
        Connection connection = null; // manages connection
        Statement statement = null; // query statement
        ResultSet resultSet = null; // manages results
        String idsos=null;
// connect to database books and query database
        try {
// establish connection to database
            connection = DriverManager.getConnection(
                    DATABASE_URL);
// create Statement for querying database
            statement = connection.createStatement();
// query database
//            resultSet = statement.executeQuery(
//
//            "SELECT id_sosing FROM  `llamada_clave200` WHERE((`id_clave`  ="+ idclave +"))" );
//            
//// process query results
//ResultSetMetaData metaData = resultSet.getMetaData();
//            int numberOfColumns = metaData.getColumnCount();
////            System.out.println("Authors Table of Books Database:\n");
////            for (int i = 1; i <= numberOfColumns; i++) {
////                System.out.printf("%-8s\t", metaData.getColumnName(i));
////            }
////            System.out.println();
//            
//            while (resultSet.next()) {
//                for (int i = 1; i <= numberOfColumns; i++) {
//                    idsos=resultSet.getObject(i).toString();
////                    System.out.printf("%-8s\t", resultSet.getObject(i));
//                }
//                
////                System.out.println();
//            } // end while
//            System.out.println(idsos);
        } // end try
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } // end catch
        finally // ensure resultSet, statement and connection are closed
        {
            try {
//                resultSet.close();
                statement.close();
                connection.close();
            } // end try
            catch (Exception exception) {
                exception.printStackTrace();
            } // end catch
        } // end finally
        return idsos;
    } // end main
    
          public static void main(String[] args) {

        ConsultaIdSOS con = new ConsultaIdSOS("1");
        String idsos=con.consultar();
      System.out.println(idsos);
    }
} // end class DisplayAuthors
