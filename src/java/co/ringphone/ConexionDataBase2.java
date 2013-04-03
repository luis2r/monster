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
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luis
 */
public class ConexionDataBase2 {

    static String status = "";
    String origen;
    String destino;
    String idClave;
    int numreintentos;
    int tiempoentrereintento;
    int consumoSegundos;
    double valorConsumo;
    int estado;
    Connection conn = null; // manages connection
    Statement stmt = null; // query statement
    int rsett;
    String numberPatron;

    public ConexionDataBase2() {
    }

    public void setOrigen(String origen) {
        this.origen = origen;

    }

    public void setDestino(String destino) {
        this.destino = destino;

    }

    public void setConsumo(int consumoSegundos) {
        this.consumoSegundos = consumoSegundos;

    }

    public void setEstado(int estado) {
        this.estado = estado;

    }

    public void setNumberPatron(String cadenaTodoElNumero) {
        String cadena = cadenaTodoElNumero;

        int index = cadena.indexOf("*");
        System.out.println(index);

        if (index > 0) {
            cadena = cadena.substring(0, index);
            System.out.println(cadena);
            cadena = cadena.substring(0, cadena.length() - 7);
            System.out.println(cadena);
        } else {
            cadena = cadena.substring(0, cadena.length() - 7);
            System.out.println(cadena);
        }



        //        System.out.println(cadena);
        this.numberPatron = cadena;

    }

    public double getValorConsumo() {

        return this.valorConsumo;
    }

