package org.example.commands;
import org.example.dto.Ticket;
import org.example.managers.*;
import org.example.utility.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The command to display the collection in reverse order
 */
public class PrintDescending extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "PrintDescending".hashCode();

    @Override
    public void execute() {
        if (collection.getHashMap().isEmpty()){
            console.print("Коллекция пуста");
        }
        else{
            ArrayList<Ticket> tempList = new ArrayList<>(collection.getHashMap().values());
            tempList.sort(null);
            Collections.reverse(tempList);
            for (Ticket ticket : tempList) {
                console.print(ticket.toString());
            }
        }

    }
}