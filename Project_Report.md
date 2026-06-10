# SELECTION SORT VS QUICK SORT WEBPAGE

*An Interactive Algorithmic Performance Visualizer*

---

<!-- PAGE BREAK -->

# PROJECT TEAM MEMBERS

* ### **Medavarapu Saathvik** — Roll No: 2510030150
* ### **Akshith Kumar** — Roll No: 2510030048

---

<!-- PAGE BREAK -->

## 1. Problem Statement

In computer science education, understanding the dynamic execution and efficiency profiles of sorting algorithms is often hindered by abstract mathematical models and static code listings. While students are taught that Selection Sort is $O(N^2)$ and Quick Sort is $O(N \log N)$, they rarely get to see how these algorithms perform in real-time on the same data. 

Additionally, standard textbook comparisons ignore hardware-level constraints—such as the fact that write operations (swaps) are physically damaging to EEPROM/Flash memory, meaning a "slower" algorithm like Selection Sort can sometimes be preferred over a "faster" one like Quick Sort due to its low swap count. 

There is a lack of simple, offline-capable interactive platforms that combine backend execution metrics (actual CPU time, actual comparisons, actual swaps) with a side-by-side synchronized step-by-step visual animation. This project addresses this gap by building a serverless, hybrid visual comparison visualizer.

---

## 2. Methodology

The core methodology of this project lies in a hybrid, serverless execution and visualization framework. Rather than simulating the sorting process dynamically inside JavaScript (which is prone to runtime lags and does not represent native compiled speed), the sorting process is executed on a native Java JVM. Java acts as the backend engine, running the actual algorithms and recording their metrics, while a static web interface acts as a synchronized playback dashboard.

### A. Core Implementation Details & Bridge Architecture
To enable offline-compatible execution without web servers, we designed a file-based data bridge. The project consists of the following components:

1. **Java Core Engine (`SortComparison.java`)**: 
   * Takes user input elements from the console using the `Scanner` class.
   * Implements native Selection Sort and Quick Sort algorithms using basic loops and division pivots.
   * Instantiates tracker classes that count the exact comparison steps and swap operations.
   * Uses a custom class, `SortStep`, to capture the snapshot state of the array at each operation.
   * Saves these operation steps and time metrics directly to a Javascript file, `data.js`, in the root folder.
2. **The Web Visualizer Interface**:
   * `index.html`: Structures the visual cards, color legend, performance table, and Java source codes.
   * `style.css`: Modern, highly clean dark-theme layout using CSS variables, grids, and glassmorphic cards.
   * `script.js`: Reads the `data.js` snapshots and handles the playback. It runs a synchronized timeout loop at a fixed slow speed (800ms) to animate the bars side-by-side, displaying descriptive text of each swap/comparison.

---

### B. Algorithmic Internals & Real-World Jobs

#### 1. Selection Sort
* **Code Location**: `SSort` class in `SortComparison.java`.
* **Execution Job**: Divides the array into a sorted and unsorted boundary. It searches the unsorted part to find the minimum element, and swaps it with the first element of the unsorted boundary, expanding the sorted section by one. It runs in $O(N^2)$ time complexity but performs at most $O(N)$ swaps.
* **Real-World Job**: Utilized in embedded firmware and flash memory hardware where write-cycles are slow and damage the physical storage cells. It optimizes hardware lifespan by reducing swaps.

#### 2. Quick Sort
* **Code Location**: `QSort` class in `SortComparison.java`.
* **Execution Job**: A divide-and-conquer partition algorithm. It selects a pivot, moves all elements smaller than the pivot to the left and larger to the right, and recursively partitions the left and right sub-sections. It runs in $O(N \log N)$ average-case speed and operates in-place.
* **Real-World Job**: Added in operating system kernels, databases, and standard programming libraries (like Java's `Arrays.sort()`) to handle massive data files at CPU clock speed.

---

## 3. Experiments and Results

To evaluate the operational performance and verify the visualizer, we conducted an experiment using a custom input array.

### A. Experimental Setup
* **Input Array Size**: 10
* **Input Elements**: `[25, 40, 10, 55, 30, 5, 20, 15, 35, 50]`
* **Platform**: Java Runtime Environment (JVM) executing the CLI compiler, visual playback conducted on a standard web browser at a step interval of 800ms.

---

### B. Performance Metrics Captured

| Metric | Selection Sort | Quick Sort |
| :--- | :--- | :--- |
| **Array Before Sorting** | `[25, 40, 10, 55, 30, 5, 20, 15, 35, 50]` | `[25, 40, 10, 55, 30, 5, 20, 15, 35, 50]` |
| **Array After Sorting** | `[5, 10, 15, 20, 25, 30, 35, 40, 50, 55]` | `[5, 10, 15, 20, 25, 30, 35, 40, 50, 55]` |
| **Total Comparisons** | 45 | 36 |
| **Total Swaps** | 7 | 10 |
| **Execution Time** | 362.29 µs (362,292 ns) | 110.42 µs (110,416 ns) |

---

### C. Analysis and Discussion

1. **Comparisons**: 
   Selection Sort performed exactly 45 comparisons. This matches the mathematical formula $\frac{N(N-1)}{2} = \frac{10 \times 9}{2} = 45$. This confirms that Selection Sort always scans the remaining array elements regardless of their order. Quick Sort performed only 36 comparisons because its divide-and-conquer approach avoids redundant comparisons.
2. **Swaps**: 
   Selection Sort performed only 7 swaps (less than $N$), showing its high write efficiency. Quick Sort performed 10 swaps because elements are frequently swapped back and forth during the partitioning phase.
3. **Execution Time**: 
   Quick Sort completed in 110.42 µs, which is approximately **3.2 times faster** than Selection Sort (362.29 µs). This experimental result demonstrates the practical superiority of $O(N \log N)$ complexity over $O(N^2)$ for time-sensitive systems.
4. **Visual Verification**: 
   The frontend successfully parsed the exported steps from `data.js` and showed:
   * **Selection Sort**: Blue bars indicating normal elements, amber highlights indicating comparisons, a pink bar indicating the active minimum element search, and red highlights during a swap.
   * **Quick Sort**: A cyan bar indicating the pivot element, amber highlights during partition comparisons, and red highlights during partition swaps.
   * Once finished, both visual panels turned green to indicate sorted arrays.
