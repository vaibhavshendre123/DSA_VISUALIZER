import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GraphPanel extends JPanel implements ActionListener {
    private DSAVisualizerUltraPro mainFrame;
    private Map<Integer, java.util.List<Integer>> adjacencyList = new HashMap<>();
    private java.util.List<Integer> bfsResult = new ArrayList<>();
    private java.util.List<Integer> dfsResult = new ArrayList<>();
    private Set<Integer> visited = new HashSet<>();
    private javax.swing.Timer animTimer;
    private boolean animating = false;
    
    public GraphPanel(DSAVisualizerUltraPro frame) {
        this.mainFrame = frame;
        setBackground(frame.getBgColor());
        
        JPanel controls = new JPanel();
        controls.setBackground(frame.getBgColor());
        
        JButton addNodeBtn = new JButton("Add Node");
        JButton addEdgeBtn = new JButton("Add Edge");
        JButton bfsBtn = new JButton("BFS");
        JButton dfsBtn = new JButton("DFS");
        JButton clearBtn = new JButton("Clear");
        JButton randomBtn = new JButton("Random Graph");
        
        addNodeBtn.addActionListener(e -> {
            String val = JOptionPane.showInputDialog(this, "Enter node value:");
            if (val != null && !val.isEmpty()) {
                adjacencyList.putIfAbsent(Integer.parseInt(val), new ArrayList<>());
                repaint();
            }
        });
        
        addEdgeBtn.addActionListener(e -> {
            String from = JOptionPane.showInputDialog(this, "Enter source node:");
            String to = JOptionPane.showInputDialog(this, "Enter destination node:");
            if (from != null && to != null) {
                int f = Integer.parseInt(from);
                int t = Integer.parseInt(to);
                adjacencyList.computeIfAbsent(f, k -> new ArrayList<>()).add(t);
                adjacencyList.computeIfAbsent(t, k -> new ArrayList<>()).add(f);
                repaint();
            }
        });
        
        bfsBtn.addActionListener(e -> {
            String start = JOptionPane.showInputDialog(this, "Enter start node:");
            if (start != null) {
                bfsResult.clear();
                bfs(Integer.parseInt(start));
            }
        });
        
        dfsBtn.addActionListener(e -> {
            String start = JOptionPane.showInputDialog(this, "Enter start node:");
            if (start != null) {
                dfsResult.clear();
                visited.clear();
                dfs(Integer.parseInt(start));
            }
        });
        
        clearBtn.addActionListener(e -> {
            adjacencyList.clear();
            bfsResult.clear();
            dfsResult.clear();
            visited.clear();
            repaint();
        });
        
        randomBtn.addActionListener(e -> {
            adjacencyList.clear();
            Random rand = new Random();
            int nodes = 5 + rand.nextInt(3);
            for (int i = 0; i < nodes; i++) {
                adjacencyList.put(i, new ArrayList<>());
            }
            for (int i = 0; i < nodes; i++) {
                for (int j = i + 1; j < nodes; j++) {
                    if (rand.nextBoolean()) {
                        adjacencyList.get(i).add(j);
                        adjacencyList.get(j).add(i);
                    }
                }
            }
            repaint();
        });
        
        controls.add(addNodeBtn);
        controls.add(addEdgeBtn);
        controls.add(bfsBtn);
        controls.add(dfsBtn);
        controls.add(clearBtn);
        controls.add(randomBtn);
        
        add(controls, BorderLayout.SOUTH);
    }
    
    private void bfs(int start) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> q = new LinkedList<>();
        q.add(start);
        visited.add(start);
        
        while (!q.isEmpty()) {
            int node = q.poll();
            bfsResult.add(node);
            for (int neighbor : adjacencyList.getOrDefault(node, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    q.add(neighbor);
                }
            }
        }
        JOptionPane.showMessageDialog(this, "BFS: " + bfsResult);
        repaint();
    }
    
    private void dfs(int node) {
        if (visited.contains(node)) return;
        visited.add(node);
        dfsResult.add(node);
        for (int neighbor : adjacencyList.getOrDefault(node, new ArrayList<>())) {
            dfs(neighbor);
        }
        JOptionPane.showMessageDialog(this, "DFS: " + dfsResult);
        repaint();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {}
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(mainFrame.getFgColor());
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Graph (Adjacency List)", 20, 30);
        
        if (adjacencyList.isEmpty()) return;
        
        Map<Integer, Point> positions = new HashMap<>();
        int n = adjacencyList.size();
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int radius = Math.min(200, n * 40);
        
        int idx = 0;
        for (Integer node : adjacencyList.keySet()) {
            double angle = 2 * Math.PI * idx / n - Math.PI / 2;
            int x = cx + (int) (radius * Math.cos(angle));
            int y = cy + (int) (radius * Math.sin(angle));
            positions.put(node, new Point(x, y));
            idx++;
        }
        
        for (Integer node : adjacencyList.keySet()) {
            for (Integer neighbor : adjacencyList.get(node)) {
                if (node < neighbor) {
                    Point p1 = positions.get(node);
                    Point p2 = positions.get(neighbor);
                    g2.setStroke(new BasicStroke(2));
                    g2.setColor(mainFrame.getFgColor());
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
        
        for (Integer node : adjacencyList.keySet()) {
            Point p = positions.get(node);
            
            boolean inBfs = bfsResult.contains(node);
            boolean inDfs = dfsResult.contains(node);
            Color c = inBfs ? Color.GREEN : (inDfs ? Color.CYAN : mainFrame.getAccentColor());
            
            GradientPaint gp = new GradientPaint(p.x - 25, p.y - 25, c.brighter(), p.x + 25, p.y + 25, c.darker());
            g2.setPaint(gp);
            g2.fillOval(p.x - 25, p.y - 25, 50, 50);
            
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            String val = String.valueOf(node);
            int strW = g2.getFontMetrics().stringWidth(val);
            g2.drawString(val, p.x - strW / 2, p.y + 5);
        }
    }
    
    static class Point {
        int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
    }
}
