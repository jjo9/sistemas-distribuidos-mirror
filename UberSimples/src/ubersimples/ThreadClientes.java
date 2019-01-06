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
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 *
 * @author Crisanto
 */
public class ThreadClientes extends Thread {

    private Socket socket = null; // socket do cliente atualmente logado
    private String username; // username do cliente atualmente logado
    ArrayList<Socket> listaCondutores;
    ArrayList<Socket> listaUsers;
    ArrayList<String> credenciaisCondutores;
    ArrayList<String> credenciaisUsers;
    USClist listaUserSocket;
    SynchronizedArrayList mensagensPorEnviar;
    SynchronizedArrayList mensagensPorEnviarMulicast;
    SynchronizedArrayList historicoMensagens;
    ArrayList<String> historicoPontos;
    SynchronizedArrayList pedidosDeViagens;

    String clienteTipo;
    String processoTemp;
    String[] processoTempArray;
    ArrayList<Integer> clientsCount;

    boolean logado = false;

    public ThreadClientes(ArrayList clientsCount, Socket acceptedSocket, ArrayList listaCondutores, ArrayList listaUsers, ArrayList credenciaisCondutores, ArrayList credenciaisUsers, USClist listaUserSocket, SynchronizedArrayList mensagensPorEnviar, SynchronizedArrayList mensagensPorEnviarMulticast, SynchronizedArrayList historicoMensagens, ArrayList historicoPontos, SynchronizedArrayList pedidosDeViagens) {
        super("WorkerThread");
        this.socket = acceptedSocket;
        this.listaCondutores = listaCondutores;
        this.listaUsers = listaUsers;
        this.credenciaisCondutores = credenciaisCondutores;
        this.credenciaisUsers = credenciaisUsers;
        this.listaUserSocket = listaUserSocket; // depois do login associa o username e a socket 
        this.mensagensPorEnviar = mensagensPorEnviar; // acho que não vou chegar a usar este ...
        this.mensagensPorEnviarMulicast = mensagensPorEnviarMulticast;
        this.historicoMensagens = historicoMensagens;
        this.historicoPontos = historicoPontos; // formato "Condutor/User/pontuacao/origem/destino"
        this.pedidosDeViagens = pedidosDeViagens; // onde estão guardados os pedidos de viagem
        this.clientsCount = clientsCount; // [Total,Condutores,Users]
    }

