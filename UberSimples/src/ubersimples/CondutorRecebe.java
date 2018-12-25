/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Crisanto
 */
public class CondutorRecebe extends Thread {

    Socket echoSocket = null;
    SynchronizedArrayList mensagemRecebidasCondutor;
    ArrayList estado;

    public CondutorRecebe(Socket echoSocket, SynchronizedArrayList mensagemRecebidasCondutor, ArrayList estado) {
        this.echoSocket = echoSocket;
        this.mensagemRecebidasCondutor = mensagemRecebidasCondutor;
        this.estado = estado;
    }

    @Override
    public void run() {
        String recebo;

        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(this.echoSocket.getInputStream())); // para se obter um objeto do tipo BufferedReader
            while (((recebo = in.readLine()) != null) && (this.echoSocket != null)) {
                if (!this.estado.isEmpty()) { // modo de não incomodar 
                    // recebo = in.readLine();// o que eu recebo
                    this.mensagemRecebidasCondutor.add(recebo);
                    System.out.println("recebo: " + recebo);
                }
            }
            //System.out.println("Recebe Closed");
            in.close();
            this.echoSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(CondutorRecebe.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
