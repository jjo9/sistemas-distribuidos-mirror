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

/**
 *
 * @author Crisanto
 */
public class ThreadClientes extends Thread {

    private Socket socket = null;
    ArrayList<Socket> listaCondutores;
    ArrayList<Socket> listaUsers;
    SynchronizedArrayList mensagensPorEnviar;
    SynchronizedArrayList historicoMensagens;

    
    public ThreadClientes(Socket acceptedSocket,ArrayList listaCondutores,ArrayList listaUsers,ArrayList credenciaisCondutores,ArrayList credenciaisUsers,SynchronizedArrayList mensagensPorEnviar,SynchronizedArrayList mensagensPorEnviarMulticast,SynchronizedArrayList historicoMensagens,ArrayList historicoPontos) {
        super("WorkerThread");
        this.socket = acceptedSocket;
        this.listalistaCondutores = listaCondutores;
        this.mensagensPorEnviar = mensagensPorEnviar;
        this.historicoMensagens = historicoMensagens;
    }

    @Override
    public void run() {

        System.out.print("run() Started!");
        listaClientes.add(socket); // adiciona cliente

        try {
            // para leitura do que o cliente envia para o server
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine;
            
            //System.out.println("Read  Created!");

            while ((inputLine = in.readLine()) != null) { // lê o que o cliente lhe envia
                
                
                String TreatedinputLine = new ProcessaMensagem(inputLine, socket).processa(); // ver se é Condutor ou User
                
                // o que faz com o que reve do cliente 
                // poem numa lista o que o cliente enviou
                this.mensagensPorEnviar.add(TreatedinputLine); // para depois fazer multicast
                this.historicoMensagens.add(TreatedinputLine); // para ficar no historico
                
                
                if (inputLine.equals("Bye")) {
                    listaClientes.remove(socket);
                    System.out.println("-------------------------------remove");
                    break;
                }
            }

            in.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
