/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ringphone;

/**
 *
 * @author luis
 */



import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A Java MySQL UPDATE example.
 * Demonstrates the use of a SQL UPDATE statement against a
 * MySQL database, called from a Java program.
 * 
 * Created by Alvin Alexander, http://devdaily.com
 *
 */
public class UpdateDB
{
        String status = "";
        Date tiempo;
        int idClave;
        
        public UpdateDB(int idClave,Date tiempo){
            this.idClave=idClave;
            this.tiempo=tiempo;
    
}

  public void actualizarLlamadaDB()
  {Connection conn = null;
    try
    {
      

            try {


                Class.forName("com.mysql.jdbc.Driver").newInstance();

                String url = "jdbc:mysql://localhost:3306/autodialer?user=autodialer&password=aut0d14l3rs3rv1c3s";

                conn = DriverManager.getConnection(url);

                status = "Connection opened";

            } catch (SQLException e) {
                status = e.getMessage();
            } catch (ClassNotFoundException e) {

                status = e.getMessage();

            } catch (Exception e) {

                status = e.getMessage();

            }


            System.out.println(status);
String mensaje ="";

            //Criando um objeto Statement para enviar requisições SQL para o Banco de Dados         
//            Statement stmt = conn.createStatement();

    
      // create the java mysql update preparedstatement
      String query = "update llamada_clave200 set inicio_llamada = ? where id_clave = ?";
//      Date fecha = new Date();
     
    java.sql.Date sqlfecha = new java.sql.Date(tiempo.getTime());
    System.out.println("utilDate:" + tiempo);
    System.out.println("sqlDate:" + sqlfecha);
      
      PreparedStatement preparedStmt = conn.prepareStatement(query);
      preparedStmt.setDate(1,sqlfecha);
      preparedStmt.setInt(2, idClave);

      // execute the java preparedstatement
      preparedStmt.executeUpdate();
      
      preparedStmt.close();
      conn.close();
    }
    catch (Exception e)
    {
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
    }
  }
  
      public static void main(String[] args) {
          Date fecha = new Date();
        UpdateDB con = new UpdateDB(1,fecha);
        con.actualizarLlamadaDB();
    }
}