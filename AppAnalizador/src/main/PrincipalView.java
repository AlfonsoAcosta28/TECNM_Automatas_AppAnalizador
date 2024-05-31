package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import compilerTools.ErrorLSSL;
import compilerTools.Functions;
import compilerTools.Grammar;
import compilerTools.Production;
import compilerTools.Token;
import view.CodeView;
import view.ConsoleView;
import view.OptionsView;

public class PrincipalView extends JFrame {

	private static final long serialVersionUID = 1L;
	// Panels
	private OptionsView panelOptions;
	private CodeView panelCode;
	private ConsoleView panelConsole;

	// Input-Output
	private JTextPane textAreaCode;
	private JTextArea textAreaConsole;
	private JLabel lblArchivo;
	private JLabel lblPlay;

	// Data Structures
	private ArrayList<Token> tokens;
	private ArrayList<ErrorLSSL> errors;
	private ArrayList<Production> idProduction;
	private ArrayList<Production> idProductionAssigment;
	private ArrayList<Production> idProductionFunctions;
	private Map<String, String> ids;
	private Map<String, String> idsValues;
	private boolean codeCompiled = false;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrincipalView frame = new PrincipalView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PrincipalView() {

		// Variables Init
		tokens = new ArrayList<>();
		errors = new ArrayList<>();
		idProduction = new ArrayList<>();
		idProductionAssigment = new ArrayList<>();
		idProductionFunctions = new ArrayList<>();
		ids = new LinkedHashMap<>();
		idsValues = new LinkedHashMap<>();

		getContentPane().setBackground(new Color(24, 24, 24));
		setTitle("AGA IDE");
		setIconImage(Toolkit.getDefaultToolkit().getImage(PrincipalView.class.getResource("/img/logo.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		JPanel panelContainer = new JPanel();
		panelContainer.setBorder(new EmptyBorder(0, 0, 0, 0));
		panelContainer.setBackground(new Color(24, 24, 24));
		getContentPane().add(panelContainer);
		GridBagLayout gbl_panelContainer = new GridBagLayout();
		gbl_panelContainer.columnWeights = new double[] { 0.0, 99.0 };
		gbl_panelContainer.columnWidths = new int[] { 60, 0 };
		gbl_panelContainer.rowWeights = new double[] { 1.0 };
		gbl_panelContainer.rowHeights = new int[] { 0 };
		panelContainer.setLayout(gbl_panelContainer);

		panelOptions = new OptionsView();
		GridBagConstraints gbc_panelOptions = new GridBagConstraints();
		gbc_panelOptions.fill = GridBagConstraints.BOTH;
		gbc_panelOptions.gridx = 0;
		gbc_panelOptions.gridy = 0;
		gbc_panelOptions.weightx = 0.2;
		panelContainer.add(panelOptions, gbc_panelOptions);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerSize(10);
		splitPane.setUI(new BasicSplitPaneUI() {
			@Override
			public BasicSplitPaneDivider createDefaultDivider() {
				return new BasicSplitPaneDivider(this) {
					private static final long serialVersionUID = 1L;

					@Override
					public void paint(Graphics g) {
						g.setColor(new Color(24, 24, 24));
						g.fillRect(0, 0, getWidth(), getHeight());
						super.paint(g);
					}

					@Override
					public void setBorder(Border border) {
					}
				};
			}
		});
		splitPane.setForeground(new Color(24, 24, 24));
		splitPane.setBackground(new Color(24, 24, 24));
		splitPane.setBorder(null);
		splitPane.setResizeWeight(0.8);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 1;
		gbc_splitPane.gridy = 0;
		gbc_splitPane.weightx = 0.8;
		panelContainer.add(splitPane, gbc_splitPane);

		panelCode = new CodeView();
		// Pone un codigo ejemplo desde que inicia el programa
		panelCode.getTextPane().setText(
				"INICIO{\r\n\tENTERO a1, a2=20, a3;\r\n\tFLOTANTE b21, b22, b23=2.5;\r\n\tFLOTANTE a1;\r\n\tLEER (a1);\r\n\tLEER (b21);\r\n\ta3=SUM(a1,a2);\r\n\ta2=RES(a1,SUM(30,a3));\r\n\tb22=MUL(b21,DIV(b23,5.0));\r\n\tIMPRIMIR (a2);\r\n\tIMPRIMIR (b22);\r\n}FIN");
		splitPane.setTopComponent(panelCode);

		panelConsole = new ConsoleView();
		splitPane.setBottomComponent(panelConsole);

		textAreaCode = panelCode.getTextPane();
		textAreaConsole = panelConsole.getTextArea();

		lblArchivo = panelOptions.getLblArchivo();
		lblArchivo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Establece un look and feel de windows
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception look) {
					look.printStackTrace();
				}

				// Abre un FileChooser para abrir un archivo de texto
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Desktop"));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
				fileChooser.setDialogTitle("OPEN A TXT FILE");

				int result = fileChooser.showOpenDialog(null);

				// Si seleccionamos un archivo lo abre y pone su contenido en el pandel de texto
				if (result == JFileChooser.APPROVE_OPTION) {
					try {
						File selectedFile = fileChooser.getSelectedFile();
						String content = "";
						Scanner scanner = new Scanner(selectedFile);
						while (scanner.hasNext()) {
							content += scanner.nextLine() + "\r";
						}
						scanner.close();
						textAreaCode.setText(content);
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}
		});

		// Boton de ejecutar el codigo
		lblPlay = panelOptions.getLblPlay();
		lblPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Metodo para compilar
				compile();
				// Si el codigo ha compilado
				if (codeCompiled) {
					// Condicional para saber si hubo errores
					if (!errors.isEmpty()) {
						textAreaConsole.append("Compilation Error.");
					}else {
						execute();
						fileAnalysis();
					}
				}
			}
		});

	}

	// Metodo para compilar aplicando en orden los diferentes analisis
	private void compile() {
		clear();
		lexicalAnalysis();
		syntacticAnalysis();
		semanticAnalysis();
		printConsole();
		codeCompiled = true;
	}

	private void lexicalAnalysis() {
		Lexer lexer;
		try {
			// Crea un archivo con el codigo
			File codigo = new File("code.txt");
			try (FileOutputStream output = new FileOutputStream(codigo)) {
				byte[] bytesText = textAreaCode.getText().getBytes();
				output.write(bytesText);
			}
			// Lo los bytes del codigo
			BufferedReader entrada = new BufferedReader(new InputStreamReader(new FileInputStream(codigo), "UTF8"));
			lexer = new Lexer(entrada);
			while (true) {
				// Analiza si el token corresponde a una regla
				Token token = lexer.yylex();
				if (token == null) {
					break;
				}
				tokens.add(token);
			}
		} catch (FileNotFoundException ex) {
			System.out.println("El archivo no pudo ser encontrado... " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("Error al escribir en el archivo... " + ex.getMessage());
		}
	}

	// Analisis Sintactico
	private void syntacticAnalysis() {
		Grammar gramatica = new Grammar(tokens, errors);

		/* Deshabilitar mensajes y validaciones */
		gramatica.disableMessages();
		gramatica.disableValidations();

		/* Eliminación de errores */
		gramatica.delete(new String[] { "ERROR", "ERROR_1", "ERROR_2" }, 14);

		// INICIO
		gramatica.group("INICIO", "tk_inicio tk_llaveAbre", true);
		// ERRORES INICIO
		gramatica.group("INICIO", "INICIO (INICIO)+", 1, " × Syntax Error {}: Multiple Inits [#, %]");

		// DECLARACIONES
		gramatica.group("DECLARACIONENTERO",
				"tk_entero (tk_identificador | tk_identificador tk_igual tk_numero) (tk_coma tk_identificador | tk_coma tk_identificador tk_igual tk_numero)* tk_puntoComa",
				true, idProduction);
		gramatica.group("DECLARACIONFLOTANTE",
				"tk_flotante (tk_identificador | tk_identificador tk_igual tk_numero) (tk_coma tk_identificador | tk_coma tk_identificador tk_igual tk_numero)* tk_puntoComa",
				true, idProduction);

		// FUNCIONES
		gramatica.group("FUNCION",
				"(tk_leer | tk_imprimir) tk_parentesisAbre tk_identificador tk_parentesisCierra tk_puntoComa", true,
				idProductionFunctions);

		// METODOS
		gramatica.loopForFunExecUntilChangeNotDetected(() -> {
			gramatica.group("METODO",
					"(tk_sum | tk_res | tk_div | tk_mul) tk_parentesisAbre (tk_identificador | tk_numero | METODO) tk_coma (tk_identificador | tk_numero | METODO) tk_parentesisCierra tk_puntoComa");
			gramatica.group("METODO",
					"(tk_sum | tk_res | tk_div | tk_mul) tk_parentesisAbre (tk_identificador | tk_numero) tk_coma (tk_identificador | tk_numero) tk_parentesisCierra");
		});

		// ASIGNACIONES
		gramatica.group("ASIGNACION",
				"tk_identificador tk_igual (tk_numero tk_puntoComa | tk_identificador tk_puntoComa | METODO)",
				idProductionAssigment);

		// FIN
		gramatica.group("FIN", "tk_llaveCierra tk_fin", true);
		// ERRORES FIN
		gramatica.group("FIN", "FIN (FIN)+", 1, " × Syntax Error {}: Multiple Ends [#, %]");

		// TOKENS MAL PUESTOS
		gramatica.delete("tk_inicio", 1, " × Syntax Error {}: Unexpected Token INICIO [#, %]");
		gramatica.delete("tk_llaveAbre", 2, " × Syntax Error {}: Unexpected Token '{' [#, %]");
		gramatica.delete("tk_entero", 3, " × Syntax Error {}: Unexpected Token ENTERO [#, %]");
		gramatica.delete("tk_flotante", 4, " × Syntax Error {}: Unexpected Token FLOTANTE [#, %]");
		gramatica.delete("tk_identificador", 5, " × Syntax Error {}: Unexpected Id [#, %]");
		gramatica.delete("tk_puntoComa", 6, " × Syntax Error {}: Unexpected Token ';' [#, %]");
		gramatica.delete("tk_igual", 7, " × Syntax Error {}: Unexpected Token '=' [#, %]");
		gramatica.delete("tk_numero", 8, " × Syntax Error {}: Unexpected Number [#, %]");
		gramatica.delete("tk_coma", 9, " × Syntax Error {}: Unexpected Token ',' [#, %]");
		gramatica.delete("tk_sum", 10, " × Syntax Error {}: Unexpected Token SUM [#, %]");
		gramatica.delete("tk_res", 11, " × Syntax Error {}: Unexpected Token RES [#, %]");
		gramatica.delete("tk_mul", 12, " × Syntax Error {}: Unexpected Token MUL [#, %]");
		gramatica.delete("tk_div", 13, " × Syntax Error {}: Unexpected Token DIV [#, %]");
		gramatica.delete("tk_parentesisAbre", 14, " × Syntax Error {}: Unexpected Token '(' [#, %]");
		gramatica.delete("tk_parentesisCierra", 15, " × Syntax Error {}: Unexpected Token ')' [#, %]");
		gramatica.delete("tk_llaveCierra", 16, " × Syntax Error {}: Unexpected Token '{' [#, %]");
		gramatica.delete("tk_fin", 17, " × Syntax Error {}: Unexpected Token FIN [#, %]");

		gramatica.show();
	}

	private void semanticAnalysis() {
		
		// Decorar el arbol de ID's con su tipo
		for (Production id : idProduction) {
			//Recorre toda la produccion para encontrar todos los identificadores
			for (int j = 0; j < id.getSizeTokens(); j++) {
				//Verifica si encuentra un identificador
				if (id.lexicalCompRank(j, j).equalsIgnoreCase("tk_identificador")) {
					//Si ya existia previamente quiere decir que hay una doble declaracion
					if (ids.containsKey(id.lexemeRank(j, j))) {
						errors.add(new ErrorLSSL(1, " × Semantic Error {}: Double Declaration [#, %]", id, true));
					//Sino existia se guarda en en el hashmap
					} else {
						ids.put(id.lexemeRank(j, j), id.lexemeRank(0, 0));
						//Asigna valores por defecto dependiendo si se trata de un produccion de ENTERO o FLOTANTE
						if (id.lexemeRank(0, 0).equalsIgnoreCase("ENTERO")) {
							idsValues.put(id.lexemeRank(j, j), "0");
						}else {
							idsValues.put(id.lexemeRank(j, j), "0.0");
						}
					}
				}
				//Si la produccion esta decorada como ENTERO
				if (id.lexemeRank(0, 0).equalsIgnoreCase("ENTERO")) {
					//Verifica si se encuentran asignaciones
					if (id.lexicalCompRank(j, j).equalsIgnoreCase("tk_numero")) {
						//Como la produccion esta decorada como ENTERO verifica que el numero a 
						//asignar a una variable coincida con un patron de entero
						if (id.lexemeRank(j, j).matches("^\\d+$")) {
							//Si coincide le asigna ese valor a la variable en el hash map de valores
							idsValues.put(id.lexemeRank(j - 2, j - 2), id.lexemeRank(j, j));
						} else {
							errors.add(
									new ErrorLSSL(1, " × Semantic Error {}: Incompatible Data Types [#, %]", id, true));
						}
					}
				//Si la produccion esta decorada como FLOTANTE
				} else if (id.lexemeRank(0, 0).equalsIgnoreCase("FLOTANTE")) {
					//Verifica si se encuentran asignaciones
					if (id.lexicalCompRank(j, j).equalsIgnoreCase("tk_numero")) {
						//Como la produccion esta decorada como ENTERO verifica que el numero a 
						//asignar a una variable coincida con un patron de flotante
						if (id.lexemeRank(j, j).matches("^\\d*\\.\\d+$")) {
							//Si coincide le asigna ese valor a la variable en el hash map de valores
							idsValues.put(id.lexemeRank(j - 2, j - 2), id.lexemeRank(j, j));
						} else {
							errors.add(
									new ErrorLSSL(1, " × Semantic Error {}: Incompatible Data Types [#, %]", id, true));
						}
					}
				}
			}
		}

		// Reglas Semanticas Funciones
		for (Production id : idProductionFunctions) {
			for (int j = 0; j < id.getSizeTokens(); j++) {
				// La funcion es de lectura
				if (id.lexicalCompRank(j, j).equalsIgnoreCase("tk_leer")) {
					// El valor es un identificador?
					if (id.lexicalCompRank(j + 2, j + 2).equalsIgnoreCase("tk_identificador")) {
						// El Identificador ya esta declarado?
						if (!ids.containsKey(id.lexemeRank(j + 2, j + 2))) {
							errors.add(
									new ErrorLSSL(1, " × Semantic Error {}: Undeclarated Variable [#, %]", id, true));
						}
					}
				}
				// La funcion es de impresion
				else if (id.lexicalCompRank(j, j).equalsIgnoreCase("tk_imprimir")) {
					// El valor es un identificador
					if (id.lexicalCompRank(j + 2, j + 2).equalsIgnoreCase("tk_identificador")) {
						// El identificador esta declarado?
						if (ids.containsKey(id.lexemeRank(j + 2, j + 2))) {
							// El Identificador tiene valor?
							if (!idsValues.containsKey(id.lexemeRank(j + 2, j + 2))) {
								errors.add(
										new ErrorLSSL(1, " × Semantic Error {}: Unassigned Variable [#, %]", id, true));
							}
						} else {
							errors.add(
									new ErrorLSSL(1, " × Semantic Error {}: Undeclarated Variable [#, %]", id, true));
						}
					}
				}
			}
		}

		// Reglas Semanticas de Asignaciones
		for (Production id : idProductionAssigment) {
			// El ID de la Declaracion Esta declarado?
			if (ids.containsKey(id.lexemeRank(0, 0))) {
				for (int j = 2; j < id.getSizeTokens(); j++) {

					// El valor es un metodo(s)
					if (id.lexicalCompRank(j, j).matches("tk_sum|tk_res|tk_mul|tk_div")) {

						for (int i = j + 2; i < id.getSizeTokens(); i++) {
							// El valor es un numero?
							if (id.lexicalCompRank(i, i).equalsIgnoreCase("tk_numero")) {
								// La asignacion es de tipo ENTERO
								if (ids.get(id.lexemeRank(0, 0)).equalsIgnoreCase("ENTERO")) {
									if (id.lexemeRank(i, i).matches("^\\d+$")) {
										idsValues.put(id.lexemeRank(0, 0), id.lexemeRank(i, i));
									} else {
										errors.add(new ErrorLSSL(1,
												" × Semantic Error {}: Incompatible Data Types [#, %]", id, true));
									}
									// La asignacion es de tipo FLOTANTE
								} else {
									if (id.lexemeRank(i, i).matches("^\\d*\\.\\d+$")) {
										idsValues.put(id.lexemeRank(0, 0), id.lexemeRank(i, i));
									} else {
										errors.add(new ErrorLSSL(1,
												" × Semantic Error {}: Incompatible Data Types [#, %]", id, true));
									}
								}
							}

							// El valor es un identificador
							if (id.lexicalCompRank(i, i).equalsIgnoreCase("tk_identificador")) {
								// El identificador ya esta declarado?
								if (ids.containsKey(id.lexemeRank(i, i))) {
									// El Identificador tiene valor?
									if (idsValues.containsKey(id.lexemeRank(i, i))) {
										if (!ids.get(id.lexemeRank(0, 0))
												.equalsIgnoreCase(ids.get(id.lexemeRank(i, i)))) {
											errors.add(new ErrorLSSL(1,
													" × Semantic Error {}: Incompatible Data Types [#, %]", id, true));
										}
									} else {
										errors.add(new ErrorLSSL(1, " × Semantic Error {}: Unassigned Variable [#, %]",
												id, true));
									}
								} else {
									errors.add(new ErrorLSSL(1, " × Semantic Error {}: Undeclarated Variable [#, %]",
											id, true));
								}
							}

						}

						if (errors.isEmpty()) {
							// Metodo para sacar el valor final
							String resultado = methodResult(id, j);
							if (ids.get(id.lexemeRank(0, 0)).equalsIgnoreCase("ENTERO")) {
								idsValues.put(id.lexemeRank(0, 0), resultado.substring(0, resultado.length() - 2));
							} else {
								idsValues.put(id.lexemeRank(0, 0), resultado);
							}
						}

						break;

					}

					// El valor es un numero?
					if (id.lexicalCompRank(j, j).equalsIgnoreCase("tk_numero")) {
						// El tipo de asignacion es entero o flotante?
						if (ids.get(id.lexemeRank(0, 0)).equalsIgnoreCase("ENTERO")) {
							// El numero es un entero?
							if (id.lexemeRank(j, j).matches("^\\d+$")) {
								idsValues.put(id.lexemeRank(0, 0), id.lexemeRank(j, j));
							} else {
								errors.add(new ErrorLSSL(1, " × Semantic Error {}: Incompatible Data Types [#, %]", id,
										true));
							}
						} else {
							// El numero es un flotante?
							if (id.lexemeRank(j, j).matches("^\\d*\\.\\d+$")) {
								idsValues.put(id.lexemeRank(0, 0), id.lexemeRank(j, j));
							} else {
								errors.add(new ErrorLSSL(1, " × Semantic Error {}: Incompatible Data Types [#, %]", id,
										true));
							}
						}
					}

					// El valor es un identificador
					if (id.lexicalCompRank(j, j).equalsIgnoreCase("tk_identificador")) {
						// El identificador ya esta declarado?
						if (ids.containsKey(id.lexemeRank(j, j))) {
							// El Identificador tiene valor?
							if (idsValues.containsKey(id.lexemeRank(j, j))) {
								// El identificador tiene el mismo tipo de dato que la asignacion
								if (ids.get(id.lexemeRank(0, 0)).equalsIgnoreCase(ids.get(id.lexemeRank(j, j)))) {
									idsValues.put(id.lexemeRank(0, 0), idsValues.get(id.lexemeRank(j, j)));
								} else {
									errors.add(new ErrorLSSL(1, " × Semantic Error {}: Incompatible Data Types [#, %]",
											id, true));
								}
							} else {
								errors.add(
										new ErrorLSSL(1, " × Semantic Error {}: Unassigned Variable [#, %]", id, true));
							}
						} else {
							errors.add(
									new ErrorLSSL(1, " × Semantic Error {}: Undeclarated Variable [#, %]", id, true));
						}
					}

				}
			} else {
				errors.add(new ErrorLSSL(1, " × Semantic Error {}: Undeclarated Variable [#, %]", id, true));
			}

		}

	}

	// Metodo para sacar el valor final de un metodo(s)
	private String methodResult(Production id, int init) {
		double valor1 = 0;
		double valor2 = 0;
		String operation = "";

		// El valor es un numero?
		if (id.lexicalCompRank(init + 2, init + 2).equalsIgnoreCase("tk_numero")) {
			valor1 = Double.parseDouble(id.lexemeRank(init + 2, init + 2));
		}

		// El valor es un identificador
		if (id.lexicalCompRank(init + 2, init + 2).equalsIgnoreCase("tk_identificador")) {
			valor1 = Double.parseDouble(idsValues.get(id.lexemeRank(init + 2, init + 2)));
		}

		// El valor es un metodo(s)
		if (id.lexicalCompRank(init + 2, init + 2).matches("tk_sum|tk_res|tk_mul|tk_div")) {
			// Metodo para sacar el valor final
			valor1 = Double.parseDouble(methodResult(id, init + 2));
		}

		// El valor 2 es un numero?
		if (id.lexicalCompRank(init + 4, init + 4).equalsIgnoreCase("tk_numero")) {
			valor2 = Double.parseDouble(id.lexemeRank(init + 4, init + 4));
		}

		// El valor 2 es un identificador
		if (id.lexicalCompRank(init + 4, init + 4).equalsIgnoreCase("tk_identificador")) {
			valor2 = Double.parseDouble(idsValues.get(id.lexemeRank(init + 4, init + 4)));
		}

		// El valor 2 es un metodo(s)
		if (id.lexicalCompRank(init + 4, init + 4).matches("tk_sum|tk_res|tk_mul|tk_div")) {
			// Metodo para sacar el valor final
			valor2 = Double.parseDouble(methodResult(id, init + 4));
		}

		if (id.lexicalCompRank(init, init).matches("tk_sum")) {
			operation = String.valueOf(valor1 + valor2);
		} else if (id.lexicalCompRank(init, init).matches("tk_res")) {
			operation = String.valueOf(valor1 - valor2);
		} else if (id.lexicalCompRank(init, init).matches("tk_mul")) {
			operation = String.valueOf(valor1 * valor2);
		} else if (id.lexicalCompRank(init, init).matches("tk_div")) {
			operation = String.valueOf(valor1 / valor2);
		}

		return operation;
	}

	// Metodo para imprimir todos los errores que hubo
	private void printConsole() {
	    int sizeErrors = errors.size();
	    if (sizeErrors > 0) {
	        Functions.sortErrorsByLineAndColumn(errors);
	        StringBuilder strErrors = new StringBuilder();
	        
	        int valor = 7;
	        for (ErrorLSSL error : errors) {
	            strErrors.append("ERROR IN LINE ").append(error.getLine()).append(": ").append(error.toString().substring(0, error.toString().length() - valor)).append("\n");
	        }
	        
	        Map<Integer, Integer> lineOffsets = new HashMap<>();
	        String[] lines = textAreaCode.getText().split("\n");
	        int currentOffset = 0;
	        for (int i = 0; i < lines.length; i++) {
	            lineOffsets.put(i, currentOffset);
	            currentOffset += lines[i].length(); 
	        }
	        
	        textAreaConsole.setText("Some Errors Were Found: \n" + strErrors);
	        textAreaConsole.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                super.mouseClicked(e);
	                try {
	                	int offset = textAreaConsole.viewToModel(e.getPoint());
	                    int line = textAreaConsole.getLineOfOffset(offset);
	                    ErrorLSSL selectedError ;
	                    try {
	                    	selectedError = errors.get(line - 1);
						} catch (Exception e2) {
							return;
						}
	                    int errorLine = selectedError.getLine();
	                    int startOffset = lineOffsets.get(errorLine - 1);
	                    int lineLength = lines[errorLine - 1].length(); 
	                    int endOffset = startOffset + lineLength;
	                    textAreaCode.select(startOffset, endOffset); 
	                    textAreaCode.requestFocus();

	                    textAreaCode.requestFocus();
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	            }
	        });
	    } else {
	        textAreaConsole.setText("The Analysis Was Successfully Without Errors");
	    }
	}

	private void execute() {
		for (Production id : idProductionFunctions) {
			// La funcion es de lectura
			if (id.lexicalCompRank(0, 0).equalsIgnoreCase("tk_leer")) {
				String valor = JOptionPane.showInputDialog(null, "INPUT");
				if (ids.get(id.lexemeRank(2, 2)).equalsIgnoreCase("ENTERO")) {
					if (valor.matches("^\\d+$")) {
						idsValues.put(id.lexemeRank(2, 2), valor);
					} else {
						errors.add(new ErrorLSSL(1, " × Semantic Error {}: Incompatible Data Types [#, %]", id, true));
					}
				} else {
					if (valor.matches("^\\d*\\.\\d+$")) {
						idsValues.put(id.lexemeRank(2, 2), valor);
					} else {
						errors.add(new ErrorLSSL(1, " × Semantic Error {}: Incompatible Data Types [#, %]", id, true));
					}
				}
			}
			// La funcion es de impresion
			else if (id.lexicalCompRank(0, 0).equalsIgnoreCase("tk_imprimir")) {
				JOptionPane.showMessageDialog(null, idsValues.get(id.lexemeRank(2, 2)));
			}
			printConsole();
		}
	}

	// Metodo para imprimir todos los arboles y estructuras
	private void fileAnalysis() {
		File analysis = new File("analysis.txt");

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(analysis))) {
			// Crea una cadena con todos los tokens
			String tokensString = "********************\nTOKENS\n********************\n\n";
			for (int i = 0; i < tokens.size(); i++) {
				tokensString += tokens.get(i) + "\n";
			}

			// Crea una cadena con todas las producciones
			String productions = "********************\nPRODUCTIONS\n********************\n";
			for (int i = 0; i < idProduction.size(); i++) {
				productions += "\n" + idProduction.get(i) + "\n";
			}
			for (int i = 0; i < idProductionAssigment.size(); i++) {
				productions += "\n" + idProductionAssigment.get(i) + "\n";
			}

			// Crea una cadena con el árbol decorado
			String decoratedIds = "********************\nSYMBOLS TABLE\n********************\n\n";
			
			for (String key : ids.keySet()) {
			    // Obtener el valor correspondiente de 'ids' y 'idsValues' para la clave actual
			    String valueId = ids.get(key);
			    String valueTree = idsValues.get(key);
			    
			    // Agregar los valores formateados a las cadenas correspondientes
			    decoratedIds += "[" + key + " -> " + valueId + " -> " + valueTree + "]\n";
			}

			// Escribe todas las cadenas en el archivo txt
			writer.write(tokensString);
			writer.newLine();
			writer.write(productions);
			writer.newLine();
			writer.write(decoratedIds);
			writer.newLine();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Metodo para limpiar y evitar solapamiento en cada ejecucion
	private void clear() {
		tokens.clear();
		errors.clear();
		idProduction.clear();
		idProductionAssigment.clear();
		idProductionFunctions.clear();
		ids.clear();
		idsValues.clear();
		codeCompiled = false;
	}

}
