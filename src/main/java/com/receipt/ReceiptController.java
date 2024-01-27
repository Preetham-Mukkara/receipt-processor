package com.receipt;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.json.tree.JsonObject;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class ReceiptController {
    private static HashMap<String, Long> points;

    static {
        points =  new HashMap<>();
    }

    @Get("/receipts/{id}/points")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> getPoints(@PathVariable String id) {
        String idPattern = "^\\S+$";
        if(!id.matches(idPattern)) return HttpResponse.notFound("id is not in expected format");
        //Check points hashmap if id exists
        if(points.containsKey(id)) {
            return HttpResponse.ok(JsonObject.createObjectNode(Map.of("points", JsonNode.createNumberNode(points.get(id)))));
        }
        return HttpResponse.notFound("No receipt found for that id");
    }

    @Post("/receipts/process")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> processReceipts(@Valid @Body Receipt payload) {
        long points = 0L;
        //Checking whether retailer has alphanumeric characters
        String retailer = payload.getRetailer();
        for(int i = 0 ; i < retailer.length();i++){
            if(Character.isLetterOrDigit(retailer.charAt(i))){
                points++;
            }
        }

        //Checking total for potential points
        String totalPattern = "^\\d+\\.\\d{2}$";
        if(!payload.getTotal().matches(totalPattern)) return HttpResponse.badRequest("Total is not of the expected format. The receipt is invalid.");
        double total = Double.parseDouble(payload.getTotal());
        if(total % 1 == 0) points += 50;
        if(total % 0.25 == 0) points += 25;

        //Going through items and adding points if criteria is met
        Item[] items = payload.getItems();
        if(items.length == 0) return HttpResponse.badRequest("There should be at least one item. The receipt is invalid.");
        points += (items.length/2)* 5L;
        for (Item item : items){
                String desc = item.getShortDescription();
                String descPattern = "^[\\w\\s\\-]+$";
                if(!desc.matches(descPattern)) return HttpResponse.badRequest("Description is not of expected format. The receipt is invalid.");
                String pricePattern = "^\\d+\\.\\d{2}$";
                if(!item.getPrice().matches(pricePattern)) return HttpResponse.badRequest("Price is not of expected format. The receipt is invalid.");
                double price = Double.parseDouble(item.getPrice());
                if (desc.trim().length() % 3 == 0) points += (long) Math.ceil(price * 0.2);
        }

        //Checking for purchase date being on odd days
        String purchaseDate = payload.getPurchaseDate();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        try{
            LocalDate date = LocalDate.parse(purchaseDate,dateFormatter);
            int day = date.getDayOfMonth();
            if(day % 2 != 0) points += 6;
        } catch ( DateTimeParseException e ){
            return HttpResponse.badRequest("The purchase date is invalid.");
        }

        //Checking for purchase time between 2:00pm and 4:00pm in 24hr format
        String purchaseTime = payload.getPurchaseTime();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        try{
            LocalTime time = LocalTime.parse(purchaseTime, timeFormatter);
            int hour = time.getHour();
            if(hour >= 14 && hour <= 16) points += 10;
        } catch(DateTimeParseException e){
            return HttpResponse.badRequest("The purchase time is invalid.");
        }

        //Assigning a UUID to each receipt (Note: each retailer could have multiple receipts but
        //each receipt will have its own UUID, will need additional functionality to keep aggregate
        //of points for each retailer.)
        UUID id = UUID.randomUUID();
        ReceiptController.points.put(id.toString(),points);
        JsonNode obj = JsonObject.createObjectNode(Map.of("id", JsonNode.createStringNode(id.toString())));
        return HttpResponse.ok(obj);
    }
}