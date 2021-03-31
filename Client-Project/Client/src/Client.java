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

    public static void main(String[] args) throws IOException, SocketException{
        
        Socket s = new Socket("127.0.0.1", 50000);
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter username");
        


        String userName = userInput.nextLine();
        String userCmd = "";
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
    }
}