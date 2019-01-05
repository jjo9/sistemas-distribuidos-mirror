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
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Crisanto
 */
public class ClienteUser extends Cliente {

//    protected SynchronizedArrayList mensagemPorEnviarUser;
//    protected SynchronizedArrayList mensagemRecebidasUser;
    protected SynchronizedArrayList mensagemPorEnviarUser = new SynchronizedArrayList();
    protected SynchronizedArrayList mensagemRecebidasUser = new SynchronizedArrayList();
    protected ArrayList ativo = new ArrayList();

    public ClienteUser() {
        // ao iniciar temos que por a correr as threads de enviar e receber ?? Sim
        startThreads();
        ativo.add("ON");
    }

    // Cliente User
    // Registar;
    // Logar;
    // Inserir origem da viagem; done
    // Inserir destino da viagem; done
    // Solicitar uma viatura com condutor para uma viagem específica; done...
    // Atribuir uma pontuação (1 a 5) ao condutor para uma viagem específica; 
    // Visualizar o seu histórico de viagens e respetiva pontuação atribuída;
    // Sair.
    private void startThreads() {
        Socket echoSocket;
        try {
            echoSocket = new Socket("127.0.0.1", 7777); // é usada para estabelecer a ligação
            // criar uma thread para enviar
            new UserEnvia(echoSocket, mensagemPorEnviarUser, ativo).start();
            // criar uma thread para receber normal
            new UserRecebe(echoSocket, mensagemRecebidasUser, ativo).start();
        } catch (IOException ex) {
            Logger.getLogger(ClienteUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    void historico() {
        System.out.println("----- Historico -----");
        ArrayList pacoteRecebido;

        // para ver o historico fazer pedido ao servidor, este envia toda a informação
        this.mensagemPorEnviarUser.add("Historico/");

        // Visualizar o seu histórico de viagens e respetiva pontuação ATRIBUIDA
        while (true) {
            try {
                sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (this.mensagemRecebidasUser.getSize() != 0) {
//                pacoteRecebido = (String) this.mensagemRecebidasUser.get().get(0);
//                this.mensagemRecebidasUser.removeFromPosition(0);
                pacoteRecebido = this.mensagemRecebidasUser.get();
                break;
            }
        }

        if (pacoteRecebido.equals("")) {
            System.out.println("Historico vazio");
        } else {
            //System.out.println(pacoteRecebido);
            for (int x = 0; x < pacoteRecebido.size(); x++) {
                System.out.println("" + pacoteRecebido.get(x));
            }
        }

        // limpar historico da lista de mensagens recevidas localmente
        this.mensagemRecebidasUser.clear();
        System.out.println("---------------------");

    }

    public int pedirViagem() {
        int re = 0;

        BufferedReader lerTeclado = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println("insira a Origem da viagem"); // tratar par não por "/[]|\!()"
            String viagemOrigem = lerTeclado.readLine();
            System.out.println("insira o Destino da viagem");
            String viagemDestino = lerTeclado.readLine();

            String pedirViagemPacote = "SolicitarViagem/" + this.username + "/" + viagemOrigem + "/" + viagemDestino;

            this.mensagemPorEnviarUser.add(pedirViagemPacote); // enviar info para server, formato [ação,user,origem,destino]

            // fico à espera a ver se há Condutores ou não
            while (this.mensagemRecebidasUser.getSize() == 0) {
                try {
                    sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClienteUser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            String resposta = (String) this.mensagemRecebidasUser.get().get(0);
            this.mensagemRecebidasUser.removeFromPosition(0);

            if (resposta.equals("Processando")) {
                System.out.println("O seu Pedido Foi enviado, à espera de um Conduntor...");
                // fica à espera que alguem aceite o seu pedido

                // ele recebe o numero total de condutores
                // ok ele recebe que foi regeitado ou que foi aceite
                // quando todos 
                // vai recevendo mensagens e vê o que faz com elas
                boolean keep = true;
                String tempString = "";
                while (keep) {
                    while (this.mensagemRecebidasUser.getSize() == 0) { // fica à espera de receber mensagens
                        try {
                            sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ClienteUser.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    tempString = this.mensagemRecebidasUser.get().get(0).toString();
                    this.mensagemRecebidasUser.removeFromPosition(0);

                    if (tempString.equals("CondutorEncontrado")) {
                        keep = false;
                    } else {
                        System.out.println("::"+tempString);
                        if(tempString.equals("NaoHaMais")){
                            System.out.println("Não há mais condutores...");
                            break;
                        }
                    }
                }

                if (tempString.equals("CondutorEncontrado")) {
                    // mostrar o que recebeu (só recebe a "Mensagem" porque o resto ficou para traz por causa do split do server)
                    String condutor = this.mensagemRecebidasUser.get().get(0).toString();
                    System.out.println("O seu Condutor é: " + condutor); // print "pedido aceite por CONDUTOR nome"
                    this.mensagemRecebidasUser.removeFromPosition(0); // é importante remover pois usamos sempre o indice zero para ver a mensagem

                    // print "viagem começou"            
                    while (this.mensagemRecebidasUser.getSize() == 0) { // fica à espera de receber mensagens
                        try {
                            sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ClienteUser.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.println("A sua viagem começou :" + this.mensagemRecebidasUser.get().get(0).toString());
                    this.mensagemRecebidasUser.removeFromPosition(0);

                    // print "viagem terminou"
                    while (this.mensagemRecebidasUser.getSize() == 0) { // fica à espera de receber mensagens
                        try {
                            sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ClienteUser.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.println("A sua viagem terminou :" + this.mensagemRecebidasUser.get().get(0).toString());
                    this.mensagemRecebidasUser.removeFromPosition(0); // para tirar o "Terminou"

                    // fica à espera da cena a dizer que pode dar pontuação ao condutor ("ver qual é o formato")
                    // codigo para ler pontuação de 1 a 5
                    int pontuacaoInt = 0;
                    System.out.println("Atribua uma pontuação ao condutor: " + condutor);
                    while (pontuacaoInt > 5 || pontuacaoInt < 1) {
                        System.out.println("a pontuação deve ser de 1 a 5");
                        String pontuacao = lerTeclado.readLine();
                        pontuacaoInt = Integer.parseInt(pontuacao);
                    }

                    // enviar pontução
                    this.mensagemPorEnviarUser.add(condutor + "/" + this.username + "/" + pontuacaoInt + "/" + viagemOrigem + "/" + viagemDestino); // "Condutor/User/Pontuacao/Origem/Destino"
                }else{
                    System.out.println("Já não existem condutores para si");
                }

            } else if (resposta.equals("SemCondutores")) {
                System.out.println("Não existem condutores");
            } else {
                System.out.println(" pedido erro ?");
            }

        } catch (IOException ex) {
            Logger.getLogger(ClienteUser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return re;
    }

    @Override
    int menu() {
        int re = 0;
        //boolean menuLogIn = true;
        boolean menuRuning = true;

        BufferedReader lerMenu = new BufferedReader(new InputStreamReader(System.in));

        this.mensagemPorEnviarUser.add("User"); // para que o server consiga saber em que array vai guardar o Cliente

        while (menuRuning) {
            System.out.print(" --- User --- \n"
                    + " 1 -> registo\n"
                    + " 2 -> login\n"
                    + " 0 -> Sair\n");
            // this.mensagemPorEnviarUser.add("User"); // para que o server consiga saber em que array vai guardar o Cliente
            try {
                String opcao = lerMenu.readLine();

                if (opcao.compareTo("1") == 0) {
                    Registo(this.mensagemPorEnviarUser, this.mensagemRecebidasUser); // fazer registo
                } else if (opcao.compareTo("2") == 0) {
                    LogIn(this.mensagemPorEnviarUser, this.mensagemRecebidasUser); // fazer login -> depois de ser feito o LogIn() o userStatus fica == 1
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
            System.out.println("lista:Enviar::" + this.mensagemPorEnviarUser);
            System.out.println("lista:Recebidas::" + this.mensagemRecebidasUser);
            //  DEBUG //  DEBUG //  DEBUG //  DEBUG //  DEBUG //  DEBUG //  DEBUG 
            System.out.println(" --- User --- ");
            System.out.println("Username: " + this.username);
            System.out.print(" 1 -> ver historico\n"
                    + " 2 -> solicitar viagem\n"
                    + " 3 -> opção3\n"
                    + " 0 -> Sair\n");

            try {
                String opcao = lerMenu.readLine();

                if (opcao.compareTo("1") == 0) {
                    historico();
                } else if (opcao.compareTo("2") == 0) {
                    pedirViagem();
                } else if (opcao.compareTo("3") == 0) {
                    // fazer opção 3
                } else if (opcao.compareTo("0") == 0) {
                    menuRuning = false;
                }

            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        //antes de para as thread tenho que mandar para o server a dizer que terminei a seção
        // assim sabe que pode fechar a thread de forma segura/ livertar sockets
        this.mensagemPorEnviarUser.add("TerminaSessao/");
        this.mensagemPorEnviarUser.add("TerminaSessao/");

        // espera que a mensagem seja enviada
        while (this.mensagemPorEnviarUser.getSize() != 0) {
            try {
                // para aquando não houver mensagens
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClienteCondutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // parar Threads aqui ao sair !!! com o ativo
        this.ativo.clear();

        System.out.println("--- Fim de execução ---");

        return re;

    }

}
