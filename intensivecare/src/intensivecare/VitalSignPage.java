package intensivecare;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
	
public final class VitalSignPage extends JFrame{
	private final static JLabel lblSbp = new JLabel("SBP");
	private final static JLabel lblDbp = new JLabel("DBP");
	private final static JLabel lblHr = new JLabel("HR");
	private final static JLabel lblTemp = new JLabel("T");
	private final static JLabel lblDSbp = new JLabel("0");
	private final static JLabel lblDDbp = new JLabel("0");
	private final static JLabel lblDHr = new JLabel("0");
	private final static JLabel lblDT = new JLabel("0");
	private final JScrollPane scrollVitals = new JScrollPane(vitals);
	private final JScrollPane scrollAdmin = new JScrollPane(admin);
	private final static JTextArea admin = new JTextArea();
	private final static JTextArea vitals = new JTextArea();
	private static final Choice patientsList = new Choice();
	private static final Choice limitedPatientList = new Choice();
	private static final JButton addToListBtn = new JButton("Aggiungi");
	private static final JButton remFromListBtn = new JButton("Rimuovi");
	private final JPanel panel = new JPanel();
	private final Container frmContentPane = this.getContentPane();		
	
	private static boolean limit = false;
	private boolean loggedMode = false;
	
	private static final VitalSignPage vital = new VitalSignPage();

	public static VitalSignPage getInstance() {
		return vital;
	}
	
