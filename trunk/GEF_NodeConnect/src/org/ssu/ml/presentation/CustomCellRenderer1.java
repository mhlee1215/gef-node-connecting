package org.ssu.ml.presentation;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class CustomCellRenderer1 extends JTextPane implements TableCellRenderer {

	public CustomCellRenderer1() {
		StyledDocument doc = this.getStyledDocument();
		MutableAttributeSet standard = new SimpleAttributeSet();
		StyleConstants.setAlignment(standard, StyleConstants.ALIGN_LEFT);
		StyleConstants.setFontFamily(standard, "Arial");
		doc.setParagraphAttributes(0, 0, standard, true);
	}

	public Component getTableCellRendererComponent(JTable jTable, Object obj,
			boolean isSelected, boolean hasFocus, int row, int column) {
		setText((String) obj);
		return this;
	}
}