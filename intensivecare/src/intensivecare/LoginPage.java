package intensivecare;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//bisogna scindere l'implementazione del login dalla parte puramente grafica della pagina
public final class LoginPage extends JFrame {
	private static final String name = "Terapia intesiva: LOGIN";
	
	private static final JTextField user = new JTextField();
    private static final JPasswordField psw = new JPasswordField();
    
    private static final JLabel userLabel = new JLabel("Username: ");
    private static final JLabel pswLabel = new JLabel("Password: ");
    
    private static final JButton btnLogin = new JButton("Login");
    private static final JButton btnCancel = new JButton("Exit");

    private static final JPanel panel = new JPanel(new GridLayout(2,1));
    private static final JPanel sPanel = new JPanel(new GridLayout(2,1));

    private static LogAs log;
    private static boolean alreadyLogged = false;
    private static String loginType;
    private static String loggedUser = "";
	private final Container frmContentPane = this.getContentPane();
	
	private static final LoginPage login = new LoginPage();

	public static LoginPage getInstance() {
		return login;
	}
	
	private LoginPage() {
		super(name);
        btnLogin.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent event) {
                try {
                	if(alreadyLogged) {
                		JOptionPane.showMessageDialog(LoginPage.this,
					            "Un utente ha già effettuato l'accesso",
					            "Errore",
					            JOptionPane.ERROR_MESSAGE);
                	} else if(user.getText().trim().isEmpty() || (new String (psw.getPassword())).isEmpty()) {
            			JOptionPane.showMessageDialog(LoginPage.this,
            		            "Password e/o username non inserito",
            		            "Errore",
            		            JOptionPane.ERROR_MESSAGE);
            		} else if (authenticate(user.getText().trim(), (new String (psw.getPassword())))) {
					    JOptionPane.showMessageDialog(LoginPage.this,
					            "Login effettuato con successo.",
					            "Login",
					            JOptionPane.INFORMATION_MESSAGE);
					    					    
					    NursePage nursePage;
					    HeadPhysicianPage headPhyisicianPage;
					    DoctorPage doctorPage;
					    					    
					    switch (log) {
						case NURSE:
							nursePage = new NursePage(loggedUser); break;
						case HEADPHYSICIAN:
							headPhyisicianPage= new HeadPhysicianPage(loggedUser); break;
						case DOCTOR:
							if(!IntensiveCare.patients.isEmpty())
								doctorPage =new DoctorPage(loggedUser);
							else {
								alreadyLogged = false;
								JOptionPane.showMessageDialog(LoginPage.this,
							            "Nessun paziente ricoverato",
							            "Errore",
							            JOptionPane.ERROR_MESSAGE);
							}
						default:
							break;
					    }
					} else {
						JOptionPane.showMessageDialog(LoginPage.this,
						            "Password e/o username non valido",
						            "Errore",
						            JOptionPane.ERROR_MESSAGE);
						}
				} catch (HeadlessException e) {
					e.printStackTrace();
				}
               
            resetFields();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                saveAndExit();
            }
        });
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(LoginPage.this, 
                    "Sei sicuro di voler uscire", "Chiudere la finestra?", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    saveAndExit();
                }
            }
        });
        
        addComponents();
		setup();
	}

	//funzione che si occupa di aprire il file con le informazioni degli utenti e confronta
	//psw e user inseriti con quelli dei file
	//se trova una coppia uguale va a controllare anche il tipo di login(magazziniere ecc) e lo salva
	//nella variabile log  e restituisce true, altrimenti false
	
	private boolean authenticate(String username, String password) {
		String record = "";
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader("./login.csv"));
			while ((record = br.readLine()) != null) {
				String[] split = record.split(";");
				
				if(split.length != 4)
					throw new CsvFileCorruptedException("Controllare l'integrità  del file login.csv");
				
				if (username.equals(split[0]) && password.equals(split[1])) {
					alreadyLogged = true;
					loggedUser = split[3];
					loginType = split[2];
					
					switch (loginType) {
					case "N":
						log = LogAs.NURSE;
						break;
					case "H":
						log = LogAs.HEADPHYSICIAN;
						break;
					case "D":
						log = LogAs.DOCTOR;
						break;
					default:
						break;
					}
					return true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvFileCorruptedException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
		return false;
	}
	
	private void saveAndExit() {
		dispose();
		try {
			FileOutputStream fileOut = new FileOutputStream("./patients/patientslist.ser");
    		ObjectOutputStream out = new ObjectOutputStream(fileOut);
    		out.writeObject(IntensiveCare.patients);
    		out.close();
    		fileOut.close();
    		System.out.println("Saved");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        System.exit(0);
	}
	
	private void resetFields() {
        user.setText("");
	    psw.setText("");
	}
	
	private void setup() {
		this.getRootPane().setDefaultButton(btnLogin);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setSize(300, 200);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private void addComponents() {
		frmContentPane.add(panel, BorderLayout.CENTER);
		frmContentPane.add(sPanel, BorderLayout.SOUTH);
        
        sPanel.add(btnLogin);
        sPanel.add(btnCancel);
		panel.add(userLabel);
        panel.add(user);
        panel.add(pswLabel);
        panel.add(psw);
	}
	
	public static void unlog() {
		alreadyLogged = false;
		loginType = "";
	}
	
	public static String getLoginType() {
		return loginType;
	}
}
