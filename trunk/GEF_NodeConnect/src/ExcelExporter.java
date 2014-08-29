import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import jxl.*;
import jxl.write.*;

public class ExcelExporter {

    void fillData(JTable table, File file) {
        try {

            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
            WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0); 
            TableModel model = table.getModel();

            for (int i = 0; i < model.getColumnCount(); i++) {
                Label column = new Label(i, 0, model.getColumnName(i));
                sheet1.addCell(column);
            }
            int j = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                for (j = 0; j < model.getColumnCount(); j++) {
                    Label row = new Label(j, i + 1, 
                            model.getValueAt(i, j).toString());
                    sheet1.addCell(row);
                }
            }
            workbook1.write();
            workbook1.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    void fillData(List<JTable> tables, List<String> sheetNames, WritableWorkbook workbook1) {
        try {

            //WritableWorkbook workbook1 = Workbook.createWorkbook(file);
        	
        	int sheetCnt = 0;
        	for(JTable table : tables){
        		WritableSheet sheet1 = workbook1.createSheet(sheetNames.get(sheetCnt), sheetCnt);
        		sheetCnt++;
                TableModel model = table.getModel();

                for (int i = 0; i < model.getColumnCount(); i++) {
                    Label column = new Label(i, 0, model.getColumnName(i));
                    sheet1.addCell(column);
                }
                int j = 0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (j = 0; j < model.getColumnCount(); j++) {
                        Label row = new Label(j, i + 1, 
                                model.getValueAt(i, j).toString());
                        sheet1.addCell(row);
                    }
                }
        	}
        	
            
            workbook1.write();
            workbook1.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String[][] data = {{"Housewares", "Rs.1275.00"},
            {"Pets", "Rs.125.00"}, {"Electronics", "Rs.2533.00"},
            {"Menswear", "Rs.497.00"}
        };
        String[] headers = {"Department", "Daily Revenue"};

        JFrame frame = new JFrame("JTable to Excel");
        DefaultTableModel model = new DefaultTableModel(data, headers);
        final JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        JButton export = new JButton("Export");
        export.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {

                try {
                    ExcelExporter exp = new ExcelExporter();
                    List<JTable> test = new ArrayList<JTable>();
                    test.add(table);
                    test.add(table);
                    List<String> sheetNames = new ArrayList<String>();
                    sheetNames.add("first");
                    sheetNames.add("second");
                    
                    WritableWorkbook workbook1 = Workbook.createWorkbook(new File("/Users/mac/Desktop/result2.xls"));
                    
                    exp.fillData(test, sheetNames, workbook1);
                    
                    //exp.fillData(table, new File("/Users/mac/Desktop/result.xls"));
                    JOptionPane.showMessageDialog(null, "Data saved at " +
                            "'/Users/mac/Desktop/result.xls' successfully", "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.getContentPane().add("Center", scroll);
        frame.getContentPane().add("South", export);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
    }
}

//Read more: http://niravjavadeveloper.blogspot.com/2011/05/java-swing-export-jtable-to-excel-file.html#ixzz3BeJ5C0co