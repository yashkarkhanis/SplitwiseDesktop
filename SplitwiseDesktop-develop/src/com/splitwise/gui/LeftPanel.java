package com.splitwise.gui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JPanel;

import com.splitwise.SplitwiseCore;
import com.splitwise.SplitwiseGUI;
import com.splitwise.core.Group;
import com.splitwise.core.People;
import com.splitwise.gui.custom.CJPanel;
import com.splitwise.gui.custom.OptionItem;



public class LeftPanel extends CJPanel{

	private int width = 200;
	private int optionHeight = 24;
	private List<OptionItem> links;
	private List<OptionItem> groups;
	private List<OptionItem> friends;
	private OptionItem groupsHeader;
	private OptionItem friendsHeader;
	private OptionItem selectedItem = null;
	private OptionItem recentActivity;
	private int newNotificationCount = 0;
	
	
	private int paddingTop = 10;
	private int paddingRight = 20;
	
	
	LeftPanel() {
		this.init();
		
		links.get(0).setSelected(true);
		selectedItem = links.get(0);
		
	}
	
	@Override
	public void initComponents() {
		// TODO Auto-generated method stub
		links = new ArrayList<OptionItem>();
		
		recentActivity = new OptionItem("Recent Activity", (OptionItem oi) -> showRecentActivity(oi));
		links.add(new OptionItem("Dashboard",(OptionItem oi) -> SplitwiseGUI.getInstance().showDashboard()));
		links.add(recentActivity);
		links.add(new OptionItem("All expenses", (OptionItem oi) -> SplitwiseGUI.getInstance().showAllExpenses()));
		
		groupsHeader = new OptionItem("Groups", OptionItem.HEADER);
		groupsHeader.showAddButton((arg)-> showGroupModel());
		
		groups = new ArrayList<OptionItem>();
		for(Group group : SplitwiseCore.getInstance().getGroups()) {
			Date today = new Date(System.currentTimeMillis());
			today.setMonth(today.getMonth() - 1);
			if(group.getId() != 0 && group.getUpdatedAt().after(today)) {
				OptionItem oi = new OptionItem(group.getName());
				oi.setGroupId(group.getId());
				oi.setCallback((OptionItem o) -> showGroupsExpense(o));
				groups.add(oi);
			}
		}
		
		
		friendsHeader = new OptionItem("Friends", OptionItem.HEADER);
		friendsHeader.showAddButton((arg)-> showFriendModel());
		
		friends = new ArrayList<OptionItem>();
		for(People friend : SplitwiseCore.getInstance().getFriends()) {
			Date today = new Date(System.currentTimeMillis());
			today.setMonth(today.getMonth() - 1);
			if(friend.getUpdatedAt().after(today)) {
				OptionItem oi = new OptionItem(friend.getName());
				oi.setFriendId(friend.getId());
				oi.setCallback((OptionItem o) -> showFriendsExpense(o));
				friends.add(oi);
			}
		}
		
		packComponents();
	}
	
	private void showRecentActivity(OptionItem oi) {
		oi.setText("Recent Activity");
		oi.repaint();
		newNotificationCount = 0;
		SplitwiseGUI.getInstance().showRecentActivity();
	}

	private void showFriendsExpense(OptionItem o) {
		long id = o.getFriendId();
		LOGGER.info("Option Item Selected for " + o.getText() + " with id " + o.getFriendId());
		SplitwiseGUI.getInstance().showExpenses(o.getFriendId());
		
	}

	private void showGroupsExpense(OptionItem oi) {
		long id = oi.getGroupId();
		LOGGER.info("Option Item Selected for " + oi.getText() + " with id " + oi.getGroupId());
		SplitwiseGUI.getInstance().showGroupExpenses(oi.getGroupId());
		
	}
	
	private void showFriendModel() {
		LOGGER.info("Add Bill Button on Dashboard Clicked");
		SplitwiseGUI.getInstance().showFriendModel();
	}
	
	private void showGroupModel() {
		LOGGER.info("Add Bill Button on Dashboard Clicked");
		SplitwiseGUI.getInstance().showGroupModel();
	}

	public void packComponents() {
		this.removeAll();
		
		for(OptionItem oi : links) {
			add(oi);
		}
		
		add(groupsHeader);
		
		for(OptionItem oi : groups) {
			add(oi);
		}
		
		if(groups.size() == 0) {
			add(new OptionItem("You do not have any groups yet",OptionItem.MESSAGE));
		}
		
		add(friendsHeader);
		
		for(OptionItem oi : friends) {
			add(oi);
		}
		
		if(friends.size() == 0) {
			add(new OptionItem("You do not have any friends yet",OptionItem.MESSAGE));
		}
	}

	@Override
	public void configureComponents() {
		setSize(width,getSize().height);
		setLayout(null);
		setOpaque(false);
	}

	@Override
	public void computeSize() {
		for(Component oi : this.getComponents()) {
			((OptionItem)oi).setSize(getSize().width - paddingRight,oi.getHeight());
		}
	}

	@Override
	public void computePlacement() {
		int x = 0;
		int y = paddingTop;
		for(Component c : this.getComponents()) {
			c.setLocation(x, y);
			if(c instanceof OptionItem) {
				if(((OptionItem)c).isSeparator()) {
					y = y + paddingTop;
					c.setLocation(x, y);
				}
			}
			y = y + c.getSize().height;
		}
	}

	public OptionItem getSelectedItem() {
		return this.selectedItem;
	}
	
	public void setSelectedItem(OptionItem oi) {
		if(oi != this.selectedItem) { // To avoid multiple click on same options
			this.selectedItem = oi;
		}
	}
	
	public void gotNewNotification(int count) {
		newNotificationCount += count;
		recentActivity.setText("Recent Activity (" + newNotificationCount + ")");
		recentActivity.repaint();
	}

}
