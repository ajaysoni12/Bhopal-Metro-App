package com.example.bhopalmetroapp;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

public class MetroUtilities {

    ArrayList<JunctionDetails> junctionDetailsList = new ArrayList<>();
    ArrayList<String> allStationList = new ArrayList<>();
    ArrayList<ArrayList<Pair<Integer, Pair<Double, Integer>>>> adjacencyList = new ArrayList<>();
    ArrayList<ArrayList<Pair<Integer, Pair<Double, Double>>>> adjacencyListCost = new ArrayList<>();
    Context context;

    public MetroUtilities(Context context) {
        this.context = context;
    }

    public String convertJsonToString() {
        String jsonString = null;
        InputStream is = context.getResources().openRawResource(R.raw.bhopal_metro);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];

        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            jsonString = writer.toString();
            Log.d("Res", jsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public ArrayList<JunctionDetails> getJunctionDetailsList() {
        if (junctionDetailsList.size() != 0) return junctionDetailsList;

        String jsonString = convertJsonToString();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("junctions");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject junctionObject = jsonArray.getJSONObject(i);

                JunctionDetails junctionDetails = new JunctionDetails();

                // Extracting data from the current junction object
                String name = junctionObject.getString("name");
                String firstMetro = junctionObject.getString("first_metro_time");
                String lastMetro = junctionObject.getString("last_metro_time");

                ArrayList<String> facilities = new ArrayList<>();
                JSONArray facilitiesJsonArray = junctionObject.getJSONArray("facilities");
                for (int j = 0; j < facilitiesJsonArray.length(); j++) {
                    facilities.add("\n" + facilitiesJsonArray.getString(j));
                }
                facilities.add("\n");

                ArrayList<String> lines = new ArrayList<>();
                JSONArray linesJsonArray = junctionObject.getJSONArray("lines");
                for (int j = 0; j < linesJsonArray.length(); j++) {
                    lines.add(linesJsonArray.getString(j));
                }

                ArrayList<String> near_parking_slots = new ArrayList<>();
                JSONArray near_parking_slotsJsonArray = junctionObject.getJSONArray("near_parking_slots");
                for (int j = 0; j < near_parking_slotsJsonArray.length(); j++) {
                    near_parking_slots.add("\n" + near_parking_slotsJsonArray.getString(j));
                }
                near_parking_slots.add("\n");

                ArrayList<String> upcoming_metro = new ArrayList<>();
                JSONArray upcoming_metroJsonArray = junctionObject.getJSONArray("upcoming_metro");
                for (int j = 0; j < upcoming_metroJsonArray.length(); j++) {
                    upcoming_metro.add("\n" + upcoming_metroJsonArray.getString(j));
                }
                upcoming_metro.add("\n");

                junctionDetails.setName(name);
                junctionDetails.setFirstMetroTime(firstMetro);
                junctionDetails.setLastMetroTime(lastMetro);
                junctionDetails.setFacilities(facilities);
                junctionDetails.setLines(lines);
                junctionDetails.setNearParkingSlots(near_parking_slots);
                junctionDetails.setUpcomingMetro(upcoming_metro);

                Log.d("Name", junctionDetails.getName());
                Log.d("firstMetro", junctionDetails.getFirstMetroTime());
                Log.d("lines", junctionDetails.getLines().get(0));
                junctionDetailsList.add(junctionDetails);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return junctionDetailsList;
    }

    public ArrayList<String> getStationList(HashMap<String, Integer> stationToCode, HashMap<Integer, String> codeToStation,
                                            HashMap<String, String> stationToLines) {
        if (allStationList.size() != 0) return allStationList;
        String jsonString = convertJsonToString();
        try {
            JSONObject jsonRoutes = new JSONObject(jsonString).getJSONObject("routes");
            for (int i = 1; i <= 6; i++) {
                JSONArray jsonRoutesLines = jsonRoutes.getJSONObject("Line-" + i).getJSONArray("stations");
                for (int j = 0; j < jsonRoutesLines.length(); j++) {
                    String stationName = jsonRoutesLines.getJSONObject(j).getString("name");
                    Integer stationCode = jsonRoutesLines.getJSONObject(j).getInt("code");
                    stationToCode.put(stationName, stationCode);
                    codeToStation.put(stationCode, stationName);
                    stationToLines.put(stationName, "Line-" + i);
                    allStationList.add(stationName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return allStationList;
    }

    public ArrayList<ArrayList<String>> getArrivalDepartureTime(int stationCode) {
        System.out.println("" + stationCode);
        ArrayList<ArrayList<String>> arrivalDepartureTime = new ArrayList<>();

        String jsonString = convertJsonToString();
        try {
            int code = 0;
            JSONObject jsonRoutes = new JSONObject(jsonString).getJSONObject("routes");
            for (int i = 1; i <= 6; i++) {
                JSONObject jsonRoutesLines = jsonRoutes.getJSONObject("Line-" + i).getJSONObject("train_times");
                JSONArray jsonRoutesLines1 = jsonRoutes.getJSONObject("Line-" + i).getJSONArray("stations");
                for (int j = 0; j < jsonRoutesLines1.length(); j++) {
                    /*String stationName = jsonRoutesLines.getJSONObject(j).getString("name");
                    Integer stationCode = jsonRoutesLines.getJSONObject(j).getInt("code");
                    stationToCode.put(stationName, stationCode);
                    codeToStation.put(stationCode, stationName);
                    stationToLines.put(stationName, "Line-" + i);
                    allStationList.add(stationName);*/
                    if (code == stationCode) {
                        JSONArray jsonArray = jsonRoutesLines.getJSONObject("" + code).getJSONArray("arrival");
                        JSONArray jsonArray1 = jsonRoutesLines.getJSONObject("" + code).getJSONArray("departure");

                        ArrayList<String> arrival = new ArrayList<>();
                        ArrayList<String> departure = new ArrayList<>();

                        for (int k = 0; k < jsonArray.length(); k++) {
                            System.out.println("" + jsonArray.get(k));
                            System.out.println("" + jsonArray1.get(k));
                            // arrival.add(jsonArray.get(k))
                            arrival.add((String) jsonArray.get(k));
                            departure.add((String) jsonArray1.get(k));

                        }
                        arrivalDepartureTime.add(arrival);
                        arrivalDepartureTime.add(departure);
                        break;
                    }

                    code++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return arrivalDepartureTime;
    }

    public ArrayList<ArrayList<Pair<Integer, Pair<Double, Double>>>> getAdjacencyListCost() {
        if (adjacencyListCost.size() != 0) return adjacencyListCost;

        String jsonString = convertJsonToString();

        try {
            JSONObject jsonAdjacencyList = new JSONObject(jsonString).getJSONObject("adjacency_list");
            for (int node = 0; node < 72; node++) {
                JSONArray jsonArray = jsonAdjacencyList.getJSONArray("" + node);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Iterator<String> keys = jsonObject.keys();

                    ArrayList<Pair<Integer, Pair<Double, Double>>> adj = new ArrayList<>();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        Log.d("Key", key); // or do whatever you want with the key

                        int adjNode = Integer.parseInt(key);
                        double distance = jsonObject.getJSONObject(key).getDouble("distance");
                        double time = jsonObject.getJSONObject(key).getDouble("cost");

                        adj.add(new Pair<>(adjNode, new Pair<>(distance, time)));

                    }
                    adjacencyListCost.add(adj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return adjacencyListCost;
    }

    public ArrayList<ArrayList<Pair<Integer, Pair<Double, Integer>>>> getAdjacencyList() {
        if (adjacencyList.size() != 0) return adjacencyList;

        String jsonString = convertJsonToString();

        try {
            JSONObject jsonAdjacencyList = new JSONObject(jsonString).getJSONObject("adjacency_list");
            for (int node = 0; node < 72; node++) {
                JSONArray jsonArray = jsonAdjacencyList.getJSONArray("" + node);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Iterator<String> keys = jsonObject.keys();

                    ArrayList<Pair<Integer, Pair<Double, Integer>>> adj = new ArrayList<>();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        Log.d("Key", key); // or do whatever you want with the key

                        int adjNode = Integer.parseInt(key);
                        double distance = jsonObject.getJSONObject(key).getDouble("distance");
                        int time = jsonObject.getJSONObject(key).getInt("time");

                        adj.add(new Pair<>(adjNode, new Pair<>(distance, time)));

                    }
                    adjacencyList.add(adj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return adjacencyList;
    }

    public ArrayList<String> findPathAccordingToDistance(int sourceStationCode, int destinationStationCode,
                                                         HashMap<Integer, String> codeToStation) {
        ArrayList<String> path = new ArrayList<>();

        // Initialize distances array and priority queue
        double[] distances = new double[72];
        int[] predecessor = new int[72]; // To store predecessors for path reconstruction
        boolean[] visited = new boolean[72];

        // {distance, node}
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();

        // Initialize distances array with infinity and mark all nodes as unvisited
        for (int i = 0; i < 72; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
            visited[i] = false;
        }

        // Distance from source to itself is 0
        distances[sourceStationCode] = 0.0;
        pq.offer(new NodeDistance(0.0, sourceStationCode));

        // Dijkstra's algorithm
        while (!pq.isEmpty()) {
            // Extract the node with the minimum distance
            NodeDistance minNode = pq.poll();
            if (minNode == null) continue;
            int u = minNode.node;
            visited[u] = true;

            // Explore neighbors of the current node
            for (Pair<Integer, Pair<Double, Integer>> neighbor : adjacencyList.get(u)) {
                int v = neighbor.first;
                double weight = neighbor.second.first;

                // Relaxation step: Update distance if shorter path is found
                if (!visited[v] && distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                    pq.offer(new NodeDistance(distances[v], v));
                    // Update predecessor for path reconstruction
                    predecessor[v] = u;
                }
            }
        }

        if (distances[destinationStationCode] == Double.POSITIVE_INFINITY) {
            path.add("There is no direct path!");
            System.out.println("No path");
            return path;
        }

        int current = destinationStationCode;
        while (current != sourceStationCode) {
            String stationName = codeToStation.get(current);
            path.add(stationName);
            current = predecessor[current];
        }
        path.add(codeToStation.get(sourceStationCode));
        Collections.reverse(path);

        for (int i = 0; i < path.size(); i++) {
            System.out.println(path.get(i));
        }
        return path;
    }

    public ArrayList<String> findPathAccordingToCost(int sourceStationCode, int destinationStationCode,
                                                     HashMap<Integer, String> codeToStation) {
        ArrayList<String> path = new ArrayList<>();

        // Initialize distances array and priority queue
        double[] distances = new double[72];
        int[] predecessor = new int[72]; // To store predecessors for path reconstruction
        boolean[] visited = new boolean[72];

        // {distance, time}
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();

        // Initialize distances array with infinity and mark all nodes as unvisited
        for (int i = 0; i < 72; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
            visited[i] = false;
        }

        // Distance from source to itself is 0
        distances[sourceStationCode] = 0.0;
        pq.offer(new NodeDistance(0.0, sourceStationCode));

        // Dijkstra's algorithm
        while (!pq.isEmpty()) {
            // Extract the node with the minimum distance
            NodeDistance minNode = pq.poll();
            if (minNode == null) continue;
            int u = minNode.node;
            visited[u] = true;

            // Explore neighbors of the current node
            for (Pair<Integer, Pair<Double, Double>> neighbor : adjacencyListCost.get(u)) {
                int v = neighbor.first;
                double weight = neighbor.second.first;

                // Relaxation step: Update distance if shorter path is found
                if (!visited[v] && distances[u] + (weight) < distances[v]) {
                    distances[v] = distances[u] + (weight);
                    pq.offer(new NodeDistance(distances[v], v));
                    // Update predecessor for path reconstruction
                    predecessor[v] = u;
                }
            }
        }

        if (distances[destinationStationCode] == Double.POSITIVE_INFINITY) {
            path.add("There is no direct path!");
            System.out.println("No path");
            return path;
        }

        int current = destinationStationCode;
        while (current != sourceStationCode) {
            String stationName = codeToStation.get(current);
            path.add(stationName);
            current = predecessor[current];
        }
        path.add(codeToStation.get(sourceStationCode));
        Collections.reverse(path);

        for (int i = 0; i < path.size(); i++) {
            System.out.println(path.get(i));
        }
        return path;
    }

    public double findPathAccordingToCost1(int sourceStationCode, int destinationStationCode,
                                           HashMap<Integer, String> codeToStation) {
        // Initialize distances array and priority queue
        double[] distances = new double[72];
        boolean[] visited = new boolean[72];

        // {distance, time}
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();

        // Initialize distances array with infinity and mark all nodes as unvisited
        for (int i = 0; i < 72; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
            visited[i] = false;
        }

        // Distance from source to itself is 0
        distances[sourceStationCode] = 0.0;
        pq.offer(new NodeDistance(0.0, sourceStationCode));

        // Dijkstra's algorithm
        while (!pq.isEmpty()) {
            // Extract the node with the minimum distance
            NodeDistance minNode = pq.poll();
            if (minNode == null) continue;
            int u = minNode.node;
            visited[u] = true;

            // Explore neighbors of the current node
            for (Pair<Integer, Pair<Double, Integer>> neighbor : adjacencyList.get(u)) {
                int v = neighbor.first;
                double weight = neighbor.second.first;

                // Relaxation step: Update distance if shorter path is found
                if (!visited[v] && distances[u] + (weight * 2.5) < distances[v]) {
                    distances[v] = distances[u] + (weight * 2.5);
                    pq.offer(new NodeDistance(distances[v], v));
                    // Update predecessor for path reconstruction
                }
            }
        }

        if (distances[destinationStationCode] == Double.POSITIVE_INFINITY) {
            System.out.println("No path");
            return Double.MAX_VALUE;
        }

        return distances[destinationStationCode];
    }

    static class NodeTime implements Comparable<NodeTime> {
        int node;
        int time;

        public NodeTime(int time, int node) {
            this.node = node;
            this.time = time;
        }

        @Override
        public int compareTo(NodeTime other) {
            return Double.compare(this.time, other.time);
        }
    }

    static class NodeDistance implements Comparable<NodeDistance> {
        int node;
        double distance;

        public NodeDistance(double distance, int node) {
            this.node = node;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeDistance other) {
            // Compare based on distance
            return Double.compare(this.distance, other.distance);
        }
    }
}
