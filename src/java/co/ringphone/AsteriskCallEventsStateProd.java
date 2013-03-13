/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ringphone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
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
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.HangupEvent;
//import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewAccountCodeEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.StatusCompleteEvent;
import org.asteriskjava.manager.event.StatusEvent;
//import org.asteriskjava.manager.event.UnlinkEvent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 *
 * @author luis
 */
public class AsteriskCallEventsStateProd implements OriginateCallback, ManagerEventListener {

//    private final Logger logger = LoggerFactory.getLogger(AsteriskCallEventsState.class);
    private DefaultAsteriskServer asteriskServer = null;
    private String strChannelStatus = "NoStatusYet";
    private String user = "";
    private int absoluteTimeOut=0;
    private String phoneNumber = "";
    private ManagerConnection managerConnection;
    boolean outEvent = false;
    Boolean out2 = false;
    private String sourceUniqueIdCall = "";
    private String destinationUniqueIdCall = "";
    private String sourceUniqueIdCallChannel = "";
    private String destinationUniqueIdCallChannel = "";
    private List< String> list;
    private String currentChannel;
    private int[] respuesta = {0, 0, 0};//estado, tiempo hablado, tiempo timbrado.

    public AsteriskCallEventsStateProd() {
//        this.asteriskServer = new DefaultAsteriskServer("somewhere.org", "admin", "mypass");
        this.asteriskServer = new DefaultAsteriskServer("localhost", "manager", "4u70d14l3rp455w0rd");
//                ("192.168.0.17", "manager", "4u70");
        ManagerConnectionFactory factory = new ManagerConnectionFactory("localhost", "manager", "4u70d14l3rp455w0rd");
//                ("192.168.0.17", "manager", "4u70");

        this.managerConnection = factory.createManagerConnection();
    }

    public OriginateAction setupOriginate(String user, String phoneNumber) {

        OriginateAction originateAction = new OriginateAction();


        originateAction = new OriginateAction();
        originateAction.setChannel("Local/" + user + "@autodialer");
        originateAction.setContext("autodialer");
        originateAction.setExten(phoneNumber);
        originateAction.setPriority(new Integer(1));
        originateAction.setTimeout(new Long(40000));
        originateAction.setVariable("customernum", phoneNumber);
        originateAction.setCallerId("RingPhone"+"<"+user+">");
        
//        originateAction.
        return originateAction;
    }

