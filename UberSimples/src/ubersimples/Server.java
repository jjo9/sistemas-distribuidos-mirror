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
        
        ArrayList listaCondutores = new ArrayList();
        ArrayList listaUsers = new ArrayList();
        ArrayList historicoPontos = new ArrayList(); // [quem submeteu(condutor/user),para quem(condutor/user),pontuação]
        ArrayList credenciaisCondutores  = new ArrayList(); // [username,password]
        ArrayList credenciaisUsers  = new ArrayList(); // [username,password]
        
        SynchronizedArrayList mensagensPorEnviar = new SynchronizedArrayList();
        SynchronizedArrayList mensagensPorEnviarMulticast = new SynchronizedArrayList();
        SynchronizedArrayList historicoMensagens = new SynchronizedArrayList();
        
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server Socket Created " + serverSocket);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port + ".");
            System.exit(-1);
        }
        
        System.out.println("port: " + port);
        new ThreadEnviaMensagens(listaCondutores,listaUsers,mensagensPorEnviar,historicoMensagens).start(); // envia mensagens tanto para Condutores como para Users (a escolha é processada lá dentro)
        new CondutorMulticast(mensagensPorEnviarMulticast,historicoMensagens).star();     // multicast que envia para os condutores todos
        
        while (listening) { // onde fica preso à espera de clientes
            // capturador de clientes
            Socket acceptedSocket = serverSocket.accept(); // recever clientes e o que eles enviam
            new ThreadClientes(acceptedSocket,listaCondutores,listaUsers,credenciaisCondutores,credenciaisUsers,mensagensPorEnviar,mensagensPorEnviarMulticast,historicoMensagens,historicoPontos).start();
        }
        
        // 

        serverSocket.close();
        
        
        
    }
    
}
