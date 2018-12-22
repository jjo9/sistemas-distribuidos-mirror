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

    public CondutorRecebeMulticast(MulticastSocket echoSocket, ArrayList ativo, SynchronizedArrayList mensagensMulticast, SynchronizedArrayList listaDePedidos) {
        this.echoSocket = echoSocket;
        this.ativo = ativo;
        this.mensagensMulticast = mensagensMulticast;
    }

    @Override
    public void run() {
//        String recebo;
//        
//        BufferedReader in;
//        try {
//            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream())); // para se obter um objeto do tipo BufferedReader
//            while (((recebo = in.readLine()) != null) && (echoSocket != null)) {
////                recebo = in.readLine();// o que eu recebo
//                System.out.println("recebo: " + recebo);
//            }
////            System.out.println("Recebe Closed");
//            in.close();
//            echoSocket.close();
//        } catch (IOException ex) {
//            Logger.getLogger(ClienteRecebe.class.getName()).log(Level.SEVERE, null, ex);
//        }

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
            this.mensagensMulticast.add(received); // preciso de fazer isto sequer ? tipo para que é que estou a guardar ?
            System.out.println("recebe: " + received);
            
            
            // fazer alguma coisa para retirar da lista
            // fazer split "/"
            // se for "Add/"  // foi este o nome que dei ?!!?!?
            // se for "Drop/" -> ir a "this.listaDePedidos" e retirar esse pedido
            
        }

    }

}
