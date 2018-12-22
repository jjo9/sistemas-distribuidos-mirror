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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Crisanto
 */
public class ClienteUser extends Cliente {

    protected SynchronizedArrayList mensagemPorEnviarUser;
    protected SynchronizedArrayList mensagemRecebidasUser;

    public ClienteUser() {
        // ao iniciar temos que por a correr as threads de enviar e receber ?? Sim
        startThreads();
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
            new UserEnvia(echoSocket, mensagemPorEnviarUser).start();
            // criar uma thread para receber normal
            new UserRecebe(echoSocket, mensagemRecebidasUser).start();
        } catch (IOException ex) {
            Logger.getLogger(ClienteUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    void historico() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        // para ver o historico fazer pedido ao servidor, este envia toda a informação
        // Visualizar o seu histórico de viagens e respetiva pontuação ATRIBUIDA
    }

    public int pedirViagem() {
        int re = 0;

        BufferedReader lerTeclado = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println("insira a Origem da viagem"); // tratar par não por "/[]|\!()"
            String viagemOrigem = lerTeclado.readLine();
            System.out.println("insira o Destino da viagem");
            String viagemDestino = lerTeclado.readLine();

            String pedirViagemPacote = this.username + viagemOrigem + viagemDestino;

            this.mensagemPorEnviarUser.add(pedirViagemPacote); // enviar info para server, formato [user,origem,destino]

            // fica à espera que alguem aceite o seu pedido
            while(this.mensagemRecebidasUser.getSize() == 0){ // fica à espera de receber mensagens
                try {
                    sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClienteUser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
            
            // print "pedido aceite por CONDUTOR nome"
            // print "viagem começou"
            // print "viagem terminou"
            // fica à espera da cena a dizer que pode dar pontuação ao condutor ("ver qual é o formato")
        } catch (IOException ex) {
            Logger.getLogger(ClienteUser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return re;
    }

    public int fazRegisto() { // já tá feito no pai
        int re = 0;

        return re;
    }

    public int fazLogIn() { // já tá feito no pai
        int re = 0;

        return re;
    }

    @Override
    int menu() {
        int re = 0;
        //boolean menuLogIn = true;
        boolean menuRuning = true;

        BufferedReader lerMenu = new BufferedReader(new InputStreamReader(System.in));

        while (menuRuning) {
            System.out.print(" --- Condutor --- \n"
                    + " 1 -> registo\n"
                    + " 2 -> login\n"
                    + " 0 -> Sair\n");
            this.mensagemPorEnviarUser.add("User"); // para que o server consiga saber em que array vai guardar o Cliente
            try {
                String opcao = lerMenu.readLine();

                if (opcao.compareTo("1") == 0) {
                    // fazer registo
                } else if (opcao.compareTo("2") == 0) {
                    // fazer login
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
            System.out.print(" 1 -> ver historico\n"
                    + " 2 -> solicitar viagem\n"
                    + " 3 -> opção3\n"
                    + " 4 -> opção4\n"
                    + " 5 -> opção5\n"
                    + " 6 -> opção6\n"
                    + " 7 -> opção7\n"
                    + " 0 -> Sair\n");

            try {
                String opcao = lerMenu.readLine();

                if (opcao.compareTo("1") == 0) {
                    historico();
                } else if (opcao.compareTo("2") == 0) {
                    pedirViagem();
                } else if (opcao.compareTo("3") == 0) {
                    // fazer opção 3
                } else if (opcao.compareTo("4") == 0) {
                    // fazer opção 4
                } else if (opcao.compareTo("5") == 0) {
                    // fazer opção 5
                } else if (opcao.compareTo("6") == 0) {
                    // fazer opção 6
                } else if (opcao.compareTo("7") == 0) {
                    // fazer opção 7
                } else if (opcao.compareTo("0") == 0) {
                    menuRuning = false;
                }

            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return re;

    }

}
