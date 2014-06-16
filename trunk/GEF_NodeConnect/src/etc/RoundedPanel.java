package etc;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.UIManager;

import org.jdesktop.swingx.JXPanel;

public class RoundedPanel extends JXPanel {
 
    // all other methods remain the same
	Color panelColor;
	int curvature = 10;
   // modified to enable Gradient Painting from JXPanel
    public void paintComponent(Graphics g) {            
        super.paintComponent(g);
        panelColor = UIManager.getColor("Panel.background");
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor( panelColor.darker() ); 
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), curvature, curvature);
        
        g2.setColor(panelColor);
 
//        if(isDrawGradient()) {
//            g2.setPaint( getGradientPaint() );
//        }    
   
        g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, curvature, curvature);
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
}