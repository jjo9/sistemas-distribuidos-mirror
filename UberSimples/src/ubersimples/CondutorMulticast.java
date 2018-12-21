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
public class CondutorMulticast extends Thread {

    SynchronizedArrayList mensagensPorEnviarMulticast;
    SynchronizedArrayList historicoMensagens;
    DatagramSocket socket;

    public CondutorMulticast(SynchronizedArrayList mensagensPorEnviarMulticast,SynchronizedArrayList historicoMensagens) throws SocketException{
        this.mensagensPorEnviarMulticast = mensagensPorEnviarMulticast;
        this.historicoMensagens = historicoMensagens;
        this.socket = new DatagramSocket(4445);
    }
    
    @Override
    public void run() { // é aqui que O SERVER ENVIA as mensagens por Multicast para os CONDUTORES

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(CondutorMulticast.class.getName()).log(Level.SEVERE, null, ex);
            }

            for (int i = 0; i < mensagensPorEnviarMulticast.getSize(); i++) {
                try {

                    byte[] buf = new byte[256];

                    System.out.println("envia: " + mensagensPorEnviarMulticast.get().get(i));
                    String dString = mensagensPorEnviarMulticast.get().get(i).toString();
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
            this.mensagensPorEnviarMulticast.clear();
        }

    }
}
