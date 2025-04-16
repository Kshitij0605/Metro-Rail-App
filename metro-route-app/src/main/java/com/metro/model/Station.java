package com.metro.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Station {

    @Id
    private String name;

    // Use @ElementCollection with @CollectionTable for map persistence
    @ElementCollection
    @CollectionTable(name = "station_connections", joinColumns = @JoinColumn(name = "station_name"))
    @MapKeyColumn(name = "connected_station")
    @Column(name = "distance")
    private Map<String, Integer> connections = new HashMap<>();

    public Station() {}

    public Station(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getConnections() {
        return connections;
    }

    public void addConnection(String to, int distance) {
        this.connections.put(to, distance);
    }
}
