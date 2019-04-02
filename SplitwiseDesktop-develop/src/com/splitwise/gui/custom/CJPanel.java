package com.splitwise.gui.custom;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.logging.Logger;

import javax.swing.JPanel;

public abstract class CJPanel extends JPanel {
	
	final public static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public LayoutManager defaultLayoutManager;
	
	public CJPanel() {
		defaultLayoutManager = getLayout();
		setLayout(null);
		setOpaque(false);
	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		computeSize();
	}
	
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		computePlacement();
	}
	
	public void init() {
		initComponents();
		configureComponents();
		computeSize();
		computePlacement();
	}
	
	public abstract void initComponents();
	public abstract void configureComponents();
	public abstract void computeSize();
	public abstract void computePlacement();
	
	// Quick Fix to magical problem
	/*public void paint(Graphics g) {
		super.paint(g);
		computeSize();
		computePlacement();
		super.paintChildren(g);
	}*/
}
