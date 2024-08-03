Overview
The Graph_M class represents a metro map using vertices and edges:

Vertices represent metro stations.
Edges represent connections between stations with associated distances or times.
This program supports:

Adding and removing vertices and edges.
Displaying the metro map and stations.
Calculating the shortest path between stations.
Calculating fare based on distance or time.
Identifying interchanges in the path.

Features

Add/Remove Vertices and Edges: Manage the metro map by adding or removing stations and connections.
Display Map and Stations: Print the current state of the metro map and list all stations.
Shortest Path Calculation: Find the shortest path between two stations using Dijkstra's algorithm.
Fare Calculation: Compute fares based on distance or time, including additional charges for interchanges.
Interchange Detection: Identify the number of interchanges in a path.

Setup

Clone the Repository:
Copy code
git clone <repository-url>

Compile the Code:
Navigate to the directory containing the Graph_M.java file and run:

Copy code
javac Graph_M.java
Run the Program:
Execute the compiled class file:

Copy code
java Graph_M
