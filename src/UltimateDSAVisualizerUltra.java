
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class UltimateDSAVisualizerUltra extends JFrame {

    public UltimateDSAVisualizerUltra() {
        setTitle("Ultimate 3D DSA Visualizer Ultra");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Array Sorting", new ArrayPanel());
        tabs.addTab("Stack", new StackPanel());
        tabs.addTab("Queue", new QueuePanel());
        tabs.addTab("Linked List", new LinkedListPanel());

        add(tabs);
        setVisible(true);
    }

    // =================== ARRAY PANEL ===================
    static class ArrayPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
        private int[] array;
        private int currentI = -1, currentJ = -1;
        private Timer timer;
        private boolean sorting = false;
        private int i = 0, j = 0;
        private int draggedIndex = -1;
        private String algorithm = "Bubble Sort";

        public ArrayPanel() {
            askArrayInput();
            setBackground(new Color(30, 30, 30));
            addMouseListener(this);
            addMouseMotionListener(this);

            timer = new Timer(20, this);

            JPanel controls = new JPanel();
            JButton startBtn = new JButton("Auto Sort");
            JButton stepBtn = new JButton("Next Step");
            JButton resetBtn = new JButton("New Array");
            JComboBox<String> algoBox = new JComboBox<>(new String[]{"Bubble Sort", "Selection Sort", "Insertion Sort"});
            JSlider speedSlider = new JSlider(1, 50, 20);

            startBtn.addActionListener(e -> {
                algorithm = (String) algoBox.getSelectedItem();
                sorting = true;
                i = 0;
                j = 0;
                timer.start();
            });

            stepBtn.addActionListener(e -> {
                algorithm = (String) algoBox.getSelectedItem();
                nextStep();
                repaint();
            });

            resetBtn.addActionListener(e -> {
                askArrayInput();
                repaint();
            });

            speedSlider.addChangeListener(e -> timer.setDelay(100 / speedSlider.getValue()));

            controls.add(algoBox);
            controls.add(startBtn);
            controls.add(stepBtn);
            controls.add(resetBtn);
            controls.add(new JLabel("Speed:"));
            controls.add(speedSlider);

            add(controls, BorderLayout.SOUTH);
        }

        private void askArrayInput() {
            try {
                String sizeStr = JOptionPane.showInputDialog(this, "Enter array size (max 20):");
                int n = Math.min(Math.max(Integer.parseInt(sizeStr), 1), 20);
                array = new int[n];
                for (int k = 0; k < n; k++) {
                    String valStr = JOptionPane.showInputDialog(this, "Enter element " + (k + 1) + ":");
                    array[k] = Integer.parseInt(valStr);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Using default array.");
                array = new int[]{5, 3, 8, 2, 7, 1, 4, 6, 9, 10};
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth() / array.length;
            for (int k = 0; k < array.length; k++) {
                int height = array[k] * 15;
                int x = k * width;
                int y = getHeight() - height;

                Color base = (k == currentI || k == currentJ) ? Color.RED : Color.getHSBColor((float) array[k] / 30, 0.8f, 0.9f);

                // 3D front
                GradientPaint gp = new GradientPaint(x, y, base.brighter(), x + width, y + height, base.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(x + 5, y, width - 10, height, 20, 20);

                // Side face
                Polygon side = new Polygon();
                side.addPoint(x + width - 10, y);
                side.addPoint(x + width - 5, y + 5);
                side.addPoint(x + width - 5, y + height + 5);
                side.addPoint(x + width - 10, y + height);
                g2.setColor(base.darker().darker());
                g2.fill(side);

                // Top face
                Polygon top = new Polygon();
                top.addPoint(x + 5, y);
                top.addPoint(x + 10, y - 5);
                top.addPoint(x + width - 5, y - 5);
                top.addPoint(x + width - 10, y);
                g2.setColor(base.brighter());
                g2.fill(top);

                // Number
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                String val = String.valueOf(array[k]);
                int strWidth = g2.getFontMetrics().stringWidth(val);
                g2.drawString(val, x + (width - strWidth) / 2, y - 10);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (sorting) nextStep();
            repaint();
        }

        private void nextStep() {
            switch (algorithm) {
                case "Bubble Sort": bubbleStep(); break;
                case "Selection Sort": selectionStep(); break;
                case "Insertion Sort": insertionStep(); break;
            }
        }

        private void bubbleStep() {
            if (i < array.length - 1) {
                if (j < array.length - i - 1) {
                    currentI = j; currentJ = j + 1;
                    if (array[j] > array[j + 1]) {
                        int temp = array[j]; array[j] = array[j + 1]; array[j + 1] = temp;
                    }
                    j++;
                } else { j = 0; i++; }
            } else sorting = false;
        }

        private void selectionStep() {
            if (i < array.length - 1) {
                int min = i;
                for (int k = i + 1; k < array.length; k++)
                    if (array[k] < array[min]) min = k;
                currentI = i; currentJ = min;
                int temp = array[i]; array[i] = array[min]; array[min] = temp;
                i++;
            } else sorting = false;
        }

        private void insertionStep() {
            if (i < array.length) {
                int key = array[i]; int k = i - 1;
                while (k >= 0 && array[k] > key) { array[k + 1] = array[k]; currentI = k; currentJ = k + 1; k--; }
                array[k + 1] = key; i++;
            } else sorting = false;
        }

        @Override public void mousePressed(MouseEvent e) {
            int width = getWidth() / array.length;
            for (int k = 0; k < array.length; k++) {
                int x = k * width;
                if (e.getX() >= x && e.getX() <= x + width) { draggedIndex = k; break; }
            }
        }

        @Override public void mouseDragged(MouseEvent e) {
            if (draggedIndex != -1) {
                array[draggedIndex] = Math.max(1, (getHeight() - e.getY()) / 15);
                repaint();
            }
        }

        @Override public void mouseReleased(MouseEvent e) { draggedIndex = -1; }
        @Override public void mouseClicked(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}
        @Override public void mouseMoved(MouseEvent e) {}
    }

    // =================== STACK PANEL ===================
    static class StackPanel extends JPanel {
        private Stack<Integer> stack = new Stack<>();
        private int pushVal = -1;
        private boolean pushing = false;
        private float progress = 0f;

        public StackPanel() {
            setBackground(new Color(30, 30, 30));
            JPanel controls = new JPanel();
            JButton pushBtn = new JButton("Push");
            JButton popBtn = new JButton("Pop");

            pushBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value to push:");
                if (val != null && !val.isEmpty()) {
                    try { pushVal = Integer.parseInt(val); pushing = true; progress = 0f;
                        Timer t = new Timer(15, ev -> {
                            progress += 0.05f;
                            if (progress >= 1f) { stack.push(pushVal); pushing = false; ((Timer)ev.getSource()).stop(); }
                            repaint();
                        }); t.start();
                    } catch(Exception ex){ JOptionPane.showMessageDialog(this,"Invalid number"); }
                }
            });

            popBtn.addActionListener(e -> {
                if(!stack.isEmpty()) stack.pop(); else JOptionPane.showMessageDialog(this,"Stack empty");
                repaint();
            });

            controls.add(pushBtn); controls.add(popBtn);
            add(controls, BorderLayout.SOUTH);
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = 100, y = getHeight() - 50;
            for(int k=stack.size()-1; k>=0; k--){
                draw3DRect(g2, x, y-30, 80, 30, Color.CYAN);
                String val = stack.get(k).toString();
                drawCenteredString(g2,val,x, y-30,80,30);
                y -= 35;
            }

            if(pushing){
                int yPush = (int)(getHeight() - 35 - 35*stack.size()*(1-progress));
                draw3DRect(g2, x, yPush-30, 80, 30, Color.CYAN);
                drawCenteredString(g2,String.valueOf(pushVal),x,yPush-30,80,30);
            }
        }

        private void draw3DRect(Graphics2D g2,int x,int y,int w,int h,Color base){
            GradientPaint gp = new GradientPaint(x,y,base.brighter(),x+w,y+h,base.darker());
            g2.setPaint(gp); g2.fillRoundRect(x,y,w,h,10,10);
            Polygon side = new Polygon(); side.addPoint(x+w,y); side.addPoint(x+w+5,y+5); side.addPoint(x+w+5,y+h+5); side.addPoint(x+w,y+h); g2.setColor(base.darker().darker()); g2.fill(side);
            Polygon top = new Polygon(); top.addPoint(x,y); top.addPoint(x+5,y-5); top.addPoint(x+w+5,y-5); top.addPoint(x+w,y); g2.setColor(base.brighter()); g2.fill(top);
        }

        private void drawCenteredString(Graphics2D g2,String val,int x,int y,int w,int h){
            g2.setColor(Color.BLACK); g2.setFont(new Font("Arial",Font.BOLD,14));
            int strW=g2.getFontMetrics().stringWidth(val); int strH=g2.getFontMetrics().getHeight();
            g2.drawString(val,x+(w-strW)/2,y+(h+strH)/2-5);
        }
    }

    // =================== QUEUE PANEL ===================
    static class QueuePanel extends JPanel {
        private Queue<Integer> queue = new LinkedList<>();
        private int enqueueVal=-1;
        private boolean enqueuing=false;
        private float progress=0f;

        public QueuePanel(){
            setBackground(new Color(30,30,30));
            JPanel controls=new JPanel();
            JButton enqueueBtn=new JButton("Enqueue");
            JButton dequeueBtn=new JButton("Dequeue");

            enqueueBtn.addActionListener(e->{
                String val=JOptionPane.showInputDialog(this,"Enter value to enqueue:");
                if(val!=null && !val.isEmpty()){
                    try{ enqueueVal=Integer.parseInt(val); enqueuing=true; progress=0f;
                        Timer t=new Timer(15, ev->{ progress+=0.05f; if(progress>=1f){ queue.add(enqueueVal); enqueuing=false; ((Timer)ev.getSource()).stop(); } repaint();}); t.start();
                    }catch(Exception ex){ JOptionPane.showMessageDialog(this,"Invalid number");}
                }
            });

            dequeueBtn.addActionListener(e->{ if(!queue.isEmpty()){ queue.poll(); repaint(); }else JOptionPane.showMessageDialog(this,"Queue empty"); });

            controls.add(enqueueBtn); controls.add(dequeueBtn);
            add(controls,BorderLayout.SOUTH);
        }

        @Override protected void paintComponent(Graphics g){
            super.paintComponent(g); Graphics2D g2=(Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int xStart=50,y=getHeight()/2,index=0;
            for(Integer val:queue){
                int x=xStart+index*70;
                draw3DRect(g2,x,y,60,40,Color.ORANGE);
                drawCenteredString(g2,val.toString(),x,y,60,40);
                index++;
            }
            if(enqueuing){
                int x=xStart+index*70;
                draw3DRect(g2,x,y,60,40,Color.ORANGE);
                drawCenteredString(g2,String.valueOf(enqueueVal),x,y,60,40);
            }
        }

        private void draw3DRect(Graphics2D g2,int x,int y,int w,int h,Color base){
            GradientPaint gp=new GradientPaint(x,y,base.brighter(),x+w,y+h,base.darker());
            g2.setPaint(gp); g2.fillRoundRect(x,y,w,h,10,10);
            Polygon side=new Polygon(); side.addPoint(x+w,y); side.addPoint(x+w+5,y+5); side.addPoint(x+w+5,y+h+5); side.addPoint(x+w,y+h); g2.setColor(base.darker().darker()); g2.fill(side);
            Polygon top=new Polygon(); top.addPoint(x,y); top.addPoint(x+5,y-5); top.addPoint(x+w+5,y-5); top.addPoint(x+w,y); g2.setColor(base.brighter()); g2.fill(top);
        }
        private void drawCenteredString(Graphics2D g2,String val,int x,int y,int w,int h){
            g2.setColor(Color.BLACK); g2.setFont(new Font("Arial",Font.BOLD,14));
            int strW=g2.getFontMetrics().stringWidth(val); int strH=g2.getFontMetrics().getHeight();
            g2.drawString(val,x+(w-strW)/2,y+(h+strH)/2-5);
        }
    }

    // =================== LINKED LIST PANEL ===================
    static class LinkedListPanel extends JPanel {
        private LinkedList<Integer> list = new LinkedList<>();

        public LinkedListPanel(){
            setBackground(new Color(30,30,30));
            JPanel controls = new JPanel();
            JButton insertBtn = new JButton("Insert");
            JButton deleteBtn = new JButton("Delete");

            insertBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value to insert:");
                if(val!=null && !val.isEmpty()){ try{ list.add(Integer.parseInt(val)); repaint(); }catch(Exception ex){ JOptionPane.showMessageDialog(this,"Invalid number"); } }
            });

            deleteBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this, "Enter value to delete:");
                if(val!=null && !val.isEmpty()){ try{ list.remove(Integer.parseInt(val)); repaint(); }catch(Exception ex){ JOptionPane.showMessageDialog(this,"Value not found"); } }
            });

            controls.add(insertBtn); controls.add(deleteBtn);
            add(controls,BorderLayout.SOUTH);
        }

        @Override protected void paintComponent(Graphics g){
            super.paintComponent(g); Graphics2D g2=(Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int x=50,y=getHeight()/2;
            for(int i=0;i<list.size();i++){
                draw3DRect(g2,x,y,60,40,Color.MAGENTA);
                drawCenteredString(g2,list.get(i).toString(),x,y,60,40);
                if(i<list.size()-1){
                    g2.setColor(Color.WHITE);
                    g2.drawLine(x+60,y+20,x+90,y+20);
                    g2.drawLine(x+85,y+15,x+90,y+20);
                    g2.drawLine(x+85,y+25,x+90,y+20);
                }
                x+=90;
            }
        }

        private void draw3DRect(Graphics2D g2,int x,int y,int w,int h,Color base){
            GradientPaint gp=new GradientPaint(x,y,base.brighter(),x+w,y+h,base.darker());
            g2.setPaint(gp); g2.fillRoundRect(x,y,w,h,10,10);
            Polygon side=new Polygon(); side.addPoint(x+w,y); side.addPoint(x+w+5,y+5); side.addPoint(x+w+5,y+h+5); side.addPoint(x+w,y+h); g2.setColor(base.darker().darker()); g2.fill(side);
            Polygon top=new Polygon(); top.addPoint(x,y); top.addPoint(x+5,y-5); top.addPoint(x+w+5,y-5); top.addPoint(x+w,y); g2.setColor(base.brighter()); g2.fill(top);
        }

        private void drawCenteredString(Graphics2D g2,String val,int x,int y,int w,int h){
            g2.setColor(Color.BLACK); g2.setFont(new Font("Arial",Font.BOLD,14));
            int strW=g2.getFontMetrics().stringWidth(val); int strH=g2.getFontMetrics().getHeight();
            g2.drawString(val,x+(w-strW)/2,y+(h+strH)/2-5);
        }
    }

    public static void main(String[] args){ SwingUtilities.invokeLater(UltimateDSAVisualizerUltra::new); }
}
