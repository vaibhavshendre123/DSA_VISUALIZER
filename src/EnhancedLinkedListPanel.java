import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EnhancedLinkedListPanel extends JPanel {
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
