import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Visualisation implements Runnable {
    public ArrayList<BSPDungeon> bsp;
    private ArrayList<VoronoiDungeon> voronoi;
    private long bTime;
    private long vTime;
    private JFrame frame;
    JTextPane respane1;
    JTextPane respane2;
    int height = 20;
    int width = 80;
    Font font = new Font("Ubuntu mono", Font.PLAIN, 20);

    public Visualisation(ArrayList bsp, long bspTime, ArrayList voronoi, long voronoiTime) {
        this.bsp = bsp;
        this.voronoi = voronoi;
        this.bTime = bspTime;
        this.vTime = voronoiTime;
    }

    @Override
    public void run() {
        frame = new JFrame("Dungeons");
        frame.setPreferredSize(new Dimension(1000, 1000));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        generateComponents(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    private void generateComponents(Container container) {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        JPanel resultsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // searchPanel
        JLabel searchLabel = new JLabel("enter level #");
        searchLabel.setFont(font);
        JTextField searchField = new JTextField("", 10);
        searchField.setFont(font);
        JLabel enterLabel = new JLabel("then {enter}");
        enterLabel.setFont(font);

        c.insets = new Insets(80, 0, 0, 10);
        c.gridx = 0;
        searchPanel.add(searchLabel, c);
        c.gridx = 1;
        searchPanel.add(searchField, c);
        c.gridx = 2;
        searchPanel.add(enterLabel, c);

        // resultsPanel
        respane1 = new JTextPane();
        respane1.setFont(font);
        respane1.setPreferredSize(new Dimension(900, 350));
        respane1.setBackground(Color.BLACK);
        respane1.setForeground(Color.gray);

        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyledDocument style = respane1.getStyledDocument();
        StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_CENTER);
        StyleConstants.setLineSpacing(attributes, -0.3f);
        style.setParagraphAttributes(0, 0, attributes, false);//*/

        respane2 = new JTextPane();
        respane2.setFont(font);
        respane2.setPreferredSize(new Dimension(900, 350));
        respane2.setBackground(Color.BLACK);
        respane2.setForeground(Color.gray);

        style = respane2.getStyledDocument();
        StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_CENTER);
        StyleConstants.setLineSpacing(attributes, -0.3f);
        style.setParagraphAttributes(0, style.getLength(), attributes, false);//*/

        // infoPanel
        JTextPane timePane = new JTextPane();
        timePane.setText("Käytetty aika:\nVoronoi-luolasto: "+((double)vTime/1000000000)+"s, BSP-luolasto: "+((double)bTime/1000000000+"s"));
        timePane.setFont(font);
        timePane.setPreferredSize(new Dimension(900, 50));
        timePane.setBackground(Color.BLACK);
        timePane.setForeground(Color.WHITE);

        style = timePane.getStyledDocument();
        StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_CENTER);
        style.setParagraphAttributes(0, style.getLength(), attributes, false);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        resultsPanel.add(timePane, c);
        c.gridx = 0;
        c.gridy = 1;
        resultsPanel.add(respane1, c);
        c.gridx = 0;
        c.gridy = 2;
        resultsPanel.add(respane2, c);

        // Listeners
        searchField.addKeyListener(new EnterListener(searchField));

        // container
        container.setLayout(new BorderLayout());
        container.add(searchPanel, BorderLayout.NORTH);
        container.add(resultsPanel, BorderLayout.CENTER);
    }

    private class EnterListener extends KeyAdapter {

        JTextField text;
        public EnterListener (JTextField search) {
            text = search;
        }

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!text.getText().isEmpty()) {
                    try {
                        int level = Integer.parseInt(text.getText());
                        String bspDungeon = "BSP\n";
                        String voronoiDungeon = "Voronoi\n";

                        for (int y = 0; y < height; y++) {
                            for (int x = 0; x < width; x++) {
                                bspDungeon += bsp.get(level).dungeon[y][x];
                                voronoiDungeon += voronoi.get(level).dungeon[y][x];
                            }
                            bspDungeon += "\n";
                            voronoiDungeon += "\n";
                        }

                        respane1.setText(bspDungeon);
                        respane2.setText(voronoiDungeon);
                    } catch (NumberFormatException numba) {
                        //System.out.println(numba);
                    }
                }
            }
        }
    }
}
