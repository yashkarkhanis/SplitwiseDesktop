package com.splitwise.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.splitwise.SplitwiseCore;
import com.splitwise.gui.custom.CJPanel;
import com.splitwise.gui.custom.CustomImage;
import com.splitwise.gui.theme.DefaultTheme;
import com.splitwise.splitwisesdk.SplitwiseSDK;

public class HeaderPanel extends CJPanel {

	private int height = 30;
	private String splitwiseLogoFilename = "assets/Splitwise.png";
	private int contentWidth = 980;
	
	// Fonts
	private Font headerFont = new Font("Helvetica Neue",Font.PLAIN, 14);
	
	// Border
	private int bottomBorderPixel = 4; //Others are 0 for now
	
	// Components
	private JLabel headerText;
	private JLabel usernameLabel;
	private JLabel logoutButton;
	private CustomImage image;
	
	HeaderPanel() {
		init();
	}
	
	@Override
	public void initComponents() {
		image = new CustomImage(splitwiseLogoFilename);
		headerText = new JLabel(image.getImageIcon());
		usernameLabel = new JLabel("");
		usernameLabel.setText(SplitwiseCore.getInstance().getCurrentUser().getName());
		
		usernameLabel.setForeground(DefaultTheme.getColor("headerPanelForeground"));
		usernameLabel.setFont(headerFont);
		
		logoutButton = new JLabel("Logout");
		logoutButton.setForeground(DefaultTheme.getColor("headerPanelForeground"));
		logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		logoutButton.setFont(headerFont);
		
		computeSize();
		computePlacement();
		
		add(headerText);
		add(usernameLabel);
		add(logoutButton);
	}

	@Override
	public void configureComponents() {
		// TODO Auto-generated method stub
		setLayout(null);
		setBackground(DefaultTheme.getColor("headerPanelBackground"));
		setSize(getSize().width,this.height);
		setOpaque(true);
		
		// Configure Border
		Border matteBorder = BorderFactory.createMatteBorder(0,0,bottomBorderPixel,1,DefaultTheme.getColor("headerPanelBorderColor"));
		setBorder(matteBorder);
		
		logoutButton.addMouseListener(new MouseListener() {
				@Override public void mouseClicked(MouseEvent e) {
					LOGGER.info("Add clicked on Logout");
					SplitwiseSDK.getInstance().revokeOauth();
					System.exit(1);
				}
				@Override public void mousePressed(MouseEvent e) {}
				@Override public void mouseReleased(MouseEvent e) {}
				@Override public void mouseEntered(MouseEvent e) {
					LOGGER.info("Mouse Entered");
					String fontName = logoutButton.getFont().getFontName();
					int fontSize = logoutButton.getFont().getSize();
					logoutButton.setForeground(logoutButton.getForeground().darker());
					//logoutButton.setFont(new Font(fontName, Font.BOLD, fontSize));
					
					logoutButton.repaint();
				}
				@Override public void mouseExited(MouseEvent e) {
					String fontName = logoutButton.getFont().getFontName();
					int fontSize = logoutButton.getFont().getSize();
					logoutButton.setForeground(DefaultTheme.getColor("headerPanelForeground"));
					//logoutButton.setFont(new Font(fontName, Font.PLAIN, fontSize));
					
					logoutButton.repaint();
				}
				
			});
	}

	@Override
	public void computeSize() {
		
		headerText.setSize(165,12);
		headerText.setIcon(image.setSize(headerText.getSize()).getImageIcon());
		
		usernameLabel.setSize(usernameLabel.getPreferredSize());
		logoutButton.setSize(logoutButton.getPreferredSize());
	}

	@Override
	public void computePlacement() {
		
		int leftX = (getSize().width - contentWidth)/2;
		
		int relativeX = leftX + 10;
		int relativeY = (getSize().height - headerText.getSize().height)/2;
		headerText.setLocation(relativeX, relativeY);
		
		relativeX = leftX + contentWidth - usernameLabel.getSize().width;
		relativeY = (getSize().height - usernameLabel.getSize().height)/2;
		logoutButton.setLocation(relativeX, relativeY);
		
		relativeX = logoutButton.getX() - usernameLabel.getSize().width - 10;
		relativeY = (getSize().height - usernameLabel.getSize().height)/2;
		usernameLabel.setLocation(relativeX, relativeY);
		
		
		
	}

}
