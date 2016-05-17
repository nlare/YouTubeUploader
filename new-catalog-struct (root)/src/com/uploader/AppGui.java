package com.uploader;

import java.util.*;
import java.util.regex.*;
import java.io.File;
import java.io.IOException;
import java.lang.*;
import java.net.*;
import java.io.PrintStream;

import java.lang.Object;

// import java.awt.*;
import java.awt.Dimension;
// import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.Date;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class AppGui extends JFrame  {

    public static String nameData;
    public static String numPagesData;
    public static String delayData;

    private JTextArea textArea;
    private JTextPane textComponent;

    private JPanel textPanel;

    private JButton buttonStart = new JButton("Start");
    private JButton buttonReauth = new JButton("Remove Credentials (Заново авторизоваться на YouTube)");

    private JTextField profileNameField = new JTextField();
    private JTextField delayField = new JTextField();

    private JLabel profileNameLabel = new JLabel("Videohive.net profile name (Имя профиля): ");
    private JLabel delayFieldLabel = new JLabel("Delay in minutes (Интервал между загрузками в минутах): ");

    private PrintStream standardOut;

    GridBagLayout layout;

    public AppGui()    {

        super("YouTubeUploader");

        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea(100, 10);
        textArea.setEditable(false);
        // textArea.setContentType("text/html;charset=UTF-8");

        // textComponent = new JTextPane(50, 10);

        textComponent = new JTextPane();
        textComponent.setEditable(false);
        textComponent.setContentType("text/html;charset=UTF-8");
        // textComponent.setFont(new java.awt.Font("Courier New", 0, 10));

        textComponent.setPreferredSize(new Dimension(100,800));

        textPanel = new JPanel();
        textPanel.setLayout(null);
        textPanel.setPreferredSize(new Dimension(200,200));

        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));

        standardOut = System.out;

        System.setOut(printStream);
        System.setErr(printStream);

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // constraints.gridx = 0;
        // constraints.gridy = 0;
        // constraints.insets = new Insets(5,5,5,5);
        constraints.anchor = GridBagConstraints.WEST;

        // add(buttonStart,constraints);

        // constraints.gridx = 1;
        // constraints.gridy = 0;

        // profileNameLabel.setFont(new Font("", Font.BOLD, 12));
        // profileNameField.setFont(new Font("", Font.BOLD, 12));
        // delayField.setFont(new Font("", Font.BOLD, 12));
        // delayFieldLabel.setFont(new Font("serif", Font.BOLD, 12));

        // Задаем размеры элементов
        profileNameLabel.setSize(new Dimension(450,20));
        delayFieldLabel.setSize(new Dimension(450,20));
        delayField.setPreferredSize(new Dimension(100,20));
        delayField.setPreferredSize(new Dimension(100,20));

        Box labelFieldBox1 = Box.createHorizontalBox();

        labelFieldBox1.add(profileNameLabel,constraints);
        labelFieldBox1.add(profileNameField,constraints);
        // labelFieldBox1.add(Box.createHorizontalGlue());

        // labelFieldBox1.setSize(new Dimension(1000,20));

        // constraints.gridx = 2;
        // constraints.gridy = 0;

        Box labelFieldBox2 = Box.createHorizontalBox();

        labelFieldBox2.add(delayFieldLabel,constraints);
        labelFieldBox2.add(delayField,constraints);
        labelFieldBox2.add(Box.createHorizontalGlue());

        Box buttonsBox = Box.createHorizontalBox();

        buttonsBox.add(buttonStart);
        buttonsBox.add(buttonReauth);
        buttonsBox.add(Box.createHorizontalGlue());

        // constraints.gridx = 1;
        // constraints.gridy = 0;
        // add(profileNameLabel,constraints);

        // constraints.gridx = 2;
        // constraints.gridy = 0;
        // add(profileNameField,constraints);

        // constraints.gridx = 1;
        // constraints.gridy = 1;
        // add(delayFieldLabel,constraints);

        // constraints.gridx = 2;
        // constraints.gridy = 1;
        // add(delayField,constraints);

        // constraints.gridx = 0;
        // constraints.gridy = 3;

        // constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;

        Box textAreaBox = Box.createHorizontalBox();
        textAreaBox.add(textArea);
        // textAreaBox.createHorizontalStrut(100);
        // textAreaBox.createVerticalStrut(500);

        Box mainBox = Box.createVerticalBox();

        // mainBox.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));

        // constraints.insets = new Insets(5,5,5,5);

        mainBox.add(labelFieldBox1,constraints);
        mainBox.add(Box.createHorizontalStrut(15));
        mainBox.add(Box.createVerticalStrut(10));
        
        mainBox.add(labelFieldBox2,constraints);
        mainBox.add(Box.createHorizontalStrut(15));
        mainBox.add(Box.createVerticalStrut(10));
        // mainBox.add(Box.createHorizontalGlue());

        mainBox.add(buttonsBox,constraints);
        mainBox.add(Box.createHorizontalStrut(15));
        mainBox.add(Box.createVerticalStrut(10));
        // mainBox.add(Box.createHorizontalGlue());

        mainBox.add(textAreaBox);
        // mainBox.add(textComponent);
        // mainBox.add(Box.createHorizontalGlue());
        mainBox.add(new JScrollPane(textArea), constraints);

        // MessageConsole mc = new MessageConsole(textComponent);

        // mc.redirectOut();
        // mc.redirectErr(Color.RED, null);

        setContentPane(mainBox);

        mainBox.setBorder(new EmptyBorder(10, 10, 10, 10));

        pack();

        setResizable(true);

        buttonStart.addActionListener(new ActionListener()  {

            public void actionPerformed(ActionEvent evt)    {

                String textInNameField = profileNameField.getText();
                String textInDelayField = delayField.getText();

                double textInDelayFieldInDouble = Double.parseDouble(textInDelayField);

                upload_hive_to_tube(textInNameField, textInDelayFieldInDouble);
                // System.out.println("I like that!");
            }
        });

        buttonReauth.addActionListener(new ActionListener()  {

            public void actionPerformed(ActionEvent evt)    {
                try {

                    Runtime.getRuntime().exec("remove-credentials.exe");
                    // try {
                    //     Files.delete(path);
                    // } catch (NoSuchFileException x) {
                    //     System.err.format("%s: no such" + " file or directory%n", path);
                    // } catch (DirectoryNotEmptyException x) {
                    //     System.err.format("%s not empty%n", path);
                    // } catch (IOException x) {
                    //     // File permission problems are caught here.
                    //     System.err.println(x);
                    // }

                }   catch(Exception e)   {

                    e.printStackTrace();

                }
                // System.out.println("I like that!");
            }
        });

    }

    private void upload_hive_to_tube(String _name_of_profile, double _delay_in_min)    {

        Thread thread = new Thread(new Runnable()   {

            public void run()   {
                // System.out.println("I like that!");

                HiveToYoutube h2t = new HiveToYoutube(_name_of_profile, _delay_in_min);
                h2t.GrabAndLoad();
            }

        });
        thread.start();
    }
    
    public static void main(String[] args)  {

        SwingUtilities.invokeLater(new Runnable()   {

            public void run()   {

                JFrame jfrm = new AppGui();

                jfrm.setSize(640,640);
                jfrm.setVisible(true);

            }

        });

    }

}


