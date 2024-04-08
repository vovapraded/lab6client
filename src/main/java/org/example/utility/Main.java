package org.example.utility;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.example.connection.UdpClient;
import  org.example.dto.*;
import   org.example.utility.*;
import org.example.commands.*;
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
                        throw new InvalidFormatExeption("Слишком много аргументов");
                    }
                    var command = creator.createComand(cmd, arg2);
                    udpClient.sendCommand(command);
                } catch (InvalidFormatExeption e) {
                    console.print(e.getMessage());
                }
            }

        }
    }
}