package intensivecare;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public final class Patient implements Serializable{
	private final String healthCode;
	private final String surname, name;
	private final Date dateB;
	private final String placeB;
	private String diagnosis;
	private final List<Prescription> prescriptions = new ArrayList<Prescription>();
	private final Queue<Administration> admins = new ArrayBlockingQueue<Administration>(70, true);
	private transient VitalSignMonitor monitor;

	public Patient(String surname, String name, Date dateB, String placeB, String healthCode) {
		this.surname = surname;
		this.name = name;
		this.dateB = dateB;
		this.placeB = placeB;
		this.diagnosis = "#";
		this.healthCode = healthCode;
		monitor = new VitalSignMonitor(healthCode);
		monitor.startMonitoring();
	}
	
	public void makePrescription(Prescription pres) {
		prescriptions.add(pres);
	}
	
	public void makeAdministration(Administration admin) {
		if(admins.size() == 50) {
			admins.poll();
			admins.offer(admin);
			VitalSignPage.updateAdminsArea(admins, healthCode);
			return;
		}
		admins.offer(admin);
		VitalSignPage.updateAdminsArea(admins, healthCode);
	}
	
	public void readDiagnosisFromFile() {
		try {
			diagnosis = new String(Files.readAllBytes(Paths.get("./patients/" + healthCode + "/diagnosis.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startMonitoring() {
		monitor.startMonitoring();
	}
	
	@Override
	public String toString() {
		return "Codice paziente: " + healthCode + "\nNome: " + name + "\nCognome: " + surname + 
				"\nData di nascita: " + dateB.toString() + "\nLuogo di nascita: " + placeB + "\n";
	}
	
	@Override
	public int hashCode() {
		return healthCode.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Patient) {
			Patient otherAsPatient = (Patient) other;
			return otherAsPatient.hashCode() == hashCode();
		} else {
			return false;
		}
	}
	
	public String getHealthCode() {
		return healthCode;	
	}
	
	public String getDiagnosis() {
		return diagnosis;
	}
	
	public List<Prescription> getPrescriptions() {
		return prescriptions;
	}
	
	public Queue<Administration> getAdministrations() {
		return admins;
	}
	
	public VitalSignMonitor getMonitor() {
		return monitor;
	}
	
	public void setMonitor(VitalSignMonitor monitor) {
		this.monitor = monitor;
	}
}
