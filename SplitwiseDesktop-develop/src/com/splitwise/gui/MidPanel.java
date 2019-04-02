package com.splitwise.gui;

import java.awt.Color;
import java.awt.Graphics;


import com.splitwise.gui.custom.CJPanel;
import com.splitwise.gui.custom.ShadowBorder;

public class MidPanel  extends CJPanel{

	private int borderLeft = 12;
	private int borderRight = 12;
	private DashboardPanel dashboardPanel;
	private RecentActivityPanel recentActivityPanel;
	private ExpensePanel expensePanel;
	
	MidPanel() {
		init();
		showDashboardPanel();
	}
	
	@Override
	public void initComponents() {
		dashboardPanel = new DashboardPanel();
		recentActivityPanel = new RecentActivityPanel();
		expensePanel = new ExpensePanel();
		
		add(dashboardPanel);
		add(recentActivityPanel);
		add(expensePanel);
	}

	@Override
	public void configureComponents() {
		setOpaque(false);
		ShadowBorder border = new ShadowBorder(0,borderLeft,0,borderRight,new Color(204,204,204),50);
		
		setBorder(border);
	}

	@Override
	public void computeSize() {
		dashboardPanel.setSize(getSize().width - borderLeft - borderRight,getSize().height);
		recentActivityPanel.setSize(getSize().width - borderLeft - borderRight,getSize().height);
		expensePanel.setSize(getSize().width - borderLeft - borderRight,getSize().height);
		
	}

	@Override
	public void computePlacement() {
		dashboardPanel.setLocation(borderLeft,0);
		recentActivityPanel.setLocation(borderLeft,0);
		expensePanel.setLocation(borderLeft,0);
		
	}
	
	public void hideAll() {
		dashboardPanel.setVisible(false);
		recentActivityPanel.setVisible(false);
		expensePanel.setVisible(false);
	}
	public void showDashboardPanel() {
		this.hideAll();
		dashboardPanel.showPanel();
		dashboardPanel.setVisible(true);
	}
	
	public void showRecentActivityPanel() {
		this.hideAll();
		recentActivityPanel.showActivities();
		recentActivityPanel.setVisible(true);
	}
	
	
	public void showExpensePanel() {
		this.hideAll();
		expensePanel.setDefaultHeader();
		expensePanel.showExpenseList();
		expensePanel.setVisible(true);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(204,204,204)); //Border Color
		g.fillRect(borderLeft-1, 0, 1, getSize().height);
		g.fillRect(getSize().width-borderRight, 0, 1, getSize().height);
		super.paintChildren(g);
		
	}

	public void showFriendExpensePanel(long friendId) {
		this.hideAll();
		expensePanel.setFriendId(friendId);
		expensePanel.showExpenseList();
		expensePanel.setVisible(true);
	}

	public void showGroupExpensePanel(long groupId) {
		this.hideAll();
		expensePanel.setGroupId(groupId);
		expensePanel.showExpenseList();
		expensePanel.setVisible(true);
	}

}
