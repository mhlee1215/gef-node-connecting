import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class DesktopSample {

  public static void main(String[] args) {
	  try {
          UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
      } catch (UnsupportedLookAndFeelException e) {
      	e.printStackTrace();
      } catch (ClassNotFoundException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          try {
			  UIManager.setLookAndFeel(
			    UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
			}
      } catch (InstantiationException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      } catch (IllegalAccessException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
      
    String title = "Desktop Sample";
    JFrame frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JDesktopPane desktop = new JDesktopPane();
    JInternalFrame internalFrames[] = {
        new JInternalFrame("Can Do All", true, true, true, true),
        new JInternalFrame("Not Resizable", false, true, true, true),
        new JInternalFrame("Not Closable", true, false, true, true),
        new JInternalFrame("Not Maximizable", true, true, false, true),
        new JInternalFrame("Not Iconifiable", true, true, true, false) };

    InternalFrameListener internalFrameListener = new InternalFrameIconifyListener();

    for (int i = 0, n = internalFrames.length; i < n; i++) {
      desktop.add(internalFrames[i]);
      internalFrames[i].setBounds(i * 25, i * 25, 200, 100);
      internalFrames[i].addInternalFrameListener(internalFrameListener);

      JLabel label = new JLabel(internalFrames[i].getTitle(),  JLabel.CENTER);
      Container content = internalFrames[i].getContentPane();
      content.add(label, BorderLayout.CENTER);

      internalFrames[i].setVisible(true);
    }

    JInternalFrame palette = new JInternalFrame("Palette", true, false,  true, false);
    palette.setBounds(350, 150, 300, 100);
    palette.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
    desktop.add(palette, JDesktopPane.PALETTE_LAYER);
    palette.setVisible(true);

    desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

    Container content = frame.getContentPane();
    content.add(desktop, BorderLayout.CENTER);
    frame.setSize(500, 300);
    frame.setVisible(true);
  }
}

class InternalFrameIconifyListener extends InternalFrameAdapter {
  public void internalFrameIconified(InternalFrameEvent internalFrameEvent) {
    JInternalFrame source = (JInternalFrame) internalFrameEvent.getSource();
    System.out.println("Iconified: " + source.getTitle());
  }

  public void internalFrameDeiconified(InternalFrameEvent internalFrameEvent) {
    JInternalFrame source = (JInternalFrame) internalFrameEvent.getSource();
    System.out.println("Deiconified: " + source.getTitle());
  }
}
