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

public final class WriteDischargeLetterPage extends JFrame{
	private final JTextArea letter = new JTextArea();
	private final JLabel lblCode = new JLabel("Codice paziente");
	private final JButton dischargeBtn = new JButton("Conferma");
	private final Choice patientsList = new Choice();
	private final JPanel panel = new JPanel();
	private final Container frmContentPane = this.getContentPane();		
	private final JScrollPane scrollPane = new JScrollPane(letter);
	
	public WriteDischargeLetterPage(String loggedUser) {
		super("DIMISSIONE: login come " + loggedUser + " (P)");
		fillPatientsList();
		
		dischargeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					SaveToFile.saveDischargeLetter(patientsList.getSelectedItem(), letter.getText(), loggedUser);
					VitalSignPage.addPatientChoice(false, getPatient(patientsList.getSelectedItem()).getHealthCode());
					getPatient(patientsList.getSelectedItem()).getMonitor().stopMonitoring();
					IntensiveCare.patients.remove(getPatient(patientsList.getSelectedItem()));
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
			patientsList.add(pat.getHealthCode());
	}
	
	private Patient getPatient(String healthCode) {
		for (Patient patient: IntensiveCare.patients)
			if(healthCode.equals(patient.getHealthCode()))
				return patient;
		
		return null;
	}
		
	private void setup() {
		this.getRootPane().setDefaultButton(dischargeBtn);
		this.setLocationRelativeTo(null);
		this.setSize(281, 296);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void addComponents() {
		frmContentPane.add(panel, null);
		
		panel.setLayout(null);
		
		patientsList.setBounds(117, 29, 133, 20);
		panel.add(patientsList);
		
		scrollPane.setBounds(22, 55, 228, 168);
		panel.add(scrollPane);
		letter.setLineWrap(true);
		letter.setWrapStyleWord(true);
		
		lblCode.setBounds(22, 29, 96, 14);
		panel.add(lblCode);
		
		dischargeBtn.setBounds(82, 233, 102, 23);
		panel.add(dischargeBtn);
	}
}
	
