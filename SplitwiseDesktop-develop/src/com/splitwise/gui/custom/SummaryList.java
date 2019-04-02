package com.splitwise.gui.custom;

import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.splitwise.gui.theme.DefaultTheme;

public class SummaryList extends CJPanel {
		private JLabel headerText;
		
		private int headerTextHeight = 46;
		private int preferredHeight = 0;
		private Insets contentPadding = new Insets(0,15,0,15);
		
		private List<SummaryListItem> itemList;
		
		public SummaryList() {
			itemList = new ArrayList<SummaryListItem>();
			
			init();
		}
		@Override
		public void initComponents() {
			headerText = new JLabel("");
			headerText.setForeground(DefaultTheme.getColor("SecondaryForeground"));
			headerText.setFont(new Font("Helvetica Neue",Font.BOLD,14));
			headerText.setVerticalAlignment(SwingConstants.CENTER);
			add(headerText);
		}

		public void setHeaderText(String text) {
			headerText.setText(text.toUpperCase());
		}
		
		public void setHeaderTextAlignement(int alignment) {
			headerText.setHorizontalAlignment(alignment);
		}
		
		public int getPreferredHeight() {
			return this.preferredHeight;
		}
		
		public void removeAll() {
			for(SummaryListItem sil : itemList) {
				super.remove(sil);
			}
			itemList.clear();
			computeSize();
			computePlacement();
		}
		public void addItem(SummaryListItem item) {
			itemList.add(item);
			add(item);
			this.repaint();
		}
		
		@Override
		public void configureComponents() {
			// TODO Auto-generated method stub
		}

		@Override
		public void computeSize() {
			headerText.setSize(getSize().width - contentPadding.left - contentPadding.right, headerTextHeight);
			
			for(SummaryListItem sil : itemList) {
				sil.setSize(getSize().width, sil.getPreferredHeight());
				sil.computeSize();
			}
		}

		@Override
		public void computePlacement() {
			preferredHeight = 0;
			headerText.setLocation(contentPadding.left, 0);
			preferredHeight += headerText.getHeight();
			
			for(SummaryListItem sil : itemList) {
				sil.setLocation(0, preferredHeight);
				sil.computePlacement();
				preferredHeight += sil.getHeight();
				LOGGER.info(sil.toString());
			}
			LOGGER.info("List bounds " + headerText.getText() + " "  + getBounds().toString());
		}
		
	}
