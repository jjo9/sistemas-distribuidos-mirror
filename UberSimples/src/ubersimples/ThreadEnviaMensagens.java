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
public class ThreadEnviaMensagens extends Thread {

    ArrayList<Socket> listaCondutores;
    ArrayList<Socket> listaUsers;
    SynchronizedArrayList mensagensPorEnviar;
    SynchronizedArrayList historicoMensagens;

    public ThreadEnviaMensagens(ArrayList<Socket> listaCondutores, ArrayList<Socket> listaUsers, SynchronizedArrayList mensagensPorEnviar, SynchronizedArrayList historicoMensagens) {
        this.listaCondutores = listaCondutores;
        this.listaUsers = listaUsers;
        this.mensagensPorEnviar = mensagensPorEnviar;
        this.historicoMensagens = historicoMensagens;
    }

    @Override
    public void run() {  // !!!!!!!!!!!!!!!!! pagina descontinuada  !!!!!!!!!!!!!!!!!  
                        // esta pagina não é preciso para nada pois temos o Multicast e as resposta são feitas só para o user que enviou o pedido ou seja, 1 para 1

        // por aqui o Sleep de 5 segunds
        //TimeUnit.SECONDS.sleep(5);
        String outputLine;

        while (true) { // tenho que ter 2 loops para os CONDUTORES e USERS \- não para cada mensagem tenho que ver se é USER ou CONDUTOR
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadEnviaMensagens.class.getName()).log(Level.SEVERE, null, ex);
            }
            // ------ CONDUTOR ------
//            for (String mensagem : this.mensagensPorEnviar.get()) { // para cada mensagem
            for (int i = 0; i < mensagensPorEnviar.getSize(); i++) {
                // ver se é CONDUTOR ou USER
                if ( == "condutor") {
                    for (Socket item : this.listaCondutores) { // para cada CONDUTOR
                        System.out.println(mensagensPorEnviar.get().get(i)); // para poder ver as Mensagens que serão enviadas para os Clientes na consola do servidor
                        PrintWriter out;
                        try {
                            out = new PrintWriter(item.getOutputStream(), true); // tipo o so para um mas vou iterando pela lista
                            out.println(mensagensPorEnviar.get().get(i));
                        } catch (IOException ex) {
                            Logger.getLogger(ThreadEnviaMensagens.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }else{
                    for (Socket item : this.listaUsers) { // para cada USER
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
            }
            // depois de enviar as mensagens todas tenho que tirar as mensagens
            this.mensagensPorEnviar.clear(); // isto não deve ser a melhor solução

        }

    }

}
