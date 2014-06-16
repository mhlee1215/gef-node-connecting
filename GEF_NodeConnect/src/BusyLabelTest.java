import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.util.PaintUtils;


public class BusyLabelTest {
	public static void main(String[] argv){
		JXFrame frame = new JXFrame();
		
		//JXPanel subPanel = new JXPanel();
		//mainPanel.setLayout(new GridLayout(1, 1));
		
		//subPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		
		JXPanel mainPanel = new JXPanel();
		frame.setContentPane(mainPanel);
		mainPanel.setBackgroundPainter(new MattePainter(PaintUtils.BLUE_EXPERIENCE, true));
		JXBusyLabel label = new JXBusyLabel(new Dimension(25, 25));
		label.getBusyPainter().setPoints(25);
		label.getBusyPainter().setTrailLength(12);
		label.setName("busyLabel");
        label.getBusyPainter().setHighlightColor(new Color(44, 61, 146).darker());
        label.getBusyPainter().setBaseColor(new Color(168, 204, 241).brighter());
        
        label.setBusy(true);
        label.setText("Now loading node info...");
        label.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        label.setToolTipText("2222");
       
		//subPanel.add(label);
		//mainPanel.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
		//mainPanel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        mainPanel.setLayout(new BorderLayout(mainPanel.getWidth()/2-130, 0));
        mainPanel.add(BorderLayout.WEST, new JLabel(""));
		mainPanel.add(BorderLayout.CENTER, label);
	}
}
