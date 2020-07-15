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

public final class HeadPhysicianPage extends JFrame{
	private final JButton writeDisLetterBtn = new JButton("Compila lettera di dimissione");
	private final JButton weeklyReport = new JButton("Report settimanale");
	private final Container frmContentPane = this.getContentPane();	
	private final JPanel panel = new JPanel();

	public HeadPhysicianPage(String loggedUser) {
		super("TERAPIA INTENSIVA: login come " + loggedUser + " (P)");
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LoginPage.unlog();
			}
		});
		
		weeklyReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
					try {
						WeeklyReportPage reportPage = new WeeklyReportPage(loggedUser);
					} catch (HeadlessException e ) {
						e.printStackTrace();
					}
			}
		});
		
		writeDisLetterBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(IntensiveCare.patients.isEmpty()) {
					JOptionPane.showMessageDialog(HeadPhysicianPage.this,
				            "Nessun paziente ricoverato",
				            "Errore",
				            JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						WriteDischargeLetterPage disPage = new WriteDischargeLetterPage(loggedUser);
					} catch (HeadlessException e ) {
						e.printStackTrace();
					}
				}
			}
		});
		
		addComponents();		
		setup();
	}
	
	private void setup() {
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	private void addComponents() {
		frmContentPane.add(panel, BorderLayout.CENTER);
		panel.add(writeDisLetterBtn);
		panel.add(weeklyReport);
	}
}

