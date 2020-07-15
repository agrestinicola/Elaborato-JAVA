package intensivecare;

import java.io.Serializable;

public final class Prescription implements Serializable{
	private final String medName;
	private final Date presDate;
	private final int therapyLength;
	private final int dailyDoses;
	private final int howMuch;
	private final String respDoctor;

	public Prescription(String medName, Date presDate, int therapyLength, int dailyDoses, int howMuch, String respDoctor) {
		this.medName = medName;
		this.presDate = presDate;
		this.therapyLength = therapyLength;
		this.dailyDoses = dailyDoses;
		this.howMuch = howMuch;
		this.respDoctor = respDoctor;
	}
	
	public String fastPrint() {
		return medName + therapyLength + dailyDoses + howMuch + presDate.toString();
	}
	
	@Override
	public String toString() {
		return "Data: " + presDate.toString() + "\nMedico Responsabile: " + respDoctor + "\nFarmaco: " + medName +"\nLunghezza terapia (in giorni): " 
				+ therapyLength + "\nNumero dosi giornaliere: " + dailyDoses + "\nQuantità (in pastiglie): " + howMuch + "\n";
	}
	
	public int getHowMuch() {
		return howMuch;
	}
}
