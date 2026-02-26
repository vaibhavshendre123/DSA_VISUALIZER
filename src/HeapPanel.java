import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class HeapPanel extends JPanel implements ActionListener {
    private DSAVisualizerUltraPro mainFrame;
    private int[] heap = new int[0];
    private boolean isMinHeap = false;
    private javax.swing.Timer timer;
    private int highlight1 = -1, highlight2 = -1;
    
    public HeapPanel(DSAVisualizerUltraPro frame) {
        this.mainFrame = frame;
        setBackground(frame.getBgColor());
        
        JPanel controls = new JPanel();
        controls.setBackground(frame.getBgColor());
        
        JButton insertBtn = new JButton("Insert");
        JButton extractBtn = new JButton("Extract Max");
        JButton minExtractBtn = new JButton("Extract Min");
        JButton heapifyBtn = new JButton("Heapify");
        JButton clearBtn = new JButton("Clear");
        JButton randomBtn = new JButton("Random");
        
        insertBtn.addActionListener(e -> {
            String val = JOptionPane.showInputDialog(this, "Enter value:");
            if (val != null && !val.isEmpty()) {
                try {
                    int num = Integer.parseInt(val);
                    insert(num);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number");
                }
            }
        });
        
        extractBtn.addActionListener(e -> {
            if (heap.length > 0) {
                extractMax();
            } else {
                JOptionPane.showMessageDialog(this, "Heap is empty");
            }
        });
        
        minExtractBtn.addActionListener(e -> {
            if (heap.length > 0) {
                extractMin();
            } else {
                JOptionPane.showMessageDialog(this, "Heap is empty");
            }
        });
        
        heapifyBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter numbers separated by comma:");
            if (input != null && !input.isEmpty()) {
                String[] parts = input.split(",");
                heap = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    heap[i] = Integer.parseInt(parts[i].trim());
                }
                for (int i = heap.length / 2 - 1; i >= 0; i--) {
                    heapify(heap.length, i);
                }
                repaint();
            }
        });
        
        clearBtn.addActionListener(e -> {
            heap = new int[0];
            repaint();
        });
        
        randomBtn.addActionListener(e -> {
            insert(1 + new Random().nextInt(50));
        });
        
        controls.add(insertBtn);
        controls.add(extractBtn);
        controls.add(minExtractBtn);
        controls.add(heapifyBtn);
        controls.add(clearBtn);
        controls.add(randomBtn);
        
        add(controls, BorderLayout.SOUTH);
    }
    
    private void insert(int val) {
        int[] newHeap = new int[heap.length + 1];
        System.arraycopy(heap, 0, newHeap, 0, heap.length);
        newHeap[heap.length] = val;
        heap = newHeap;
        heapifyUp(heap.length - 1);
        repaint();
    }
    
    private void extractMax() {
        if (heap.length == 0) return;
        int max = heap[0];
        heap[0] = heap[heap.length - 1];
        int[] newHeap = new int[heap.length - 1];
        System.arraycopy(heap, 0, newHeap, 0, heap.length - 1);
        heap = newHeap;
        if (heap.length > 0) heapifyDown(0);
        JOptionPane.showMessageDialog(this, "Extracted: " + max);
        repaint();
    }
    
    private void extractMin() {
        if (heap.length == 0) return;
        int min = heap[heap.length - 1];
        int[] newHeap = new int[heap.length - 1];
        System.arraycopy(heap, 0, newHeap, 0, heap.length - 1);
        heap = newHeap;
        repaint();
    }
    
    private void heapifyUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap[i] > heap[parent]) {
                highlight1 = i; highlight2 = parent;
                int temp = heap[i];
                heap[i] = heap[parent];
                heap[parent] = temp;
                i = parent;
            } else break;
        }
    }
    
    private void heapifyDown(int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        if (left < heap.length && heap[left] > heap[largest]) largest = left;
        if (right < heap.length && heap[right] > heap[largest]) largest = right;
        
        if (largest != i) {
            highlight1 = i; highlight2 = largest;
            int temp = heap[i];
            heap[i] = heap[largest];
            heap[largest] = temp;
            heapifyDown(largest);
        }
    }
    
    private void heapify(int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        if (left < n && heap[left] > heap[largest]) largest = left;
        if (right < n && heap[right] > heap[largest]) largest = right;
        
        if (largest != i) {
            int temp = heap[i];
            heap[i] = heap[largest];
            heap[largest] = temp;
            heapify(n, largest);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        highlight1 = -1;
        highlight2 = -1;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(mainFrame.getFgColor());
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Max Heap", 20, 30);
        g2.drawString("Size: " + heap.length, 20, 55);
        
        if (heap.length > 0) {
            drawHeap(g2, 0, getWidth() / 2, 50, getWidth() / 4);
        }
    }
    
    private void drawHeap(Graphics2D g2, int i, int x, int y, int offset) {
        if (i >= heap.length) return;
        
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        if (left < heap.length) {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(mainFrame.getFgColor());
            g2.drawLine(x, y, x - offset, y + 60);
            drawHeap(g2, left, x - offset, y + 60, offset / 2);
        }
        
        if (right < heap.length) {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(mainFrame.getFgColor());
            g2.drawLine(x, y, x + offset, y + 60);
            drawHeap(g2, right, x + offset, y + 60, offset / 2);
        }
        
        Color c = (i == highlight1 || i == highlight2) ? Color.RED : mainFrame.getAccentColor();
        GradientPaint gp = new GradientPaint(x - 20, y - 20, c.brighter(), x + 20, y + 20, c.darker());
        g2.setPaint(gp);
        g2.fillOval(x - 20, y - 20, 40, 40);
        
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        String val = String.valueOf(heap[i]);
        int strW = g2.getFontMetrics().stringWidth(val);
        g2.drawString(val, x - strW / 2, y + 5);
    }
}
