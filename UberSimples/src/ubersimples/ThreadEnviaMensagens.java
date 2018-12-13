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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Crisanto
 */
public class ThreadEnviaMensagens extends Thread{
    ArrayList<Socket> listaClientes;
//    ArrayList<String> mensagensPorEnviar;
    SynchronizedArrayList mensagensPorEnviar;

    public ThreadEnviaMensagens(ArrayList<Socket> listaClientes, SynchronizedArrayList mensagensPorEnviar) {
        this.listaClientes = listaClientes;
        this.mensagensPorEnviar = mensagensPorEnviar;
    }

    @Override
    public void run() {

        // por aqui o Sleep de 5 segunds
        //TimeUnit.SECONDS.sleep(5);
        String outputLine;

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1); // default 5 !!
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadEnviaMensagens.class.getName()).log(Level.SEVERE, null, ex);
            }
            
//            for (String mensagem : this.mensagensPorEnviar.get()) { // para cada mensagem
              for (int i = 0;i < mensagensPorEnviar.getSize();i++){
                for (Socket item : this.listaClientes) { // para cada cliente

                    // outputLine = mensagem; // tenho que tratar a mensagem !! pus o processar mensagens no WorkerThread !!!!!!!

                    System.out.println(mensagensPorEnviar.get().get(i)); // para poder ver as Mensagens que serão enviadas para os Clientes na consola do servidor
                    PrintWriter out;
                    try {
                        out = new PrintWriter(item.getOutputStream(), true); // tipo o so para um mas vou iterando pela lista
                        out.println(mensagensPorEnviar.get().get(i));
                    } catch (IOException ex) {
                        Logger.getLogger(ThreadEnviaMensagens.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
            // depois tenho que tirar as mensagens enviadas 
            this.mensagensPorEnviar.clear(); // isto não deve ser a melhor solução
        }

    }

}
