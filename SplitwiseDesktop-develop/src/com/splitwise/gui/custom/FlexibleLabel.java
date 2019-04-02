package com.splitwise.gui.custom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

import javax.swing.JTextArea;

import com.splitwise.gui.theme.DefaultTheme;

public class FlexibleLabel extends JTextArea {
	private int rows = 1;
	private int cols = 10;
	private String placeholder = "";
	private boolean showBadge;
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public FlexibleLabel(int rows, int cols) {
		super(rows,cols);
		this.rows = rows;
		this.cols = cols;
		init();
	}
	
	public FlexibleLabel(String text) {
		super();
		setRows(rows);
		setColumns(cols);
		setText(text);
		init();
	}
	public FlexibleLabel() {
		this("");
	}

	private void init() {
		configureComponent();
	}
	
	private void configureComponent() {
		setLineWrap(true);
		setWrapStyleWord(true);
		setOpaque(false);
		setEditable(false);
		setSize(getPreferredSize());
	}
	
	public void setHorizontalAlignment(int alignment) {
		this.setAlignmentX(alignment);
	}
	public void setVerticalAlignment(int alignment) {
		this.setAlignmentY(alignment);
	}
	
	public void setPlaceholder(String text) {
		this.placeholder = text;
	}

	public void paintComponent(final Graphics g) {
		if(this.showBadge) {
			Dimension d = new Dimension(
					g.getFontMetrics().stringWidth(this.getText()),
					g.getFontMetrics().getHeight());
			g.setColor(DefaultTheme.getColor("PageHeaderBackground"));
			g.fillRoundRect(0, 0, d.width+8, d.height, 5, 5);
			g.setColor(Color.DARK_GRAY);
			g.drawRoundRect(0, 0, d.width+8, d.height, 5, 5);
			g.translate(4, 0);
		}
		super.paintComponent(g);
		if(this.showBadge) {
			g.translate(-4, 0);
		}
		if(this.placeholder == null || this.placeholder.length() == 0 || getText().length() > 0) {
			return;
		}
		
		final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getDisabledTextColor());
        g2.drawString(placeholder, getInsets().left, g.getFontMetrics()
            .getMaxAscent() + getInsets().top);
	}

	public void showBadge(boolean b) {
		this.showBadge = true;
		
	}
}
