package com.splitwise.gui.custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.splitwise.gui.LeftPanel;
import com.splitwise.gui.MainFrame;
import com.splitwise.gui.theme.DefaultTheme;

public class OptionItem extends CJPanel{
	private String text;
	
	// Placing
	private int paddingLeft = 8;
	private int paddingTop = 3;
	private int paddingRight = 8;
	private int paddingBottom = 2;
	private int highlighterLeft = 6;
	private int height = 24;
	private boolean isSelected;
	private boolean isHighlighted;
	private boolean isSeparator;
	private boolean isMessage;
	private int fontSize;
	
	// Components
	private JLabel textLabel;
	private JLabel icon;
	private JLabel addLabel;
	private Font font;
	
	// Callback
	private Callback callback;
	private Callback addButtonCallback;
	public static String MESSAGE = "MESSAGE";
	public static String HEADER = "HEADER";
	public static String OPTION = "OPTION";
	
	private long id; //Id for friend or group

	private boolean showAddButton;
	
	public OptionItem(String text, String type) {
		this.text = text;
		fontSize = 16;
		if(type == MESSAGE) {
			this.isMessage = true;
			fontSize = 11;
			height = 30;
		} else if(type == HEADER) {
			this.isSeparator = true;
			height = 20;
			fontSize = 11;
		} 
		this.font = new Font("Helvetica Neue",Font.PLAIN, fontSize);
		this.init();
		if(type == OPTION) {
			addListener();
		}
		
	}
	
	public OptionItem(String text) {
		this(text, OPTION);
	}
	
	public OptionItem(String text, Callback callback) {
		this(text);
		this.callback = callback;
	}
	
	public void showAddButton(Callback callback) {
		this.addButtonCallback = callback;
		this.showAddButton = true;
		this.addLabel.setVisible(true);
		addListener();
		this.repaint();
	}
	
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	public long getFriendId() {
		return this.id;
	}
	
	public long getGroupId() {
		return this.id;
	}
	
	public void setFriendId(long id) {
		this.id = id;
	}
	public void setGroupId(long id) {
		this.id = id;
	}
	
	public String getText() {
		return this.text;
	}
	public void setText(String text) {
		this.text = text;
		textLabel.setText(text);
	}

