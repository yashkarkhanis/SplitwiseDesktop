package com.splitwise.gui.custom;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.splitwise.gui.theme.DefaultTheme;

public class ExpenseItem extends CJPanel {

	
	private String whoPaid;
	private String howMuchPaid;
	private float yourShare;
	private String description;
	private String month;
	private String date;
	private String groupName;
	
	private int preferredHeight = 56;
	private int lineGap = 10;
	private int paddingLeft = 15;
	
	private boolean isHighlighted;
	
	private JPanel datePanel;
	private JLabel monthLabel;
	private JLabel dateLabel;
	
	private JPanel descriptionPanel;
	private JLabel descriptionLabel;
	private JLabel groupLabel;
	
	private JPanel whoPaidPanel;
	private JLabel whoPaidLabel;
	private JLabel paidAmountLabel;
	
	private JPanel whoLentPanel;
	private JLabel whoLentLabel;
	private JLabel lentAmountLabel;
	
	public ExpenseItem() {
		this.whoPaid = "Sandesh M.";
		this.howMuchPaid = "$25.20";
		this.yourShare = 0;
		this.description = "Lyft for De Anza";
		this.groupName = "TL 513";
		this.month = "MAR";
		this.date = "02";
		
		init();
		
		addListener();
	}
	public ExpenseItem(String date, String month, String whoPaid, String howMuchPaid, float yourShare, String description, String groupName) {
		this.whoPaid = whoPaid;
		this.howMuchPaid = howMuchPaid;
		this.yourShare = yourShare;
		this.description = description;
		this.groupName = groupName;
		this.month = month;
		this.date = date;
		init();
		addListener();
	}
	@Override
	public void initComponents() {
		
		datePanel = new JPanel();
		datePanel.setLayout(null);
		datePanel.setOpaque(false);
		
		dateLabel = new JLabel(this.date);
		monthLabel = new JLabel(this.month);
		
		datePanel.add(monthLabel);
		datePanel.add(dateLabel);
		
		descriptionPanel = new JPanel();
		descriptionPanel.setLayout(null);
		descriptionPanel.setOpaque(false);
		
		descriptionLabel = new JLabel(this.description);
		groupLabel = new JLabel(this.groupName);
		
		descriptionPanel.add(descriptionLabel);
		descriptionPanel.add(groupLabel);
		
		// Who Paid Panel
		whoPaidPanel = new JPanel();
		whoPaidPanel.setLayout(null);
		whoPaidPanel.setOpaque(false);
		
		whoPaidLabel = new JLabel(this.whoPaid + " paid");
		paidAmountLabel = new JLabel(this.howMuchPaid);
		
		whoPaidPanel.add(paidAmountLabel);
		whoPaidPanel.add(whoPaidLabel);
		
		// who Lent Panel
		whoLentPanel = new JPanel();
		whoLentPanel.setLayout(null);
		whoLentPanel.setOpaque(false);
		
		whoLentLabel = new JLabel(this.whoPaid + " lent");
		lentAmountLabel = new JLabel("$" + Math.abs(this.yourShare));
		if(this.yourShare == 0) {
			whoLentLabel = new JLabel("<html>not<br/>involved</html>");	
		}
		
		whoLentPanel.add(whoLentLabel);
		whoLentPanel.add(lentAmountLabel);
		
		add(datePanel);
		add(whoPaidPanel);
		add(descriptionPanel);
		add(whoLentPanel);
	}

	@Override
	public void configureComponents() {
		monthLabel.setFont(new Font("Helvetica Neue",Font.PLAIN,14));
		dateLabel.setFont(new Font("Helvetica Neue",Font.PLAIN,20));
		descriptionLabel.setFont(new Font("Helvetica Neue",Font.PLAIN,16));
		groupLabel.setFont(new Font("Helvetica Neue",Font.PLAIN,10));
		whoPaidLabel.setFont(new Font("Helvetica Neue",Font.PLAIN,12));
		paidAmountLabel.setFont(new Font("Helvetica Neue",Font.PLAIN,16));
		whoLentLabel.setFont(new Font("Helvetica Neue",Font.PLAIN,12));
		lentAmountLabel.setFont(new Font("Helvetica Neue",Font.PLAIN,16));
		
		monthLabel.setVerticalAlignment(SwingConstants.CENTER);
		dateLabel.setVerticalAlignment(SwingConstants.CENTER);
		descriptionLabel.setVerticalAlignment(SwingConstants.CENTER);
		groupLabel.setVerticalAlignment(SwingConstants.CENTER);
		whoPaidLabel.setVerticalAlignment(SwingConstants.CENTER);
		paidAmountLabel.setVerticalAlignment(SwingConstants.CENTER);
		whoLentLabel.setVerticalAlignment(SwingConstants.CENTER);
		lentAmountLabel.setVerticalAlignment(SwingConstants.CENTER);
		
		monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		descriptionLabel.setHorizontalAlignment(SwingConstants.LEFT);
		groupLabel.setHorizontalAlignment(SwingConstants.LEFT);
		whoPaidLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		paidAmountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		whoLentLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lentAmountLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		dateLabel.setForeground(DefaultTheme.getColor("SecondaryForeground"));
		monthLabel.setForeground(DefaultTheme.getColor("SecondaryForeground"));
		groupLabel.setForeground(DefaultTheme.getColor("SecondaryForeground"));
		whoPaidLabel.setForeground(DefaultTheme.getColor("SecondaryForeground"));
		whoLentLabel.setForeground(DefaultTheme.getColor("SecondaryForeground"));
		if(this.yourShare > 0) {
			lentAmountLabel.setForeground(DefaultTheme.getColor("PositiveForeground"));
		} else if (this.yourShare < 0){
			lentAmountLabel.setForeground(DefaultTheme.getColor("NegativeForeground"));
		} else {
			lentAmountLabel.setPreferredSize(new Dimension(0,0));
			lentAmountLabel.setVisible(false);
		}
	}

