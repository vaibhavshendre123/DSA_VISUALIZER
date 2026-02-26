import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class HashTablePanel extends JPanel implements ActionListener {
    private DSAVisualizerUltraPro mainFrame;
    private Map<Integer, java.util.List<Integer>> hashTable = new HashMap<>();
    private int tableSize = 10;
    private int highlight = -1;
    
    public HashTablePanel(DSAVisualizerUltraPro frame) {
        this.mainFrame = frame;
        setBackground(frame.getBgColor());
        
        JPanel controls = new JPanel();
        controls.setBackground(frame.getBgColor());
        
        JButton insertBtn = new JButton("Insert");
        JButton searchBtn = new JButton("Search");
        JButton deleteBtn = new JButton("Delete");
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
        
        searchBtn.addActionListener(e -> {
            String val = JOptionPane.showInputDialog(this, "Enter value to search:");
            if (val != null && !val.isEmpty()) {
                int num = Integer.parseInt(val);
                int hash = num % tableSize;
                highlight = hash;
                repaint();
                if (hashTable.containsKey(hash) && hashTable.get(hash).contains(num)) {
                    JOptionPane.showMessageDialog(this, "Found at index " + hash + " (collision handling: Chaining)");
                } else {
                    JOptionPane.showMessageDialog(this, "Not found");
                }
            }
        });
        
        deleteBtn.addActionListener(e -> {
            String val = JOptionPane.showInputDialog(this, "Enter value to delete:");
            if (val != null && !val.isEmpty()) {
                int num = Integer.parseInt(val);
                int hash = num % tableSize;
                if (hashTable.containsKey(hash)) {
                    hashTable.get(hash).remove((Integer) num);
                    if (hashTable.get(hash).isEmpty()) hashTable.remove(hash);
                }
                repaint();
            }
        });
        
        clearBtn.addActionListener(e -> {
            hashTable.clear();
            repaint();
        });
        
        randomBtn.addActionListener(e -> {
            insert(1 + new Random().nextInt(99));
        });
        
        controls.add(insertBtn);
        controls.add(searchBtn);
        controls.add(deleteBtn);
        controls.add(clearBtn);
        controls.add(randomBtn);
        
        add(controls, BorderLayout.SOUTH);
    }
    
    private void insert(int val) {
        int hash = val % tableSize;
        hashTable.computeIfAbsent(hash, k -> new ArrayList<>()).add(val);
        highlight = hash;
        repaint();
            javax.swing.Timer t = new javax.swing.Timer(500, this);
        t.setRepeats(false);
        t.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        highlight = -1;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(mainFrame.getFgColor());
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Hash Table (Chaining) - Size: " + tableSize, 20, 30);
        g2.drawString("Hash Function: key % " + tableSize, 20, 55);
        
        int cellWidth = 80;
        int startX = (getWidth() - tableSize * cellWidth) / 2;
        int y = 80;
        
        for (int i = 0; i < tableSize; i++) {
            int x = startX + i * cellWidth;
            
            Color c = (i == highlight) ? Color.RED : (hashTable.containsKey(i)) ? mainFrame.getAccentColor() : Color.GRAY;
            GradientPaint gp = new GradientPaint(x, y, c.brighter(), x + cellWidth - 10, y + 30, c.darker());
            g2.setPaint(gp);
            g2.fillRoundRect(x, y, cellWidth - 10, 30, 5, 5);
            
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString(String.valueOf(i), x + 10, y + 20);
            
            g2.setColor(mainFrame.getFgColor());
            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            if (hashTable.containsKey(i)) {
                String values = hashTable.get(i).toString();
                g2.drawString(values, x, y + 50);
            }
            
            g2.setColor(mainFrame.getFgColor());
            g2.drawString("->", x + 15, y + 20);
        }
    }
}
