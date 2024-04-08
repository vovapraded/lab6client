package org.example.managers;

import com.google.gson.*;
import org.example.dto.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * A class for deserializing venue
 */

public class VenueFormatting implements JsonDeserializer<Venue> {

    @Override
    public Venue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        // Чтение полей из JSON
        String name = jsonObject.get("name").getAsString();
        VenueType type = null;
        if (jsonObject.has("type")) {
            String venueTypeStr = jsonObject.get("type").getAsString();
            try {
                type = VenueType.valueOf(venueTypeStr);
            }catch (IllegalArgumentException e){
                throw new JsonParseException("Ошибка обработки Venue");
            }
        }

        Long capacity = null;
        if (jsonObject.has("capacity")) capacity=jsonObject.get("capacity").getAsLong();
        Long id = jsonObject.get("id").getAsLong();
        if (id<=0 ||(jsonObject.has("capacity")&& capacity!=null && capacity<=0 )||name.isEmpty()) throw new JsonParseException("Ошибка обработки Venue");
        return new Venue(type,capacity,name,id);
    }
}