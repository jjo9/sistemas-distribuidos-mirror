/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.lang.reflect.Array;
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

    protected ArrayList estado = new ArrayList(); // 0 = indisponível / 1 = disponível -> é só "não me chatêm" ou seja apenas não recebe novos pedidos // depois mudar para boolean se der ... ou int outra vez ...
    protected SynchronizedArrayList listaDePedidos = new SynchronizedArrayList(); // [user,origem,destino] // mudar para 
    protected int viagemEstado; // 0 = não esta em viagem / 1 = viagem em progresso / 2 = viagem acabou ?
    protected SynchronizedArrayList mensagemPorEnviarCondutor = new SynchronizedArrayList();
    protected SynchronizedArrayList mensagemRecebidasCondutor = new SynchronizedArrayList();
    protected SynchronizedArrayList mensagemRecebidasMulticastCondutor = new SynchronizedArrayList();
    protected ArrayList<String> activo = new ArrayList(); // não sei se ponha este como SyncArraylista também ...
    // ativo é para dizer se está ativo e se não tiver vai parar as threads todas pois isso é ao acabar a execução do programa

    public ClienteCondutor() {
        this.estado.add("On"); // apenas não recebe novos pedidos de viagem
        this.activo.add("On");
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
    private void startThreads() {
        try {
            // ao iniciar temos que por a correr as threads de enviar e receber ??
            Socket echoSocket = new Socket("127.0.0.1", 7777); // é usada para estabelecer a ligação
            // criar uma thread para enviar
            new CondutorEnvia(echoSocket, mensagemPorEnviarCondutor, activo).start();
            // criar uma thread para receber normal
            new CondutorRecebe(echoSocket, mensagemRecebidasCondutor, estado, activo).start();
            // criar uma thread para receber em multicast
            MulticastSocket echoSocketRecebe = new MulticastSocket(4446);
            InetAddress address = InetAddress.getByName("230.0.0.1");
            echoSocketRecebe.joinGroup(address);
            new CondutorRecebeMulticast(echoSocketRecebe, activo, mensagemRecebidasMulticastCondutor, listaDePedidos, estado).start();
        } catch (IOException ex) {
            Logger.getLogger(ClienteCondutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList getEstado() {
        return estado;
    }

    public void setEstado(ArrayList estado) {
        this.estado = estado;
    }

    public String disponivilidade() {
        String re;

        if (!this.estado.isEmpty()) {
            re = "Disponivel";
        } else {
            re = "Indisponivel";
        }

        return re;
    }

    @Override
    void historico() {
        System.out.println("----- Historico -----");
        ArrayList pacoteRecebido;

        // para ver o historico fazer pedido ao servidor, este envia toda a informação
        this.mensagemPorEnviarCondutor.add("Historico/");

        // Visualizar o seu histórico de viagens e respetiva pontuação RECEBIDA
        while (this.mensagemRecebidasCondutor.getSize() == 0) {
            System.out.println("lista:::" + this.mensagemRecebidasCondutor.toString()); // por no menu para ver tudo
            System.out.println("processando...");
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
// isto só punha na variavel a primeira linha do historico
//        pacoteRecebido = (String) this.mensagemRecebidasCondutor.get().get(0);
//        this.mensagemRecebidasCondutor.removeFromPosition(0);

        pacoteRecebido = this.mensagemRecebidasCondutor.get();

        if (pacoteRecebido.equals("")) {
            System.out.println("Historico vazio");
        } else {
            //System.out.println(pacoteRecebido);
            for (int x = 0; x < pacoteRecebido.size(); x++) {
                System.out.println("" + pacoteRecebido.get(x));
            }
        }

        // limpar historico da lista de mensagens recevidas localmente
        this.mensagemRecebidasCondutor.clear();
        System.out.println("---------------------");

    }

    public int mudarEstado() {
        int re = 0;

        if (!getEstado().isEmpty()) { // esta disponivel e fica indesponivel
            this.estado.clear();
            System.out.println("O estado é agora Indesponivel");
        } else if (getEstado().isEmpty()) { // esta indisponivel e fica disponivel
            this.estado.add("On");
            System.out.println("O estado é agora Dindesponivel");
        }

        return re;
    }

    public int verPedidos() {
        int re = 0;

        if (listaDePedidos.getSize() == 0) {
            System.out.println("Não existem pedidos");
        } else {
            for (int i = 0; i < listaDePedidos.getSize(); i++) {
                System.out.println(i + "->" + listaDePedidos.get().get(i));
            }
            re = 1;
        }

        return re;

    }

    public int aceitarPedidoDeViagem() {
        int re = 0;

        int check = verPedidos();

        if (check == 1) {
            BufferedReader lerEscolha = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.println("Insira o numero da viagem que quer aceitar");
                // ler escolha
                String escolha = lerEscolha.readLine(); // se não numero parte ?
                // ver se é um numero
                int escolhaInt = Integer.parseInt(escolha);

                if (escolhaInt >= 0 && escolhaInt < this.listaDePedidos.getSize()) { // vê se está na range de pedidos

                    String pedidoPacote = this.listaDePedidos.get().get(escolhaInt).toString();

                    this.mensagemPorEnviarCondutor.add("AceitarPedidoViagem/" + pedidoPacote); // enviar mensagem ao server

                    // fica à espera de saber que o pedido foi aceite
                    boolean working = true;
                    while (working) {
                        try {
                            sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ClienteCondutor.class.getName()).log(Level.SEVERE, null, ex);
                        }
// ponho antes um IF ??????????????????????????????????????????????
                        while (this.mensagemRecebidasCondutor.getSize() != 0) { // ---------------- ponderar em por isto num while (True) pois assim pode não funcionar pois se for muito rapido passa à frente
                            working = false;
                            if (this.mensagemRecebidasCondutor.get().get(0).equals("PedidoAceite")) {

                                while (true) {// LOOP que fica aqui até dizer que COMEÇOU a viagem
                                    System.out.println("a viagem já começou? [y/n]");
                                    String viagemInicio = lerEscolha.readLine();
                                    if (viagemInicio.equals("y")) {
                                        // envia mensagem ao server a dizer que a viagem já COMEÇOU e notifica o cliente
                                        this.mensagemPorEnviarCondutor.add("Comecou");
                                        break;
                                    }
                                }
                                while (true) {// LOOP que fica aqui até dizer que a viagem TERMINOU
                                    System.out.println("a viagem já acabou? [y/n]");
                                    String viagemFim = lerEscolha.readLine();
                                    if (viagemFim.equals("y")) {
                                        // envia mensagem ao server a dizer que a viagem já TERMINOU e notifica o cliente
                                        this.mensagemPorEnviarCondutor.add("Terminou");
                                        break;
                                    }
                                }
                            } else if (this.mensagemRecebidasCondutor.get().get(0).equals("PedidoNaoExiste")) {
                                System.out.println("O pedido já não existe");
                            } else {
                                System.out.println("Erro");
                            }
                            this.mensagemRecebidasCondutor.removeFromPosition(0);
                        }
                    }

                } else {
                    System.out.println("Pedido invalido");
                }

            } catch (IOException ex) {
                Logger.getLogger(ClienteCondutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return re;
    }

    @Override
    int menu() {

        int re = 0;
        boolean menuRuning = true;

        BufferedReader lerMenu = new BufferedReader(new InputStreamReader(System.in));
        this.mensagemPorEnviarCondutor.add("Condutor"); // é o que tá em baixo mas aqui só envia uma vez porque só preciso de enviar uma vez
        while (menuRuning) {
            System.out.print(" --- Condutor --- \n"
                    + " 1 -> registo\n"
                    + " 2 -> login\n"
                    + " 0 -> Sair\n");
//            this.mensagemPorEnviarCondutor.add("Condutor"); // para que o server consiga saber em que array vai guardar o Cliente --- este está sempre a ser enviado e acho que só precisa de ser uma vez
            try {
                String opcao = lerMenu.readLine();  // não está a parar aqui para ler a linha existe aqui um erro !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                if (opcao.compareTo("1") == 0) { // mudar para equals ?!?!? no fim
                    Registo(this.mensagemPorEnviarCondutor, this.mensagemRecebidasCondutor);
                } else if (opcao.compareTo("2") == 0) {
                    LogIn(this.mensagemPorEnviarCondutor, this.mensagemRecebidasCondutor);
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
            //  DEBUG //  DEBUG //  DEBUG //  DEBUG //  DEBUG //  DEBUG //  DEBUG 
            System.out.println("//  DEBUG //  DEBUG //  DEBUG //  DEBUG //  DEBUG //  DEBUG //  DEBUG ");
            System.out.println("lista:Enviar::" + this.mensagemPorEnviarCondutor.toString());
            System.out.println("lista:Recebidas::" + this.mensagemRecebidasCondutor.toString());
            System.out.println("lista:RecebidasMulticast::" + this.mensagemRecebidasMulticastCondutor.toString());
            //  DEBUG //  DEBUG //  DEBUG //  DEBUG //  DEBUG //  DEBUG //  DEBUG 
            System.out.println(" --- Condutor --- ");
            System.out.println("Username: " + this.username);

            System.out.print(" 1 -> ver historico\n" // a implementar isto no server 
                    + " 2 -> ver pedidos de viagem\n"
                    + " 3 -> aceitar pedido de viagem\n"
                    + " 4 -> mudar estado (atualmente: " + this.disponivilidade() + " )\n"
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
                    //this.activo.clear(); // para para as cenas todas ...
                }

            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        //antes de para as thread tenho que mandar para o server a dizer que terminei a seção
        // assim sabe que pode fechar a thread de forma segura/ livertar sockets
        this.mensagemPorEnviarCondutor.add("TerminaSessao/");

        // espera que a mensagem seja enviada
        while (this.mensagemPorEnviarCondutor.getSize() != 0) {
            try {
                // para aquando não houver mensagens
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClienteCondutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // parar Threads aqui ao sair !!! com o ativo
        this.activo.clear();
        
        return re;

    }

}
