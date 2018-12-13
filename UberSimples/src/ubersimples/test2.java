/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class test2 {
  public static void main(String args[]) {
    JFrame frame = new JFrame("test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel panel = new JPanel(new BorderLayout());
    JLabel label = new JLabel("op1");
    label.setDisplayedMnemonic(KeyEvent.VK_N);
    JTextField textField = new JTextField();
    label.setLabelFor(textField);
    panel.add(label, BorderLayout.WEST);
    panel.add(textField, BorderLayout.CENTER);
    frame.add(panel, BorderLayout.NORTH);
    frame.add(new JButton("Somewhere Else"), BorderLayout.SOUTH);
    frame.setSize(250, 150);
    frame.setVisible(true);

    textField.setText("your text");
    String filename = "test.txt";

    FileWriter writer = null;
    try {
      writer = new FileWriter(filename);
      textField.write(writer);
    } catch (IOException exception) {
      System.err.println("Save oops");
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException exception) {
          System.err.println("Error closing writer");
          exception.printStackTrace();
        }
      }
    }
  }
}