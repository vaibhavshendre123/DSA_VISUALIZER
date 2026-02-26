import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AlgorithmComparisonPanel extends JPanel {
    private JTextArea leftCodeArea;
    private JTextArea rightCodeArea;
    private JLabel leftLabel;
    private JLabel rightLabel;

    public AlgorithmComparisonPanel(String algorithm1Name, String algorithm1Code, 
                                   String algorithm2Name, String algorithm2Code) {
        initComponents();
        setLeftAlgorithm(algorithm1Name, algorithm1Code);
        setRightAlgorithm(algorithm2Name, algorithm2Code);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 55));

        JPanel headerPanel = new JPanel(new GridLayout(1, 2, 2, 2));
        headerPanel.setBackground(new Color(50, 50, 70));

        leftLabel = new JLabel("Algorithm 1", SwingConstants.CENTER);
        leftLabel.setFont(new Font("Arial", Font.BOLD, 14));
        leftLabel.setForeground(Color.WHITE);
        leftLabel.setBackground(new Color(50, 50, 70));
        leftLabel.setOpaque(true);
        leftLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        rightLabel = new JLabel("Algorithm 2", SwingConstants.CENTER);
        rightLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightLabel.setForeground(Color.WHITE);
        rightLabel.setBackground(new Color(50, 50, 70));
        rightLabel.setOpaque(true);
        rightLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        headerPanel.add(leftLabel);
        headerPanel.add(rightLabel);

        leftCodeArea = new JTextArea();
        leftCodeArea.setEditable(false);
        leftCodeArea.setLineWrap(true);
        leftCodeArea.setWrapStyleWord(true);
        leftCodeArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        leftCodeArea.setBackground(new Color(45, 45, 65));
        leftCodeArea.setForeground(new Color(255, 180, 180));
        leftCodeArea.setBorder(new EmptyBorder(12, 15, 12, 15));

        JScrollPane leftScroll = new JScrollPane(leftCodeArea);
        leftScroll.setBorder(null);
        leftScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        rightCodeArea = new JTextArea();
        rightCodeArea.setEditable(false);
        rightCodeArea.setLineWrap(true);
        rightCodeArea.setWrapStyleWord(true);
        rightCodeArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        rightCodeArea.setBackground(new Color(45, 45, 65));
        rightCodeArea.setForeground(new Color(180, 255, 180));
        rightCodeArea.setBorder(new EmptyBorder(12, 15, 12, 15));

        JScrollPane rightScroll = new JScrollPane(rightCodeArea);
        rightScroll.setBorder(null);
        rightScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 2, 2));
        centerPanel.setBackground(new Color(45, 45, 65));
        centerPanel.add(leftScroll);
        centerPanel.add(rightScroll);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    public void setLeftAlgorithm(String name, String code) {
        leftLabel.setText(name);
        leftCodeArea.setText(code);
    }

    public void setRightAlgorithm(String name, String code) {
        rightLabel.setText(name);
        rightCodeArea.setText(code);
    }

    public void applyTheme(Color background, Color foreground, Color panelBackground) {
        setBackground(panelBackground);
        leftCodeArea.setBackground(new Color(45, 45, 65));
        leftCodeArea.setForeground(new Color(255, 180, 180));
        rightCodeArea.setBackground(new Color(45, 45, 65));
        rightCodeArea.setForeground(new Color(180, 255, 180));
    }
}