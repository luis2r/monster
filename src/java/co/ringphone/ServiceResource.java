/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ringphone;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author luis
 */
@Path("service")
public class ServiceResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ServiceResource
     */
    public ServiceResource() {
    }

    /**
     * Retrieves representation of an instance of co.ringphone.ServiceResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/oneCall/{extorigen}/{numteldestino}")
    @Produces({"application/xml"})
    public Response oneCall(@PathParam("extorigen") String extorigen, @PathParam("numteldestino") String numteldestino) throws Exception {

//        String respuestallam = "";
        String respuestallamId = "";

//        int[] resp = null;
        int[] resp = {0, 0, 0};
        ConexionDataBase connnn = new ConexionDataBase("99999", "102", "104", 1, 1);
        connnn.InsertarLlamadaDataBase();

        ConsultaIdSOS conn2 = new ConsultaIdSOS("1");
        String idsos = conn2.consultar();

//        ConsultaHash con4 = new ConsultaHash(45,"c9e1074f5b3f9fc8ea15d152add07294");
        ConsultaHash con4 = new ConsultaHash(extorigen);
//        ConsultaHash con4 = new ConsultaHash(45);
        String hashorigen = con4.consultar();

//        ConsultaHash con4 = new ConsultaHash(45,"c9e1074f5b3f9fc8ea15d152add07294");
////        ConsultaHash con = new ConsultaHash(4);
//        String hashorigen=con4.consultar();


        ConsultaSaldoMinutos con = null;
        int saldoMinutos = Integer.MAX_VALUE;

        AsteriskCallEventsStateProd call = null;
//        System.out.println("Hash: "+ hashorigen);
        if (!(hashorigen == null)) {

            con = new ConsultaSaldoMinutos(extorigen);
//        ConsultaSaldoMinutos con = new ConsultaSaldoMinutos(45);
            saldoMinutos = con.consultar();

            if (saldoMinutos >= 1 && saldoMinutos < Integer.MAX_VALUE) {



//        int i = 0;
//        for (i = 1; i <= numreintentos; i++) {
                call = new AsteriskCallEventsStateProd();
                call.setNumber(numteldestino); //destino
//            call.setMessage(hashorigen);//origen
                call.setMessage(hashorigen);//origen
                resp = call.originate();
                System.out.println(resp);

                if (resp[0] == 1) {
                    respuestallamId = "1";
                    connnn = new ConexionDataBase("99999", "102", "104", 1, 1);
                    connnn.InsertarLlamadaDataBase();
                    conn2 = new ConsultaIdSOS("1");
                    idsos = conn2.consultar();
                    InsertLlamada con2 = new InsertLlamada(extorigen, numteldestino, resp[1],respuestallamId);
                    con2.InsertarLlamadaDataBase();
                    UpdateusuarioPorRecargaOLlamada con1 = new UpdateusuarioPorRecargaOLlamada(extorigen);
                    con1.actualizarSaldoUsuario("llamada");
                    
//                respuestallam = "&iexclLlamada exitosa exitosa! <br>Duraci&oacuten: "+ resp[1]+ " segundos";


                } else if (resp[0] == 2) {
                    respuestallamId = "2";
                    connnn = new ConexionDataBase("99999", "102", "104", 1, 1);
                    connnn.InsertarLlamadaDataBase();
                    conn2 = new ConsultaIdSOS("1");
                    idsos = conn2.consultar();
                    InsertLlamada con2 = new InsertLlamada(extorigen, numteldestino, resp[1],respuestallamId);
                    con2.InsertarLlamadaDataBase();
//                    respuestallam = "&iexclEs posible que el tel&eacutefono destino est&eacute ocupado en este momento!";


                } else if (resp[0] == 3) {
                    respuestallamId = "3";
                    connnn = new ConexionDataBase("99999", "102", "104", 1, 1);
                    connnn.InsertarLlamadaDataBase();
                    conn2 = new ConsultaIdSOS("1");
                    idsos = conn2.consultar();
                    InsertLlamada con2 = new InsertLlamada(extorigen, numteldestino, resp[1],respuestallamId);
                    con2.InsertarLlamadaDataBase();
//                    respuestallam = "&iexclTodos nuestros agentes est&aacuten ocupados en est&eacute momento. <br>Intentelo m&aacutes tarde!";

                } else if (resp[0] == 4) {
                    respuestallamId = "4";
                    connnn = new ConexionDataBase("99999", "102", "104", 1, 1);
                    connnn.InsertarLlamadaDataBase();
                    conn2 = new ConsultaIdSOS("1");
                    idsos = conn2.consultar();
                    InsertLlamada con2 = new InsertLlamada(extorigen, numteldestino, resp[1],respuestallamId);
                    con2.InsertarLlamadaDataBase();
//                    respuestallam = "&iexclEs posible que el tel&eacutefono destino no exista o est&eacute fuera de servicio. <br>Intente con otro tel&eacutefono!";
                }
            } else {
                respuestallamId = "5";
                connnn = new ConexionDataBase("99999", "102", "104", 1, 1);
                connnn.InsertarLlamadaDataBase();
                conn2 = new ConsultaIdSOS("1");
                idsos = conn2.consultar();
                InsertLlamada con2 = new InsertLlamada(extorigen, numteldestino, resp[1],respuestallamId);
                con2.InsertarLlamadaDataBase();
//                respuestallam = "&iexclNo tiene saldo disponible,Hay inconsitencias en su saldo!";
//                respuestallam = "&iexclNo ha sido posible realizar la comunicaci&oacuten!";

            }

        } else {
            respuestallamId = "6";
            connnn = new ConexionDataBase("99999", "102", "104", 1, 1);
            connnn.InsertarLlamadaDataBase();
            conn2 = new ConsultaIdSOS("1");
            idsos = conn2.consultar();
            InsertLlamada con2 = new InsertLlamada(extorigen, numteldestino, resp[1],respuestallamId);
            con2.InsertarLlamadaDataBase();
//            System.out.println("Condicion 6");
//            respuestallam = "&iexclEl usuario no ha sido encontrado revise su hash e id!" + hashorigen;

        }


//        respuestallam = idllamada + "|" + resp[0] + "|" + resp[1] + "|" + idsos + "|" + i + "|" + resp[2];

//        return "<H1>"+respuestallam+"  "+url+"</H1>";




        return Response.ok("<dialStatus>"
                + "<state>"
                + respuestallamId
                + "</state>"
                + "<time>"
                + resp[1]
                + "</time>"
                + "</dialStatus>").header("Access-Control-Allow-Origin", "*").build();
//        return respuestallam;
    }

    @GET
    @Path("/recarga/{idUsuario}/{valorRecarga}")//id de usuario sera el hash de aquie en adelante
    @Produces({"text/plain"})
    public String recarga(@PathParam("idUsuario") String idUsuario, @PathParam("valorRecarga") int valorRecarga) throws Exception {
        InsertRecarga con = new InsertRecarga(idUsuario, valorRecarga);
        con.InsertarRecargaDataBase();
        UpdateusuarioPorRecargaOLlamada con1 = new UpdateusuarioPorRecargaOLlamada(idUsuario);
        con1.actualizarSaldoUsuario("recarga");
        return "recarga exitosa";
    }

    /**
     * PUT method for updating or creating an instance of ServiceResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}
