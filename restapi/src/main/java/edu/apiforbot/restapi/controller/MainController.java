package edu.apiforbot.restapi.controller;

import edu.apiforbot.restapi.domain.Item;
import edu.apiforbot.restapi.domain.ShopsWithBestDiscounts;
import edu.apiforbot.restapi.parser.Parser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/")
public class MainController {

    @GetMapping(produces = "application/json", path = "shops/{cityName}")
    public List<ShopsWithBestDiscounts> getGeneralInfo(@PathVariable String cityName){
        Parser parser = new Parser();
        return parser.getShopsWithBestDiscounts(cityName)
                .stream()
                .limit(6)
                .collect(Collectors.toList());
    }

    @GetMapping(produces = "application/json", path = "discounts/{cityName}")
    public List<Item> getBestDiscounts(@PathVariable String cityName){
        Parser parser = new Parser();
        return parser.getBestDiscounts(cityName, null)
                .stream()
                .limit(6)
                .collect(Collectors.toList());
    }

    @GetMapping(produces = "application/json", path = "discounts/{cityName}/{itemName}")
    public List<Item> getRequiredItems(@PathVariable String cityName,
                                       @PathVariable String itemName){
        Parser parser = new Parser();
        return parser.getBestDiscounts(cityName, itemName)
                .stream()
                .limit(6)
                .collect(Collectors.toList());
    }
}
