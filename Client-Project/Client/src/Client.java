import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class Client {
    // global variables initialised for sending messages according to user guide
    private static String HELO = "HELO";
    private static String AUTH = "AUTH";
    private static String REDY = "REDY";
    private static String NONE = "NONE";
    private static String GETS = "GETS All";
    private static String OK = "OK";
    private static String SCHD = "SCHD";
    private static String QUIT = "QUIT";
    private static String JOBN = "JOBN";
    private static String JCPL = "JCPL";
    private static final String dot = ".";

    // global variables initialised for holding important shceduling data
    private static int biggestCS = 0;
    private static int biggestSID = 0;
    private static String biggestST = "";

    private static int counter = 0;
    
    // global variable to hold messages sent from server
    private static String str = "";


    

    // this method is responsible for taking the ArrayList parameter looping through
    // it and splitting each String of the list into an array so that the indexes of
    // that array can be assigned to the attributes of the ServerInfo object.
    // the object is added to a seperate ArrayList where the allToLargest is checked
    // on the coreCount of each ServerInfo object
    public static void createServerInfo(ArrayList<String> SLI) {
        String[] SLIHold;
        String res = null;
        ArrayList<ServerInfo> serverHold = new ArrayList<>();
        for (int i = 0; i < SLI.size(); i++) {
            ServerInfo si = new ServerInfo();
            SLIHold = SLI.get(i).split("\\s+");
            System.out.println("SLI:" + SLIHold);
            si.type = SLIHold[0];
            si.id = Integer.parseInt(SLIHold[1]);
            si.coreCount = Integer.parseInt(SLIHold[4]);
            si.memory = Integer.parseInt(SLIHold[5]);
            si.disk = Integer.parseInt(SLIHold[6]);
            serverHold.add(si);
            System.out.println("si:" + si);
        }
            biggestCS = serverHold.get(0).coreCount;
            biggestSID = serverHold.get(0).id;
            biggestST = serverHold.get(0).type;
        for (int i = 0; i < serverHold.size(); i++) {
            counter = i;
            if (serverHold.get(i).coreCount > biggestCS) {
                biggestCS = serverHold.get(i).coreCount;
                biggestSID = serverHold.get(i).id;
                biggestST = serverHold.get(i).type;
            }
        }
        //return res;

    }

     // this function is responsible for sending the GETS All message to get all
    // server information and add it into an ArrayList
    public static void getLargestServer(Socket s, PrintWriter pw, BufferedReader bf) {
        String reply = "";
        ArrayList<String> SLI = new ArrayList<>();
        //ArrayList<ServerInfo> serverHold = new ArrayList<>();
        try {
            pw.println(GETS);
            pw.flush();
            reply = bf.readLine();
            System.out.println("server outer : " + reply);
            while (!reply.equals(dot)) {
                pw.println(OK);
                pw.flush();
                reply = bf.readLine();
                System.out.println("server inner: " + reply);
                if (!reply.equals(dot)) {
                    SLI.add(reply);
                    System.out.println(SLI);
                    
                }
            }
                
            // for (int i = 0; i < serverHold.size(); i++) {
            //     System.out.println("here:" + serverHold.get(i).type);
            // }
           // reply = 
            createServerInfo(SLI);
        } catch (Exception e) {
            System.out.println("Error: ArrayList invalid");
        }
        //return reply;
    }


    // intial handshake between client-server where client is authenticated before
    // proceeding to job scheduling
    public static void handShake(PrintWriter pw, BufferedReader bf) {
        String userName = System.getProperty("user.name");

        try {
            pw.println((HELO));
            pw.flush();
            str = bf.readLine();
            System.out.println("server : " + str);

            pw.println(AUTH + " " + userName);
            pw.flush();

            str = bf.readLine();
            System.out.println("server : " + str);

            pw.println(REDY);
            pw.flush();

            str = bf.readLine();
            System.out.println("server : " + str);
        } catch (IOException e) {
            System.out.println("Error: handshake invalid");
            e.printStackTrace();
        }

    }

    // JCPL messages will be sent to client which highlight the status of the jobs
    // that have been scheduled and need a REDY reponse from the client
    public static void jobStatus(PrintWriter pw, BufferedReader bf) {
        try {
            if (str.contains(JCPL)) {
                pw.println(REDY);
                pw.flush();
                str = bf.readLine();
                System.out.println("server : " + str);
            }
        } catch (IOException e) {
            System.out.println("Error: jobStatus invalid");
            e.printStackTrace();
        }
    }


    // the scheduling of jobs by splitting the JOBN string and taking the job ID to
    // use in the schedule message along with biggestST and biggestSID
    public static void schedJob(PrintWriter pw, BufferedReader bf) {
        try {
            if (str.contains(JOBN)) {

                String[] hold = str.split("\\s+");
                int jbId = Integer.parseInt(hold[2]);

                pw.println(SCHD + " " + jbId + " " + biggestST + " " + biggestSID);
                pw.flush();
                str = bf.readLine();
                System.out.println("server : " + str);
            }
        } catch (IOException e) {
            System.out.println("Error: schedJob invalid");
            e.printStackTrace();
        }

    }



    // function which initialises the socket so that a connection can be established
    // with client/server and messages can be sent between.
    // Calls core functions of stage 1 like the handshake as well contains main loop
    // for listening for jobs or other messages
    public static void main(String[] args) throws IOException, SocketException{
        
        Socket s = new Socket("127.0.0.1", 50000);
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter username");
        


        String userName = userInput.nextLine();
        String userCmd = "";
        ArrayList<String> SLI = new ArrayList<>();
        pw.println((HELO));
        pw.flush();
        



        String str = bf.readLine();
        System.out.println("server : " + str);


        pw.println(AUTH + userName) ;
        pw.flush();

        str = bf.readLine();
        System.out.println("server : " + str);

        pw.println(REDY);
        pw.flush();

        str = bf.readLine();
        System.out.println("server : " + str);

        getLargestServer(s, pw, bf);
        
        while(str.equals(NONE)){
            System.out.println(" I am here" + str);
            // pw.println(OK);
            // pw.flush();
            // str = bf.readLine();
            // userCmd = "Get Server Information";
            // userInput.nextLine();

           // String reply = "";
           // String[] SLIHold;
            // if (userCmd.equals("Get Server Information")) {

            //     userCmd = "FINISHED";

            // }
//            if (userCmd.equals("FINISHED")) {
                
                
                    System.out.println(" I am here IF DOT");
                    
                    
                    // pw.println(REDY);
                    // pw.flush();
                    // str = bf.readLine();
                    while(str.equals(dot) || str.equals("")){
                        str = bf.readLine();
                        System.out.println("please work " + str);
                    }
                    if(str.contains(JCPL)){
                        pw.println(REDY);
                        pw.flush();
                        str = bf.readLine();
                        System.out.println("server: " + str);
                    }
                    if(str.equals(NONE)){
                        break;
                    }
                    if(str.equals(OK)){
                        //str = bf.readLine();
                        pw.println(REDY);
                        pw.flush();
                        str = bf.readLine();
                    }
                    if(str.contains(JOBN)){
                        
                    System.out.println("after dot: " + str);
                    String[] hold = str.split("\\s+");

                    

                    System.out.println("sched: " + hold[1]);
                    // Thread.sleep(9000);
                    int jbId = Integer.parseInt(hold[2]);

                    pw.println(SCHD + " " + jbId + " " + biggestST + " " + biggestSID);
                    // pw.println(GETS);
                    pw.flush();
                    str = bf.readLine();
                    System.out.println("server : " + str);
                   // pw.println(OK);
                    //pw.flush();
                    //str = bf.readLine();
                    System.out.println("server : " + str);
                    }
          

        }
        if(str.equals(NONE)){
            pw.println(QUIT);
            pw.flush();
            str = bf.readLine();
            System.out.println("server : " + str);
            if(str.equals(QUIT)){
                s.close();
            }
        }
        
    }
    }