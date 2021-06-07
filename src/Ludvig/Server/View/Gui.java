package Ludvig.Server.View;

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
 * The servers GUI
 * @version 1.0
 */
public class Gui extends JFrame implements ActionListener {
    private JPanel northPanel = new JPanel(), centerPanel = new JPanel(), southPanel = new JPanel(), nWestPanel, nEastPanel;
    private JTextArea txtArea = new JTextArea();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"), timeFormat = new SimpleDateFormat("HH:mm:ss");
    private JFormattedTextField tfFromDate, tfToDate, tfFromTime, tfToTime;
    private JLabel lblFromDate, lblToDate, lblFromTime, lblToTime, lblStart, lblStartTime, lblIpPort;
    private JButton btnSearch = new JButton("Search"), btnClear = new JButton("Clear");
    private Date date = new Date();

    public Gui(InetAddress ip, int port){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        System.out.println(ip + " : " + port);
        setPreferredSize(new Dimension(600,600));
        setLayout(new BorderLayout());
        txtArea.setPreferredSize(new Dimension(580, 450));
        centerPanel.add(txtArea);

        lblIpPort = new JLabel("Host: " + ip + " ::: Server Port: " + port);
        southPanel.add(lblIpPort);

        createNorthPanels();
        northPanel.setLayout(new GridLayout(1,2));
        northPanel.add(nWestPanel);
        northPanel.add(nEastPanel);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);


        setLocationByPlatform(true);
        pack();
        setVisible(true);
    }

    public void createNorthPanels(){
        tfFromDate = new JFormattedTextField(dateFormat);
        tfToDate = new JFormattedTextField(dateFormat);
        tfFromTime = new JFormattedTextField(timeFormat);
        tfToTime = new JFormattedTextField(timeFormat);

        nWestPanel = new JPanel();
        nWestPanel.setLayout(new GridLayout(2,4));
        tfFromDate.setText("");
        tfToDate.setText("");
        tfFromTime.setText("00:00:00");
        tfToTime.setText("23:59:59");

        lblFromDate = new JLabel("From date:");
        lblFromTime = new JLabel("From time:");
        lblToDate = new JLabel("To date:");
        lblToTime = new JLabel("To time:");

        nWestPanel.add(lblFromDate);
        nWestPanel.add(tfFromDate);
        nWestPanel.add(lblFromTime);
        nWestPanel.add(tfFromTime);

        nWestPanel.add(lblToDate);
        nWestPanel.add(tfToDate);
        nWestPanel.add(lblToTime);
        nWestPanel.add(tfToTime);

        nEastPanel = new JPanel();
        nEastPanel.setLayout(new GridLayout(2,2));
        lblStart = new JLabel("Server start time: ");
        lblStartTime = new JLabel(date.toString());

        btnSearch.addActionListener(this);
        btnClear.addActionListener(this);

        nEastPanel.add(lblStart);
        nEastPanel.add(lblStartTime);
        nEastPanel.add(btnSearch);
        nEastPanel.add(btnClear);
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
                        txtArea.append(o.toString() + "\n");
                        txtArea.append(ois.readObject() + "\n");
                    } else {
                        ois.readObject();
                    }
                }
                txtArea.append("END OF SEARCH\n");
            } catch (EOFException e) {
                txtArea.append("END OF FILE\n");
            } catch (ClassNotFoundException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * ActionListeners for buttons
     * @param e Source of call
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnClear){
            txtArea.setText("");
        }
        else if (e.getSource() == btnSearch){
            searchFile();
        }
    }
}
