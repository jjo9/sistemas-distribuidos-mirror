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
    ArrayList<String> credenciaisCondutores;
    ArrayList<String> credenciaisUsers;
    SynchronizedArrayList mensagensPorEnviar;
    SynchronizedArrayList mensagensPorEnviarMulicast;
    SynchronizedArrayList historicoMensagens;
    ArrayList historicoPontos;
    String clienteTipo;
    String processoTemp;
    String[] processoTempArray;

    public ThreadClientes(Socket acceptedSocket, ArrayList listaCondutores, ArrayList listaUsers, ArrayList credenciaisCondutores, ArrayList credenciaisUsers, SynchronizedArrayList mensagensPorEnviar, SynchronizedArrayList mensagensPorEnviarMulticast, SynchronizedArrayList historicoMensagens, ArrayList historicoPontos) {
        super("WorkerThread");
        this.socket = acceptedSocket;
        this.listaCondutores = listaCondutores;
        this.listaUsers = listaUsers;
        this.credenciaisCondutores = credenciaisCondutores;
        this.credenciaisUsers = credenciaisUsers;
        this.mensagensPorEnviar = mensagensPorEnviar;
        this.mensagensPorEnviarMulicast = mensagensPorEnviarMulticast;
        this.historicoMensagens = historicoMensagens;
        this.historicoPontos = historicoPontos;
    }

    @Override
    public void run() {

        System.out.print("run() Started!");

        try {
            // para leitura do que o cliente envia para o server
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine;

            //System.out.println("Read  Created!");
            // ------------- poem cliente na lista certa --------
            while ((inputLine = in.readLine()) != null) { // lê o que o cliente lhe envia
                // adiciona cliente Mas tenho que ver se é um "User" ou um "Condutor"
                this.clienteTipo = inputLine;
                if (this.clienteTipo.equals("Condutor")) {
                    listaCondutores.add(socket);
                } else if (this.clienteTipo.equals("User")) {
                    listaUsers.add(socket);
                }
                this.historicoMensagens.add(this.clienteTipo); // para ficar no historico
                if (this.clienteTipo.equals("Condutor") || this.clienteTipo.equals("User")) {
                    System.out.println("--- Cliente Adicionado ---");
                    break;
                }
            }
            // sai do loop de reconheciemnto e passa para o loop de LogIn
            if (this.clienteTipo.equals("Condutor")) {// ---- Tudo para CONDUTOR

                // ------------ processo normal para ler para sempre o que está a enviar
                while ((inputLine = in.readLine()) != null) { // lê o que o CONDUTOR lhe envia
                    this.processoTemp = inputLine; // variavel temporaria que armazena o pacote de informação enviado pelo cliente
                    // --- Loop de LogIn ---
                    // ver se é Registo ou LogIn
                    this.processoTempArray = this.processoTemp.split("/"); // separa "Registo ou LogIn/Username/Password" por "/"
                    
                    if(this.processoTempArray[0].equals("Registo")){
                        
                    }else if(this.processoTempArray[0].equals("LogIn")){
                        
                    }else{
                        System.out.println("Erro no pacote de Registo/LogIn");
                    }
                    


                    this.historicoMensagens.add(inputLine); // para ficar no historico
                    if (inputLine.equals("Bye")) { // especifico que é o LogIn
                        this.listaCondutores.remove(socket);
                        System.out.println("--- Condutor Removido ---");
                        break;
                    }
                }

                in.close();
                socket.close();

            } else if (this.clienteTipo.equals("User")) {// ---- Tudo para USER

                // ------------ processo normal para ler para sempre o que está a enviar
                while ((inputLine = in.readLine()) != null) { // lê o que o User lhe envia

                    String TreatedinputLine = new ProcessaMensagem(inputLine, socket).processa(); // ver se é Condutor ou User

                    // o que faz com o que reve do cliente 
                    // poem numa lista o que o cliente enviou
                    //this.mensagensPorEnviar.add(TreatedinputLine); // para depois fazer multicast
                    this.historicoMensagens.add(TreatedinputLine); // para ficar no historico

                    if (inputLine.equals("Bye")) {
                        this.listaUsers.remove(socket);
                        System.out.println("--- User Removido ---");
                        break;
                    }
                }

                in.close();
                socket.close();
            } else {
                System.out.println("Chegada de Cliente Erro!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
