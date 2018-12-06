/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

/**
 *
 * @author buize
 */

import javax.swing.JOptionPane;

public class teste_menu {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String nome = JOptionPane.showInputDialog("ensira o nome de utilizador");
        String pass = JOptionPane.showInputDialog("ensira a password");
        
        System.out.println(nome);
        System.out.println(pass);
        
    }
    
}
