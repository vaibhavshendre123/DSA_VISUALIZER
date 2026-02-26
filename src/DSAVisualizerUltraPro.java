import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DSAVisualizerUltraPro extends JFrame {
    private ThemeManager themeManager;
    private ThemeManager.Theme currentTheme;
    private JTabbedPane dataStructureTabs;
    private JComboBox<String> themeSelector;
    
    private StepByStepPanel stepPanel;
    private CodeTogglePanel codeTogglePanel;
    private AlgorithmComparisonPanel comparisonPanel;
    private PracticeModePanel practicePanel;
    
    private boolean isFocusMode = false;
    private int currentDataStructure = 0;
    
    private JButton btnCompare, btnPractice, btnFocus;
    private JList<String> dsList;
    private JSplitPane centerSplit, mainSplit;
    
    public DSAVisualizerUltraPro() {
        themeManager = new ThemeManager();
        currentTheme = themeManager.getTheme();
        
        setTitle("DSA Visualizer Ultra Pro");
        setSize(1400, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        applyTheme();
        setVisible(true);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(currentTheme.background);
        
        JPanel topBar = createTopBar();
        
        JPanel leftSidebar = createSidebar();
        
        JPanel visualizationPanel = createVisualizationPanel();
        
        JPanel codePanel = createCodePanel();
        
        centerSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, visualizationPanel, codePanel);
        centerSplit.setDividerLocation(550);
        centerSplit.setDividerSize(8);
        centerSplit.setResizeWeight(1.0);
        
        mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSidebar, centerSplit);
        mainSplit.setDividerLocation(250);
        mainSplit.setDividerSize(8);
        mainSplit.setResizeWeight(0.0);
        
        add(topBar, BorderLayout.NORTH);
        add(mainSplit, BorderLayout.CENTER);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout(20, 0));
        topBar.setBackground(currentTheme.panelBackground);
        topBar.setBorder(new EmptyBorder(12, 20, 12, 20));
        
        JLabel titleLabel = new JLabel("DSA Visualizer - Ultra Pro");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(currentTheme.foreground);
        
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        controlsPanel.setBackground(currentTheme.panelBackground);
        
        JLabel themeLabel = new JLabel("Theme:");
        themeLabel.setFont(new Font("Arial", Font.BOLD, 13));
        themeLabel.setForeground(currentTheme.textSecondary);
        
        themeSelector = new JComboBox<>(ThemeManager.getAllThemes());
        themeSelector.setSelectedItem(themeManager.getCurrentTheme());
        themeSelector.setFont(new Font("Arial", Font.PLAIN, 13));
        themeSelector.setPreferredSize(new Dimension(140, 30));
        themeSelector.addActionListener(e -> {
            String selected = (String) themeSelector.getSelectedItem();
            themeManager.setTheme(selected);
            currentTheme = themeManager.getTheme(selected);
            applyTheme();
        });
        
        btnCompare = createButton("Compare", currentTheme.success);
        btnCompare.addActionListener(e -> showComparisonPanel());
        
        btnPractice = createButton("Practice", new Color(245, 158, 11));
        btnPractice.addActionListener(e -> showPracticePanel());
        
        btnFocus = createButton("Focus Mode", currentTheme.accent);
        btnFocus.addActionListener(e -> enterFocusMode());
        
        controlsPanel.add(themeLabel);
        controlsPanel.add(themeSelector);
        controlsPanel.add(btnCompare);
        controlsPanel.add(btnPractice);
        controlsPanel.add(btnFocus);
        
        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(controlsPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 32));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private JButton createNavButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(90, 28));
        
        final Color baseColor = color;
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
        
        return button;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout(0, 0));
        sidebar.setBackground(currentTheme.panelBackgroundSecondary);
        sidebar.setBorder(null);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(currentTheme.panelBackgroundSecondary);
        headerPanel.setBorder(new EmptyBorder(15, 15, 10, 15));
        
        JLabel dsLabel = new JLabel("Data Structures");
        dsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dsLabel.setForeground(currentTheme.foreground);
        dsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel hintLabel = new JLabel("Select to visualize");
        hintLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        hintLabel.setForeground(currentTheme.textSecondary);
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        headerPanel.add(dsLabel);
        headerPanel.add(Box.createVerticalStrut(3));
        headerPanel.add(hintLabel);
        
        String[] dataStructures = {
            "Array Sorting",
            "Stack",
            "Queue", 
            "Circular Queue",
            "Linked List",
            "BST",
            "Heap",
            "Hash Table",
            "Graph",
            "Problems"
        };
        
        dsList = new JList<>(dataStructures);
        dsList.setFont(new Font("Arial", Font.PLAIN, 14));
        dsList.setBackground(currentTheme.panelBackground);
        dsList.setForeground(currentTheme.foreground);
        dsList.setSelectedIndex(0);
        dsList.setSelectionBackground(currentTheme.accent);
        dsList.setSelectionForeground(Color.WHITE);
        dsList.setBorder(null);
        
        dsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                currentDataStructure = dsList.getSelectedIndex();
                loadDataStructurePanel(currentDataStructure);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(dsList);
        scrollPane.setBackground(currentTheme.panelBackgroundSecondary);
        scrollPane.setBorder(null);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(currentTheme.panelBackgroundSecondary);
        bottomPanel.setBorder(new EmptyBorder(10, 15, 15, 15));
        
        JLabel stepsLabel = new JLabel("Step-by-Step");
        stepsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        stepsLabel.setForeground(currentTheme.foreground);
        stepsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        stepPanel = new StepByStepPanel(new ArrayList<>(Arrays.asList(
            "Initialize the array with given values",
            "Compare adjacent elements", 
            "Swap if elements are in wrong order",
            "Repeat until no swaps needed",
            "Array is now sorted"
        )));
        
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        navPanel.setBackground(currentTheme.panelBackgroundSecondary);
        
        JButton prevBtn = createNavButton("Previous", currentTheme.buttonBackground);
        prevBtn.addActionListener(e -> stepPanel.previousStep());
        
        JButton nextBtn = createNavButton("Next", currentTheme.buttonBackground);
        nextBtn.addActionListener(e -> stepPanel.nextStep());
        
        navPanel.add(prevBtn);
        navPanel.add(nextBtn);
        
        bottomPanel.add(stepsLabel);
        bottomPanel.add(Box.createVerticalStrut(8));
        bottomPanel.add(stepPanel);
        bottomPanel.add(Box.createVerticalStrut(8));
        bottomPanel.add(navPanel);
        
        sidebar.add(headerPanel, BorderLayout.NORTH);
        sidebar.add(scrollPane, BorderLayout.CENTER);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);
        
        return sidebar;
    }
    
    private JPanel createVisualizationPanel() {
        JPanel vizPanel = new JPanel(new BorderLayout());
        vizPanel.setBackground(currentTheme.background);
        
        dataStructureTabs = new JTabbedPane();
        dataStructureTabs.setFont(new Font("Arial", Font.BOLD, 13));
        
        dataStructureTabs.addTab("Array Sorting", new EnhancedArrayPanel(this));
        dataStructureTabs.addTab("Stack", new EnhancedStackPanel(this));
        dataStructureTabs.addTab("Queue", new EnhancedQueuePanel(this));
        dataStructureTabs.addTab("Circular Queue", new CircularQueuePanel(this));
        dataStructureTabs.addTab("Linked List", new EnhancedLinkedListPanel(this));
        dataStructureTabs.addTab("BST", new BSTPanel(this));
        dataStructureTabs.addTab("Heap", new HeapPanel(this));
        dataStructureTabs.addTab("Hash Table", new HashTablePanel(this));
        dataStructureTabs.addTab("Graph", new GraphPanel(this));
        dataStructureTabs.addTab("Problems", new ProblemsPanel(this));
        
        vizPanel.add(dataStructureTabs, BorderLayout.CENTER);
        
        return vizPanel;
    }
    
    private JPanel createCodePanel() {
        JPanel codePanelContainer = new JPanel(new BorderLayout());
        codePanelContainer.setBackground(currentTheme.background);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(currentTheme.panelBackgroundSecondary);
        headerPanel.setBorder(new EmptyBorder(8, 15, 8, 15));
        
        JLabel codeLabel = new JLabel("Pseudocode");
        codeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        codeLabel.setForeground(currentTheme.foreground);
        
        JLabel algoLabel = new JLabel("Currently: Bubble Sort");
        algoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        algoLabel.setForeground(currentTheme.textSecondary);
        
        headerPanel.add(codeLabel, BorderLayout.WEST);
        headerPanel.add(algoLabel, BorderLayout.EAST);
        
        codeTogglePanel = new CodeTogglePanel(
            "public void bubbleSort(int[] arr) {\n    int n = arr.length;\n    for (int i = 0; i < n-1; i++) {\n        for (int j = 0; j < n-i-1; j++) {\n            if (arr[j] > arr[j+1]) {\n                swap(arr[j], arr[j+1]);\n            }\n        }\n    }\n}",
            "bubbleSort(arr) {\n    n = arr.length\n    for (i = 0; i < n-1; i++) {\n        for (j = 0; j < n-i-1; j++) {\n            if (arr[j] > arr[j+1]) {\n                swap(arr[j], arr[j+1])\n            }\n        }\n    }\n}"
        );
        
        codePanelContainer.add(headerPanel, BorderLayout.NORTH);
        codePanelContainer.add(codeTogglePanel, BorderLayout.CENTER);
        
        return codePanelContainer;
    }
    
    private void loadDataStructurePanel(int index) {
        if (dataStructureTabs != null) {
            dataStructureTabs.setSelectedIndex(index);
        }
    }
    
    private void applyTheme() {
        currentTheme = themeManager.getTheme();
        
        setBackground(currentTheme.background);
        
        Container content = getContentPane();
        content.setBackground(currentTheme.background);
        
        for (Component comp : content.getComponents()) {
            applyThemeRecursive(comp);
        }
        
        if (btnCompare != null) btnCompare.setBackground(currentTheme.success);
        if (btnFocus != null) btnFocus.setBackground(currentTheme.accent);
        
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    private void applyThemeRecursive(Component comp) {
        if (comp instanceof JPanel) {
            JPanel panel = (JPanel) comp;
            panel.setBackground(currentTheme.panelBackground);
            for (Component child : panel.getComponents()) {
                applyThemeRecursive(child);
            }
        } else if (comp instanceof JLabel) {
            ((JLabel) comp).setForeground(currentTheme.foreground);
        } else if (comp instanceof JButton) {
            JButton btn = (JButton) comp;
            btn.setBackground(currentTheme.buttonBackground);
            btn.setForeground(currentTheme.foreground);
        } else if (comp instanceof JSplitPane) {
            comp.setBackground(currentTheme.background);
        } else if (comp instanceof JList) {
            JList<?> list = (JList<?>) comp;
            list.setBackground(currentTheme.panelBackground);
            list.setForeground(currentTheme.foreground);
            list.setSelectionBackground(currentTheme.accent);
            list.setSelectionForeground(Color.WHITE);
        } else if (comp instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) comp;
            sp.setBackground(currentTheme.panelBackgroundSecondary);
        }
        
        if (stepPanel != null) {
            stepPanel.applyTheme(currentTheme.background, currentTheme.foreground, currentTheme.panelBackground);
        }
        if (codeTogglePanel != null) {
            codeTogglePanel.applyTheme(currentTheme.background, currentTheme.foreground, currentTheme.panelBackground);
        }
    }
    
    private void enterFocusMode() {
        if (isFocusMode) return;
        isFocusMode = true;
        
        dispose();
        
        JFrame focusFrame = new JFrame("DSA Visualizer - Focus Mode");
        focusFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        focusFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel focusContainer = new JPanel(new BorderLayout());
        focusContainer.setBackground(currentTheme.background);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(currentTheme.panelBackground);
        topPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        JLabel titleLabel = new JLabel("Focus Mode - " + getCurrentDataStructureName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(currentTheme.foreground);
        
        JButton exitButton = createButton("Exit Focus Mode", new Color(239, 68, 68));
        exitButton.addActionListener(e -> exitFocusMode(focusFrame));
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(exitButton, BorderLayout.EAST);
        
        JPanel vizCopy = createVisualizationPanel();
        JPanel codeCopy = createCodePanel();
        
        JSplitPane centerSplitFocus = new JSplitPane(JSplitPane.VERTICAL_SPLIT, vizCopy, codeCopy);
        centerSplitFocus.setDividerLocation(600);
        
        focusContainer.add(topPanel, BorderLayout.NORTH);
        focusContainer.add(centerSplitFocus, BorderLayout.CENTER);
        
        focusFrame.add(focusContainer);
        focusFrame.setVisible(true);
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    exitFocusMode(focusFrame);
                    return true;
                }
                return false;
            }
        });
    }
    
    private void exitFocusMode(JFrame focusFrame) {
        isFocusMode = false;
        focusFrame.dispose();
        
        SwingUtilities.invokeLater(() -> {
            new DSAVisualizerUltraPro();
        });
    }
    
    private String getCurrentDataStructureName() {
        String[] names = {"Array Sorting", "Stack", "Queue", "Circular Queue", 
                         "Linked List", "BST", "Heap", "Hash Table", "Graph", "Problems"};
        return names[currentDataStructure];
    }
    
    private void showComparisonPanel() {
        JDialog dialog = new JDialog(this, "Algorithm Comparison", true);
        dialog.setSize(950, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(currentTheme.panelBackgroundSecondary);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Compare Sorting Algorithms");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(currentTheme.foreground);
        headerPanel.add(titleLabel);
        
        comparisonPanel = new AlgorithmComparisonPanel(
            "Bubble Sort",
            "public void bubbleSort(int[] arr) {\n    int n = arr.length;\n    for (int i = 0; i < n-1; i++) {\n        for (int j = 0; j < n-i-1; j++) {\n            if (arr[j] > arr[j+1]) {\n                int temp = arr[j];\n                arr[j] = arr[j+1];\n                arr[j+1] = temp;\n            }\n        }\n    }\n}\n\nTime: O(n2)\nSpace: O(1)",
            "Quick Sort",
            "public void quickSort(int[] arr, int low, int high) {\n    if (low < high) {\n        int pi = partition(arr, low, high);\n        quickSort(arr, low, pi - 1);\n        quickSort(arr, pi + 1, high);\n    }\n}\n\nTime: O(n log n) avg\nSpace: O(log n)"
        );
        
        comparisonPanel.applyTheme(currentTheme.background, currentTheme.foreground, currentTheme.panelBackground);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(headerPanel, BorderLayout.NORTH);
        wrapper.add(comparisonPanel, BorderLayout.CENTER);
        
        dialog.add(wrapper);
        dialog.setVisible(true);
    }
    
    private void showPracticePanel() {
        ArrayList<PracticeModePanel.PracticeQuestion> questions = new ArrayList<>();
        questions.add(new PracticeModePanel.PracticeQuestion(
            "What is the time complexity of binary search?",
            "O(log n)",
            "Binary search divides the search space in half each time.",
            "Think about how many times you can divide n by 2."
        ));
        questions.add(new PracticeModePanel.PracticeQuestion(
            "Which data structure uses LIFO principle?",
            "Stack",
            "Stack follows Last In First Out ordering.",
            "Think about a stack of plates."
        ));
        questions.add(new PracticeModePanel.PracticeQuestion(
            "What is the worst case of QuickSort?",
            "O(n^2)",
            "When the pivot is always the smallest or largest element.",
            "Consider what happens with a sorted array."
        ));
        questions.add(new PracticeModePanel.PracticeQuestion(
            "What data structure is used in BFS?",
            "Queue",
            "Breadth-First Search uses a Queue.",
            "Think about level-by-level traversal."
        ));
        
        practicePanel = new PracticeModePanel(questions);
        practicePanel.applyTheme(currentTheme.background, currentTheme.foreground, currentTheme.panelBackground);
        
        JDialog dialog = new JDialog(this, "Practice Mode", true);
        dialog.setSize(500, 420);
        dialog.setLocationRelativeTo(this);
        dialog.add(practicePanel);
        dialog.setVisible(true);
    }
    
    Color getBgColor() { return currentTheme.background; }
    Color getFgColor() { return currentTheme.foreground; }
    Color getAccentColor() { return currentTheme.accent; }
    boolean isDarkTheme() { return !currentTheme.name.equals("Light"); }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DSAVisualizerUltraPro::new);
    }
}