    public int[] originate() throws IOException, AuthenticationFailedException,
            TimeoutException, InterruptedException {
        OriginateAction originateAction = this.setupOriginate(this.user, this.phoneNumber);

        try {
            this.asteriskServer.originateAsync(originateAction, this);
        } catch (ManagerCommunicationException e) {
//            logger.error("ManagerCommunicationException");
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
//                    logger.info("Current Channel State: " + currentStatus);
                    System.out.println("Current Channel State: " + currentStatus);
                    previousStatus = currentStatus;
                }
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
//                logger.info("Interrupted.");
            }
        }




        while (!this.outEvent) {
            
            
                    int i = 0;
        while (!this.out2) {
            i++;
            Thread.sleep(500L);

//            if (contestado==true) {
//                out2=true;
//            }

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
//            Thread.sleep(3000);
                Thread.sleep(500L);
            } catch (InterruptedException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
//        logger.info("Final Verdict: " + this.strChannelStatus);


        // and finally log off and disconnect//        

        System.out.println("Final Verdict: " + this.strChannelStatus);
        this.managerConnection.logoff();
        this.asteriskServer.shutdown();
        System.out.println("respuesta: ");
        for (int resp : respuesta) {
            System.out.println(resp);
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
        this.currentChannel=channel.getName();
        this.asteriskServer.getChannelByName(this.currentChannel).setAbsoluteTimeout(this.absoluteTimeOut);

        this.strChannelStatus = "Dialing";
        System.out.print(strChannelStatus);
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
        System.out.print(strChannelStatus);
        this.respuesta[0] = 2;
        this.outEvent = true;
    }

    @Override
    public void onSuccess(AsteriskChannel channel) {
        this.sourceUniqueIdCall = channel.getId();
        System.out.println(channel.getAccount());
        System.out.println(channel.getCallDetailRecord());
        System.out.println(channel.getCallerId());
        System.out.println(channel.getCurrentExtension());
        System.out.println(channel.getDateOfCreation());
        System.out.println(channel.getDateOfRemoval());
        System.out.println(channel.getDialedChannel());
        System.out.println(channel.getDialedChannelHistory());
        System.out.println(channel.getFirstExtension());
        System.out.println(channel.getDtmfReceived());
        System.out.println(channel.getDtmfSent());
        System.out.println(channel.getExtensionHistory());
        System.out.println(channel.getFirstExtension());
        System.out.println(channel.getHangupCause());
        System.out.println(channel.getHangupCauseText());
        System.out.println(channel.getId());
        System.out.println(channel.getLinkedChannel());
        System.out.println(channel.getLinkedChannelHistory());
        System.out.println(channel.getCallDetailRecord());
        System.out.println(channel.getDialingChannel());



        System.out.println("getId() :" + channel.getId());
//        System.out.println("getAccount() :" + channel.getAccount());
//        System.out.println("getCallDetailRecord() :" + channel.getCallDetailRecord());
//        System.out.println("getCallerId() :" + channel.getCallerId());
//        System.out.println("getCurrentExtension() :" + channel.getCurrentExtension());
        System.out.println("getDateOfCreation() :" + channel.getDateOfCreation());
//        System.out.println("getDateOfRemoval() :" + channel.getDateOfRemoval());
        System.out.println("getDialedChannel() :" + channel.getDialedChannel());
//        System.out.println("getDialedChannelHistory() :" + channel.getDialedChannelHistory());
//        System.out.println("getDialingChannel() :" + channel.getDialingChannel());
//        System.out.println("getDtmfReceived() :" + channel.getDtmfReceived());
//        System.out.println("getDtmfSent() :" + channel.getDtmfSent());
//        System.out.println("getExtensionHistory() :" + channel.getExtensionHistory());
//        System.out.println("getFirstExtension() :" + channel.getFirstExtension());
//        System.out.println("getHangupCause() :" + channel.getHangupCause());
//        System.out.println("getHangupCauseText() :" + channel.getHangupCauseText());
//        System.out.println("getLinkedChannel() :" + channel.getLinkedChannel());
//        System.out.println("getLinkedChannelHistory() :" + channel.getLinkedChannelHistory());
//        System.out.println("getMeetMeUser() :" + channel.getMeetMeUser());
        System.out.println("getName() :" + channel.getName());
//        System.out.println("getParkedAt() :" + channel.getParkedAt());
//        System.out.println("getQueueEntry() :" + channel.getQueueEntry());
//        System.out.println("getState() :" + channel.getState());
//        System.out.println("getVariables() :" + channel.getVariables());
// void 	hangup()
// void 	hangup(HangupCause cause)
//        try {
//            Thread.sleep(1000L);
//        } catch (InterruptedException ex) {
//            java.util.logging.Logger.getLogger(AsteriskCallEventsState.class.getName()).log(Level.SEVERE, null, ex);
//        }

        this.strChannelStatus = "Success";
//        System.out.print("imprimo canal \n" + channel + "\nimprimo canal \n");
//        System.out.println(channel.getId());
//        System.out.print(strChannelStatus);
    }

    public void onManagerEvent(ManagerEvent event) {

//        System.out.println(event);
//        System.out.println("se imprime evento");

        if (event instanceof DialEvent) {

            System.out.println("se hace DialEvent");
            Date tiempoInicioEvento = event.getDateReceived();
            DialEvent e = (DialEvent) event;
            String callerIdNum = e.getCallerIdNum();
            String callerIdName = e.getCallerIdName();
            String DestUniqueId = e.getDestUniqueId();
            String UniqueId = e.getUniqueId();
            String destination = e.getDestination();
            String dialStatus = e.getDialStatus();
            String dialString = e.getDialString();
            String todoevent = e.toString();
            String status = e.getDialStatus();
            String subEvent = e.getSubEvent();
            String channel = e.getChannel();
            System.out.println("Tiempo inicio evento DialEvent: " + tiempoInicioEvento + "\n"
                    + "Numero de quien llama: " + callerIdNum + "\n"
                    + "Nombre de quien llama: " + callerIdName + "\n"
                    + "Id unico dest: " + DestUniqueId + "\n"
                    + "Id unico source: " + UniqueId + "\n"
                    + "status: " + status + "\n"
                    + "Destino: " + destination + "\n"
                    + "Dial Status: " + dialStatus + "\n"
                    + "Dial string: " + dialString + "\n"
                    + "Dial subEvent: " + subEvent + "\n"
                    + "channel: " + channel + "\n"
                    + "Todos los datos de la llmada: " + todoevent + "\n" + "\n");

        } else if (event instanceof BridgeEvent) {

            System.out.println("se hace BridgeEvent");

            Date tiempoInicioEvento = event.getDateReceived();
            BridgeEvent e = (BridgeEvent) event;
            String bridgeState = e.getBridgeState();//.getCidCallingPresTxt();
//            double timestamp = e.getTimestamp();
            String callerId1 = e.getCallerId1();//.getCallerIdNum();
            String callerId2 = e.getCallerId2();//.getCallerIdName();
            String channel1 = e.getChannel1();//.getUniqueId();
            String channel2 = e.getChannel2();
            String uniqueId1 = e.getUniqueId1();//.getChannel();
            String uniqueId2 = e.getUniqueId2();
            e.isLink();
            e.isUnlink();
            String todoevent = e.toString();
            if (e.isUnlink()) {
                System.out.println("Desligada: " + e.getCallerId1() + " Destino: " + e.getCallerId2());
                if (this.sourceUniqueIdCall.equals(e.getUniqueId1())) {
                    this.destinationUniqueIdCall = e.getUniqueId2();
                    this.sourceUniqueIdCallChannel = e.getChannel1();
                    this.destinationUniqueIdCallChannel = e.getChannel2();

                    this.outEvent = true;
                }
            }
            if (e.isLink()) {
                System.out.println("ligada: " + e.getCallerId1() + " Destino: " + e.getCallerId2());
                if (this.sourceUniqueIdCall.equals(e.getUniqueId1())) {



                    this.out2 = true;
                }

            }


            System.out.println("Tiempo inicio evento BridgeEvent: " + tiempoInicioEvento + "\n"
                    + "bridgeState: " + bridgeState + "\n"
                    + "callerId1: " + callerId1 + "\n"
                    + "callerId2: " + callerId2 + "\n"
                    + "channel1: " + channel1 + "\n"
                    + "channel2: " + channel2 + "\n"
                    + "uniqueId1: " + uniqueId1 + "\n"
                    + "uniqueId2: " + uniqueId2 + "\n"
                    + "Todos los datos de la llmada: " + todoevent + "\n" + "\n");
        } //        else if (event instanceof UnlinkEvent) {
        //            System.out.println("se hace UnlinkEvent");
        //            Date tiempoInicioEvento = event.getDateReceived();
        //            UnlinkEvent e = (UnlinkEvent) event;
        //            String bridgeState = e.getBridgeState();
        //            String BbidgeType = e.getBridgeType();
        //            String callerId1 = e.getCallerId1();
        //            String callerId2 = e.getCallerId2();
        //            String channel1 = e.getChannel1();
        //            String channel2 = e.getChannel2();
        //            String uniqueId1 = e.getUniqueId1();
        //            String uniqueId2 = e.getUniqueId2();
        //            boolean isLink = e.isLink();
        //            boolean isUnlink = e.isUnlink();
        //            String todoevent = e.toString();
        //
        //            System.out.println("Desligada: " + e.getCallerId1() + " Destino: " + e.getCallerId2());
        //
        //            System.out.println("Tiempo inicio evento UnlinkEvent: " + tiempoInicioEvento + "\n"
        //                    + "bridgeState: " + bridgeState + "\n"
        //                    + "BbidgeType: " + BbidgeType + "\n"
        //                    + "callerId1: " + callerId1 + "\n"
        //                    + "callerId2: " + callerId2 + "\n"
        //                    + "Ichannel1: " + channel1 + "\n"
        //                    + "channel2: " + channel2 + "\n"
        //                    + "uniqueId1: " + uniqueId1 + "\n"
        //                    + "uniqueId2: " + uniqueId2 + "\n"
        //                    + "isLink: " + isLink + "\n"
        //                    + "isUnlink: " + isUnlink + "\n"
        //                    + "Todos los datos de la llmada: " + todoevent + "\n" + "\n");
        //
        //
        //        } 
        else if (event instanceof HangupEvent) {

            System.out.println("se hace hangup");
            Date tiempoInicioEvento = event.getDateReceived();
            HangupEvent e = (HangupEvent) event;
            String callerIdNum = e.getCallerIdNum();
            String callerIdName = e.getCallerIdName();
            String causaHangupTxt = e.getCauseTxt();
            int causaHangup = e.getCause();
            String uniqueId = e.getUniqueId();
            String todoevent = e.toString();
            String chanel = e.getChannel();
            System.out.println("Tiempo inicio evento HangupEvent: " + tiempoInicioEvento + "\n"
                    + "Numero de quien llama: " + callerIdNum + "\n"
                    + "Nombre de quien llama: " + callerIdName + "\n"
                    + "causa de hangup txt: " + causaHangupTxt + "\n"
                    + "causa de hangup id: " + causaHangup + "\n"
                    + "channel: " + chanel + "\n"
                    //                                "Id de la acción: "+actionId+"\n"+
                    + "Id unico source: " + uniqueId + "\n"
                    + "Todos los datos de la llmada: " + todoevent + "\n" + "\n");

//            this.outEvent = true;
        }


//        if (event instanceof LinkEvent) {
//            LinkEvent link = (LinkEvent) event;
//            System.out.println("Atendida: " + link.getCallerId1() + " Destino: " + link.getCallerId2());
//        }
        if (event instanceof CdrEvent) {
            System.out.println("se hace CdrEvent");
            CdrEvent cdr = (CdrEvent) event;
            System.out.println("getChannel cdr: " + cdr.getChannel());

            if (this.destinationUniqueIdCallChannel.equals(cdr.getDestinationChannel())) {
                System.out.println("\n getChannel cdr: " + cdr.getChannel());
                System.out.println("getDestinationChannel() cdr: " + cdr.getDestinationChannel());
                System.out.println("getDestination() cdr: " + cdr.getDestination());
                System.out.println("getCallerId() cdr: " + cdr.getCallerId());

                System.out.println("getSrc() cdr: " + cdr.getSrc());
                System.out.println("getAccountCode cdr: " + cdr.getAccountCode());


                System.out.println("getBillableSeconds cdr: " + cdr.getBillableSeconds());
                System.out.println("getUniqueId() cdr: " + cdr.getUniqueId());
                System.out.println("getEndTimeAsDate() cdr: " + cdr.getEndTimeAsDate());
                System.out.println("getStartTime() cdr: " + cdr.getStartTime());
                System.out.println("getStartTimeAsDate() cdr: " + cdr.getStartTimeAsDate());
                System.out.println("getDuration() cdr: " + cdr.getDuration());
                System.out.println("getAnswerTimeAsDate cdr: " + cdr.getAnswerTimeAsDate());
                System.out.println("getAnswerTime cdr: " + cdr.getAnswerTime());
                System.out.println("getEndTime() cdr: " + cdr.getEndTime());

                Integer tiempotimbrado = cdr.getDuration();
//                
//this.list = new ArrayList< String>();
//
//                    this.list.add(cdr.getBillableSeconds().toString()); // adds color to end of list
//                    this.list.add(tiempotimbrado.toString());
//                    this.list.add(this.strChannelStatus);
//                
                this.respuesta[0] = 1;
                this.respuesta[1] = cdr.getBillableSeconds(); // add at 2rd index
                this.respuesta[2] = tiempotimbrado; // add at 2rd index


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
