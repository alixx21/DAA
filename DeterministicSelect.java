import java.util.Arrays;

public class DeterministicSelect {

    public static int select(int[] arr, int k) {
        if (k < 0 || k >= arr.length) throw new IllegalArgumentException();
        return helper(arr, 0, arr.length - 1, k);
    }

    private static int helper(int[] arr, int l, int r, int k) {
        while (true) {
            if (l == r) return arr[l];
            int pivot = mom(arr, l, r);
            int p = partition(arr, l, r, pivot);
            if (k == p) return arr[k];
            if (k < p) r = p - 1;
            else l = p + 1;
        }
    }

    private static int mom(int[] arr, int l, int r) {
        int n = r - l + 1;
        if (n <= 5) {
            Arrays.sort(arr, l, r + 1);
            return arr[l + n / 2];
        }
        int m = (int) Math.ceil((double) n / 5);
        int[] med = new int[m];
        for (int i = 0; i < m; i++) {
            int s = l + i * 5;
            int e = Math.min(s + 4, r);
            Arrays.sort(arr, s, e + 1);
            med[i] = arr[s + (e - s) / 2];
        }
        return mom(med, 0, m - 1);
    }

    private static int partition(int[] arr, int l, int r, int pivot) {
        int pi = l;
        for (int i = l; i <= r; i++) if (arr[i] == pivot) { pi = i; break; }
        swap(arr, pi, r);
        int s = l;
        for (int i = l; i < r; i++) if (arr[i] < pivot) swap(arr, i, s++);
        swap(arr, s, r);
        return s;
    }

    private static void swap(int[] arr, int i, int j) {
        int t = arr[i]; arr[i] = arr[j]; arr[j] = t;
    }

    public static void main(String[] args) {
        int[] arr = {9, 2, 7, 1, 6, 8, 3, 5, 4};
        int k = 4;
        System.out.println("k-й элемент = " + select(arr, k));
    }
}
