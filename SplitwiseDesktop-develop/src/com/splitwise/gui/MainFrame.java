package com.splitwise.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.splitwise.SplitwiseCore;
import com.splitwise.gui.custom.CustomImage;
import com.splitwise.gui.theme.DefaultTheme;
import com.splitwise.splitwisesdk.SplitwiseSDK;

import java.util.logging.*;

public class MainFrame extends JFrame implements ComponentListener{
	private static MainFrame instance;
	private HeaderPanel headerPanel;
	private MainContentPanel mainContentPanel;
	private JLabel splitwiseLogo;
	private CustomImage splitwiseLogoImage;
	private JPanel defaultPanel;
	private LayoutManager defaultLayoutManager;
	private JLayeredPane layeredPane;
	public JPanel backdrop;
	
	
	private String splitwiseLogoFilename = "assets/SplitwiseLogo.png";
	
	final private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public MainFrame() {
		configureComponents();
		initComponents();
		computeSize();
		computePlacement();
		
		//LOGGER.finest("Size of Content Pane" + getContentPane().getSize());
		//LOGGER.finest("Size of Default Pane" + defaultPanel.getSize());
		//LOGGER.setLevel(Level.FINEST);
	}
	
	public static MainFrame getInstance() {
		if(instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}
	private void initComponents() {
		layeredPane = new JLayeredPane();
		
		defaultPanel = new JPanel();
		defaultPanel.setLayout(null);
		defaultPanel.setBackground(DefaultTheme.getColor("mainFrameBackground"));
		
		splitwiseLogoImage = new CustomImage(splitwiseLogoFilename);
		splitwiseLogo = new JLabel(splitwiseLogoImage.setSize(200,200).getImageIcon());
		
		defaultPanel.add(splitwiseLogo);
		
		getContentPane().setLayout(null);
		//getContentPane().add(defaultPanel);
		
		getContentPane().add(layeredPane);
		layeredPane.setBackground(DefaultTheme.getColor("mainFrameBackground"));
		layeredPane.setOpaque(true);
		layeredPane.add(defaultPanel,JLayeredPane.DEFAULT_LAYER);
		
		addComponentListener(this);
	}
	
	public void initMainPane() {
		LOGGER.info("Initializing Main Page");
		//getContentPane().setLayout(null);
		
		//Initialize Header Panel
		headerPanel = new HeaderPanel();
		
		//Initialize Main Content Panel
		mainContentPanel = new MainContentPanel();
		
		computeSize();
		computePlacement();
		
		//revalidate();
	}
	
	public void showLoginPane() {
		LoginPanel lp = LoginPanel.getInstance();
		String url = SplitwiseSDK.getInstance().getAuthorizationURL();
		lp.load(url);
		lp.setVisible(false);
		lp.setLocation(0, 0);
		lp.setSize(layeredPane.getSize());
		layeredPane.add(lp, JLayeredPane.POPUP_LAYER);
		repaint();
	}
	public void showMainPane() {
		if(headerPanel == null && mainContentPanel == null) {
			initMainPane();
		}
		layeredPane.removeAll();
		layeredPane.add(headerPanel,0);
		layeredPane.add(mainContentPanel,0);
		
		showDashboard();
	}
	
	public void showDefaultPane() {
		for(Component comp : layeredPane.getComponentsInLayer(JLayeredPane.POPUP_LAYER)) {
			layeredPane.remove(comp);
			LOGGER.fine("Removing " + comp.getClass().getCanonicalName());
		}
		layeredPane.moveToFront(defaultPanel);
	}
	
	public void showDashboard() {
		this.mainContentPanel.showDashboard();
	}
	public void showAllExpenses() {
		this.mainContentPanel.showAllExpenses();
		repaint();
	}
	
	public void showFriendExpenses(long friendId) {
		this.mainContentPanel.showFriendExpenses(friendId);
		repaint();
	}
	public void showGroupExpenses(long groupId) {
		this.mainContentPanel.showGroupExpenses(groupId);
		repaint();
	}
	
	public void showRecentActivity() {
		this.mainContentPanel.showRecentActivity();
		repaint();
	}
	public LayoutManager getDefaultLayoutManager() {
		return this.defaultLayoutManager;
	}
	
	private void configureComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
		getLayeredPane().setBackground(DefaultTheme.getColor("mainFrameBackground"));
		defaultLayoutManager = getLayeredPane().getLayout();
	}
	
