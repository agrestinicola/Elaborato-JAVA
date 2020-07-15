package intensivecare; 

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
//pagina di esempio da implementare
public final class NursePage extends JFrame{
	private final JPanel panel = new JPanel();
	private final JButton addPatientBtn = new JButton("Ricovera");
	private final JButton adminBtn = new JButton("Somministra");
	private final Container frmContentPane = this.getContentPane();
	
	public NursePage(String loggedUser) {
		super("TERAPIA INTENSIVA: login come " + loggedUser + " (N)");

		VitalSignPage.getInstance().logged(loggedUser);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LoginPage.unlog();
				VitalSignPage.getInstance().unlogged();
			}
		});
		
		adminBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(IntensiveCare.patients.isEmpty()) {
					JOptionPane.showMessageDialog(NursePage.this,
				            "Nessun paziente ricoverato",
				            "Errore",
				            JOptionPane.ERROR_MESSAGE);
				} else {
					AdministrationPage adminPage = new AdministrationPage(loggedUser);
				}
			}
		});
		
		addPatientBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NurseFormPage formPage = new NurseFormPage(loggedUser);
			}
		});
		
		addComponents();
		setup();
	}
	
	private void setup() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(200,200);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}
	
	private void addComponents() {
		frmContentPane.add(panel, BorderLayout.CENTER);
		panel.add(addPatientBtn);
		panel.add(adminBtn);
	}
}
