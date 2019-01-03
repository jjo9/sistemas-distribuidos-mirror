/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.net.Socket;

/**
 *
 * @author Crisanto
 */
public class UsernameSocketConection {

    private String username; // username
    private Socket socket; // socket onde está guardado
    private String tipo; // tipo de Cliente ( "User" ou "Condutor" )

    public UsernameSocketConection(Socket socket, String username, String tipo) {
        this.username = username;
        this.socket = socket;
        this.tipo = tipo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
