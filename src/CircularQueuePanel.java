import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CircularQueuePanel extends JPanel {
    private DSAVisualizerUltraPro mainFrame;
    private int[] queue = new int[10];
    private int front = -1;
    private int rear = -1;
    private int size = 0;
    private int maxSize = 10;
    
    public CircularQueuePanel(DSAVisualizerUltraPro frame) {
        this.mainFrame = frame;
        setBackground(frame.getBgColor());
        
        JPanel controls = new JPanel();
        controls.setBackground(frame.getBgColor());
        
        JButton enqueueBtn = new JButton("Enqueue");
        JButton dequeueBtn = new JButton("Dequeue");
        JButton clearBtn = new JButton("Clear");
        JButton randomBtn = new JButton("Random");
        
        enqueueBtn.addActionListener(e -> {
            String val = JOptionPane.showInputDialog(this, "Enter value:");
            if (val != null && !val.isEmpty()) {
                try {
                    int num = Integer.parseInt(val);
                    if (size < maxSize) {
                        if (front == -1) front = 0;
                        rear = (rear + 1) % maxSize;
                        queue[rear] = num;
                        size++;
                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(this, "Queue is full");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number");
                }
            }
        });
        
        dequeueBtn.addActionListener(e -> {
            if (size > 0) {
                queue[front] = 0;
                front = (front + 1) % maxSize;
                size--;
                if (size == 0) {
                    front = -1;
                    rear = -1;
                }
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Queue is empty");
            }
        });
        
        clearBtn.addActionListener(e -> {
            queue = new int[10];
            front = -1;
            rear = -1;
            size = 0;
            repaint();
        });
        
        randomBtn.addActionListener(e -> {
            if (size < maxSize) {
                int val = 1 + new Random().nextInt(99);
                if (front == -1) front = 0;
                rear = (rear + 1) % maxSize;
                queue[rear] = val;
                size++;
                repaint();
            }
        });
        
        controls.add(enqueueBtn);
        controls.add(dequeueBtn);
        controls.add(clearBtn);
        controls.add(randomBtn);
        
        add(controls, BorderLayout.SOUTH);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int radius = 150;
        
        g2.setColor(mainFrame.getFgColor());
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Circular Queue - Size: " + size + "/" + maxSize, 20, 30);
        
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);
        
        double angle = 2 * Math.PI / maxSize;
        for (int i = 0; i < maxSize; i++) {
            double a = i * angle - Math.PI / 2;
            int x = cx + (int) (radius * Math.cos(a));
            int y = cy + (int) (radius * Math.sin(a));
            
            Color c = (i == front) ? Color.RED : (i == rear && size > 0) ? Color.GREEN : mainFrame.getBgColor();
            if (i < size) {
                int idx = (front + i) % maxSize;
                c = mainFrame.getAccentColor();
                if (idx == front) c = Color.RED;
                if (idx == rear) c = Color.GREEN;
            }
            
            GradientPaint gp = new GradientPaint(x - 25, y - 20, c.brighter(), x + 25, y + 20, c.darker());
            g2.setPaint(gp);
            g2.fillRoundRect(x - 25, y - 20, 50, 40, 10, 10);
            
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            String val = (i < size) ? String.valueOf(queue[(front + i) % maxSize]) : "";
            int strW = g2.getFontMetrics().stringWidth(val);
            g2.drawString(val, x - strW / 2, y + 5);
            
            g2.setColor(mainFrame.getFgColor());
            g2.drawString(String.valueOf(i), x - 3, y + 35);
        }
        
        g2.setColor(Color.RED);
        g2.drawString("F", cx, cy - radius - 15);
        g2.setColor(Color.GREEN);
        g2.drawString("R", cx + radius + 20, cy);
    }
}
