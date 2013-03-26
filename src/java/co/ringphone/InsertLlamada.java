/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ringphone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luis
 */
public class InsertLlamada {

    static String status = "";
//    int idClave;
    String origen;
    String destino;
    String estado;
    int consumoSegundos;
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/ringpa_user";

    public InsertLlamada(String origen, String destino, int consumoSegundos, String estado) {

//        this.idClave = idClave;
        this.origen = origen;
        this.destino = destino;
        this.consumoSegundos = consumoSegundos;
        this.estado = estado;
    }

    public void InsertarLlamadaDataBase() {
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
                        DATABASE_URL, "RINGPA_DB", "IeF5Poh1*1&");
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


            String query1 = "INSERT INTO `llamada_ringphone` (`id_llamada`, `id_usuario`, `origen`, `destino`, `tiempo_llamada_segundos`, `tiempo_llamada_minutos`, `valor_llamada`, `estado`) "
                    + "                                     VALUES (NULL,(SELECT  `id_usuario` FROM  `usuario` WHERE `usuario`.`extension_md5` = ?),?,?,?,CEILING(`tiempo_llamada_segundos`/60), `llamada_ringphone`.`tiempo_llamada_minutos`*(SELECT  `tarifa` FROM  `usuario` WHERE `usuario`.`extension_md5` = ?),?)";
            PreparedStatement ps = conn.prepareStatement(query1);
//            ps.setInt(1, 45);
            ps.setString(1, this.origen);
            ps.setString(2, this.origen);
            ps.setString(3, this.destino);
            ps.setInt(4, this.consumoSegundos);
            ps.setString(5, this.origen);
            ps.setString(6, this.estado);

            ps.executeUpdate();


//            int rsett = stmt.executeUpdate("INSERT INTO alarma (`alarma_id`) VALUES ('3333')");
//            int rsett = stmt.executeUpdate("INSERT INTO alarma (`alarma_id`,`mensaje`,`destino`,`numero_reintentos`,`tiempo_reintentos`) VALUES ("+idClave+","+destino+","+origen+","+numreintentos+","+tiempoentrereintento+")");
//            rsett = stmt.executeUpdate("INSERT INTO `llamada_ringphone` (`id_llamada`, `id_usuario`, `origen`, `destino`, `tiempo_llamada`, `valor_llamada`) VALUES (NULL, "+idClave+", "+origen+", "+destino+", "+(consumoSegundos/60)+", `llamada_ringphone`.`tiempo_llamada`*(SELECT  `tarifa` FROM  `usuario` WHERE `usuario`.`id_usuario` = "+idClave+"))");
//            rsett = stmt.executeUpdate("INSERT INTO `recarga` (`id_recarga`, `id_usuario`, `fecha_recarga`, `valor_recarga`) VALUES (NULL, "+idClave+", CURRENT_TIMESTAMP," + valorRecarga + ")");
//            rsett = stmt.executeUpdate("INSERT INTO llamada_clave200 (`id_clave`,`origen`,`destino`,`num_reintentos`,`tiempo_entre_reintento`) VALUES (" + idClave + "," + origen + "," + destino + "," + numreintentos + "," + tiempoentrereintento + ")");

        } catch (SQLException ex) {
            Logger.getLogger(InsertLlamada.class.getName()).log(Level.SEVERE, null, ex);
        } finally // ensure resultSet, statement and connection are closed
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
        InsertLlamada con = new InsertLlamada("c9e1074f5b3f9fc8ea15d152add07294", "dialer2", 119,"1");
        con.InsertarLlamadaDataBase();
    }
}
