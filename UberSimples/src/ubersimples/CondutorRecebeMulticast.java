/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Crisanto
 */
public class CondutorRecebeMulticast extends Thread {

    MulticastSocket echoSocket = null;
    ArrayList ativo;
    SynchronizedArrayList mensagensMulticast;
    SynchronizedArrayList listaDePedidos;
    ArrayList estado;

    public CondutorRecebeMulticast(MulticastSocket echoSocket, ArrayList ativo, SynchronizedArrayList mensagensMulticast, SynchronizedArrayList listaDePedidos, ArrayList estado) {
        this.echoSocket = echoSocket;
        this.ativo = ativo;
        this.mensagensMulticast = mensagensMulticast;
        this.listaDePedidos = listaDePedidos;
        this.estado = estado;
    }

    @Override
    public void run() {

        DatagramPacket packet;
        while (!ativo.isEmpty()) { // eu sei 
            byte[] buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);

            try {
                echoSocket.receive(packet);
            } catch (IOException ex) {
                Logger.getLogger(CondutorRecebeMulticast.class.getName()).log(Level.SEVERE, null, ex);
            }

            String received = new String(packet.getData(), 0, packet.getLength());
            this.mensagensMulticast.add(received); // preciso de fazer isto sequer ? tipo para que é que estou a guardar ? por isso também não preciso de tirar no fim
            System.out.println("recebe Multicast: " + received);

            // fazer alguma coisa para retirar da lista de pedidos de mensagens
            // fazer split "/"
            String[] receivedTemp = received.split("/");

            if (receivedTemp[0].equals("Add")) {// se for "Add/"  // foi este o nome que dei ?!!?!?
                if(!this.estado.isEmpty()){ // se não estiver vazio ou seja está On guarda o Add 
                    this.listaDePedidos.add(receivedTemp[1] + "/" + receivedTemp[2] + "/" + receivedTemp[3]); // "User/Origem/Destino"
                }else{
                    System.out.println("Add ignorado pois esta Indisponivel");
                }
            } else if (receivedTemp[0].equals("Drop")) {// se for "Drop/" -> ir a "this.listaDePedidos" e retirar esse pedido

                for (int x = 0; x < this.listaDePedidos.getSize(); x++) { // encontrar o pedido que será removido
                    if (this.listaDePedidos.get().get(x).equals(receivedTemp[1] + "/" + receivedTemp[2] + "/" + receivedTemp[3])) { // se for igual ao pedido de viagem que chegou para ser removido
                        this.listaDePedidos.removeFromPosition(x); // remove pedido
                        break;
                    }
                }
                System.out.println("Removido com sucesso -- Drop --");

            } else {
                System.out.println("Erro");
            }

        }

    }

}
