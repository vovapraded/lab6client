package org.example.utility;

import java.io.IOException;
import java.net.PortUnreachableException;

import org.example.commands.Exit;
import org.example.connection.UdpClient;
import org.example.managers.*;
/**
 *Main class
 */

public class Main {

    public static void main(String[] args) throws IOException {
        UdpClient udpClient = new UdpClient();
        Console console = Console.getInstance();
        CreatorOfCommands creator = new CreatorOfCommands();
        ParseInput parseInput = new ParseInput();
        while (true) {
            String s = console.getInput();
            if (!s.isEmpty()) {
                try {
                    parseInput.parseInput(s);
                    String cmd = parseInput.getArg1();
                    String arg2 = parseInput.getArg2();
                    if (parseInput.getArg3Exist() == 1) {
                        throw new InvalidFormatException("Слишком много аргументов");
                    }
                   var command = creator.createCommand(cmd, arg2);
                   if (command==null){
                       continue;
                   }
                    try {
                        udpClient.sendCommand(command);
                        console.print(udpClient.getResponse().trim());
                    }catch (PortUnreachableException e){
                        console.print("Сервер недоступен, повторите позже");
                    }catch (NoResponseException e){
                        console.print(e.getMessage());
                    }

                } catch (InvalidFormatException e) {
                    console.print(e.getMessage());
                }
            }

        }
    }
}