/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Crisanto
 */
public class CondutorEnvia extends Thread {

    Socket echoSocket;
    //SynchronizedArrayList mensagemPorEnviarCondutor = new SynchronizedArrayList();
    SynchronizedArrayList mensagemPorEnviarCondutor; // tava como em cima mas acho que é antes assim
    ArrayList<String> activo;

    public CondutorEnvia(Socket echoSocket, SynchronizedArrayList mensagemPorEnviarCondutor, ArrayList<String> activo) {
        this.echoSocket = echoSocket;
        this.mensagemPorEnviarCondutor = mensagemPorEnviarCondutor;
        this.activo = activo;
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

        // BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); // tal como acontece no User este está a ler da consola e não é nessessario
        String envio;

        // criar aqui um loop infinito --- o MELHOR !!!! até aquela cena de ativo estar vazia(fica assim quando o user acaba a sua execução)
        while (!this.activo.isEmpty()) {

            try {
                sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(CondutorEnvia.class.getName()).log(Level.SEVERE, null, ex);
            }

            while (this.mensagemPorEnviarCondutor.getSize() != 0) { // enquanto tiver coisas para enviar
                envio = (String) this.mensagemPorEnviarCondutor.get().get(0);
                out.println(envio); // envia o mais recente 
                System.out.println("envia: " + envio);
                if (envio.equals("Bye")) { // criterio de saida [mudar ?!] agora tenho o this.activo.getSize() continuo a usar este !?!?
                    break;
                }
                this.mensagemPorEnviarCondutor.removeFromPosition(0);
            }

        }
        System.out.println("--- Thread Envia foi fechada ---");
        // stdIn.close();
        out.close();

        try {
            echoSocket.close(); //socket não será fechada aqui mas sim no programa principal
        } catch (IOException ex) {
            Logger.getLogger(CondutorEnvia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