	private VitalSignPage() {
		super("MONITOR PAZIENTI: login non effettuato");
		
		fillPatientsList(); 
		
		limitedPatientList.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if(event.getStateChange() == ItemEvent.SELECTED) {
					forcePrintVitals();
					updateVitalsArea(getPatient(limitedPatientList.getSelectedItem()).getMonitor().getVitalsQueue(), 
							getPatient(limitedPatientList.getSelectedItem()).getHealthCode());
					if(loggedMode)
						updateAdminsArea(getPatient(limitedPatientList.getSelectedItem()).getAdministrations(), 
								getPatient(limitedPatientList.getSelectedItem()).getHealthCode());
				}
			}	
		});
		
		remFromListBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(limitedPatientList.getItemCount() != 0) {
					patientsList.add(limitedPatientList.getSelectedItem());
					limitedPatientList.remove(limitedPatientList.getSelectedItem());
				}
			}
		});
		
		addToListBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(patientsList.getItemCount() != 0 && limitedPatientList.getItemCount() < 10) {
					limitedPatientList.add(patientsList.getSelectedItem());
					patientsList.remove(patientsList.getSelectedItem());
				}
			}
		});
		
		addComponents();
		this.setSize(365, 285);
		setup();
	}

	public static void updateVitals(String healthCode, String what, int newValue) {
		if(limitedPatientList.getSelectedItem().equals(healthCode)) {
			switch(what) {
				case "T":
					lblDT.setText(String.valueOf(newValue));
					return;
				case "DBP":
					lblDDbp.setText(String.valueOf(newValue));
					return;
				case "HR":
					lblDHr.setText(String.valueOf(newValue));
					return;
				case "SBP":
					lblDSbp.setText(String.valueOf(newValue));
					return;
			}
		}			
	}
	
	public static void updateVitalsArea(Queue<String> vitalQueue, String healthCode) {
		if(limitedPatientList.getSelectedItem().equals(healthCode)) {
			vitals.setText("");
			int counter = 0;
			for (String insert: vitalQueue) {
				if(limit) {
					if(counter < 15)
					vitals.append(insert);
					counter++;			
				} else {
					vitals.append(insert);
				}
			}
		}
	}
	
	public static void updateAdminsArea(Queue<Administration> admins, String healthCode) {
		if(limitedPatientList.getSelectedItem().equals(healthCode)) {
			admin.setText("");
			int today = new Date().getDay();
			for (Administration administration: admins)
				if(administration.getDay() == today || administration.getDay() == ((today - 1)  == 0 ? 31 : today - 1))
					admin.append(administration.toString());
			if(admin.getText().isEmpty())
				admin.setText("Nessuna somministrazione negli ultimi due giorni");
		}
	}
	
	public static void addPatientChoice(boolean adding, String insert) {
		if(adding)
			if(limitedPatientList.getItemCount() < 10)
				limitedPatientList.add(insert);
			else
				patientsList.add(insert);
		else { 
			sureDelete(limitedPatientList, insert);
			sureDelete(patientsList, insert);
			if(limitedPatientList.getItemCount() == 0) {
				lblDDbp.setText("");
				lblDHr.setText("");
				lblDSbp.setText("Nessun Paziente");
				lblDT.setText("");
				vitals.setText("Nessun Paziente");
				admin.setText("Nessun Paziente");
			}
		}
	}
	
	public void logged(String loggedUser) {
		this.setTitle("MONITOR PAZIENTI: login come " + loggedUser);
		this.setSize(361, 402);
		limit = true;
		loggedMode = true;
		try {
			updateAdminsArea(getPatient(limitedPatientList.getSelectedItem()).getAdministrations(), 
					getPatient(limitedPatientList.getSelectedItem()).getHealthCode());
		} catch (NullPointerException e) {
			System.out.println("Nessun paziente ricoverato");
			return;
		}
	}
	
	public void unlogged() {
		this.setTitle("MONITOR PAZIENTI: login non effettuato");
		this.setSize(365, 279);
		limit = false;
		loggedMode = false;
		admin.setText("");
	}
	
	private void fillPatientsList() {
		for (Patient pat: IntensiveCare.patients) {
			if(limitedPatientList.getItemCount() < 10)
				limitedPatientList.add(pat.getHealthCode());
			else
				patientsList.add(pat.getHealthCode());
		}
	}
	
	private static void sureDelete(Choice choice, String delete) {
		for(int i = 0; i < choice.getItemCount(); i++) {
			if(choice.getItem(i).equals(delete)) {
				choice.remove(delete);
				return;
			}
		}
	}
	
	private Patient getPatient(String healthCode) {
		for (Patient patient: IntensiveCare.patients)
			if(healthCode.equals(patient.getHealthCode()))
				return patient;
		
		return null;
	}
	
	private void forcePrintVitals() {
		int[] values = getPatient(limitedPatientList.getSelectedItem()).getMonitor().getValues();
		lblDDbp.setText(String.valueOf(values[0]));
		lblDSbp.setText(String.valueOf(values[1]));
		lblDHr.setText(String.valueOf(values[2]));
		lblDT.setText(String.valueOf(values[3]));
	}
	
	private void setup() {
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void addComponents() {
		frmContentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		addToListBtn.setBounds(230, 42, 98, 23);
		panel.add(addToListBtn);
		
		remFromListBtn.setBounds(62, 42, 98, 23);
		panel.add(remFromListBtn);
		
		limitedPatientList.setBounds(22, 16, 138, 20);
		panel.add(limitedPatientList);
		
		patientsList.setBounds(190, 16, 138, 20);
		panel.add(patientsList);
		
		lblTemp.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblTemp.setBounds(179, 76, 47, 23);
		panel.add(lblTemp);
		
		lblSbp.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblSbp.setBounds(22, 76, 47, 23);
		panel.add(lblSbp);
		
		lblDbp.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblDbp.setBounds(101, 76, 47, 23);
		panel.add(lblDbp);
		
		lblHr.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblHr.setBounds(259, 76, 47, 23);
		panel.add(lblHr);
		
		lblDHr.setFont(new Font("Tahoma", Font.BOLD, 37));
		lblDHr.setBounds(259, 114, 80, 40);
		panel.add(lblDHr);
		
		lblDDbp.setFont(new Font("Tahoma", Font.BOLD, 37));
		lblDDbp.setBounds(101, 114, 80, 40);
		panel.add(lblDDbp);
		
		lblDSbp.setFont(new Font("Tahoma", Font.BOLD, 37));
		lblDSbp.setBounds(22, 114, 80, 40);
		panel.add(lblDSbp);
		
		lblDT.setFont(new Font("Tahoma", Font.BOLD, 37));
		lblDT.setBounds(177, 114, 80, 40);
		panel.add(lblDT);
		
		scrollVitals.setBounds(22, 170, 306, 79);
		panel.add(scrollVitals);
		vitals.setText("Nessun paziente");
		vitals.setLineWrap(true);
		vitals.setWrapStyleWord(true);
		vitals.setEditable(false);
		
		scrollAdmin.setBounds(22, 260, 306, 75);
		panel.add(scrollAdmin);
		admin.setText("Nessun paziente");
		admin.setLineWrap(true);
		admin.setWrapStyleWord(true);
		admin.setEditable(false);	
	}
}

