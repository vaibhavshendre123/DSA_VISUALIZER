import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ConceptLinkingPanel extends JPanel {
    private JLabel conceptLabel;
    private JLabel actionLabel;
    private JLabel ruleLabel;
    private String currentConcept = "";
    private String currentAction = "";
    private String currentRule = "";

    public ConceptLinkingPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 45, 65));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 110), 1),
            new EmptyBorder(10, 15, 10, 15)
        ));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(45, 45, 65));

        JLabel titleLabel = new JLabel("Concept → Action Linking");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        titleLabel.setForeground(new Color(255, 180, 100));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        conceptLabel = new JLabel("Ready to start...");
        conceptLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        conceptLabel.setForeground(Color.WHITE);
        conceptLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        actionLabel = new JLabel("");
        actionLabel.setFont(new Font("Arial", Font.BOLD, 13));
        actionLabel.setForeground(new Color(100, 200, 255));
        actionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        ruleLabel = new JLabel("");
        ruleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        ruleLabel.setForeground(new Color(180, 180, 200));
        ruleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(conceptLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(actionLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(ruleLabel);

        JLabel iconLabel = new JLabel("💡");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        iconLabel.setForeground(new Color(255, 200, 100));

        add(iconLabel, BorderLayout.WEST);
        add(leftPanel, BorderLayout.CENTER);
    }

    public void updateConcept(String concept, String action, String rule) {
        this.currentConcept = concept;
        this.currentAction = action;
        this.currentRule = rule;

        conceptLabel.setText(concept);
        actionLabel.setText(action);
        ruleLabel.setText(rule);

        repaint();
    }

    public void reset() {
        conceptLabel.setText("Ready to start...");
        actionLabel.setText("");
        ruleLabel.setText("");
    }

    public void applyTheme(Color background, Color foreground) {
        setBackground(background);
    }
}