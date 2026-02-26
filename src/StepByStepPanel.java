import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class StepByStepPanel extends JPanel {
    private JTextArea explanationArea;
    private ArrayList<String> steps;
    private int currentStep = 0;
    private JLabel titleLabel;
    private JLabel stepIndicator;

    public StepByStepPanel(ArrayList<String> steps) {
        this.steps = steps;
        initComponents();
        updateExplanation();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 70));
        setBorder(new EmptyBorder(8, 10, 8, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(50, 50, 70));
        headerPanel.setBorder(new EmptyBorder(5, 8, 5, 8));

        titleLabel = new JLabel("Algorithm Steps");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(Color.WHITE);

        stepIndicator = new JLabel("Step " + (currentStep + 1) + "/" + (steps != null ? steps.size() : 1));
        stepIndicator.setFont(new Font("Arial", Font.PLAIN, 11));
        stepIndicator.setForeground(new Color(160, 160, 180));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(stepIndicator, BorderLayout.EAST);

        explanationArea = new JTextArea();
        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        explanationArea.setBackground(new Color(55, 55, 80));
        explanationArea.setForeground(Color.WHITE);
        explanationArea.setBorder(null);

        JScrollPane scrollPane = new JScrollPane(explanationArea);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void updateExplanation() {
        if (steps == null || steps.isEmpty()) {
            explanationArea.setText("No explanation available.");
            return;
        }

        if (currentStep >= steps.size()) currentStep = steps.size() - 1;
        if (currentStep < 0) currentStep = 0;

        stepIndicator.setText("Step " + (currentStep + 1) + "/" + steps.size());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= currentStep; i++) {
            if (i == currentStep) {
                sb.append(">> ");
            } else {
                sb.append("   ");
            }
            sb.append("Step ").append(i + 1).append(": ").append(steps.get(i)).append("\n\n");
        }

        explanationArea.setText(sb.toString());
        explanationArea.setCaretPosition(0);
    }

    public void nextStep() {
        currentStep++;
        if (currentStep >= steps.size()) currentStep = steps.size() - 1;
        updateExplanation();
    }

    public void previousStep() {
        currentStep--;
        if (currentStep < 0) currentStep = 0;
        updateExplanation();
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
        this.currentStep = 0;
        updateExplanation();
    }

    public void applyTheme(Color background, Color foreground, Color panelBackground) {
        setBackground(panelBackground);
        setBorder(new EmptyBorder(8, 10, 8, 10));
        
        if (titleLabel != null) {
            titleLabel.setForeground(foreground);
        }
        
        if (stepIndicator != null) {
            stepIndicator.setForeground(new Color(160, 160, 180));
        }
        
        if (explanationArea != null) {
            explanationArea.setBackground(new Color(55, 55, 80));
            explanationArea.setForeground(foreground);
        }
        
        repaint();
    }
}