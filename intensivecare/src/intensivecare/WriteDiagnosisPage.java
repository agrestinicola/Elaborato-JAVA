package intensivecare;

import java.awt.Choice;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
	
public final class WriteDiagnosisPage extends JFrame{
	private final JTextArea diagnosis = new JTextArea("");
	private final JLabel lblCode = new JLabel("Codice paziente");
	private final JButton confirmBtn = new JButton("Conferma");
	private final JScrollPane scrollPane = new JScrollPane(diagnosis);

	private final Choice patientsList = new Choice();
	
	private final JPanel panel = new JPanel();
	private final Container frmContentPane = this.getContentPane();		

	public WriteDiagnosisPage(String loggedUser) {
		super("DIAGNOSI: login come " + loggedUser + " (D)");
		fillPatientsList();
		
		confirmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					SaveToFile.saveDiagnosis(patientsList.getSelectedItem(), diagnosis.getText());
					patientsList.remove(patientsList.getSelectedItem());
					dispose();
				} catch (HeadlessException e) {
					e.printStackTrace();
				}
			}
		});
		
		addComponents();
		setup();
	}
	
	private void fillPatientsList() {
		for (Patient pat: IntensiveCare.patients)
			if(pat.getDiagnosis().equals("#"))
				patientsList.add(pat.getHealthCode());
	}
	
	private void setup() {
		this.getRootPane().setDefaultButton(confirmBtn);
		this.setLocationRelativeTo(null);
		this.setSize(281, 296);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void addComponents() {
		frmContentPane.add(panel, null);

		panel.setLayout(null);
		
		confirmBtn.setBounds(85, 234, 95, 23);
		panel.add(confirmBtn);
		
		patientsList.setBounds(114, 29, 136, 20);
		panel.add(patientsList);
		
		scrollPane.setBounds(23, 55, 227, 168);
		panel.add(scrollPane);
		diagnosis.setLineWrap(true);
		diagnosis.setWrapStyleWord(true);
		
		lblCode.setBounds(23, 29, 101, 14);
		panel.add(lblCode);
	}
}

