/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Crisanto
 */
public class CondutorEnvia extends Thread {

    Socket echoSocket;
    SynchronizedArrayList mensagemPorEnviarCondutor = new SynchronizedArrayList();

    public CondutorEnvia(Socket echoSocket, SynchronizedArrayList mensagemPorEnviarCondutor) {
        this.echoSocket = echoSocket;
        this.mensagemPorEnviarCondutor = mensagemPorEnviarCondutor;
    }

    @Override
    public void run() {
        // ENVIAR Condutor

        PrintWriter out = null;
        try {
            out = new PrintWriter(echoSocket.getOutputStream(), true); //para se obter um objeto do tipo PrintWriter
        } catch (IOException ex) {
            Logger.getLogger(CondutorEnvia.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String envio;
        try {
//            while ((envio = stdIn.readLine()) != null) {
//                out.println(envio); //o que eu mando/envio
//                if (envio.equals("Bye")) { // criterio de saida [mudar ?!]
//                    break;
//                }
//            }
            while (this.mensagemPorEnviarCondutor.getSize() != 0) { // enquanto tiver coisas para enviar
                envio = (String) this.mensagemPorEnviarCondutor.get().get(0);
                out.println(envio); // envia o mais recente 
                if (envio.equals("Bye")) { // criterio de saida [mudar ?!]
                    break;
                }
                this.mensagemPorEnviarCondutor.removeFromPosition(0);
            }
            System.out.println("Envia Closed");
            stdIn.close();
            out.close();
            echoSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(CondutorEnvia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
