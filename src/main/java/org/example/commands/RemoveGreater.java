package org.example.commands;


import org.example.managers.*;
import org.example.utility.*;
import org.example.dto.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * The remove items with a price higher than the specified one command
 */
public class RemoveGreater extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "RemoveGreater".hashCode();


    @Override
    public void execute() {
            Ticket ticket = CreateTicket.createTicket(collection.getFreeId());
            collection.removeGreater(ticket);
            console.print("Удалено успешно");
    }
}