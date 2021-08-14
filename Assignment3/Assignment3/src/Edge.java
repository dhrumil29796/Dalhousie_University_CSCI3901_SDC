// Edge class to create a new edge
public class Edge implements Comparable<Edge> {
    String source;
    String destination;
    int weight;

    // Parameterized constructor to create a new Edge
    public Edge(String source, String destination, int weight) {
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