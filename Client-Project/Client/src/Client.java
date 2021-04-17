import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class Client {
    private static String HELO = "HELO";
    private static String AUTH = "AUTH";
    private static String REDY = "REDY";
    private static String NONE = "NONE";
    private static String GETS = "GETS All";
    private static String OK = "OK";
    private static String SCHD = "SCHD";
    private static String QUIT = "QUIT";
    private static final String dot = ".";
    private static int biggestCS = 0;
    private static int biggestSID = 0;
    private static int counter = 0;
    private static String biggestST = "";


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
        
        while(str.equals(NONE)){
          

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