package CSCI3901;

public class Fibo {

    public int fiboRecursion(int n, int depth, int max) {

        depth++;
        if (depth > max) {
            throw new MaximumRecursionDepth("Maximum recursion depth exceeded", depth);
        } else {
            if (n == 0) {
                return 0;
            }
            if (n == 1) {
                return 1;
            }
            return fiboRecursion(n - 2, depth, max) + fiboRecursion(n - 1, depth, max);
        }
    }

    public static void main(String[] args) {
        int num = 12;
        Fibo f = new Fibo();
        try {
            System.out.println("The Fibonacci Number of " + num + " is " + f.fiboRecursion(num, 0, 10));
        } catch (MaximumRecursionDepth m) {
            System.out.println(m.getMessage());
            System.out.println("The depth of Fibonacci Recursion is " + m.getDepth());
        }

    }
}
