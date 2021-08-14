package CSCI3901;

// A node of chains
public class HashNode<K, V>
{
    public K key;
    public V value;

    // Referencing the next node
    HashNode<K, V> next;

    // Parameterized Constructor
    public HashNode(K key, V value)
    {
        this.key = key;
        this.value = value;
    }
}
