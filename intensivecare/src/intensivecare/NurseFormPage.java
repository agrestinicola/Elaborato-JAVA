package intensivecare;

import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public final class NurseFormPage extends JFrame {
	private final JTextField patientName = new JTextField();
	private final JTextField patientSurname = new JTextField();
	private final JTextField patientPlaceB = new JTextField();
	private final JLabel lblName = new JLabel("Nome");
	private final JLabel lblSurname = new JLabel("Cognome");
	private final JLabel lblPlaceB = new JLabel("Luogo di nascita");
	private final JLabel lblDateB = new JLabel("Data di nascita");
	private final JButton registerBtn = new JButton("Registra");
	private final SpinnerNumberModel spin1 = new SpinnerNumberModel(1,1,31,1);
	private final SpinnerNumberModel spin2 = new SpinnerNumberModel(1,1,12,1);
	private final SpinnerNumberModel spin3 = new SpinnerNumberModel(1900,1900,SaveToFile.getTodayYear(),1);
	private final JSpinner daySpinner = new JSpinner(spin1);
	private final JSpinner monthSpinner = new JSpinner(spin2);
	private final JSpinner yearSpinner = new JSpinner(spin3);
	
	private boolean insert = true;
	
	private final JPanel panel = new JPanel();
	private final Container frmContentPane = this.getContentPane();
	
	public NurseFormPage(String loggedUser) {
		super("RICOVERO: login come " + loggedUser + " (N)");
		
		registerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					if(!Date.checkDate((int) daySpinner.getValue(), (int) monthSpinner.getValue(),(int) yearSpinner.getValue())) {
						JOptionPane.showMessageDialog(NurseFormPage.this,
					            "Data inserita non valida (giorno e mese non corrispondono)",
					            "Errore",
					            JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					if(!Date.checkMax((int) daySpinner.getValue(), (int) monthSpinner.getValue(),(int) yearSpinner.getValue())) {
						JOptionPane.showMessageDialog(NurseFormPage.this,
					            "Data inserita non valida (il giorno inserito è maggiore ad oggi)",
					            "Errore",
					            JOptionPane.ERROR_MESSAGE);
						return;
					}
						
					if (patientName.getText().length() < 2 || patientSurname.getText().length() < 2 || patientPlaceB.getText().length() < 3){
						JOptionPane.showMessageDialog(NurseFormPage.this,
					            "Dati paziente non validi (sono stati lasciati dei campi vuoti)",
					            "Errore",
					            JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					String pn = patientName.getText().substring(0, 1).toUpperCase() + patientName.getText().substring(1);
					String ps = patientSurname.getText().substring(0, 1).toUpperCase() + patientSurname.getText().substring(1);
					String pb = patientPlaceB.getText().substring(0, 1).toUpperCase() + patientPlaceB.getText().substring(1);
					
					String tempHealthCode = ps.substring(0, 1) + pn.substring(0, 1) + String.format("02%d", daySpinner.getValue()) + String.format("02%d", monthSpinner.getValue()) + 
							yearSpinner.getValue().toString() + pb.substring(0,3).toUpperCase();
					
					for (Patient patient: IntensiveCare.patients) {
						if(tempHealthCode.equals(patient.getHealthCode())) {
							insert = false;
							break;
						}
					}
					
					if (insert) {
						VitalSignPage.addPatientChoice(true, tempHealthCode);
						Patient temp = new Patient(ps, pn, new Date((int) daySpinner.getValue(), (int) monthSpinner.getValue(),
											(int) yearSpinner.getValue()),pb, tempHealthCode);
						IntensiveCare.patients.add(temp);
						SaveToFile.savePersonalInformations(temp);
					} else {
						JOptionPane.showMessageDialog(NurseFormPage.this,
					            "Paziente già  registrato",
					            "Errore",
					            JOptionPane.ERROR_MESSAGE);
						insert = true;
						return;
					}
					
					dispose();
				} catch (HeadlessException e) {
					e.printStackTrace();
				}
			}
		});
		
		addComponents();
		setup();		
	}
	
	private void setup() {
		this.getRootPane().setDefaultButton(registerBtn);
		this.setSize(305,242);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void addComponents() {
		frmContentPane.add(panel, null);
		panel.setLayout(null);

		patientName.setBounds(127, 35, 144, 20);
		panel.add(patientName);
		patientName.setColumns(10);
		
		patientSurname.setBounds(127, 66, 144, 20);
		panel.add(patientSurname);
		patientSurname.setColumns(10);
		
		patientPlaceB.setBounds(127, 97, 144, 20);
		panel.add(patientPlaceB);
		patientPlaceB.setColumns(10);
		
		lblName.setBounds(28, 38, 46, 14);
		panel.add(lblName);
		
		lblDateB.setBounds(29, 131, 106, 33);
		panel.add(lblDateB);

		lblSurname.setBounds(28, 69, 77, 14);
		panel.add(lblSurname);
		
		lblPlaceB.setBounds(28, 100, 96, 14);
		panel.add(lblPlaceB);
		
		daySpinner.setBounds(123, 137, 37, 20);
		daySpinner.setEditor(new JSpinner.NumberEditor(daySpinner, "#"));
		panel.add(daySpinner);
		
		monthSpinner.setBounds(170, 137, 37, 20);		
		monthSpinner.setEditor(new JSpinner.NumberEditor(monthSpinner, "#"));
		panel.add(monthSpinner);
		
		yearSpinner.setBounds(217, 137, 54, 20);
		yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
		panel.add(yearSpinner);
		
		registerBtn.setBounds(100, 176, 89, 23);
		panel.add(registerBtn);
	}
}