	@Override
	public void initComponents() {
		textLabel = new JLabel(text);
		addLabel = new JLabel("+ add");
		
		textLabel.setForeground(DefaultTheme.getColor("OptionForeground"));
		if(isSeparator) {
			textLabel.setText(textLabel.getText().toUpperCase());
			textLabel.setForeground(DefaultTheme.getColor("OptionSeparatorForeground"));
			addLabel.setForeground(DefaultTheme.getColor("OptionSeparatorForeground"));
			addLabel.setVisible(false);
			addLabel.setFont(font);
			add(addLabel);
		}
		if(isMessage) {
			textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		textLabel.setFont(font);
		add(textLabel);
	}

	@Override
	public void configureComponents() {
		setLayout(null);
		setBackground(Color.WHITE);
		setSize(getSize().width, height);
		if(!isSeparator && !isMessage) {
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(isSeparator) {
			addLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}

	@Override
	public void computeSize() {
		textLabel.setSize(getSize().width - paddingLeft - paddingRight - highlighterLeft, getSize().height - paddingTop - paddingBottom);
		addLabel.setSize(addLabel.getPreferredSize().width, getSize().height - paddingTop - paddingBottom);
	}

	@Override
	public void computePlacement() {
		textLabel.setLocation(paddingLeft + highlighterLeft,paddingTop);
		addLabel.setLocation(this.getWidth() - paddingRight - addLabel.getSize().width, paddingTop);
	}
	
	public void setSelected(boolean value) {
		if(this.isSelected != value) {
			this.isSelected = value;
			if(isSelected) {
				textLabel.setFont(new Font("Helvetica Neue",Font.BOLD, 16));
				textLabel.setForeground(DefaultTheme.getColor("OptionSelectedColor"));
			} else {
				textLabel.setFont(font);
				textLabel.setForeground(DefaultTheme.getColor("OptionForeground"));
			}
			computeSize();
			computePlacement();
			//this.getParent().revalidate();
			this.repaint();
			//MainFrame.getInstance().repaint();
		}
	}
	
	public void setHighlight(boolean value) {
		if(this.isHighlighted != value) {
			this.isHighlighted = value;
			repaint();
		}
	}
	
	public void addListener() {
		OptionItem that = this;
		if(!isSeparator) {
			this.addMouseListener(new MouseListener() {
				@Override public void mouseClicked(MouseEvent e) {
					if(that.getParent() instanceof LeftPanel) {
						LeftPanel lp = (LeftPanel)that.getParent();
						OptionItem oi = lp.getSelectedItem();
						if (oi != null){
							LOGGER.info(oi.text + " -> " + that.text);
							if(oi != that) {
								oi.setSelected(false);
								lp.setSelectedItem(that);
								if(callback != null) {
									callback.callback(that);
								}
							}
						} else {
							LOGGER.info("null" + " -> " + that.text);
							lp.setSelectedItem(that);
						}
						that.setSelected(true);
					} else {
						if(callback != null) {
							callback.callback(that);
						}
					}
					LOGGER.info("Clicked");
				}
				@Override public void mousePressed(MouseEvent e) {}
				@Override public void mouseReleased(MouseEvent e) {}
				@Override public void mouseEntered(MouseEvent e) {
					that.setHighlight(true);
				}
				@Override public void mouseExited(MouseEvent e) {
					that.setHighlight(false);
				}
			});
		} else {
			addLabel.addMouseListener(new MouseListener() {
				@Override public void mouseClicked(MouseEvent e) {
					LOGGER.info("Add clicked on " + that.textLabel.getText());
					if(that.addButtonCallback != null) {
						LOGGER.info("Callback called");
						that.addButtonCallback.callback(that);
					}
				}
				@Override public void mousePressed(MouseEvent e) {}
				@Override public void mouseReleased(MouseEvent e) {}
				@Override public void mouseEntered(MouseEvent e) {
					LOGGER.info("Mouse Entered");
					String fontName = addLabel.getFont().getFontName();
					int fontSize = addLabel.getFont().getSize();
					addLabel.setForeground(addLabel.getForeground().darker());
					addLabel.setFont(new Font(fontName, Font.BOLD, fontSize));
					addLabel.repaint();
				}
				@Override public void mouseExited(MouseEvent e) {
					String fontName = addLabel.getFont().getFontName();
					int fontSize = addLabel.getFont().getSize();
					addLabel.setForeground(DefaultTheme.getColor("OptionSeparatorForeground"));
					addLabel.setFont(new Font(fontName, Font.PLAIN, fontSize));
					addLabel.repaint();
				}
				
			});
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if(isHighlighted) {
			g.setColor(DefaultTheme.getColor("OptionHighlightColor"));
			g.fillRect(0, 0, getSize().width, getSize().height);
		}
		if(isSelected) {
			g.setColor(DefaultTheme.getColor("OptionSelectedColor"));
			g.fillRect(0, 0, highlighterLeft, getSize().height);
		}
		if(isSeparator) {
			g.setColor(DefaultTheme.getColor("OptionSeparatorBackground"));
			g.fillRect(highlighterLeft, 0, getSize().width - highlighterLeft, getSize().height);
			g.setColor(DefaultTheme.getColor("OptionSeparatorBorder"));
			g.fillRect(highlighterLeft, getSize().height - 2, getSize().width - highlighterLeft, getSize().height);
		}
		super.paintChildren(g);
	}
	public boolean isSeparator() {
		return isSeparator;
	}
	
	public void clickAction() {
		if(callback != null) {
			callback.callback(this);
		}
	}
	public static interface Callback {
		public void callback(OptionItem oi);
	}
}
