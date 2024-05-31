package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TablesView extends JPanel {
    private static final long serialVersionUID = 1L;

    public TablesView(ArrayList<String> idList, ArrayList<String> lexemeList, ArrayList<String> tokenList) {
        setBackground(new Color(24, 24, 24));
        setBorder(new EmptyBorder(50, 50, 50, 50));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        JTable tableId = createTable(idList);
        JScrollPane scrollPaneIds = new JScrollPane(tableId);
        customizeScrollPane(scrollPaneIds);
        GridBagConstraints gbc_scrollPaneIds = new GridBagConstraints();
        gbc_scrollPaneIds.insets = new Insets(0, 0, 0, 5);
        gbc_scrollPaneIds.fill = GridBagConstraints.BOTH;
        gbc_scrollPaneIds.gridx = 0;
        gbc_scrollPaneIds.gridy = 1;
        gbc_scrollPaneIds.insets = new Insets(0, 0, 0, 80);
        add(scrollPaneIds, gbc_scrollPaneIds);

        JTable tableLexemes = createTableLexemes(lexemeList, tokenList);
        JScrollPane scrollPaneLexemes = new JScrollPane(tableLexemes);
        customizeScrollPane(scrollPaneLexemes);
        GridBagConstraints gbc_scrollPaneLexemes = new GridBagConstraints();
        gbc_scrollPaneLexemes.insets = new Insets(0, 0, 0, 5);
        gbc_scrollPaneLexemes.fill = GridBagConstraints.BOTH;
        gbc_scrollPaneLexemes.gridx = 1;
        gbc_scrollPaneLexemes.gridy = 1;
        gbc_scrollPaneLexemes.insets = new Insets(0, 0, 0, 80);
        add(scrollPaneLexemes, gbc_scrollPaneLexemes);
    }

    private JTable createTable(ArrayList<String> dataList) {
        String[] columnNames = {"Data"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (int i = 0; i < dataList.size(); i++) {
            String data = dataList.get(i);
            model.addRow(new String[]{data});
        }

        JTable table = new JTable(model);
        customizeTable(table);

        return table;
    }
    
    private JTable createTableLexemes(ArrayList<String> lexemeList, ArrayList<String> tokenList) {
        String[] columnNames = {"Lexeme", "Token"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (int i = 0; i < lexemeList.size(); i++) {
            model.addRow(new String[]{lexemeList.get(i), tokenList.get(i)});
        }

        JTable table = new JTable(model);
        customizeTable(table);

        return table;
    }

    private void customizeScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(null);
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();

        verticalScrollBar.setUI(new CustomScrollBarUI());
        horizontalScrollBar.setUI(new CustomScrollBarUI());
    }

    private class CustomScrollBarUI extends BasicScrollBarUI {
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createBlankButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createBlankButton();
        }

        private JButton createBlankButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void configureScrollBarColors() {
            trackColor = new Color(31, 31, 31);
            thumbColor = new Color(60, 60, 60);
            thumbDarkShadowColor = new Color(60, 60, 60);
        }
    }

    private void customizeTable(JTable table) {
        table.setFont(new Font("Cascadia", Font.PLAIN, 22));
        table.setBackground(new Color(24, 24, 24));
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.WHITE);
        table.setSelectionForeground(new Color(255, 255, 255));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setBorder(null);
        table.setRowHeight(30);

        // Alinea el contenido al centro en todas las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Establece el fondo de la tabla cuando está vacía igual que cuando está llena
        table.setBackground(new Color(24, 24, 24));

        // Personaliza el renderizado del encabezado de la tabla
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(2, 110, 193));
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setForeground(Color.WHITE);
        table.getTableHeader().setDefaultRenderer(headerRenderer);

        // Deshabilita la edición de las celdas
        table.setDefaultEditor(Object.class, null);
        table.setEditingColumn(0);

        // Deshabilita la reordenación de columnas
        table.getTableHeader().setReorderingAllowed(false);
        
    }
    
}
