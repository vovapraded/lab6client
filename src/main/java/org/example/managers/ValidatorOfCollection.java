package org.example.managers;

import org.example.dto.Ticket;
import org.example.dto.Venue;
import org.example.utility.Console;

import java.util.*;

/**
 * A class that checks the collection for the correctness of the data
 */
public class ValidatorOfCollection {
    private final Collection collection = Collection.getInstance();
    private final  Console console = Console.getInstance();
    public void validateCollection() {
        Set<Long> setTicketId = new HashSet<>();
        Set<Long> setVenueId = new HashSet<>();

        for (Ticket ticket: Ticket.getInstancesTicket()){
            setTicketId.add(ticket.getId());
        }
        for (Venue venue: Venue.getInstancesVenue()){
            setVenueId.add(venue.getId());
        }
        if (setVenueId.size()!=Venue.getInstancesVenue().size() || setTicketId.size()!=Ticket.getInstancesTicket().size()){
            console.print("Ошибка уникальности id в файле");
            collection.clearCollection();
            console.print("Коллекция очищена");
        }
        for (Long id: collection.getHashMap().keySet()){
            if (!Objects.equals(id, collection.getHashMap().get(id).getId())){
                console.print("Ошибка. Не совпадает id билета с ключом");
                collection.clearCollection();
                console.print("Коллекция очищена");
                break;

            }
        }



    }
}