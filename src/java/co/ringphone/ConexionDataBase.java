/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ringphone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luis
 */
public class ConexionDataBase {

    static String status = "";
    String origen;
    String destino;
    String idClave;
    int numreintentos;
    int tiempoentrereintento;

    public ConexionDataBase(String idClave, String origen, String destino, int numreintentos, int tiempoentrereintento) {
        this.destino = destino;
        this.idClave = idClave;
        this.numreintentos = numreintentos;
        this.origen = origen;
        this.tiempoentrereintento = tiempoentrereintento;
    }

    public void InsertarLlamadaDataBase() {
        Connection conn = null; // manages connection
Statement stmt = null; // query statement
int rsett;


        try {
//            Connection conn = null;

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
            String mensaje = "";

            //Criando um objeto Statement para enviar requisições SQL para o Banco de Dados         
            stmt = conn.createStatement();
//            int rsett = stmt.executeUpdate("INSERT INTO alarma (`alarma_id`) VALUES ('3333')");
//            int rsett = stmt.executeUpdate("INSERT INTO alarma (`alarma_id`,`mensaje`,`destino`,`numero_reintentos`,`tiempo_reintentos`) VALUES ("+idClave+","+destino+","+origen+","+numreintentos+","+tiempoentrereintento+")");
//            rsett = stmt.executeUpdate("INSERT INTO llamada_clave200 (`id_clave`,`origen`,`destino`,`num_reintentos`,`tiempo_entre_reintento`) VALUES (" + idClave + "," + origen + "," + destino + "," + numreintentos + "," + tiempoentrereintento + ")");

        } 
        catch (SQLException ex) {
            Logger.getLogger(ConexionDataBase.class.getName()).log(Level.SEVERE, null, ex);
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
        ConexionDataBase con = new ConexionDataBase("99999", "102", "104", 1, 1);
        con.InsertarLlamadaDataBase();
    }
}
