import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class FocusModePanel extends JPanel {
    private JButton enterFocusButton;
    private JPanel visualizationContainer;
    private boolean isFocusMode = false;
    private Runnable onExitCallback;

    public FocusModePanel(JPanel visualizationPanel) {
        this.visualizationContainer = visualizationPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(35, 45, 60));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(35, 45, 60));

        JLabel titleLabel = new JLabel("Full-Screen Focus Mode");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><center>Enter distraction-free mode to focus<br>on visualizing algorithms</center></html>");
        descLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        descLabel.setForeground(new Color(180, 180, 180));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        enterFocusButton = new JButton("Enter Focus Mode");
        enterFocusButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        enterFocusButton.setBackground(new Color(100, 149, 237));
        enterFocusButton.setForeground(Color.WHITE);
        enterFocusButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterFocusButton.setMaximumSize(new Dimension(250, 50));
        enterFocusButton.addActionListener(e -> enterFocusMode());

        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(descLabel);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(enterFocusButton);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void enterFocusMode() {
        if (isFocusMode) return;

        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame == null) return;

        isFocusMode = true;
        topFrame.dispose();

        JFrame focusFrame = new JFrame("DSA Visualizer - Focus Mode");
        focusFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        focusFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel focusContainer = new JPanel(new BorderLayout());
        focusContainer.setBackground(new Color(20, 25, 35));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(35, 45, 60));
        topPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Focus Mode - Distraction Free");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JButton exitButton = new JButton("Exit Focus Mode (ESC)");
        exitButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        exitButton.setBackground(new Color(220, 53, 69));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        exitButton.addActionListener(e -> exitFocusMode(focusFrame));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(exitButton, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBackground(new Color(20, 25, 35));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        if (visualizationContainer != null) {
            centerPanel.add(visualizationContainer);
        }

        JPanel codeContainer = new JPanel(new BorderLayout());
        codeContainer.setBackground(new Color(30, 40, 55));
        codeContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel codeLabel = new JLabel("Code / Explanation");
        codeLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        codeLabel.setForeground(Color.WHITE);
        
        JTextArea codeArea = new JTextArea();
        codeArea.setEditable(false);
        codeArea.setText("Algorithm code will appear here...\n\nUse this focused view to understand the algorithm step by step.");
        codeArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        codeArea.setBackground(new Color(40, 50, 65));
        codeArea.setForeground(Color.WHITE);
        
        JScrollPane codeScroll = new JScrollPane(codeArea);
        codeContainer.add(codeLabel, BorderLayout.NORTH);
        codeContainer.add(codeScroll, BorderLayout.CENTER);
        
        centerPanel.add(codeContainer);

        focusContainer.add(topPanel, BorderLayout.NORTH);
        focusContainer.add(centerPanel, BorderLayout.CENTER);

        focusFrame.add(focusContainer);
        focusFrame.setVisible(true);
        focusFrame.toFront();

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
        if (!isFocusMode) return;
        isFocusMode = false;
        focusFrame.dispose();

        SwingUtilities.invokeLater(() -> {
            new DSAVisualizerUltraPro();
        });
    }

    public void setOnExitCallback(Runnable callback) {
        this.onExitCallback = callback;
    }

    public boolean isFocusMode() {
        return isFocusMode;
    }

    public void applyTheme(Color background, Color foreground, Color panelBackground) {
        setBackground(background);
    }
}