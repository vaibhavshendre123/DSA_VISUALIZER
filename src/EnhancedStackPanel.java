import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EnhancedStackPanel extends JPanel {
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