	private void computeSize() {
		getContentPane().setSize(getSize());
		layeredPane.setSize(getSize());
		Dimension contentPanelDimension = getLayeredPane().getSize();
		
		if(defaultPanel != null) {
			defaultPanel.setSize(getSize());
			splitwiseLogo.setSize(200,200);
			LOGGER.info("Default Pane " + contentPanelDimension);
		}
		
		if(headerPanel != null) {
			headerPanel.setSize(contentPanelDimension.width,headerPanel.getSize().height);
			LOGGER.finest("Header Panel size " + headerPanel.getSize());
			headerPanel.computeSize();
		}
		
		if(mainContentPanel != null) {
			mainContentPanel.setSize(contentPanelDimension.width, contentPanelDimension.height - headerPanel.getSize().height);
			LOGGER.finest("Main Content Panel size " + mainContentPanel.getSize());
			mainContentPanel.computeSize();
		}
		
		if(backdrop != null) {
			backdrop.setSize(contentPanelDimension);
		}
	}
	
	private void computePlacement() {
		layeredPane.setLocation(0,0);
		if(defaultPanel != null) {
			defaultPanel.setLocation(0, 0);
			splitwiseLogo.setLocation(
					(defaultPanel.getSize().width - splitwiseLogo.getSize().width)/2,
					(defaultPanel.getSize().height - splitwiseLogo.getSize().height)/2
					);
		}
		
		
		if(headerPanel != null) {
			headerPanel.setLocation(0, 0);
			headerPanel.computePlacement();
		}
		
		if(mainContentPanel != null) {
			mainContentPanel.setLocation(0, headerPanel.getSize().height);
			mainContentPanel.computePlacement();
		}
		
		if(backdrop != null) {
			backdrop.setLocation(0,0);
		}
	}
	
	/*public void paint(Graphics g) {
		//computeSize();
		//computePlacement();
		super.paint(g);
		LOGGER.info("Main Frame Paint event triggered");
	}*/

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		LOGGER.info("Main Frame resized event triggered");
		LOGGER.info("Content Pane Size" + getLayeredPane().getSize());
		computeSize();
		computePlacement();
		getContentPane().revalidate();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void createBackdrop() {
		backdrop = new JPanel();
		backdrop.setLayout(null);
		backdrop.setBackground(new Color(0,0,0,100));
		backdrop.setSize(getContentPane().getSize());
		backdrop.setLocation(0,0);
		
		layeredPane.add(backdrop,10000);
		layeredPane.moveToFront(backdrop);
		
		backdrop.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				LOGGER.info("Mouse Clicked on backdrop");
				hideBackdrop();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	public void showAddBill(long peopleId, long groupId, AddBillModel.Callback saveCallback) {
		createBackdrop();
		
		AddBillModel adb = null;
		if(peopleId != 0) {
			adb = new AddBillModel(SplitwiseCore.getInstance().getFriend(peopleId));
		} else if(groupId != 0) {
			adb = new AddBillModel(SplitwiseCore.getInstance().getGroup(groupId));
		} else {
			adb = new AddBillModel();
		}
		adb.setSaveCallback(saveCallback);
		
		backdrop.add(adb);
		
		
		adb.setSize(adb.getPreferredSize());
		adb.setLocation((getContentPane().getSize().width - adb.getSize().width)/2,
				(getContentPane().getSize().height - adb.getSize().height)/2);

		repaint();
		
	}
	
	public void hideBackdrop() {
		layeredPane.remove(backdrop);
		backdrop = null;
		layeredPane.repaint();
	}

	public void showFriendModel() {
		createBackdrop();
		
		AddFriendModel afm = new AddFriendModel();
		afm.setInviteCallback((args)->SplitwiseCore.getInstance().createFriend(args));
		
		backdrop.add(afm);
		
		
		afm.setSize(afm.getPreferredSize());
		afm.setLocation((getContentPane().getSize().width - afm.getSize().width)/2,
				(getContentPane().getSize().height - afm.getSize().height)/2);

		repaint();
		
	}
	
	public void showGroupModel() {
		createBackdrop();
		
		AddGroupModel afm = new AddGroupModel();
		afm.setSaveCallback((args)->SplitwiseCore.getInstance().createGroup(args));
		
		backdrop.add(afm);
		
		
		afm.setSize(afm.getPreferredSize());
		afm.setLocation((getContentPane().getSize().width - afm.getSize().width)/2,
				(getContentPane().getSize().height - afm.getSize().height)/2);

		repaint();
	}
	
	public void showGroupMemberModel() {
		createBackdrop();
		
		AddGroupMemberModel agmm = new AddGroupMemberModel();
		agmm.setInviteCallback((args)->SplitwiseCore.getInstance().createGroupMember(args));
		
		backdrop.add(agmm);
		
		
		agmm.setSize(agmm.getPreferredSize());
		agmm.setLocation((getContentPane().getSize().width - agmm.getSize().width)/2,
				(getContentPane().getSize().height - agmm.getSize().height)/2);
		
		repaint();
	}
	
	public void reInitLeftPanel() {
		mainContentPanel.reInitLeftPanel();
	}

	public void gotNewNotification(int count) {
		mainContentPanel.gotNewNotification(count);
		
	}

	

	
}
