/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Crisanto
 */
public class ClienteCondutor extends Cliente {
    
    protected int estado; // 0 = indisponível / 1 = disponível
    protected ArrayList listaDePedidos = new ArrayList();
    protected int viagemEstado; // 0 = não esta em viagem / 1 = viagem em progresso / 2 = viagem acabou ?

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
            try {
                String opcao = lerMenu.readLine();
                
                if (opcao.compareTo("1") == 0) {
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
            System.out.print(" 1 -> solicitar viagem\n"
                    + " 2 -> ver historico\n"
                    + " 3 -> opção3\n"
                    + " 4 -> opção4\n"
                    + " 5 -> opção5\n"
                    + " 6 -> opção6\n"
                    + " 7 -> opção7\n"
                    + " 0 -> Sair\n");
            
            try {
                String opcao = lerMenu.readLine();
                
                if (opcao.compareTo("1") == 0) {
                    // fazer opção 1
                } else if (opcao.compareTo("2") == 0) {
                    // fazer opção 2
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
