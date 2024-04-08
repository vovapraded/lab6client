package org.example.managers;

import org.example.commands.*;
import org.example.utility.*;

import java.util.HashMap;

/**
 * A class for executing commands
 */
public class CreatorOfCommands {
    private final Collection collection = Collection.getInstance();
    private final  Console console = Console.getInstance();
    private  HashMap<String, Command> commands = new HashMap<String, Command>();

    public CreatorOfCommands(){
        commands.put("insert",new Insert());
        commands.put("help",new Help());
        commands.put("remove_key",new RemoveKey());
        commands.put("update",new Update());
        commands.put("show",new Show());
        commands.put("exit",new Exit());
        commands.put("save",new Save());
        commands.put("clear",new Clear());
        commands.put("info",new Info());
        commands.put("remove_greater",new RemoveGreater());
        commands.put("remove_greater_key", new RemoveGreaterKey());
        commands.put("print_descending", new PrintDescending());
        commands.put("average_of_price",new AverageOfPrice());
        commands.put("execute_script",new ExecuteScript());
        commands.put("replace_if_greater",new ReplaceIfGreater());
        commands.put("filter_less_than_venue",new FilterLessThanVenue());
    }
    public Command createComand(String cmd, String arg1) {
        if (commands.containsKey(cmd)){
            int a = arg1.isEmpty() ? 0 : 1;
            try {
            if (a!= Commands.valueOf(cmd).getCountArgs()){
                throw new InvalidFormatExeption("Неверное число аргументов");
            }
            }catch (IllegalArgumentException e){
                throw new RuntimeException("Нет такой команды");
            }
            var command=commands.get(cmd);
            command.prepareToSend(arg1,Commands.valueOf(cmd).isTicketArgIsNeeded());
            return command;
        }
        else {
            throw new InvalidFormatExeption("Нет такой команды");
        }
    }


//
//
//



//








}






