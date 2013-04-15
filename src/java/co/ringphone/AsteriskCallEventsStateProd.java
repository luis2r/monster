/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ringphone;

import java.io.IOException;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.LiveException;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.OriginateCallback;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.ManagerEvent;

/**
 *
 * @author luis
 */
public class AsteriskCallEventsStateProd implements OriginateCallback, ManagerEventListener {

    private DefaultAsteriskServer asteriskServer = null;
    private String strChannelStatus = "NoStatusYet";
    private String user = "";
    private int absoluteTimeOut = 0;
    private String phoneNumber = "";
    private ManagerConnection managerConnection;
    boolean outEvent = false;
    Boolean out2 = false;
    private String sourceUniqueIdCall = "";
    private String destinationUniqueIdCallChannel = "";
    private String currentChannel;
    private int[] respuesta = {0, 0, 0};//estado, tiempo hablado, tiempo timbrado.
    private static boolean debug = false;

    public AsteriskCallEventsStateProd() {
        this.asteriskServer = new DefaultAsteriskServer("localhost", "manager", "4u70d14l3rp455w0rd");
        ManagerConnectionFactory factory = new ManagerConnectionFactory("localhost", "manager", "4u70d14l3rp455w0rd");

        this.managerConnection = factory.createManagerConnection();
    }

    public OriginateAction setupOriginate(String user, String phoneNumber) {

        OriginateAction originateAction = new OriginateAction();
        originateAction.setChannel("Local/" + user + "@autodialer");
        originateAction.setContext("autodialer");
        originateAction.setExten(phoneNumber);
        originateAction.setPriority(new Integer(1));
        originateAction.setTimeout(new Long(40000));
        originateAction.setVariable("customernum", phoneNumber);
        originateAction.setCallerId("RingPhone" + "<" + user + ">");

//        originateAction.
        return originateAction;
    }

