import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EnhancedQueuePanel extends JPanel {
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
