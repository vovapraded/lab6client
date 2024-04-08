package org.example.connection;
import org.example.commands.Clear;
import org.example.commands.Command;
import org.example.utility.PropertyUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import com.google.common.primitives.Bytes;


public class UdpClient {
    private final int PACKET_SIZE = 1024;
    private final int DATA_SIZE = PACKET_SIZE - 1;
    private  DatagramSocket socket;
    private final InetAddress serverAddress;
    private final int serverPort = PropertyUtil.getPort();

    {
        try {
            serverAddress = InetAddress.getByName(PropertyUtil.getAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCommand(Command command) {
        try {
            // Данные для отправки
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(command);
            objectStream.flush();
            //отправляем
            sendData(byteStream.toByteArray());
        } catch (IOException e) {
            System.out.println("Сереализация не удалась: "+e.getMessage());
        }

    }
    private void sendData(byte[] data) throws IOException {
        loadNewSocket();
        byte[][] packets=new byte[(int)Math.ceil(data.length / (double)DATA_SIZE)][PACKET_SIZE];
        for (int i = 0; i<packets.length;i++){
            if (i == packets.length - 1) {
                packets[i] = Bytes.concat(Arrays.copyOfRange(data,i*DATA_SIZE,(i+1)*DATA_SIZE), new byte[]{1});
            } else {
                packets[i] = Bytes.concat(Arrays.copyOfRange(data,i*DATA_SIZE,(i+1)*DATA_SIZE), new byte[]{0});
            }
        }
        for (byte[] packet : packets) {
            DatagramPacket sendPacket = new DatagramPacket(packet, packet.length, serverAddress, serverPort);
            socket.send(sendPacket);
        }
        socket.close();
    }
    private void loadNewSocket(){
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
