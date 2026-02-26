import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PracticeModePanel extends JPanel {
    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JButton hintButton;
    private JLabel feedbackLabel;
    private JTextArea hintArea;
    private JLabel scoreLabel;
    private int currentQuestion = 0;
    private int score = 0;
    private ArrayList<PracticeQuestion> questions;

    public PracticeModePanel(ArrayList<PracticeQuestion> questions) {
        this.questions = questions;
        initComponents();
        loadQuestion();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 55));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(50, 50, 70));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Practice Mode");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setForeground(new Color(100, 220, 130));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(scoreLabel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(40, 40, 55));
        centerPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        questionLabel = new JLabel("Question will appear here");
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        answerField = new JTextField(25);
        answerField.setFont(new Font("Arial", Font.PLAIN, 15));
        answerField.setBackground(new Color(55, 55, 75));
        answerField.setForeground(Color.WHITE);
        answerField.setBorder(new LineBorder(new Color(80, 80, 105), 2));
        answerField.setMaximumSize(new Dimension(350, 40));
        answerField.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerField.setCaretColor(Color.WHITE);
        answerField.addActionListener(e -> checkAnswer());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(40, 40, 55));

        submitButton = createButton("Submit", new Color(34, 197, 94));
        submitButton.addActionListener(e -> checkAnswer());

        hintButton = createButton("Hint", new Color(245, 158, 11));
        hintButton.addActionListener(e -> showHint());

        buttonPanel.add(submitButton);
        buttonPanel.add(hintButton);

        feedbackLabel = new JLabel("");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        hintArea = new JTextArea();
        hintArea.setEditable(false);
        hintArea.setLineWrap(true);
        hintArea.setWrapStyleWord(true);
        hintArea.setFont(new Font("Arial", Font.PLAIN, 13));
        hintArea.setBackground(new Color(55, 55, 80));
        hintArea.setForeground(new Color(255, 220, 100));
        hintArea.setBorder(new EmptyBorder(10, 12, 10, 12));
        hintArea.setVisible(false);
        hintArea.setMaximumSize(new Dimension(400, 70));

        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(answerField);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(buttonPanel);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(feedbackLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(hintArea);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
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

    private void loadQuestion() {
        if (questions == null || questions.isEmpty()) {
            questionLabel.setText("No questions available");
            return;
        }

        if (currentQuestion >= questions.size()) {
            currentQuestion = 0;
            score = 0;
        }

        PracticeQuestion q = questions.get(currentQuestion);
        questionLabel.setText("Q" + (currentQuestion + 1) + ": " + q.getQuestion());
        answerField.setText("");
        feedbackLabel.setText("");
        hintArea.setVisible(false);
        scoreLabel.setText("Score: " + score + "/" + questions.size());
    }

    private void checkAnswer() {
        if (questions == null || currentQuestion >= questions.size()) return;

        PracticeQuestion q = questions.get(currentQuestion);
        String userAnswer = answerField.getText().trim();

        if (q.checkAnswer(userAnswer)) {
            score++;
            feedbackLabel.setText("Correct! " + q.getExplanation());
            feedbackLabel.setForeground(new Color(100, 220, 130));
        } else {
            feedbackLabel.setText("Incorrect. " + q.getExplanation());
            feedbackLabel.setForeground(new Color(255, 130, 130));
        }

        Timer timer = new Timer(2000, e -> {
            currentQuestion++;
            loadQuestion();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showHint() {
        if (questions == null || currentQuestion >= questions.size()) return;

        PracticeQuestion q = questions.get(currentQuestion);
        hintArea.setText("Hint: " + q.getHint());
        hintArea.setVisible(true);
    }

    public void applyTheme(Color background, Color foreground, Color panelBackground) {
        setBackground(panelBackground);
        questionLabel.setForeground(foreground);
        answerField.setBackground(new Color(55, 55, 75));
        answerField.setForeground(foreground);
    }

    public static class PracticeQuestion {
        private String question;
        private String expectedAnswer;
        private String explanation;
        private String hint;

        public PracticeQuestion(String question, String expectedAnswer, String explanation, String hint) {
            this.question = question;
            this.expectedAnswer = expectedAnswer;
            this.explanation = explanation;
            this.hint = hint;
        }

        public String getQuestion() { return question; }
        public String getExpectedAnswer() { return expectedAnswer; }
        public String getExplanation() { return explanation; }
        public String getHint() { return hint; }

        public boolean checkAnswer(String answer) {
            return answer.equalsIgnoreCase(expectedAnswer);
        }
    }
}