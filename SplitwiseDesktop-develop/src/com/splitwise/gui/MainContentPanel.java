package com.splitwise.gui;

import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import com.splitwise.gui.custom.CJPanel;
import com.splitwise.gui.theme.DefaultTheme;

public class MainContentPanel extends CJPanel {
	private RightPanel rightPanel;
	private LeftPanel leftPanel;
	private MidPanel midPanel;
	
	private int contentWidth = 980;
	
	MainContentPanel() {
		init();
	}
	@Override
	public void initComponents() {
		rightPanel = new RightPanel();
		leftPanel = new LeftPanel();
		midPanel = new MidPanel();
		
		add(rightPanel);
		add(midPanel);
		add(leftPanel);
	}

	@Override
	public void configureComponents() {
		setLayout(null);
		setBackground(DefaultTheme.getColor("mainContentPanelBackground"));
	}

	@Override
	public void computeSize() {
		// TODO Auto-generated method stub
		midPanel.setSize(
				contentWidth 
				- leftPanel.getSize().width
				- rightPanel.getSize().width,
				getSize().height
				);
		leftPanel.setSize(leftPanel.getSize().width, getSize().height);
		leftPanel.computeSize();
		rightPanel.setSize(rightPanel.getSize().width, getSize().height);
	}
	
	public void showDashboard() {
		rightPanel.hideGroupSummary();
		midPanel.showDashboardPanel();
		repaint();
	}
	
	public void showAllExpenses() {
		rightPanel.hideGroupSummary();
		midPanel.showExpensePanel();
		repaint();
	}
	
	public void showFriendExpenses(long friendId) {
		rightPanel.hideGroupSummary();
		midPanel.showFriendExpensePanel(friendId);
		repaint();
	}
	
	public void showGroupExpenses(long groupId) {
		rightPanel.showGroupSummary();
		midPanel.showGroupExpensePanel(groupId);
		repaint();
	}
	
	public void showRecentActivity() {
		rightPanel.hideGroupSummary();
		midPanel.showRecentActivityPanel();
		repaint();
	}

	@Override
	public void computePlacement() {
		midPanel.setLocation((getSize().width - midPanel.getSize().width)/2,0);
		leftPanel.setLocation(midPanel.getLocation().x - leftPanel.getSize().width,0);
		leftPanel.computePlacement();
		rightPanel.setLocation(midPanel.getLocation().x + midPanel.getSize().width,0);
		
	}
	public void reInitLeftPanel() {
		leftPanel.initComponents();
		leftPanel.computeSize();
		leftPanel.computePlacement();
		leftPanel.repaint();
		
	}
	public void gotNewNotification(int count) {
		leftPanel.gotNewNotification(count);
	}
	

}
