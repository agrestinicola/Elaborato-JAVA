package intensivecare;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public final class DoctorPage extends JFrame{
	private final JPanel panel = new JPanel();
	private final JButton makePrescriptionBtn = new JButton("Prescrivi");
	private final JButton makeDiagnosisBtn = new JButton("Scrivere diagnosi d'ingresso");
	private final Container frmContentPane = this.getContentPane();
	
	public DoctorPage(String loggedUser) {
		super("TERAPIA INTENSIVA: login come " + loggedUser + " (D)");

		VitalSignPage.getInstance().logged(loggedUser);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LoginPage.unlog();
				VitalSignPage.getInstance().unlogged();
			}
		});
		
		makePrescriptionBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
						
						MakePrescriptionPage presPage = new MakePrescriptionPage(loggedUser);
					} catch (HeadlessException e ) {
						e.printStackTrace();
					}
				}
		});
		
		makeDiagnosisBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
						WriteDiagnosisPage diagPage;
						if(checkDiagnosisNeed()) {
							diagPage = new WriteDiagnosisPage(loggedUser);
						}
						else {
							JOptionPane.showMessageDialog(DoctorPage.this,
						            "Nessuna diagnosi da inserire",
						            "Errore",
						            JOptionPane.ERROR_MESSAGE);
						}
					} catch (HeadlessException e) {
						e.printStackTrace();
					}
				}
		});
		
		addCompontents();
		setup();
	}
	
	private boolean checkDiagnosisNeed() {
		boolean check = false;
		for (Patient pat: IntensiveCare.patients)
			if(pat.getDiagnosis().equals("#"))
				check = true;
				
		return check;
	}
	
	private void setup() {
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	private void addCompontents() {
		frmContentPane.add(panel, BorderLayout.CENTER);
		panel.add(makeDiagnosisBtn);
		panel.add(makePrescriptionBtn);
	}
}
