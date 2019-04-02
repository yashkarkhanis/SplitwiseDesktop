package com.splitwise.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLayeredPane;

import com.splitwise.SplitwiseCore;
import com.splitwise.core.Group;
import com.splitwise.core.People;
import com.splitwise.gui.custom.CJPanel;
import com.splitwise.gui.custom.CustomButton;
import com.splitwise.gui.custom.FlexibleLabel;
import com.splitwise.gui.theme.DefaultTheme;

public class AddGroupModel extends CJPanel {

	private int preferredHeight = 100;
	private int preferredWidth = 350;
	
	private int headerPanelHeight = 39;
	private int paddingLeft = 10;
	private int paddingTop = 20;
	
	private JPanel headerPanel;
	private JLabel headerText;
	
	//private JPanel inputFieldPanel;
	//private FlexibleLabel inputArea;
	
	// For Body
	private JLayeredPane body;
	private FlexibleLabel description;
	//private FlexibleLabel amount;
	//private JLabel humanSummary;
	//private JLabel details;
	//private String splitAmount;
	//private JLabel date;
	//private String dateStr;
	//private JLabel dollarSymbol;
	
	private JPanel buttonPanel;
	private CustomButton cancelButton;
	private CustomButton saveButton;
	
	private Callback saveCallback;
	
	public AddGroupModel() {
		init();
	}
	
	@Override
	public void initComponents() {
		headerPanel = new JPanel();
		headerPanel.setLayout(null);
		headerPanel.setOpaque(false);
		//headerPanel.setBackground(DefaultTheme.getColor("ModelHeaderBackground"));
		
		headerText = new JLabel("Start a new group");
		
		headerPanel.add(headerText);
		
		body = new JLayeredPane();
		body.setLayout(null);
		body.setOpaque(false);
		
		description = new FlexibleLabel();
		description.setPlaceholder("My group shall be called");
		description.setEditable(true);
		description.setRows(1);
		description.setFont(new Font("Helvetica Neue",Font.PLAIN, 18));
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setOpaque(false);
		
		saveButton = new CustomButton("Save");
		saveButton.setTheme(CustomButton.GREENTHEME);

		saveButton.addCallback(()->saveAction());
		cancelButton = new CustomButton("Cancel");
		cancelButton.addCallback(()-> MainFrame.getInstance().hideBackdrop());
		cancelButton.setTheme(CustomButton.GREYTHEME);

		
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);
		
		body.add(description,JLayeredPane.DEFAULT_LAYER);
		body.add(buttonPanel, JLayeredPane.DEFAULT_LAYER);
		
		add(body);
		add(headerPanel);
	}

	@Override
	public void configureComponents() {
		
		setSize(preferredWidth, preferredHeight);
		setOpaque(false);
		//setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		headerText.setFont(new Font("Helvetica Neue",Font.BOLD,18));
		headerText.setForeground(DefaultTheme.getColor("ModelHeaderForeground"));
		
	}

	@Override
	public void computeSize() {
		headerPanel.setSize(getSize().width, headerPanelHeight);
		headerText.setSize(headerText.getPreferredSize());
		
		// Input
		
		// Body Width & Height
		description.setSize(250, 31);
		buttonPanel.setSize(getSize().width, 55);
		body.setSize(getSize().width, description.getHeight() + buttonPanel.getHeight()+5);
	}

	@Override
	public void computePlacement() {
		headerPanel.setLocation(0,0);
		headerText.setLocation(paddingLeft, (headerPanel.getSize().height - headerText.getSize().height)/2);
		
		body.setLocation(0, headerPanel.getSize().height + paddingTop);
		
		int paddingBetween = 10;
		int relative_x = (body.getSize().width - description.getSize().width)/2;
		int relative_y = 0;
		description.setLocation(relative_x, relative_y);
		
		relative_y = relative_y + description.getSize().height + paddingBetween;
		buttonPanel.setLocation(0,relative_y);
		
		saveButton.setLocation(getSize().width - paddingLeft - saveButton.getWidth(), (buttonPanel.getSize().height - saveButton.getSize().height)/2);
		cancelButton.setLocation(saveButton.getX() - paddingLeft - cancelButton.getWidth(), (buttonPanel.getSize().height - cancelButton.getSize().height)/2);
		preferredHeight = body.getY() + body.getSize().height + 10;
	}
	
	public int getPreferredHeight() {
		return this.preferredHeight;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(preferredWidth, preferredHeight);
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRoundRect(0, 0, getSize().width, getSize().height, 20, 20);
		g.setColor(DefaultTheme.getColor("ModelHeaderBackground"));
		g.fillArc(0, 0, 20, 20, 90, 90);
		g.fillArc(getSize().width - 20, 0, 20, 20, 0, 90);
		g.fillRect(10, 0, getSize().width - 20, 10);
		g.fillRect(0, 10, getSize().width, headerPanel.getSize().height - 10);
		
		// Border for description
		g.translate(body.getX(), body.getY());
		g.setColor(DefaultTheme.getColor("PageHeaderBorder"));
		int x1 = description.getX();
		int x2 = description.getX() + description.getSize().width;
		int y = description.getY() + description.getSize().height;
		g.drawLine(x1, y, x2, y);
		
		x1 = 0;
		x2 = getSize().width;
		y = description.getY() + description.getSize().height + 10;
		g.drawLine(x1, y, x2, y);
		
		g.translate(-body.getX(), -body.getY());
		
		super.paint(g);
	}
	
	private int getPreferredHeight(Container c) {
		int height = 0;
		for(Component comp : c.getComponents()) {
			if(comp.isVisible()) {
				height = Math.max(height, comp.getY() + comp.getSize().height);
			}
		}
		return height;
	}

	private String calcDate(long millisecs) {
        SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
    }
	
	public void saveAction() {
		String name = description.getText();
		LOGGER.info("Name :" + name);
		if(this.saveCallback != null) {
			HashMap<String,String> args = new HashMap<String,String>();
			args.put("name",name);
			saveCallback.callback(args);
		}
		MainFrame.getInstance().hideBackdrop();
	}
	public void setSaveCallback(Callback callback) {
		this.saveCallback = callback;
	}
	public static interface Callback {
		public void callback(Map<String,String> arguments);
	}
}
