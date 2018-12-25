/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Crisanto
 */
public abstract class Cliente {
    
    protected String username;
    protected String password;
    protected int userStatus; // 0 o user não está logado / 1 o user esta logado

    // Cliente User
    // Registar; 
    // Logar;
    // Inserir origem da viagem;
    // Inserir destino da viagem;
    // Solicitar uma viatura com condutor para uma viagem específica;
    // Atribuir uma pontuação (1 a 5) ao condutor para uma viagem específica;
    // Visualizar o seu histórico de viagens e respetiva pontuação atribuída;
    // Sair.
    // Cliente Condutor
    // Registar;
    // Logar;
    // Alterar estado (disponível / indisponível)
    // Receber pedidos de viatura com condutor para uma viagem específica e aceitar ou rejeitar os mesmos;
    // Visualizar o seu histórico de viagens e respetiva pontuação recebida;
    // Sair.
    // o que têm em comum
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getUserStatus() {
        return userStatus;
    }
    
    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
    
    public int enterCredentials() {
        int re = 0;
        
        String ClientUserName;
        String ClientPassword;
        
        BufferedReader clientRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your username: ");
        try {
            ClientUserName = clientRead.readLine();
            System.out.print("Enter your password: ");
            try {
                ClientPassword = clientRead.readLine();
                this.username = ClientUserName;
                this.password = ClientPassword;
                re = 1;
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return re; // se re for 1 as credenciais foram inseridas corretamente
    }
    // ---- Creds Listas ---- Creds Listas ---- Creds Listas ---- Creds Listas ---- Creds Listas ---- Creds Listas ---- Creds Listas ---- Creds Listas ---- Creds Listas ---- Creds Listas ---- Creds Listas 
    public int sendRegistoCreds2Server(SynchronizedArrayList listaEnvia,SynchronizedArrayList listaReceve) {
        int re = 0;
        String pacoteEnviado;
        
        pacoteEnviado = "Registo/" + this.username + "/" + this.password;

        // por isto nas MENSAGENS POR ENVIADAR
        listaEnvia.add(pacoteEnviado); // mandar para o server
        // formato "Registo/Username/Password"
        
        // ver resposta --- estou aqui  --- estou aqui  --- estou aqui  --- estou aqui  --- estou aqui  --- estou aqui  --- estou aqui  --- estou aqui  --- estou aqui  --- estou aqui  --- estou aqui  
        
        
        // se retornar 0 é porque já existe o user
        // se retornar 1 é porque foi registado com sucesso
        return re;
    }
    
    public int Registo(SynchronizedArrayList listaEnvia,SynchronizedArrayList listaReceve) { // adicionar passagem por referencia a ArrayList de mensagens por enviar !!
        int re = 0;

        // le credenciais
        if (enterCredentials() == 1) { // faz o resto
            // mandar para o server
            int registoResponse = sendRegistoCreds2Server(listaEnvia,listaReceve);
            if (registoResponse == 1) { // se retornar 1 é porque foi registado com sucesso
                System.out.println("registado com sucesso!");
            } else if (registoResponse == 0) { // se retornar 0 é porque já existe o user
                System.out.println("o username já existe");
            }
        } else {
            System.out.println("erro ao inserir credenciais");
        }
        
        return re;
    }
    
    public int sendLoginCreds2Server(SynchronizedArrayList listaEnvia,SynchronizedArrayList listaReceve) { // ---------- este ainda não faz nada
        int re = 0;
        // mandar para o server
        
        
        
        // ver resposta
        // se retornar 0 é porque o user não existe
        // se retornar 1 é porque a password está errada
        // se retornar 2 é porque o logIn foi feito com sucesso
        return re;
    }
    
    public int LogIn(SynchronizedArrayList listaEnvia,SynchronizedArrayList listaReceve) {
        int re = 0;

        // le credenciais
        if (enterCredentials() == 1) { // faz o resto
            // mandar para o server
            int loginResponse = sendLoginCreds2Server(listaEnvia,listaReceve);
            if (loginResponse == 2) { // se retornar 2 é porque o logIn foi feito com sucesso
                this.userStatus = 1; // fica identificado como logado
                System.out.println("login com sucesso!");
            } else if (loginResponse == 1) { // se retornar 1 é porque a password está errada
                System.out.println("a password está errada");
            } else if (loginResponse == 0) { // se retornar 0 é porque o user não existe
                System.out.println("o user não existe");
            }
            
        } else {
            System.out.println("erro ao inserir credenciais");
        }
        
        return re;
    }
    
    abstract void historico();
    
    abstract int menu();
    
}
