package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ConsoleView extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private JTextArea lineNumberArea;
	private JScrollPane scrollPane;

	/**
	 * Create the panel.
	 */
	public ConsoleView() {
		setBorder(null);
		setBackground(new Color(24, 24, 24));
		setBorder(new EmptyBorder(15, 30, 15, 30));
		setLayout(new GridLayout(0, 1, 0, 0));

		textArea = new JTextArea();
		textArea.setSelectedTextColor(new Color(255, 255, 255));
		textArea.setSelectionColor(new Color(2, 110, 193));
		textArea.setBorder(new EmptyBorder(15, 15, 15, 15));
		textArea.setCaretColor(new Color(255, 255, 255));
		textArea.setForeground(new Color(255, 255, 255));
		textArea.setFont(new Font("Cascadia Code", Font.PLAIN, 20));
		textArea.setBackground(new Color(24, 24, 24));
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setText("Itz AGA_Shell\nCopyrigth (c) 2023.\nAll Rights Reserved.\n\n");
		textArea.setEditable(false);

		scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(null);
		scrollPane.setRowHeaderView(lineNumberArea);

		add(scrollPane);
		customizeScrollPaneScrollbar(scrollPane);

	}

	private void customizeScrollPaneScrollbar(JScrollPane scrollPane) {
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

	public JTextArea getTextArea() {
		return textArea;
	}

}
