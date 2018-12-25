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
public class UserEnvia extends Thread {

    Socket echoSocket;
    //SynchronizedArrayList mensagemPorEnviarUser = new SynchronizedArrayList();
    SynchronizedArrayList mensagemPorEnviarUser;
    SynchronizedArrayList ativo;

    public UserEnvia(Socket echoSocket, SynchronizedArrayList mensagemPorEnviarUser, SynchronizedArrayList ativo) {
        this.echoSocket = echoSocket;
        this.mensagemPorEnviarUser = mensagemPorEnviarUser;
        this.ativo = ativo;
    }

    @Override
    public void run() {
        // ENVIAR User

        PrintWriter out = null;
        try {
            out = new PrintWriter(echoSocket.getOutputStream(), true); //para se obter um objeto do tipo PrintWriter
        } catch (IOException ex) {
            Logger.getLogger(CondutorEnvia.class.getName()).log(Level.SEVERE, null, ex);
        }

        // --- isto está a usar a consola para ler input e não devia ter nada porque para 
        // enviar é pelo socket e lê da lista de mensagens por enviar
        //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String envio; // ---- tenho que por o que se segue num loop infinito ---- tenho que por o que se segue num loop infinito ---- tenho que por o que se segue num loop infinito 

        // also por um sleep de 1 segundo !
        while (this.ativo.getSize() != 0) {
            try {
                sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(UserEnvia.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
//            while ((envio = stdIn.readLine()) != null) {
//                out.println(envio); //o que eu mando/envio
//                if (envio.equals("Bye")) { // criterio de saida [mudar ?!]
//                    break;
//                }
//            }
                while (this.mensagemPorEnviarUser.getSize() != 0) { // enquanto tiver cenas para enviar
                    envio = (String) this.mensagemPorEnviarUser.get().get(0);
                    out.println(envio); // envia o mais recente 
                    if (envio.equals("Bye")) { // criterio de saida [mudar ?!]
                        break;
                    }
                    this.mensagemPorEnviarUser.removeFromPosition(0);
                }
                System.out.println("Envia Closed");  // ------ esta linha é ativada ------ esta linha é ativada ------ esta linha é ativada ------ esta linha é ativada ------ esta linha é ativada 
                //stdIn.close();
                out.close();
                echoSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(CondutorEnvia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
