package org.example.commands;

import org.example.dto.Ticket;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
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
                ArrayList<Ticket> tempList = new ArrayList<>(collection.getHashMap().values());
                tempList.sort(null);

                for (Ticket ticket : tempList) {
                    console.print(ticket.toString());
                }
            }

    }
}
