package CSCI;

// Code referenced from https://www.geeksforgeeks.org/java-program-for-binary-search-recursive-and-iterative/

// Java implementation of iterative Binary Search
class BinarySearch {
    // Returns index of x if it is present in arr[], else
    // return -1
    private int binarySearch(int[] arr, int x) {
        int l = 0, r = arr.length - 1;
        // To check whether the array is sorted before loop
        assert isArraySorted(arr) : "Array is not sorted";
        while (l <= r) {
            int m = l + (r - l) / 2;

            // Check if leftmost index l is less than equal to middle index m
            assert l <= m;
            // Check if middle index m is less than equal to rightmost index r
            assert m <= r;
            // Check if x is present at mid
            if (arr[m] == x)
                return m;

            // If x greater, ignore left half
            if (arr[m] < x)
                l = m + 1;

                // If x is smaller, ignore right half
            else
                r = m - 1;
        }
        // To check whether the array is sorted after loop
        assert isArraySorted(arr) : "Array is not sorted";
        // if we reach here, then element was not present
        return -1;
    }

    // Checking whether array is sorted
    private boolean isArraySorted(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1]) {
                return false;
            }
        }
        return true;
    }

    // Driver method to test above
    public static void main(String[] args) {
        BinarySearch ob = new BinarySearch();
        int[] arr = {1, 2, 3, 4, 10, 20, 34, 39, 40, 50, 55, 60, 71};
        int x = 10;
        int result = ob.binarySearch(arr, x);
        if (result == -1) {
            System.out.println("Element not present");
        } else {
            System.out.println("Element found at index " + result);
        }
    }
}
