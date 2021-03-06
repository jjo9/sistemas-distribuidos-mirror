/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Crisanto
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        ServerSocket serverSocket = null;
        int port = 7777;
        boolean listening = true;
        
        ArrayList listaCondutores = new ArrayList(); // guarda socket de condutores
        ArrayList listaUsers = new ArrayList(); // guarda socket de users
        ArrayList historicoPontos = new ArrayList(); // [quem submeteu(condutor/user),para quem(condutor/user),pontuação]
        ArrayList credenciaisCondutores  = new ArrayList(); // [username,password]
        ArrayList credenciaisUsers  = new ArrayList(); // [username,password]
        // será que devia ser sincornized ?
        ArrayList<Integer> clientsCount  = new ArrayList(); // [Total,Condutores,Users]
        clientsCount.add(0);
        clientsCount.add(0);
        clientsCount.add(0);
        
        USClist listaUserSocket = new USClist(); // lista que cria a relação entre sockets e usernames tanto dos Users como dos Condutores
        
        SynchronizedArrayList mensagensPorEnviar = new SynchronizedArrayList(); // este não vai ser usado por causa da descontinuação de "ThreadEnviaMensagens"
        SynchronizedArrayList mensagensPorEnviarMulticast = new SynchronizedArrayList(); // mensagens contendo pacotes a informar que "existe um pedido de viagem" e "um pedido de viagem já foi tomado"
        SynchronizedArrayList historicoMensagens = new SynchronizedArrayList(); // guarda todas as mensagens enviadas e recebidas
        SynchronizedArrayList pedidosDeViagens = new SynchronizedArrayList(); // formato "UserQuePediu/Origem/Destino"
        
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server Socket Created " + serverSocket);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port + ".");
            System.exit(-1);
        }
        
        System.out.println("port: " + port);
        
        new ThreadEnviaMensagens(listaCondutores,listaUsers,mensagensPorEnviar,historicoMensagens,listaUserSocket).start(); // envia mensagens tanto para Condutores como para Users (a escolha é processada lá dentro)
        new CondutorMulticast(mensagensPorEnviarMulticast,historicoMensagens).start();     // multicast que envia para os condutores todos
        
        while (listening) { // onde fica preso à espera de clientes
            // capturador de clientes
            Socket acceptedSocket = serverSocket.accept(); // recever clientes e o que eles enviam
            new ThreadClientes(clientsCount,acceptedSocket,listaCondutores,listaUsers,credenciaisCondutores,credenciaisUsers,listaUserSocket,mensagensPorEnviar,mensagensPorEnviarMulticast,historicoMensagens,historicoPontos,pedidosDeViagens).start();
        }
        
        // se ouver um "Exception in thread "WorkerThread" java.lang.NullPointerException
	// at ubersimples.ThreadClientes.run(ThreadClientes.java:83)"
        // será que dá para o tirar da lista de clientes ativos ??

        serverSocket.close();
        
        // por os processos todos a parar quando a execução acabar tanto para o SERVER como para os CONDUTORES como para os USERS
        
    }
    
}
