package com.noteS.dest;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Gui implements ActionListener {
    
    JFrame window;

    //Text Area
    JTextArea textArea;
    JScrollPane scrollPane;
    boolean wordWrapOn = false;

    // Top MENU BAR
    JMenuBar menuBar;
    JMenu menuFile, menuEdit, menuFormat, menuColor, menuNetwork;

    // FILE MENU
    JMenuItem iNew, iOpen, iSave, iSaveAs, iExit;

    // EDIT MENU
    JMenuItem iUndo, iRedo;

    // FORMAT MENU
    JMenuItem iWrap, iFontArial, iFontCSMS, iFontTNR, iFontSize8, iFontSize12, iFontSize16, iFontSize20, iFontSize24, iFontSize28;
    JMenu iFontSize, iFont;

    // COLOR MENU
    JMenuItem iColor1, iColor2, iColor3;

    Function_File file = new Function_File(this);
    Function_Format format = new Function_Format(this);
    Function_Color color = new Function_Color(this);
    Function_Edit edit = new Function_Edit(this);

    KeyHandler khandler = new KeyHandler(this);

    UndoManager um = new UndoManager();



    public Gui(){
        createWindow();
        createTextArea();
        createMenuBar();
        createFileMenu();
        createEditMenu();
        createFormatMenu();
        createColorMenu();
        createNetworkMenu();

        format.selectedFont = "Arial";
        format.createFont(16);
        format.wordWrap();
        color.changeColor("White");
        window.setVisible(true);
    }

    public void createWindow(){
        ImageIcon icon = new ImageIcon("E:\\Projects\\NoteScan\\Notepad\\images\\Pad.jpg");
        String ipAddress = getLocalIpAddress();
        String title = "NSPad" + (ipAddress != null ? " - " + ipAddress : "");

        window = new JFrame(title);
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setIconImage(icon.getImage());
    }

    private String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            String fallbackAddress = null;
            String wifiAddress = null;
            String ethernetAddress = null;

            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual()) continue;

                String displayName = iface.getDisplayName().toLowerCase();
                String name = iface.getName().toLowerCase();
                boolean isWifi = displayName.contains("wi-fi") || displayName.contains("wifi") || displayName.contains("wlan") || displayName.contains("wireless") || name.contains("wlan");
                boolean isEthernet = displayName.contains("ethernet") || displayName.contains("lan") || name.contains("eth");

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (!(addr instanceof Inet4Address)) continue;
                    if (addr.isLoopbackAddress()) continue;

                    String host = addr.getHostAddress();
                    if (!(host.startsWith("192.168.") || host.startsWith("10.") || host.startsWith("172."))) {
                        continue;
                    }

                    if (isWifi) {
                        return host;
                    }
                    if (isEthernet && ethernetAddress == null) {
                        ethernetAddress = host;
                    }
                    if (fallbackAddress == null) {
                        fallbackAddress = host;
                    }
                }
            }

            if (wifiAddress != null) {
                return wifiAddress;
            }
            if (ethernetAddress != null) {
                return ethernetAddress;
            }
            return fallbackAddress;
        } catch (SocketException e) {
            System.err.println("Unable to read network interfaces: " + e.getMessage());
        }
        return null;
    }

    public void createTextArea(){

        textArea = new JTextArea();
        textArea.setFont(format.arial);

        textArea.addKeyListener(khandler);

        textArea.getDocument().addUndoableEditListener(
                new UndoableEditListener() {
                    @Override
                    public void undoableEditHappened(UndoableEditEvent e) {
                        um.addEdit(e.getEdit());
                    }
                }
        );

        scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        window.add(scrollPane);

    }

    public void createMenuBar(){
        menuBar = new JMenuBar();
        window.setJMenuBar(menuBar);

        menuFile = new JMenu("File");
        menuBar.add(menuFile);

        menuEdit = new JMenu("Edit");
        menuBar.add(menuEdit);

        menuFormat = new JMenu("Format");
        menuBar.add(menuFormat);

        menuColor = new JMenu("Color");
        menuBar.add(menuColor);
    }

    public void createFileMenu(){
        iNew = new JMenuItem("New");
        iNew.addActionListener(this);
        iNew.setActionCommand("New");
        menuFile.add(iNew);

        iOpen = new JMenuItem("Open");
        iOpen.addActionListener(this);
        iOpen.setActionCommand("Open");
        menuFile.add(iOpen);

        iSave = new JMenuItem("Save");
        iSave.addActionListener(this);
        iSave.setActionCommand("Save");
        menuFile.add(iSave);

        iSaveAs = new JMenuItem("Save As");
        iSaveAs.addActionListener(this);
        iSaveAs.setActionCommand("Save As");
        menuFile.add(iSaveAs);

        iExit = new JMenuItem("Exit");
        iExit.addActionListener(this);
        iExit.setActionCommand("Exit");
        menuFile.add(iExit);
    }

    public void createEditMenu(){

        iUndo = new JMenuItem("Undo");
        iUndo.addActionListener(this);
        iUndo.setActionCommand("Undo");
        menuEdit.add(iUndo);

        iRedo = new JMenuItem("Redo");
        iRedo.addActionListener(this);
        iRedo.setActionCommand("Redo");
        menuEdit.add(iRedo);
    }

    public void createFormatMenu(){
        iWrap = new JMenuItem("Word Wrap: Off");
        iWrap.addActionListener(this);
        iWrap.setActionCommand("Word Wrap");
        menuFormat.add(iWrap);

        iFont = new JMenu("Font");
        menuFormat.add(iFont);

        iFontArial = new JMenuItem("Arial");
        iFontArial.addActionListener(this);
        iFontArial.setActionCommand("Arial");
        iFont.add(iFontArial);

        iFontCSMS = new JMenuItem("Comic Sans MS");
        iFontCSMS.addActionListener(this);
        iFontCSMS.setActionCommand("Comic Sans MS");
        iFont.add(iFontCSMS);

        iFontTNR = new JMenuItem("Times New Roman");
        iFontTNR.addActionListener(this);
        iFontTNR.setActionCommand("Times New Roman");
        iFont.add(iFontTNR);

        iFontSize = new JMenu("Font Size");
        menuFormat.add(iFontSize);

        iFontSize8 = new JMenuItem("8");
        iFontSize8.addActionListener(this);
        iFontSize8.setActionCommand("size8");
        iFontSize.add(iFontSize8);

        iFontSize12 = new JMenuItem("12");
        iFontSize12.addActionListener(this);
        iFontSize12.setActionCommand("size12");
        iFontSize.add(iFontSize12);

        iFontSize16 = new JMenuItem("16");
        iFontSize16.addActionListener(this);
        iFontSize16.setActionCommand("size16");
        iFontSize.add(iFontSize16);

        iFontSize20 = new JMenuItem("20");
        iFontSize20.addActionListener(this);
        iFontSize20.setActionCommand("size20");
        iFontSize.add(iFontSize20);

        iFontSize24 = new JMenuItem("24");
        iFontSize24.addActionListener(this);
        iFontSize24.setActionCommand("size24");
        iFontSize.add(iFontSize24);

        iFontSize28 = new JMenuItem("28");
        iFontSize28.addActionListener(this);
        iFontSize28.setActionCommand("size28");
        iFontSize.add(iFontSize28);

    }

    public void createColorMenu(){
        iColor1 = new JMenuItem("White");
        iColor1.addActionListener(this);
        iColor1.setActionCommand("White");
        menuColor.add(iColor1);

        iColor2 = new JMenuItem("Black");
        iColor2.addActionListener(this);
        iColor2.setActionCommand("Black");
        menuColor.add(iColor2);

        iColor3 = new JMenuItem("Red");
        iColor3.addActionListener(this);
        iColor3.setActionCommand("Red");
        menuColor.add(iColor3);
    }

    public void createNetworkMenu(){
        menuNetwork = new JMenu("Network");
        menuBar.add(menuNetwork);

        JMenuItem iRefreshIp = new JMenuItem("Refresh IP");
        iRefreshIp.addActionListener(this);
        iRefreshIp.setActionCommand("Refresh IP");
        menuNetwork.add(iRefreshIp);
    }

    private void updateWindowTitle() {
        String ipAddress = getLocalIpAddress();
        String title = "NSPad" + (ipAddress != null ? " - " + ipAddress : " - IP not found");
        window.setTitle(title);
    }

    public void appendTextFromPhone(String newText) {
        // Replace 'textArea' with whatever you named your JTextArea!
        this.textArea.append(newText + "\n\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // handle menu actions here

        String command = e.getActionCommand();

        switch(command){
            case "New" : file.newFile(); break;
            case "Open" : file.open(); break;
            case "Save" : file.save(); break;
            case "Save As" : file.saveAs(); break;
            case "Exit" : file.exit(); break;
            case "Undo" : edit.undo(); break;
            case "Redo" : edit.redo(); break;
            case "Word Wrap" : format.wordWrap(); break;
            case "Arial" : format.setFont(command); break;
            case "Comic Sans MS" : format.setFont(command); break;
            case "Times New Roman" : format.setFont(command); break;
            case "size8" : format.createFont(8); break;
            case "size12" : format.createFont(12); break;
            case "size16" : format.createFont(16); break;
            case "size20" : format.createFont(20); break;
            case "size24" : format.createFont(24); break;
            case "size28" : format.createFont(28); break;
            case "White" : color.changeColor(command); break;
        case "Black" : color.changeColor(command); break;
        case "Red" : color.changeColor(command); break;
        case "Refresh IP" : updateWindowTitle(); break;
        }
    }
}
