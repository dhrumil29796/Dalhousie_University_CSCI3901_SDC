// Vertex class to create a new vertex
public class Vertex implements Comparable<Vertex> {
    String name;
    int weight;
    int cluster;

    // Parameterized constructor to create a new Vertex
    public Vertex(String name, int weight, int cluster) {
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