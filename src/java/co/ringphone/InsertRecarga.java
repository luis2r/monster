package co.ringphone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author luis
 */


public class InsertRecarga {

    static String status = "";

    String idClave;
    float valorRecarga;
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/autodialer";

    public InsertRecarga(String idClave, float valorRecarga) {

        this.idClave = idClave;
        this.valorRecarga=valorRecarga;
    }

    public void InsertarRecargaDataBase() {
        Connection conn = null; // manages connection
Statement stmt = null; // query statement
int rsett;


        try {
//            Connection conn = null;

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
                conn = DriverManager.getConnection(
                        DATABASE_URL, "autodialer", "aut0d14l3rs3rv1c3s");

//                Class.forName("com.mysql.jdbc.Driver").newInstance();
//
//                String url = "jdbc:mysql://localhost:3306/autodialer?user=autodialer&password=aut0d14l3rs3rv1c3s";
//
//                conn = DriverManager.getConnection(url);

                status = "Connection opened";

            } catch (SQLException e) {
                status = e.getMessage();
            } 
//            catch (ClassNotFoundException e) {
//
//                status = e.getMessage();
//
//            } 
            catch (Exception e) {

                status = e.getMessage();

            }


            System.out.println(status);
            String mensaje = "";

            //Criando um objeto Statement para enviar requisições SQL para o Banco de Dados         
            stmt = conn.createStatement();
//            int rsett = stmt.executeUpdate("INSERT INTO alarma (`alarma_id`) VALUES ('3333')");
//            int rsett = stmt.executeUpdate("INSERT INTO alarma (`alarma_id`,`mensaje`,`destino`,`numero_reintentos`,`tiempo_reintentos`) VALUES ("+idClave+","+destino+","+origen+","+numreintentos+","+tiempoentrereintento+")");
            String query1="INSERT INTO `recarga` (`id_recarga`, `id_usuario`, `fecha_recarga`, `valor_recarga`) VALUES (NULL, (SELECT  `id_usuario` FROM  `usuario` WHERE `usuario`.`extension_md5` = ?), CURRENT_TIMESTAMP,?)";
//            rsett = stmt.executeUpdate("INSERT INTO llamada_clave200 (`id_clave`,`origen`,`destino`,`num_reintentos`,`tiempo_entre_reintento`) VALUES (" + idClave + "," + origen + "," + destino + "," + numreintentos + "," + tiempoentrereintento + ")");

            
            
            
            PreparedStatement ps = conn.prepareStatement(query1);
//            ps.setInt(1, 45);
            ps.setString(1, this.idClave);
            ps.setFloat(2, this.valorRecarga);
//            ps.setString(3, this.destino);
//            ps.setInt(4, this.consumoSegundos);
//            ps.setString(5, this.origen);

            ps.executeUpdate();
            
            
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(InsertRecarga.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally // ensure resultSet, statement and connection are closed
        {
            try {
//                rsett.close();
                stmt.close();
                conn.close();
                System.out.println("conexión cerrada");
            } // end try
            catch (Exception exception) {
                exception.printStackTrace();
            } // end catch
        } // end finally




    }

    public static void main(String[] args) {
        InsertRecarga con = new InsertRecarga("yuyu", 5000);
        con.InsertarRecargaDataBase();
    }
}
