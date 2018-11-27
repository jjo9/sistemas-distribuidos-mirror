/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

/**
 *
 * @author Crisanto
 */
public abstract class Cliente {
    
    String nome;
    String password;
    
    
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    
    abstract void historico();
    
}
