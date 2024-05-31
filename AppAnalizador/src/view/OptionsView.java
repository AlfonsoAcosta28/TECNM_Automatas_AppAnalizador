package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class OptionsView extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel lblArchivo;
	private JLabel lblPlay;
	private JPanel panelArchivo;
	private JPanel panelPlay;

	public OptionsView() {
		setBorder(new LineBorder(new Color(45, 45, 45)));
		setBackground(new Color(24, 24, 24));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		panelArchivo = new JPanel();
		panelArchivo.setBorder(null);
		panelArchivo.setBackground(new Color(24, 24, 24));
		panelArchivo.setPreferredSize(new Dimension(60, 60));
		GridBagConstraints gbc_panelArchivo = new GridBagConstraints();
		gbc_panelArchivo.anchor = GridBagConstraints.NORTH;
		gbc_panelArchivo.gridx = 0;
		gbc_panelArchivo.gridy = 0;
		add(panelArchivo, gbc_panelArchivo);
		panelArchivo.setLayout(new GridLayout(0, 1, 0, 0));

		ImageIcon archivoIcon = new ImageIcon(OptionsView.class.getResource("/img/archiveIcon.png"));
		lblArchivo = new JLabel();
		lblArchivo.setIcon(archivoIcon);
		lblArchivo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				panelArchivo.setBorder(new MatteBorder(0, 5, 0, 0, new Color(2, 110, 193)));
				panelPlay.setBorder(null);
			}
		});
		lblArchivo.setHorizontalAlignment(SwingConstants.CENTER);
		lblArchivo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblArchivo.setBackground(new Color(24, 24, 24));
		lblArchivo.setOpaque(true);
		lblArchivo.setPreferredSize(new Dimension(60, 60));
		lblArchivo.setBorder(null);
		panelArchivo.add(lblArchivo);

		panelPlay = new JPanel();
		panelPlay.setBackground(new Color(24, 24, 24));
		panelPlay.setPreferredSize(new Dimension(60, 60));
		GridBagConstraints gbc_panelPlay = new GridBagConstraints();
		gbc_panelPlay.anchor = GridBagConstraints.NORTH;
		gbc_panelPlay.gridx = 0;
		gbc_panelPlay.gridy = 1;
		add(panelPlay, gbc_panelPlay);
		panelPlay.setLayout(new GridLayout(0, 1, 0, 0));
		
		ImageIcon playIcon = new ImageIcon(OptionsView.class.getResource("/img/playIcon.png"));
		lblPlay = new JLabel();
		lblPlay.setIcon(playIcon);
		lblPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				panelPlay.setBorder(new MatteBorder(0, 5, 0, 0, new Color(2, 110, 193)));
				panelArchivo.setBorder(null);
			}
		});
		lblPlay.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblPlay.setBackground(new Color(24, 24, 24));
		lblPlay.setOpaque(true);
		lblPlay.setPreferredSize(new Dimension(60, 60));
		lblPlay.setBorder(null);
		panelPlay.add(lblPlay);

	}

	public JLabel getLblArchivo() {
		return lblArchivo;
	}

	public JLabel getLblPlay() {
		return lblPlay;
	}
	
}
