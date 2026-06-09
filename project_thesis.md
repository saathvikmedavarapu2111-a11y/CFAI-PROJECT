# Project Thesis: Selection Sort vs Quick Sort Comparison Visualizer

This document serves as a complete explanatory thesis for this project, outlining the architecture, the algorithmic details of both Selection Sort and Quick Sort, their complexities, real-world applications, and the implementation layout.

---

## 1. Project Overview & Objectives

The goal of this project is to build an interactive, educational tool that compares two fundamental sorting algorithms: **Selection Sort** and **Quick Sort**. 

Rather than relying purely on abstract complexities (like $O(N^2)$ vs $O(N \log N)$), this tool provides:
1. **Side-by-side animated visualization** of the sorting steps on the exact same array elements.
2. **Accurate performance metrics** (execution time, total comparisons, and total swaps) calculated by a compiled backend processor.
3. **Real-world use cases** highlighting why speed is not the only metric that matters in computer science (e.g., memory write limits).

---

## 2. System Architecture

The project is designed with a **Serverless Hybrid Architecture** divided into two components:
1. **Java CLI Backend (`SortComparison.java`)**: Responsible for data input, algorithm execution, performance measurement, and state logging.
2. **Web Frontend (`index.html`, `style.css`, `script.js`)**: Responsible for presenting the dashboard, rendering the data logs as animated visual bars, and comparing complexities.

### The Communication Bridge (`data.js`)
Typically, connecting a Java program to a web browser requires setting up HTTP web servers (like Spring Boot or Tomcat), which adds extensive socket and network complexity. 

To keep the project simple, clean, and offline-compatible, we used a **file-based bridge**:
* The Java program writes its complete execution timeline and metrics to a JavaScript file called `data.js` as a global variable: `const sortingData = { ... };`.
* The HTML page imports `data.js` statically. This completely bypasses CORS security restrictions and port configurations. The user can open `index.html` locally by double-clicking it, and it will visualize the sorting steps.

---

## 3. Algorithm Internals & Code

### A. Selection Sort
Selection Sort divides the array into a sorted section (at the front) and an unsorted section. It repeatedly scans the unsorted section to find the smallest element, and swaps it to the boundary of the sorted section.

#### Java Code (from `SortComparison.java`):
```java
static void SelectionSort(int[] A) {
    int n = A.length;
    for (int i = 0; i < n - 1; i++) {
        int min_idx = i;
        for (int j = i + 1; j < n; j++) {
            if (A[j] < A[min_idx]) {
                min_idx = j;
            }
        }
        if (min_idx != i) {
            int temp = A[min_idx];
            A[min_idx] = A[i];
            A[i] = temp;
        }
    }
}
```

#### Step-by-Step Execution:
1. Initialize `min_idx` at the beginning of the unsorted boundary `i`.
2. Compare each subsequent element `j` with the current minimum at `min_idx`.
3. If an element is smaller, update `min_idx` to `j`.
4. After checking the whole unsorted section, swap the element at `min_idx` with the element at `i`.
5. Move the boundary `i` forward by 1, and repeat.

---

### B. Quick Sort
Quick Sort is a **Divide-and-Conquer** algorithm. It selects a "pivot" element (our code uses the first element `A[LI]` as pivot) and partitions the array such that all elements smaller than the pivot go to its left, and all elements larger go to its right. It then recursively applies the same process to the left and right halves.

#### Java Code (from `SortComparison.java`):
```java
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
    i = LI;
    j = RI + 1;
    while (i < j) {
        do {
            i++;
        } while (i < RI && A[i] < Pivot); // Boundary check added for safety
        
        do {
            j--;
        } while (j > LI && A[j] > Pivot); // Boundary check added for safety
        
        if (i < j) {
            Temp = A[i];
            A[i] = A[j];
            A[j] = Temp;
        }
    }
    Temp = A[LI];
    A[LI] = A[j];
    A[j] = Temp;
    return j;
}
```

#### Step-by-Step Partitioning:
1. Select the pivot `Pivot = A[LI]`.
2. Increment the pointer `i` until an element larger than the pivot is found.
3. Decrement the pointer `j` until an element smaller than the pivot is found.
4. If `i < j`, swap `A[i]` and `A[j]`, then continue searching.
5. If the pointers cross (`i >= j`), the partition is complete. Swap the pivot `A[LI]` with the element at `A[j]` to place the pivot in its final sorted position.
6. Return `j` as the split index.

---

## 4. Complexity & Real-World Use Cases

| Metric / Feature | Selection Sort | Quick Sort |
| :--- | :--- | :--- |
| **Best-Case Time** | $O(N^2)$ | $O(N \log N)$ |
| **Average-Case Time** | $O(N^2)$ | $O(N \log N)$ |
| **Worst-Case Time** | $O(N^2)$ | $O(N^2)$ |
| **Auxiliary Space** | $O(1)$ (Constant) | $O(\log N)$ (Recursive Call Stack) |
| **Stability** | Unstable | Unstable |
| **Maximum Swaps** | $O(N)$ (Minimal writes) | $O(N \log N)$ (Standard writes) |

### Why use Selection Sort?
While Selection Sort is slow for large datasets ($O(N^2)$ average time), it has one major advantage: it performs a **maximum of $O(N)$ write/swap operations**. 
* **Real-World Job**: In embedded systems, IoT sensors, or hardware using flash memory or EEPROM, write operations are slow and physically degrade the memory cells. Selection Sort is selected in these environments for small datasets because it minimizes memory wear-and-tear.

### Why use Quick Sort?
Quick Sort is one of the fastest general-purpose sorting algorithms in practice ($O(N \log N)$ average time). It works in-place and has excellent **CPU cache locality**, meaning it retrieves contiguous memory locations efficiently.
* **Real-World Job**: Used in high-performance library functions (like primitive sorting in Java's standard libraries), operating system kernels, and database query engines to sort large lists at maximum clock speed.

---

## 5. How to Compile & Run

1. Open your terminal in the project directory.
2. Compile the Java file:
   ```bash
   javac SortComparison.java
   ```
3. Run the compiled application:
   ```bash
   java SortComparison
   ```
4. Enter your desired array parameters in the console:
   * It will ask for the size of the array (e.g., `10`).
   * It will ask for the array elements (e.g., `25 40 10 55 30 5 20 15 35 50`).
5. Open **`index.html`** in your browser (just double-click the file).
6. Click **"Start Visualizer"** to see the side-by-side execution trace!
