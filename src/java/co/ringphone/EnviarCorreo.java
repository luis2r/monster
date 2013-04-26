package co.ringphone;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author luis
 */
public class EnviarCorreo {

    String servidorSMTP = "mail.ringphone.co";
    String puerto = "25";
    String usuario = "soporte@ringphone.co";
    String password = "s0p0rt3*1&r1n9ph0n3";
//    String destino = "lider.desarrollo@sos-ingenieria.com";
    String destino = "luis2r@hotmail.com";
//    String destino = "luis2r@gmail.com";
    String asunto = "Recarga tu RingPhone";
    String mensaje = "<div style=color:black;>"
            + "El saldo de su botón RingPhone Identificado con el número"
            + ""
            + ""
            + ""
            + ""
            + "Este es un mensaje de prueba html.</div>";
    Properties props = new Properties();

    EnviarCorreo(String destino, String telefono) {

        this.mensaje = "<div style=color:black;>"
                + "<p>Alguien  está intentando contactarlo, pero el saldo de su botón RingPhone identificado con el número "
                + telefono
                + " ha vencido!<br>"
                + "Para que siga disfrutando de las ventajas que le ofrece este servicio <br>"
                + "Puede recargar entrando a su cuenta en la plataforma RingPhone "
                + "<a href='www.ringphone.co'>www.ringphone.co</a><br>"
                + "O escribiendonos a:<br>"
                + "<a href='mailto:ventas@ringphone.co?Subject=Recarga RingPhone"
                + telefono
                + "'> ventas@ringphone.co </a></p>"
                + "</div>";


        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", false);
        props.put("mail.smtp.host", servidorSMTP);
        props.put("mail.smtp.port", puerto);


        Session session = Session.getInstance(props, null);

        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                    destino));
            message.setSubject(asunto);
            message.setSentDate(new Date());
//            message.setText(mensaje);
            message.setContent(mensaje, "text/html; charset=utf-8");


            message.setFrom(new InternetAddress(
                    usuario));


            Transport tr = session.getTransport("smtp");
            tr.connect(servidorSMTP, usuario, password);
            message.saveChanges();
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(
                    "ventas@ringphone.co"));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(
                    "luis2r@hotmail.com"));
            
//            
//            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(
//                    "ventas@ringphone.co"));

            tr.sendMessage(message, message.getAllRecipients());
            tr.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EnviarCorreo n = new EnviarCorreo("luis2r@gmail.com", "3113017045");
    }
}