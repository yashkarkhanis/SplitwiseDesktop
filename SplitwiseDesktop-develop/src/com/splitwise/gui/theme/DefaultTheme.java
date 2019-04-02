package com.splitwise.gui.theme;

import java.awt.Color;
import java.util.HashMap;

public class DefaultTheme {
	static Color colorPalette[] = {
			fromHex("#5bc5a7"), //0
			fromHex("#eeeeee"), //1
			fromHex("#48be9d"), //2
			fromHex("#999999"), //3
			fromHex("#cccccc"), //4
			fromHex("#f6f6f6"), //5
			fromHex("#dddddd"), //6
			fromHex("#333333"), //7
			fromHex("#ff652f"), //8
			new Color(0,0,0,(int)(0.1*255)), //9
			new Color(0,0,0,(int)(0.25*255)), //10
			fromHex("#ff5216"), //11
			fromHex("#5cc5a7"), //12
			fromHex("#e9e9e9"), //13
			fromHex("#41b795"), //14
			fromHex("#F38E56"), //15
			fromHex("#101010")
	};
	static HashMap<String, Color> colorMap = new HashMap<String, Color>();
	
	static {
		// Main Frame
		colorMap.put("mainFrameBackground",Color.WHITE);
		// Header Panel
		colorMap.put("headerPanelBackground",colorPalette[0]);
		colorMap.put("headerPanelForeground",Color.WHITE);
		colorMap.put("headerPanelBorderColor",colorPalette[2]);
		
		
		// Main Content Panel
		colorMap.put("mainContentPanelBackground", Color.WHITE);
		
		// Left Panel Option
		colorMap.put("OptionSelectedColor", colorPalette[0]);
		colorMap.put("OptionHighlightColor", colorPalette[1]);
		colorMap.put("OptionForeground",colorPalette[3]);
		colorMap.put("OptionSeparatorForeground", colorPalette[4]);
		colorMap.put("OptionSeparatorBackground", colorPalette[5]);
		colorMap.put("OptionSeparatorBorder", colorPalette[6]);
		
		// Mid Panel
		colorMap.put("PageHeaderBackground", colorPalette[1]);
		colorMap.put("PageHeaderBorder", colorPalette[6]);
		colorMap.put("PageHeaderForeground", colorPalette[7]);
		
		// Foreground
		colorMap.put("PrimaryForeground",colorPalette[7]);
		colorMap.put("SecondaryForeground",colorPalette[3]);
		colorMap.put("PositiveForeground",colorPalette[0]);
		colorMap.put("NegativeForeground",colorPalette[8]);
		
		// Custom Button
		colorMap.put("ButtonOrange", colorPalette[8]);
		colorMap.put("ButtonOrangeForeground", colorPalette[1]);
		colorMap.put("ButtonOrangeBorderTop", colorPalette[9]);
		colorMap.put("ButtonOrangeBorderBottom", colorPalette[10]);
		colorMap.put("ButtonOrangeHighlight", colorPalette[11]);
		
		colorMap.put("ButtonGrey", colorPalette[5]);
		colorMap.put("ButtonGreyForeground", colorPalette[7]);
		colorMap.put("ButtonGreyBorderTop", colorPalette[9]);
		colorMap.put("ButtonGreyBorderBottom", colorPalette[10]);
		colorMap.put("ButtonGreyHighlight", colorPalette[13]);
		
		colorMap.put("ButtonGreen", colorPalette[12]);
		colorMap.put("ButtonGreenForeground", colorPalette[1]);
		colorMap.put("ButtonGreenBorderTop", colorPalette[9]);
		colorMap.put("ButtonGreenBorderBottom", colorPalette[10]);
		colorMap.put("ButtonGreenHighlight", colorPalette[14]);
		
		// Model
		colorMap.put("ModelHeaderBackground", colorPalette[12]);
		colorMap.put("ModelHeaderForeground", colorPalette[1]);
		
		//ScrollBar
		colorMap.put("scrollBarColor", colorPalette[15]);
		colorMap.put("scrollBorderColor", colorPalette[16]);
		
	}
	
	public static Color getColor(String name) {
		return colorMap.get(name);
	}
	
	static Color fromHex(String hex) {
		int r = Integer.parseInt(hex.substring(1, 3),16);
		int g = Integer.parseInt(hex.substring(3, 5),16);
		int b = Integer.parseInt(hex.substring(5, 7),16);
		return new Color(r,g,b);
	}
}
