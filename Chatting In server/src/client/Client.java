package client;


import javax.swing.*;
import javax.swing.text.DefaultCaret;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.Date;
import java.net.*;
import java.io.*;

public class Client extends JFrame {
    private JTextArea showtext;
    private JTextField usertext;
    private String hostname;
    private Socket connectionsock;
    private BufferedReader serverinput;
    private PrintWriter serveroutput;
    private int port;


    private static final long serialVersionUID = 1L;

    private class entersend implements KeyListener {
        public void keyTyped(KeyEvent e) {

        }

        public void keyPressed(KeyEvent e) {
            String text = usertext.getText();

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (usertext.getText().equals(""))
                    return;
                else {
                    //try{
                    serveroutput.println(text);

                    usertext.setText("");
                    //showtext.append("[" + ft.format(dNow) + "]" + "\n" + "UserName: " + serverdata +"\n\n");
                    //}
                    /*catch(IOException in)
                    {
                        showtext.append(in.getMessage());
                    }*/

                }
            } else
                return;
        }

        public void keyReleased(KeyEvent e) {

        }
    }

    private class sender implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (usertext.getText().equals(""))
                return;
            else {
                Date dNow = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
                String text = usertext.getText();
                usertext.setText("");

                //* Send message to server


                showtext.append(ft.format(dNow) + "\n" + "UserName: " + text + "\n\n");
            }
        }
    }

    private class startconnection implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            clientconnection connect = new clientconnection();
            connect.start();
        }
    }

    private class CheckOnExit implements WindowListener {
        public void windowOpened(WindowEvent e) {
        }

        public void windowClosing(WindowEvent e) {
            ConfirmWindow checkers = new ConfirmWindow();
            checkers.setVisible(true);
        }

        public void windowClosed(WindowEvent e) {
        }

        public void windowIconified(WindowEvent e) {
        }

        public void windowDeiconified(WindowEvent e) {
        }

        public void windowActivated(WindowEvent e) {
        }

        public void windowDeactivated(WindowEvent e) {
        }
    }


    private class ConfirmWindow extends JFrame implements ActionListener {

        private static final long serialVersionUID = 1L;

        public ConfirmWindow() {
            setSize(200, 100);
            getContentPane().setBackground(Color.LIGHT_GRAY);
            setLayout(new BorderLayout());

            JLabel confirmLabel = new JLabel(
                    "Are you sure you want to exit?");
            add(confirmLabel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(Color.GRAY);
            buttonPanel.setLayout(new FlowLayout());

            JButton exitButton = new JButton("Yes");
            exitButton.addActionListener(this);
            buttonPanel.add(exitButton);

            JButton cancelButton = new JButton("No");
            cancelButton.addActionListener(this);
            buttonPanel.add(cancelButton);

            add(buttonPanel, BorderLayout.SOUTH);
        }

        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();

            if (actionCommand.equals("Yes"))
                System.exit(0);
            else if (actionCommand.equals("No"))
                dispose();
            else
                System.out.println("Unexpected Error in Confirm Window.");
        }
    }


    public static void main(String[] args) {

        Client client = new Client();
        client.setVisible(true);

    }

    public Client() {
        setTitle("Chating!");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(550, 400);
        setResizable(false);
        addWindowListener(new CheckOnExit());
        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                usertext.requestFocus();
            }
        });
        getContentPane().setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        showtext = new JTextArea(15, 35);
        showtext.setEditable(false);
        showtext.setLineWrap(true);
        DefaultCaret caret = (DefaultCaret) showtext.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);


        JScrollPane scrollbar = new JScrollPane(showtext);
        scrollbar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollbar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        //c.gridwidth = 2;
        c.insets = new Insets(20, 20, 20, 20);
        c.weightx = .5;
        c.weighty = .5;
        c.gridx = 0;
        c.gridy = 0;
        add(scrollbar, c);

        usertext = new JTextField(35);

        usertext.requestFocusInWindow();
        usertext.addKeyListener(new entersend());
        //c.gridwidth = 1;
        c.insets = new Insets(0, 20, 0, 0);
        c.weightx = .5;
        c.gridx = 0;
        c.gridy = 1;
        add(usertext, c);

        JPanel empty = new JPanel();
        c.gridx = 1;
        c.gridy = 0;
        empty.setLayout(new GridLayout(2, 1));

        JButton test1 = new JButton("test1");
        empty.add(test1);

        JButton test2 = new JButton("test2");
        empty.add(test2);

        add(empty, c);

        JButton send = new JButton("Send");
        send.addActionListener(new sender());
        c.ipady = -5;
        c.insets = new Insets(0, 0, 20, 0);
        c.gridx = 1;
        c.gridy = 1;
        add(send, c);

        JMenu menu = new JMenu("File");
        JMenuItem connection = new JMenuItem("Connect");
        connection.addActionListener(new startconnection());
        menu.add(connection);

        JMenuBar bar = new JMenuBar();
        bar.add(menu);
        setJMenuBar(bar);


    }

    private class clientconnection extends Thread {
        public void run() {
            try {
                // Set host name and port//
                hostname = "localhost";
                port = 16666;

                // connect to socket of the server//
                connectionsock = new Socket(hostname, port);

                // set up client input from server//
                serverinput = new BufferedReader(new InputStreamReader(connectionsock.getInputStream()));

                // set up client output to server//
                serveroutput = new PrintWriter(connectionsock.getOutputStream(), true);

                // set up a looping thread to constantly check if server has sent anything
                String serverdata = "";

                while ((serverdata = serverinput.readLine()) != null) {
                    Thread.sleep(500);
                    Date dNow = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
                    showtext.append("[" + ft.format(dNow) + "]" + "\n" + "Recieved from server: \n" + "UserName: " + serverdata + "\n\n");
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException inter) {
                showtext.append("Unexpected interruption\n\n");
            }
        }


    }
}