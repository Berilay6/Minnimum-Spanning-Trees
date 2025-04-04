# Minnimum-Spanning-Trees

This program constructs a Minimum Spanning Tree (MST) using **Prim's Algorithm** and allows various operations on it.
The tree is stored in a **multiway tree format** and can be updated through different commands.

---

## 1️⃣ How to Compile and Run:

To compile the program, use:
javac MSTProgram.java

To run the program with an input file:
java MSTProgram path/to/input/file

Make sure you provide a correct path for the input file.

---

## 2️⃣ Known Bugs and Limitations:

- The program does not handle graphs with disconnected components properly. If the input graph is disconnected, the MST will only cover the connected part of the first vertex.
- The program does not detect duplicate edges in the input file as we assumed.
- The program is designed only for **undirected graphs**, meaning that edges in the adjacency list are bidirectional.
- Large graphs with many edges might slow down execution due to the complexity of Prim’s Algorithm.
- The program does not indicate the causes of invalid operations, it just prints `"Invalid Operation"`.
- If an invalid command is given in the input file, the program just prints `"Invalid Command"` and ignores it.
- We assume that the input format is correct; therefore, there are **no input format error handlers**.
- Due to precision issues of floating-point numbers in Java, very small weight changes (e.g., `"decrease-weight u v 0.000000001"`) might not be detected properly.

---

## 3️⃣ File Directory:

This project consists of multiple Java source files, which are organized as follows:

📂 **Project Root Directory**  
│── **MultiwayTreeNode.java** → Implements a node of a multiway tree structure for MST representation.  
│ - Each node contains:  
│ - Vertex ID (`id`),  
│ - Parent node (`parent`),  
│ - First child (`firstChild`),  
│ - Next sibling (`nextSibling`),  
│ - Previous sibling (`prevSibling`).  
│ - Contains a method `addChild()`, which adds a new child to the current node.  
│  
│── **MinHeap.java** → Implements a **priority queue (min-heap)** to efficiently extract the minimum key value for **Prim’s Algorithm**.  
│ - Maintains a **hash map (`trackMap`)** for tracking the indices of nodes in the heap for fast access.  
│ - Key methods:  
│ - `decreaseKey()`, `swap()`, `insert()`, `extractMin()`, `minHeapify()`.  
│  
│── **MSTProgram.java** → The **main class** that initializes the program, reads input from the file, constructs the graph, runs Prim’s MST algorithm, and processes commands.  
│ - Contains:  
│ - **`Edge` Class** → Represents graph edges (`srcV`, `destV`, `weight`).  
│ - **`Vertex` Class** → Represents graph vertices with adjacency lists (stored as a `TreeSet` for alphabetical order).  
│ - **Methods**:  
│ - `PrimMST()` → Runs Prim’s Algorithm to construct the MST.  
│ - `evert()`, `cut()`, `link()` → Helper methods for MST operations.  
│ - `printMST()` → Prints the MST in multiway tree format.  
│ - `path()` → Finds and prints the path between two vertices.  
│ - `insertEdge()`, `decreaseWeight()` → Updates the MST when edges are modified.  
│ - `updateMST()` → Helper function to update the MST after changes.  
│ - `getEdgeWeight()` → Helper function to retrieve the weight of a specific edge.

---
