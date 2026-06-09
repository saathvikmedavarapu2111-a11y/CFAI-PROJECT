import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Step class to store the array state and indices involved in a sorting step
class SortStep {
    String type; // "compare", "swap", "min", "pivot"
    int index1;
    int index2;
    int[] arrayState;

    SortStep(String type, int index1, int index2, int[] array) {
        this.type = type;
        this.index1 = index1;
        this.index2 = index2;
        this.arrayState = array.clone();
    }
}

// Quick Sort Class using standard syntax with step tracing and boundary checks
class QSort {
    static List<SortStep> steps = new ArrayList<>();
    static int comparisons = 0;
    static int swaps = 0;

    static void QuickSort(int[] A, int LI, int RI) {
        int j;
        if (LI < RI) {
            j = Divide(A, LI, RI);
            QuickSort(A, LI, j - 1);
            QuickSort(A, j + 1, RI);
        }
    }

    static int Divide(int[] A, int LI, int RI) {
        int Pivot, j, i, Temp;
        Pivot = A[LI];
        
        // Record pivot selection step
        steps.add(new SortStep("pivot", LI, -1, A));
        
        i = LI;
        j = RI + 1;
        while (i < j) {
            do {
                i++;
                if (i <= RI) {
                    comparisons++;
                    steps.add(new SortStep("compare", i, LI, A)); // comparing with Pivot at index LI
                }
            } while (i < RI && A[i] < Pivot); // Boundary check added for safety
            
            do {
                j--;
                if (j >= LI) {
                    comparisons++;
                    steps.add(new SortStep("compare", j, LI, A)); // comparing with Pivot at index LI
                }
            } while (j > LI && A[j] > Pivot); // Boundary check added for safety
            
            if (i < j) {
                Temp = A[i];
                A[i] = A[j];
                A[j] = Temp;
                swaps++;
                steps.add(new SortStep("swap", i, j, A));
            }
        }
        Temp = A[LI];
        A[LI] = A[j];
        A[j] = Temp;
        swaps++;
        steps.add(new SortStep("swap", LI, j, A));
        return j;
    }
}

// Selection Sort Class matching the syntax style of Quick Sort
class SSort {
    static List<SortStep> steps = new ArrayList<>();
    static int comparisons = 0;
    static int swaps = 0;

    static void SelectionSort(int[] A) {
        int n = A.length;
        for (int i = 0; i < n - 1; i++) {
            int min_idx = i;
            // Record minimum selection marker step
            steps.add(new SortStep("min", min_idx, -1, A));
            
            for (int j = i + 1; j < n; j++) {
                comparisons++;
                steps.add(new SortStep("compare", j, min_idx, A));
                if (A[j] < A[min_idx]) {
                    min_idx = j;
                    steps.add(new SortStep("min", min_idx, -1, A));
                }
            }
            if (min_idx != i) {
                int temp = A[min_idx];
                A[min_idx] = A[i];
                A[i] = temp;
                swaps++;
                steps.add(new SortStep("swap", i, min_idx, A));
            }
        }
    }
}

// Main class to run comparison and generate data.js
public class SortComparison {
    public static void main(String[] args) {
        Scanner SC = new Scanner(System.in);
        System.out.println("================================================");
        System.out.println("  Selection Sort vs Quick Sort Comparison Tool  ");
        System.out.println("================================================");
        System.out.println("Enter Size of the Array: ");
        int N = SC.nextInt();
        
        System.out.println("Enter " + N + " Integer Elements: ");
        int[] A1 = new int[N];  
        for (int i = 0; i < N; i++) {
            A1[i] = SC.nextInt();
        }

        System.out.println("\nElements of Array Before Sorting: ");
        printArray(A1);

        // Run Selection Sort on a clone of the array
        int[] selectionArray = A1.clone();
        long startSel = System.nanoTime();
        SSort.SelectionSort(selectionArray);
        long timeSel = System.nanoTime() - startSel;

        // Run Quick Sort on a clone of the array
        int[] quickArray = A1.clone();
        long startQuick = System.nanoTime();
        QSort.QuickSort(quickArray, 0, quickArray.length - 1);
        long timeQuick = System.nanoTime() - startQuick;

        System.out.println("\nElements of Array After Selection Sort: ");
        printArray(selectionArray);

        System.out.println("Elements of Array After Quick Sort: ");
        printArray(quickArray);

        // Write the execution steps and metrics directly to data.js
        try {
            FileWriter writer = new FileWriter("data.js");
            writer.write("// Generated automatically by SortComparison.java\n");
            writer.write("const sortingData = {\n");
            
            // Original array
            writer.write("  originalArray: " + arrayToString(A1) + ",\n");
            
            // Selection Sort results
            writer.write("  selection: {\n");
            writer.write("    comparisons: " + SSort.comparisons + ",\n");
            writer.write("    swaps: " + SSort.swaps + ",\n");
            writer.write("    timeNs: " + timeSel + ",\n");
            writer.write("    steps: " + stepsToJson(SSort.steps) + "\n");
            writer.write("  },\n");
            
            // Quick Sort results
            writer.write("  quick: {\n");
            writer.write("    comparisons: " + QSort.comparisons + ",\n");
            writer.write("    swaps: " + QSort.swaps + ",\n");
            writer.write("    timeNs: " + timeQuick + ",\n");
            writer.write("    steps: " + stepsToJson(QSort.steps) + "\n");
            writer.write("  }\n");
            
            writer.write("};\n");
            writer.close();
            
            System.out.println("\n================================================");
            System.out.println("SUCCESS: Sorting data written to 'data.js'!");
            System.out.println("Open 'index.html' in your browser to view the visual comparison.");
            System.out.println("================================================");
            
        } catch (IOException e) {
            System.out.println("Error writing data.js file: " + e.getMessage());
        }
    }

    static void printArray(int[] A) {
        for (int x : A) {
            System.out.print(x + "  ");
        }
        System.out.println();
    }

    static String arrayToString(int[] A) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < A.length; i++) {
            sb.append(A[i]);
            if (i < A.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    static String stepsToJson(List<SortStep> steps) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < steps.size(); i++) {
            SortStep step = steps.get(i);
            sb.append("{");
            sb.append("\"type\":\"").append(step.type).append("\",");
            sb.append("\"index1\":").append(step.index1).append(",");
            sb.append("\"index2\":").append(step.index2).append(",");
            sb.append("\"array\":").append(arrayToString(step.arrayState));
            sb.append("}");
            if (i < steps.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
