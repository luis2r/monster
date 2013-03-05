/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ringphone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luis
 */
public class UpdateusuarioPorRecargaOLlamada {

    static String status = "";
    String idClave;
    // JDBC driver name and database URL
// database URL
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/autodialer";

    public UpdateusuarioPorRecargaOLlamada(String idClave) {
        this.idClave = idClave;


    }

    public void actualizarSaldoUsuario(String motivo) {
        Connection conn = null;
        try {


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


//                conn = DriverManager.getConnection(url);

                status = "Connection opened";

            } catch (SQLException e) {
                status = e.getMessage();
//            } catch (ClassNotFoundException e) {
//
//                status = e.getMessage();

            } catch (Exception e) {

                status = e.getMessage();

            }


            System.out.println(status);
            String mensaje = "";

            //Criando um objeto Statement para enviar requisições SQL para o Banco de Dados         
//            Statement stmt = conn.createStatement();


            // create the java mysql update preparedstatement
            String query = "";
            if (motivo.equals("recarga")) {
                query = "UPDATE `usuario` SET `valor_final_saldo`=`usuario`.`valor_final_saldo`+(SELECT `valor_recarga` FROM `recarga` WHERE `id_recarga`=(SELECT `id_recarga`  FROM `recarga` ORDER BY `id_recarga` DESC LIMIT 1)),`saldo_en_minutos`=FLOOR(`valor_final_saldo`/`tarifa`),`saldo_en_dolares`=`valor_final_saldo`/1900 WHERE `extension_md5`= ?";
            }
            if (motivo.equals("llamada")) {
                query = "UPDATE `usuario` SET `valor_final_saldo`=`usuario`.`valor_final_saldo`-(SELECT `valor_llamada` FROM `llamada_ringphone` WHERE `id_llamada`=(SELECT `id_llamada`  FROM `llamada_ringphone` ORDER BY `id_llamada` DESC LIMIT 1)),`saldo_en_minutos`=FLOOR(`valor_final_saldo`/`tarifa`),`saldo_en_dolares`=`valor_final_saldo`/1900,`acumulado_de_llamadas`=`acumulado_de_llamadas`+1 WHERE `extension_md5`=?";
            }


            PreparedStatement preparedStmt = conn.prepareStatement(query);
//      preparedStmt.setDate(1,sqlfecha);
            preparedStmt.setString(1, idClave);

            // execute the java preparedstatement
            preparedStmt.executeUpdate();

            preparedStmt.close();
            conn.close();
            status = "Connection closed";
            System.out.println(status);
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        UpdateusuarioPorRecargaOLlamada con = new UpdateusuarioPorRecargaOLlamada("c9e1074f5b3f9fc8ea15d152add07294");
        con.actualizarSaldoUsuario("llamada");
//        con.actualizarSaldoUsuario("recarga");
    }
}