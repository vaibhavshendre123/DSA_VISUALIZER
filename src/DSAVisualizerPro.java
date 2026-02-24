import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DSAVisualizerPro extends JFrame {

    public DSAVisualizerPro() {
        setTitle("DSA Visualizer Pro");
        setSize(1100, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Array Sorting", new ArraySortingPanel());
        tabs.addTab("Stack", new StackPanel());
        tabs.addTab("Queue", new QueuePanel());
        tabs.addTab("Linked List", new LinkedListPanel());

        add(tabs);
        setVisible(true);
    }

    // ===================== ARRAY SORTING =====================
    static class ArraySortingPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
        private int[] array;
        private int i=0,j=0;
        private int draggedIndex=-1;
        private String algorithm="Bubble Sort";
        private boolean sorting=false;
        private Timer timer;
        private int stepCount=0;
        private int speed=50; // animation speed
        private int comparing1=-1, comparing2=-1;

        // Smooth animation variables
        private boolean isSwapping=false;
        private double x1, x2;
        private int swapIndex1=-1, swapIndex2=-1;
        private int width;

        public ArraySortingPanel() {
            setBackground(new Color(30,30,30));
            JPanel controls = new JPanel();

            JButton inputBtn = new JButton("Input Array");
            JButton startBtn = new JButton("Auto Sort");
            JButton stepBtn = new JButton("Next Step");
            JComboBox<String> algoBox = new JComboBox<>(new String[]{"Bubble Sort","Selection Sort","Insertion Sort"});
            JSlider speedSlider = new JSlider(20,500,speed);
            speedSlider.setToolTipText("Adjust animation speed (ms)");

            inputBtn.addActionListener(e -> inputArray());
            startBtn.addActionListener(e -> {
                algorithm = (String) algoBox.getSelectedItem();
                sorting=true; i=0;j=0; stepCount=0; comparing1=-1; comparing2=-1;
                timer.start();
            });
            stepBtn.addActionListener(e -> {
                algorithm = (String) algoBox.getSelectedItem();
                sorting=false; nextStep(); repaint();
            });
            speedSlider.addChangeListener(e -> speed = speedSlider.getValue());

            controls.add(inputBtn); controls.add(algoBox); controls.add(startBtn);
            controls.add(stepBtn); controls.add(new JLabel("Speed:")); controls.add(speedSlider);
            add(controls, BorderLayout.SOUTH);

            addMouseListener(this);
            addMouseMotionListener(this);

            timer = new Timer(speed,this);
        }

        private void inputArray() {
            try {
                String sizeStr = JOptionPane.showInputDialog(this,"Enter array size (max 20):");
                int n = Integer.parseInt(sizeStr);
                if(n<1 || n>20) return;
                array = new int[n];
                for(int k=0;k<n;k++){
                    String val = JOptionPane.showInputDialog(this,"Enter element "+(k+1)+":");
                    array[k] = Integer.parseInt(val);
                }
                i=0;j=0;stepCount=0;
                repaint();
            } catch(Exception ex){ JOptionPane.showMessageDialog(this,"Invalid input"); }
        }

        @Override
        public void actionPerformed(ActionEvent e){
            if(array==null) return;
            timer.setDelay(speed);

            if(isSwapping){
                double dx = (swapIndex2-swapIndex1)*width/10.0;
                if(Math.abs(x1-swapIndex2*width)<1){
                    // Swap complete
                    int tmp = array[swapIndex1];
                    array[swapIndex1] = array[swapIndex2];
                    array[swapIndex2] = tmp;
                    isSwapping=false; j++; stepCount++;
                } else {
                    x1 += dx;
                    x2 -= dx;
                }
            } else if(sorting){
                nextStep();
            }
            repaint();
        }

        private void nextStep() {
            comparing1=-1; comparing2=-1;
            if(array==null) return;

            switch(algorithm){
                case "Bubble Sort":
                    if(i<array.length-1){
                        if(j<array.length-i-1){
                            comparing1=j; comparing2=j+1;
                            if(array[j]>array[j+1]){
                                swapIndex1=j; swapIndex2=j+1;
                                x1=swapIndex1*width; x2=swapIndex2*width;
                                isSwapping=true;
                            } else { j++; stepCount++; }
                        } else { j=0; i++; }
                    } else sorting=false;
                    break;
                case "Selection Sort":
                    if(i<array.length-1){
                        int min=i;
                        for(int k=i+1;k<array.length;k++) if(array[k]<array[min]) min=k;
                        comparing1=i; comparing2=min;
                        if(min!=i){
                            swapIndex1=i; swapIndex2=min;
                            x1=swapIndex1*width; x2=swapIndex2*width;
                            isSwapping=true;
                        } else i++;
                    } else sorting=false;
                    break;
                case "Insertion Sort":
                    if(i<array.length){
                        int key=array[i]; int k=i-1;
                        while(k>=0 && array[k]>key){ array[k+1]=array[k]; k--; stepCount++; }
                        array[k+1]=key; i++;
                    } else sorting=false;
                    break;
            }
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            if(array==null) return;
            Graphics2D g2=(Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            width = getWidth()/array.length;

            for(int k=0;k<array.length;k++){
                int height = array[k]*15;
                int y = getHeight()-height-50;
                int x = k*width;
                if(isSwapping){
                    if(k==swapIndex1) x=(int)x1;
                    else if(k==swapIndex2) x=(int)x2;
                }

                Color base = Color.getHSBColor((float)array[k]/30,0.8f,0.9f);
                if(k==comparing1 || k==comparing2) base=Color.RED;

                draw3DRect(g2,x,y,width-10,height,base);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial",Font.BOLD,14));
                String val = String.valueOf(array[k]);
                int strW = g2.getFontMetrics().stringWidth(val);
                g2.drawString(val,x+(width-10-strW)/2,y-5);
            }

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial",Font.BOLD,16));
            g2.drawString("Steps: "+stepCount,10,20);
        }

        private void draw3DRect(Graphics2D g2,int x,int y,int w,int h,Color base){
            GradientPaint gp = new GradientPaint(x,y,base.brighter(),x+w,y+h,base.darker());
            g2.setPaint(gp); g2.fillRoundRect(x,y,w,h,15,15);

            Polygon side = new Polygon();
            side.addPoint(x+w,y); side.addPoint(x+w+5,y+5);
            side.addPoint(x+w+5,y+h+5); side.addPoint(x+w,y+h);
            g2.setColor(base.darker().darker()); g2.fill(side);

            Polygon top = new Polygon();
            top.addPoint(x,y); top.addPoint(x+5,y-5);
            top.addPoint(x+w+5,y-5); top.addPoint(x+w,y);
            g2.setColor(base.brighter()); g2.fill(top);
        }

        @Override public void mousePressed(MouseEvent e){
            if(array==null) return;
            for(int k=0;k<array.length;k++){
                int x = k*width;
                if(e.getX()>=x && e.getX()<=x+width){ draggedIndex=k; break; }
            }
        }
        @Override public void mouseDragged(MouseEvent e){
            if(draggedIndex!=-1){
                int newVal = Math.max(1,(getHeight()-e.getY()-50)/15);
                array[draggedIndex]=newVal; repaint();
            }
        }
        @Override public void mouseReleased(MouseEvent e){ draggedIndex=-1; }
        @Override public void mouseClicked(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}
        @Override public void mouseMoved(MouseEvent e) {}
    }

    // ===================== STACK =====================
    static class StackPanel extends JPanel {
        private Stack<Integer> stack = new Stack<>();
        private int highlightTop=-1;

        public StackPanel() {
            setBackground(new Color(30,30,30));
            JPanel controls = new JPanel();
            JButton pushBtn = new JButton("Push");
            JButton popBtn = new JButton("Pop");

            pushBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this,"Enter value to push:");
                if(val!=null && !val.isEmpty()){
                    try{ stack.push(Integer.parseInt(val)); highlightTop=stack.size()-1; repaint(); }
                    catch(Exception ex){ JOptionPane.showMessageDialog(this,"Invalid number"); }
                }
            });

            popBtn.addActionListener(e -> {
                if(!stack.isEmpty()) { stack.pop(); highlightTop=-1; }
                else JOptionPane.showMessageDialog(this,"Stack is empty");
                repaint();
            });

            controls.add(pushBtn); controls.add(popBtn);
            add(controls, BorderLayout.SOUTH);
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2=(Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x=100, y=50;
            for(int k=stack.size()-1;k>=0;k--){
                Color c = (k==highlightTop)?Color.RED:Color.CYAN;
                draw3DRect(g2,x,y,80,30,c);
                drawCenteredString(g2,stack.get(k).toString(),x,y,80,30);
                y+=35;
            }
        }

        private void draw3DRect(Graphics2D g2,int x,int y,int w,int h,Color base){
            GradientPaint gp=new GradientPaint(x,y,base.brighter(),x+w,y+h,base.darker());
            g2.setPaint(gp); g2.fillRoundRect(x,y,w,h,10,10);
        }

        private void drawCenteredString(Graphics2D g2,String val,int x,int y,int w,int h){
            g2.setColor(Color.BLACK); g2.setFont(new Font("Arial",Font.BOLD,14));
            int strW=g2.getFontMetrics().stringWidth(val);
            int strH=g2.getFontMetrics().getHeight();
            g2.drawString(val,x+(w-strW)/2,y+(h+strH)/2-5);
        }
    }

    // ===================== QUEUE =====================
    static class QueuePanel extends JPanel {
        private Queue<Integer> queue = new LinkedList<>();
        private int highlightFront=-1, highlightRear=-1;

        public QueuePanel() {
            setBackground(new Color(30,30,30));
            JPanel controls = new JPanel();
            JButton enqueueBtn = new JButton("Enqueue");
            JButton dequeueBtn = new JButton("Dequeue");

            enqueueBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this,"Enter value to enqueue:");
                if(val!=null && !val.isEmpty()){
                    try{ queue.add(Integer.parseInt(val)); highlightRear=queue.size()-1; repaint(); }
                    catch(Exception ex){ JOptionPane.showMessageDialog(this,"Invalid number"); }
                }
            });

            dequeueBtn.addActionListener(e -> {
                if(!queue.isEmpty()){ queue.poll(); highlightFront=-1; repaint(); }
                else JOptionPane.showMessageDialog(this,"Queue is empty");
            });

            controls.add(enqueueBtn); controls.add(dequeueBtn);
            add(controls, BorderLayout.SOUTH);
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2=(Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x=50, y=getHeight()/2;
            int idx=0;
            for(Integer val:queue){
                Color c = (idx==highlightFront)?Color.RED:(idx==highlightRear)?Color.GREEN:Color.ORANGE;
                draw3DRect(g2,x,y,60,40,c);
                drawCenteredString(g2,val.toString(),x,y,60,40);
                x+=70; idx++;
            }
        }

        private void draw3DRect(Graphics2D g2,int x,int y,int w,int h,Color base){
            GradientPaint gp=new GradientPaint(x,y,base.brighter(),x+w,y+h,base.darker());
            g2.setPaint(gp); g2.fillRoundRect(x,y,w,h,10,10);
        }

        private void drawCenteredString(Graphics2D g2,String val,int x,int y,int w,int h){
            g2.setColor(Color.BLACK); g2.setFont(new Font("Arial",Font.BOLD,14));
            int strW=g2.getFontMetrics().stringWidth(val);
            int strH=g2.getFontMetrics().getHeight();
            g2.drawString(val,x+(w-strW)/2,y+(h+strH)/2-5);
        }
    }

    // ===================== LINKED LIST =====================
    static class LinkedListPanel extends JPanel {
        private LinkedList<Integer> list = new LinkedList<>();
        private int highlight=-1;

        public LinkedListPanel() {
            setBackground(new Color(30,30,30));
            JPanel controls = new JPanel();
            JButton insertBtn = new JButton("Insert");
            JButton deleteBtn = new JButton("Delete");

            insertBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this,"Enter value to insert:");
                if(val!=null && !val.isEmpty()){
                    try{ list.add(Integer.parseInt(val)); highlight=list.size()-1; repaint(); }
                    catch(Exception ex){ JOptionPane.showMessageDialog(this,"Invalid number"); }
                }
            });

            deleteBtn.addActionListener(e -> {
                String val = JOptionPane.showInputDialog(this,"Enter value to delete:");
                if(val!=null && !val.isEmpty()){
                    try{ list.remove(Integer.parseInt(val)); highlight=-1; repaint(); }
                    catch(Exception ex){ JOptionPane.showMessageDialog(this,"Value not found"); }
                }
            });

            controls.add(insertBtn); controls.add(deleteBtn);
            add(controls, BorderLayout.SOUTH);
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2=(Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x=50, y=getHeight()/2;
            for(int i=0;i<list.size();i++){
                Color c = (i==highlight)?Color.RED:Color.MAGENTA;
                draw3DRect(g2,x,y,60,40,c);
                drawCenteredString(g2,list.get(i).toString(),x,y,60,40);
                if(i<list.size()-1){
                    g2.setColor(Color.WHITE);
                    g2.drawLine(x+60,y+20,x+80,y+20);
                    g2.drawLine(x+75,y+15,x+80,y+20);
                    g2.drawLine(x+75,y+25,x+80,y+20);
                }
                x+=90;
            }
        }

        private void draw3DRect(Graphics2D g2,int x,int y,int w,int h,Color base){
            GradientPaint gp=new GradientPaint(x,y,base.brighter(),x+w,y+h,base.darker());
            g2.setPaint(gp); g2.fillRoundRect(x,y,w,h,10,10);
        }

        private void drawCenteredString(Graphics2D g2,String val,int x,int y,int w,int h){
            g2.setColor(Color.BLACK); g2.setFont(new Font("Arial",Font.BOLD,14));
            int strW=g2.getFontMetrics().stringWidth(val);
            int strH=g2.getFontMetrics().getHeight();
            g2.drawString(val,x+(w-strW)/2,y+(h+strH)/2-5);
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new DSAVisualizerPro());
    }
}
