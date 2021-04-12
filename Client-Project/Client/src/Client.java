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
            String reply = "";
            String[] SLIHold;
            if (userCmd.equals("Get Server Information")) {
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
               

                userCmd = "FINISHED";

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