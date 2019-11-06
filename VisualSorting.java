import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VisualSorting {
    //FRAME
    private JFrame jf;
    //GENERAL VARIABLES
    private int len = 30;
    private int spd = 100;
    //GRAPH VARIABLES
    private final int SIZE = 600;
    private int current = -1;
    private int check = -1;
    private int width = SIZE / len;
    private int type = 0;
    //ARRAYS
    private int[] list;
    //BOOLEANS
    private boolean sorting = false;
    private boolean shuffled = true;
    //UTIL OBJECTS
    SortingAlgorithms algorithm = new SortingAlgorithms();
    Random random = new Random();
    //PANELS
    JPanel tools = new JPanel();
    GraphCanvas canvas;

    //BUTTONS
    JButton sort = new JButton("Sort");
    JButton shuffle = new JButton("Shuffle");


    //CONSTRUCTOR
    public VisualSorting() {
        shuffleList();    //CREATE THE LIST
        initialize();    //INITIALIZE THE GUI
    }

    public void createList() {
        list = new int[len];    //CREATES A LIST EQUAL TO THE LENGTH
        for (int i = 0; i < len; i++) {    //FILLS THE LIST FROM 1-LEN
            list[i] = i + 1;
        }
    }

    public void shuffleList() {
        createList();
        for (int a = 0; a < 500; a++) {    //SHUFFLE RUNS 500 TIMES
            for (int i = 0; i < len; i++) {    //ACCESS EACH ELEMENT OF THE LIST
                int rand = random.nextInt(len);    //PICK A RANDOM NUM FROM 0-LEN
                int temp = list[i];            //SETS TEMP INT TO CURRENT ELEMENT
                list[i] = list[rand];        //SWAPS THE CURRENT INDEX WITH RANDOM INDEX
                list[rand] = temp;            //SETS THE RANDOM INDEX TO THE TEMP
            }
        }
        sorting = false;
        shuffled = true;
    }

    public void initialize() {
        //SET UP FRAME
        jf = new JFrame();
        jf.setSize(800, 635);
        jf.setTitle("Visual Sort");
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.getContentPane().setLayout(null);

        //SET UP TOOLBAR
        tools.setLayout(null);
        tools.setBounds(5, 10, 180, 590);


        //SET UP SORT BUTTON
        sort.setBounds(40, 150, 100, 25);
        tools.add(sort);

        //SET UP SHUFFLE BUTTON
        shuffle.setBounds(40, 190, 100, 25);
        tools.add(shuffle);

        //SET UP CANVAS FOR GRAPH
        canvas = new GraphCanvas();
        canvas.setBounds(190, 0, SIZE, SIZE);
        canvas.setBorder(BorderFactory.createLineBorder(Color.black));
        jf.getContentPane().add(tools);
        jf.getContentPane().add(canvas);

        sort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (shuffled) {
                    sorting = true;
                }

            }
        });
        shuffle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shuffleList();
                reset();
            }
        });

        sorting();
    }

    //SORTING STATE
    public void sorting() {
        if (sorting) {
            try {
                algorithm.quickSort(0, len - 1);
            } catch (IndexOutOfBoundsException e) {
            }    //EXCEPTION HANDLER INCASE LIST ACCESS IS OUT OF BOUNDS
        }
        reset();    //RESET
        pause();    //GO INTO PAUSE STATE
    }

    //PAUSE STATE
    public void pause() {
        int i = 0;
        while (!sorting) {    //LOOP RUNS UNTIL SORTING STARTS
            i++;
            if (i > 100)
                i = 0;
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
        sorting();    //EXIT THE LOOP AND START SORTING
    }

    //RESET SOME VARIABLES
    public void reset() {
        sorting = false;
        current = -1;
        check = -1;
        Update();
    }

    //DELAY METHOD
    public void delay() {
        try {
            Thread.sleep(spd);
        } catch (Exception e) {
        }
    }

    //UPDATES THE GRAPH AND LABELS
    public void Update() {
        width = SIZE / len;
        canvas.repaint();
    }

    //MAIN METHOD
    public static void main(String[] args) {
        new VisualSorting();
    }

    //SUB CLASS TO CONTROL THE GRAPH
    class GraphCanvas extends JPanel {

        public GraphCanvas() {
            setBackground(Color.black);
        }

        //PAINTS THE GRAPH
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < len; i++) {    //RUNS TROUGH EACH ELEMENT OF THE LIST
                int HEIGHT = list[i] * width;    //SETS THE HEIGHT OF THE ELEMENT ON THE GRAPH
                g.setColor(Color.white);    //DEFAULT COLOR
                if (current > -1 && i == current) {
                    g.setColor(Color.green);    //COLOR OF CURRENT
                }
                if (check > -1 && i == check) {
                    g.setColor(Color.red);    //COLOR OF CHECKING
                }
                //DRAWS THE BAR AND THE BLACK OUTLINE
                g.fillRect(i * width, SIZE - HEIGHT, width, HEIGHT);
                g.setColor(Color.black);
                g.drawRect(i * width, SIZE - HEIGHT, width, HEIGHT);

            }
        }
    }

    //SUB CLASS FOR ALGORITHMS
    class SortingAlgorithms {

        public void quickSort(int lo, int hi) {
            if (!sorting)
                return;
            current = hi;
            if (lo < hi) {
                int p = partition(lo, hi);
                quickSort(lo, p - 1);
                quickSort(p + 1, hi);
            }
        }

        public int partition(int lo, int hi) {
            int pivot = list[hi];
            int i = lo - 1;
            for (int j = lo; j < hi; j++) {
                check = j;
                if (!sorting)
                    break;
                if (list[j] < pivot) {
                    i++;
                    swap(i, j);
                }
                Update();
                delay();
            }
            swap(i + 1, hi);
            Update();
            delay();
            return i + 1;
        }

        public void swap(int i1, int i2) {
            int temp = list[i1];
            list[i1] = list[i2];
            list[i2] = temp;
        }

    }
}
