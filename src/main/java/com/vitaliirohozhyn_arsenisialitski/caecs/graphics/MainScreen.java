package com.vitaliirohozhyn_arsenisialitski.caecs.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.ToolBarInstrument;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.UIAndSimulationSettings;

public class MainScreen {
  private final JFrame frame;
  private Viewport viewport;
  private final UIAndSimulationSettings settings;
  private final ECS ecs;
  public MainScreen(ECS a_ecs) {
    this.ecs = a_ecs;
    this.settings = new UIAndSimulationSettings();
    this.frame = new JFrame();
    this.frame.setLayout(new GridBagLayout());
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setupBottomBar();
    this.setupRightBar();
  }
  public void setupRightBar() {
    JPanel panel = new JPanel();
    BoxLayout lay = new BoxLayout(panel, BoxLayout.Y_AXIS);
    panel.setLayout(lay);
    Border color = BorderFactory.createLineBorder(Color.BLUE);
    Border freeSpace = BorderFactory.createEmptyBorder(10,10,10,10);
    panel.setBorder(BorderFactory.createCompoundBorder(color, freeSpace));
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 2;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 3;
    c.fill = GridBagConstraints.BOTH;
    JButton btn = new JButton("Change empty pixels to air");
    JCheckBox gravity = new JCheckBox("Enable gravity?", false);
    btn.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    panel.add(btn);
    panel.add(Box.createRigidArea(new Dimension(0,5)));
    panel.add(gravity);
    for (ToolBarInstrument i : ToolBarInstrument.values()) {
      JButton btn_in = new JButton(i.name);
      btn_in.addActionListener((l) -> {
        this.settings.selectedInstrument = i;
        this.viewport.useOnClick = i.action.apply(this.ecs);
      });
      btn_in.setBorder(color);
      panel.add(Box.createRigidArea(new Dimension(0,5)));
      panel.add(btn_in);
    }
    this.frame.add(panel, c);
  }
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
    JButton btnl = new JButton("Load save");
    btns.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    btnl.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    panel.add(btns);
    panel.add(btnl);
    this.frame.add(panel, c);    
  }

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
  public void showWindow() {
    this.frame.setMinimumSize(this.frame.getMinimumSize()); //idk why it works
    this.frame.pack();
    this.frame.setVisible(true);
  }
}
