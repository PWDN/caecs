package com.vitaliirohozhyn_arsenisialitski.caecs.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import org.json.JSONObject;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.ToolBarInstrument;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.UIAndSimulationSettings;

/**
 * Klasa odpowiadająca za tworzewnie całego interfrejsu użytkownika
 */
public class MainScreen {
    private final JFrame frame;
    private Viewport viewport;
    private final UIAndSimulationSettings settings;
    private final ECS ecs;
    private JComboBox<String> selectRenderMode;

    /**
     * Konstruktor, tworzący potrzebne przyciski, handler'y i t.d.
     *
     * @param a_ecs potrzebny jest do odwolania oraz zmiany ustawień(np. ustalenie
     *              aktywnego instrumentu, zmiana trybu renderu)
     */
    public MainScreen(ECS a_ecs) {
        this.ecs = a_ecs;
        this.settings = a_ecs.settings;
        this.frame = new JFrame();
        this.frame.setLayout(new GridBagLayout());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setupBottomBar();
        this.setupRightBar();
    }

    /**
     * Inicjalizacja prawego menu. Zawiera ustawienia symulajci oraz wybór
     * instrumentów interakcji z symulacją
     */
    public void setupRightBar() {
        JPanel panel = new JPanel();
        BoxLayout lay = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(lay);
        Border color = BorderFactory.createLineBorder(Color.BLUE);
        Border freeSpace = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(BorderFactory.createCompoundBorder(color, freeSpace));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.fill = GridBagConstraints.BOTH;
        JButton btn = new JButton("Change empty pixels to air");
        JCheckBox gravity = new JCheckBox("Enable gravity?", settings.gravityEnabled);

        RenderMode[] modes = RenderMode.values();
        String[] mapped = Arrays.stream(modes).map(n -> n.name).toArray(s -> new String[s]);
        JComboBox<String> selectRenderMode = new JComboBox<String>(mapped);
        selectRenderMode.setAlignmentX(-selectRenderMode.getPreferredSize().width / 2);
        selectRenderMode.setBorder(color);
        selectRenderMode.setSelectedIndex(0);
        selectRenderMode.setMaximumSize(selectRenderMode.getPreferredSize());
        selectRenderMode.addItemListener((l) -> {
            this.settings.renderMode = modes[selectRenderMode.getSelectedIndex()];
        });
        gravity.addItemListener((l) -> {
            this.settings.gravityEnabled = gravity.isSelected();
        });
        btn.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        panel.add(new JLabel("Choose render mode:"));
        panel.add(selectRenderMode);
        this.selectRenderMode = selectRenderMode;
        panel.add(gravity);
        panel.add(new JLabel("Instruments:"));
        for (ToolBarInstrument i : ToolBarInstrument.values()) {
            JButton btn_in = new JButton(i.name);
            btn_in.addActionListener((l) -> {
                this.settings.selectedInstrument = i;
                this.viewport.useOnClick = i.action.apply(this.ecs);
            });
            btn_in.setBorder(color);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
            panel.add(btn_in);
        }
        this.frame.add(panel, c);
    }

    /**
     * Inicjalizacja dolnego menu, które jest odpowiedzialnie za eksport i import
     * plików symulacyjnych
     */
    public void setupBottomBar() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        JButton btns = new JButton("Create save");
        btns.addActionListener((l) -> {
            JFileChooser saveFileDialog = new JFileChooser();
            int rVal = saveFileDialog.showSaveDialog(new JFrame());
            if (rVal == JFileChooser.APPROVE_OPTION) {
                File saveFile = new File(saveFileDialog.getSelectedFile().getAbsolutePath() + ".json");
                try {
                    saveFile.createNewFile();
                    FileWriter writer = new FileWriter(saveFile);
                    writer.write(this.ecs.toJSON().toString());
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        JButton btnl = new JButton("Load save");
        btnl.addActionListener((l) -> {
            JFileChooser loadFileDialog = new JFileChooser();
            int rVal = loadFileDialog.showSaveDialog(new JFrame());
            if (rVal == JFileChooser.APPROVE_OPTION) {
                File saveFile = new File(loadFileDialog.getSelectedFile().getAbsolutePath());
                if (!saveFile.exists() || saveFile.isDirectory())
                    return;
                try {
                    StringBuilder builder = new StringBuilder();
                    Scanner reader = new Scanner(saveFile);
                    while (reader.hasNextLine()) {
                        builder.append(reader.nextLine());
                    }
                    reader.close();
                    this.ecs.fromJSON(new JSONObject(builder.toString()));
                    this.selectRenderMode.setSelectedIndex(this.ecs.settings.renderMode.ordinal());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btns.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        btnl.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        panel.add(btns);
        panel.add(btnl);
        this.frame.add(panel, c);
    }

    /**
     * Inicjalizacja {@link Viewport} w którym jest realizowany render symulacji
     *
     * @param a_viewport jest potrzebny, bo tworzymy go w metodzie głównej i musimy
     *                   go przedać
     */
    public void setupViewport(Viewport a_viewport) {
        this.viewport = a_viewport;
        JPanel panel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 2;
        panel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        panel.add(this.viewport);
        this.frame.add(panel, c);
    }

    /**
     * Pokazywanie okna po skonfigurowaniu wszystkich jego części
     */
    public void showWindow() {
        this.frame.setMinimumSize(this.frame.getMinimumSize()); // idk why it works
        this.frame.pack();
        this.frame.setVisible(true);
    }
}
