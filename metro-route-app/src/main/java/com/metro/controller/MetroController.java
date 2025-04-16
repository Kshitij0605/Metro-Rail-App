package com.metro.controller;

import com.metro.model.Station;
import com.metro.service.MetroGraphService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MetroController {

    private final MetroGraphService metroService;

    public MetroController(MetroGraphService metroService) {
        this.metroService = metroService;
    }

    // ✅ Returns only station names for frontend to render list/dropdown
    @GetMapping("/stations")
    public List<String> getAllStationNames() {
        return metroService.getAllStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

    // ✅ Distance-based route
    @GetMapping("/route")
    public String route(@RequestParam String from, @RequestParam String to) {
        return metroService.shortestPath(from, to, false);
    }

    // ✅ Time-based route
    @GetMapping("/route-time")
    public String routeTime(@RequestParam String from, @RequestParam String to) {
        return metroService.shortestPath(from, to, true);
    }

    // ✅ Calculate fare (by distance or time)
    @GetMapping("/fare")
    public String fare(@RequestParam String from, @RequestParam String to, @RequestParam(defaultValue = "distance") String type) {
        boolean byTime = type.equalsIgnoreCase("time");
        return "Fare: ₹" + metroService.calculateFare(from, to, byTime);
    }

    // ✅ Get interchange list
    @GetMapping("/interchanges")
    public List<String> interchanges(@RequestParam String from, @RequestParam String to) {
        return metroService.getInterchanges(from, to);
    }
}
