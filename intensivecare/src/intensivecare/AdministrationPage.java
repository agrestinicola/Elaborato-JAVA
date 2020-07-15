package intensivecare;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public final class AdministrationPage extends JFrame{	
	private final JTextArea notes = new JTextArea();
	private final JScrollPane scrollNotes = new JScrollPane(notes);
	private final Choice patientsList = new Choice();
	private final Choice presList = new Choice();
	private final JButton btnConfirm = new JButton("Conferma");
	private final JButton btnGetPrescriptions = new JButton("Prescrizioni");
	private final JButton detailsBtn = new JButton("Prescrizioni");
	private final JTextArea prescription = new JTextArea();


	private final JPanel panel = new JPanel();
	private final Container frmContentPane = this.getContentPane();

	public AdministrationPage(String loggedUser) {
		super("SOMMINISTRAZIONE: login come " + loggedUser + " (N)");
		
		fillPatientsList();
		
		btnGetPrescriptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!getPatient(patientsList.getSelectedItem()).getPrescriptions().isEmpty()) {
					presList.removeAll();
					for (Prescription pres: getPatient(patientsList.getSelectedItem()).getPrescriptions())
						presList.add(pres.fastPrint());
				} else {
					JOptionPane.showMessageDialog(AdministrationPage.this,
				            "Nessuna prescrizione",
				            "Errore",
				            JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getPatient(patientsList.getSelectedItem()).makeAdministration(new Administration(getPrescription(), 
						(notes.getText().equals("") ? "#" : notes.getText())));
				SaveToFile.logActivity(patientsList.getSelectedItem(), presList.getSelectedItem() + " " + (notes.getText().equals("") ? "#" : notes.getText()));
				dispose();
			}
		});
		
		addComponents();
		setup();
	}
	
	private void fillPatientsList() {
		for (Patient pat: IntensiveCare.patients)
			patientsList.add(pat.getHealthCode());
	}
	
	private Prescription getPrescription() {
		for (Prescription pres : getPatient(patientsList.getSelectedItem()).getPrescriptions())
			if(pres.fastPrint().equals(presList.getSelectedItem()))
				return pres;
		
		return null;
	}
	
	private Patient getPatient(String healthCode) {
		for (Patient patient: IntensiveCare.patients)
			if(healthCode.equals(patient.getHealthCode()))
				return patient;
		
		return null;
	}
	
	private void setup() {
		this.getRootPane().setDefaultButton(btnConfirm);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setSize(339, 206);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void addComponents() {
		frmContentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		patientsList.setSize(110, 20);
		patientsList.setLocation(10, 11);
		
		panel.add(patientsList);
		presList.setSize(135, 20);
		presList.setLocation(10, 37);
		panel.add(presList);
		
		scrollNotes.setBounds(10, 63, 306, 79);
		panel.add(scrollNotes);
		notes.setLineWrap(true);
		notes.setWrapStyleWord(true);
		
		btnConfirm.setBounds(111, 149, 95, 23);
		panel.add(btnConfirm);
		btnGetPrescriptions.setBounds(126, 11, 110, 23);
		panel.add(btnGetPrescriptions);
	}
}
