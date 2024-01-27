package com.receipt;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.json.tree.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class ReceiptController {
    private static HashMap<UUID, Long> points;

    static {
        points =  new HashMap<>();
    }

    @Get("/receipts/{id}/points")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> getPoints(@PathVariable UUID id) {
        if(id == null) return HttpResponse.notFound("No receipt found for that id");
        //Check points hashmap if key exists
        if(points.containsKey(id)) {
            return HttpResponse.ok(JsonObject.createObjectNode(Map.of("points", JsonNode.createNumberNode(points.get(id)))));
        }
        return HttpResponse.notFound("No receipt found for that id");
    }

    @Post("/receipts/process")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> processReceipts(@Body Receipt payload) {
        long points = 0L;
        //Checking whether retailer has alphanumeric characters
        String retailer = payload.getRetailer();
        if(retailer == null) return HttpResponse.badRequest("The receipt is invalid");
        for(int i = 0 ; i < retailer.length();i++){
            if(Character.isLetterOrDigit(retailer.charAt(i))){
                points++;
            }
        }
        //Checking total for potential points
        if(payload.getTotal() == null) return HttpResponse.badRequest("The receipt is invalid");
        double total = Double.parseDouble(payload.getTotal());
        if(total % 1 == 0) points += 50;
        if(total % 0.25 == 0) points += 25;

        //Going through items and adding points if criteria is met
        Item[] items = payload.getItems();
        if(items == null || items.length == 0) return HttpResponse.badRequest("The receipt is invalid");
        points += (items.length/2)* 5L;
        for (Item item : items) {
            String desc = item.getShortDescription();
            if(desc == null || item.getPrice() == null) return HttpResponse.badRequest("The receipt is invalid");
            double price = Double.parseDouble(item.getPrice());
            if (desc.trim().length() % 3 == 0) points += (long) Math.ceil(price * 0.2);
        }

        //Checking for purchase date being on odd days
        String purchaseDate = payload.getPurchaseDate();
        if(purchaseDate == null) return HttpResponse.badRequest("The receipt is invalid");
        int day = Integer.parseInt(purchaseDate.split("-")[2]);
        if(day % 2 != 0) points += 6;
        //Checking for purchase time between 2:00pm and 4:00pm in 24hr format
        String purchaseTime = payload.getPurchaseTime();
        if(purchaseTime == null) return HttpResponse.badRequest("The receipt is invalid");
        int hour = Integer.parseInt(purchaseTime.split(":")[0]);
        if(hour >= 14 && hour <= 16) points += 10;

        //Assigning a UUID to each receipt (Note: each retailer could have multiple receipts but
        //each receipt will have its own UUID, will need additional functionality to keep aggregate
        //of points for each retailer.)
        UUID id = UUID.randomUUID();
        ReceiptController.points.put(id,points);
        JsonNode obj = JsonObject.createObjectNode(Map.of("id", JsonNode.createStringNode(id.toString())));
        return HttpResponse.ok(obj);
    }
}