package CSCI3901;

import java.util.*;

public class VertexCluster1 {

    // Edge class to create a new edge
    static class Edge implements Comparable<Edge> {
        String source;
        String destination;
        int weight;

        public Edge(String source, String destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge o) {
            if (this.weight - o.weight > 0) {
                return 1;
            } else if (this.weight - o.weight == 0) {
                if (this.source.compareTo(o.source) > 0) {
                    return 1;
                } else if (this.source.compareTo(o.source) == 0) {
                    if (this.destination.compareTo(o.destination) > 0) {
                        return 1;
                    }
                }
            }
            return -1;
        }
    }

    static class Vertex implements Comparable<Vertex> {
        String name;
        int weight;
        int cluster;

        public Vertex(String name, int weight, int cluster) {
            this.name = name;
            this.weight = weight;
            this.cluster = cluster;
        }

        @Override
        public int compareTo(Vertex o) {
            if (this.name.compareTo(o.name) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    // Graph class to create and find the MST
    static class Graph {
        int vertices;
        int numCluster = 0;

        // List of all edges
        ArrayList<Edge> allEdges = new ArrayList<>();

        // List of all vertices
        ArrayList<Vertex> allVertices = new ArrayList<>();

        // Store the set of clusters
        Set<Set<String>> clusterSet = new HashSet<>();

        // Parameterized constructor
        Graph(int vertices) {

            // No.of vertices should be greater than 0
            if (vertices > 0) {
                this.vertices = vertices;
            }

        }

        // Method to validate the input
        public boolean validateInput(String source, String destination, int weight) {

            // Source & destination cannot be null
            if (source == null || destination == null) {
                return false;
            }

            // Trimming extra spaces
            source = source.trim();
            destination = destination.trim();

            // Source & destination cannot be empty
            if (source.isEmpty() || destination.isEmpty()) {
                return false;
            }

            // Checking if source & destination are same
            if (source.equals(destination)) {
                return false;
            }

            // Weight cannot be negative
            if (weight < 0) {
                return false;
            }

            // All parameters are valid
            return true;
        }

        // Method to add edges to allEdges arraylist
        public boolean addEdge(String source, String destination, int weight) {

            // Calling validateInput method for Input validation
            boolean validate = validateInput(source, destination, weight);

            if (validate) {

                // Creating a new Edge
                Edge edge = new Edge(source.trim(), destination.trim(), weight);

                // Current edge's source >  destination lexicographically
                // Swapping the current edge's source & destination
                if (edge.source.compareTo(edge.destination) > 0) {
                    String tmp = edge.source;
                    edge.source = edge.destination;
                    edge.destination = tmp;
                }

                // Storing trimmed source & destination in local variables
                String fSource = source.trim();
                String fDestination = destination.trim();

                // Checking whether an edge exists between the fSource & fDestination vertices
                boolean edgeExists = allEdges.stream().anyMatch(edges ->
                        (edges.source.equals(fSource) && (edges.destination.equals(fDestination)) ||
                                (edges.source.equals(fDestination) && (edges.destination.equals(fSource)))));

                // If an edge exists then we return false & don't add the edge
                if (edgeExists) {
                    return false;
                }
                // Adding the current edge to the allEdges arraylist
                allEdges.add(edge);

                return true;
            } else {

                // Returns false if input is not valid
                return false;
            }
        }

        // Private method to add vertices from allEdges arraylist
        // Adding from input validated allEdges arraylist so no need for input validation
        private void addVertex() {

            // Iterating through allEdges
            for (int i = 0; i < allEdges.size(); i++) {

                // Getting the source vertex of the current edge
                String source = allEdges.get(i).source;

                // Getting the destination vertex of the current edge
                String destination = allEdges.get(i).destination;

                // Comparing allVertices stream to the source vertex
                boolean vertexExists1 = allVertices.stream().anyMatch(vertexes ->
                        (vertexes.name.equals(source)));

                // If vertex does not exist
                if (!vertexExists1) {

                    // Create a new vertex
                    Vertex vertex = new Vertex(source, 1, ++numCluster);

                    // add to total vertices
                    allVertices.add(vertex);
                }

                // Comparing allVertices stream to the destination vertex
                boolean vertexExists2 = allVertices.stream().anyMatch(vertexes ->
                        (vertexes.name.equals(destination)));

                // If vertex does not exist
                if (!vertexExists2) {

                    // Create a new vertex
                    Vertex vertex = new Vertex(destination, 1, ++numCluster);

                    // add to total vertices
                    allVertices.add(vertex);
                }
            }
        }

        public Set<Set<String>> clusterVertices(float tolerance) {

            // Tolerance cannot be negative
            if (tolerance < 0) {
                return null;
            }

            // HashMap with key as vertexes cluster & value as the heaviest edge used to create the cluster
            HashMap<Integer, Integer> cWMap = new HashMap<>();

            // HashMap with key vertexes cluster & value as arraylist of all vertex indices
            HashMap<Integer, ArrayList<Integer>> cVMap = new HashMap<>();

            // Variables to hold the index of the vertices of the current edge
            int v1Index = 0;
            int v2Index = 0;

            // Iterate through allEdges arraylist
            for (Edge edge : allEdges) {

                // looping through allVertices arraylist to get index of vertices
                for (int i = 0; i < allVertices.size(); i++) {
                    if (allVertices.get(i).name.equals(edge.source)) {
                        v1Index = i;
                    }
                    if (allVertices.get(i).name.equals(edge.destination)) {
                        v2Index = i;
                    }
                }

                // Fetching the vertex object for the above vertex indices
                Vertex v1 = allVertices.get(v1Index);
                Vertex v2 = allVertices.get(v2Index);

                // Comparing the cluster of both vertices
                if (v1.cluster != v2.cluster) {
                    int v1Weight = cWMap.getOrDefault(v1.cluster, 1);
                    int v2Weight = cWMap.getOrDefault(v2.cluster, 1);

                    // Calculating the tolerance
                    float calcTolerance = (float) edge.weight / Math.min(v1Weight, v2Weight);

                    //
                    if (calcTolerance <= tolerance) {

                        // Max weight in current cluster
                        int maxWeight = Math.max(edge.weight, Math.max(v1Weight, v2Weight));

                        // Add/Update weight of v1 cluster
                        cWMap.put(v1.cluster, maxWeight);

                        // Merge v2 cluster in v1 cluster
                        if (cVMap.containsKey(v1.cluster)) {
                            if (cVMap.containsKey(v2.cluster)) {
                                for (Integer vIndex : cVMap.get(v2.cluster)) {
                                    allVertices.get(vIndex).cluster = v1.cluster;
                                    ArrayList<Integer> clusterVertexIndices = cVMap.get(v1.cluster);
                                    clusterVertexIndices.add(vIndex);
                                    cVMap.put(v1.cluster, clusterVertexIndices);
                                }
                                cVMap.get(v2.cluster).clear();
                            } else {
                                v2.cluster = v1.cluster;
                                ArrayList<Integer> clusterVertexIndices = cVMap.get(v1.cluster);
                                clusterVertexIndices.add(v2Index);
                                cVMap.put(v1.cluster, clusterVertexIndices);
                            }
                        } else {
                            v2.cluster = v1.cluster;
                            ArrayList<Integer> clusterVertexIndices = new ArrayList<>();
                            clusterVertexIndices.add(v1Index);
                            clusterVertexIndices.add(v2Index);
                            cVMap.put(v1.cluster, clusterVertexIndices);
                        }
                    } else {
                        if (!cWMap.containsKey(v1.cluster)) {
                            createSeparateClusters(v1.cluster, cWMap, cVMap, v1Index);
                        }

                        if (!cWMap.containsKey(v1.cluster)) {
                            createSeparateClusters(v2.cluster, cWMap, cVMap, v2Index);
                        }
                    }
                }
            }

            allVertices.sort(Comparator.comparingInt(o -> o.cluster));

            for(Vertex v: allVertices){
                System.out.println(v.cluster);
            }

            Set<String> cluster = null;
            int c = -1;
            for (Vertex vertex : allVertices) {
                if (c == -1) {
                    cluster = new HashSet<>();
                    c = vertex.cluster;
                    cluster.add(vertex.name);
                } else if (c == vertex.cluster) {
                    cluster.add(vertex.name);
                } else {
                    //System.out.println(cluster);
                    clusterSet.add(cluster);
                    c = vertex.cluster;
                    cluster = new HashSet<>();
                    cluster.add(vertex.name);
                }
            }
            clusterSet.add(cluster);

            return clusterSet;
        }

        private static void createSeparateClusters(int startCluster, HashMap<Integer, Integer> cWMap, HashMap<Integer, ArrayList<Integer>> cVMap, int v2Index) {
            cWMap.put(startCluster, 1);
            ArrayList<Integer> vIndices = new ArrayList<>();
            vIndices.add(v2Index);
            cVMap.put(startCluster, vIndices);
        }

        public void printGraph(ArrayList<Edge> edgeList) {
            for (int i = 0; i < edgeList.size(); i++) {
                Edge edge = edgeList.get(i);
                System.out.println("Edge-" + i + " source: " + edge.source +
                        " destination: " + edge.destination +
                        " weight: " + edge.weight);
            }
        }

        public void printClusters(ArrayList<Vertex> clusterList) {
            for (int i = 0; i < clusterList.size(); i++) {
                Vertex vertex = clusterList.get(i);
                System.out.println("Vertex-" + i + " name: " + vertex.name +
                        " weight: " + vertex.weight +
                        " cluster: " + vertex.cluster);
            }
        }
    }

    public static void main(String[] args) {
        int vertices = 10;
        float tolerance = 3;

        Graph graph = new Graph(vertices);

        graph.addEdge(null, null, -1);
        graph.addEdge(null, null, 0);
        graph.addEdge("A", "A", 3);
        graph.addEdge("A", "B", 3);
        graph.addEdge("A  ", "B", 3);
        graph.addEdge("A", "H", 1);
        graph.addEdge("I", "H", 1);
        graph.addEdge("   I", "B", 4);
        graph.addEdge("B", "C", 7);
        graph.addEdge("I", "C", 8);
        graph.addEdge("G", "H", 7);
        graph.addEdge("H", "  J  ", 10);
        graph.addEdge("H", "  J  ", 10);
        graph.addEdge("D", "I", 12);
        graph.addEdge("D", "C", 14);
        graph.addEdge("D", "E", 8);
        graph.addEdge("    D", "E   ", 10);
        graph.addEdge("J", "F", 3);
        graph.addEdge("E", "F", 2);
        graph.addEdge("F", "G", 7);
        graph.addEdge("J", "D", 1);
        //graph.addEdge("     X", "     Z     ", 15);
        //graph.addEdge("      Y", "W", 19);

        System.out.println("No.of edges: " + graph.allEdges.size() + "\n------------------");

        // Sorting allEdges using Comparable
        Collections.sort(graph.allEdges);

        // Calling the method to add vertices
        graph.addVertex();

        // Sorting allVertices using Comparable
        Collections.sort(graph.allVertices);

        // Calling the clusterVertices method
        graph.clusterVertices(tolerance);

    }
}
