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
    public HttpResponse<?> getPoints(@PathVariable UUID id){
        Map<String, JsonNode> map = Map.of("points", JsonNode.createNumberNode(points.getOrDefault(id,0L)));
        JsonNode obj = JsonObject.createObjectNode(map);
        return HttpResponse.ok(obj);
    }

    @Post("/receipts/process")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> processReceipts(@Body JsonObject payload){
        long points = 0L;
        System.out.println(payload);
        String retailer = payload.get("retailer").getStringValue();
        System.out.println(retailer);
        for(int i = 0 ; i < retailer.length();i++){
            if(Character.isLetterOrDigit(retailer.charAt(i))){
                points++;
            }
        }
        double total = Double.parseDouble(payload.get("total").getStringValue());
        if(total % 1 == 0) points += 50;
        if(total % 0.25 == 0) points += 25;
        JsonNode items = payload.get("items");
        System.out.println(items);
        points += (items.size()/2)* 5L;
        for(int i = 0; i < items.size();i++){
            String desc = items.get(i).get("shortDescription").getStringValue();
            double price = Double.parseDouble(items.get(i).get("price").getStringValue());
            if(desc.trim().length() % 3 == 0) points += (long) Math.ceil(price*0.2);
        }
        String purchaseDate = payload.get("purchaseDate").getStringValue();
        int day = Integer.parseInt(purchaseDate.split("-")[2]);
        if(day % 2 != 0) points += 6;
        String purchaseTime = payload.get("purchaseTime").getStringValue();
        int hour = Integer.parseInt(purchaseTime.split(":")[0]);
        if(hour >= 14 && hour <= 16) points += 10;
        System.out.println("Total points: " + points);
        UUID id = UUID.randomUUID();
        ReceiptController.points.put(id,points);
        JsonNode obj = JsonObject.createObjectNode(Map.of("id", JsonNode.createStringNode(id.toString())));
        return HttpResponse.ok(obj);
    }
}