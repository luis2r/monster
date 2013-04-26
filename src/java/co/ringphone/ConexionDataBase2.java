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
    String hash;
    private static boolean debug = false;

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
        if (debug) {
            System.out.println(index);
        }

        if (index > 0) {
            cadena = cadena.substring(0, index);
            if (debug) {
                System.out.println(cadena);
            }
            cadena = cadena.substring(0, cadena.length() - 7);
            if (debug) {
                System.out.println(cadena);
            }
        } else {
            cadena = cadena.substring(0, cadena.length() - 7);
            if (debug) {
                System.out.println(cadena);
            }
        }

        this.numberPatron = cadena;

    }

    public double getValorConsumo() {

        return this.valorConsumo;
    }

    public String getHashToNumberConsulta(String hashProporcionado) {

        PreparedStatement ps = null; // query statement
        ResultSet resultSet = null; // manages results
        String number = null;

        try {

            this.conectar();
            if (debug) {
                System.out.println(this.status);
            }
            ps = conn.prepareStatement("SELECT `mainPhone`FROM `ma_RingphoneButton` WHERE `hash`=?");
            ps.setString(1, hashProporcionado);

            resultSet = ps.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();


            while (resultSet.next()) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    number = resultSet.getObject(i).toString();
                }

            } // end while
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDataBase2.class.getName()).log(Level.SEVERE, null, ex);
        } finally // ensure resultSet, statement and connection are closed
        {
            this.desconectar();
        } // end finally
        this.hash = number;
        return number;
    }

    public String getConfirmationConsulta(String confirmationProporcionado) {
        //        Connection conn = null; // manages connection
        PreparedStatement ps = null; // query statement
        ResultSet resultSet = null; // manages results
        String number = null;

        try {

            this.conectar();
            if (debug) {
                System.out.println(this.status);
            }
            ps = conn.prepareStatement("SELECT `mainPhone`FROM `ma_RingphoneButton` WHERE `confirmationCode`=?");
            ps.setString(1, confirmationProporcionado);

            resultSet = ps.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    number = resultSet.getObject(i).toString();
                }
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

        PreparedStatement ps = null; // query statement
        ResultSet resultSet = null; // manages results
        double number = Integer.MAX_VALUE;;

        try {

            this.conectar();
            if (debug) {
                System.out.println(this.status);
            }
            ps = conn.prepareStatement("SELECT `cashOnHand`FROM `ma_RingphoneButton` WHERE `hash`=?");
            ps.setString(1, hashp);
//                statement.setString(2, );

            resultSet = ps.executeQuery();


            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();


            while (resultSet.next()) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    number = Double.parseDouble(resultSet.getObject(i).toString());
                }

            } // end while
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDataBase2.class.getName()).log(Level.SEVERE, null, ex);
        } finally // ensure resultSet, statement and connection are closed
        {
            this.desconectar();
        } // end finally

        return number;

    }

    
        public String getEmail(String hashp) {

        String dato = "";
        try {

            this.conectar();
            if (debug) {
                System.out.println(this.status);
            }

            String query = "";

            query = "SELECT `email` FROM `ma_User` WHERE `id`=(SELECT  `userId` FROM  `ma_RingphoneButton` WHERE `ma_RingphoneButton`.`hash` = ?)";

//            this.numberPatron = "57320";

            PreparedStatement ps = conn.prepareStatement(query);

//            ps.setString(1, this.numberPatron);
            ps.setString(1, this.origen);

            // execute the java preparedstatement
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dato = rs.getString(1);
            }




        } catch (SQLException ex) {
            Logger.getLogger(ConexionDataBase2.class.getName()).log(Level.SEVERE, null, ex);
        } finally // ensure resultSet, statement and connection are closed
        {
            this.desconectar();
        } // end finally


        return dato;

    }
    
    public double getDbPrecioMinuto(String hashp) {

        double dato = 0;
        try {

            this.conectar();
            if (debug) {
                System.out.println(this.status);
            }

            String query = "";

            query = "SELECT `minutePriceBase` FROM `ma_CallPrice` WHERE `numberPattern`=? and `planId`=(SELECT  `planId` FROM  `ma_RingphoneButton` WHERE `ma_RingphoneButton`.`hash` = ?)";

//            this.numberPatron = "57320";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, this.numberPatron);
            ps.setString(2, this.origen);

            // execute the java preparedstatement
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dato = rs.getInt(1);
            }




        } catch (SQLException ex) {
            Logger.getLogger(ConexionDataBase2.class.getName()).log(Level.SEVERE, null, ex);
        } finally // ensure resultSet, statement and connection are closed
        {
            this.desconectar();
        } // end finally


        return dato;

    }

    public void conectar() {
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();


            String url = "jdbc:mysql://192.168.1.252:3306/RINGPA_DB";
//            String url = "jdbc:mysql://localhost:3306/RINGPA_DB";
            this.conn = DriverManager.getConnection(url, "ringpa_user", "IeF5Poh1*1&");

            this.status = "Connection opened";
            if (debug) {
                System.out.println("conextion ok");
            }

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

            this.conn.close();
            if (debug) {
                System.out.println("conection closed");
            }
        } // end try
        catch (Exception exception) {
            exception.printStackTrace();
        } // end catch

    }

    public void InsertarLlamadaDataBase() {

        try {

            this.conectar();
            if (debug) {
                System.out.println(this.status);
            }

            String query = "";

            query = "SELECT `minutePriceBase` FROM `ma_CallPrice` WHERE `numberPattern`=? and `planId`=(SELECT  `planId` FROM  `ma_RingphoneButton` WHERE `ma_RingphoneButton`.`hash` = ?)";

//            this.numberPatron = "57320";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, this.numberPatron);
            ps.setString(2, this.origen);

            // execute the java preparedstatement
            ResultSet rs = ps.executeQuery();
            double dato = 0;
            while (rs.next()) {
                dato = rs.getInt(1);
            }
            this.valorConsumo = dato * Math.ceil((double) this.consumoSegundos / 60);


            stmt = conn.createStatement();

            String query1 = "INSERT INTO `ma_RingCall` (`id`, `buttonId`, `source`, `destination`, `durationOnSeconds`, `durationOnMinutes`, `callPrice`, `date`,`state`) " + " VALUES (NULL,(SELECT  `id` FROM  `ma_RingphoneButton` WHERE `ma_RingphoneButton`.`hash` = ?),?,?,?,CEILING(`durationOnSeconds`/60), ?,CURRENT_TIMESTAMP,?)";
            ps = conn.prepareStatement(query1);

            ps.setString(1, this.origen);
            ps.setString(2, this.hash);
            ps.setString(3, this.destino);
            ps.setInt(4, this.consumoSegundos);;

            ps.setDouble(5, this.valorConsumo);
            ps.setInt(6, this.estado);

            ps.executeUpdate();

            query = "UPDATE `ma_RingphoneButton` SET `cashOnHand`=`ma_RingphoneButton`.`cashOnHand`-? WHERE `hash`=?";



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
        {
            System.out.println("Prueba " + con.getHashToNumberConsulta("xyz"));
            System.out.println("Prueba " + con.getConfirmationConsulta("123"));
        }
        if (con.getHashToNumberConsulta("xyz").equals(con.getConfirmationConsulta("123"))) {
            System.out.println("true");

        } else {
            System.out.println("false");
        }
        con.setOrigen("xyz");
        con.setDestino("104");
//        con.setConsumo(400);
        con.setNumberPatron("573206390201");
//        con.setEstado(1);
//        con.InsertarLlamadaDataBase();
//        con.getDbSaldo("xyz");
//        con.getDbPrecioMinuto("xyz");
        System.out.println("saldo " + con.getDbSaldo("xyz") + "precio " + con.getDbPrecioMinuto("xyz"));
        System.out.println("email " + con.getEmail("xyz"));
        
    }
}//