package com.splitwise.gui.custom;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import com.splitwise.gui.theme.DefaultTheme;

public class ActivityListItem extends CJPanel {

	private JLabel contentText;
	
	private int preferredHeight = 80;
	private int lineGap = 10;
	private int paddingLeft = 15;
	private int paddingTop = 15;
	private String content;
	
	public static int OWE = 0;
	public static int OWED = 1;
	private int whoOwe;
	private boolean isHighlighted;
	
	public ActivityListItem(String content) {
		this.content = content;
		init();
		
		addListener();
	}
	@Override
	public void initComponents() {
		contentText = new JLabel("<html>\n" + this.content + "</html>");
		
		add(contentText);
	}

	@Override
	public void configureComponents() {
		contentText.setFont(new Font("Helvetica Neue",Font.PLAIN,16));
		
	}

	@Override
	public void computeSize() {
		contentText.setSize(getSize().width,contentText.getPreferredSize().height);
	}

	@Override
	public void computePlacement() {
		int relative_x = paddingLeft;
		int relative_y = paddingTop; 
		contentText.setLocation(relative_x, relative_y);
		
	}
	
	public void addListener() {
		ActivityListItem that = this;
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {
				that.setHighlight(true);
			}			

			@Override
			public void mouseExited(MouseEvent e) {
				that.setHighlight(false);
			}
		});
	}
	
	protected void setHighlight(boolean b) {
		if(b != isHighlighted) {
			isHighlighted = b;
			this.repaint();
		}
		
	}
	
	public int getPreferredHeight() {
		return this.preferredHeight;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if(isHighlighted) {
			g.setColor(DefaultTheme.getColor("OptionHighlightColor"));
			g.fillRect(0, 0, getSize().width, getSize().height);
		}
		g.setColor(DefaultTheme.getColor("PageHeaderBorder"));
		g.fillRect(0, getSize().height - 1, getSize().width, 1);
		super.paintChildren(g);
	}
}