package gui.mainEngine;

import java.awt.Color;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

public class JItemsCreator {
	
	public JButton createJButton(String name, int x, int y, int width, int height ) {
		
		JButton button = new JButton(name);
		button.setBounds(x, y, width, height);
		
		button.setVisible(false);
		
		return button;
	}
	
	public JLabel createJLabel(String name, String text, int x, int y, int width, int height, Color fg) {
		
		JLabel label = new JLabel(name);
		label.setBounds(x, y, width, height);
		label.setForeground(fg);
		label.setText(text);
		
		return label;
	}

	public JScrollPane createJScrollPane(int x, int y, int width, int height) {
	
		JScrollPane scrollPane = new JScrollPane();

		scrollPane.setBounds(x, y, width, height);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		return scrollPane;
	}
	
	public JPanel createJPanel() {
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 280, 600);
		panel.setBackground(Color.DARK_GRAY);
		
		GroupLayout gl_sideMenu = createGroupLayout(panel);
		panel.setLayout(gl_sideMenu);
		
		return panel;
		
	}
	
	public JDialog createJDialog(int x, int y, int width, int height) {
		
		JDialog dialog = new JDialog();
		dialog.setBounds(x, y, width, height);
		
		return dialog;
	}
	
	
	private GroupLayout createGroupLayout(JPanel panel) {
		
		GroupLayout layout = new GroupLayout(panel);
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.LEADING)
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(Alignment.LEADING)
		);
		
		return layout;
		
	}
	
}
