package com.splitwise.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.splitwise.SplitwiseCore;
import com.splitwise.core.People;
import com.splitwise.gui.custom.CJPanel;
import com.splitwise.gui.custom.OptionItem;
import com.splitwise.gui.custom.SummaryList;
import com.splitwise.gui.custom.SummaryListItem;
import com.splitwise.gui.theme.DefaultTheme;

public class SummaryPanel extends CJPanel {

	private JPanel headerPanel;
	private HeaderItem totalBalance;
	private HeaderItem youOwe;
	private HeaderItem youOwed;
	
	private int headerPanelSize = 50;
	private int preferredHeight = 0;
	private Insets contentPadding = new Insets(0,15,0,15);
	
	private SummaryList youOweList;
	private SummaryList youOwedList;
	
	SummaryPanel() {
		init();
	}
	@Override
	public void initComponents() {
		headerPanel = new JPanel() {
			public void paint(Graphics g) {
				super.paint(g);
				g.setColor(DefaultTheme.getColor("PageHeaderBorder"));
				g.fillRect(0, getSize().height - 1, getSize().width, 1);
				g.setColor(DefaultTheme.getColor("PageHeaderBorder"));
				int lineHeight = (int) (0.70 * getSize().height);
				g.drawLine(headerPanel.getSize().width/3, getSize().height/2 - lineHeight/2, getSize().width/3, getSize().height/2 + lineHeight/2);
				g.drawLine(2*getSize().width/3, getSize().height/2 - lineHeight/2, 2*getSize().width/3, getSize().height/2 + lineHeight/2);
				super.paintChildren(g);
			}
		};
		initHeaderPanel();
		
		youOweList = new SummaryList();
		youOweList.setHeaderText("You Owe");
		youOweList.setHeaderTextAlignement(SwingConstants.LEFT);
		
		youOwedList = new SummaryList();
		youOwedList.setHeaderText("You are Owed");
		youOwedList.setHeaderTextAlignement(SwingConstants.RIGHT);
		
		add(headerPanel);
		add(youOweList);
		add(youOwedList);
	}
	
	public void showSummary() {
		People currentUser = SplitwiseCore.getInstance().getCurrentUser();
		Float balance = currentUser.getNetAmount();
		LOGGER.info("Net Balance " + Math.abs(balance));
		totalBalance.setSecondayText(((balance<0)?"-$":"$") + Math.abs(balance));
		totalBalance.setPositive(balance >= 0);
		
		youOwe.setSecondayText("$" + currentUser.getTotalNegativeBalance());
		youOwed.setSecondayText("$" + currentUser.getTotalPositiveBalance());
		
		youOweList.removeAll();
		for(People people : currentUser.getOweList()) {
			LOGGER.info("Owe List" + people.getName() + people.getNetAmount());
			youOweList.addItem(new SummaryListItem(people.getName(), "$" + Math.abs(people.getNetAmount()),SummaryListItem.OWE));
		}
		
		youOwedList.removeAll();
		for(People people : currentUser.getOwedList()) {
			LOGGER.info("Owed List" + people.getName() + people.getNetAmount());
			youOwedList.addItem(new SummaryListItem(people.getName(), "$" + Math.abs(people.getNetAmount()),SummaryListItem.OWED));
		}
		youOweList.computeSize();
		youOweList.computePlacement();
		youOweList.repaint();
		youOwedList.computeSize();
		youOwedList.computePlacement();
		youOwedList.repaint();
		computeSize();
		computePlacement();
		
		this.repaint();
	}
	
	private void initHeaderPanel() {
		headerPanel.setLayout(null);
		headerPanel.setBackground(DefaultTheme.getColor("PageHeaderBackground"));
		
		totalBalance = new HeaderItem();
		totalBalance.setPrimaryText("total balance");
		totalBalance.setSecondayText("-$26.43");
		
		youOwe = new HeaderItem();
		youOwe.setPrimaryText("you owe");
		youOwe.setSecondayText("$69.40");
		
		youOwed = new HeaderItem();
		youOwed.setPrimaryText("you are owed");
		youOwed.setSecondayText("$43.90");
		youOwed.setPositive(true);
		
		add(totalBalance);
		add(youOwe);
		add(youOwed);
	}

	@Override
	public void configureComponents() {
		//setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void computeSize() {
		headerPanel.setSize(getSize().width, headerPanelSize);
		totalBalance.setSize(getSize().width/3, headerPanel.getSize().height);
		youOwe.setSize(getSize().width/3, headerPanel.getSize().height);
		youOwed.setSize(getSize().width/3, headerPanel.getSize().height);
		
		int summaryListWidth = (getSize().width)/2;
		youOweList.setSize(summaryListWidth, youOweList.getPreferredHeight());
		youOwedList.setSize(summaryListWidth, youOwedList.getPreferredHeight());
		
	}

	@Override
	public void computePlacement() {
		preferredHeight = 0;
		headerPanel.setLocation(0, 0);
		
		totalBalance.setLocation(0,0);
		youOwe.setLocation(getSize().width/3,0);
		youOwed.setLocation(2*getSize().width/3,0);
		
		preferredHeight += headerPanel.getSize().height;
		
		youOweList.setLocation(0,headerPanel.getSize().height);
		youOwedList.setLocation(getSize().width - youOweList.getSize().width,headerPanel.getSize().height);
		
		preferredHeight += Math.max(youOwedList.getSize().height, youOweList.getSize().height);
	}
	
	public int getPreferredHeight() {
		return preferredHeight;
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(DefaultTheme.getColor("PageHeaderBorder"));
		int lineHeight = Math.max(youOweList.getPreferredHeight(), youOwedList.getPreferredHeight());
		
		g.drawLine(getSize().width/2, 46 + youOweList.getY(), getSize().width/2, 46 + youOweList.getY() + lineHeight);
		super.paintChildren(g);
	}
	
	static class HeaderItem extends CJPanel {
		private JLabel primaryText;
		private JLabel secondaryText;
		private boolean isPositive;
		private int lineGap = 5;
		
		HeaderItem() {init();setOpaque(false);}
		
		public void setPrimaryText(String text) {
			primaryText.setText(text);
			primaryText.setForeground(DefaultTheme.getColor("SecondaryForeground"));
		}
		
		public void setSecondayText(String text) {
			secondaryText.setText(text);
			setPositive(isPositive);
		}
		
		public void setPositive(boolean isPositive) {
			this.isPositive = isPositive;
			if(isPositive) {
				secondaryText.setForeground(DefaultTheme.getColor("PositiveForeground"));
			} else {
				secondaryText.setForeground(DefaultTheme.getColor("NegativeForeground"));
			}
		}
		
		@Override
		public void initComponents() {
			primaryText = new JLabel();
			secondaryText = new JLabel();
			
			add(primaryText);
			add(secondaryText);
		}
		@Override
		public void configureComponents() {}
		
		@Override
		public void computeSize() {
			primaryText.setSize(primaryText.getPreferredSize());
			secondaryText.setSize(secondaryText.getPreferredSize());
		}
		@Override
		public void computePlacement() {
			int relative_x = (getSize().width - primaryText.getSize().width)/2;
			int relative_y = (getSize().height/2) - (primaryText.getSize().height - lineGap/2); 
			primaryText.setLocation(relative_x, relative_y);
			relative_x = (getSize().width - secondaryText.getSize().width)/2;
			relative_y = (getSize().height/2) + lineGap/2;
			secondaryText.setLocation(relative_x, relative_y);
		}
	}
	
}
