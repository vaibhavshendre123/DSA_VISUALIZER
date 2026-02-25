# DSA Visualizer Ultra Pro

A comprehensive Java Swing application for visualizing Data Structures and Algorithms with interactive animations.

## Features

### Data Structures
- **Array Sorting** - Bubble, Selection, Insertion, Quick, Merge, Radix Sort
- **Stack** - Push, Pop, Peek operations with undo
- **Queue** - Enqueue, Dequeue with front/rear visualization
- **Circular Queue** - Visual front/rear pointer rotation
- **Linked List** - Insert (head/tail/position), Delete, Search, Reverse
- **Binary Search Tree** - Insert, Delete, Search, BFS/DFS Traversal
- **Heap** - Max Heap with insert, extract, heapify
- **Hash Table** - Chaining collision handling visualization
- **Graph** - BFS/DFS traversal visualization

### Problems Practice (LeetCode-style)
- **16 Pattern Categories** from DSA Cheat Sheet:
  - Two Pointers, Fast & Slow Pointers, Sliding Window
  - Kadane Pattern, Merge Intervals, Cyclic Sort
  - In-place Reversal, Stack, Monotonic Stack
  - Tree BFS/DFS, Graphs, Island
  - Subsets, Bitwise XOR, Top K Elements
  - Greedy, Backtracking, Trie, Union Find
- **Problem Details**: Name, Difficulty, Time Complexity
- **Code Editor**: Write and test solutions
- **Hints**: Pattern-based hints for each problem
- **Solutions**: Reference solutions for each problem
- **Links**: Direct links to LeetCode/GeeksforGeeks

### Interactive Features
- **Random Generator** - Generate random arrays/data structures
- **Step-by-Step** - Manual stepping through algorithms
- **Undo** - Revert last operation
- **Pause/Resume** - Control animation playback
- **Speed Control** - Slow/Medium/Fast presets + slider

### Educational Features
- **Complexity Display** - Shows O(n), O(n²), O(n log n) for each algorithm
- **Pseudocode Panel** - View algorithm code while visualizing
- **Comparison Counter** - Track number of comparisons and swaps

### UI Features
- **Dark/Light Theme Toggle**
- **3D-style Visualizations** - Gradient bars and nodes

## Requirements
- Java 8 or higher
- No external dependencies (uses only Swing/AWT)

## Running

```bash
# Compile
javac src/DSAVisualizerUltraPro.java

# Run
java DSAVisualizerUltraPro
```

Or open in IntelliJ IDEA and run the main method.

## Controls

### Array Sorting Panel
- **Manual Input** - Enter array elements manually
- **Random Array** - Generate random array (10-20 elements)
- **Start** - Begin automatic sorting animation
- **Pause** - Toggle pause/resume
- **Step** - Execute single step
- **Undo** - Revert to previous state
- **Reset** - Restore original array

### Tree/Graph Panels
- **Insert/Add** - Add nodes/values
- **Delete** - Remove nodes
- **Search** - Find specific values
- **Traversal** - BFS/DFS operations

### Problems Panel
- Select a category from the left panel
- Choose a problem from the center list
- View problem details (difficulty, complexity) on the right
- Write your solution in the code editor
- Click **Hint** for pattern-based hints
- Click **Solution** to see reference solution
- Click **Visualize** to see algorithm animation

## Project Structure

```
src/
├── DSAVisualizerUltraPro.java  # Main application (all features)
├── DSAVisualizerPro.java       # Original version
├── UltimateDSAVisualizerUltra.java  # Intermediate version
└── Main.java                   # Placeholder
```

## Screenshots

The application features:
- Tabbed interface for easy navigation
- Real-time algorithm visualization
- Interactive mouse dragging for array elements
- Animated transitions between states

## License

MIT License
