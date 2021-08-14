package CSCI3901;

public class MaximumRecursionDepth extends RuntimeException {
    private final int deep;
    private final String message;

    public MaximumRecursionDepth(String s, int deep) {
        this.message = s;
        this.deep = deep;
    }

    public String getMessage() {
        return this.message;
    }

    public int getDepth() {
        return this.deep;
    }
}
