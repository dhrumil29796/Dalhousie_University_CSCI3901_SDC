package CSCI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class UWGraph {

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

    static class Vertex {
        String name;
        int weight;
        int cluster;

        public Vertex(String name, int weight, int cluster) {
            this.name = name;
            this.weight = weight;
            this.cluster = cluster;
        }
    }

    // Graph class to create and find the MST
    static class Graph {
        int vertices;
        int numCluster = 0;

        // Tracks the number of components in the union find
        private int numComponents;

        ArrayList<Edge> allEdges = new ArrayList<>();
        ArrayList<Vertex> allVertices = new ArrayList<>();
        HashMap<String, Integer> mapping = new HashMap<>();

        // Used to track the size of each of the component
        int[] sz;

        // parent[i] points to the parent of i, if parent[i] = i then i is a root node
        int[] parent;

        Graph(int vertices) {
            if (vertices > 0) {
                this.vertices = vertices;

                numComponents = vertices;
                sz = new int[vertices];
                parent = new int[vertices];

                for (int i = 0; i < vertices; i++) {

                    // Link to itself (self root)
                    parent[i] = i;

                    // Each component is originally of size 1
                    sz[i] = 1;
                }
            }

        }

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

        public boolean addEdge(String source, String destination, int weight) {

            // Calling validateInput method for Input validation
            boolean validate = validateInput(source, destination, weight);

            if (validate) {
                Edge edge = new Edge(source.trim(), destination.trim(), weight);

                // Current edge's source >  destination lexicographically
                // Swapping the current edge's source & destination
                if (edge.source.compareTo(edge.destination) > 0) {
                    String tmp = edge.source;
                    edge.source = edge.destination;
                    edge.destination = tmp;
                }
                // add to total edges
                allEdges.add(edge);

                return true;
            } else {
                return false;
            }
        }

        public void addVertex(String name) {
            Vertex vertex = new Vertex(name, 1, ++numCluster);

            // add to total vertices
            allVertices.add(vertex);
        }

        public void createMapping(int[] values) {
            for (int i = 0; i < values.length; i++) {
                mapping.put(allVertices.get(i).name, values[i]);
            }
        }

        // Find which component/set 'p' belongs to, takes amortized constant time.
        public int find(int p) {

            // Find the root of the component/set
            int root = p;
            while (root != parent[root]) {
                root = parent[root];
            }

            // Compress the path leading back to the root.
            // Doing this operation is called "path compression"
            // and is what gives us amortized time complexity.
            while (p != root) {
                int next = parent[p];
                parent[p] = root;
                p = next;
            }

            return root;
        }

/*        // Sorting using Comparator
        public static Comparator<Edge> edgeWeight = new Comparator<Edge>() {
            public int compare(Edge e1, Edge e2) {

                int edge1 = e1.weight;
                int edge2 = e2.weight;

                // For ascending order
                return edge1 - edge2;
            }
        };*/

        // Unify the components/sets containing elements 'p' and 'q'
        public void unify(int p, int q) {

            int root1 = find(p);
            int root2 = find(q);

            if (root1 != root2) {
                // Merge smaller component/set into the larger one.
                if (sz[root1] < sz[root2]) {
                    sz[root2] += sz[root1];
                    parent[root1] = root2;
                } else {
                    sz[root1] += sz[root2];
                    parent[root2] = root1;
                }
                // Since the roots found are different we know that the
                // number of components/sets has decreased by one
                numComponents--;
            }
        }

        public Set<Set<String>> clusterVertices(float tolerance) {

            // Tolerance cannot be negative
            if (tolerance < 0) {
                return null;
            }
            for (Edge allEdge : allEdges) {
                int s = mapping.get(allEdge.source);
                int d = mapping.get(allEdge.destination);

                // Remaining to write the logic for tolerance

                unify(s, d);
            }
            return null;
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

        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addVertex("F");
        graph.addVertex("G");
        graph.addVertex("H");
        graph.addVertex("I");
        graph.addVertex("J");

        // Called to create a HashMap to map the vertices to parent indices
        graph.createMapping(graph.parent);

        System.out.println(Collections.singletonList(graph.mapping));

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

        System.out.println("No.of edges: " + graph.allEdges.size() + "\n------------------");

        // Sorting using Comparable
        Collections.sort(graph.allEdges);

        // Sorting using Comparator
        //Collections.sort(graph.allEdges, graph.edgeWeight);

        // Printing the parent[] before operations
        for (int j = 0; j < graph.parent.length; j++) {
            System.out.print(graph.parent[j]);
        }
        System.out.println("\n--------------------");

        graph.clusterVertices(tolerance);

        // Printing the parent[] after operations
        for (int j = 0; j < graph.parent.length; j++) {
            System.out.print(graph.parent[j]);
        }

        // Printing the no.of components
        System.out.println("\n--------------------");
        System.out.println(graph.numComponents);

        graph.printGraph(graph.allEdges);
        System.out.println("----------------------");
        graph.printClusters(graph.allVertices);

    }
}
