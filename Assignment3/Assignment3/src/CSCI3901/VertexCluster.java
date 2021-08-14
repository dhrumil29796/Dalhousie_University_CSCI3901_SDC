package CSCI3901;


import java.util.*;

// Class to implement Graph Clustering
public class VertexCluster {

    // Edge class to create a new edge
    private static class Edge implements Comparable<Edge> {
        String source;
        String destination;
        int weight;

        // Parameterized constructor to create a new Edge
        private Edge(String source, String destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        // Overriding compareTo method by implementing Comparable
        // Writing custom logic for sorting the allEdges arraylist
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

    // Vertex class to create a new vertex
    private static class Vertex implements Comparable<Vertex> {
        String name;
        int weight;
        int cluster;

        // Parameterized constructor to create a new Vertex
        private Vertex(String name, int weight, int cluster) {
            this.name = name;
            this.weight = weight;
            this.cluster = cluster;
        }

        // Overriding compareTo method by implementing Comparable
        // Writing custom logic for sorting the allVertices arraylist
        @Override
        public int compareTo(Vertex o) {
            if (this.name.compareTo(o.name) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    // Initial cluster number starting at 0
    // Assigned to each vertex & keeps increasing as new vertices are added
    int numCluster = 0;

    // List of all the edges
    ArrayList<Edge> allEdges = new ArrayList<>();

    // List of all the vertices
    ArrayList<Vertex> allVertices = new ArrayList<>();

    // Stores the set of clusters
    Set<Set<String>> clusterSet = new HashSet<>();


    // Private method to validate the input
    private boolean validateInput(String source, String destination, int weight) {

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
        if (weight <= 0) {
            return false;
        }

        // All parameters are valid
        return true;
    }

    // Method to add edges to allEdges arraylist
    public boolean addEdge(String source, String destination, int weight) {

        // Calling validateInput method for Input validation
        boolean validate = validateInput(source, destination, weight);

        // Parameters valid
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
        for (Edge allEdge : allEdges) {

            // Getting the source vertex of the current edge
            String source = allEdge.source;

            // Getting the destination vertex of the current edge
            String destination = allEdge.destination;

            // Comparing allVertices stream to the source vertex
            boolean vertexExists1 = allVertices.stream().anyMatch(vertexes ->
                    (vertexes.name.equals(source)));

            // If vertex does not exist
            if (!vertexExists1) {

                // Create a new vertex
                Vertex vertex = new Vertex(source, 1, ++numCluster);

                // Add to total vertices
                allVertices.add(vertex);
            }

            // Comparing allVertices stream to the destination vertex
            boolean vertexExists2 = allVertices.stream().anyMatch(vertexes ->
                    (vertexes.name.equals(destination)));

            // If vertex does not exist
            if (!vertexExists2) {

                // Create a new vertex
                Vertex vertex = new Vertex(destination, 1, ++numCluster);

                // Add to total vertices
                allVertices.add(vertex);
            }
        }
    }

    // Private method to create separate clusters
    private static void createSeparateClusters(int startCluster,  HashMap<Integer, Integer> cWMap,
                                                HashMap<Integer, ArrayList<Integer>> cVMap, int v2Index) {
        cWMap.put(startCluster, 1);
        ArrayList<Integer> vIndices = new ArrayList<>();
        vIndices.add(v2Index);
        cVMap.put(startCluster, vIndices);
    }

    // Method for clustering the vertices using the passed tolerance
    public Set<Set<String>> clusterVertices(float tolerance) {

        // Calling the method to add vertices from allEdges arraylist
        // Making the program dynamic
        addVertex();

        // Sorting allVertices using Comparable
        Collections.sort(allVertices);

        // Tolerance cannot be negative
        // Allowing tolerance to be 0
        if (tolerance < 0) {
            return null;
        }

        try {
            // HashMap with key as vertexes cluster & value as the heaviest edge used to create the cluster
            // So this is a vertex cluster & weight map
            HashMap<Integer, Integer> cWMap = new HashMap<>();

            // HashMap with key as vertexes cluster & value as arraylist of all vertex indices
            // So this is a vertex cluster & vertex indices arraylist
            HashMap<Integer, ArrayList<Integer>> cVMap = new HashMap<>();

            // Stores the set of clusters
            Set<Set<String>> cluster1Set = new HashSet<>();

            // Variables to hold the index of the vertices of the current edge
            int v1Index = 0;
            int v2Index = 0;

            // Iterate through allEdges arraylist
            for (Edge edge : allEdges) {

                // Looping through allVertices arraylist to get index of vertices
                for (int i = 0; i < allVertices.size(); i++) {
                    if (allVertices.get(i).name.equals(edge.source)) {
                        v1Index = i;
                    }
                    if (allVertices.get(i).name.equals(edge.destination)) {
                        v2Index = i;
                    }
                }

                // Fetching the respective vertex object for the above vertex indices
                Vertex v1 = allVertices.get(v1Index);
                Vertex v2 = allVertices.get(v2Index);

                // Comparing the cluster of both vertices
                // Not equal then merge
                if (v1.cluster != v2.cluster) {

                    // Using getOrDefault to get the weights of both vertex clusters
                    // In case the key is not mapped in cWMap it passes 1 as default
                    int v1Weight = cWMap.getOrDefault(v1.cluster, 1);
                    int v2Weight = cWMap.getOrDefault(v2.cluster, 1);

                    // Calculating the tolerance
                    float calcTolerance = (float) edge.weight / Math.min(v1Weight, v2Weight);

                    // Calculated tolerance <= tolerance than merge the clusters
                    if (calcTolerance <= tolerance) {

                        // Storing max weight used in forming the cluster
                        int maxWeight = Math.max(edge.weight, Math.max(v1Weight, v2Weight));

                        // Mapping the v1 cluster & max weight in cWMap
                        cWMap.put(v1.cluster, maxWeight);

                        // Merging the v2 cluster in v1 cluster
                        // Checking whether v1 cluster is mapped in cVMap
                        if (cVMap.containsKey(v1.cluster)) {

                            // Checking whether v2 cluster is mapped in cVMap
                            if (cVMap.containsKey(v2.cluster)) {

                                // Iterating through cVMap using v2 cluster
                                for (Integer vIndex : cVMap.get(v2.cluster)) {
                                    allVertices.get(vIndex).cluster = v1.cluster;

                                    //  Creating a new arraylist to store everything in v1 cluster
                                    ArrayList<Integer> cVIndices = cVMap.get(v1.cluster);

                                    // Adding to the arraylist
                                    cVIndices.add(vIndex);

                                    // Mapping the v1 cluster & arraylist to cVMap
                                    cVMap.put(v1.cluster, cVIndices);
                                }
                                // Clearing the v2 cluster mapping in cVMap as it merged with v1 cluster
                                cVMap.get(v2.cluster).clear();
                            } else {

                                // Assigning cluster values to merge into one cluster
                                v2.cluster = v1.cluster;

                                //  Creating a new arraylist to store everything in v1 cluster
                                ArrayList<Integer> cVIndices = cVMap.get(v1.cluster);

                                // Adding to the arraylist
                                cVIndices.add(v2Index);

                                // Mapping the v1 cluster & arraylist to cVMap
                                cVMap.put(v1.cluster, cVIndices);
                            }
                        } else {
                            if (cVMap.containsKey(v2.cluster)) {

                                // Creating an arraylist to store merged clusters
                                ArrayList<Integer> cVIndices1 = new ArrayList<>();

                                // Creating an arraylist to store the v2 cluster
                                ArrayList<Integer> cVIndices = cVMap.get(v2.cluster);

                                // Adding v1 cluster to cVIndices1 arraylist
                                cVIndices1.add(v1Index);

                                // Iterating through v2 cluster & adding it to cVIndices1 arraylist
                                for (Integer vIndex : cVIndices) {
                                    allVertices.get(vIndex).cluster = v1.cluster;
                                    cVIndices1.add(vIndex);
                                }
                                // Mapping v1 cluster & cVIndices1 arraylist to cVMap
                                cVMap.put(v1.cluster, cVIndices1);

                                // Clearing the v2 cluster mapping in cVMap as it merged with v1 cluster
                                cVMap.get(v2.cluster).clear();
                            } else {
                                // Assigning cluster values to merge into one cluster
                                v2.cluster = v1.cluster;

                                // Creating a new arraylist to store both v1 & v2 cluster
                                ArrayList<Integer> cVIndices = new ArrayList<>();

                                // Adding to the arraylist
                                cVIndices.add(v1Index);
                                cVIndices.add(v2Index);

                                // Mapping the v1 cluster & arraylist to cVMap
                                cVMap.put(v1.cluster, cVIndices);
                            }
                        }
                    } else {

                        // Checking whether v1 cluster is mapped in cWMap
                        if (!cWMap.containsKey(v1.cluster)) {

                            // Calling private method for creating separate clusters with v1 cluster & its index
                            createSeparateClusters(v1.cluster, cWMap, cVMap, v1Index);
                        }
                        // Checking whether v2 cluster is mapped in cWMap
                        if (!cWMap.containsKey(v2.cluster)) {

                            // Calling private method for creating separate clusters with v2 cluster & its index
                            createSeparateClusters(v2.cluster, cWMap, cVMap, v2Index);
                        }
                    }
                }
            }

            // Using inline comparator to sort the allVertices arraylist
            // Based on cluster value after performing clustering operation
            allVertices.sort(Comparator.comparingInt(o -> o.cluster));

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
                    cluster1Set.add(cluster);
                    c = vertex.cluster;
                    cluster = new HashSet<>();
                    cluster.add(vertex.name);
                }
            }
            // Adding cluster set to the main 2d clusterSet
            cluster1Set.add(cluster);

            // Returning the final 2d clusterSet containing the clustered graph components
            return cluster1Set;
        } catch (Exception e) {
            return null;
        }
    }

    // Logic to print allEdges arraylist forming the graph
    private void printGraph(ArrayList<Edge> edgeList) {
        for (int i = 0; i < edgeList.size(); i++) {
            Edge edge = edgeList.get(i);
            System.out.println("Edge-" + i + " source: " + edge.source +
                    " destination: " + edge.destination +
                    " weight: " + edge.weight);
        }
    }

    // Logic to print allVertices arraylist in the graph
    private void printClusters(ArrayList<Vertex> clusterList) {
        for (int i = 0; i < clusterList.size(); i++) {
            Vertex vertex = clusterList.get(i);
            System.out.println("Vertex-" + i + " name: " + vertex.name +
                    " weight: " + vertex.weight +
                    " cluster: " + vertex.cluster);
        }
    }

    // Driver method
    public static void main(String[] args) {

        // Pre-defined tolerance for performing clustering
        float tolerance = 3f;

        // Instantiating the graph class
        VertexCluster graph = new VertexCluster();

        // Normal
        graph.addEdge("A", "B", 3);
        graph.addEdge("A", "H", 1);
        graph.addEdge("I", "H", 1);
        graph.addEdge("I", "B", 4);
        graph.addEdge("B", "C", 7);
        graph.addEdge("I", "C", 8);
        graph.addEdge("G", "H", 7);
        graph.addEdge("H", "J", 10);
        graph.addEdge("D", "I", 12);
        graph.addEdge("D", "C", 14);
        graph.addEdge("D", "E", 8);
        graph.addEdge("J", "F", 3);
        graph.addEdge("E", "F", 2);
        graph.addEdge("F", "G", 7);
        graph.addEdge("J", "D", 1);


        // Testing combination1
/*        graph.addEdge("z", "A", 0);
        graph.addEdge(null, null, -1);
        graph.addEdge(null, null, 0);*/

        // Testing combination2
/*        graph.addEdge("Apple.txt", "Ball camp.doc", 3);
        graph.addEdge("Allotment_sequence.ppt", "Higher.pdf", 1);
        graph.addEdge("India-country.com", "Hamilton", 1);
        graph.addEdge("Indie.aspx", "Bahamas_And_All.pptx", 4);
        graph.addEdge("Bam can.txt", "Coding   .com", 7);
        graph.addEdge("   Igloo .txt", "Camping.txt      ", 8);
        graph.addEdge("Grammy.txt", "        Hat.pdf     ", 7);*/

        // Testing combination3
/*        graph.addEdge("x", "y", 0);
        graph.addEdge("A", "B", 3);
        graph.addEdge("B", "C", 2);
        graph.addEdge("C", "D", 7);
        graph.addEdge("D", "E", 2);
        graph.addEdge("E", "A", 5);
        graph.addEdge("J", "F", 5);
        graph.addEdge("F", "D", 2);
        graph.addEdge("H", "G", 2);
        graph.addEdge("F", "C", 4);
        graph.addEdge("D", "A", 3);
        graph.addEdge("I", "H", 3);
        graph.addEdge("J", "I", 6);
        graph.addEdge("G", "E", 6);
        graph.addEdge("G", "J", 5);*/


        // Sorting allEdges using Comparable
        Collections.sort(graph.allEdges);

        // Printing
/*        graph.printGraph(graph.allEdges);
        System.out.println("----------------------");
        graph.printClusters(graph.allVertices);*/

        // Calling the clusterVertices method
        graph.clusterSet = graph.clusterVertices(tolerance);

        // Converting to String & printing
        System.out.println("The clustered vertex set are:");

        try {
            System.out.println(graph.clusterSet.toString());
        } catch (Exception e) {
            System.out.println("Exception faced in printing the Set");
        }
    }
}
