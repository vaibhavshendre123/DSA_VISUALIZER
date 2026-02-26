import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CodeTogglePanel extends JPanel {
    private JTextArea pseudocodeArea;

    public CodeTogglePanel(String code, String pseudocode) {
        initComponents();
        setCode(code);
        setPseudocode(pseudocode);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 55));
        setBorder(null);

        pseudocodeArea = new JTextArea();
        pseudocodeArea.setEditable(false);
        pseudocodeArea.setLineWrap(true);
        pseudocodeArea.setWrapStyleWord(true);
        pseudocodeArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        pseudocodeArea.setBackground(new Color(45, 45, 65));
        pseudocodeArea.setForeground(new Color(180, 210, 255));
        pseudocodeArea.setBorder(new EmptyBorder(10, 12, 10, 12));

        JScrollPane pseudoScroll = new JScrollPane(pseudocodeArea);
        pseudoScroll.setBorder(null);
        pseudoScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(pseudoScroll, BorderLayout.CENTER);
    }

    public void setCode(String code) {
    }

    public void setPseudocode(String pseudocode) {
        pseudocodeArea.setText(pseudocode);
    }

    public void applyTheme(Color background, Color foreground, Color panelBackground) {
        setBackground(panelBackground);
        pseudocodeArea.setBackground(new Color(45, 45, 65));
        pseudocodeArea.setForeground(new Color(180, 210, 255));
    }
}