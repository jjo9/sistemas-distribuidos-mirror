/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Crisanto
 */
public class USClist {

    private ArrayList<UsernameSocketConection> lista;
    // private SynchronizedArrayList<UsernameSocketConection> lista; fica aqui se mudar-mos de ideis mas acho que temos de fazer alterações 
    //aos metedos de Sync isso ou usamos os array todo como temos feito até agora

    public USClist() {
        this.lista = new ArrayList();
    }

    public boolean add(Socket socket, String username, String tipo) {
        boolean re = false; // não tá a fazer nada a variavel mas podemos mudar no futuro por isso fica
        UsernameSocketConection temp = new UsernameSocketConection(socket, username, tipo);
        this.lista.add(temp);
        re = true;
        return re;
    }

    public boolean usernameExiste(String username, String tipo) {
        boolean re = false;

        for (UsernameSocketConection item : this.lista) {
            if (item.getUsername().equals(username) && item.getTipo().equals(tipo)) {
                re = true;
                break;
            }
        }

        return re;
    }

    public Socket socketDeUsername(String username, String tipo) {
        Socket re = null;

        for (UsernameSocketConection item : this.lista) {
            if (item.getUsername().equals(username) && item.getTipo().equals(tipo)) {
                re = item.getSocket();
                break;
            }
        }
        return re;
    }

    public boolean removeUsername(String username) {
        boolean re = true;
        int index = -1;

        for (UsernameSocketConection item : this.lista) {
            index++;
            if (item.getUsername().equals(username)) {
                re = true;
                break;
            }
        }
        if (re == true) {
            this.lista.remove(index);
        }

        return re;
    }

}
