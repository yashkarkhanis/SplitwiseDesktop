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

public class AddBillModel extends CJPanel {

	private int preferredHeight = 100;
	private int preferredWidth = 350;
	
	private int headerPanelHeight = 39;
	private int paddingLeft = 10;
	private int paddingTop = 20;
	
	private JPanel headerPanel;
	private JLabel headerText;
	
	private JPanel inputFieldPanel;
	private JLabel inputFieldLabel;
	private FlexibleLabel inputArea;
	
	// For Body
	private JLayeredPane body;
	private FlexibleLabel description;
	private FlexibleLabel amount;
	private JLabel humanSummary;
	private JLabel details;
	private String splitAmount;
	private JLabel date;
	private String dateStr;
	private JLabel dollarSymbol;
	
	private JPanel buttonPanel;
	private CustomButton cancelButton;
	private CustomButton saveButton;
	
	private Callback saveCallback;
	private People friend;
	private Group group;
	private String name;
	private int no_of_person;
	
	public AddBillModel() {
		no_of_person = 1;
		init();
	}
	
	public AddBillModel(People people) {
		no_of_person = 1;
		if(people != null) {
			this.name = people.getFirstName();
			this.friend = people;
			no_of_person = 2;
		}
		init();
	}
	
	public AddBillModel(Group group) {
		no_of_person = 1;
		if(group != null) {
			this.name = group.getName();
			this.group = group;
			no_of_person = group.getMembers().size();
			if(no_of_person == 0) no_of_person = 1;
		}
		init();
	}
	
