package com.lyrasaurus.ciphereditor;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.text.*;

// Set up the Editor
class editor extends JFrame implements ActionListener {
    // Text component
    JTextArea t;

    // Frame
    JFrame f;

    // GUI
    editor()
    {
        // Create a frame
        f = new JFrame("Lyra's Cipher Editor");
        Cipher cipher = new Cipher(3);

        // actually close the program when we hit the "x" button. Not sure why this didn't work for default
        f.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                initEditor();
                System.exit(0);
            }
        });

        // Text component
        t = new JTextArea();
        t.setLineWrap(true);
        t.setWrapStyleWord(true);

        // Scrollbar
        JScrollPane scrollPane = new JScrollPane(t);

        // Panel for Buttons
        JPanel panel = new JPanel();

        // Cipher button and action for it
        JButton cipherButton = new JButton("Cipher Selected");
        cipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (t.getSelectedText() != null) { // See if they selected something
                    String s = t.getSelectedText();
                    String cipheredString = cipher.caesar(s);

                    int start = t.getSelectionStart();
                    int end = t.getSelectionEnd();
                    StringBuilder strBuilder = new StringBuilder(t.getText());
                    strBuilder.replace(start, end, cipheredString);
                    t.setText(strBuilder.toString());
                } else {
                    JOptionPane.showMessageDialog(f, "No text is selected");
                }
            }
        });

        // Decipher button and action for it
        JButton decipherButton = new JButton("Decipher Selected");
        decipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (t.getSelectedText() != null) { // See if they selected something
                    String s = t.getSelectedText();
                    String decipheredString = cipher.undoCaesar(s);

                    int start = t.getSelectionStart();
                    int end = t.getSelectionEnd();
                    StringBuilder strBuilder = new StringBuilder(t.getText());
                    strBuilder.replace(start, end, decipheredString);
                    t.setText(strBuilder.toString());
                } else {
                    JOptionPane.showMessageDialog(f, "No text is selected");
                }
            }
        });

        // Create a menu bar
        JMenuBar mb = new JMenuBar();

        // Create a menu for menu
        JMenu m1 = new JMenu("File");

        // Create menu items
        JMenuItem mi1 = new JMenuItem("New");
        JMenuItem mi2 = new JMenuItem("Open");
        JMenuItem mi3 = new JMenuItem("Save");

        // Add action listeners
        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);

        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);

        // Create a menu for menu
        JMenu m2 = new JMenu("Edit");

        // Create menu items
        JMenuItem mi4 = new JMenuItem("Cut");
        JMenuItem mi5 = new JMenuItem("Copy");
        JMenuItem mi6 = new JMenuItem("Paste");

        // Add action listener
        mi4.addActionListener(this);
        mi5.addActionListener(this);
        mi6.addActionListener(this);

        m2.add(mi4);
        m2.add(mi5);
        m2.add(mi6);

        mb.add(m1);
        mb.add(m2);

        // add buttons to panel
        panel.add(cipherButton, BorderLayout.LINE_END);
        panel.add(decipherButton, BorderLayout.LINE_END);

        // add everything to the frame
        f.add(panel, BorderLayout.SOUTH);
        f.setJMenuBar(mb);
        f.add(scrollPane);
        f.setSize(500, 500);
        f.setVisible(true);
    }

    // Class to encrypt and decrypt text
    public static class Cipher
    {
        int offset;

        // Constructor
        public Cipher(int offset)
        {
            this.offset = offset;
        }

        public String caesar(String input) {
            String tobeEncrypted = input;
            int shift = this.offset;
            String cipheredString = "";

            // Encrypt the string using caesar cipher
            for (int i = 0; i < tobeEncrypted.length(); i++) {
                cipheredString += Character.toString(tobeEncrypted.charAt(i) + shift);
            }
            return cipheredString;
        }

        public String undoCaesar(String input) {
            String tobeDecrypted = input;
            int shift = this.offset;
            String decipheredString = "";

            // Decrypt the string using caesar cipher
            for (int i = 0; i < tobeDecrypted.length(); i++) {
                decipheredString += Character.toString(tobeDecrypted.charAt(i) - shift);
            }
            return decipheredString;
        }

    }

    // Function to save a file
    public void saveFile() {
        // Create an object of JFileChooser class
        JFileChooser j = new JFileChooser("f:");

        // Invoke the showsSaveDialog function to show the save dialog
        int r = j.showSaveDialog(null);

        if (r == JFileChooser.APPROVE_OPTION) {

            // Set the label to the path of the selected directory
            File fi = new File(j.getSelectedFile().getAbsolutePath());

            try {
                // Create a file writer
                FileWriter wr = new FileWriter(fi, false);

                // Create buffered writer to write
                BufferedWriter w = new BufferedWriter(wr);

                // Write
                w.write(t.getText());

                w.flush();
                w.close();
            }
            catch (Exception evt) {
                JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        }
        // If the user cancelled the operation
        else
            JOptionPane.showMessageDialog(f, "Operation Cancelled");
    }

    // Function to open a file
    public void openFile() {
        // Create an object of JFileChooser class
        JFileChooser j = new JFileChooser("f:");

        // Invoke the showsOpenDialog function to show the open dialog
        int r = j.showOpenDialog(null);

        // If the user selects a file
        if (r == JFileChooser.APPROVE_OPTION) {
            // Set the label to the path of the selected directory
            File fi = new File(j.getSelectedFile().getAbsolutePath());

            try {
                // String
                String s1 = "", sl = "";

                // File reader
                FileReader fr = new FileReader(fi);

                // Buffered reader
                BufferedReader br = new BufferedReader(fr);

                // Initialize sl
                sl = br.readLine();

                // Take the input from the file
                while ((s1 = br.readLine()) != null) {
                    sl = sl + "\n" + s1;
                }

                // Set the text
                t.setText(sl);
            }
            catch (Exception evt) {
                JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        }
        // If the user cancelled the operation
        else
            JOptionPane.showMessageDialog(f, "Operation Canceled");
    }

    // Ask to save before the editor gets nuked
    public void initEditor() {
        int n = JOptionPane.showConfirmDialog(
                f,
                "You're about to initialize the editor or close. Save first?",
                "Initialize",
                JOptionPane.YES_NO_OPTION);
        if (n == 0) {
            // Create an object of JFileChooser class
            JFileChooser j = new JFileChooser("f:");

            // Invoke the showsSaveDialog function to show the save dialog
            int r = j.showSaveDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {

                // Set the label to the path of the selected directory
                File fi = new File(j.getSelectedFile().getAbsolutePath());

                try {
                    // Create a file writer
                    FileWriter wr = new FileWriter(fi, false);

                    // Create buffered writer to write
                    BufferedWriter w = new BufferedWriter(wr);

                    // Write
                    w.write(t.getText());

                    w.flush();
                    w.close();
                    t.setText("");
                }
                catch (Exception evt) {
                    JOptionPane.showMessageDialog(f, evt.getMessage());
                }
            }
            // If the user cancelled the operation
            else
                JOptionPane.showMessageDialog(f, "the user cancelled the operation");
        }
        else {
            t.setText("");
        }
    }

    // If a button is pressed on the menu
    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();

        if (s.equals("Cut")) {
            t.cut();
        }
        else if (s.equals("Copy")) {
            t.copy();
        }
        else if (s.equals("Paste")) {
            t.paste();
        }
        else if (s.equals("Save")) {
            saveFile();
        }
        else if (s.equals("Open")) {
            openFile();
        }
        else if (s.equals("New")) {
            initEditor();
        }
    }

    // Main class
    public static void main(String args[])
    {
        editor e = new editor();
    }
}

