import java.util.Arrays;
import java.util.Collections;
public class SortingExperiment {

    // Helper method
    private static void placeInserter(int[] a, int place, int current) {
        int temp = a[current];
        for (int k = current; k > place; k--) {
            a[k] = a[k - 1];
        }
        a[place] = temp;
    }

    private static int binaryLocFinder(int[] a, int start, int end, int key) {
        int loc;
        if (start >= end) {
            if (start == end) {
                loc = (a[start] > key) ? start : start + 1;
            } else {
                loc = start;
            }
        } else {
            int middle = (start + end) / 2;
            if (a[middle] < key) {
                loc = binaryLocFinder(a, middle + 1, end, key);
            } else if (a[middle] > key) {
                loc = binaryLocFinder(a, start, middle - 1, key);
            } else {
                loc = middle;
            }
        }
        return loc;
    }

    public static void clusteredBinaryInsertionSort(int[] a) {
        int POP = 0; // Position pointer initialized to 0
        for (int i = 1; i < a.length; i++) {
            int COP = i; // Current pointer
            int key = a[COP];
            int place;
            if (key >= a[POP]) {
                place = binaryLocFinder(a, POP + 1, COP - 1, key);
            } else {
                place = binaryLocFinder(a, 0, POP - 1, key);
            }
            POP = place; 
            placeInserter(a, place, COP);
        }
    }

    public static void randomizedQuickSort(int[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = low + (int) (Math.random() * (high - low + 1));
            swap(array, pivotIndex, high);

            int pivot = partition(array, low, high);
            randomizedQuickSort(array, low, pivot - 1);
            randomizedQuickSort(array, pivot + 1, high);
        }
    }

    private static int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static Integer[] generateDataset(int size, String type) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }

        switch (type) {
            case "random":
                Collections.shuffle(Arrays.asList(array));
                break;
            case "reversed":
                Collections.reverse(Arrays.asList(array));
                break;
        }
        return array;
    }

   private static int[] toPrimitiveArray(Integer[] array) {
        return Arrays.stream(array).mapToInt(Integer::intValue).toArray();
    }

    // Method to benchmark sorting algorithms
    public static long[] benchmarkSorts(Integer[] dataset) {
        int[] datasetForCBIS = toPrimitiveArray(dataset.clone());
        int[] datasetForRQS = toPrimitiveArray(dataset.clone());

        long startTimeCBIS = System.currentTimeMillis();
        clusteredBinaryInsertionSort(datasetForCBIS);
        long endTimeCBIS = System.currentTimeMillis();

        long startTimeRQS = System.currentTimeMillis();
        randomizedQuickSort(datasetForRQS, 0, datasetForRQS.length - 1);
        long endTimeRQS = System.currentTimeMillis();

        return new long[]{endTimeCBIS - startTimeCBIS, endTimeRQS - startTimeRQS};
    }

    public static void main(String[] args) {
        System.out.println("| Dataset Size | Dataset Type | CBIS Time (ms) | RQS Time (ms) |");
        System.out.println("|--------------|--------------|----------------|---------------|");

        String[] types = {"reversed", "random", "sorted"};
        int[] sizes = {200, 2000, 20000};

        for (String type : types) {
            for (int size : sizes) {
                Integer[] dataset = generateDataset(size, type);
                long[] times = benchmarkSorts(dataset);
                System.out.printf("| %12d | %12s | %14d | %13d |%n", size, type, times[0], times[1]);
            }
        }
    }
}
