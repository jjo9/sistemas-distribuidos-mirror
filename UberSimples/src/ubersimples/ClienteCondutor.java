/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Crisanto
 */
public class ClienteCondutor extends Cliente {

    protected int estado; // 0 = indisponível / 1 = disponível -> é só "não me chatêm" ou seja apenas não recebe novos pedidos
    protected ArrayList listaDePedidos = new ArrayList(); // [user,origem,destino]
    protected int viagemEstado; // 0 = não esta em viagem / 1 = viagem em progresso / 2 = viagem acabou ?
    protected SynchronizedArrayList mensagemPorEnviarCondutor;
    protected SynchronizedArrayList mensagemRecebidasCondutor;

    public ClienteCondutor() {
        this.estado = 1;
        this.viagemEstado = 0; // não sei se vamos usar este ?...
        startThreads();
    }

    // contrutor deverá começar com o estado a 1 !!
    // Cliente Consumidor Final
    // Registar;
    // Logar;
    // Alterar estado (disponível / indisponível)
    // Receber pedidos de viatura com condutor para uma viagem específica e aceitar ou rejeitar os mesmos;
    // Visualizar o seu histórico de viagens e respetiva pontuação recebida;
    // Sair.
    // tem que dizer que acavou a viagem
    // em cmd se eu fizer print da lista de pedidos e um outro condutor aceitar o pedido, eu já não poderei aceita-lo, então ao escolher que aceito deve retornar mensagem de "este pedido já foi aceite"
    // em GUI retirar quadrado ?...
    
    private void startThreads(){
        try {
            // ao iniciar temos que por a correr as threads de enviar e receber ??
            Socket echoSocket = new Socket("127.0.0.1", 7777); // é usada para estabelecer a ligação
            // criar uma thread para enviar
            new CondutorEnvia(echoSocket, mensagemPorEnviarCondutor).start();
            // criar uma thread para receber normal
            new CondutorRecebe(echoSocket, mensagemRecebidasCondutor).start();
            // criar uma thread para receber em multicast
            MulticastSocket echoSocketRecebe = new MulticastSocket(4446);
            InetAddress address = InetAddress.getByName("230.0.0.1");
            echoSocketRecebe.joinGroup(address);
            new CondutorMulticast(mensagemPorEnviarCondutor, mensagemPorEnviarCondutor).start(); // ERRO isto não é condutor RECEBE MULTYCAST !!!!!!!!!!! CRITICO !!!!!!!!!!!!!
        } catch (IOException ex) {
            Logger.getLogger(ClienteCondutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    void historico() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        // para ver o historico fazer pedido ao servidor, este envia toda a informação
        // Visualizar o seu histórico de viagens e respetiva pontuação RECEBIDA
    }

    public int mudarEstado() {
        int re = 0;

        if (getEstado() == 1) { // esta disponivel e fica indesponivel
            setEstado(0);
        } else if (getEstado() == 0) { // esta indisponivel e fica disponivel
            setEstado(1);
        }

        return re;
    }

    public void verPedidos() {
        for (int i = 0; i < listaDePedidos.size(); i++) {
            System.out.println(i + "->" + listaDePedidos.get(i));
        }
    }

    public int aceitarPedidoDeViagem() {
        int re = 0;

        verPedidos();
        BufferedReader lerEscolha = new BufferedReader(new InputStreamReader(System.in));
        try {
            // ler escolha
            String escolha = lerEscolha.readLine();
            // ver se é um numero
            int escolhaInt = Integer.parseInt(escolha);

            // enviar mensagem ao server
            while (true) {// LOOP que fica aqui até dizer que COMEÇOU a viagem
                System.out.println("a viagem já começou? [y/n]");
                String viagemInicio = lerEscolha.readLine();
                if (viagemInicio.compareTo("y") == 0) {
                    break;
                }
                // envia mensagem ao server a dizer que a viagem já COMEÇOU e notifica o cliente
            }
            while (true) {// LOOP que fica aqui até dizer que a viagem TERMINOU
                System.out.println("a viagem já começou? [y/n]");
                String viagemInicio = lerEscolha.readLine();
                if (viagemInicio.compareTo("y") == 0) {
                    break;
                }
                // envia mensagem ao server a dizer que a viagem já TERMINOU e notifica o cliente
            }

        } catch (IOException ex) {
            Logger.getLogger(ClienteCondutor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return re;
    }

    @Override
    int menu() {

        int re = 0;
        boolean menuRuning = true;

        BufferedReader lerMenu = new BufferedReader(new InputStreamReader(System.in));

        while (menuRuning) {
            System.out.print(" --- Condutor --- \n"
                    + " 1 -> registo\n"
                    + " 2 -> login\n"
                    + " 0 -> Sair\n");
            this.mensagemPorEnviarCondutor.add("Condutor"); // para que o server consiga saber em que array vai guardar o Cliente
            try {
                String opcao = lerMenu.readLine();

                if (opcao.compareTo("1") == 0) { // mudar para equals ?!?!? no fim
                    Registo();
                } else if (opcao.compareTo("2") == 0) {
                    LogIn();
                } else if (opcao.compareTo("0") == 0) {
                    menuRuning = false;
                }

            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (getUserStatus() == 1) {
                break;
            }
        }

        while (menuRuning) {
            System.out.print(" 1 -> ver historico\n"  // a implementar isto no server 
                    + " 2 -> ver pedidos de viagem\n"
                    + " 3 -> aceitar pedido de viagem\n"
                    + " 4 -> mudar estado\n"
                    + " 5 -> opção5\n"
                    + " 6 -> opção6\n"
                    + " 7 -> opção7\n"
                    + " 0 -> Sair\n");

            try {
                String opcao = lerMenu.readLine();

                if (opcao.compareTo("1") == 0) {
                    historico();
                } else if (opcao.compareTo("2") == 0) {
                    verPedidos();
                } else if (opcao.compareTo("3") == 0) {
                    aceitarPedidoDeViagem();
                } else if (opcao.compareTo("4") == 0) {
                    mudarEstado();
                } else if (opcao.compareTo("5") == 0) {
                    // fazer opção 5
                } else if (opcao.compareTo("6") == 0) {
                    // fazer opção 6
                } else if (opcao.compareTo("7") == 0) {
                    // fazer opção 7
                } else if (opcao.compareTo("0") == 0) {
                    // sair
                    menuRuning = false;
                }

            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return re;

    }

}
