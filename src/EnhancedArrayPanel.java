import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

public class EnhancedArrayPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    private DSAVisualizerUltraPro mainFrame;
    private int[] array;
    private int[] originalArray;
    private Stack<int[]> history = new Stack<>();
    private int currentI = -1, currentJ = -1;
    private Timer timer;
    private boolean sorting = false;
    private boolean paused = false;
    private boolean next = false;
    private int i = 0, j = 0;
    private int draggedIndex = -1;
    private String algorithm = "Bubble Sort";
    private int comparisons = 0;
    private int swaps = 0;
    private String complexity = "";
    private String currentConcept = "";
    private String currentAction = "";
    private String currentRule = "";
    private JLabel compLabel, swapLabel, complexLabel;
    private JTextArea pseudoCode;
    private boolean showArrows = true;
    private String[] pseudoCodes = {
        "Bubble Sort:\nfor i = 0 to n-1\n  for j = 0 to n-i-1\n    if arr[j] > arr[j+1]\n      swap(arr[j], arr[j+1])",
        "Selection Sort:\nfor i = 0 to n-1\n  min = i\n  for j = i+1 to n\n    if arr[j] < arr[min]\n      min = j\n  swap(arr[i], arr[min])",
        "Insertion Sort:\nfor i = 1 to n\n  key = arr[i]\n  j = i-1\n  while j >= 0 and arr[j] > key\n    arr[j+1] = arr[j]\n    j--\n  arr[j+1] = key",
        "Quick Sort:\npivot = partition(arr, low, high)\nquickSort(arr, low, pivot-1)\nquickSort(arr, pivot+1, high)",
        "Merge Sort:\nmergeSort(arr, l, r):\n  if l < r:\n    mid = (l+r)/2\n    mergeSort(arr, l, mid)\n    mergeSort(arr, mid+1, r)\n    merge(arr, l, mid, r)",
        "Radix Sort:\nfor d = 1 to maxDigits\n  buckets = array of 10 queues\n  for each element\n    digit = getDigit(element, d)\n    buckets[digit].enqueue(element)\n  combine buckets to array"
    };
    
    public EnhancedArrayPanel(DSAVisualizerUltraPro frame) {
        this.mainFrame = frame;
        setBackground(frame.getBgColor());
        
        timer = new Timer(700, this);
        
        JPanel controls = new JPanel();
        controls.setBackground(frame.getBgColor());
        
        JButton inputBtn = new JButton("Manual Input");
        JButton randomBtn = new JButton("Random Array");
        JButton startBtn = new JButton("Start");
        JButton pauseBtn = new JButton("Pause");
        JButton stepBtn = new JButton("Step");
        JButton undoBtn = new JButton("Undo");
        JButton resetBtn = new JButton("Reset");
        
        String[] algos = {"Bubble Sort", "Selection Sort", "Insertion Sort", "Quick Sort", "Merge Sort", "Radix Sort"};
        JComboBox<String> algoBox = new JComboBox<>(algos);
        
        // Speed options with recommended delays
        String[] speeds = {"Slow (700ms)", "Normal (500ms)", "Fast (300ms)", "Turbo (50ms)"};
        JComboBox<String> speedBox = new JComboBox<>(speeds);
        speedBox.setSelectedIndex(0);  // Default to Slow
        speedBox.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Set default timer delay to 700ms (Slow)
        timer.setDelay(700);
        
        compLabel = new JLabel("Comparisons: 0");
        swapLabel = new JLabel("Swaps: 0");
        complexLabel = new JLabel("Complexity: O(?)");
        compLabel.setForeground(frame.getFgColor());
        swapLabel.setForeground(frame.getFgColor());
        complexLabel.setForeground(frame.getFgColor());
        
        inputBtn.addActionListener(e -> inputArray());
        randomBtn.addActionListener(e -> generateRandomArray());
        startBtn.addActionListener(e -> startSorting(algoBox));
        pauseBtn.addActionListener(e -> { paused = !paused; });
        stepBtn.addActionListener(e -> { if (!sorting) { sorting = true; next = true; pausedStep(); } else { nextStep(); } });
        undoBtn.addActionListener(e -> undo());
        resetBtn.addActionListener(e -> reset());
        
        // Speed selection from dropdown
        speedBox.addActionListener(e -> {
            String selected = (String) speedBox.getSelectedItem();
            if (selected != null) {
                if (selected.startsWith("Slow")) timer.setDelay(700);
                else if (selected.startsWith("Normal")) timer.setDelay(500);
                else if (selected.startsWith("Fast")) timer.setDelay(300);
                else if (selected.startsWith("Turbo")) timer.setDelay(50);
            }
        });
        
        algoBox.addActionListener(e -> updateComplexity((String) algoBox.getSelectedItem()));
        
        controls.add(inputBtn);
        controls.add(randomBtn);
        controls.add(algoBox);
        controls.add(startBtn);
        controls.add(pauseBtn);
        controls.add(stepBtn);
        controls.add(undoBtn);
        controls.add(resetBtn);
        controls.add(new JLabel("Speed:"));
        controls.add(speedBox);
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBackground(frame.getBgColor());
        statsPanel.add(compLabel);
        statsPanel.add(swapLabel);
        statsPanel.add(complexLabel);
        
        pseudoCode = new JTextArea(6, 30);
        pseudoCode.setEditable(false);
        pseudoCode.setFont(new Font("Monospaced", Font.PLAIN, 11));
        pseudoCode.setBackground(frame.isDarkTheme() ? new Color(50, 50, 50) : Color.WHITE);
        pseudoCode.setForeground(frame.getFgColor());
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(controls, BorderLayout.NORTH);
        southPanel.add(statsPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        add(pseudoCode, BorderLayout.EAST);
        
        addMouseListener(this);
        addMouseMotionListener(this);
        
        generateRandomArray();
        updateComplexity("Bubble Sort");
    }
    
    private void updateComplexity(String algo) {
        algorithm = algo;
        switch(algo) {
            case "Bubble Sort": complexity = "O(n²)"; pseudoCode.setText(pseudoCodes[0]); break;
            case "Selection Sort": complexity = "O(n²)"; pseudoCode.setText(pseudoCodes[1]); break;
            case "Insertion Sort": complexity = "O(n²)"; pseudoCode.setText(pseudoCodes[2]); break;
            case "Quick Sort": complexity = "O(n log n)"; pseudoCode.setText(pseudoCodes[3]); break;
            case "Merge Sort": complexity = "O(n log n)"; pseudoCode.setText(pseudoCodes[4]); break;
            case "Radix Sort": complexity = "O(nk)"; pseudoCode.setText(pseudoCodes[5]); break;
        }
        complexLabel.setText("Complexity: " + complexity);
    }
    
    private void saveState() {
        history.push(array.clone());
        if (history.size() > 50) history.remove(0);
    }
    
    private void undo() {
        if (!history.isEmpty()) {
            array = history.pop();
            comparisons = 0;
            swaps = 0;
            i = 0; j = 0;
            sorting = false;
            timer.stop();
            updateStats();
            repaint();
        }
    }
    
    private void reset() {
        if (originalArray != null) {
            array = originalArray.clone();
            history.clear();
            comparisons = 0;
            swaps = 0;
            i = 0; j = 0;
            sorting = false;
            currentI = -1;
            currentJ = -1;
            timer.stop();
            updateStats();
            repaint();
        }
    }
    
    private void startSorting(JComboBox<String> algoBox) {
        algorithm = (String) algoBox.getSelectedItem();
        saveState();
        sorting = true;
        paused = false;
        i = 0; j = 0;
        comparisons = 0;
        swaps = 0;
        timer.start();
    }
    
    private void updateStats() {
        compLabel.setText("Comparisons: " + comparisons);
        swapLabel.setText("Swaps: " + swaps);
    }
    
    private void generateRandomArray() {
        int size = 10 + (int)(Math.random() * 10);
        array = new int[size];
        Random rand = new Random();
        for (int k = 0; k < size; k++) {
            array[k] = 1 + rand.nextInt(20);
        }
        originalArray = array.clone();
        history.clear();
        comparisons = 0;
        swaps = 0;
        i = 0; j = 0;
        sorting = false;
        timer.stop();
        updateStats();
        repaint();
    }
    
    private void inputArray() {
        try {
            String sizeStr = JOptionPane.showInputDialog(this, "Enter array size (max 20):");
            int n = Math.min(Math.max(Integer.parseInt(sizeStr), 1), 20);
            array = new int[n];
            for (int k = 0; k < n; k++) {
                String val = JOptionPane.showInputDialog(this, "Enter element " + (k + 1) + ":");
                array[k] = Integer.parseInt(val);
            }
            originalArray = array.clone();
            history.clear();
            comparisons = 0;
            swaps = 0;
            i = 0; j = 0;
            sorting = false;
            timer.stop();
            updateStats();
            repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input");
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (next) {
            nextStep();
            next = false;
        } else if (!paused && sorting) {
            nextStep();
        }
        repaint();
    }
    
    private void pausedStep() {
        next = true;
    }
    
    private void nextStep() {
        if (array == null) return;
        currentI = -1;
        currentJ = -1;
        
        switch (algorithm) {
            case "Bubble Sort": bubbleStep(); break;
            case "Selection Sort": selectionStep(); break;
            case "Insertion Sort": insertionStep(); break;
            case "Quick Sort": quickStep(); break;
            case "Merge Sort": mergeStep(); break;
            case "Radix Sort": radixStep(); break;
        }
        updateStats();
    }
    
    private void bubbleStep() {
        if (i < array.length - 1) {
            if (j < array.length - i - 1) {
                currentI = j;
                currentJ = j + 1;
                comparisons++;
                
                // Update concept linking
                currentConcept = "Bubble Sort: Compare Adjacent Elements";
                currentAction = "Comparing arr[" + j + "]=" + array[j] + " with arr[" + (j+1) + "]=" + array[j+1];
                
                if (array[j] > array[j + 1]) {
                    currentRule = "Rule: If arr[j] > arr[j+1], swap them (element out of order)";
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swaps++;
                    currentAction = "Swapping " + array[j+1] + " with " + array[j] + " (larger element moves right)";
                } else {
                    currentRule = "Rule: No swap needed (elements already in order)";
                }
                
                j++;
            } else {
                j = 0;
                i++;
            }
        } else {
            sorting = false;
            timer.stop();
            currentConcept = "Sorting Complete!";
            currentAction = "Array is now sorted in ascending order";
            currentRule = "Bubble Sort passes through entire array, moving largest elements to the end";
        }
    }
    
    private void selectionStep() {
        if (i < array.length - 1) {
            int min = i;
            for (int k = i + 1; k < array.length; k++) {
                comparisons++;
                if (array[k] < array[min]) min = k;
            }
            currentI = i;
            currentJ = min;
            
            // Update concept linking
            currentConcept = "Selection Sort: Find Minimum";
            currentAction = "Finding minimum element in unsorted portion";
            
            if (min != i) {
                currentRule = "Rule: Swap current element with minimum element found";
                int temp = array[i];
                array[i] = array[min];
                array[min] = temp;
                swaps++;
                currentAction = "Swapping arr[" + i + "]=" + array[i] + " with minimum " + array[min];
            } else {
                currentRule = "Rule: Element already in correct position";
            }
            i++;
        } else {
            sorting = false;
            timer.stop();
            currentConcept = "Sorting Complete!";
            currentAction = "Array is now sorted";
            currentRule = "Selection Sort builds sorted portion one element at a time";
        }
    }
    
    private void insertionStep() {
        if (i < array.length) {
            int key = array[i];
            int k = i - 1;
            
            currentI = i;
            currentJ = k;
            
            // Update concept linking
            currentConcept = "Insertion Sort: Insert Element";
            currentAction = "Inserting element " + key + " into sorted portion";
            
            while (k >= 0) {
                comparisons++;
                if (array[k] > key) {
                    currentRule = "Rule: Shift larger elements right to make space";
                    array[k + 1] = array[k];
                    swaps++;
                    k--;
                } else {
                    break;
                }
            }
            array[k + 1] = key;
            if (k != i - 1) {
                currentAction = "Inserted " + key + " at correct position";
            } else {
                currentRule = "Rule: Element already in correct position";
            }
            i++;
        } else {
            sorting = false;
            timer.stop();
            currentConcept = "Sorting Complete!";
            currentAction = "Array is now sorted";
            currentRule = "Insertion Sort builds sorted array one element at a time";
        }
    }
    
    private int quickPivot = -1;
    private int quickLow = 0;
    
    private void quickStep() {
        if (i >= array.length - 1) {
            sorting = false;
            timer.stop();
            return;
        }
        if (j == -1) {
            quickPivot = array[i];
            quickLow = i;
            j = i + 1;
        }
        if (j < array.length) {
            comparisons++;
            if (array[j] < quickPivot) {
                quickLow++;
                int temp = array[quickLow];
                array[quickLow] = array[j];
                array[j] = temp;
                swaps++;
            }
            j++;
        } else {
            int temp = array[quickLow];
            array[quickLow] = array[i];
            array[i] = temp;
            swaps++;
            currentI = quickLow;
            i = quickLow + 1;
            j = -1;
        }
    }
    
    private int mergeLeft, mergeRight, mergeEnd;
    
    private void mergeStep() {
        if (i >= array.length) {
            sorting = false;
            timer.stop();
            return;
        }
        if (j == 0) {
            mergeLeft = i;
            mergeRight = Math.min(i + 1, array.length);
            mergeEnd = Math.min(i + 2, array.length);
            j = 1;
        }
        if (j < 3) {
            j++;
        } else {
            i += 2;
            j = 0;
        }
    }
    
    private int radixDigit = 1;
    
    private void radixStep() {
        if (radixDigit > 100) {
            sorting = false;
            timer.stop();
            return;
        }
        int maxVal = Arrays.stream(array).max().orElse(0);
        if (radixDigit <= maxVal * 10) {
            java.util.List<Integer>[] buckets = new ArrayList[10];
            for (int b = 0; b < 10; b++) buckets[b] = new ArrayList<>();
            
            for (int val : array) {
                int d = (val / radixDigit) % 10;
                buckets[d].add(val);
            }
            
            int idx = 0;
            for (java.util.List<Integer> bucket : buckets) {
                for (int val : bucket) {
                    array[idx++] = val;
                }
            }
            radixDigit *= 10;
        } else {
            sorting = false;
            timer.stop();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (array == null) return;
        
        int width = getWidth() / array.length;
        for (int k = 0; k < array.length; k++) {
            int height = array[k] * 15;
            int x = k * width;
            int y = getHeight() - height - 100;
            
            Color base = (k == currentI || k == currentJ) ? Color.RED : mainFrame.getAccentColor();
            if (k == quickPivot || k == mergeLeft || k == mergeRight) base = Color.GREEN;
            
            GradientPaint gp = new GradientPaint(x, y, base.brighter(), x + width, y + height, base.darker());
            g2.setPaint(gp);
            g2.fillRoundRect(x + 5, y, width - 10, height, 15, 15);
            
            g2.setColor(mainFrame.getFgColor());
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            String val = String.valueOf(array[k]);
            int strW = g2.getFontMetrics().stringWidth(val);
            g2.drawString(val, x + (width - strW) / 2, y - 5);
        }
        
        // Draw arrows for current comparison/swap
        if (showArrows && currentI >= 0 && currentJ >= 0 && currentI < array.length && currentJ < array.length) {
            int x1 = currentI * width + width / 2;
            int x2 = currentJ * width + width / 2;
            int arrowY = getHeight() - 70;
            
            g2.setColor(new Color(255, 100, 100));
            g2.setStroke(new BasicStroke(3));
            
            // Arrow from currentI
            g2.fillPolygon(new int[]{x1 - 8, x1 + 8, x1}, new int[]{arrowY - 15, arrowY - 15, arrowY - 25}, 3);
            g2.drawLine(x1, arrowY - 10, x1, arrowY);
            
            // Arrow from currentJ  
            g2.fillPolygon(new int[]{x2 - 8, x2 + 8, x2}, new int[]{arrowY - 15, arrowY - 15, arrowY - 25}, 3);
            g2.drawLine(x2, arrowY - 10, x2, arrowY);
            
            // Draw swap direction indicator
            g2.setColor(new Color(255, 200, 100));
            g2.setFont(new Font("Arial", Font.BOLD, 11));
            String swapText = "Comparing";
            if (array.length > currentI && array.length > currentJ) {
                if (array[currentI] > array[currentJ]) {
                    swapText = "Will Swap!";
                }
            }
            int midX = (x1 + x2) / 2 - 25;
            g2.drawString(swapText, midX, arrowY - 30);
        }
        
        // Draw concept/rule info
        if (sorting && !currentConcept.isEmpty()) {
            g2.setColor(new Color(255, 180, 100));
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString("Concept: " + currentConcept, 10, getHeight() - 45);
            
            g2.setColor(new Color(100, 200, 255));
            g2.drawString("Action: " + currentAction, 10, getHeight() - 28);
        }
        
        g2.setColor(mainFrame.getFgColor());
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Comparisons: " + comparisons, 10, 25);
        g2.drawString("Swaps: " + swaps, 10, 45);
        g2.drawString("Algorithm: " + algorithm, 10, 65);
        g2.drawString("Complexity: " + complexity, 10, 85);
    }
    
    @Override public void mousePressed(MouseEvent e) {
        if (array == null) return;
        int width = getWidth() / array.length;
        for (int k = 0; k < array.length; k++) {
            int x = k * width;
            if (e.getX() >= x && e.getX() <= x + width) {
                draggedIndex = k;
                break;
            }
        }
    }
    
    @Override public void mouseDragged(MouseEvent e) {
        if (draggedIndex != -1 && array != null) {
            array[draggedIndex] = Math.max(1, (getHeight() - e.getY() - 100) / 15);
            repaint();
        }
    }
    
    @Override public void mouseReleased(MouseEvent e) { draggedIndex = -1; }
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
