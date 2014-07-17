package ac.kaist.ccs.base;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {

	boolean editable = false;

	public MyTableModel() {
		this.editable = false;
	}

	public MyTableModel(boolean editable) {
		this.editable = editable;
	}

	private Vector<String> columnNames;
	private Vector<Vector> data;

	public Vector<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(Vector<String> columnNames) {
		this.columnNames = columnNames;
	}

	public Vector<Vector> getData() {
		return data;
	}

	public void setData(Vector<Vector> data) {
		this.data = data;
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	public Object getValueAt(int row, int col) {
		return data.get(row).get(col);
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column would
	 * contain text ("true"/"false"), rather than a check box.
	 */
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		return editable;
		// if (col < 2) {
		// return false;
		// } else {
		// return true;
		// }
	}

}