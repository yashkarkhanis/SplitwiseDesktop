package com.splitwise.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.splitwise.SplitwiseCore;
import com.splitwise.SplitwiseGUI;
import com.splitwise.gui.custom.CJPanel;
import com.splitwise.gui.custom.CustomButton;
import com.splitwise.gui.custom.CustomImage;
import com.splitwise.gui.custom.FlexibleLabel;
import com.splitwise.gui.theme.DefaultTheme;

public class DashboardPanel extends CJPanel {
	private PageHeader pageHeader;
	private JPanel defaultPanel;
	private JPanel defaultTextPanel;
	private JLabel personLabel;
	private FlexibleLabel defaultPanelTitle;
	private FlexibleLabel defaultPanelSubText;
	private CustomButton addBillButton;
	
	private SummaryPanel summaryPanel;
	
	//DefaultPanel
	private Insets defaultPanelPadding = new Insets(30, 45, 10, 15);
	private int defaultPanelHeight = 294;
	private Dimension personLabelDimension = new Dimension(120,234);
	DashboardPanel() {
		init();
		//this.showDefaultPanel();
	}
	
	@Override
	public void initComponents() {
		pageHeader = new PageHeader("Dashboard");
		defaultPanel = new JPanel();
		
		initDefaultPanel();
		
		summaryPanel = new SummaryPanel();
		summaryPanel.setVisible(false);
		
		addBillButton = new CustomButton("Add a Bill");
		addBillButton.addCallback(()-> showAddBill());
		
		//pageHeader.add(addBillButton);
		add(defaultPanel);
		add(pageHeader);
		add(summaryPanel);
	}
	
	private void showAddBill() {
		LOGGER.info("Add Bill Button on Dashboard Clicked");
		SplitwiseGUI.getInstance().showAddBill(0, 0, null);
	}

	public void initDefaultPanel() {
		defaultPanel.setLayout(null);
		defaultPanel.setOpaque(false);
		defaultPanel.setVisible(false);
		
		defaultTextPanel = new JPanel();
		defaultTextPanel.setLayout(null);
		defaultTextPanel.setOpaque(false);
		
		personLabel = new JLabel((new CustomImage("assets/SplitwisePerson.png")).setSize(personLabelDimension).getImageIcon());
		
		defaultPanelTitle = new FlexibleLabel("Welcome to Splitwise!");
		defaultPanelSubText = new FlexibleLabel("Splitwise helps you split bills with friends.\n\n" + 
				"Click “Add a bill” above to get started");
		
		defaultPanelTitle.setHorizontalAlignment(SwingConstants.LEFT);
		defaultPanelSubText.setHorizontalAlignment(SwingConstants.LEFT);
		
		defaultPanelTitle.setVerticalAlignment(SwingConstants.CENTER);
		defaultPanelSubText.setVerticalAlignment(SwingConstants.CENTER);
		
		defaultPanelTitle.setFont(new Font("Helvetica Neue",Font.BOLD,28));
		defaultPanelSubText.setFont(new Font("Helvetica Neue",Font.PLAIN,18));
		
		defaultPanelTitle.setForeground(DefaultTheme.getColor("PrimaryForeground"));
		defaultPanelSubText.setForeground(DefaultTheme.getColor("SecondaryForeground"));
		
		defaultPanelTitle.setSize(defaultPanelTitle.getPreferredSize());
		defaultPanelSubText.setSize(defaultPanelSubText.getPreferredSize());
		
		defaultTextPanel.add(defaultPanelTitle);
		defaultTextPanel.add(defaultPanelSubText);
		
		defaultPanel.add(personLabel);
		defaultPanel.add(defaultTextPanel);
	}
	
	public void hideAll() {
		defaultPanel.setVisible(false);
		summaryPanel.setVisible(false);
	}
	
	public void showDefaultPanel() {
		hideAll();
		defaultPanel.setVisible(true);
		//LOGGER.info("DealerPanel " + defaultPanel.getBounds() + "");
		this.repaint();
	}
	
	public void showSummaryPanel() {
		hideAll();
		summaryPanel.showSummary();
		summaryPanel.setVisible(true);
		computeSize();
		computePlacement();
		this.repaint();
	}
	
	public void showPanel() {
		if(SplitwiseCore.getInstance().getFriends().size() > 0) {
			showSummaryPanel();
		} else {
			showDefaultPanel();
		}
	}

	@Override
	public void configureComponents() {}

	@Override
	public void computeSize() {
		pageHeader.setSize(getSize().width,pageHeader.getSize().height);
		
		defaultPanel.setSize(getSize().width,defaultPanelHeight);
		personLabel.setSize(personLabelDimension);
		defaultTextPanel.setSize(defaultPanel.getSize().width - personLabel.getWidth() - 2*this.defaultPanelPadding.left - this.defaultPanelPadding.right,
				defaultPanel.getSize().height - this.defaultPanelPadding.top - this.defaultPanelPadding.bottom);
		
		defaultPanelTitle.setSize(defaultTextPanel.getSize().width, defaultPanelTitle.getPreferredSize().height);
		defaultPanelSubText.setSize(defaultTextPanel.getSize().width,defaultPanelSubText.getPreferredSize().height);
		
		summaryPanel.setSize(getSize().width, summaryPanel.getPreferredHeight());
	}

	@Override
	public void computePlacement() {
		pageHeader.setLocation(0,0);
		addBillButton.setLocation(pageHeader.getSize().width - addBillButton.getSize().width - pageHeader.getPaddingRight(),
				(pageHeader.getSize().height - addBillButton.getHeight())/2);
		defaultPanel.setLocation(0,pageHeader.getSize().height);
		personLabel.setLocation(defaultPanelPadding.left, defaultPanelPadding.top);
		defaultTextPanel.setLocation(2*defaultPanelPadding.left + personLabel.getSize().width
				, 2*defaultPanelPadding.top);
		defaultPanelTitle.setLocation(0,0);
		defaultPanelSubText.setLocation(0
				, defaultPanelTitle.getLocation().y + defaultPanelTitle.getSize().height + 20);
		
		summaryPanel.setLocation(0,pageHeader.getSize().height);
	}

}