    public int[] originate() throws IOException, AuthenticationFailedException,
            TimeoutException, InterruptedException {
        OriginateAction originateAction = this.setupOriginate(this.user, this.phoneNumber);

        try {
            this.asteriskServer.originateAsync(originateAction, this);
        } catch (ManagerCommunicationException e) {
        }

        Boolean out = false;


        String previousStatus = "";
        // register for events
        this.managerConnection.addEventListener(this);
        // connect to Asterisk and log in
        this.managerConnection.login();
        // request channel state
        this.managerConnection.sendAction(new StatusAction());

        while (!out) {
            if (this.strChannelStatus.equals("Busy") || this.strChannelStatus.equals("Success") || this.strChannelStatus.equals("No Answer") || this.strChannelStatus.equals("Failed")) {
                out = true;
            }
            try {
                String currentStatus = this.strChannelStatus;
                if (!currentStatus.equals(previousStatus)) {
                    if (debug) {
                        System.out.println("Current Channel State: " + currentStatus);
                    }
                    previousStatus = currentStatus;
                }
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
            }
        }




        while (!this.outEvent) {


            int i = 0;
            while (!this.out2) {
                i++;
                Thread.sleep(500L);


                if (i == 100) {
                    this.out2 = true;
                    this.outEvent = true;
                    this.respuesta[0] = 3;
                    this.respuesta[1] = 0; // add at 2rd index
                    this.respuesta[2] = 0; // add at 2rd index
                    HangupAction hangupAction = new HangupAction(this.sourceUniqueIdCall);
                    try {
                        this.managerConnection.sendAction(hangupAction);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }


            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        // and finally log off and disconnect//  
        if (debug) {
            System.out.println("Final Verdict: " + this.strChannelStatus);
        }
        this.managerConnection.logoff();
        this.asteriskServer.shutdown();
        if (debug) {
            System.out.println("respuesta: ");
            for (int resp : respuesta) {
                System.out.println(resp);
            }
        }
        return respuesta;
    }

    @Override
    public void onBusy(AsteriskChannel channel) {
        this.strChannelStatus = "Busy";
        this.respuesta[0] = 2;
        this.outEvent = true;
    }

    @Override
    public void onDialing(AsteriskChannel channel) {
        this.currentChannel = channel.getName();
        this.asteriskServer.getChannelByName(this.currentChannel).setAbsoluteTimeout(this.absoluteTimeOut);

        this.strChannelStatus = "Dialing";
        if (debug) {
            System.out.print(strChannelStatus);
        }
    }

    @Override
    public void onFailure(LiveException cause) {
        this.strChannelStatus = "Failed";
        this.respuesta[0] = 4;
        this.outEvent = true;
    }

    @Override
    public void onNoAnswer(AsteriskChannel channel) {
        this.strChannelStatus = "No Answer";
        if (debug) {
            System.out.print(strChannelStatus);
        }
        this.respuesta[0] = 2;
        this.outEvent = true;
    }

    @Override
    public void onSuccess(AsteriskChannel channel) {
        this.sourceUniqueIdCall = channel.getId();
        if (debug) {
            System.out.println(channel.getCallerId());
            System.out.println(channel.getCurrentExtension());
            System.out.println(channel.getId());
            System.out.println(channel.getLinkedChannelHistory());
            System.out.println("getId() :" + channel.getId());
        }
        this.strChannelStatus = "Success";

    }

    public void onManagerEvent(ManagerEvent event) {


        if (event instanceof BridgeEvent) {
            if (debug) {
                System.out.println("se hace BridgeEvent");
            }
            BridgeEvent e = (BridgeEvent) event;
            String bridgeState = e.getBridgeState();//.getCidCallingPresTxt();
            e.isLink();
            e.isUnlink();
            String todoevent = e.toString();
            if (e.isUnlink()) {
                if (debug) {
                    System.out.println("Desligada: " + e.getCallerId1() + " Destino: " + e.getCallerId2());
                }
                if (this.sourceUniqueIdCall.equals(e.getUniqueId1())) {
                    this.destinationUniqueIdCallChannel = e.getChannel2();

                }
            }
            if (e.isLink()) {
                if (debug) {
                    System.out.println("ligada: " + e.getCallerId1() + " Destino: " + e.getCallerId2());
                }
                if (this.sourceUniqueIdCall.equals(e.getUniqueId1())) {
                    this.out2 = true;
                }

            }
            if (debug) {
                System.out.println(
                        "bridgeState: " + bridgeState + "\n"
                        + "Todos los datos de la llmada: " + todoevent + "\n" + "\n");
            }
        }


        if (event instanceof CdrEvent) {
            CdrEvent cdr = (CdrEvent) event;
            if (debug) {
                System.out.println("se hace CdrEvent");
                System.out.println("*****canal cdr*****");
                System.out.println("cdr.getDestinationChannel() cdr: " + cdr.getDestinationChannel());
                System.out.println("*****canal llamada*****");
                System.out.println("getChannel2 " + this.destinationUniqueIdCallChannel);
            }
            if (this.destinationUniqueIdCallChannel.equals(cdr.getDestinationChannel())) {
//                System.out.println("\n getChannel cdr: " + cdr.getChannel());
//                System.out.println("getDestinationChannel() cdr: " + cdr.getDestinationChannel());
//                System.out.println("getDestination() cdr: " + cdr.getDestination());
//                System.out.println("getCallerId() cdr: " + cdr.getCallerId());

//                System.out.println("getSrc() cdr: " + cdr.getSrc());
//                System.out.println("getAccountCode cdr: " + cdr.getAccountCode());
                if (debug) {
                    System.out.println("getBillableSeconds cdr: " + cdr.getBillableSeconds());
                }
//                System.out.println("getUniqueId() cdr: " + cdr.getUniqueId());
//                System.out.println("getEndTimeAsDate() cdr: " + cdr.getEndTimeAsDate());
//                System.out.println("getStartTime() cdr: " + cdr.getStartTime());
//                System.out.println("getStartTimeAsDate() cdr: " + cdr.getStartTimeAsDate());
//                System.out.println("getDuration() cdr: " + cdr.getDuration());
//                System.out.println("getAnswerTimeAsDate cdr: " + cdr.getAnswerTimeAsDate());
//                System.out.println("getAnswerTime cdr: " + cdr.getAnswerTime());
//                System.out.println("getEndTime() cdr: " + cdr.getEndTime());

                Integer tiempotimbrado = cdr.getDuration();


                this.respuesta[0] = 1;
                this.respuesta[1] = cdr.getBillableSeconds(); // add at 2rd index
                this.respuesta[2] = tiempotimbrado; // add at 2rd index
                this.outEvent = true;

            }

        }

    }

    public void setNumber(String number) {

        this.user = number;
    }

    public void setAbsoluteTimeout(int absoluteTimeOut) {

        this.absoluteTimeOut = absoluteTimeOut;
    }

    public void setMessage(String message) {
        this.phoneNumber = message;
    }

    public static void main(String[] args) throws Exception {

        AsteriskCallEventsStateProd call = new AsteriskCallEventsStateProd();
        call.setNumber("101"); //destino
        call.setMessage("104");//origen
        call.setAbsoluteTimeout(40);
        int[] resp = call.originate();
        System.out.println(resp);
    }
}