    public String getHashToNumberConsulta(String hashProporcionado) {
//        Connection conn = null; // manages connection
        PreparedStatement ps = null; // query statement
        ResultSet resultSet = null; // manages results
        String number = null;

        try {

            this.conectar();

            System.out.println(this.status);
            ps = conn.prepareStatement("SELECT `mainPhone`FROM `ma_RingphoneButton` WHERE `hash`=?");
            ps.setString(1, hashProporcionado);
//                statement.setString(2, );

            resultSet = ps.executeQuery();

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
                    number = resultSet.getObject(i).toString();
//                    System.out.printf("%-8s\t", resultSet.getObject(i));
                }

//                System.out.println();
            } // end while
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDataBase2.class.getName()).log(Level.SEVERE, null, ex);
        } finally // ensure resultSet, statement and connection are closed
        {
            this.desconectar();
        } // end finally

        return number;
    }

    public String getConfirmationConsulta(String confirmationProporcionado) {
        //        Connection conn = null; // manages connection
        PreparedStatement ps = null; // query statement
        ResultSet resultSet = null; // manages results
        String number = null;

        try {

            this.conectar();

            System.out.println(this.status);
            ps = conn.prepareStatement("SELECT `mainPhone`FROM `ma_RingphoneButton` WHERE `confirmationCode`=?");
            ps.setString(1, confirmationProporcionado);
//                statement.setString(2, );

            resultSet = ps.executeQuery();

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
                    number = resultSet.getObject(i).toString();
//                    System.out.printf("%-8s\t", resultSet.getObject(i));
                }

//                System.out.println();
            } // end while
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDataBase2.class.getName()).log(Level.SEVERE, null, ex);
        } finally // ensure resultSet, statement and connection are closed
        {
            this.desconectar();
        } // end finally

        return number;

    }

    public double getDbSaldo(String hashp) {
        //        Connection conn = null; // manages connection
        PreparedStatement ps = null; // query statement
        ResultSet resultSet = null; // manages results
        double number = Integer.MAX_VALUE;;

        try {

            this.conectar();

            System.out.println(this.status);
            ps = conn.prepareStatement("SELECT `cashOnHand`FROM `ma_RingphoneButton` WHERE `hash`=?");
            ps.setString(1, hashp);
//                statement.setString(2, );

            resultSet = ps.executeQuery();

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
                    number = Double.parseDouble(resultSet.getObject(i).toString());
//                    System.out.printf("%-8s\t", resultSet.getObject(i));
                }

//                System.out.println();
            } // end while
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDataBase2.class.getName()).log(Level.SEVERE, null, ex);
        } finally // ensure resultSet, statement and connection are closed
        {
            this.desconectar();
        } // end finally

        return number;

    }

    public void conectar() {
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
//            String url = "jdbc:mysql://localhost:3306/RINGPA_DB?user=autodialer&password=aut0d14l3rs3rv1c3s";
//            String url = "jdbc:mysql://192.168.1.252:3306/RINGPA_DB?user=ringpa_user&password=IeF5Poh1*1&";

//            this.conn = DriverManager.getConnection(url);
            
            String url = "jdbc:mysql://192.168.1.252:3306/RINGPA_DB";

            this.conn = DriverManager.getConnection(url,"ringpa_user","IeF5Poh1*1&");

            this.status = "Connection opened";
            System.out.println("conextion ok");

        } catch (SQLException e) {
            this.status = e.getMessage();
        } catch (ClassNotFoundException e) {
            this.status = e.getMessage();

        } catch (Exception e) {
            this.status = e.getMessage();
        }

    }

    public void desconectar() {
        try {
//                rsett.close();
//            this.stmt.close();
            this.conn.close();
            System.out.println("conection closed");
        } // end try
        catch (Exception exception) {
            exception.printStackTrace();
        } // end catch

    }

    public void InsertarLlamadaDataBase() {

        try {

            this.conectar();

            System.out.println(this.status);

            String query = "";

            query = "SELECT `minutePriceBase` FROM `ma_CallPrice` WHERE `numberPattern`=? and `planId`=(SELECT  `id` FROM  `ma_RingphoneButton` WHERE `ma_RingphoneButton`.`hash` = ?)";

//            this.numberPatron = "57320";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, this.numberPatron);
            ps.setString(2, this.origen);

            // execute the java preparedstatement
            ResultSet rs = ps.executeQuery();
            double dato = 0;
            while (rs.next()) {
                System.out.println("while " + rs.getInt(1));
                dato = rs.getInt(1);
            }
            this.valorConsumo = dato * Math.ceil((double) this.consumoSegundos / 60);
            System.out.println(this.valorConsumo);




            //Criando um objeto Statement para enviar requisições SQL para o Banco de Dados         
            stmt = conn.createStatement();
//            String numberPatron = "57320";

            String query1 = "INSERT INTO `ma_RingCall` (`id`, `buttonId`, `source`, `destination`, `durationOnSeconds`, `durationOnMinutes`, `callPrice`, `date`,`state`) " + " VALUES (NULL,(SELECT  `id` FROM  `ma_RingphoneButton` WHERE `ma_RingphoneButton`.`hash` = ?),?,?,?,CEILING(`durationOnSeconds`/60), ?,CURRENT_TIMESTAMP,?)";
            ps = conn.prepareStatement(query1);

            ps.setString(1, this.origen);
            ps.setString(2, this.origen);
            ps.setString(3, this.destino);
            ps.setInt(4, this.consumoSegundos);;
//            Double valConsumo=this.getValorConsumo();
            ps.setDouble(5, this.valorConsumo);
            ps.setInt(6, this.estado);

            ps.executeUpdate();

//            String query2 = "INSERT INTO `ma_RingCall` (`id`, `buttonId`, `source`, `destination`, `durationOnSeconds`, `durationOnMinutes`, `callPrice`, `date`) " + " VALUES (NULL,(SELECT  `id` FROM  `ma_RingphoneButton` WHERE `ma_RingphoneButton`.`hash` = ?),?,?,?,CEILING(`durationOnSeconds`/60), ?,CURRENT_TIMESTAMP)";
//            ps = conn.prepareStatement(query2);




//            if (motivo.equals("recarga")) {
//                query = "UPDATE `usuario` SET `valor_final_saldo`=`usuario`.`valor_final_saldo`+(SELECT `valor_recarga` FROM `recarga` WHERE `id_recarga`=(SELECT `id_recarga`  FROM `recarga` ORDER BY `id_recarga` DESC LIMIT 1)),`saldo_en_minutos`=FLOOR(`valor_final_saldo`/`tarifa`),`saldo_en_dolares`=`valor_final_saldo`/1900 WHERE `extension_md5`= ?";
//            }
//            if (motivo.equals("llamada")) {
            query = "UPDATE `ma_RingphoneButton` SET `cashOnHand`=`ma_RingphoneButton`.`cashOnHand`-? WHERE `hash`=?";
//            }


            ps = conn.prepareStatement(query);
            ps.setDouble(1, this.valorConsumo);
            ps.setString(2, this.origen);




            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ConexionDataBase2.class.getName()).log(Level.SEVERE, null, ex);
        } finally // ensure resultSet, statement and connection are closed
        {
            this.desconectar();
        } // end finally
    }

    public static void main(String[] args) {

        ConexionDataBase2 con = new ConexionDataBase2();
        System.out.println("Prueba " + con.getHashToNumberConsulta("xyz"));
        System.out.println("Prueba " + con.getConfirmationConsulta("123"));
        if (con.getHashToNumberConsulta("xyz").equals(con.getConfirmationConsulta("123"))) {
            System.out.println("true");

        } else {
            System.out.println("false");
        }
        con.setOrigen("xyz");
        con.setDestino("104");
        con.setConsumo(400);
        con.setNumberPatron("573206390201");
        con.setEstado(1);
        con.InsertarLlamadaDataBase();
        con.getDbSaldo("xyz");
        System.out.println("Prueba " + con.getDbSaldo("xyz"));
    }
}//