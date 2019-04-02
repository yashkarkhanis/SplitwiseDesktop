package com.splitwise.gui.custom;



import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * Border with a drop shadow.
 */
public class ShadowBorder extends EmptyBorder
{
	private Color color;
	private int maxAlpha;
	
	public ShadowBorder(int top, int left, int bottom, int right, Color color, int maxAlpha) {
		super(top, left, bottom, right);
		this.color = color;
		this.maxAlpha = maxAlpha;
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Insets insets = getBorderInsets(c);
        Color oldColor = g.getColor();
        g.translate(x, y);

        if (color != null) {
            g.setColor(color);
            //Top Border
            g.fillRect(0, 0, width - insets.right, insets.top);
            
            //Left Border
            for(int i=0;i<=insets.left;i++) {
            	//g.fillRect(0, insets.top, insets.left, height - insets.top);
            	g.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),(int)((double)i*maxAlpha/(double)insets.left)));
            	g.fillRect(i, insets.top, 1, height - insets.top);
            	
            }
            g.setColor(color);
            g.fillRect(insets.left, height - insets.bottom, width - insets.left, insets.bottom);
            //Right Border
            for(int i=0;i<=insets.right;i++) {
            	//g.fillRect(0, insets.top, insets.left, height - insets.top);
            	g.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),(int)((double)i*maxAlpha/(double)insets.left)));
            	g.fillRect(width - i, 0, 1, height - insets.top);
            }
            //g.fillRect(width - insets.right, 0, insets.right, height - insets.bottom);

        } 
        
        g.translate(-x, -y);
        g.setColor(oldColor);

    }
	
	
	
}
