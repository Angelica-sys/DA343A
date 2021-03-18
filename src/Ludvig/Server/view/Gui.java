package Ludvig.Server.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Gui is a JFrame-sub class that shows the Server log with options to check
 * the log between specified dates and timestamps
 * @version 1.0
 */
public class Gui extends JFrame implements ActionListener {
    private JPanel northPanel = new JPanel(), centerPanel = new JPanel(), southPanel = new JPanel(), northWestPanel, northEastPanel;
    private JTextArea textArea = new JTextArea();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"), timeFormat = new SimpleDateFormat("HH:mm:ss");
    private JFormattedTextField tfFromDate, tfToDate, tfFromTime, tfToTime;
    private JLabel labelFromDate, labelToDate, labelFromTime, labelToTime, labelStart, labelStartTime, hostIPandPort;
    private JButton search = new JButton("Search"), clear = new JButton("Clear");
    private Date date = new Date();

    public Gui(InetAddress ip, int port){
        System.out.println(ip + " : " + port);
        setPreferredSize(new Dimension(600,600));
        setLayout(new BorderLayout());
        textArea.setPreferredSize(new Dimension(580, 450));
        centerPanel.add(textArea);

        hostIPandPort = new JLabel("Host: " + ip + " ::: Server Port: " + port);
        southPanel.add(hostIPandPort);

        createNorthPanels();
        northPanel.setLayout(new GridLayout(1,2));
        northPanel.add(northWestPanel);
        northPanel.add(northEastPanel);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);


        setLocationByPlatform(true);
        pack();
        setVisible(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    public void createNorthPanels(){
        tfFromDate = new JFormattedTextField(dateFormat);
        tfToDate = new JFormattedTextField(dateFormat);
        tfFromTime = new JFormattedTextField(timeFormat);
        tfToTime = new JFormattedTextField(timeFormat);

        northWestPanel = new JPanel();
        northWestPanel.setLayout(new GridLayout(2,4));
        tfFromDate.setText("");
        tfToDate.setText("");
        tfFromTime.setText("00:00:00");
        tfToTime.setText("23:59:59");

        labelFromDate = new JLabel("From date:");
        labelFromTime = new JLabel("From time:");
        labelToDate = new JLabel("To date:");
        labelToTime = new JLabel("To time:");

        northWestPanel.add(labelFromDate);
        northWestPanel.add(tfFromDate);
        northWestPanel.add(labelFromTime);
        northWestPanel.add(tfFromTime);

        northWestPanel.add(labelToDate);
        northWestPanel.add(tfToDate);
        northWestPanel.add(labelToTime);
        northWestPanel.add(tfToTime);

        northEastPanel = new JPanel();
        northEastPanel.setLayout(new GridLayout(2,2));
        labelStart = new JLabel("Server start time: ");
        labelStartTime = new JLabel(date.toString());

        search.addActionListener(this);
        clear.addActionListener(this);

        northEastPanel.add(labelStart);
        northEastPanel.add(labelStartTime);
        northEastPanel.add(search);
        northEastPanel.add(clear);
    }

    public void searchFile(){
        ObjectInputStream ois;

        String[] readDate = (tfFromDate.getText() + "-" + tfToDate.getText()).split("-");
        String[] readTime = (tfFromTime.getText() + ":" + tfToTime.getText()).split(":");

        int[] intDate = new int[6];
        int[] intTime = new int[6];
        if(readDate.length>0 && readTime.length>0) {
            for (int i = 0; i < 6; i++) {
                intDate[i] = Integer.parseInt(readDate[i]);
                intTime[i] = Integer.parseInt(readTime[i]);
            }

            Date fromDate = new Date(intDate[0] - 1900, intDate[1] - 1, intDate[2], intTime[0], intTime[1], intTime[2]);
            Date toDate = new Date(intDate[3] - 1900, intDate[4] - 1, intDate[5], intTime[3], intTime[4], intTime[5] + 1);

            Object o;
            try {
                ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("files/ServerLog.dat")));
                while ((o = ois.readObject()) != null) {
                    if ((((Date) o).after(fromDate)) && ((Date) o).before(toDate)) {
                        textArea.append(o.toString() + "\n");
                        textArea.append(ois.readObject() + "\n");
                    } else {
                        ois.readObject();
                    }
                }
                textArea.append("END OF SEARCH\n");
            } catch (EOFException e) {
                textArea.append("END OF FILE\n");
            } catch (ClassNotFoundException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear){
            textArea.setText("");
        }
        else if (e.getSource() == search){
            searchFile();
        }
    }
}
