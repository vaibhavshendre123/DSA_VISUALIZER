import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.*;

public class BSTPanel extends JPanel implements ActionListener {
    private DSAVisualizerUltraPro mainFrame;
    private BSTNode root = null;
    private BSTNode highlight = null;
    private javax.swing.Timer animTimer;
    private String animState = "";
    private int animValue = -1;
    
    public BSTPanel(DSAVisualizerUltraPro frame) {
        this.mainFrame = frame;
        setBackground(frame.getBgColor());
        
        JPanel controls = new JPanel();
        controls.setBackground(frame.getBgColor());
        
        JButton insertBtn = new JButton("Insert");
        JButton deleteBtn = new JButton("Delete");
        JButton searchBtn = new JButton("Search");
        JButton bfsBtn = new JButton("BFS Traversal");
        JButton dfsBtn = new JButton("DFS Traversal");
        JButton clearBtn = new JButton("Clear");
        JButton randomBtn = new JButton("Random Insert");
        
        insertBtn.addActionListener(e -> {
            String val = JOptionPane.showInputDialog(this, "Enter value:");
            if (val != null && !val.isEmpty()) {
                try {
                    int num = Integer.parseInt(val);
                    root = insert(root, num);
                    highlightNode(num);
                    repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number");
                }
            }
        });
        
        deleteBtn.addActionListener(e -> {
            String val = JOptionPane.showInputDialog(this, "Enter value to delete:");
            if (val != null && !val.isEmpty()) {
                try {
                    root = delete(root, Integer.parseInt(val));
                    highlight = null;
                    repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Value not found");
                }
            }
        });
        
        searchBtn.addActionListener(e -> {
            String val = JOptionPane.showInputDialog(this, "Enter value to search:");
            if (val != null && !val.isEmpty()) {
                try {
                    BSTNode result = search(root, Integer.parseInt(val));
                    if (result != null) {
                        JOptionPane.showMessageDialog(this, "Found: " + result.value);
                        highlight = result;
                    } else {
                        JOptionPane.showMessageDialog(this, "Not found");
                    }
                    repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number");
                }
            }
        });
        
        bfsBtn.addActionListener(e -> {
            java.util.List<Integer> result = bfs();
            JOptionPane.showMessageDialog(this, "BFS: " + result);
        });
        
        dfsBtn.addActionListener(e -> {
            java.util.List<Integer> result = new java.util.ArrayList<>();
            dfs(root, result);
            JOptionPane.showMessageDialog(this, "DFS: " + result);
        });
        
        clearBtn.addActionListener(e -> {
            root = null;
            highlight = null;
            repaint();
        });
        
        randomBtn.addActionListener(e -> {
            int val = 1 + new Random().nextInt(50);
            root = insert(root, val);
            highlightNode(val);
            repaint();
        });
        
        controls.add(insertBtn);
        controls.add(deleteBtn);
        controls.add(searchBtn);
        controls.add(bfsBtn);
        controls.add(dfsBtn);
        controls.add(clearBtn);
        controls.add(randomBtn);
        
        add(controls, BorderLayout.SOUTH);
    }
    
    private void highlightNode(int val) {
        animTimer = new javax.swing.Timer(200, this);
        animValue = val;
        animTimer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        highlight = search(root, animValue);
        repaint();
        animTimer.stop();
    }
    
    private BSTNode insert(BSTNode node, int val) {
        if (node == null) return new BSTNode(val);
        if (val < node.value) node.left = insert(node.left, val);
        else if (val > node.value) node.right = insert(node.right, val);
        return node;
    }
    
    private BSTNode delete(BSTNode node, int val) {
        if (node == null) return null;
        if (val < node.value) node.left = delete(node.left, val);
        else if (val > node.value) node.right = delete(node.right, val);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            node.value = minValue(node.right);
            node.right = delete(node.right, node.value);
        }
        return node;
    }
    
    private int minValue(BSTNode node) {
        int min = node.value;
        while (node.left != null) {
            min = node.left.value;
            node = node.left;
        }
        return min;
    }
    
    private BSTNode search(BSTNode node, int val) {
        if (node == null || node.value == val) return node;
        if (val < node.value) return search(node.left, val);
        return search(node.right, val);
    }
    
    private java.util.List<Integer> bfs() {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        if (root == null) return result;
        Queue<BSTNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            BSTNode node = q.poll();
            result.add(node.value);
            if (node.left != null) q.add(node.left);
            if (node.right != null) q.add(node.right);
        }
        return result;
    }
    
    private void dfs(BSTNode node, java.util.List<Integer> result) {
        if (node != null) {
            dfs(node.left, result);
            result.add(node.value);
            dfs(node.right, result);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(mainFrame.getFgColor());
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Binary Search Tree", 20, 30);
        
        if (root != null) {
            drawTree(g2, root, getWidth() / 2, 60, getWidth() / 4);
        }
    }
    
    private void drawTree(Graphics2D g2, BSTNode node, int x, int y, int offset) {
        if (node == null) return;
        
        if (node.left != null) {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(mainFrame.getFgColor());
            g2.drawLine(x, y, x - offset, y + 60);
            drawTree(g2, node.left, x - offset, y + 60, offset / 2);
        }
        
        if (node.right != null) {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(mainFrame.getFgColor());
            g2.drawLine(x, y, x + offset, y + 60);
            drawTree(g2, node.right, x + offset, y + 60, offset / 2);
        }
        
        Color c = (node == highlight) ? Color.RED : mainFrame.getAccentColor();
        draw3DCircle(g2, x, y, 25, c);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        String val = String.valueOf(node.value);
        int strW = g2.getFontMetrics().stringWidth(val);
        g2.drawString(val, x - strW / 2, y + 5);
    }
    
    private void draw3DCircle(Graphics2D g2, int x, int y, int r, Color base) {
        GradientPaint gp = new GradientPaint(x - r, y - r, base.brighter(), x + r, y + r, base.darker());
        g2.setPaint(gp);
        g2.fillOval(x - r, y - r, r * 2, r * 2);
    }
    
    static class BSTNode {
        int value;
        BSTNode left, right;
        BSTNode(int v) { value = v; }
    }
}
