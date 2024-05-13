package com.vitaliirohozhyn_arsenisialitski.caecs.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainScreen {
  private final JFrame frame;
  private Viewport viewport;
  public MainScreen() {
    this.frame = new JFrame();
    this.frame.setLayout(new GridBagLayout());
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setupBottomBar();
    this.setupRightBar();
  }
  public void setupRightBar() {
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
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
    panel.add(gravity);
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
