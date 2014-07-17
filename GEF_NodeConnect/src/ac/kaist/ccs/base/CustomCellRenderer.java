package ac.kaist.ccs.base;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomCellRenderer extends DefaultTableCellRenderer {
	
	public static Color headerBG = new Color(79, 129, 189);
	public static Color focusRowBG = new Color(168, 176, 192);
	public static Color oddRowBG = new Color(208, 216, 232);
	public static Color evenRowBG = new Color(233, 237, 244);
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent
	 * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Component rendererComp = super.getTableCellRendererComponent(table,
				value, isSelected, hasFocus, row, column);

		// Set foreground color
		
		if(isSelected)
			rendererComp.setBackground(focusRowBG);
		else if (row %2 == 0){
			rendererComp.setBackground(oddRowBG);
		}else{
			rendererComp.setBackground(evenRowBG);
		}
		
//		rendererComp.setForeground(Color.red);
//
//		// Set background color
//		rendererComp.setBackground(Color.blue);

		return rendererComp;
	}

}