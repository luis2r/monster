/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ringphone;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author luis
 */
@Path("service")
public class ServiceResource {

    @Context
    private UriInfo context;

    public ServiceResource() {
    }

    @GET
    @Path("/oneCall/{extorigen}/{numteldestino}/{confirmacion}")
    @Produces({"application/xml"})
    public Response oneCall(@PathParam("extorigen") String extorigen, @PathParam("numteldestino") String numteldestino, @PathParam("confirmacion") String confirmacion) throws Exception {
        System.out.println();
        System.out.println();
        String respuestallamId = "";

        int[] resp = {0, 0, 0};

        ConexionDataBase2 con2 = new ConexionDataBase2();

        String hashorigen = con2.getHashToNumberConsulta(extorigen);

        String consulta = con2.getConfirmationConsulta(confirmacion);

        double saldoMinutos = Double.MAX_VALUE;

        AsteriskCallEventsStateProd call = null;
//        System.out.println("Hash: " + hashorigen);
//        System.out.println("Hash: " + hashorigen);
        if (!(hashorigen == null) && (hashorigen.equals(consulta))) {

            saldoMinutos = con2.getDbSaldo(extorigen);

            if (saldoMinutos >= 1 && saldoMinutos < Integer.MAX_VALUE) {


                call = new AsteriskCallEventsStateProd();
                call.setNumber(numteldestino); //destino
                call.setMessage(hashorigen);//origen
                call.setAbsoluteTimeout(40);
                resp = call.originate();
//                System.out.println(resp);

                if (resp[0] == 1) {
                    respuestallamId = "1";

                    con2.setOrigen(extorigen);
                    con2.setDestino(numteldestino);
                    con2.setConsumo(resp[1]);
                    con2.setNumberPatron(numteldestino);
                    con2.setEstado(resp[0]);
                    con2.InsertarLlamadaDataBase();


                } else if (resp[0] == 2) {
                    respuestallamId = "2";

                    con2.setOrigen(extorigen);
                    con2.setDestino(numteldestino);
                    con2.setConsumo(resp[1]);
                    con2.setNumberPatron(numteldestino);
                    con2.setEstado(resp[0]);
                    con2.InsertarLlamadaDataBase();


                } else if (resp[0] == 3) {
                    respuestallamId = "3";

                    con2.setOrigen(extorigen);
                    con2.setDestino(numteldestino);
                    con2.setConsumo(resp[1]);
                    con2.setNumberPatron(numteldestino);
                    con2.setEstado(resp[0]);
                    con2.InsertarLlamadaDataBase();

                } else if (resp[0] == 4) {
                    respuestallamId = "4";

                    con2.setOrigen(extorigen);
                    con2.setDestino(numteldestino);
                    con2.setConsumo(resp[1]);
                    con2.setNumberPatron(numteldestino);
                    con2.setEstado(resp[0]);
                    con2.InsertarLlamadaDataBase();
                }
            } else {
                respuestallamId = "5";

                con2.setOrigen(extorigen);
                con2.setDestino(numteldestino);
                con2.setConsumo(resp[1]);
                con2.setNumberPatron(numteldestino);
                con2.setEstado(resp[0]);
                con2.InsertarLlamadaDataBase();

            }

        } else {
            respuestallamId = "6";

            con2.setOrigen(extorigen);
            con2.setDestino(numteldestino);
            con2.setConsumo(resp[1]);
            con2.setNumberPatron(numteldestino);
            con2.setEstado(resp[0]);
            con2.InsertarLlamadaDataBase();

        }

        return Response.ok("<dialStatus>"
                + "<state>"
                + respuestallamId
                + "</state>"
                + "<time>"
                + resp[1]
                + "</time>"
                + "</dialStatus>").header("Access-Control-Allow-Origin", "*").build();

    }
}
