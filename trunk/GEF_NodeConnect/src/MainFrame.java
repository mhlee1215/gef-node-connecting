import  java.awt.FlowLayout;
import  java.awt.Dimension;
import  javax.swing.*;
 
public class MainFrame
{
   public static void main (String[] args) {
      //Window-Frame
      JFrame frame       = new JFrame ( "Test LayeredPane" );
 
      //cast the JContentPane to JPanel
      JPanel contentPane = (JPanel) frame.getContentPane ();
 
      //the panel that is on top
      JPanel topPanel    = new JPanel ( new FlowLayout () );
 
      frame.setSize(new Dimension(305, 115));
 
      //add Components to the topPanel
      topPanel.add ( new JLabel( "This is a JLabel" ) );
      topPanel.add ( new JTextField ( "This is a JTextField" ) );
 
      //setOpaque(false) sets the Panels transparent
      topPanel.setOpaque    ( false );
      contentPane.setOpaque ( false );
 
      //add the topPanel to the frames contentPane
      contentPane.add ( topPanel );
 
      //Select an ImageIcon to use as Background-Image
      ImageIcon bgimage = new ImageIcon ( "O:\\undecon1.gif" );
 
      //get the ScreenSize
      Dimension screendim = frame.getToolkit ().getScreenSize ();
      double xfaktor = screendim.getWidth  () / bgimage.getIconWidth ();
      double yfaktor = screendim.getHeight () / bgimage.getIconHeight ();
 
      //background-panel
      JPanel bgpanel = new JPanel ( null );
      JLabel bglabel = null;
 
      //add as much labels with the selected imageIcon that the panel fits
      //the full screen (needed when frame is resized)
      for ( int x = 0; x <= xfaktor; x++ ) {
         for ( int y = 0; y <= yfaktor; y++ ) {
            bglabel = new JLabel ( bgimage );
            bgpanel.add ( bglabel );
            bglabel.setBounds ( x * bgimage.getIconWidth  (),
                                y * bgimage.getIconHeight (),
                                bgimage.getIconWidth  (),
                                bgimage.getIconHeight () );
         }
      }
 
      //set the background panel's size to screenSize
      bgpanel.setBounds ( 0, 0, (int)screendim.getWidth(), (int)screendim.getHeight() );
 
      //get the frame's layeredPane and add the background panel
      JLayeredPane layeredPane = frame.getLayeredPane ();
      layeredPane.add ( bgpanel, new Integer ( Integer.MIN_VALUE ) );
 
      //display frame
      frame.setVisible(true);
   }
}