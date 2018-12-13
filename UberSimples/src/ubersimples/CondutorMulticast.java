/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Crisanto
 */
public class CondutorMulticast extends Thread{
    
    SynchronizedArrayList mensagensPorEnviar;
    DatagramSocket socket;

    public CondutorMulticast(SynchronizedArrayList mensagensPorEnviar) throws SocketException {
        this.mensagensPorEnviar = mensagensPorEnviar;
        this.socket = new DatagramSocket(4445);
    }

    @Override
    public void run() {

        // por aqui o Sleep de 5 segunds
        //TimeUnit.SECONDS.sleep(5);
        String outputLine;

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1); // default 5 !!
            } catch (InterruptedException ex) {
                Logger.getLogger(CondutorMulticast.class.getName()).log(Level.SEVERE, null, ex);
            }

            for (int i = 0; i < mensagensPorEnviar.getSize(); i++) {
                try {

                    byte[] buf = new byte[256];

//                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
//                    socket.receive(packet);

                    System.out.println("envia: "+mensagensPorEnviar.get().get(i));
                    String dString = mensagensPorEnviar.get().get(i).toString();
                    buf = dString.getBytes();

                    InetAddress group = InetAddress.getByName("230.0.0.1");
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);

                    socket.send(packet);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(CondutorMulticast.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CondutorMulticast.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            // depois tenho que tirar as mensagens enviadas 
            this.mensagensPorEnviar.clear(); // isto não deve ser a melhor solução
        }

    }
}
