import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.*;
import javax.swing.Timer;

public class ProblemsPanel extends JPanel {
    private DSAVisualizerUltraPro mainFrame;
    private Map<String, List<Problem>> problemsByCategory;
    private JList<String> categoryList;
    private JList<String> problemList;
    private Problem selectedProblem;
    private JTextArea problemDescription;
    private JTextArea codeEditor;
    private JLabel difficultyLabel;
    private JLabel complexityLabel;
    
    public ProblemsPanel(DSAVisualizerUltraPro frame) {
        this.mainFrame = frame;
        setBackground(frame.getBgColor());
        problemsByCategory = new LinkedHashMap<>();
        loadProblems();
        
        setLayout(new BorderLayout());
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(frame.getBgColor());
        
        JLabel catLabel = new JLabel("Categories");
        catLabel.setForeground(frame.getFgColor());
        catLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        categoryList = new JList<>(problemsByCategory.keySet().toArray(new String[0]));
        categoryList.setBackground(frame.isDarkTheme() ? new Color(50,50,50) : Color.WHITE);
        categoryList.setForeground(frame.getFgColor());
        categoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showProblemsForCategory(categoryList.getSelectedValue());
            }
        });
        
        JScrollPane catScroll = new JScrollPane(categoryList);
        catScroll.setPreferredSize(new Dimension(200, 0));
        
        leftPanel.add(catLabel, BorderLayout.NORTH);
        leftPanel.add(catScroll, BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(frame.getBgColor());
        
        JLabel probLabel = new JLabel("Problems");
        probLabel.setForeground(frame.getFgColor());
        probLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        problemList = new JList<>();
        problemList.setBackground(frame.isDarkTheme() ? new Color(50,50,50) : Color.WHITE);
        problemList.setForeground(frame.getFgColor());
        problemList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showProblemDetails(problemList.getSelectedValue());
            }
        });
        
        JScrollPane probScroll = new JScrollPane(problemList);
        probScroll.setPreferredSize(new Dimension(250, 0));
        
        centerPanel.add(probLabel, BorderLayout.NORTH);
        centerPanel.add(probScroll, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(frame.getBgColor());
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(frame.getBgColor());
        
        difficultyLabel = new JLabel("Difficulty: -");
        complexityLabel = new JLabel("Complexity: -");
        difficultyLabel.setForeground(frame.getFgColor());
        complexityLabel.setForeground(frame.getFgColor());
        infoPanel.add(difficultyLabel);
        infoPanel.add(complexityLabel);
        
        problemDescription = new JTextArea(8, 40);
        problemDescription.setEditable(false);
        problemDescription.setFont(new Font("Arial", Font.PLAIN, 13));
        problemDescription.setBackground(frame.isDarkTheme() ? new Color(40,40,40) : Color.WHITE);
        problemDescription.setForeground(frame.getFgColor());
        problemDescription.setLineWrap(true);
        problemDescription.setWrapStyleWord(true);
        
        JScrollPane descScroll = new JScrollPane(problemDescription);
        
        JLabel descLabel = new JLabel("Problem Description");
        descLabel.setForeground(frame.getFgColor());
        descLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel codeLabel = new JLabel("Code Editor");
        codeLabel.setForeground(frame.getFgColor());
        codeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        codeEditor = new JTextArea(12, 40);
        codeEditor.setFont(new Font("Monospaced", Font.PLAIN, 13));
        codeEditor.setBackground(frame.isDarkTheme() ? new Color(30,30,30) : Color.WHITE);
        codeEditor.setForeground(frame.getFgColor());
        codeEditor.setText("// Write your solution here\n\n");
        
        JScrollPane codeScroll = new JScrollPane(codeEditor);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(frame.getBgColor());
        JButton runBtn = new JButton("Run Code");
        JButton visualizeBtn = new JButton("Visualize");
        JButton hintBtn = new JButton("Hint");
        JButton solutionBtn = new JButton("Show Solution");
        
        runBtn.addActionListener(e -> runCode());
        visualizeBtn.addActionListener(e -> visualizeProblem());
        hintBtn.addActionListener(e -> showHint());
        solutionBtn.addActionListener(e -> showSolution());
        
        buttonPanel.add(runBtn);
        buttonPanel.add(visualizeBtn);
        buttonPanel.add(hintBtn);
        buttonPanel.add(solutionBtn);
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(frame.getBgColor());
        northPanel.add(infoPanel, BorderLayout.NORTH);
        northPanel.add(descLabel, BorderLayout.SOUTH);
        northPanel.add(descScroll, BorderLayout.CENTER);
        rightPanel.add(northPanel, BorderLayout.NORTH);
        
        JPanel codePanel = new JPanel(new BorderLayout());
        codePanel.add(codeLabel, BorderLayout.NORTH);
        codePanel.add(codeScroll, BorderLayout.CENTER);
        codePanel.add(buttonPanel, BorderLayout.SOUTH);
        rightPanel.add(codePanel, BorderLayout.SOUTH);
        
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
    
    private void loadProblems() {
        problemsByCategory.put("Two Pointers", Arrays.asList(
            new Problem("Pair with Target Sum", "Easy", "O(n)", "https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/"),
            new Problem("Remove Duplicates", "Easy", "O(n)", "https://leetcode.com/problems/remove-duplicates-from-sorted-list/"),
            new Problem("Squaring a Sorted Array", "Easy", "O(n)", "https://leetcode.com/problems/squares-of-a-sorted-array/"),
            new Problem("Triplet Sum to Zero", "Medium", "O(n²)", "https://leetcode.com/problems/3sum/"),
            new Problem("Triplet Sum Close to Target", "Medium", "O(n²)", "https://leetcode.com/problems/3sum-closest/"),
            new Problem("Dutch National Flag", "Medium", "O(n)", "https://leetcode.com/problems/sort-colors/")
        ));
        
        problemsByCategory.put("Fast & Slow Pointers", Arrays.asList(
            new Problem("LinkedList Cycle", "Easy", "O(n)", "https://leetcode.com/problems/linked-list-cycle/"),
            new Problem("Happy Number", "Medium", "O(log n)", "https://leetcode.com/problems/happy-number/"),
            new Problem("Middle of LinkedList", "Easy", "O(n)", "https://leetcode.com/problems/middle-of-the-linked-list/"),
            new Problem("Palindrome LinkedList", "Medium", "O(n)", "https://leetcode.com/problems/palindrome-linked-list/"),
            new Problem("Start of LinkedList Cycle", "Medium", "O(n)", "https://leetcode.com/problems/linked-list-cycle-ii/")
        ));
        
        problemsByCategory.put("Sliding Window", Arrays.asList(
            new Problem("Max Sum Subarray of Size K", "Easy", "O(n)", "https://www.geeksforgeeks.org/problems/max-sum-subarray-of-size-k5313/1"),
            new Problem("Smallest Subarray with Given Sum", "Easy", "O(n)", "https://leetcode.com/problems/minimum-size-subarray-sum/"),
            new Problem("Longest Substring with K Distinct", "Medium", "O(n)", "https://www.geeksforgeeks.org/problems/longest-k-unique-characters-substring0853/1"),
            new Problem("Fruits into Baskets", "Medium", "O(n)", "https://leetcode.com/problems/fruit-into-baskets/"),
            new Problem("No-repeat Substring", "Hard", "O(n)", "https://leetcode.com/problems/longest-substring-without-repeating-characters/")
        ));
        
        problemsByCategory.put("Kadane Pattern", Arrays.asList(
            new Problem("Maximum Subarray", "Easy", "O(n)", "https://leetcode.com/problems/maximum-subarray/"),
            new Problem("Maximum Sum Subarray", "Easy", "O(n)", "https://www.geeksforgeeks.org/problems/kadanes-algorithm-1587115620/1"),
            new Problem("Find Maximum Sum", "Easy", "O(n)", "")
        ));
        
        problemsByCategory.put("Merge Intervals", Arrays.asList(
            new Problem("Merge Intervals", "Medium", "O(n log n)", "https://leetcode.com/problems/merge-intervals/"),
            new Problem("Insert Interval", "Medium", "O(n)", "https://leetcode.com/problems/insert-interval/"),
            new Problem("Interval Intersection", "Medium", "O(n)", "https://leetcode.com/problems/interval-list-intersections/")
        ));
        
        problemsByCategory.put("Cyclic Sort", Arrays.asList(
            new Problem("Find Missing Number", "Easy", "O(n)", "https://leetcode.com/problems/missing-number/"),
            new Problem("Find Duplicate", "Medium", "O(n)", "https://leetcode.com/problems/find-the-duplicate-number/"),
            new Problem("Find All Duplicates", "Medium", "O(n)", "https://leetcode.com/problems/find-all-duplicates-in-an-array/")
        ));
        
        problemsByCategory.put("In-place Reversal", Arrays.asList(
            new Problem("Reverse LinkedList", "Easy", "O(n)", "https://leetcode.com/problems/reverse-linked-list/"),
            new Problem("Reverse LinkedList II", "Medium", "O(n)", "https://leetcode.com/problems/reverse-linked-list-ii/"),
            new Problem("Reverse Between", "Medium", "O(n)", "")
        ));
        
        problemsByCategory.put("Stack", Arrays.asList(
            new Problem("Valid Parentheses", "Easy", "O(n)", "https://leetcode.com/problems/valid-parentheses/"),
            new Problem("Min Stack", "Medium", "O(1)", "https://leetcode.com/problems/min-stack/"),
            new Problem("Decode String", "Medium", "O(n)", "https://leetcode.com/problems/decode-string/")
        ));
        
        problemsByCategory.put("Tree BFS", Arrays.asList(
            new Problem("Level Order Traversal", "Easy", "O(n)", "https://leetcode.com/problems/binary-tree-level-order-traversal/"),
            new Problem("Reverse Level Order", "Easy", "O(n)", ""),
            new Problem("Zigzag Traversal", "Medium", "O(n)", "https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/")
        ));
        
        problemsByCategory.put("Tree DFS", Arrays.asList(
            new Problem("Inorder Traversal", "Easy", "O(n)", "https://leetcode.com/problems/binary-tree-inorder-traversal/"),
            new Problem("Preorder Traversal", "Easy", "O(n)", "https://leetcode.com/problems/binary-tree-preorder-traversal/"),
            new Problem("Postorder Traversal", "Easy", "O(n)", "https://leetcode.com/problems/binary-tree-postorder-traversal/")
        ));
        
        problemsByCategory.put("Graphs", Arrays.asList(
            new Problem("Number of Islands", "Medium", "O(n*m)", "https://leetcode.com/problems/number-of-islands/"),
            new Problem("Clone Graph", "Medium", "O(n)", "https://leetcode.com/problems/clone-graph/"),
            new Problem("Walls and Gates", "Medium", "O(n*m)", "")
        ));
        
        problemsByCategory.put("Top K Elements", Arrays.asList(
            new Problem("Kth Largest Element", "Easy", "O(n log n)", "https://leetcode.com/problems/kth-largest-element-in-an-array/"),
            new Problem("Top K Frequent Elements", "Medium", "O(n log n)", "https://leetcode.com/problems/top-k-frequent-elements/"),
            new Problem("Sort Characters by Frequency", "Medium", "O(n log n)", "")
        ));
        
        problemsByCategory.put("Subsets", Arrays.asList(
            new Problem("Subsets", "Medium", "O(n * 2^n)", "https://leetcode.com/problems/subsets/"),
            new Problem("Subsets II", "Medium", "O(n * 2^n)", "https://leetcode.com/problems/subsets-ii/")
        ));
        
        problemsByCategory.put("Greedy", Arrays.asList(
            new Problem("Task Scheduler", "Medium", "O(n)", "https://leetcode.com/problems/task-scheduler/"),
            new Problem("Jump Game", "Medium", "O(n)", "https://leetcode.com/problems/jump-game/")
        ));
        
        problemsByCategory.put("Backtracking", Arrays.asList(
            new Problem("Permutations", "Medium", "O(n!)", "https://leetcode.com/problems/permutations/"),
            new Problem("Subsets", "Medium", "O(n * 2^n)", "")
        ));
        
        problemsByCategory.put("Trie", Arrays.asList(
            new Problem("Implement Trie", "Medium", "O(n)", "https://leetcode.com/problems/implement-trie-prefix-tree/"),
            new Problem("Word Search II", "Hard", "O(n * m)", "")
        ));
        
        problemsByCategory.put("Union Find", Arrays.asList(
            new Problem("Number of Provinces", "Medium", "O(n²)", "https://leetcode.com/problems/number-of-provinces/"),
            new Problem("Redundant Connection", "Medium", "O(n)", "")
        ));
    }
    
    private void showProblemsForCategory(String category) {
        List<Problem> problems = problemsByCategory.get(category);
        if (problems != null) {
            String[] problemNames = new String[problems.size()];
            for (int i = 0; i < problems.size(); i++) {
                Problem p = problems.get(i);
                problemNames[i] = p.name + " [" + p.difficulty + "]";
            }
            problemList.setListData(problemNames);
        }
    }
    
    private void showProblemDetails(String problemInfo) {
        if (problemInfo == null) return;
        
        String name = problemInfo.split("\\[")[0].trim();
        
        for (List<Problem> problems : problemsByCategory.values()) {
            for (Problem p : problems) {
                if (p.name.equals(name)) {
                    selectedProblem = p;
                    difficultyLabel.setText("Difficulty: " + p.difficulty);
                    complexityLabel.setText("Time Complexity: " + p.complexity);
                    
                    String fullDesc = getProblemDescription(p.name);
                    String examples = getProblemExamples(p.name);
                    String linkText = p.link.isEmpty() ? "" : "\n\nProblem Link: " + p.link;
                    
                    problemDescription.setText(
                        "=== PROBLEM ===\n" + p.name + "\n\n" +
                        "Difficulty: " + p.difficulty + " | Time Complexity: " + p.complexity + "\n\n" +
                        "=== DESCRIPTION ===\n" + fullDesc + "\n\n" +
                        "=== EXAMPLES ===\n" + examples + linkText + "\n\n" +
                        "Click 'Hint' for hints, 'Visualize' to see algorithm, or 'Show Solution' for code."
                    );
                    return;
                }
            }
        }
    }
    
    private void runCode() {
        String userCode = codeEditor.getText();
        if (userCode.trim().isEmpty() || userCode.contains("// Write your solution here")) {
            JOptionPane.showMessageDialog(this, "Please write your code first!", "No Code", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String output = compileAndRun(userCode);
        JTextArea outputArea = new JTextArea(output);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Output", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String compileAndRun(String userCode) {
        StringBuilder result = new StringBuilder();
        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            String fileName = "UserSolution";
            
            String fullCode = "import java.util.*;\n" +
                "import java.io.*;\n" +
                "public class " + fileName + " {\n" +
                "    public static void main(String[] args) {\n" +
                "        try {\n" +
                userCode.replace("public class Solution", "// public class Solution") +
                "\n        } catch (Exception e) {\n" +
                "            System.out.println(\"Error: \" + e.getMessage());\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
            
            File tempFile = new File(tempDir, fileName + ".java");
            FileWriter writer = new FileWriter(tempFile);
            writer.write(fullCode);
            writer.close();
            
            Process compileProcess = Runtime.getRuntime().exec("javac " + tempFile.getAbsolutePath());
            int compileResult = compileProcess.waitFor();
            
            if (compileResult != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
                String line;
                while ((line = errorReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                return "Compilation Error:\n" + result.toString();
            }
            
            Process runProcess = Runtime.getRuntime().exec("java -cp " + tempDir + " " + fileName);
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
            
            String line;
            while ((line = outputReader.readLine()) != null) {
                result.append(line).append("\n");
            }
            while ((line = errorReader.readLine()) != null) {
                result.append("Error: ").append(line).append("\n");
            }
            
            tempFile.delete();
            new File(tempDir, fileName + ".class").delete();
            
            if (result.length() == 0) {
                return "Program executed successfully (no output)";
            }
            return result.toString();
            
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    private void visualizeProblem() {
        if (selectedProblem == null) {
            JOptionPane.showMessageDialog(this, "Please select a problem first!");
            return;
        }
        
        String category = "";
        for (Map.Entry<String, List<Problem>> entry : problemsByCategory.entrySet()) {
            if (entry.getValue().contains(selectedProblem)) {
                category = entry.getKey();
                break;
            }
        }
        
        int targetTab = getTargetTabIndex(category, selectedProblem.name);
        
        JTabbedPane tabbedPane = null;
        for (Component comp : mainFrame.getContentPane().getComponents()) {
            if (comp instanceof JTabbedPane) {
                tabbedPane = (JTabbedPane) comp;
                break;
            }
        }
        
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(targetTab);
            String[] tabNames = {"Array Sorting", "Stack", "Queue", "Circular Queue", "Linked List", "Binary Search Tree", "Heap", "Hash Table", "Graph", "Problems"};
            JOptionPane.showMessageDialog(this, 
                "Switched to: " + tabNames[targetTab] + " tab\n\nProblem: " + selectedProblem.name);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Visualization: " + selectedProblem.name + "\n\n" +
                "Category: " + category + "\n" +
                "This will open the appropriate visualization panel for this problem.");
        }
    }
    
    private int getTargetTabIndex(String category, String problemName) {
        String name = problemName.toLowerCase();
        
        if (name.contains("linked") || name.contains("cycle") || name.contains("palindrome") || 
            name.contains("middle")) {
            return 4;
        }
        if (name.contains("tree") || name.contains("bst") || name.contains("traversal") ||
            name.contains("inorder") || name.contains("preorder") || name.contains("postorder") ||
            name.contains("level order")) {
            return 5;
        }
        if (name.contains("heap") || name.contains("kth largest") || name.contains("kth smallest")) {
            return 6;
        }
        if (name.contains("hash")) {
            return 7;
        }
        if (name.contains("graph") || name.contains("island") || name.contains("clone") || 
            name.contains("province")) {
            return 8;
        }
        if (name.contains("queue") || name.contains("deque")) {
            return 2;
        }
        if (name.contains("stack") || name.contains("parentheses") || name.contains("decode")) {
            return 1;
        }
        
        if (category.contains("Two Pointers") || category.contains("Fast & Slow") || 
            category.contains("Sliding Window") || category.contains("Kadane") ||
            category.contains("Merge Intervals") || category.contains("Cyclic Sort") ||
            category.contains("In-place Reversal") || category.contains("Top K") || 
            category.contains("Subsets") || category.contains("Greedy") || 
            category.contains("Backtracking") || category.contains("Trie") || 
            category.contains("Union Find")) {
            return 0;
        }
        
        return 0;
    }
    
    private void showHint() {
        if (selectedProblem != null) {
            String hint = getHintForProblem(selectedProblem.name);
            JOptionPane.showMessageDialog(this, "Hint:\n\n" + hint);
        }
    }
    
    private String getHintForProblem(String name) {
        Map<String, String> hints = new HashMap<>();
        hints.put("Pair with Target Sum", "Use two pointers - one at start, one at end. Sum them: if > target, move end pointer left; if < target, move start pointer right. Array is sorted!");
        hints.put("Remove Duplicates", "Use two pointers: one for iteration, one for tracking unique elements. Skip duplicates by checking if current equals previous.");
        hints.put("Triplet Sum to Zero", "Sort the array first. Then fix one element and use two pointers for the remaining two. Handle duplicates carefully.");
        hints.put("LinkedList Cycle", "Use Floyd's Tortoise and Hare algorithm: slow pointer moves 1 step, fast moves 2 steps. If they meet, cycle exists.");
        hints.put("Happy Number", "Use slow/fast pointers. Calculate sum of squares of digits repeatedly. If it reaches 1, it's happy. If it loops, it's not.");
        hints.put("Middle of LinkedList", "Use two pointers: slow moves 1 step, fast moves 2 steps. When fast reaches end, slow is at middle.");
        hints.put("Palindrome LinkedList", "Find middle, reverse second half, compare both halves, then restore the list.");
        hints.put("Maximum Subarray", "Use Kadane's algorithm: Keep track of current sum and max sum. Reset current sum if it becomes negative.");
        hints.put("Max Sum Subarray of Size K", "Use sliding window of size K. Calculate initial sum, then slide by subtracting leftmost and adding next element.");
        hints.put("Smallest Subarray with Given Sum", "Use sliding window. Expand right, shrink left when sum >= K. Track minimum length.");
        hints.put("Valid Parentheses", "Use a stack. Push opening brackets '(', '{', '['. For closing, check if top of stack matches.");
        hints.put("Min Stack", "Use two stacks: one for values, one for minimums. Push/pop from both synchronously.");
        hints.put("Merge Intervals", "Sort intervals by start time. Iterate and merge overlapping: if current.start <= previous.end, merge them.");
        hints.put("Reverse LinkedList", "Use three pointers: prev, current, next. Iterate and reverse each link. Finally, update head.");
        hints.put("Subsets", "Use backtracking or bit manipulation. For each element, either include or exclude it. Total 2^n subsets.");
        hints.put("Level Order Traversal", "Use BFS with a queue. Process level by level: poll node, add children, count nodes at each level.");
        hints.put("Inorder Traversal", "Use recursion or stack: left -> root -> right. For BST, gives sorted order.");
        hints.put("Number of Islands", "Use DFS or BFS. For each unvisited '1', explore all connected land cells and mark as visited. Count islands.");
        hints.put("Kth Largest Element", "Use QuickSelect or Priority Queue (Min-Heap of size K). O(n) average time.");
        hints.put("Task Scheduler", "Use greedy: count frequencies, sort, schedule most frequent first with cooldown n.");
        hints.put("Jump Game", "Start from index 0. Keep track of max reachable index. If at any point current index > max reach, return false.");
        hints.put("Permutations", "Use backtracking: swap each element with all subsequent elements, recurse, then backtrack.");
        
        return hints.getOrDefault(name, "Think about the core pattern. What data structure fits best?\n\nHint: Consider using Two Pointers, Sliding Window, Fast/Slow Pointers, or Stack patterns.");
    }
    
    private void showSolution() {
        if (selectedProblem != null) {
            String solution = getSolutionForProblem(selectedProblem.name);
            JOptionPane.showMessageDialog(this, "Solution:\n\n" + solution);
        }
    }
    
    private String getSolutionForProblem(String name) {
        Map<String, String> solutions = new HashMap<>();
        
        solutions.put("Pair with Target Sum", 
            "public int[] twoSum(int[] numbers, int target) {\n" +
            "    int left = 0, right = numbers.length - 1;\n" +
            "    while (left < right) {\n" +
            "        int sum = numbers[left] + numbers[right];\n" +
            "        if (sum == target) return new int[]{left+1, right+1};\n" +
            "        else if (sum < target) left++;\n" +
            "        else right--;\n" +
            "    }\n" +
            "    return new int[]{-1, -1};\n" +
            "}");
        
        solutions.put("Remove Duplicates",
            "public int removeDuplicates(int[] nums) {\n" +
            "    if (nums.length == 0) return 0;\n" +
            "    int slow = 0;\n" +
            "    for (int fast = 1; fast < nums.length; fast++) {\n" +
            "        if (nums[fast] != nums[slow]) {\n" +
            "            slow++;\n" +
            "            nums[slow] = nums[fast];\n" +
            "        }\n" +
            "    }\n" +
            "    return slow + 1;\n" +
            "}");
        
        solutions.put("Maximum Subarray",
            "public int maxSubArray(int[] nums) {\n" +
            "    int maxSum = nums[0];\n" +
            "    int currentSum = nums[0];\n" +
            "    for (int i = 1; i < nums.length; i++) {\n" +
            "        currentSum = Math.max(nums[i], currentSum + nums[i]);\n" +
            "        maxSum = Math.max(maxSum, currentSum);\n" +
            "    }\n" +
            "    return maxSum;\n" +
            "}");
        
        solutions.put("LinkedList Cycle",
            "public boolean hasCycle(ListNode head) {\n" +
            "    ListNode slow = head, fast = head;\n" +
            "    while (fast != null && fast.next != null) {\n" +
            "        slow = slow.next;\n" +
            "        fast = fast.next.next;\n" +
            "        if (slow == fast) return true;\n" +
            "    }\n" +
            "    return false;\n" +
            "}");
        
        solutions.put("Middle of LinkedList",
            "public ListNode middleNode(ListNode head) {\n" +
            "    ListNode slow = head, fast = head;\n" +
            "    while (fast != null && fast.next != null) {\n" +
            "        slow = slow.next;\n" +
            "        fast = fast.next.next;\n" +
            "    }\n" +
            "    return slow;\n" +
            "}");
        
        solutions.put("Reverse LinkedList",
            "public ListNode reverseList(ListNode head) {\n" +
            "    ListNode prev = null;\n" +
            "    ListNode curr = head;\n" +
            "    while (curr != null) {\n" +
            "        ListNode next = curr.next;\n" +
            "        curr.next = prev;\n" +
            "        prev = curr;\n" +
            "        curr = next;\n" +
            "    }\n" +
            "    return prev;\n" +
            "}");
        
        solutions.put("Valid Parentheses",
            "public boolean isValid(String s) {\n" +
            "    Stack<Character> stack = new Stack<>();\n" +
            "    for (char c : s.toCharArray()) {\n" +
            "        if (c == '(' || c == '{' || c == '[')\n" +
            "            stack.push(c);\n" +
            "        else if (stack.isEmpty() ||\n" +
            "            (c == ')' && stack.pop() != '(') ||\n" +
            "            (c == '}' && stack.pop() != '{') ||\n" +
            "            (c == ']' && stack.pop() != '['))\n" +
            "            return false;\n" +
            "    }\n" +
            "    return stack.isEmpty();\n" +
            "}");
        
        solutions.put("Merge Intervals",
            "public int[][] merge(int[][] intervals) {\n" +
            "    if (intervals.length <= 1) return intervals;\n" +
            "    Arrays.sort(intervals, (a,b) -> a[0] - b[0]);\n" +
            "    List<int[]> result = new ArrayList<>();\n" +
            "    int[] newInterval = intervals[0];\n" +
            "    result.add(newInterval);\n" +
            "    for (int[] interval : intervals) {\n" +
            "        if (interval[0] <= newInterval[1]) {\n" +
            "            newInterval[1] = Math.max(newInterval[1], interval[1]);\n" +
            "        } else {\n" +
            "            newInterval = interval;\n" +
            "            result.add(newInterval);\n" +
            "        }\n" +
            "    }\n" +
            "    return result.toArray(new int[result.size()][]);\n" +
            "}");
        
        solutions.put("Max Sum Subarray of Size K",
            "public int maxSumSubarray(int[] arr, int k) {\n" +
            "    int windowSum = 0;\n" +
            "    for (int i = 0; i < k; i++)\n" +
            "        windowSum += arr[i];\n" +
            "    int maxSum = windowSum;\n" +
            "    for (int i = k; i < arr.length; i++) {\n" +
            "        windowSum += arr[i] - arr[i - k];\n" +
            "        maxSum = Math.max(maxSum, windowSum);\n" +
            "    }\n" +
            "    return maxSum;\n" +
            "}");
        
        solutions.put("Find Missing Number",
            "public int missingNumber(int[] nums) {\n" +
            "    int n = nums.length;\n" +
            "    int expectedSum = n * (n + 1) / 2;\n" +
            "    int actualSum = 0;\n" +
            "    for (int num : nums) actualSum += num;\n" +
            "    return expectedSum - actualSum;\n" +
            "}");
        
        solutions.put("Number of Islands",
            "public int numIslands(char[][] grid) {\n" +
            "    int count = 0;\n" +
            "    for (int i = 0; i < grid.length; i++) {\n" +
            "        for (int j = 0; j < grid[0].length; j++) {\n" +
            "            if (grid[i][j] == '1') {\n" +
            "                dfs(grid, i, j);\n" +
            "                count++;\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "    return count;\n" +
            "}\n" +
            "private void dfs(char[][] grid, int i, int j) {\n" +
            "    if (i < 0 || j < 0 || i >= grid.length || \n" +
            "        j >= grid[0].length || grid[i][j] != '1') return;\n" +
            "    grid[i][j] = '0';\n" +
            "    dfs(grid, i+1, j); dfs(grid, i-1, j);\n" +
            "    dfs(grid, i, j+1); dfs(grid, i, j-1);\n" +
            "}");
        
        solutions.put("Kth Largest Element",
            "public int findKthLargest(int[] nums, int k) {\n" +
            "    PriorityQueue<Integer> minHeap = new PriorityQueue<>();\n" +
            "    for (int num : nums) {\n" +
            "        minHeap.add(num);\n" +
            "        if (minHeap.size() > k) minHeap.poll();\n" +
            "    }\n" +
            "    return minHeap.poll();\n" +
            "}");
        
        solutions.put("Inorder Traversal",
            "public List<Integer> inorderTraversal(TreeNode root) {\n" +
            "    List<Integer> result = new ArrayList<>();\n" +
            "    inorder(root, result);\n" +
            "    return result;\n" +
            "}\n" +
            "private void inorder(TreeNode node, List<Integer> result) {\n" +
            "    if (node == null) return;\n" +
            "    inorder(node.left, result);\n" +
            "    result.add(node.val);\n" +
            "    inorder(node.right, result);\n" +
            "}");
        
        return solutions.getOrDefault(name, "Solution not available. Please refer to the problem link.");
    }
    
    static class Problem {
        String name;
        String difficulty;
        String complexity;
        String link;
        
        Problem(String name, String difficulty, String complexity, String link) {
            this.name = name;
            this.difficulty = difficulty;
            this.complexity = complexity;
            this.link = link;
        }
    }
    
    private String getProblemDescription(String name) {
        Map<String, String> descriptions = new HashMap<>();
        descriptions.put("Pair with Target Sum", "Given a 1-indexed array of integers 'numbers' that is sorted in non-decreasing order, find two numbers such that they add up to a specific 'target' number. Return the indices of the two numbers (1-indexed).\n\nConstraints:\n- 2 <= numbers.length <= 3 * 10^4\n- -1000 <= numbers[i] <= 1000\n- -1000 <= target <= 1000\n- Exactly one solution exists\n- Cannot use same element twice\n- Must use only constant extra space");
        
        descriptions.put("Valid Parentheses", "Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.\n\nAn input string is valid if:\n1. Open brackets must be closed by the same type of brackets.\n2. Open brackets must be closed in the correct order.\n3. Every close bracket has a corresponding open bracket of the same type.\n\nConstraints:\n- 1 <= s.length <= 10^4\n- s consists of parentheses only");
        
        descriptions.put("LinkedList Cycle", "Given head, the head of a linked list, determine if the linked list has a cycle in it.\n\nThere is a cycle in a linked list if there is some node in the list that can be reached again by continuously following the next pointer.\n\nConstraints:\n- The number of nodes in the list is in the range [0, 10^4]\n- -10^5 <= Node.val <= 10^5");
        
        descriptions.put("Maximum Subarray", "Given an integer array nums, find the subarray with the largest sum, and return its sum.\n\nConstraints:\n- 1 <= nums.length <= 10^5\n- -10^4 <= nums[i] <= 10^4");
        
        descriptions.put("Merge Intervals", "Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals, and return an array of the non-overlapping intervals that cover all the intervals in the input.\n\nConstraints:\n- 1 <= intervals.length <= 10^4\n- intervals[i].length == 2\n- 0 <= starti <= endi <= 10^4");
        
        descriptions.put("Reverse LinkedList", "Given the head of a singly linked list, reverse the list, and return the reversed list.\n\nConstraints:\n- The number of nodes in the list is in the range [0, 5000]\n- -5000 <= Node.val <= 5000");
        
        descriptions.put("Middle of LinkedList", "Given the head of a singly linked list, return the middle node of the linked list.\n\nIf there are two middle nodes, return the second middle node.\n\nConstraints:\n- The number of nodes in the list is in the range [1, 100]\n- 1 <= Node.val <= 100");
        
        descriptions.put("Subsets", "Given an integer array nums of unique elements, return all possible subsets (the power set).\n\nThe solution set must not contain duplicate subsets. Return the solution in any order.\n\nConstraints:\n- 1 <= nums.length <= 10\n- -10 <= nums[i] <= 10\n- All the numbers of nums are unique");
        
        return descriptions.getOrDefault(name, "Description not available for this problem.");
    }
    
    private String getProblemExamples(String name) {
        Map<String, String> examples = new HashMap<>();
        examples.put("Pair with Target Sum", "Example 1:\nInput: numbers = [2,7,11,15], target = 9\nOutput: [1,2]\nExplanation: The sum of 2 and 7 is 9.\n\nExample 2:\nInput: numbers = [2,3,4], target = 6\nOutput: [1,3]\nExplanation: The sum of 2 and 4 is 6.");
        
        examples.put("Valid Parentheses", "Example 1:\nInput: s = \"()\"\nOutput: true\n\nExample 2:\nInput: s = \"()[]{}\"\nOutput: true\n\nExample 3:\nInput: s = \"(]\"\nOutput: false");
        
        examples.put("LinkedList Cycle", "Example 1:\nInput: head = [3,2,0,-4], pos = 1\nOutput: true\nExplanation: There is a cycle where tail connects to index 1.\n\nExample 2:\nInput: head = [1,2], pos = 0\nOutput: true\nExplanation: There is a cycle where tail connects to index 0.");
        
        examples.put("Maximum Subarray", "Example 1:\nInput: nums = [-2,1,-3,4,-1,2,1,-5,4]\nOutput: 6\nExplanation: The subarray [4,-1,2,1] has the largest sum 6.\n\nExample 2:\nInput: nums = [1]\nOutput: 1");
        
        examples.put("Merge Intervals", "Example 1:\nInput: intervals = [[1,3],[2,6],[8,10],[15,18]]\nOutput: [[1,6],[8,10],[15,18]]\nExplanation: Since [1,3] and [2,6] overlap, merge them into [1,6].\n\nExample 2:\nInput: intervals = [[1,4],[4,5]]\nOutput: [[1,5]]");
        
        examples.put("Reverse LinkedList", "Example 1:\nInput: head = [1,2,3,4,5]\nOutput: [5,4,3,2,1]\n\nExample 2:\nInput: head = [1,2]\nOutput: [2,1]");
        
        examples.put("Middle of LinkedList", "Example 1:\nInput: head = [1,2,3,4,5]\nOutput: 3\nExplanation: The middle node is 3.\n\nExample 2:\nInput: head = [1,2,3,4,5,6]\nOutput: 4\nExplanation: There are two middle nodes (3 and 4), return the second one.");
        
        examples.put("Subsets", "Example 1:\nInput: nums = [1,2,3]\nOutput: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]\n\nExample 2:\nInput: nums = [0]\nOutput: [[],[0]]");
        
        return examples.getOrDefault(name, "No examples available.");
    }
}
