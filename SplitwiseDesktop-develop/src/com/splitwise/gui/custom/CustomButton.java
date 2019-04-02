package com.splitwise.gui.custom;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.splitwise.gui.MainFrame;
import com.splitwise.gui.custom.OptionItem.Callback;
import com.splitwise.gui.theme.DefaultTheme;

public class CustomButton extends JPanel {
	private String text;
	JLabel buttonLabel;
	
	boolean isHighlighted;
	boolean isPressed;
	private Callback callback;
	private String theme = "Orange";
	public static String GREYTHEME = "Grey";
	public static String GREENTHEME = "Green";
	
	final public static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static interface Callback {
		public void callback();
	}
	
	public CustomButton(String text) {
		this.text = text;
		this.isHighlighted = false;
		init();
		addListeners();
	}
	
	public CustomButton() {
		this("");
	}
	
	public void init() {
		buttonLabel = new JLabel(text);
		buttonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		buttonLabel.setVerticalAlignment(SwingConstants.CENTER);
		buttonLabel.setFont(new Font("Helvetica Neue",Font.PLAIN,15));
		buttonLabel.setForeground(DefaultTheme.getColor("Button"+theme+"Foreground"));
		buttonLabel.setSize(buttonLabel.getPreferredSize().width + 28, buttonLabel.getPreferredSize().height + 18);
		this.setSize(buttonLabel.getSize());
		this.setLayout(null);
		this.setOpaque(false);
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(buttonLabel);
	}
	
	public void setTheme(String theme) {
		this.theme = theme;
		buttonLabel.setForeground(DefaultTheme.getColor("Button"+theme+"Foreground"));
		this.repaint();
	}
	
	public void addListeners() {
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				LOGGER.info("Add Bill Button Clicked");
				CustomButton cb = (CustomButton)e.getSource();
				cb.isPressed = false;
				cb.isHighlighted = false;
				cb.repaint();
				if(callback!=null) {
					callback.callback();
				}
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if( e.getSource() instanceof CustomButton ) {
					CustomButton cb = (CustomButton)e.getSource();
					cb.isPressed = true;
					cb.repaint();
				}
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if( e.getSource() instanceof CustomButton ) {
					CustomButton cb = (CustomButton)e.getSource();
					cb.isPressed = false;
					cb.repaint();
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if( e.getSource() instanceof CustomButton && !isHighlighted) {
					CustomButton cb = (CustomButton)e.getSource();
					cb.isHighlighted = true;
					cb.repaint();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
					if( e.getSource() instanceof CustomButton && isHighlighted) {
						CustomButton cb = (CustomButton)e.getSource();
						cb.isHighlighted = false;
						cb.repaint();
					}
			}
			
		});
	}
	
	public void paint(Graphics g) {
		
		if(isPressed) {
			g.setColor(DefaultTheme.getColor("Button"+theme+"BorderBottom"));
		} else {
			g.setColor(DefaultTheme.getColor("Button"+theme));
			g.fillRoundRect(0, 0, getSize().width, getSize().height, 10, 10);
			g.setColor(DefaultTheme.getColor("Button"+theme+"BorderTop"));
			g.fillRoundRect(0, 0, getSize().width, getSize().height-1, 10, 10);
		}
		
		if(isPressed) {
			g.setColor(DefaultTheme.getColor("Button"+theme+"BorderTop"));
		} else {
			g.setColor(DefaultTheme.getColor("Button"+theme+"BorderBottom"));
		}
		g.fillRoundRect(0, 1, getSize().width, getSize().height-1, 10, 10);
		
		if(isHighlighted) {
			g.setColor(DefaultTheme.getColor("Button"+theme+"Highlight"));
		} else {
			g.setColor(DefaultTheme.getColor("Button"+theme));
		}
		
		if(isPressed) {
			g.fillRoundRect(0, 1, getSize().width, getSize().height-1, 10, 10);
		} else {
			g.fillRoundRect(0, 1, getSize().width, getSize().height-3, 10, 10);
		}
		
		if(isPressed) {
			g.translate(0, 1);
		}
		super.paint(g);
	}
	
	public void addCallback(Callback callback) {
		this.callback = callback;
	}
}
