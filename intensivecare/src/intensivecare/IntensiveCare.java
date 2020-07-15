package intensivecare;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;

public final class IntensiveCare {
	static Set<Patient> patients;

	private static void init() {
		try {
			FileInputStream fileIn = new FileInputStream("./patients/patientslist.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			patients = (HashSet<Patient>) in.readObject();
			for(Patient patient: patients) {
				patient.setMonitor(new VitalSignMonitor(patient.getHealthCode()));
				patient.startMonitoring();
			}
			in.close();
			fileIn.close();
		} catch (FileNotFoundException e) {
			patients = new HashSet<Patient>();
			System.out.println("Nessun file patientslist.ser da cui attingere informazioni");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		init();
		LoginPage login = LoginPage.getInstance();
		VitalSignPage vital = VitalSignPage.getInstance();
	}
}
