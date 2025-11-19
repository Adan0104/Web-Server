package ThreadPool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ExecutorService threadPool;

    Server(int poolSize){
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public void handleClient(Socket clientSocket){
        try(PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(),true);
            BufferedReader textFile = new BufferedReader(new FileReader("D:\\VSCode\\Java\\Web-Server\\ThreadPool\\sampleText.txt"))
        ){
            String line = textFile.readLine();
            while(line != null){
                toSocket.println(line);
                line = textFile.readLine();
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args){
        int port = 8010;
        int poolSize = 20;
        Server server = new Server(poolSize);

        try{
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(60000);
            System.out.println("Server is listening on port: " + port);

            while(true){
                Socket clientSocket = serverSocket.accept();

                server.threadPool.execute(() -> server.handleClient(clientSocket));
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
