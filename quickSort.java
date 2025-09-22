import java.util.concurrent.ThreadLocalRandom;

public final class quickSort {
    private static final int INSERTION_CUTOFF = 16;

    public static final class Stats {
        public long comparisons = 0;
        public long swaps = 0;
        public int maxDepth = 0;
        void noteDepth(int d) { if (d > maxDepth) maxDepth = d; }
    }

    public static Stats sort(int[] a) {
        Stats s = new Stats();
        quicksort(a, 0, a.length - 1, 0, s);
        return s;
    }

    private static void quicksort(int[] a, int lo, int hi, int depth, Stats s) {
        while (lo < hi) {
            if (hi - lo + 1 <= INSERTION_CUTOFF) {
                insertion(a, lo, hi, s);
                return;
            }

            s.noteDepth(depth);

            int p = ThreadLocalRandom.current().nextInt(lo, hi + 1);
            swap(a, lo, p, s);

            int m = partitionHoare(a, lo, hi, s);

            int leftSize = m - lo + 1;
            int rightSize = hi - (m + 1) + 1;

            if (leftSize < rightSize) {
                quicksort(a, lo, m, depth + 1, s);
                lo = m + 1;
            } else {
                quicksort(a, m + 1, hi, depth + 1, s);
                hi = m;
            }
        }
    }

    private static int partitionHoare(int[] a, int lo, int hi, Stats s) {
        int pivot = a[lo];
        int i = lo - 1;
        int j = hi + 1;
        while (true) {
            do { i++; s.comparisons++; } while (a[i] < pivot);
            do { j--; s.comparisons++; } while (a[j] > pivot);
            if (i >= j) return j;
            swap(a, i, j, s);
        }
    }

    private static void insertion(int[] a, int lo, int hi, Stats s) {
        for (int i = lo + 1; i <= hi; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= lo) {
                s.comparisons++;
                if (a[j] <= key) break;
                a[j + 1] = a[j];
                j--;
                s.swaps++;
            }
            a[j + 1] = key;
        }
    }

    private static void swap(int[] a, int i, int j, Stats s) {
        if (i == j) return;
        int t = a[i]; a[i] = a[j]; a[j] = t;
        s.swaps++;
    }

    public static void main(String[] args) {
        int n = 20;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = ThreadLocalRandom.current().nextInt(0, 100);

        System.out.println("Before:");
        print(arr);

        Stats stats = quickSort.sort(arr);

        System.out.println("After:");
        print(arr);

        System.out.println("Comparisons: " + stats.comparisons);
        System.out.println("Swaps/Shifts: " + stats.swaps);
        System.out.println("Max recursion depth: " + stats.maxDepth);
    }

    private static void print(int[] a) {
        StringBuilder sb = new StringBuilder();
        for (int v : a) sb.append(v).append(' ');
        System.out.println(sb.toString().trim());
    }
}
