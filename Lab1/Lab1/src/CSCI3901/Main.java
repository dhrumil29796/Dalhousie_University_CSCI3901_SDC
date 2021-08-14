package CSCI3901;

public class Main {
    // Driver method to test Map class
    public static void main(String[] args)
    {
        Map<String, Integer>map = new Map<>();
        // Checking whether the size returns 0 for new HashMaps
        System.out.println(map.size());

        // Checking whether size doesn't decrement below 0
        System.out.println(map.remove("Hello"));

        // Checking whether a new HashMap returns 'true' for isEmpty function
        System.out.println(map.isEmpty());

        map.put("dhrumil",10 );

        // Checking whether adding an element makes isEmpty return 'false'
        System.out.println(map.isEmpty());

        map.put("coder",20 );
        map.put("this",40 );
        map.put("hi",50 );
        map.put("null",100);
        System.out.println(map.size());
        System.out.println(map.remove("this"));
        System.out.println(map.remove("dhrumil"));

        // Checking whether it gives null of the given key is already removed
        System.out.println(map.get("dhrumil"));

        System.out.println(map.get("hi"));
        System.out.println(map.get("null"));
        System.out.println(map.size());
        System.out.println(map.isEmpty());

        // Checking whether null in key is handled with a NullPointerException
        map.remove(null);

        // Checking whether null in Key is handled with a NullPointerException
        map.put(null,54);

    }
}
