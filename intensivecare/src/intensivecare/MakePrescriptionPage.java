package intensivecare;

import java.awt.Choice;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.print.Doc;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
	
	//decidere se mettere tendine per selezione di farmaci, dottori, quantità ecc oppure far scrivere tutto
public final class MakePrescriptionPage extends JFrame{
	private final String name = "TERAPIA INTENSIVA: login come DOTTORE - Inserimento dati prescrizione";
	private final JTextField medName = new JTextField();
	private final JLabel lblMedName = new JLabel("Farmaco");
	private final JLabel lblLengthT = new JLabel("Durata terapia (in giorni)");
	private final JLabel lblDoses = new JLabel("Dosi giornaliere");
	private final JLabel lblHowDoses = new JLabel("Quantit\u00E0\u00A0 (in pastiglie)");
	private final JLabel lblHealthCode = new JLabel("Codice paziente");
	private final JButton confirmBtn = new JButton("Conferma");
	private final Choice patientsList = new Choice();
	private final SpinnerNumberModel spin1 = new SpinnerNumberModel(1,1,100,1);
	private final SpinnerNumberModel spin2 = new SpinnerNumberModel(1,1,50,1);
	private final SpinnerNumberModel spin3 = new SpinnerNumberModel(1,1,30,1);
	private final JSpinner lengthSpinner = new JSpinner(spin1);
	private final JSpinner howMuchSpinner = new JSpinner(spin3);
	private final JSpinner dosesSpinner = new JSpinner(spin2);
	
	private final JPanel panel = new JPanel();
	private final Container frmContentPane = this.getContentPane();		

	public MakePrescriptionPage(String loggedUser) {
		super("PRESCRIZIONE: login come " + loggedUser + " (D)");
		
		fillPatientsList();
		
		confirmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					Prescription temp = new Prescription(medName.getText(), new Date(), (int) lengthSpinner.getValue(), 
							(int) dosesSpinner.getValue(), (int) howMuchSpinner.getValue(), loggedUser);
					getPatient(patientsList.getSelectedItem()).makePrescription(temp);
					SaveToFile.savePrescription(patientsList.getSelectedItem(), temp);
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
		this.getRootPane().setDefaultButton(confirmBtn);
		this.setSize(333,311);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void addComponents() {
		frmContentPane.add(panel, null);
		panel.setLayout(null);
		patientsList.setBounds(135, 53, 154, 20);
		panel.add(patientsList);
		
		medName.setBounds(161, 90, 128, 20);
		panel.add(medName);
		medName.setColumns(10);
		
		lengthSpinner.setBounds(234, 133, 55, 20);
		panel.add(lengthSpinner);
		
		howMuchSpinner.setBounds(234, 164, 55, 20);
		panel.add(howMuchSpinner);
		
		dosesSpinner.setBounds(234, 195, 55, 20);
		panel.add(dosesSpinner);
		
		lblHealthCode.setBounds(31, 59, 98, 14);
		panel.add(lblHealthCode);
		
		lblMedName.setBounds(31, 93, 73, 14);
		panel.add(lblMedName);
		
		lblLengthT.setBounds(31, 136, 140, 14);
		panel.add(lblLengthT);
		
		lblDoses.setBounds(31, 198, 98, 14);
		panel.add(lblDoses);
		
		lblHowDoses.setBounds(31, 167, 140, 14);
		panel.add(lblHowDoses);
		
		confirmBtn.setBounds(108, 237, 98, 23);
		panel.add(confirmBtn);
	}
}