	@Override
	public void computeSize() {
		datePanel.setSize(
				Math.max(dateLabel.getPreferredSize().width, monthLabel.getPreferredSize().width),
				dateLabel.getPreferredSize().height + monthLabel.getPreferredSize().height + 2);
		dateLabel.setSize(datePanel.getSize().width, dateLabel.getPreferredSize().height);
		monthLabel.setSize(datePanel.getSize().width, dateLabel.getPreferredSize().height);
		
		descriptionPanel.setSize(
				Math.max(descriptionLabel.getPreferredSize().width, groupLabel.getPreferredSize().width),
				descriptionLabel.getPreferredSize().height + groupLabel.getPreferredSize().height + 2);
		
		descriptionLabel.setSize(descriptionPanel.getSize().width, descriptionLabel.getPreferredSize().height);
		groupLabel.setSize(descriptionPanel.getSize().width, groupLabel.getPreferredSize().height);
		
		// Who Paid Panel
		whoPaidPanel.setSize(
				Math.max(whoPaidLabel.getPreferredSize().width, paidAmountLabel.getPreferredSize().width),
				whoPaidLabel.getPreferredSize().height + paidAmountLabel.getPreferredSize().height-2);
		
		whoPaidLabel.setSize(whoPaidPanel.getSize().width, whoPaidLabel.getPreferredSize().height);
		paidAmountLabel.setSize(whoPaidPanel.getSize().width, paidAmountLabel.getPreferredSize().height);
		
		// Who Lent Panel
		whoLentPanel.setSize(135,getSize().height);
		
		whoLentLabel.setSize(whoLentPanel.getSize().width, whoLentLabel.getPreferredSize().height);
		lentAmountLabel.setSize(whoLentPanel.getSize().width, lentAmountLabel.getPreferredSize().height);
	}

	@Override
	public void computePlacement() {
		int relative_x = paddingLeft;
		int relative_y = (getSize().height - datePanel.getSize().height)/2;
		monthLabel.setLocation(0, 0);
		dateLabel.setLocation(0,datePanel.getSize().height - dateLabel.getSize().height);
		datePanel.setLocation(relative_x, relative_y);
		
		relative_y = (getSize().height - descriptionPanel.getSize().height)/2;
		descriptionLabel.setLocation(0, 0);
		groupLabel.setLocation(0,descriptionPanel.getSize().height - groupLabel.getSize().height);
		descriptionPanel.setLocation(datePanel.getLocation().x + datePanel.getSize().width + paddingLeft,
				relative_y);
		
		relative_y = (getSize().height - whoPaidPanel.getSize().height)/2;
		whoPaidLabel.setLocation(0, 0);
		paidAmountLabel.setLocation(0,whoPaidPanel.getSize().height - paidAmountLabel.getSize().height);
		whoPaidPanel.setLocation(getSize().width - whoPaidPanel.getSize().width - 145,
				relative_y);
		
		whoLentLabel.setLocation(0, (whoLentPanel.getSize().height - whoLentLabel.getSize().height - lentAmountLabel.getSize().height)/2 + 1);
		lentAmountLabel.setLocation(0, whoLentLabel.getY() + whoLentLabel.getSize().height - 2);
		whoLentPanel.setLocation(getSize().width - 135,0);
		
	}
	
	public void addListener() {
		ExpenseItem that = this;
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {
				that.setHighlight(true);
			}			

			@Override
			public void mouseExited(MouseEvent e) {
				that.setHighlight(false);
			}
		});
	}
	
	protected void setHighlight(boolean b) {
		if(b != isHighlighted) {
			isHighlighted = b;
			this.repaint();
		}
		
	}
	
	public int getPreferredHeight() {
		return this.preferredHeight;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if(isHighlighted) {
			g.setColor(DefaultTheme.getColor("OptionHighlightColor"));
			g.fillRect(0, 0, getSize().width, getSize().height);
		}
		g.setColor(DefaultTheme.getColor("PageHeaderBorder"));
		g.fillRect(0, getSize().height - 1, getSize().width, 1);
		super.paintChildren(g);
	}
}