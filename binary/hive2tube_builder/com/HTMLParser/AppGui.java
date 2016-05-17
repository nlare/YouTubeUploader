package com.HTMLParser;

import java.util.*;
import java.util.regex.*;
import java.io.File;
import java.io.IOException;
import java.lang.*;
import java.net.*;

import java.awt.*;
// import java.awt.event.*;
import javax.swing.*;

public class AppGui extends JFrame  {

    public static String nameData;
    public static String numPagesData;
    public static String delayData;

    GridBagLayout layout;
    
    public AppGui() {

        super("First Swing App");

        // layout = new GridBagLayout();
        // JPanel pane = new JPanel(layout);
        // GridBagConstraints c = new GridBagConstraints();

        // setBounds(100,100,200,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JLabel nameLabel = new JLabel("Name: ");
        // JTextField nameLabelInput = new JTextField(20);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.fill = GridBagConstraints.HORIZONTAL;
        // c.gridy(0);
        // c.fill = GridBagConstraints.HORIZONTAL;
        // c.weightx = 10.0;
        // c.weighty = 4.0;

        // add(nameLabel, c);

        // pane.add(nameLabelInput, c);


    }

}


