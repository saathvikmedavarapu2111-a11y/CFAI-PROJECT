# Selection Sort vs Quick Sort Webpage

**Course Project Report**

---

### Team Members
* **Medavarapu Saathvik** — Roll No: 2510030150
* **Akshith Kumar** — Roll No: 2510030048

---

## 1. Problem Statement

In computer science education, understanding the dynamic execution and efficiency profiles of sorting algorithms is often hindered by abstract mathematical models and static code listings. While students are taught that Selection Sort is $O(N^2)$ and Quick Sort is $O(N \log N)$, they rarely get to see how these algorithms perform in real-time on the same data. 

Additionally, standard textbook comparisons ignore hardware-level constraints—such as the fact that write operations (swaps) are physically damaging to EEPROM/Flash memory, meaning a "slower" algorithm like Selection Sort can sometimes be preferred over a "faster" one like Quick Sort due to its low swap count. 

There is a lack of simple, offline-capable interactive platforms that combine backend execution metrics (actual CPU time, actual comparisons, actual swaps) with a side-by-side synchronized step-by-step visual animation. This project addresses this gap by building a serverless, hybrid visual comparison visualizer.

---

## 2. Methodology

The core idea of this project is to build an interactive sorting visualizer that executes the sorting logic on a Java backend and renders the step-by-step animations on a web frontend.

### A. System Architecture & Components
To keep the application simple to run and easy to explain, we avoided complex HTTP web servers (like Spring Boot or Servlets) and built a **file-based serverless bridge**:

1. **Java Backend Processor (`SortComparison.java`)**: 
   * Reads the array size and elements from the user via the console using `Scanner`.
   * Clones the array and sorts it using compiled Java implementations of Selection Sort and Quick Sort.
   * Tracks execution times using `System.nanoTime()` and counts the comparisons and swaps.
   * Logs every comparison, swap, pivot selection, and minimum-index change into a state history list.
   * Exports these logs and statistics directly into a JavaScript file called `data.js` as a global variable.
2. **Web Frontend (`index.html`, `style.css`, `script.js`, `about.html`)**:
   * `index.html` loads the generated `data.js` file.
   * `script.js` parses the sorting steps from `data.js` and updates the heights and colors of visual bars representing the array.
   * It plays back the animation frames in synchronization at a fixed slow speed (800ms) to make the visual transition easily trackable.
   * `about.html` details where the components are located, how the bridge works, and the real-world industrial jobs of each algorithm.

---

### B. Algorithm Explanations & Implementation Details

#### 1. Selection Sort
* **Concept**: Divides the array into a sorted and unsorted boundary. It repeatedly scans the unsorted section to find the minimum element, and swaps it with the first element of the unsorted section.
* **Why and Where it is used**: Selection Sort has a worst-case time complexity of $O(N^2)$, but it performs a maximum of only $O(N)$ swaps (at most 1 swap per outer loop). In embedded systems, sensors, and IoT devices with flash memory or EEPROM, writing to memory is slow and degrades the hardware. Selection Sort is used here for small arrays to minimize write cycles.

#### 2. Quick Sort
* **Concept**: A divide-and-conquer algorithm that selects a "pivot" element (the first element of the subarray in our code) and partitions the array such that all elements smaller than the pivot go to its left, and all elements larger go to its right. It then recursively applies the same process to the left and right halves.
* **Why and Where it is used**: Quick Sort has an average-case time complexity of $O(N \log N)$ and is extremely fast. It works in-place (requiring only stack memory for recursion) and has excellent CPU cache locality. It is used in operating system kernels, databases, and programming language standard libraries (like Java's `Arrays.sort()`) to sort large datasets at maximum speed.

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
