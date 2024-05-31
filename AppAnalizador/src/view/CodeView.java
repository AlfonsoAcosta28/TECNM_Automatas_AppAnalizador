package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class CodeView extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextPane textPane;
	private JTextArea lineNumberArea;
	private JPopupMenu autoCompleteMenu;
	private JList<String> autoCompleteList;
	private DefaultListModel<String> listModel;
	private JScrollPane scrollPane;
	private String reservedWordsPattern;
	private String dataTypesPattern;
	private String functionsPattern;
	private String operationsPattern;
	private String symbolsPattern;
	private boolean comment;
	private ArrayList<String> reservedWords;
	private ArrayList<String> dataTypes;
	private ArrayList<String> functions;
	private ArrayList<String> operations;
	private ArrayList<String> symbols;
	private ArrayList<String> separators;
	@SuppressWarnings("unused")
	private Highlighter.HighlightPainter highlightPainter;

	public CodeView() {

		reservedWordsPattern = "(INICIO|FIN)";
		dataTypesPattern = "(ENTERO|FLOTANTE)";
		functionsPattern = "(LEER|IMPRIMIR)";
		operationsPattern = "(SUM|RES|MUL|DIV)";
		symbolsPattern = "[]{}()=;,]";

		reservedWords = new ArrayList<>();
		reservedWords.add("INICIO");
		reservedWords.add("FIN");

		dataTypes = new ArrayList<>();
		dataTypes.add("ENTERO");
		dataTypes.add("FLOTANTE");

		functions = new ArrayList<>();
		functions.add("LEER");
		functions.add("IMPRIMIR");

		operations = new ArrayList<>();
		operations.add("SUM");
		operations.add("RES");
		operations.add("MUL");
		operations.add("DIV");

		symbols = new ArrayList<>();
		symbols.add("=");
		symbols.add("[");
		symbols.add("]");
		symbols.add("{");
		symbols.add("}");
		symbols.add("(");
		symbols.add(")");
		symbols.add(";");
		symbols.add(",");
		symbols.add("//");

		separators = new ArrayList<>();
		separators.add("\n");
		separators.add("\r");
		separators.add("\t");
		separators.add(" ");

		comment = false;

		setBackground(new Color(24, 24, 24));

		setBorder(null);
		setLayout(new GridLayout(1, 0, 0, 0));
		setBorder(new LineBorder(new Color(45, 45, 45)));

		lineNumberArea = new JTextArea();
		lineNumberArea.setSelectionColor(new Color(2, 110, 193));
		lineNumberArea.setDisabledTextColor(new Color(128, 128, 128));
		lineNumberArea.setEnabled(false);
		lineNumberArea.setBorder(new EmptyBorder(15, 30, 15, 0));
		lineNumberArea.setBackground(new Color(31, 31, 31));
		lineNumberArea.setForeground(new Color(128, 128, 128));
		lineNumberArea.setFont(new Font("Cascadia Code", Font.PLAIN, 20));
		lineNumberArea.setEditable(false);

		autoCompleteMenu = new JPopupMenu();
		listModel = new DefaultListModel<>();
		autoCompleteList = new JList<>(listModel);
		autoCompleteList.setCellRenderer(new CustomListCellRenderer());
		autoCompleteList.setBorder(null);
		autoCompleteMenu.add(autoCompleteList);
		autoCompleteMenu.setBorder(null);
		autoCompleteMenu.setBackground(new Color(45, 45, 45));
		autoCompleteList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					completeWithSelectedItem();
				}
			}
		});

		textPane = new JTextPane();
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_SPACE
						|| e.getKeyChar() == '\b') {
					hideAutoCompleteMenu();
				} else {
					if (e.getKeyCode() == KeyEvent.VK_TAB) {
						if (autoCompleteList.isShowing()) {
							e.consume();
							complete(0);
							hideAutoCompleteMenu();
						}
					} else {
						showAutoCompleteMenu();
					}
				}
			}
		});
		textPane.setSelectedTextColor(new Color(255, 255, 255));
		textPane.setBorder(new EmptyBorder(15, 30, 15, 30));
		textPane.setCaretColor(new Color(255, 255, 255));
		textPane.setForeground(new Color(255, 255, 255));
		textPane.setFont(new Font("Cascadia Code", Font.PLAIN, 20));
		textPane.setBackground(new Color(31, 31, 31));
		textPane.setSelectionColor(new Color(2, 110, 193));
		textPane.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateLineNumbers();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateLineNumbers();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateLineNumbers();
			}
		});

		scrollPane = new JScrollPane(textPane);
		scrollPane.setBackground(new Color(24, 24, 24));
		scrollPane.setBorder(null);
		scrollPane.setRowHeaderView(lineNumberArea);

		add(scrollPane);

		highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(31, 31, 31));
		textPane.setHighlighter(new DefaultHighlighter());
		customizeScrollPaneScrollbar(scrollPane);

		colors();
		updateLineNumbers();

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

	@SuppressWarnings("deprecation")
	private void showAutoCompleteMenu() {
		int caretPosition = textPane.getCaretPosition();
		Document document = textPane.getDocument();

		try {
			String textBeforeCursor = document.getText(0, caretPosition);
			String[] words = textBeforeCursor.split("\\s+");
			String currentWord = "";
			if (words.length == 0) {
				currentWord = words[0];
			} else {
				currentWord = words[words.length - 1];
			}
			ArrayList<String> suggestions = getAutoCompleteSuggestions(currentWord.toUpperCase());

			if (!suggestions.isEmpty()) {
				listModel.clear();
				for (String suggestion : suggestions) {
					listModel.addElement(suggestion);
				}
				int x = (int) textPane.modelToView(caretPosition).getX();
				int y = (int) textPane.modelToView(caretPosition).getY();
				autoCompleteMenu.show(textPane, x, y + textPane.getFontMetrics(textPane.getFont()).getHeight());

				textPane.requestFocusInWindow();
			} else {
				hideAutoCompleteMenu();
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void hideAutoCompleteMenu() {
		autoCompleteMenu.setVisible(false);
	}

	private void completeWithSelectedItem() {
		int selectedIndex = autoCompleteList.getSelectedIndex();
		@SuppressWarnings("unused")
		String actualWord = "";
		if (selectedIndex != -1) {
			int lenghtWord = 0;
			String selectedItem = listModel.get(selectedIndex);
			try {
				for (int i = textPane.getCaretPosition() - 1; i >= 0; i--) {
					if (separators.contains(textPane.getDocument().getText(i, 1))
							|| symbols.contains(textPane.getDocument().getText(i, 1))) {
						break;
					} else {
						lenghtWord++;
					}
				}
				actualWord = textPane.getDocument().getText(textPane.getCaretPosition() - lenghtWord, lenghtWord);
				textPane.getDocument().remove(textPane.getCaretPosition() - lenghtWord, lenghtWord);
				textPane.getDocument().insertString(textPane.getCaretPosition(), selectedItem, null);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
		hideAutoCompleteMenu();
	}

	private void complete(int selectedWord) {
		@SuppressWarnings("unused")
		String actualWord = "";
		if (selectedWord != -1) {
			int lenghtWord = 0;
			String selectedItem = listModel.get(selectedWord);
			try {
				for (int i = textPane.getCaretPosition() - 1; i >= 0; i--) {
					if (separators.contains(textPane.getDocument().getText(i, 1))
							|| symbols.contains(textPane.getDocument().getText(i, 1))) {
						break;
					} else {
						lenghtWord++;
					}
				}
				actualWord = textPane.getDocument().getText(textPane.getCaretPosition() - lenghtWord, lenghtWord);
				textPane.getDocument().remove(textPane.getCaretPosition() - lenghtWord, lenghtWord);
				textPane.getDocument().insertString(textPane.getCaretPosition(), selectedItem, null);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
		hideAutoCompleteMenu();
	}

	private ArrayList<String> getAutoCompleteSuggestions(String prefix) {
		ArrayList<String> suggestions = new ArrayList<>();

		for (String word : reservedWords) {
			if (word.startsWith(prefix)) {
				suggestions.add(word);
			}
		}

		for (String dataType : dataTypes) {
			if (dataType.startsWith(prefix)) {
				suggestions.add(dataType);
			}
		}

		for (String function : functions) {
			if (function.startsWith(prefix)) {
				suggestions.add(function);
			}
		}

		for (String operator : operations) {
			if (operator.startsWith(prefix)) {
				suggestions.add(operator);
			}
		}

		return suggestions;
	}

	private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}

	private void colors() {

		final StyleContext cont = StyleContext.getDefaultStyleContext();

		final AttributeSet reservedStyle = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				new Color(221, 111, 0));
		final AttributeSet dataTypesStyle = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				new Color(0, 147, 255));
		final AttributeSet functionsStyle = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				new Color(135, 255, 15));
		final AttributeSet operationsStyle = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				Color.YELLOW);
		final AttributeSet commentsStyle = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.GRAY);
		final AttributeSet symbolsStyle = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.WHITE);
		final AttributeSet defaultStyle = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.CYAN);

		DefaultStyledDocument doc = new DefaultStyledDocument() {
			private static final long serialVersionUID = 1L;

			public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
				super.insertString(offset, str, a);

				String text = getText(0, getLength());

				int before = findLastNonWordChar(text, offset);
				if (before < 0) {
					before = 0;
				}
				int after = findFirstNonWordChar(text, offset + str.length());
				int wordL = before;
				int wordR = before;

				while (wordR <= after) {
					if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
						if (text.substring(wordL, wordR).matches("(\\W)*" + symbolsPattern)) {
							comment = false;
							setCharacterAttributes(wordL, wordR - wordL + 1, symbolsStyle, false);
						} else if (text.substring(wordL, wordR).matches("(\\W)*" + reservedWordsPattern)) {
							comment = false;
							if (wordL == 0) {
								setCharacterAttributes(wordL, wordR - wordL, reservedStyle, false);
							} else {
								setCharacterAttributes(wordL + 1, wordR - wordL - 1, reservedStyle, false);
							}
						} else if (text.substring(wordL, wordR).matches("(\\W)*" + dataTypesPattern)) {
							comment = false;
							if (wordL == 0) {
								setCharacterAttributes(wordL, wordR - wordL, dataTypesStyle, false);
							} else {
								setCharacterAttributes(wordL + 1, wordR - wordL - 1, dataTypesStyle, false);
							}
						} else if (text.substring(wordL, wordR).matches("(\\W)*" + functionsPattern)) {
							comment = false;
							if (wordL == 0) {
								setCharacterAttributes(wordL, wordR - wordL, functionsStyle, false);
							} else {
								setCharacterAttributes(wordL + 1, wordR - wordL - 1, functionsStyle, false);
							}
						} else if (text.substring(wordL, wordR).matches("(\\W)*" + operationsPattern)) {
							comment = false;
							if (wordL == 0) {
								setCharacterAttributes(wordL, wordR - wordL, operationsStyle, false);
							} else {
								setCharacterAttributes(wordL + 1, wordR - wordL - 1, operationsStyle, false);
							}
						} else if (text.substring(wordL, wordR).matches("//*")) {
							comment = true;
							setCharacterAttributes(wordL, wordR - wordL + 1, commentsStyle, false);
						} else {
							if (text.substring(wordL, wordR).matches("\n")) {
								comment = false;
								if (wordL == 0) {
									setCharacterAttributes(wordL, wordR - wordL, defaultStyle, false);
								} else {
									setCharacterAttributes(wordL + 1, wordR - wordL - 1, defaultStyle, false);
								}
							} else {
								if (comment) {
									setCharacterAttributes(wordL + 1, wordR - wordL - 1, commentsStyle, false);
								} else {
									comment = false;
									if (wordL == 0) {
										setCharacterAttributes(wordL, wordR - wordL, defaultStyle, false);
									} else {
										setCharacterAttributes(wordL + 1, wordR - wordL - 1, defaultStyle, false);
									}
								}
							}
						}
						wordL = wordR;
					}
					wordR++;
				}
				updateLineNumbers();
			}

			public void remove(int offset, int len) throws BadLocationException {
				super.remove(offset, len);

				String text = getText(0, getLength());

				int before = findLastNonWordChar(text, offset);
				if (before < 0) {
					before = 0;
				}
				int after = findFirstNonWordChar(text, offset + len);
				int wordL = before;
				int wordR = before;

				try {
					while (wordR <= after) {
						if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
							if (text.substring(wordL, wordR).matches("(\\W)*" + symbolsPattern)) {
								comment = false;
								setCharacterAttributes(wordL, wordR - wordL + 1, symbolsStyle, false);
							} else if (text.substring(wordL, wordR).matches("(\\W)*" + reservedWordsPattern)) {
								comment = false;
								if (wordL == 0) {
									setCharacterAttributes(wordL, wordR - wordL, reservedStyle, false);
								} else {
									setCharacterAttributes(wordL + 1, wordR - wordL - 1, reservedStyle, false);
								}
							} else if (text.substring(wordL, wordR).matches("(\\W)*" + dataTypesPattern)) {
								comment = false;
								if (wordL == 0) {
									setCharacterAttributes(wordL, wordR - wordL, dataTypesStyle, false);
								} else {
									setCharacterAttributes(wordL + 1, wordR - wordL - 1, dataTypesStyle, false);
								}
							} else if (text.substring(wordL, wordR).matches("(\\W)*" + functionsPattern)) {
								comment = false;
								if (wordL == 0) {
									setCharacterAttributes(wordL, wordR - wordL, functionsStyle, false);
								} else {
									setCharacterAttributes(wordL + 1, wordR - wordL - 1, functionsStyle, false);
								}
							} else if (text.substring(wordL, wordR).matches("(\\W)*" + operationsPattern)) {
								comment = false;
								if (wordL == 0) {
									setCharacterAttributes(wordL, wordR - wordL, operationsStyle, false);
								} else {
									setCharacterAttributes(wordL + 1, wordR - wordL - 1, operationsStyle, false);
								}
							} else if (text.substring(wordL, wordR).matches("//*")) {
								comment = true;
								setCharacterAttributes(wordL, wordR - wordL + 1, commentsStyle, false);
							} else {
								if (text.substring(wordL, wordR).matches("\n")) {
									comment = false;
									if (wordL == 0) {
										setCharacterAttributes(wordL, wordR - wordL, defaultStyle, false);
									} else {
										setCharacterAttributes(wordL + 1, wordR - wordL - 1, defaultStyle, false);
									}
								} else {
									if (comment) {
										setCharacterAttributes(wordL + 1, wordR - wordL - 1, commentsStyle, false);
									} else {
										comment = false;
										if (wordL == 0) {
											setCharacterAttributes(wordL, wordR - wordL, defaultStyle, false);
										} else {
											setCharacterAttributes(wordL + 1, wordR - wordL - 1, defaultStyle, false);
										}
									}
								}
							}
							wordL = wordR;
						}
						wordR++;
					}
				} catch (Exception e) {
					
				}
				updateLineNumbers();
			}
		};

		String temp = textPane.getText();
		textPane.setStyledDocument(doc);
		textPane.setText(temp);

	}

	private void updateLineNumbers() {
		int lineCount = getLineCount(textPane);
		StringBuilder lineNumbers = new StringBuilder();
		for (int i = 1; i <= lineCount; i++) {
			lineNumbers.append(i).append("\n");
		}
		lineNumberArea.setText(lineNumbers.toString());
	}

	public static int getLineCount(JTextPane textPane) {
		Document document = textPane.getDocument();
		if (document instanceof AbstractDocument) {
			AbstractDocument abstractDocument = (AbstractDocument) document;
			Element root = abstractDocument.getDefaultRootElement();
			return root.getElementCount();
		}
		return -1;
	}

	public class CustomListCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			c.setBackground(new Color(45, 45, 45));
			c.setForeground(Color.WHITE);
			c.setFont(new Font("Cascadia Code", Font.PLAIN, 20));
			return c;
		}
	}

	public JTextPane getTextPane() {
		return textPane;
	}

}
