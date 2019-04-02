package com.splitwise.gui;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.splitwise.SplitwiseCore;
import com.splitwise.core.Group;
import com.splitwise.core.GroupMember;
import com.splitwise.core.People;
import com.splitwise.gui.custom.CJPanel;
import com.splitwise.gui.custom.SummaryList;
import com.splitwise.gui.custom.SummaryListItem;



public class RightPanel  extends CJPanel{

	private int width = 200;
	private SummaryList summary;
	RightPanel() {
		init();
	}
	
	@Override
	public void initComponents() {
		// TODO Auto-generated method stub
		summary = new SummaryList();
		summary.setHeaderText("Group Balances");
		summary.setHeaderTextAlignement(SwingConstants.LEFT);
		
		summary.addItem(new SummaryListItem("Vaibhaw", 20, SummaryListItem.GROUP_SUMMARY));
		summary.addItem(new SummaryListItem("Abhishek", -210, SummaryListItem.GROUP_SUMMARY));
		summary.addItem(new SummaryListItem("Aparna", 0, SummaryListItem.GROUP_SUMMARY));
		
		add(summary);
	}

	@Override
	public void configureComponents() {
		// TODO Auto-generated method stub
		setSize(width,getSize().height);
		setLayout(null);
		setOpaque(false);
	}
	
	public void hideGroupSummary() {
		summary.removeAll();
		computeSize();
		computePlacement();
		summary.setVisible(false);
		this.repaint();
	}
	public void showGroupSummary() {

		Group group = SplitwiseCore.getInstance().getCurrentGroup();
		People user = SplitwiseCore.getInstance().getCurrentUser();
		
		if(group == null) {
			return;
		}
		summary.removeAll();
		
		LOGGER.info("Showing Group Summary");
		for(GroupMember gm : group.getMembers()) {
			LOGGER.info(SplitwiseCore.getInstance().getFriend(gm.getMemberId()).getName() + " : " + gm.getBalance());
			summary.addItem(new SummaryListItem(SplitwiseCore.getInstance().getFriend(gm.getMemberId()).getName(), gm.getBalance(), SummaryListItem.GROUP_SUMMARY));
		}
		summary.computeSize();
		summary.computePlacement();
		computeSize();
		computePlacement();
		summary.setVisible(true);
		this.repaint();
	}

	@Override
	public void computeSize() {
		summary.setSize(getWidth(), summary.getPreferredHeight());
		
	}

	@Override
	public void computePlacement() {
		summary.setLocation(0, 0);
		
	}

}
