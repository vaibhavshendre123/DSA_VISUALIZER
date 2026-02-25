import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.*;
import javax.swing.Timer;

public class DSAVisualizerUltraPro extends JFrame {
    private boolean darkTheme = true;
    private Color bgColor = new Color(30, 30, 30);
    private Color fgColor = Color.WHITE;
    private Color accentColor = new Color(0, 150, 255);
    
    public DSAVisualizerUltraPro() {
        setTitle("DSA Visualizer Ultra Pro");
        setSize(1300, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        updateTheme();
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Array Sorting", new EnhancedArrayPanel(this));
        tabs.addTab("Stack", new EnhancedStackPanel(this));
        tabs.addTab("Queue", new EnhancedQueuePanel(this));
        tabs.addTab("Circular Queue", new CircularQueuePanel(this));
        tabs.addTab("Linked List", new EnhancedLinkedListPanel(this));
        tabs.addTab("Binary Search Tree", new BSTPanel(this));
        tabs.addTab("Heap", new HeapPanel(this));
        tabs.addTab("Hash Table", new HashTablePanel(this));
        tabs.addTab("Graph", new GraphPanel(this));
        tabs.addTab("Problems", new ProblemsPanel(this));
        
        add(tabs);
        
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton themeBtn = new JButton("Toggle Theme");
        themeBtn.addActionListener(e -> {
            darkTheme = !darkTheme;
            updateTheme();
            SwingUtilities.updateComponentTreeUI(this);
        });
        topBar.add(themeBtn);
        topBar.setBackground(bgColor);
        add(topBar, BorderLayout.NORTH);
        
        setVisible(true);
    }
    
    private void updateTheme() {
        if (darkTheme) {
            bgColor = new Color(30, 30, 30);
            fgColor = Color.WHITE;
            accentColor = new Color(0, 150, 255);
        } else {
            bgColor = new Color(240, 240, 240);
            fgColor = Color.BLACK;
            accentColor = new Color(0, 100, 200);
        }
    }
    
    Color getBgColor() { return bgColor; }
    Color getFgColor() { return fgColor; }
    Color getAccentColor() { return accentColor; }
    boolean isDarkTheme() { return darkTheme; }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DSAVisualizerUltraPro::new);
    }
    
    // =================== ENHANCED ARRAY SORTING PANEL ===================
    static class EnhancedArrayPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
        private DSAVisualizerUltraPro mainFrame;
        private int[] array;
        private int[] originalArray;
        private Stack<int[]> history = new Stack<>();
        private int currentI = -1, currentJ = -1;
        private Timer timer;
        private boolean sorting = false;
        private boolean paused = false;
        private int i = 0, j = 0;
        private int draggedIndex = -1;
        private String algorithm = "Bubble Sort";
        private int speed = 50;
        private int comparisons = 0;
        private int swaps = 0;
        private String complexity = "";
        private JLabel compLabel, swapLabel, complexLabel;
        private JTextArea pseudoCode;
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
            
            timer = new Timer(speed, this);
            
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
            
            JSlider speedSlider = new JSlider(10, 200, speed);
            speedSlider.setInverted(true);
            
            JButton slowBtn = new JButton("Slow");
            JButton mediumBtn = new JButton("Medium");
            JButton fastBtn = new JButton("Fast");
            
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
            stepBtn.addActionListener(e -> { if (!sorting) { sorting = true; paused = true; nextStep(); } else { nextStep(); } });
            undoBtn.addActionListener(e -> undo());
            resetBtn.addActionListener(e -> reset());
            
            speedSlider.addChangeListener(e -> {
                speed = speedSlider.getValue();
                timer.setDelay(210 - speed);
            });
            
            slowBtn.addActionListener(e -> speedSlider.setValue(30));
            mediumBtn.addActionListener(e -> speedSlider.setValue(100));
            fastBtn.addActionListener(e -> speedSlider.setValue(180));
            
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
            controls.add(speedSlider);
            controls.add(slowBtn);
            controls.add(mediumBtn);
            controls.add(fastBtn);
            
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
            if (!paused && sorting) {
                nextStep();
            }
            repaint();
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
                    if (array[j] > array[j + 1]) {
                        int temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;
                        swaps++;
                    }
                    j++;
                } else {
                    j = 0;
                    i++;
                }
            } else {
                sorting = false;
                timer.stop();
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
                if (min != i) {
                    int temp = array[i];
                    array[i] = array[min];
                    array[min] = temp;
                    swaps++;
                }
                i++;
            } else {
                sorting = false;
                timer.stop();
            }
        }
        
        private void insertionStep() {
            if (i < array.length) {
                int key = array[i];
                int k = i - 1;
                while (k >= 0) {
                    comparisons++;
                    if (array[k] > key) {
                        array[k + 1] = array[k];
                        swaps++;
                        k--;
                    } else break;
                }
                array[k + 1] = key;
                i++;
            } else {
                sorting = false;
                timer.stop();
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
            boolean done = true;
            int maxVal = Arrays.stream(array).max().orElse(0);
            if (radixDigit <= maxVal * 10) {
                List<Integer>[] buckets = new ArrayList[10];
                for (int b = 0; b < 10; b++) buckets[b] = new ArrayList<>();
                
                for (int val : array) {
                    int d = (val / radixDigit) % 10;
                    buckets[d].add(val);
                }
                
                int idx = 0;
                for (List<Integer> bucket : buckets) {
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
    
    // =================== ENHANCED STACK PANEL ===================
    static class EnhancedStackPanel extends JPanel {
        private DSAVisualizerUltraPro mainFrame;
        private Stack<Integer> stack = new Stack<>();
        private Stack<Integer> undoStack = new Stack<>();
        private int highlightTop = -1;
        
        public EnhancedStackPanel(DSAVisualizerUltraPro frame) {
            this.mainFrame = frame;
            setBackground(frame.getBgColor());
            
            JPanel controls = new JPanel();
            controls.setBackground(frame.getBgColor());
            
            JButton pushBtn = new JButton("Push");
            JButton popBtn = new JButton("Pop");
            JButton peekBtn = new JButton("Peek");
            JButton undoBtn = new JButton("Undo");
            JButton clearBtn = new JButton("Clear");
            JButton randomBtn = new JButton("Random Push");
            
            pushBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value:");
                if (val != null && !val.isEmpty()) {
                    try {
                        undoStack.push(null);
                        stack.push(Integer.parseInt(val));
                        highlightTop = stack.size() - 1;
                        repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number");
                    }
                }
            });
            
            popBtn.addActionListener(e -> {
                if (!stack.isEmpty()) {
                    undoStack.push(stack.pop());
                    highlightTop = stack.isEmpty() ? -1 : stack.size() - 1;
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Stack is empty");
                }
            });
            
            peekBtn.addActionListener(e -> {
                if (!stack.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Top element: " + stack.peek());
                } else {
                    JOptionPane.showMessageDialog(this, "Stack is empty");
                }
            });
            
            undoBtn.addActionListener(e -> {
                if (!undoStack.isEmpty()) {
                    Integer val = undoStack.pop();
                    if (val != null) stack.push(val);
                    else if (!stack.isEmpty()) stack.pop();
                    highlightTop = stack.isEmpty() ? -1 : stack.size() - 1;
                    repaint();
                }
            });
            
            clearBtn.addActionListener(e -> {
                stack.clear();
                undoStack.clear();
                highlightTop = -1;
                repaint();
            });
            
            randomBtn.addActionListener(e -> {
                int val = 1 + new Random().nextInt(99);
                stack.push(val);
                undoStack.push(null);
                highlightTop = stack.size() - 1;
                repaint();
            });
            
            controls.add(pushBtn);
            controls.add(popBtn);
            controls.add(peekBtn);
            controls.add(undoBtn);
            controls.add(clearBtn);
            controls.add(randomBtn);
            
            add(controls, BorderLayout.SOUTH);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int x = getWidth() / 2 - 40;
            int y = getHeight() - 80;
            
            g2.setColor(mainFrame.getFgColor());
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Stack (Top: " + (stack.isEmpty() ? "Empty" : stack.peek()) + ")", 20, 30);
            g2.drawString("Size: " + stack.size(), 20, 55);
            
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(x - 20, y + 40, x - 20, y - stack.size() * 45);
            g2.drawLine(x + 100, y + 40, x + 100, y - stack.size() * 45);
            g2.drawLine(x - 20, y + 40, x + 100, y + 40);
            
            for (int k = stack.size() - 1; k >= 0; k--) {
                Color c = (k == highlightTop) ? Color.RED : mainFrame.getAccentColor();
                draw3DRect(g2, x, y - 30, 80, 30, c);
                drawCenteredString(g2, stack.get(k).toString(), x, y - 30, 80, 30);
                y -= 40;
            }
            
            if (stack.isEmpty()) {
                g2.setColor(mainFrame.getFgColor());
                g2.drawString("Stack is empty", x + 10, y + 20);
            }
        }
        
        private void draw3DRect(Graphics2D g2, int x, int y, int w, int h, Color base) {
            GradientPaint gp = new GradientPaint(x, y, base.brighter(), x + w, y + h, base.darker());
            g2.setPaint(gp);
            g2.fillRoundRect(x, y, w, h, 10, 10);
        }
        
        private void drawCenteredString(Graphics2D g2, String val, int x, int y, int w, int h) {
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            int strW = g2.getFontMetrics().stringWidth(val);
            int strH = g2.getFontMetrics().getHeight();
            g2.drawString(val, x + (w - strW) / 2, y + (h + strH) / 2 - 5);
        }
    }
    
    // =================== ENHANCED QUEUE PANEL ===================
    static class EnhancedQueuePanel extends JPanel {
        private DSAVisualizerUltraPro mainFrame;
        private Queue<Integer> queue = new LinkedList<>();
        private Queue<Integer> undoQueue = new LinkedList<>();
        
        public EnhancedQueuePanel(DSAVisualizerUltraPro frame) {
            this.mainFrame = frame;
            setBackground(frame.getBgColor());
            
            JPanel controls = new JPanel();
            controls.setBackground(frame.getBgColor());
            
            JButton enqueueBtn = new JButton("Enqueue");
            JButton dequeueBtn = new JButton("Dequeue");
            JButton frontBtn = new JButton("Front");
            JButton rearBtn = new JButton("Rear");
            JButton undoBtn = new JButton("Undo");
            JButton clearBtn = new JButton("Clear");
            JButton randomBtn = new JButton("Random");
            
            enqueueBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value:");
                if (val != null && !val.isEmpty()) {
                    try {
                        queue.add(Integer.parseInt(val));
                        repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number");
                    }
                }
            });
            
            dequeueBtn.addActionListener(e -> {
                if (!queue.isEmpty()) {
                    undoQueue.add(queue.poll());
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Queue is empty");
                }
            });
            
            frontBtn.addActionListener(e -> {
                if (!queue.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Front: " + queue.peek());
                }
            });
            
            rearBtn.addActionListener(e -> {
                if (!queue.isEmpty()) {
                    Integer[] arr = queue.toArray(new Integer[0]);
                    JOptionPane.showMessageDialog(this, "Rear: " + arr[arr.length - 1]);
                }
            });
            
            undoBtn.addActionListener(e -> {
                if (!undoQueue.isEmpty()) {
                    queue.add(undoQueue.poll());
                    repaint();
                }
            });
            
            clearBtn.addActionListener(e -> {
                queue.clear();
                undoQueue.clear();
                repaint();
            });
            
            randomBtn.addActionListener(e -> {
                queue.add(1 + new Random().nextInt(99));
                repaint();
            });
            
            controls.add(enqueueBtn);
            controls.add(dequeueBtn);
            controls.add(frontBtn);
            controls.add(rearBtn);
            controls.add(undoBtn);
            controls.add(clearBtn);
            controls.add(randomBtn);
            
            add(controls, BorderLayout.SOUTH);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(mainFrame.getFgColor());
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Queue (Front -> Rear)", 20, 30);
            g2.drawString("Size: " + queue.size(), 20, 55);
            
            int x = 50;
            int y = getHeight() / 2;
            int idx = 0;
            
            for (Integer val : queue) {
                Color c = (idx == 0) ? Color.RED : (idx == queue.size() - 1) ? Color.GREEN : mainFrame.getAccentColor();
                draw3DRect(g2, x, y, 60, 40, c);
                drawCenteredString(g2, val.toString(), x, y, 60, 40);
                
                if (idx == 0) {
                    g2.setColor(Color.RED);
                    g2.drawString("F", x + 25, y - 10);
                }
                if (idx == queue.size() - 1 && queue.size() > 1) {
                    g2.setColor(Color.GREEN);
                    g2.drawString("R", x + 25, y + 60);
                }
                
                x += 80;
                idx++;
            }
        }
        
        private void draw3DRect(Graphics2D g2, int x, int y, int w, int h, Color base) {
            GradientPaint gp = new GradientPaint(x, y, base.brighter(), x + w, y + h, base.darker());
            g2.setPaint(gp);
            g2.fillRoundRect(x, y, w, h, 10, 10);
        }
        
        private void drawCenteredString(Graphics2D g2, String val, int x, int y, int w, int h) {
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            int strW = g2.getFontMetrics().stringWidth(val);
            int strH = g2.getFontMetrics().getHeight();
            g2.drawString(val, x + (w - strW) / 2, y + (h + strH) / 2 - 5);
        }
    }
    
    // =================== CIRCULAR QUEUE PANEL ===================
    static class CircularQueuePanel extends JPanel {
        private DSAVisualizerUltraPro mainFrame;
        private int[] queue = new int[10];
        private int front = -1;
        private int rear = -1;
        private int size = 0;
        private int maxSize = 10;
        
        public CircularQueuePanel(DSAVisualizerUltraPro frame) {
            this.mainFrame = frame;
            setBackground(frame.getBgColor());
            
            JPanel controls = new JPanel();
            controls.setBackground(frame.getBgColor());
            
            JButton enqueueBtn = new JButton("Enqueue");
            JButton dequeueBtn = new JButton("Dequeue");
            JButton clearBtn = new JButton("Clear");
            JButton randomBtn = new JButton("Random");
            
            enqueueBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value:");
                if (val != null && !val.isEmpty()) {
                    try {
                        int num = Integer.parseInt(val);
                        if (size < maxSize) {
                            if (front == -1) front = 0;
                            rear = (rear + 1) % maxSize;
                            queue[rear] = num;
                            size++;
                            repaint();
                        } else {
                            JOptionPane.showMessageDialog(this, "Queue is full");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number");
                    }
                }
            });
            
            dequeueBtn.addActionListener(e -> {
                if (size > 0) {
                    queue[front] = 0;
                    front = (front + 1) % maxSize;
                    size--;
                    if (size == 0) {
                        front = -1;
                        rear = -1;
                    }
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Queue is empty");
                }
            });
            
            clearBtn.addActionListener(e -> {
                queue = new int[10];
                front = -1;
                rear = -1;
                size = 0;
                repaint();
            });
            
            randomBtn.addActionListener(e -> {
                if (size < maxSize) {
                    int val = 1 + new Random().nextInt(99);
                    if (front == -1) front = 0;
                    rear = (rear + 1) % maxSize;
                    queue[rear] = val;
                    size++;
                    repaint();
                }
            });
            
            controls.add(enqueueBtn);
            controls.add(dequeueBtn);
            controls.add(clearBtn);
            controls.add(randomBtn);
            
            add(controls, BorderLayout.SOUTH);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            int radius = 150;
            
            g2.setColor(mainFrame.getFgColor());
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Circular Queue - Size: " + size + "/" + maxSize, 20, 30);
            
            g2.setStroke(new BasicStroke(4));
            g2.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);
            
            double angle = 2 * Math.PI / maxSize;
            for (int i = 0; i < maxSize; i++) {
                double a = i * angle - Math.PI / 2;
                int x = cx + (int) (radius * Math.cos(a));
                int y = cy + (int) (radius * Math.sin(a));
                
                Color c = (i == front) ? Color.RED : (i == rear && size > 0) ? Color.GREEN : mainFrame.getBgColor();
                if (i < size) {
                    int idx = (front + i) % maxSize;
                    c = mainFrame.getAccentColor();
                    if (idx == front) c = Color.RED;
                    if (idx == rear) c = Color.GREEN;
                }
                
                GradientPaint gp = new GradientPaint(x - 25, y - 20, c.brighter(), x + 25, y + 20, c.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(x - 25, y - 20, 50, 40, 10, 10);
                
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                String val = (i < size) ? String.valueOf(queue[(front + i) % maxSize]) : "";
                int strW = g2.getFontMetrics().stringWidth(val);
                g2.drawString(val, x - strW / 2, y + 5);
                
                g2.setColor(mainFrame.getFgColor());
                g2.drawString(String.valueOf(i), x - 3, y + 35);
            }
            
            g2.setColor(Color.RED);
            g2.drawString("F", cx, cy - radius - 15);
            g2.setColor(Color.GREEN);
            g2.drawString("R", cx + radius + 20, cy);
        }
    }
    
    // =================== ENHANCED LINKED LIST PANEL ===================
    static class EnhancedLinkedListPanel extends JPanel {
        private DSAVisualizerUltraPro mainFrame;
        private LinkedList<Integer> list = new LinkedList<>();
        
        public EnhancedLinkedListPanel(DSAVisualizerUltraPro frame) {
            this.mainFrame = frame;
            setBackground(frame.getBgColor());
            
            JPanel controls = new JPanel();
            controls.setBackground(frame.getBgColor());
            
            JButton insertHeadBtn = new JButton("Insert Head");
            JButton insertTailBtn = new JButton("Insert Tail");
            JButton insertPosBtn = new JButton("Insert Position");
            JButton deleteBtn = new JButton("Delete");
            JButton searchBtn = new JButton("Search");
            JButton clearBtn = new JButton("Clear");
            JButton reverseBtn = new JButton("Reverse");
            
            insertHeadBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value:");
                if (val != null && !val.isEmpty()) {
                    try {
                        list.addFirst(Integer.parseInt(val));
                        repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number");
                    }
                }
            });
            
            insertTailBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value:");
                if (val != null && !val.isEmpty()) {
                    try {
                        list.addLast(Integer.parseInt(val));
                        repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number");
                    }
                }
            });
            
            insertPosBtn.addActionListener(e -> {
                try {
                    String posStr = JOptionPane.showInputDialog(this, "Enter position:");
                    String valStr = JOptionPane.showInputDialog(this, "Enter value:");
                    int pos = Integer.parseInt(posStr);
                    int val = Integer.parseInt(valStr);
                    if (pos >= 0 && pos <= list.size()) {
                        list.add(pos, val);
                        repaint();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input");
                }
            });
            
            deleteBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value to delete:");
                if (val != null && !val.isEmpty()) {
                    try {
                        list.remove(Integer.parseInt(val));
                        repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Value not found");
                    }
                }
            });
            
            searchBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value to search:");
                if (val != null && !val.isEmpty()) {
                    int idx = list.indexOf(Integer.parseInt(val));
                    if (idx >= 0) {
                        JOptionPane.showMessageDialog(this, "Found at index: " + idx);
                    } else {
                        JOptionPane.showMessageDialog(this, "Not found");
                    }
                }
            });
            
            clearBtn.addActionListener(e -> {
                list.clear();
                repaint();
            });
            
            reverseBtn.addActionListener(e -> {
                Collections.reverse(list);
                repaint();
            });
            
            controls.add(insertHeadBtn);
            controls.add(insertTailBtn);
            controls.add(insertPosBtn);
            controls.add(deleteBtn);
            controls.add(searchBtn);
            controls.add(clearBtn);
            controls.add(reverseBtn);
            
            add(controls, BorderLayout.SOUTH);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(mainFrame.getFgColor());
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Linked List - Size: " + list.size(), 20, 30);
            
            int x = 50;
            int y = getHeight() / 2;
            
            if (!list.isEmpty()) {
                g2.setColor(Color.YELLOW);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.drawString("HEAD", x - 10, y - 30);
            }
            
            for (int i = 0; i < list.size(); i++) {
                Color c = (i == 0) ? Color.YELLOW : mainFrame.getAccentColor();
                draw3DRect(g2, x, y, 60, 40, c);
                drawCenteredString(g2, list.get(i).toString(), x, y, 60, 40);
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.drawString("[" + i + "]", x + 20, y + 55);
                
                if (i < list.size() - 1) {
                    g2.setStroke(new BasicStroke(2));
                    g2.drawLine(x + 60, y + 20, x + 80, y + 20);
                    g2.drawLine(x + 75, y + 15, x + 80, y + 20);
                    g2.drawLine(x + 75, y + 25, x + 80, y + 20);
                }
                
                x += 90;
            }
            
            if (!list.isEmpty()) {
                g2.setColor(Color.GREEN);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.drawString("TAIL", x - 90, y - 30);
            }
        }
        
        private void draw3DRect(Graphics2D g2, int x, int y, int w, int h, Color base) {
            GradientPaint gp = new GradientPaint(x, y, base.brighter(), x + w, y + h, base.darker());
            g2.setPaint(gp);
            g2.fillRoundRect(x, y, w, h, 10, 10);
        }
        
        private void drawCenteredString(Graphics2D g2, String val, int x, int y, int w, int h) {
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            int strW = g2.getFontMetrics().stringWidth(val);
            int strH = g2.getFontMetrics().getHeight();
            g2.drawString(val, x + (w - strW) / 2, y + (h + strH) / 2 - 5);
        }
    }
    
    // =================== BINARY SEARCH TREE PANEL ===================
    static class BSTPanel extends JPanel implements ActionListener {
        private DSAVisualizerUltraPro mainFrame;
        private BSTNode root = null;
        private BSTNode highlight = null;
        private Timer animTimer;
        private String animState = "";
        private int animValue = -1;
        
        public BSTPanel(DSAVisualizerUltraPro frame) {
            this.mainFrame = frame;
            setBackground(frame.getBgColor());
            
            JPanel controls = new JPanel();
            controls.setBackground(frame.getBgColor());
            
            JButton insertBtn = new JButton("Insert");
            JButton deleteBtn = new JButton("Delete");
            JButton searchBtn = new JButton("Search");
            JButton bfsBtn = new JButton("BFS Traversal");
            JButton dfsBtn = new JButton("DFS Traversal");
            JButton clearBtn = new JButton("Clear");
            JButton randomBtn = new JButton("Random Insert");
            
            insertBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value:");
                if (val != null && !val.isEmpty()) {
                    try {
                        int num = Integer.parseInt(val);
                        root = insert(root, num);
                        highlightNode(num);
                        repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number");
                    }
                }
            });
            
            deleteBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value to delete:");
                if (val != null && !val.isEmpty()) {
                    try {
                        root = delete(root, Integer.parseInt(val));
                        highlight = null;
                        repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Value not found");
                    }
                }
            });
            
            searchBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value to search:");
                if (val != null && !val.isEmpty()) {
                    try {
                        BSTNode result = search(root, Integer.parseInt(val));
                        if (result != null) {
                            JOptionPane.showMessageDialog(this, "Found: " + result.value);
                            highlight = result;
                        } else {
                            JOptionPane.showMessageDialog(this, "Not found");
                        }
                        repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number");
                    }
                }
            });
            
            bfsBtn.addActionListener(e -> {
                List<Integer> result = bfs();
                JOptionPane.showMessageDialog(this, "BFS: " + result);
            });
            
            dfsBtn.addActionListener(e -> {
                List<Integer> result = new ArrayList<>();
                dfs(root, result);
                JOptionPane.showMessageDialog(this, "DFS: " + result);
            });
            
            clearBtn.addActionListener(e -> {
                root = null;
                highlight = null;
                repaint();
            });
            
            randomBtn.addActionListener(e -> {
                int val = 1 + new Random().nextInt(50);
                root = insert(root, val);
                highlightNode(val);
                repaint();
            });
            
            controls.add(insertBtn);
            controls.add(deleteBtn);
            controls.add(searchBtn);
            controls.add(bfsBtn);
            controls.add(dfsBtn);
            controls.add(clearBtn);
            controls.add(randomBtn);
            
            add(controls, BorderLayout.SOUTH);
        }
        
        private void highlightNode(int val) {
            animTimer = new Timer(200, this);
            animValue = val;
            animTimer.start();
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            highlight = search(root, animValue);
            repaint();
            animTimer.stop();
        }
        
        private BSTNode insert(BSTNode node, int val) {
            if (node == null) return new BSTNode(val);
            if (val < node.value) node.left = insert(node.left, val);
            else if (val > node.value) node.right = insert(node.right, val);
            return node;
        }
        
        private BSTNode delete(BSTNode node, int val) {
            if (node == null) return null;
            if (val < node.value) node.left = delete(node.left, val);
            else if (val > node.value) node.right = delete(node.right, val);
            else {
                if (node.left == null) return node.right;
                if (node.right == null) return node.left;
                node.value = minValue(node.right);
                node.right = delete(node.right, node.value);
            }
            return node;
        }
        
        private int minValue(BSTNode node) {
            int min = node.value;
            while (node.left != null) {
                min = node.left.value;
                node = node.left;
            }
            return min;
        }
        
        private BSTNode search(BSTNode node, int val) {
            if (node == null || node.value == val) return node;
            if (val < node.value) return search(node.left, val);
            return search(node.right, val);
        }
        
        private List<Integer> bfs() {
            List<Integer> result = new ArrayList<>();
            if (root == null) return result;
            Queue<BSTNode> q = new LinkedList<>();
            q.add(root);
            while (!q.isEmpty()) {
                BSTNode node = q.poll();
                result.add(node.value);
                if (node.left != null) q.add(node.left);
                if (node.right != null) q.add(node.right);
            }
            return result;
        }
        
        private void dfs(BSTNode node, List<Integer> result) {
            if (node != null) {
                dfs(node.left, result);
                result.add(node.value);
                dfs(node.right, result);
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(mainFrame.getFgColor());
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Binary Search Tree", 20, 30);
            
            if (root != null) {
                drawTree(g2, root, getWidth() / 2, 60, getWidth() / 4);
            }
        }
        
        private void drawTree(Graphics2D g2, BSTNode node, int x, int y, int offset) {
            if (node == null) return;
            
            if (node.left != null) {
                g2.setStroke(new BasicStroke(2));
                g2.setColor(mainFrame.getFgColor());
                g2.drawLine(x, y, x - offset, y + 60);
                drawTree(g2, node.left, x - offset, y + 60, offset / 2);
            }
            
            if (node.right != null) {
                g2.setStroke(new BasicStroke(2));
                g2.setColor(mainFrame.getFgColor());
                g2.drawLine(x, y, x + offset, y + 60);
                drawTree(g2, node.right, x + offset, y + 60, offset / 2);
            }
            
            Color c = (node == highlight) ? Color.RED : mainFrame.getAccentColor();
            draw3DCircle(g2, x, y, 25, c);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            String val = String.valueOf(node.value);
            int strW = g2.getFontMetrics().stringWidth(val);
            g2.drawString(val, x - strW / 2, y + 5);
        }
        
        private void draw3DCircle(Graphics2D g2, int x, int y, int r, Color base) {
            GradientPaint gp = new GradientPaint(x - r, y - r, base.brighter(), x + r, y + r, base.darker());
            g2.setPaint(gp);
            g2.fillOval(x - r, y - r, r * 2, r * 2);
        }
        
        static class BSTNode {
            int value;
            BSTNode left, right;
            BSTNode(int v) { value = v; }
        }
    }
    
    // =================== HEAP PANEL ===================
    static class HeapPanel extends JPanel implements ActionListener {
        private DSAVisualizerUltraPro mainFrame;
        private int[] heap = new int[0];
        private boolean isMinHeap = false;
        private Timer timer;
        private int highlight1 = -1, highlight2 = -1;
        
        public HeapPanel(DSAVisualizerUltraPro frame) {
            this.mainFrame = frame;
            setBackground(frame.getBgColor());
            
            JPanel controls = new JPanel();
            controls.setBackground(frame.getBgColor());
            
            JButton insertBtn = new JButton("Insert");
            JButton extractBtn = new JButton("Extract Max");
            JButton minExtractBtn = new JButton("Extract Min");
            JButton heapifyBtn = new JButton("Heapify");
            JButton clearBtn = new JButton("Clear");
            JButton randomBtn = new JButton("Random");
            
            insertBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value:");
                if (val != null && !val.isEmpty()) {
                    try {
                        int num = Integer.parseInt(val);
                        insert(num);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number");
                    }
                }
            });
            
            extractBtn.addActionListener(e -> {
                if (heap.length > 0) {
                    extractMax();
                } else {
                    JOptionPane.showMessageDialog(this, "Heap is empty");
                }
            });
            
            minExtractBtn.addActionListener(e -> {
                if (heap.length > 0) {
                    extractMin();
                } else {
                    JOptionPane.showMessageDialog(this, "Heap is empty");
                }
            });
            
            heapifyBtn.addActionListener(e -> {
                String input = JOptionPane.showInputDialog(this, "Enter numbers separated by comma:");
                if (input != null && !input.isEmpty()) {
                    String[] parts = input.split(",");
                    heap = new int[parts.length];
                    for (int i = 0; i < parts.length; i++) {
                        heap[i] = Integer.parseInt(parts[i].trim());
                    }
                    for (int i = heap.length / 2 - 1; i >= 0; i--) {
                        heapify(heap.length, i);
                    }
                    repaint();
                }
            });
            
            clearBtn.addActionListener(e -> {
                heap = new int[0];
                repaint();
            });
            
            randomBtn.addActionListener(e -> {
                insert(1 + new Random().nextInt(50));
            });
            
            controls.add(insertBtn);
            controls.add(extractBtn);
            controls.add(minExtractBtn);
            controls.add(heapifyBtn);
            controls.add(clearBtn);
            controls.add(randomBtn);
            
            add(controls, BorderLayout.SOUTH);
        }
        
        private void insert(int val) {
            int[] newHeap = new int[heap.length + 1];
            System.arraycopy(heap, 0, newHeap, 0, heap.length);
            newHeap[heap.length] = val;
            heap = newHeap;
            heapifyUp(heap.length - 1);
            repaint();
        }
        
        private void extractMax() {
            if (heap.length == 0) return;
            int max = heap[0];
            heap[0] = heap[heap.length - 1];
            int[] newHeap = new int[heap.length - 1];
            System.arraycopy(heap, 0, newHeap, 0, heap.length - 1);
            heap = newHeap;
            if (heap.length > 0) heapifyDown(0);
            JOptionPane.showMessageDialog(this, "Extracted: " + max);
            repaint();
        }
        
        private void extractMin() {
            if (heap.length == 0) return;
            int min = heap[heap.length - 1];
            int[] newHeap = new int[heap.length - 1];
            System.arraycopy(heap, 0, newHeap, 0, heap.length - 1);
            heap = newHeap;
            repaint();
        }
        
        private void heapifyUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap[i] > heap[parent]) {
                    highlight1 = i; highlight2 = parent;
                    int temp = heap[i];
                    heap[i] = heap[parent];
                    heap[parent] = temp;
                    i = parent;
                } else break;
            }
        }
        
        private void heapifyDown(int i) {
            int largest = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            
            if (left < heap.length && heap[left] > heap[largest]) largest = left;
            if (right < heap.length && heap[right] > heap[largest]) largest = right;
            
            if (largest != i) {
                highlight1 = i; highlight2 = largest;
                int temp = heap[i];
                heap[i] = heap[largest];
                heap[largest] = temp;
                heapifyDown(largest);
            }
        }
        
        private void heapify(int n, int i) {
            int largest = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            
            if (left < n && heap[left] > heap[largest]) largest = left;
            if (right < n && heap[right] > heap[largest]) largest = right;
            
            if (largest != i) {
                int temp = heap[i];
                heap[i] = heap[largest];
                heap[largest] = temp;
                heapify(n, largest);
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            highlight1 = -1;
            highlight2 = -1;
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(mainFrame.getFgColor());
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Max Heap", 20, 30);
            g2.drawString("Size: " + heap.length, 20, 55);
            
            if (heap.length > 0) {
                drawHeap(g2, 0, getWidth() / 2, 50, getWidth() / 4);
            }
        }
        
        private void drawHeap(Graphics2D g2, int i, int x, int y, int offset) {
            if (i >= heap.length) return;
            
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            
            if (left < heap.length) {
                g2.setStroke(new BasicStroke(2));
                g2.setColor(mainFrame.getFgColor());
                g2.drawLine(x, y, x - offset, y + 60);
                drawHeap(g2, left, x - offset, y + 60, offset / 2);
            }
            
            if (right < heap.length) {
                g2.setStroke(new BasicStroke(2));
                g2.setColor(mainFrame.getFgColor());
                g2.drawLine(x, y, x + offset, y + 60);
                drawHeap(g2, right, x + offset, y + 60, offset / 2);
            }
            
            Color c = (i == highlight1 || i == highlight2) ? Color.RED : mainFrame.getAccentColor();
            GradientPaint gp = new GradientPaint(x - 20, y - 20, c.brighter(), x + 20, y + 20, c.darker());
            g2.setPaint(gp);
            g2.fillOval(x - 20, y - 20, 40, 40);
            
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            String val = String.valueOf(heap[i]);
            int strW = g2.getFontMetrics().stringWidth(val);
            g2.drawString(val, x - strW / 2, y + 5);
        }
    }
    
    // =================== HASH TABLE PANEL ===================
    static class HashTablePanel extends JPanel implements ActionListener {
        private DSAVisualizerUltraPro mainFrame;
        private Map<Integer, List<Integer>> hashTable = new HashMap<>();
        private int tableSize = 10;
        private int highlight = -1;
        
        public HashTablePanel(DSAVisualizerUltraPro frame) {
            this.mainFrame = frame;
            setBackground(frame.getBgColor());
            
            JPanel controls = new JPanel();
            controls.setBackground(frame.getBgColor());
            
            JButton insertBtn = new JButton("Insert");
            JButton searchBtn = new JButton("Search");
            JButton deleteBtn = new JButton("Delete");
            JButton clearBtn = new JButton("Clear");
            JButton randomBtn = new JButton("Random");
            
            insertBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value:");
                if (val != null && !val.isEmpty()) {
                    try {
                        int num = Integer.parseInt(val);
                        insert(num);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number");
                    }
                }
            });
            
            searchBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value to search:");
                if (val != null && !val.isEmpty()) {
                    int num = Integer.parseInt(val);
                    int hash = num % tableSize;
                    highlight = hash;
                    repaint();
                    if (hashTable.containsKey(hash) && hashTable.get(hash).contains(num)) {
                        JOptionPane.showMessageDialog(this, "Found at index " + hash + " (collision handling: Chaining)");
                    } else {
                        JOptionPane.showMessageDialog(this, "Not found");
                    }
                }
            });
            
            deleteBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value to delete:");
                if (val != null && !val.isEmpty()) {
                    int num = Integer.parseInt(val);
                    int hash = num % tableSize;
                    if (hashTable.containsKey(hash)) {
                        hashTable.get(hash).remove((Integer) num);
                        if (hashTable.get(hash).isEmpty()) hashTable.remove(hash);
                    }
                    repaint();
                }
            });
            
            clearBtn.addActionListener(e -> {
                hashTable.clear();
                repaint();
            });
            
            randomBtn.addActionListener(e -> {
                insert(1 + new Random().nextInt(99));
            });
            
            controls.add(insertBtn);
            controls.add(searchBtn);
            controls.add(deleteBtn);
            controls.add(clearBtn);
            controls.add(randomBtn);
            
            add(controls, BorderLayout.SOUTH);
        }
        
        private void insert(int val) {
            int hash = val % tableSize;
            hashTable.computeIfAbsent(hash, k -> new ArrayList<>()).add(val);
            highlight = hash;
            repaint();
            Timer t = new Timer(500, this);
            t.setRepeats(false);
            t.start();
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            highlight = -1;
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(mainFrame.getFgColor());
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Hash Table (Chaining) - Size: " + tableSize, 20, 30);
            g2.drawString("Hash Function: key % " + tableSize, 20, 55);
            
            int cellWidth = 80;
            int startX = (getWidth() - tableSize * cellWidth) / 2;
            int y = 80;
            
            for (int i = 0; i < tableSize; i++) {
                int x = startX + i * cellWidth;
                
                Color c = (i == highlight) ? Color.RED : (hashTable.containsKey(i)) ? mainFrame.getAccentColor() : Color.GRAY;
                GradientPaint gp = new GradientPaint(x, y, c.brighter(), x + cellWidth - 10, y + 30, c.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(x, y, cellWidth - 10, 30, 5, 5);
                
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.drawString(String.valueOf(i), x + 10, y + 20);
                
                g2.setColor(mainFrame.getFgColor());
                g2.setFont(new Font("Arial", Font.PLAIN, 11));
                if (hashTable.containsKey(i)) {
                    String values = hashTable.get(i).toString();
                    g2.drawString(values, x, y + 50);
                }
                
                g2.setColor(mainFrame.getFgColor());
                g2.drawString("->", x + 15, y + 20);
            }
        }
    }
    
    // =================== GRAPH PANEL ===================
    static class GraphPanel extends JPanel implements ActionListener {
        private DSAVisualizerUltraPro mainFrame;
        private Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
        private List<Integer> bfsResult = new ArrayList<>();
        private List<Integer> dfsResult = new ArrayList<>();
        private Set<Integer> visited = new HashSet<>();
        private Timer animTimer;
        private boolean animating = false;
        
        public GraphPanel(DSAVisualizerUltraPro frame) {
            this.mainFrame = frame;
            setBackground(frame.getBgColor());
            
            JPanel controls = new JPanel();
            controls.setBackground(frame.getBgColor());
            
            JButton addNodeBtn = new JButton("Add Node");
            JButton addEdgeBtn = new JButton("Add Edge");
            JButton bfsBtn = new JButton("BFS");
            JButton dfsBtn = new JButton("DFS");
            JButton clearBtn = new JButton("Clear");
            JButton randomBtn = new JButton("Random Graph");
            
            addNodeBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter node value:");
                if (val != null && !val.isEmpty()) {
                    adjacencyList.putIfAbsent(Integer.parseInt(val), new ArrayList<>());
                    repaint();
                }
            });
            
            addEdgeBtn.addActionListener(e -> {
                String from = JOptionPane.showInputDialog(this, "Enter source node:");
                String to = JOptionPane.showInputDialog(this, "Enter destination node:");
                if (from != null && to != null) {
                    int f = Integer.parseInt(from);
                    int t = Integer.parseInt(to);
                    adjacencyList.computeIfAbsent(f, k -> new ArrayList<>()).add(t);
                    adjacencyList.computeIfAbsent(t, k -> new ArrayList<>()).add(f);
                    repaint();
                }
            });
            
            bfsBtn.addActionListener(e -> {
                String start = JOptionPane.showInputDialog(this, "Enter start node:");
                if (start != null) {
                    bfsResult.clear();
                    bfs(Integer.parseInt(start));
                }
            });
            
            dfsBtn.addActionListener(e -> {
                String start = JOptionPane.showInputDialog(this, "Enter start node:");
                if (start != null) {
                    dfsResult.clear();
                    visited.clear();
                    dfs(Integer.parseInt(start));
                }
            });
            
            clearBtn.addActionListener(e -> {
                adjacencyList.clear();
                bfsResult.clear();
                dfsResult.clear();
                visited.clear();
                repaint();
            });
            
            randomBtn.addActionListener(e -> {
                adjacencyList.clear();
                Random rand = new Random();
                int nodes = 5 + rand.nextInt(3);
                for (int i = 0; i < nodes; i++) {
                    adjacencyList.put(i, new ArrayList<>());
                }
                for (int i = 0; i < nodes; i++) {
                    for (int j = i + 1; j < nodes; j++) {
                        if (rand.nextBoolean()) {
                            adjacencyList.get(i).add(j);
                            adjacencyList.get(j).add(i);
                        }
                    }
                }
                repaint();
            });
            
            controls.add(addNodeBtn);
            controls.add(addEdgeBtn);
            controls.add(bfsBtn);
            controls.add(dfsBtn);
            controls.add(clearBtn);
            controls.add(randomBtn);
            
            add(controls, BorderLayout.SOUTH);
        }
        
        private void bfs(int start) {
            Set<Integer> visited = new HashSet<>();
            Queue<Integer> q = new LinkedList<>();
            q.add(start);
            visited.add(start);
            
            while (!q.isEmpty()) {
                int node = q.poll();
                bfsResult.add(node);
                for (int neighbor : adjacencyList.getOrDefault(node, new ArrayList<>())) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        q.add(neighbor);
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "BFS: " + bfsResult);
            repaint();
        }
        
        private void dfs(int node) {
            if (visited.contains(node)) return;
            visited.add(node);
            dfsResult.add(node);
            for (int neighbor : adjacencyList.getOrDefault(node, new ArrayList<>())) {
                dfs(neighbor);
            }
            JOptionPane.showMessageDialog(this, "DFS: " + dfsResult);
            repaint();
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {}
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(mainFrame.getFgColor());
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Graph (Adjacency List)", 20, 30);
            
            if (adjacencyList.isEmpty()) return;
            
            Map<Integer, Point> positions = new HashMap<>();
            int n = adjacencyList.size();
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            int radius = Math.min(200, n * 40);
            
            int idx = 0;
            for (Integer node : adjacencyList.keySet()) {
                double angle = 2 * Math.PI * idx / n - Math.PI / 2;
                int x = cx + (int) (radius * Math.cos(angle));
                int y = cy + (int) (radius * Math.sin(angle));
                positions.put(node, new Point(x, y));
                idx++;
            }
            
            for (Integer node : adjacencyList.keySet()) {
                for (Integer neighbor : adjacencyList.get(node)) {
                    if (node < neighbor) {
                        Point p1 = positions.get(node);
                        Point p2 = positions.get(neighbor);
                        g2.setStroke(new BasicStroke(2));
                        g2.setColor(mainFrame.getFgColor());
                        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }
            
            for (Integer node : adjacencyList.keySet()) {
                Point p = positions.get(node);
                
                boolean inBfs = bfsResult.contains(node);
                boolean inDfs = dfsResult.contains(node);
                Color c = inBfs ? Color.GREEN : (inDfs ? Color.CYAN : mainFrame.getAccentColor());
                
                GradientPaint gp = new GradientPaint(p.x - 25, p.y - 25, c.brighter(), p.x + 25, p.y + 25, c.darker());
                g2.setPaint(gp);
                g2.fillOval(p.x - 25, p.y - 25, 50, 50);
                
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                String val = String.valueOf(node);
                int strW = g2.getFontMetrics().stringWidth(val);
                g2.drawString(val, p.x - strW / 2, p.y + 5);
            }
        }
        
        static class Point {
            int x, y;
            Point(int x, int y) { this.x = x; this.y = y; }
        }
    }
    
    // =================== PROBLEMS PANEL ===================
    static class ProblemsPanel extends JPanel {
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
            codeEditor.setForeground(frame.getAccentColor());
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
            
            rightPanel.add(infoPanel, BorderLayout.NORTH);
            rightPanel.add(descLabel, BorderLayout.NORTH);
            rightPanel.add(descScroll, BorderLayout.CENTER);
            
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
            String diff = problemInfo.contains("[") ? problemInfo.split("\\[")[1].replace("]", "") : "";
            
            for (List<Problem> problems : problemsByCategory.values()) {
                for (Problem p : problems) {
                    if (p.name.equals(name)) {
                        selectedProblem = p;
                        difficultyLabel.setText("Difficulty: " + p.difficulty);
                        complexityLabel.setText("Time Complexity: " + p.complexity);
                        problemDescription.setText("Problem: " + p.name + "\n\n" +
                            "Difficulty: " + p.difficulty + "\n" +
                            "Time Complexity: " + p.complexity + "\n\n" +
                            "Click 'Hint' for hints or 'Visualize' to see the algorithm visualization.\n" +
                            "Visit: " + p.link);
                        return;
                    }
                }
            }
        }
        
        private void runCode() {
            JOptionPane.showMessageDialog(this, "Running code...\n(Output will appear here)");
        }
        
        private void visualizeProblem() {
            if (selectedProblem != null) {
                String name = selectedProblem.name.toLowerCase();
                JOptionPane.showMessageDialog(this, 
                    "Visualization: " + selectedProblem.name + "\n\n" +
                    "This will open the appropriate visualization panel for this problem.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a problem first!");
            }
        }
        
        private void showHint() {
            if (selectedProblem != null) {
                String hint = getHintForProblem(selectedProblem.name);
                JOptionPane.showMessageDialog(this, "Hint:\n\n" + hint);
            }
        }
        
        private String getHintForProblem(String name) {
            Map<String, String> hints = new HashMap<>();
            hints.put("Pair with Target Sum", "Use two pointers, one at start and one at end. Sum them, if > target move end, if < target move start.");
            hints.put("LinkedList Cycle", "Use slow and fast pointers. If they meet, there's a cycle.");
            hints.put("Maximum Subarray", "Use Kadane's algorithm: keep track of current sum and max sum.");
            hints.put("Valid Parentheses", "Use a stack. Push opening brackets, pop and match for closing.");
            hints.put("Merge Intervals", "Sort intervals by start time, then merge overlapping ones.");
            hints.put("Reverse LinkedList", "Use three pointers: prev, current, next. Reverse links one by one.");
            hints.put("Middle of LinkedList", "Use slow and fast pointers. When fast reaches end, slow is at middle.");
            hints.put("Subsets", "Use backtracking or bit manipulation. For each element, add to all existing subsets.");
            
            return hints.getOrDefault(name, "Think about the core pattern. What data structure fits best?");
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
                "public int[] twoSum(int[] arr, int target) {\n" +
                "    int left = 0, right = arr.length - 1;\n" +
                "    while (left < right) {\n" +
                "        int sum = arr[left] + arr[right];\n" +
                "        if (sum == target) return new int[]{left, right};\n" +
                "        else if (sum < target) left++;\n" +
                "        else right--;\n" +
                "    }\n" +
                "    return new int[]{-1, -1};\n" +
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
    }
}