	@Override
	public void initComponents() {
		headerPanel = new JPanel();
		headerPanel.setLayout(null);
		headerPanel.setOpaque(false);
		//headerPanel.setBackground(DefaultTheme.getColor("ModelHeaderBackground"));
		
		headerText = new JLabel("Add a bill");
		
		headerPanel.add(headerText);
		
		inputFieldPanel = new JPanel();
		inputFieldPanel.setLayout(null);
		inputFieldPanel.setOpaque(false);
		// For padding
		inputFieldPanel.setBorder(BorderFactory.createEmptyBorder(4,10,4,10));
		
		inputFieldLabel = new JLabel("<html>With <b>you</b> and:</html>");
		inputArea = new FlexibleLabel("");
		inputArea.setPlaceholder("Enter Name");
		inputArea.setEditable(true);
		inputArea.setRows(1);
		inputFieldPanel.add(inputFieldLabel);
		inputFieldPanel.add(inputArea);
		
		if(this.friend != null) {
			inputArea.setText(this.friend.getName());
			inputArea.setEditable(false);
			inputArea.showBadge(true);
		} else if(this.group != null) {
			inputArea.setText(this.group.getName());
			inputArea.setEditable(false);
			inputArea.showBadge(true);
		}
		
		body = new JLayeredPane();
		body.setLayout(null);
		body.setOpaque(false);
		
		description = new FlexibleLabel();
		description.setPlaceholder("Enter a description");
		description.setEditable(true);
		description.setRows(1);
		description.setFont(new Font("Helvetica Neue",Font.PLAIN, 20));
		
		dollarSymbol = new JLabel("$");
		dollarSymbol.setFont(new Font("Helvetica Neue",Font.PLAIN, 30));
		
		amount = new FlexibleLabel();
		amount.setPlaceholder("0.00");
		amount.setEditable(true);
		amount.setRows(1);
		amount.setFont(new Font("Helvetica Neue",Font.PLAIN, 36));
		amount.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				float val = 0;
				try {
					val = Float.parseFloat(amount.getText());
				} catch(Exception exp) {
					val = 0;
				}
				details.setText("($" + (Math.round((val/no_of_person)*100.0f)/100.0f) + "/person)");
				details.repaint();
			}
			
		});
		
		humanSummary = new JLabel("<html>Paid by <font color=\"#5cc5a7\">You</font> and split <font color=\"#5cc5a7\">equally</font>.</html>");
		details = new JLabel("($0.00/person)");
		dateStr = calcDate(System.currentTimeMillis());
		date = new JLabel(dateStr) {
			public void paint(Graphics g) {
				g.setColor(DefaultTheme.getColor("PageHeaderBackground"));
				g.fillRoundRect(0, 0, getSize().width, getSize().height, getSize().height, getSize().height);
				g.setColor(DefaultTheme.getColor("PageHeaderBorder"));
				g.drawRoundRect(0, 0, getSize().width, getSize().height, getSize().height, getSize().height);
				super.paint(g);
			}
		};
		date.setHorizontalAlignment(SwingConstants.CENTER);
		date.setVerticalAlignment(SwingConstants.CENTER);
		date.setForeground(DefaultTheme.getColor("PrimaryForeground"));
		
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
		body.add(amount,JLayeredPane.DEFAULT_LAYER);
		body.add(dollarSymbol, JLayeredPane.DEFAULT_LAYER);
		body.add(humanSummary,JLayeredPane.DEFAULT_LAYER);
		body.add(details,JLayeredPane.DEFAULT_LAYER);
		body.add(date,JLayeredPane.DEFAULT_LAYER);
		body.add(buttonPanel, JLayeredPane.DEFAULT_LAYER);
		
		add(body);
		add(inputFieldPanel);
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
		inputFieldLabel.setSize(inputFieldLabel.getPreferredSize());
		Insets insets = inputFieldPanel.getInsets();
		inputArea.setSize(getSize().width - insets.left - insets.right - inputFieldLabel.getSize().width - paddingLeft, inputFieldLabel.getPreferredSize().height);
		inputFieldPanel.setSize(getSize().width, inputFieldLabel.getPreferredSize().height + inputFieldPanel.getInsets().bottom + inputFieldPanel.getInsets().top);
		
		// Body Width & Height
		description.setSize(183, 31);
		amount.setSize(128, 44);
		dollarSymbol.setSize(dollarSymbol.getPreferredSize());
		details.setSize(details.getPreferredSize());
		humanSummary.setSize(humanSummary.getPreferredSize());
		date.setSize(140, date.getPreferredSize().height + 10);
		buttonPanel.setSize(getSize().width, 55);
		body.setSize(getSize().width, 260);
	}

	@Override
	public void computePlacement() {
		headerPanel.setLocation(0,0);
		headerText.setLocation(paddingLeft, (headerPanel.getSize().height - headerText.getSize().height)/2);
		inputFieldPanel.setLocation(0,headerPanel.getSize().height);
		Insets insets = inputFieldPanel.getInsets();
		inputFieldLabel.setLocation(insets.left, insets.top);
		inputArea.setLocation(insets.left + inputFieldLabel.getSize().width + paddingLeft, insets.top);
		
		body.setLocation(0, inputFieldPanel.getY() + inputFieldPanel.getSize().height + paddingTop);
		
		int paddingBetween = 10;
		int relative_x = (body.getSize().width - description.getSize().width)/2;
		int relative_y = 0;
		description.setLocation(relative_x, relative_y);
		
		relative_x = (body.getSize().width - (amount.getSize().width - dollarSymbol.getSize().width - 3) )/2;
		relative_y = description.getY() + description.getSize().height + paddingBetween;
		amount.setLocation(relative_x, relative_y);
		
		relative_x = amount.getX() - dollarSymbol.getSize().width - 3;
		relative_y = relative_y + (amount.getSize().height - dollarSymbol.getSize().height)/2;
		dollarSymbol.setLocation(relative_x, relative_y);
		
		relative_x = (body.getSize().width - humanSummary.getSize().width)/2;
		relative_y = amount.getY() + amount.getSize().height + paddingBetween + paddingBetween;
		humanSummary.setLocation(relative_x, relative_y);
		
		relative_x = (body.getSize().width - details.getSize().width)/2;
		relative_y = humanSummary.getY() + humanSummary.getSize().height + paddingBetween;
		details.setLocation(relative_x, relative_y);
		
		relative_x = (body.getSize().width - date.getSize().width)/2;
		relative_y = details.getY() + details.getSize().height + paddingBetween + paddingBetween;
		date.setLocation(relative_x, relative_y);
		
		relative_y = relative_y + date.getSize().height + paddingBetween;
		buttonPanel.setLocation(0,relative_y);
		
		saveButton.setLocation(getSize().width - paddingLeft - saveButton.getWidth(), (buttonPanel.getSize().height - saveButton.getSize().height)/2);
		cancelButton.setLocation(saveButton.getX() - paddingLeft - cancelButton.getWidth(), (buttonPanel.getSize().height - cancelButton.getSize().height)/2);
		preferredHeight = body.getY() + body.getSize().height;
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
		
		// Border after inputFieldPanel
		g.setColor(DefaultTheme.getColor("PageHeaderBorder"));
		int y = inputFieldPanel.getY() + inputFieldPanel.getSize().height;
		g.drawLine(0, y, getSize().width, y);
		
		// Border for description
		g.translate(body.getX(), body.getY());
		g.setColor(DefaultTheme.getColor("PageHeaderBorder"));
		int x1 = description.getX();
		int x2 = description.getX() + description.getSize().width;
		y = description.getY() + description.getSize().height;
		g.drawLine(x1, y, x2, y);
		
		x1 = dollarSymbol.getX();
		x2 = amount.getX() + amount.getSize().width;
		y = amount.getY() + amount.getSize().height;
		g.drawLine(x1, y, x2, y);
		
		x1 = 0;
		x2 = getSize().width;
		y = date.getY() + date.getSize().height + 10;
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
		String cost = amount.getText();
		String desc = description.getText();
		LOGGER.info("cost :" + cost);
		LOGGER.info("desc :" + desc);
		if(saveCallback != null) {
			HashMap<String,String> args = new HashMap<String,String>();
			args.put("cost",cost);
			args.put("description",desc);
			if(this.friend != null) {
				args.put("friendId",String.valueOf(friend.getId()));
			} else if(this.group != null) {
				args.put("groupId",String.valueOf(group.getId()));
			} else {
				LOGGER.severe("This feature is not implemented currently.");
			}
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
