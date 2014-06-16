import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
 
public class TableRowColumn extends JFrame
{
	private final static String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	JTable table;
	DefaultTableModel model;
	JPanel buttonPanel;
	JButton button;
 
	public TableRowColumn()
	{
		//  Create table
 
		Object[][] data = { {"1", "A"}, {"2", "B"}, {"3", "C"} };
		String[] columnNames = {"Number","Letter"};
		model = new DefaultTableModel(data, columnNames);
		table = new JTable(model);
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
 
		//  Add table and a Button panel to the frame
 
		JScrollPane scrollPane = new JScrollPane( table );
		getContentPane().add( scrollPane );
 
		buttonPanel = new JPanel();
		getContentPane().add( buttonPanel, BorderLayout.SOUTH );
 
		//
 
		button = new JButton( "Add Row" );
		buttonPanel.add( button );
		button.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				model.addRow( createRow() );
				int row = table.getRowCount() - 1;
				table.changeSelection(row, row, false, false);
				table.requestFocusInWindow();
			}
		});
 
		//
 
		button = new JButton( "Insert Row" );
		buttonPanel.add( button );
		button.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				model.insertRow( 0, createRow() );
				table.changeSelection(0, 0, false, false);
				table.requestFocusInWindow();
			}
		});
 
		//
 
		button = new JButton( "Empty Row" );
		buttonPanel.add( button );
		button.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				model.setRowCount( model.getRowCount() + 1 );
				int row = table.getRowCount() - 1;
				table.changeSelection(row, row, false, false);
				table.requestFocusInWindow();
			}
		});
		//
		button = new JButton( "Add Column" );
		buttonPanel.add( button );
		button.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String header = "Col" + (table.getColumnCount() + 1);
				model.addColumn( header );
				table.requestFocusInWindow();
			}
		});
		//
		button = new JButton( "Add Column & Data" );
		buttonPanel.add( button );
		button.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String header = "Col" + (table.getColumnCount() + 1);
 
				int rows = table.getRowCount();
				String[] values = new String[rows];
 
				for (int j = 0; j < rows; j++)
				{
					values[j] = Integer.toString(j);
				}
 
				model.addColumn( header, values );
				table.requestFocusInWindow();
			}
		});
		//
		button = new JButton( "Add Column - No Reordering" );
		buttonPanel.add( button );
		button.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//  Use this method when you don't want existing columns
				//  to be rebuilt from the model.
				//  (ie. moved columns will not be reordered)
 
				table.setAutoCreateColumnsFromModel( false );
				String header = "Col" + (table.getColumnCount() + 1);
				model.addColumn( header );
 
				//  AutoCreate is turned off so create table column here
 
				TableColumn column = new TableColumn( table.getColumnCount() );
				column.setHeaderValue( header );
				table.addColumn( column );
 
				// These won't work once setAutoCreate... has been set to false
				buttonPanel.getComponent(3).setEnabled( false );
				buttonPanel.getComponent(4).setEnabled( false );
				table.requestFocusInWindow();
			}
		});
		//
		button = new JButton( "Remove Last Column" );
		buttonPanel.add( button );
		button.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int columns = model.getColumnCount();
 
				if (columns > 0)
				{
 
					if (!table.getAutoCreateColumnsFromModel())
					{
						int view =
							table.convertColumnIndexToView(columns - 1);
						TableColumn column =
							table.getColumnModel().getColumn(view);
						table.getColumnModel().removeColumn( column );
					}
 
					model.setColumnCount( columns - 1 );
				}
				table.requestFocusInWindow();
			}
		});
 
	}
 
	private Object[] createRow()
	{
		Object[] newRow = new Object[2];
		int row = table.getRowCount() + 1;
		newRow[0] = Integer.toString( row );
		newRow[1] = LETTERS.substring(row-1, row);
		return newRow;
	}
 
	public static void main(String[] args)
	{
		TableRowColumn frame = new TableRowColumn();
		frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
		frame.pack();
		frame.setVisible(true);
	}
}