package gui.mainEngine;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;

public class ParametersJDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private Float timeWeight=null;
	private Float changeWeight=null;
	private Boolean preprocessingTime=null;
	private Boolean preprocessingChange=null;
	private Integer numberOfPhases=null;
	private boolean confirm=false;
	private JTextField textField;
	


	/**
	 * Create the dialog.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ParametersJDialog() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.WEST);
		
		JLabel lblChooseTimeWeight = new JLabel("Choose Time Weight");
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"0.0", "0.5", "1.0"}));
		
		JLabel lblChooseChangeWeight = new JLabel("Choose Change Weight");
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"0.0", "0.5", "1.0"}));
		
		JLabel lblTimePreprocessing = new JLabel("Time PreProcessing");
		
		final JRadioButton rdbtnOn = new JRadioButton("ON");
		buttonGroup.add(rdbtnOn);
		
		final JRadioButton rdbtnOff = new JRadioButton("OFF");
		buttonGroup.add(rdbtnOff);
		
		JLabel lblNewLabel = new JLabel("Change PreProcessing");
		
		final JRadioButton rdbtnOn_1 = new JRadioButton("ON");
		buttonGroup_1.add(rdbtnOn_1);
		
		final JRadioButton rdbtnOff_1 = new JRadioButton("OFF");
		buttonGroup_1.add(rdbtnOff_1);
		
		JLabel lblGiveNumberOf = new JLabel("Give Number of Phases");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblChooseChangeWeight, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblChooseTimeWeight, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblTimePreprocessing, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(35))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(0)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(rdbtnOff_1)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(comboBox_1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(comboBox, 0, 41, Short.MAX_VALUE))
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGap(48)
									.addComponent(lblGiveNumberOf))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGap(78)
									.addComponent(textField, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(rdbtnOn)
								.addComponent(rdbtnOff)))
						.addComponent(rdbtnOn_1))
					.addGap(100))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblChooseTimeWeight, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblGiveNumberOf, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblChooseChangeWeight, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(29)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTimePreprocessing)
								.addComponent(rdbtnOn))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(rdbtnOff)
							.addGap(33)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel)
								.addComponent(rdbtnOn_1))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(rdbtnOff_1))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
					JButton okButton = new JButton("OK");
					okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						
						
						changeWeight = Float.valueOf((String) comboBox.getSelectedItem());
						timeWeight = Float.valueOf((String)comboBox_1.getSelectedItem());
						if(!textField.getText().isEmpty())
							numberOfPhases=Integer.parseInt(textField.getText());
						if(rdbtnOn.isSelected()){
							preprocessingTime=true;
						}
						else if(rdbtnOff.isSelected()){
							preprocessingTime=false;
						}
						if(rdbtnOn_1.isSelected()){
							preprocessingChange=true;
						}
						else if(rdbtnOff_1.isSelected()){
							preprocessingChange=false;
						}
						
						if(changeWeight!=null && timeWeight!=null && preprocessingTime!=null && preprocessingChange!=null && numberOfPhases!=null){
							
							confirm=true;

							setVisible(false);
						}
						else{
							
							timeWeight=null;
							changeWeight=null;
							preprocessingTime=null;
							preprocessingChange=null;
							
							JOptionPane.showMessageDialog(null, "You have to fill every field!");
							
							confirm=false;
							
						}
						
						
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						confirm=false;

						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	
	public float getTimeWeight(){
		
		return timeWeight; 
	}
	
	public float getChangeWeight(){
		
		return changeWeight; 
	}

	public Integer getNumberOfPhases() {
		return numberOfPhases;
	}
	
	public boolean getPreProcessingTime(){
		
		return preprocessingTime;
	}
	
	public boolean getPreProcessingChange(){
		
		return preprocessingChange;
	}
	
	public boolean getConfirmation(){
		
		return confirm;
	}
}
