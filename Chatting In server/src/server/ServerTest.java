package server;

import java.net.ServerSocket;
import java.io.*;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class ServerTest extends JFrame
{

    private JButton start;
    private JTextArea text;
    private BufferedReader clientinput;
    private PrintWriter clientoutput;

    private class listen implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            connection connect = new connection();
            connect.start();
        }
    }

    public ServerTest ()
    {
        setSize(400,300);
        setLayout(new BorderLayout());
        start = new JButton("Start");
        start.addActionListener(new listen());
        add(start, BorderLayout.SOUTH);

        text = new JTextArea(6, 10);
        text.setEditable(false);
        add(text, BorderLayout.CENTER);
    }
    private class connection extends Thread
    {
        public void run()
        {
            try
            {
                // Sets up a server socket with set port//
                text.setText("Waiting for connection on port 16666");
                ServerSocket serversock = new ServerSocket(16666);

                // Waits for the Client to connect to the server//
                Socket connectionsock = serversock.accept();

                // Sets up Input from the Client to go to the server//
                clientinput = new BufferedReader(new InputStreamReader(connectionsock.getInputStream()));

                // Sets up data output to send data from server to client//
                clientoutput = new PrintWriter(connectionsock.getOutputStream(), true);

                // Test server to see if it can perform a simple task//
                text.setText("Connection made, waiting to client to send there name");
                clientoutput.println("Enter your name please.");

                // Get users name//
                String clienttext = clientinput.readLine();

                // Reply back to the client//
                String replytext = "Welcome " + clienttext;
                clientoutput.println(replytext);
                text.setText("Sent: " + replytext);

                while ((replytext = clientinput.readLine()) != null)
                {
                    clientoutput.println(replytext);
                    text.setText("Sent: " + replytext);
                }

                // If you need to close the Socket connections
                // clientoutput.close();
                // clientinput.close();
                // connetionsock.close();
                // serversock.close();

            }

            catch(IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args)
    {
        ServerTest test = new ServerTest();
        test.setVisible(true);
    }
}
