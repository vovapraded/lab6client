package org.example.connection;
import lombok.SneakyThrows;
import org.example.commands.Command;
import org.example.utility.PropertyUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import com.google.common.primitives.Bytes;


public class UdpClient {
    private final int PACKET_SIZE = 1024;
    private final int DATA_SIZE = PACKET_SIZE - 1;
private final DatagramChannel client;
    private final InetSocketAddress serverSocketAddress;
    private final InetAddress serverAddress;

    private final int serverPort = PropertyUtil.getPort();
    private  SocketAddress clientAddress;

    {
        try {
            serverAddress = InetAddress.getByName(PropertyUtil.getAddress());
            serverSocketAddress = new InetSocketAddress(serverAddress, PropertyUtil.getPort());

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public UdpClient() {
        this.client = DatagramChannel.open().bind(null).connect(serverSocketAddress);
        this.client.configureBlocking(false);
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
        byte[][] packets=new byte[(int)Math.ceil(data.length / (double)DATA_SIZE)][PACKET_SIZE];
        for (int i = 0; i<packets.length;i++){
            if (i == packets.length - 1) {
                packets[i] = Bytes.concat(Arrays.copyOfRange(data,i*DATA_SIZE,(i+1)*DATA_SIZE), new byte[]{1});
            } else {
                packets[i] = Bytes.concat(Arrays.copyOfRange(data,i*DATA_SIZE,(i+1)*DATA_SIZE), new byte[]{0});
            }
        }
        for (byte[] packet : packets) {
            client.send(ByteBuffer.wrap(packet),serverSocketAddress);
        }
    }

    private byte[] receiveData(int bufferSize) throws IOException {
        var buffer = ByteBuffer.allocate(bufferSize);
        SocketAddress address = null;
        while(address == null) {
            address = client.receive(buffer);
        }
        return buffer.array();
    }
    @SneakyThrows
    private byte[] receiveData(){
        var received=false;
        var result=new byte[0];
        while (!received){
           var data= receiveData(PACKET_SIZE);
           if (data[data.length-1]==1){
               received=true;
           }
           result = Bytes.concat(result, Arrays.copyOf(data, data.length - 1));
        }
        return result;
    }
    public String getResponse(){
        return new String(receiveData());
    }


}
