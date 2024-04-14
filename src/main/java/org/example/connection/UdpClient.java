package org.example.connection;
import org.example.commands.Command;
import org.example.utility.Console;
import org.example.utility.NoResponseException;
import org.example.utility.PropertyUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.LocalDateTime;
import java.util.Arrays;
import com.google.common.primitives.Bytes;


public class UdpClient  {
    private final int PACKET_SIZE = 1024;
    private final int DATA_SIZE = PACKET_SIZE - 1;
    private final Console console = Console.getInstance();
private DatagramChannel client;
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

    public UdpClient()  {
        console.print("Пытаемся открыть канал для соединения с сервером");
        boolean channelIsOpen = false;
        int i = 0;
           while (!channelIsOpen && i<10) {
               try {
                       this.client = DatagramChannel.open().bind(null).connect(serverSocketAddress);
                       this.client.configureBlocking(false);
                       channelIsOpen=true;
               } catch (IOException e) {
                   i++;
               }
           }
           if (channelIsOpen) {
               console.print("Канал открыт");
               console.printHello();

           }else {
               console.print("Не удалось открыть канал, проверьте настройки соединения и перезапустите программу");

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
    public void sendData(byte[] data) throws IOException {
        byte[][] packets=new byte[(int)Math.ceil(data.length / (double)DATA_SIZE)][PACKET_SIZE];
        for (int i = 0; i<packets.length;i++){
            byte k = 0;
            if (i == 0){
                k+=1;
            }
            if (i == packets.length - 1) {
                k +=2;
            }
            packets[i] = Bytes.concat(Arrays.copyOfRange(data,i*DATA_SIZE,(i+1)*DATA_SIZE), new byte[]{k});

        }
        for (byte[] packet : packets) {
            client.send(ByteBuffer.wrap(packet),serverSocketAddress);

        }
   }

    private byte[] receiveData(int bufferSize) throws IOException,NoResponseException {
        var buffer = ByteBuffer.allocate(bufferSize);
        SocketAddress address = null;
        var start = LocalDateTime.now();
        while(address == null) {
                if (LocalDateTime.now().isAfter(start.plusSeconds(5L))){
                    throw new NoResponseException("Ответа нет более 5 секунд, повторите запрос");
                }
                address = client.receive(buffer);
        }
        return buffer.array();
    }

    private byte[] receiveData() throws NoResponseException{
        var received=false;
        var result=new byte[0];
        while (!received){
            byte[] data= new byte[0];
            try {
                data = receiveData(PACKET_SIZE);
            } catch (IOException e) {
                throw new NoResponseException("Не получилось получить ответ от сервера, проверьте настройки соединения и повторите запрос");
            }
            if(data.length==0){
                throw new NoResponseException("Ответ пустой");
            }
            if (data[data.length-1]==1){
               received=true;
           }
           result = Bytes.concat(result, Arrays.copyOf(data, data.length - 1));
        }
        return result;
    }
    public String getResponse() throws PortUnreachableException, NoResponseException{
        return new String(receiveData());
    }


}
