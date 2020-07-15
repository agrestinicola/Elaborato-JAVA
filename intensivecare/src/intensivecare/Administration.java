package intensivecare;

import java.io.Serializable;

public final class Administration implements Serializable{
	private final String pres;
	private final Date date;
	private final String time;
	private final String additionalNotes;
	private final int dose;
	
	public Administration(Prescription prescription, String notes) {
		this.pres = prescription.fastPrint();
		this.additionalNotes = notes;
		this.dose = prescription.getHowMuch();
		this.time = SaveToFile.getTodayTime();
		this.date = new Date();
	}
	
	@Override
	public String toString() {
		return date.toString() + " " +  time + pres + " " + additionalNotes;
	}
	
	public int getDay() {
		return date.getDay();
	}
}
