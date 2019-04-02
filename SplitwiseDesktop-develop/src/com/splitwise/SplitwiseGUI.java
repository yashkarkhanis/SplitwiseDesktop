package com.splitwise;



import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;

import com.splitwise.gui.AddBillModel;
import com.splitwise.gui.LoginPanel;
import com.splitwise.gui.MainFrame;
import com.splitwise.splitwisesdk.SplitwiseSDK;


public class SplitwiseGUI{
	private static SplitwiseGUI instance;
    private MainFrame mainFrame;
    
    final private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    SplitwiseCore core;
	SplitwiseSDK sdk;
	SplitwiseGUI() {}
	public void init() {
		
		instance = this;
		mainFrame = MainFrame.getInstance();
		mainFrame.showDefaultPane();
		mainFrame.setVisible(true);
		
		// Default action is to first login
		//login();
	}
	
	public void login() {
		// Since login requires asynchronous call to web server
		new Thread(){
			public void run() {
				LOGGER.info("Checking valid access token");
				sdk = SplitwiseSDK.getInstance();
				if(!sdk.hasValidAccessToken()) {
					LOGGER.info("Not Has valid access token");
					showLoginPanel();
				} else {
					LOGGER.info("Has valid access token");
					grantLogin();	
				}
			}
		}.start();
	}
	
	public void showLoginPanel() {
			LOGGER.info("Loading login panel");
			mainFrame.showLoginPane();
	}
	
	public void grantLogin() {
		LOGGER.info("Granting Loging");
		mainFrame.showDefaultPane();
		//Fetch data from the server on separate thread
		new Thread() {
			public void run() {
				LOGGER.info("Initializing Core");
				core = SplitwiseCore.getInstance();
				core.setCallback(()->mainFrame.showMainPane());
				//core.setCallback(()->mainFrame.showGroupMemberModel());
				core.initialize();
			}
		}.start();
		mainFrame.repaint();
	}
	
	public void showDashboard() {
		// Do All necessary activity before loading Dashboard
		mainFrame.showDashboard();
	}
	
	public void showAllExpenses() {
		// Do All necessary activity before loading All Expenses
		SplitwiseCore.getInstance().setFilterByFriendId(0);
		SplitwiseCore.getInstance().setFilterByGroupId(0);
		mainFrame.showAllExpenses();
	}
	
	public void showRecentActivity() {
		// Do All necessary activity before loading Recent Activity
		mainFrame.showRecentActivity();
	}
	
	public static SplitwiseGUI getInstance() {
		return instance;
	}
	
	public void showAddBill(long peopleId, long groupId, AddBillModel.Callback saveCallback) {
		mainFrame.showAddBill(peopleId, groupId, saveCallback);
	}
	
	public void showFriendModel() {
		mainFrame.showFriendModel();
	}
	
	public void showGroupModel() {
		mainFrame.showGroupModel();
	}
	
	public void showGroupMemberModel() {
		mainFrame.showGroupMemberModel();
	}
	
	public void showExpenses(long friendId) {
		SplitwiseCore.getInstance().setFilterByGroupId(0);
		SplitwiseCore.getInstance().setFilterByFriendId(friendId);
		mainFrame.showFriendExpenses(friendId);
		
	}
	public void showGroupExpenses(long groupId) {
		SplitwiseCore.getInstance().setFilterByFriendId(0);
		SplitwiseCore.getInstance().setFilterByGroupId(groupId);
		mainFrame.showGroupExpenses(groupId);
	}
	
}