    public static String md5(String input) {
        String md5 = null;
        if (null == input) {
            return null;
        }
        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());
            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    @Override
    public void run() {
        String salt = "WhatIsAManAMisarablePileOfSecrets...BueEnoughtTalk!HaveAtYou!12@$@4&#%^$*"; // maybe Fix ?!!? latter ?

        System.out.print("run() Started!");
        int novoNum;

        // ARRANJAR FORMA DE UM USER NÃO PODER FAZER LOGIN DUAS VEZES AO MESMO TEMPO ---------------------------------------------------------------------------------------------
        try {
            // para leitura do que o cliente envia para o server
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // acho que é assim !!
            String inputLine;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // out.println() permite falar diretamente com o Cliente desta Thread
            String outputLine = "/";

            //System.out.println("Read  Created!");
            // ------------- poem cliente na lista certa --------
            while ((inputLine = in.readLine()) != null) { // lê o que o cliente lhe envia
                // adiciona cliente Mas tenho que ver se é um "User" ou um "Condutor"
                this.clienteTipo = inputLine;
                if (this.clienteTipo.equals("Condutor")) {
                    System.out.println("<<<<<Condutor>>>>>");
                    listaCondutores.add(socket);
                } else if (this.clienteTipo.equals("User")) {
                    System.out.println("<<<<<User>>>>>");
                    listaUsers.add(socket);
                }
                this.historicoMensagens.add(this.clienteTipo); // para ficar no historico
                if (this.clienteTipo.equals("Condutor") || this.clienteTipo.equals("User")) {
                    System.out.println("--- Cliente Adicionado ---");
                    break;
                }
            }

            // sai do loop de reconheciemnto e passa para o loop de LogIn
            if (this.clienteTipo.equals("Condutor")) {// ------------------------------------------------------------------------------------------------------------------------ Tudo para CONDUTOR

                // ------------ processo normal para ler para sempre o que está a enviar
                while ((inputLine = in.readLine()) != null) { // lê o que o CONDUTOR lhe envia
                    System.out.println("Condutor Input Login " + inputLine); ////  DEBUG   ////  DEBUG   ////  DEBUG   ////  DEBUG   ////  DEBUG   ////  DEBUG   ////  DEBUG   ////  DEBUG   ////  DEBUG   ////  
                    this.processoTemp = inputLine; // variavel temporaria que armazena o pacote de informação enviado pelo cliente
                    // --- Loop de LogIn ---
                    // ver se é Registo ou LogIn
                    this.processoTempArray = this.processoTemp.split("/"); // separa "Registo ou LogIn/Username/Password" por "/"

                    if (this.processoTempArray[0].equals("Registo")) {// ------------------------------------------------------------------------------------------------------------ Registo
                        boolean flagJaExiste = false;
                        // ver se "Username" já exite
                        for (String item : this.credenciaisCondutores) {
                            if (this.processoTempArray[1].equals(item.split("/")[0])) {
                                flagJaExiste = true;
                                break;
                            }
                        }

                        if (flagJaExiste == true) { // username já exite
                            outputLine = "JaExiste";
                        } else {
                            // password gravada em plainText
                            //this.credenciaisCondutores.add(this.processoTempArray[1] + "/" + this.processoTempArray[2]); // "nome/password"
                            // password gravada com md5 e salt
                            this.credenciaisCondutores.add(this.processoTempArray[1] + "/" + md5(this.processoTempArray[2] + salt)); // "nome/password"
                            System.out.println("Condutor Registo Creds: " + this.processoTempArray[1] + "/" + md5(this.processoTempArray[2] + salt));
                            outputLine = "Registado";
                        }
                        System.out.println("Registo EStado" + outputLine);
                        out.println(outputLine); // o que envia

                    } else if (this.processoTempArray[0].equals("LogIn")) {// --------------------------------------------------------------------------------------------------------- Login

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
                        if (flagJaExiste == true) { // se já existir ver se a password está certa

                            if (md5(this.processoTempArray[2] + salt).equals(passwordTemp)) { // password esta certa
                                outputLine = "Sucesso";
                                this.username = this.processoTempArray[1];
                            } else { // password esta errada
                                outputLine = "PasswordErrada";
                            }

                        } else {
                            outputLine = "ClienteNaoExiste";
                        }
                        System.out.println("Login Estado" + outputLine);
                        out.println(outputLine); // o que envia

                    } else if (this.processoTempArray[0].equals("TerminaSessao")) { // ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  
                        outputLine = "Exit";
                    } else {
                        System.out.println("Erro no pacote de Registo/LogIn");
                    }

                    //this.historicoMensagens.add(inputLine); // para ficar no historico
                    if (outputLine.equals("Sucesso")) {
                        System.out.println("--- Condutor Logado ---");
                        this.listaUserSocket.add(this.socket, this.username, this.clienteTipo); // adiciona link entre username e socket
                        this.logado = true; // acho que posso usar isto para não logar duas vezes ao mesmo tempo !!!!--- !!!!--- !!!!--- !!!!--- !!!!--- !!!!--- !!!!--- !!!!--- !!!!--- 
                        // corresponde ao Condutor
                        novoNum = this.clientsCount.get(1);
                        novoNum++;
                        this.clientsCount.set(1, novoNum);
                        // Incremento para posição "0" que corresponde ao total de Clientes
                        novoNum = this.clientsCount.get(0);
                        novoNum++;
                        this.clientsCount.set(0, novoNum);
                        System.out.println("Current Client Count: " + this.clientsCount.get(0));
                        System.out.println("Current Condutor Count: " + this.clientsCount.get(1));
                        System.out.println("Current User Count: " + this.clientsCount.get(2));

                        // mandar lista de pedidos de viagem
                        String pedidosOnline = "";
                        for (int x = 0;x < this.pedidosDeViagens.getSize();x++){
                            pedidosOnline += this.pedidosDeViagens.get().get(x)+":";
                        }
                        out.println(pedidosOnline);
                        break;
                    } else if (outputLine.equals("Exit")) {
                        System.out.println("--- Condutor Saindo ---");
                        break;
                    }
                }
                // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                while ((inputLine = in.readLine()) != null) { // ----------- Loop Condutor Processa Viagens
                    System.out.println("Loop Condutor processa Viagens" + inputLine);
                    this.processoTemp = inputLine;
                    this.processoTempArray = this.processoTemp.split("/"); // separa "something/something/" por "/"

                    if (this.processoTempArray[0].equals("Historico")) { // ----------------------------------------------------------------------------------------------- "Condutor/User/pontuacao/origem/destino"

                        String historicoTemp = "";

                        for (String item : this.historicoPontos) { // quando o CONDUTOR o HISTORICO -> comparar a condutor
                            System.out.println("Condutor Historico Pontos ciclo " + item);
                            if (this.username.equals(item.split("/")[0])) { // o CONDUTOR foi o que efetuou a viagem como CONDUTOR
                                historicoTemp += (item + "\n");
//                                historicoTemp += (item + "/");
                            }
                        }
//                        out.print(historicoTemp); // enviar 
                        out.println(historicoTemp); // enviar 
                        System.out.println("enviado historico condutor: " + historicoTemp + " ::::");

                    } else if (this.processoTempArray[0].equals("AceitarPedidoViagem")) { // -------------------------------------------------------------------------------------------- receber pedido de viagem  
                        // formato "AceitarPedidoViagem/UserQuePediu/Origem/Destino"
                        boolean existFlag = false;

                        // ver se pedido o pedido ainda existe
                        for (int x = 0; x < this.pedidosDeViagens.getSize(); x++) {
                            System.out.println("Ver se pedido ainda existe" + this.pedidosDeViagens.get().get(x).toString()); // compara pedidos guardados no Server -com- pedidos de aceitação feito pelo Condutor
                            if (this.pedidosDeViagens.get().get(x).toString().equals(this.processoTempArray[1] + "/" + this.processoTempArray[2] + "/" + this.processoTempArray[3])) { // se existir 
                                // tira pedido do server pois este Condutor já o aceitou e vai toma-lo para si, assim mais ninguem o pode aceitar
                                String tempPedido = this.pedidosDeViagens.get().get(x).toString();
                                this.pedidosDeViagens.removeFromPosition(x);
                                // also enviar em Multicast para a dizer que já não é possivel aceitar aquele pedido no formato "Drop/User/origem/destino"
                                this.mensagensPorEnviarMulicast.add("Drop/" + tempPedido);
                                existFlag = true;
                                break;
                            }
                        }
                        if (existFlag) { // continuação do se existir
                            out.println("PedidoAceite"); // informa que o pedido existe e foi aceite

                            this.mensagensPorEnviar.add(this.processoTempArray[1] + "/User/" + "CondutorEncontrado");

                            String oSeuCondutor = this.processoTempArray[1] + "/User/" + this.username;

                            this.mensagensPorEnviar.add(oSeuCondutor); // envio ao "User" a dizer quem é condutor

                            while ((inputLine = in.readLine()) != null) { // espera que o "Condutor" diga que a viagem começou
                                if (inputLine.equals("Comecou")) {
                                    String comecou = this.processoTempArray[1] + "/User/Comecou";
                                    this.mensagensPorEnviar.add(comecou); // informa user que comecou
                                    break;
                                }
                            }

                            while ((inputLine = in.readLine()) != null) { // espera que o "Condutor" diga que a viagem terminou
                                if (inputLine.equals("Terminou")) {
                                    String terminou = this.processoTempArray[1] + "/User/Terminou";
                                    this.mensagensPorEnviar.add(terminou); // informa user que terminou
                                    break;
                                }
                            }
                            // depois notifica o cliente que pode dar a "Pontuação"
                            //this.mensagensPorEnviar.add(this.processoTempArray[1] + "/User/PodePontuar");  // username/tipoCliente/Mensagem

                        } else {// se não existir
                            out.println("PedidoNaoExiste"); // manda mensagem ao Condutor a dizer que não pode aceitar o pedido pois este já existe
                        }

                    } else if (this.processoTempArray[0].equals("TerminaSessao")) { // ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  
                        System.out.println("in session Desconetar...");
                        if (this.logado) {
                            // remove Condutor
                            novoNum = this.clientsCount.get(1);
                            novoNum--;
                            this.clientsCount.set(1, novoNum);
                            // Incremento para posição "0" que corresponde ao total de Clientes
                            novoNum = this.clientsCount.get(0);
                            novoNum--;
                            this.clientsCount.set(0, novoNum);
                            System.out.println("Current Client Count: " + this.clientsCount.get(0));
                            System.out.println("Current Condutor Count: " + this.clientsCount.get(1));
                            System.out.println("Current User Count: " + this.clientsCount.get(2));
                        }

                        break;
                    } else if (this.processoTempArray[0].equals("RecusarPedidoViagem")) {
                        // formato "RecusarPedidoViagem/UserQuePediu/Origem/Destino"
                        // qual a viagem que vai ser recusada 
                        String viagemRecusada = (this.processoTempArray[1] + ":" + this.processoTempArray[2] + ":" + this.processoTempArray[3]); //   "username/origem/destino"
                        // envio para o User que a viagem foi recusada e qual o numero de Condutores ainda disponiveis
                        // se um Condutor fizer LogOut envio também essa info mas este é para todos os Users

                        // "Username/Tipo/Mensagem"
                        // "Username/Tipo/Espera/Numero de Condutores ativos"
//                        if (this.clientsCount.get(1) == 0) {
//                            viagemRecusada = "NaoHaMais";
//                        }
                        //this.mensagensPorEnviar.add(this.processoTempArray[1] + "/User/" + viagemRecusada);
                        this.mensagensPorEnviar.add(this.processoTempArray[1] + "/User/RecusaViagem");

                    } else if (this.processoTempArray[0].equals("aaaaaaaaaaaaaaaaaaaa")) {

                    } else if (this.processoTempArray[0].equals("aaaaaaaaaaaaaaaaaaaa")) {

                    } else if (this.processoTempArray[0].equals("Bye")) {
                        // criterio de saida ???????
                    }

                }

                in.close();
                socket.close();

            } else if (this.clienteTipo.equals("User")) {// -------------------------------------------------------------------------------------------------------------------- Tudo para USER

                // ------------ processo normal para ler para sempre o que está a enviar
                while ((inputLine = in.readLine()) != null) { // lê o que o USER lhe envia
                    this.processoTemp = inputLine; // variavel temporaria que armazena o pacote de informação enviado pelo cliente
                    // --- Loop de LogIn ---
                    // ver se é Registo ou LogIn
                    this.processoTempArray = this.processoTemp.split("/"); // separa "Registo ou LogIn/Username/Password" por "/"

                    if (this.processoTempArray[0].equals("Registo")) {// ------------------------------------------------------------------------------------------------------------ Registo
                        boolean flagJaExiste = false;
                        // ver se "Username" já exite
                        for (String item : this.credenciaisUsers) {
                            if (this.processoTempArray[1].equals(item.split("/")[0])) {
                                flagJaExiste = true;
                                break;
                            }
                        }

                        if (flagJaExiste == true) { // username já exite
                            outputLine = "JaExiste";
                        } else {
                            // password gravada em plain text
                            //this.credenciaisUsers.add(this.processoTempArray[1] + "/" + this.processoTempArray[2]); // username/password
                            // password gravada com md5 e salt

                            this.credenciaisUsers.add(this.processoTempArray[1] + "/" + md5(this.processoTempArray[2] + salt)); // username/password
                            outputLine = "Registado";
                        }
                        System.out.println("User Registo status" + outputLine);
                        out.println(outputLine); // o que envia

                    } else if (this.processoTempArray[0].equals("LogIn")) {// --------------------------------------------------------------------------------------------------------- Login

                        boolean flagJaExiste = false;
                        String passwordTemp = "/";

                        // ver se "Username" existe
                        for (String item : this.credenciaisUsers) {
                            if (this.processoTempArray[1].equals(item.split("/")[0])) {
                                flagJaExiste = true;
                                passwordTemp = item.split("/")[1];
                                break;
                            }
                        }
                        if (flagJaExiste == true) { // se já existir ver se a password está certa

                            if (md5(this.processoTempArray[2] + salt).equals(passwordTemp)) { // password esta certa
                                outputLine = "Sucesso";
                                this.username = this.processoTempArray[1];
                            } else { // password esta errada
                                outputLine = "PasswordErrada";
                            }

                        } else {
                            outputLine = "ClienteNaoExiste";
                        }
                        System.out.println("User LogIn status" + outputLine);
                        out.println(outputLine); // o que envia
                    } else if (this.processoTempArray[0].equals("TerminaSessao")) { // ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  
                        outputLine = "Exit";
                    } else {
                        System.out.println("Erro no pacote de Registo/LogIn");
                    }

                    //this.historicoMensagens.add(inputLine); // para ficar no historico
                    if (outputLine.equals("Sucesso")) {
                        System.out.println("--- User Logado ---");
                        this.listaUserSocket.add(this.socket, this.username, this.clienteTipo); // adiciona link entre username e socket
                        this.logado = true;
                        novoNum = this.clientsCount.get(2);
                        novoNum++;
                        this.clientsCount.set(2, novoNum);
                        // Incremento para posição "0" que corresponde ao total de Clientes
                        novoNum = this.clientsCount.get(0);
                        novoNum++;
                        this.clientsCount.set(0, novoNum);
                        System.out.println("Current Client Count: " + this.clientsCount.get(0));
                        System.out.println("Current Condutor Count: " + this.clientsCount.get(1));
                        System.out.println("Current User Count: " + this.clientsCount.get(2));

                        break;
                    } else if (outputLine.equals("Exit")) {
                        System.out.println("--- User Saindo ---");
                        break;
                    }
                }
                // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                while ((inputLine = in.readLine()) != null) { // ----------- Loop User Processa Viagens
                    System.out.println("Loop User Processa Viagens " + inputLine);
                    this.processoTemp = inputLine;
                    this.processoTempArray = this.processoTemp.split("/"); // separa "something/something/" por "/"

                    if (this.processoTempArray[0].equals("Historico")) { // ----------------------------------------------------------------------------------------------- "Condutor/User/pontuacao/origem/destino"

                        String historicoTemp = "";

                        for (String item : this.historicoPontos) { // quando o USER o HISTORICO -> comparar a user
                            if (this.username.equals(item.split("/")[1])) { // o USER foi o que efetuou a viagem com o CONDUTOR
                                historicoTemp += (item + "\n");
                            }
                        }
                        out.println(historicoTemp); // enviar mudei de println() para print()
                        System.out.println("::fim de historico do User::");
                    } else if (this.processoTempArray[0].equals("SolicitarViagem")) { // -------------------------------------------------------------------------------------------- receber pedido de viagem  
                        // vê se existem Condutores Disponiveis
                        if (this.clientsCount.get(1) != 0) { // existem condutores

                            out.println("Processando");

                            // formato "AceitarPedidoViagem/UserQuePediu/Origem/Destino"
                            boolean existFlag = false;

                            // tenho que adicionar às mensagens existentes de pedidos 
                            this.pedidosDeViagens.add(this.username + "/" + this.processoTempArray[2] + "/" + this.processoTempArray[3]);

                            // tenho que o por na lista de mensagens por enviar Multicast ????
                            // separo e vai para o multicast porque aqui não precisa de ser mais processada
                            this.mensagensPorEnviarMulicast.add("Add/" + this.username + "/" + this.processoTempArray[2] + "/" + this.processoTempArray[3]);
                            this.mensagensPorEnviar.add(this.username + "/User/Espera:CondutoresOnline:" + this.clientsCount.get(1));
                            while ((inputLine = in.readLine()) != null) { // lê pontuação dada

                                if (inputLine.equals("RegeitadoTotal")) {
                                    int posicao = this.pedidosDeViagens.find(this.username + "/" + this.processoTempArray[2] + "/" + this.processoTempArray[3]);
                                    this.pedidosDeViagens.removeFromPosition(posicao);
                                    break;
                                } else {
                                    System.out.println("Pontuação dada " + inputLine);
                                    this.processoTemp = inputLine;
                                    this.historicoPontos.add(inputLine);
                                    break;
                                }

                            }

                            // depois fico à espera que alguem aceite
                            // ver pedidos de aceitação
                            // não preciso de fazer nada aqui por a espera é feita no user e os seguites envios para ele serão feitos pelo Condutor atravez do server 
                            // acho que depis tenho de esperar pela pontuação que o user atribuiu ao condutor ???
                        } else {// não existem condutores
                            out.println("SemCondutores");
                        }

                    } else if (this.processoTempArray[0].equals("TerminaSessao")) { // ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  ---- matar Thread  
                        System.out.println("in session Desconetar...");
                        if (this.logado) {
                            // remove User
                            novoNum = this.clientsCount.get(2);
                            novoNum--;
                            this.clientsCount.set(2, novoNum);
                            // Incremento para posição "0" que corresponde ao total de Clientes
                            novoNum = this.clientsCount.get(0);
                            novoNum--;
                            this.clientsCount.set(0, novoNum);
                            System.out.println("Current Client Count: " + this.clientsCount.get(0));
                            System.out.println("Current Condutor Count: " + this.clientsCount.get(1));
                            System.out.println("Current User Count: " + this.clientsCount.get(2));
                        }
                        break;
                    } else if (this.processoTempArray[0].equals("aaaaaaaaaaaaaaaaaaaa")) {

                    } else if (this.processoTempArray[0].equals("aaaaaaaaaaaaaaaaaaaa")) {

                    } else if (this.processoTempArray[0].equals("aaaaaaaaaaaaaaaaaaaa")) {

                    } else if (this.processoTempArray[0].equals("aaaaaaaaaaaaaaaaaaaa")) {

                    } else if (this.processoTempArray[0].equals("Bye")) {
                        // criterio de saida ???????
                    }

//                    if (outputLine.equals("Sucesso")) { // porque este tá a aqui ?
//                        System.out.println("--- Condutor Logado ---");
//                        break;
//                    }
                }

                in.close();
                socket.close(); // fecha a socket que o liga ao cliente 

            } else {
                System.out.println("Chegada de Cliente Erro!");
            }

            System.out.println("Cliente Desconectado");

            // Incremento para posição "0" que corresponde ao total de Clientes
//            novoNum = this.clientsCount.get(0);
//            novoNum--;
//            this.clientsCount.set(0, novoNum);
//            System.out.println("Current Client Count: " + this.clientsCount.get(0));
//            System.out.println("Current Condutor Count: " + this.clientsCount.get(1));
//            System.out.println("Current User Count: " + this.clientsCount.get(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
