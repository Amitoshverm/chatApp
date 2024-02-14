import java.io.*;
import java.net.*;
public class Server {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    public Server(){
        try {
            server = new ServerSocket(7779);
            System.out.println("server is ready to accept connection");
            System.out.println("waiting...");
            server.accept();

            // br will read out incoming date
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // out will send(write) the data to the server
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        }
        catch (Exception e) {
            System.exit(0);
        }
    }


    public void startReading() {
        // thread - here thread will continuously read the data and give it to us
        Runnable r1 = ()->{
            System.out.println("reader started...");
            while (!socket.isClosed()) {

                try {
                    String msg = br.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("client terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client: "+msg);

                } catch (IOException e) {
                    System.exit(0);
                }

            }
        };
        new Thread(r1).start();
    }
    public void startWriting() {
        // thread - here the thread will take date from user and send to the client
        Runnable r2 = ()->{
            System.out.println("Writer started...");
            while (!socket.isClosed()) {
                try {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                } catch (Exception e) {
                    System.exit(0);
                }
            }
        };
        new Thread(r2).start();
    }


    public static void main(String[] args) {
        System.out.print("this server going to start server");
        new Server();
    }
}
