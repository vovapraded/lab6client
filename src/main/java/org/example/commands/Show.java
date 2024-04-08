package org.example.commands;

import org.example.dto.Ticket;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

/**
 * The command outputs a collection
 */
public class Show extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "Show".hashCode();


    @Override
    public void execute() {
            if (collection.getHashMap().isEmpty()){
                console.print("Коллекция пуста");
            }
            else{
                Arrays.sort(collection.getHashMap().values().toArray());
                for (Ticket ticket : collection.getHashMap().values()){
                    console.print(ticket.toString());
                }
            }

    }
}
