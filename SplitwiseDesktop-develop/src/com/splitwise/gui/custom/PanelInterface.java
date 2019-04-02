package com.splitwise.gui.custom;

import java.util.logging.Logger;

public interface PanelInterface {
	
	default void init() {
		initComponents();
		configureComponents();
		computeSize();
		computePlacement();
	}
	
	
	void initComponents();
	void configureComponents();
	void computeSize();
	void computePlacement();
	
}
