package intensivecare;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;

public final class AlarmPage extends JFrame {
	private final JPanel panel = new JPanel();
	private final JLabel lblNote = new JLabel("Note");
	private final JTextArea whatAlarm = new JTextArea();
	private final JTextArea noteAlarm = new JTextArea();
	private final JScrollPane scrollWhat = new JScrollPane(whatAlarm);
	private final JScrollPane scrollNote = new JScrollPane(noteAlarm);
	private final JLabel label = new JLabel(":");
	private final JLabel lblMinutes = new JLabel("00");
	private final JLabel lblSeconds = new JLabel("00");
	private final JLabel lblPatient = new JLabel();
	private final JButton btnStop = new JButton("Stop");
	private final JButton btnConfirm = new JButton("Conferma");
	
	private final Container frmContentPane = this.getContentPane();		
	private int seconds;
	private String what;
	private String healthCode;
	
	public AlarmPage(String healthCode, String what, int time) {
		super("ALLARME PAZIENTE");
		seconds = time;
		this.what = what;
		this.healthCode = healthCode;
		whatAlarm.setText(what);
		
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(LoginPage.getLoginType().equals("D")) {
						alarmThread.interrupt();
						btnConfirm.setEnabled(true);
					} else {
						JOptionPane.showMessageDialog(AlarmPage.this,
					            "Necessario un dottore per completare la seguente operazione",
					            "Errore",
					            JOptionPane.ERROR_MESSAGE);
						}
				} catch (NullPointerException e) {
					System.out.println("Necessario effettuare il login");
				}
			}
		});
		
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SaveToFile.logActivity(healthCode, what, "Disattivato dopo " + (time - seconds) + " secondi " + (noteAlarm.getText().equals("") ? "#" : noteAlarm.getText()));
				dispose();
			}
		});
		
		addComponents();
		setup();
		alarmThread.start();
	}
	
	private final Thread alarmThread = new Thread(){
		public void run() {
			while(seconds > -1) {
				try {
					sleep(1000);
					int res = seconds / 60;
					lblMinutes.setText(String.format("%02d", res));
					res = seconds - (res * 60);
					lblSeconds.setText(String.format("%02d", res == 60 ? 00 : res));
					seconds--;
				} catch (InterruptedException e) {
					return;
				}
			}
			
			JOptionPane.showMessageDialog(AlarmPage.this,
		            "L'allarme non è stato disattivato in tempo",
		            "TEMPO SCADUTO",
		            JOptionPane.ERROR_MESSAGE);
			noteAlarm.setEditable(false);
			SaveToFile.logActivity(healthCode,what, "L'allarme non è stato interrotto in tempo");
			dispose();
		}
	};
	
	private void setup() {
		this.getRootPane().setDefaultButton(btnConfirm);
		this.setLocationRelativeTo(null);
		this.setSize(284, 311);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void addComponents() {
		frmContentPane.add(panel, null);
		panel.setLayout(null);
		lblMinutes.setFont(new Font("Tahoma", Font.BOLD, 35));
		
		lblMinutes.setBounds(27, 30, 53, 37);
		panel.add(lblMinutes);
		lblSeconds.setFont(new Font("Tahoma", Font.BOLD, 35));
		
		lblSeconds.setBounds(104, 26, 59, 44);
		panel.add(lblSeconds);

		lblPatient.setText(healthCode);
		lblPatient.setBounds(79, 13, 124, 14);
		panel.add(lblPatient);
		
		btnStop.setBounds(173, 38, 74, 29);
		panel.add(btnStop);
		
		whatAlarm.setLineWrap(true);
		whatAlarm.setWrapStyleWord(true);
		whatAlarm.setEditable(false);
		scrollWhat.setBounds(27, 85, 220, 50);
		panel.add(scrollWhat);
		
		lblNote.setBounds(111, 140, 46, 14);
		panel.add(lblNote);
		
		noteAlarm.setLineWrap(true);
		noteAlarm.setWrapStyleWord(true);
		scrollNote.setBounds(27, 165, 220, 62);
		panel.add(scrollNote);
		
		btnConfirm.setBounds(79, 238, 110, 23);
		btnConfirm.setEnabled(false);
		panel.add(btnConfirm);
		label.setFont(new Font("Tahoma", Font.BOLD, 35));
		label.setBounds(79, 31, 46, 34);
		
		panel.add(label);
	}
}
