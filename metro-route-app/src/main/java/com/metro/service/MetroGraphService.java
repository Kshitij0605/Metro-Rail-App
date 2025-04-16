package com.metro.service;

import com.metro.model.Station;
import com.metro.repository.StationRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;

@Service
public class MetroGraphService {

    private final StationRepository stationRepository;
    private final Map<String, Station> stations = new HashMap<>();

    public MetroGraphService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @PostConstruct
    public void initMetroMap() {
        addEdge("Noida Sector 62~B", "Botanical Garden~B", 8);
        addEdge("Botanical Garden~B", "Yamuna Bank~B", 10);
        addEdge("Yamuna Bank~B", "Vaishali~B", 8);
        addEdge("Yamuna Bank~B", "Rajiv Chowk~BY", 6);
        addEdge("Rajiv Chowk~BY", "Moti Nagar~B", 9);
        addEdge("Moti Nagar~B", "Janak Puri West~BO", 7);
        addEdge("Janak Puri West~BO", "Dwarka Sector 21~B", 6);
        addEdge("Huda City Center~Y", "Saket~Y", 15);
        addEdge("Saket~Y", "AIIMS~Y", 6);
        addEdge("AIIMS~Y", "Rajiv Chowk~BY", 7);
        addEdge("Rajiv Chowk~BY", "New Delhi~YO", 1);
        addEdge("New Delhi~YO", "Chandni Chowk~Y", 2);
        addEdge("Chandni Chowk~Y", "Vishwavidyalaya~Y", 5);
        addEdge("New Delhi~YO", "Shivaji Stadium~O", 2);
        addEdge("Shivaji Stadium~O", "DDS Campus~O", 7);
        addEdge("DDS Campus~O", "IGI Airport~O", 8);
        addEdge("Moti Nagar~B", "Rajouri Garden~BP", 2);
        addEdge("Punjabi Bagh West~P", "Rajouri Garden~BP", 2);
        addEdge("Punjabi Bagh West~P", "Netaji Subhash Place~PR", 3);

        stationRepository.saveAll(stations.values());
    }

    private void addEdge(String from, String to, int distance) {
        Station s1 = stations.computeIfAbsent(from, Station::new);
        Station s2 = stations.computeIfAbsent(to, Station::new);
        s1.addConnection(to, distance);
        s2.addConnection(from, distance);
    }

    public String shortestPath(String src, String dst, boolean byTime) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));

        for (String key : stations.keySet()) {
            dist.put(key, Integer.MAX_VALUE);
        }

        dist.put(src, 0);
        pq.add(src);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            Station station = stations.get(current);
            if (station == null) continue;

            for (Map.Entry<String, Integer> entry : station.getConnections().entrySet()) {
                String neighbor = entry.getKey();
                int weight = entry.getValue();
                int time = byTime ? (2 + weight) : weight;

                if (dist.get(current) + time < dist.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    dist.put(neighbor, dist.get(current) + time);
                    prev.put(neighbor, current);
                    pq.add(neighbor);
                }
            }
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();
        for (String at = dst; at != null; at = prev.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        StringBuilder result = new StringBuilder();
        result.append(byTime ? "Shortest time path:\n" : "Shortest distance path:\n");
        for (String p : path) {
            result.append(p).append(" -> ");
        }

        result.setLength(result.length() - 4); // remove last arrow
        int value = dist.get(dst);
        result.append(byTime ? ("\nTime: " + (int) Math.ceil(value / 60.0) + " mins") : ("\nDistance: " + value + " km"));
        return result.toString();
    }

    public double calculateFare(String src, String dest, boolean byTime) {
        int value = byTime ? getMinutes(src, dest) : getDistance(src, dest);
        if (byTime) return Math.ceil(value / 60.0) * 2.5; // ₹2.5 per min
        return value * 10; // ₹10 per km
    }

    public List<String> getInterchanges(String src, String dst) {
        String path = shortestPath(src, dst, false);
        String[] stops = path.split(" -> ");
        List<String> changes = new ArrayList<>();
        String lastLine = extractLineCode(stops[0]);

        for (int i = 1; i < stops.length; i++) {
            String currentLine = extractLineCode(stops[i]);
            if (!currentLine.equals(lastLine)) {
                changes.add(stops[i - 1] + " ==> " + stops[i]);
                lastLine = currentLine;
            }
        }
        changes.add("Total Interchanges: " + changes.size());
        return changes;
    }

    private String extractLineCode(String station) {
        int idx = station.indexOf('~');
        return (idx != -1 && idx < station.length() - 1) ? station.substring(idx + 1) : "";
    }

    private int getDistance(String src, String dst) {
        return extractValue(shortestPath(src, dst, false));
    }

    private int getMinutes(String src, String dst) {
        return (int) (Math.ceil(extractValue(shortestPath(src, dst, true)) / 60.0));
    }

    private int extractValue(String result) {
        String[] lines = result.split("\n");
        String lastLine = lines[lines.length - 1];
        return Integer.parseInt(lastLine.replaceAll("[^0-9]", ""));
    }

    public List<Station> getAllStations() {
        return new ArrayList<>(stations.values());
    }
}
