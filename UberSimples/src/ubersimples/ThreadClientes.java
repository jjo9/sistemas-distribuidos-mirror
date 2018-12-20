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
import java.util.ArrayList;

/**
 *
 * @author Crisanto
 */
public class ThreadClientes extends Thread {

    private Socket socket = null;
    private String username;
    ArrayList<Socket> listaCondutores;
    ArrayList<Socket> listaUsers;
    ArrayList<String> credenciaisCondutores;
    ArrayList<String> credenciaisUsers;
    SynchronizedArrayList mensagensPorEnviar;
    SynchronizedArrayList mensagensPorEnviarMulicast;
    SynchronizedArrayList historicoMensagens;
    ArrayList<String> historicoPontos;
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
        this.mensagensPorEnviar = mensagensPorEnviar; // acho que não vou chegar a usar este ...
        this.mensagensPorEnviarMulicast = mensagensPorEnviarMulticast;
        this.historicoMensagens = historicoMensagens;
        this.historicoPontos = historicoPontos; // formato "Condutor/User/pontuacao/origem/destino"
    }

    @Override
    public void run() {

        System.out.print("run() Started!");

        try {
            // para leitura do que o cliente envia para o server
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // acho que é assim !!
            String inputLine;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String outputLine = "/";

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

                    if (this.processoTempArray[0].equals("Registo")) {// ------------ Registo
                        boolean flagJaExiste = false;
                        // ver se "Username" já exite
                        for (String item : this.credenciaisCondutores) {
                            if (this.processoTempArray[1].equals(item.split("/")[0])) {
                                flagJaExiste = true;
                                break;
                            }
                        }
                        
                        if(flagJaExiste == true){ // username já exite
                            outputLine = "JaExiste";
                        }else{
                            this.credenciaisCondutores.add(this.processoTempArray[1]+"/"+this.processoTempArray[2]);
                            outputLine = "Registado";
                        }
                        System.out.println(outputLine);
                        out.println(outputLine); // o que envia

                    } else if (this.processoTempArray[0].equals("LogIn")) {// ------------ Login
                        
                        boolean flagJaExiste = false;
                        String passwordTemp = "/";
                        
                        // ver se "Username" existe
                        for (String item : this.credenciaisCondutores) {
                            if (this.processoTempArray[1].equals(item.split("/")[0])) {
                                flagJaExiste = true;
                                passwordTemp = item.split("/")[1];
                                break;
                            }
                        }
                        if(flagJaExiste == true){ // se já existir ver se a password está certa
                            
                            if(this.processoTempArray[2].equals(passwordTemp)){ // password esta certa
                                outputLine = "Sucesso";
                                this.username = this.processoTempArray[1];
                            }else{ // password esta errada
                                outputLine = "PasswordErrada";
                            }
                            
                        }else{
                            outputLine = "ClienteNaoExiste";
                        }
                        System.out.println(outputLine);
                        out.println(outputLine); // o que envia
                        
                        
                        
                    } else {
                        System.out.println("Erro no pacote de Registo/LogIn");
                    }

                    //this.historicoMensagens.add(inputLine); // para ficar no historico
                    if (outputLine.equals("Sucesso")) { 
                        System.out.println("--- Condutor Logado ---");
                        break;
                    }
                }
                
                while ((inputLine = in.readLine()) != null) { // ----------- Loop Condutor Processa Viagens
                    
                    this.processoTemp = inputLine; 
                    this.processoTempArray = this.processoTemp.split("/"); // separa "something/something/" por "/"
                    
                    if (this.processoTempArray[0].equals("Historico")) { // "Condutor/User/pontuacao/origem/destino"
                        
                        String historicoTemp = "";
                        
                        for(String item : this.historicoPontos){ // quando o CONDUTOR o HISTORICO -> comparar a condutor
                            if(this.username.equals(item.split("/")[0])){ // o CONDUTOR foi o que efetuou a viagem como CONDUTOR
                                historicoTemp += (item+"\n");
                            }
                        }
                        
                        
                        
                    } else if(this.processoTempArray[0].equals("aaaaaaaaaaaaaaaaaaaa")){
                        
                    }
                    
                    
                    
                    
                    
                    
                    if (outputLine.equals("Sucesso")) { 
                        System.out.println("--- Condutor Logado ---");
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
