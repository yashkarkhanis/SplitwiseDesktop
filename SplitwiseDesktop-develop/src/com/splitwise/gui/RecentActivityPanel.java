package com.splitwise.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.splitwise.SplitwiseCore;
import com.splitwise.core.Activity;
import com.splitwise.gui.custom.ActivityListItem;
import com.splitwise.gui.custom.CJPanel;
import com.splitwise.gui.custom.CustomButton;
import com.splitwise.gui.custom.CustomImage;
import com.splitwise.gui.theme.DefaultTheme;

public class RecentActivityPanel extends CJPanel {
	private PageHeader pageHeader;
	private JPanel defaultPanel;
	private JLabel defaultPanelSubText;
	
	//DefaultPanel
	private Insets defaultPanelPadding = new Insets(20, 15, 10, 15);
	private int defaultPanelHeight = 294;
	
	private List<ActivityListItem> activityList;
	
	RecentActivityPanel() {
		activityList = new ArrayList<ActivityListItem>();
		init();
		
		/*addItem(new ActivityListItem("<strong>Sandesh M.</strong> added <strong>“Vegetable oil”</strong> in <strong>“TL 513”</strong>.<br><font color=\"#ff652f\">You owe $0.92</font>"));
		addItem(new ActivityListItem("<strong>Sandesh M.</strong> updated <strong>“Bounty ”</strong> in <strong>“TL 513”</strong>.<br><font color=\"#ff652f\">You owe $2.85</font>"));
		addItem(new ActivityListItem("<strong>You</strong> updated <strong>“Toilet paper”</strong> in <strong>“TL 513”</strong>.<br><font color=\"#999999\">You do not owe anything</font>"));*/
		
		//showDefaultPanel();
	}
	@Override
	public void initComponents() {
		pageHeader = new PageHeader("Recent Activity");
		defaultPanel = new JPanel();
		
		initDefaultPanel();
		
		add(defaultPanel);
		add(pageHeader);
	}
	
	public void initDefaultPanel() {
		defaultPanel.setLayout(null);
		defaultPanel.setOpaque(false);
		defaultPanel.setVisible(false);
		
		defaultPanelSubText = new JLabel("There is no activity in your account yet. Try adding an expense!");
		defaultPanelSubText.setHorizontalAlignment(SwingConstants.LEFT);
		
		defaultPanelSubText.setVerticalAlignment(SwingConstants.CENTER);
		defaultPanelSubText.setFont(new Font("Helvetica Neue",Font.PLAIN,16));
		defaultPanelSubText.setForeground(DefaultTheme.getColor("SecondaryForeground"));
		
		defaultPanelSubText.setSize(defaultPanelSubText.getPreferredSize());
		
		defaultPanel.add(defaultPanelSubText);
	}
	
	public void hideAll() {
		defaultPanel.setVisible(false);
		for(ActivityListItem ali : activityList) {
			remove(ali);
		}
		activityList.clear();
	}
	
	public void showDefaultPanel() {
		hideAll();
		defaultPanel.setVisible(true);
	}
	
	public void showRecentActivity() {
		hideAll();
		for(Activity activity : SplitwiseCore.getInstance().getActivities()) {
			addItem(new ActivityListItem(activity.getContent()));
		}
		computeSize();
		computePlacement();
 	}
	
	public void showActivities() {
		if(SplitwiseCore.getInstance().getActivities().size() > 0) {
			showRecentActivity();
		} else {
			showDefaultPanel();
		}
		this.repaint();
	}
	@Override
	public void configureComponents() {
		setLayout(null);
		setOpaque(false);
	}

	@Override
	public void computeSize() {
		pageHeader.setSize(getSize().width,pageHeader.getSize().height);
		
		defaultPanel.setSize(getSize().width,defaultPanelHeight);
		defaultPanelSubText.setSize(defaultPanelSubText.getPreferredSize());
		
		for(ActivityListItem ali : activityList) {
			ali.setSize(getSize().width, ali.getPreferredHeight());
		}
	}

	@Override
	public void computePlacement() {
		pageHeader.setLocation(0,0);
		defaultPanel.setLocation(0,pageHeader.getSize().height);
		defaultPanelSubText.setLocation(defaultPanelPadding.left, defaultPanelPadding.top);
		
		int relative_y = pageHeader.getSize().height;
		for(ActivityListItem ali : activityList) {
			ali.setLocation(0, relative_y);
			relative_y += ali.getPreferredHeight();
		}
	}
	
	public void addItem(ActivityListItem ali) {
		activityList.add(ali);
		add(ali);
	}

}