import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Components
    private JLabel heading = new JLabel("Client");
    private JTextArea msgArea = new JTextArea();
    private JTextField msgInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client() {
        try {
            System.out.println("sending request to server");
            socket = new Socket("127.0.0.1", 7779);
            System.out.println("connection done");

            // br will read out incoming date
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // out will send(write) the data to the server
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            HandleEvents();
            startReading();
//            startWriting();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void HandleEvents() {
        msgInput.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
  //              System.out.println("key Released "+ e.getKeyCode());
                if (e.getKeyCode() == 10) {
  //                  System.out.println("you have pressed enter key ");
                    String contentToSend = msgInput.getText();
                    msgArea.append("Me: "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    msgInput.setText("");
                    msgInput.requestFocus();
                }
            }
        });
    }

    private void createGUI() {
        // GUI
        this.setTitle("Client messenger");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);   // center our screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // closes when press on X
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setIcon(new ImageIcon("2697657_apple_messages_bubble_communication_conversation_icon (1).png"));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        //Component Coding
        heading.setFont(font);
        msgArea.setFont(font);
        msgInput.setFont(font);

        //Setting frame layout
        this.setLayout(new BorderLayout());

        //adding components to frame
        this.add(heading, BorderLayout.NORTH);
        this.add(msgArea, BorderLayout.CENTER);
        this.add(msgInput, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    public void startWriting() {
        // thread - here the thread will take date from user and send to the client
        Runnable r1 = ()->{
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
        new Thread(r1).start();
    }
    public void startReading() {
        // thread - here thread will continuously read the data and give it to us
        Runnable r2 = ()->{
            System.out.println("reader started...");
            while (!socket.isClosed()) {

                try {
                    String msg = br.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, " Server terminated the chat");
                        msgInput.setEnabled(false);
                        break;
                    }
                   // System.out.println("Server: "+msg);
                    msgArea.append("Server: "+msg+ "\n");

                } catch (Exception e) {
                    System.exit(0);
                }

            }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("this is client");
        new Client();
    }
}
