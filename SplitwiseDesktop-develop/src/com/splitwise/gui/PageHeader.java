package com.splitwise.gui;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;

import com.splitwise.gui.custom.CJPanel;
import com.splitwise.gui.theme.DefaultTheme;

public class PageHeader extends CJPanel {

	private String text;
	private int paddingLeft = 15;
	private int paddingTop = 13;
	private int paddingRight = 10;
	private int paddingBottom = 10;
	private JLabel headerLabel;
	private int height = 64;
	
	PageHeader(String header) {
		this.text = header;
		
		this.init();
	}
	
	@Override
	public void initComponents() {
		headerLabel = new JLabel(text);
		
		headerLabel.setForeground(DefaultTheme.getColor("PageHeaderForeground"));
		headerLabel.setFont(new Font("Helvetica Neue",Font.BOLD,24));
		
		add(headerLabel);
	}

	@Override
	public void configureComponents() {
		setLayout(null);
		setBackground(DefaultTheme.getColor("PageHeaderBackground"));
		setSize(getSize().width, height);
		setOpaque(true);
	}

	@Override
	public void computeSize() {
		headerLabel.setSize(headerLabel.getPreferredSize());
		
	}

	@Override
	public void computePlacement() {
		headerLabel.setLocation(paddingLeft, paddingTop);
		
	}
	
	public int getPaddingRight() {
		return this.paddingRight;
	}
	
	public void setHeader(String text) {
		this.text = text;
		headerLabel.setText(text);
		headerLabel.repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(DefaultTheme.getColor("PageHeaderBorder"));
		g.fillRect(0, getSize().height - 1, getSize().width, 1);
		super.paintChildren(g);
	}
	
}
