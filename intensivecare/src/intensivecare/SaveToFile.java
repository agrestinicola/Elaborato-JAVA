package intensivecare;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class SaveToFile {
	private static LocalDateTime today = LocalDateTime.now();
	private final static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm ");
	private final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy ");
	private final static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm  ");
	
	private SaveToFile() {}
	
	public static void saveVitalSign(String healthCode, String what,int integer) {
		String path = "./patients/" + healthCode + "/";
		checkFolders(path, healthCode);
		try {
			FileWriter fw = new FileWriter(path + "vitalsign.txt", true);
			fw.write(today.format(dateTimeFormat) + what + ": " + integer + "\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void savePrescription(String healthCode, Prescription pres) {
		String path = "./patients/" + healthCode + "/";
		try {
			FileWriter fw = new FileWriter(path + "prescriptions.txt", true);
			fw.write(pres.toString());
			fw.close();
			logHP("P " + today.format(dateTimeFormat) + healthCode + " nuova prescrizione " + pres.fastPrint() + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void savePersonalInformations(Patient patient) {
		String path = "./patients/" + patient.getHealthCode() + "/";
		checkFolders(path, patient.getHealthCode());
		try {
			PrintWriter pw = new PrintWriter(path + "informations.txt");
			pw.print(patient.toString());
			pw.close();
			logHP("R " + today.format(dateTimeFormat) + patient.getHealthCode() + " ricoverato\n");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void saveDiagnosis(String healthCode, String diagnosis) {
		String path = "./patients/" + healthCode + "/";
		try {
			PrintWriter pw = new PrintWriter(path + "diagnosis.txt");
			pw.print(diagnosis);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void saveDischargeLetter(String healthCode, String letter, String loggedUser) {
		String path = "./patients/" + healthCode + "/";
		try {
			PrintWriter pw = new PrintWriter(path + "dischargeletter.txt");
			pw.print(letter + "\nResponsabile: " + loggedUser + "\nData e ora: " + today.format(dateTimeFormat));
			pw.close();
			logHP("D " + today.format(dateTimeFormat) + healthCode + " è stato dimesso\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void logActivity(String healthCode, String insert, String extra) {
		String path = "./patients/" + healthCode + "/";
		try {
			FileWriter fw = new FileWriter(path + "log.log", true);
			String written = "A " + today.format(dateTimeFormat) + insert + " " + extra + "\n"; 
			fw.write(written);
			fw.close();
			logHP(written);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void logActivity(String healthCode, String insert) {
		String path = "./patients/" + healthCode + "/";
		try {
			FileWriter fw = new FileWriter(path + "log.log", true);
			String written = "S " + today.format(dateTimeFormat) + insert + "\n";
			fw.write(written);
			fw.close();
			logHP(written);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void logHP(String insert) {
		String path = "./logs/" + today.getYear() + "/";
		checkFolders(path, "log");
		try {
			FileWriter fw = new FileWriter(path + String.format("%02d", today.getDayOfMonth()) + "-" + String.format("%02d", today.getMonthValue()) + ".log", true);
			fw.write(insert);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void checkFolders(String path, String healthCode) {
		new File(path).mkdirs();
	}
	
	public static int getTodayDay() {
		return today.getDayOfMonth();
	}
	
	public static int getTodayMonth() {
		return today.getMonthValue();
	}
	
	public static int getTodayYear() {
		return today.getYear();
	}
	
	public static String getTodayDate() {
		return today.format(dateFormat);
	}
	
	public static String getTodayDateTime() {
		return today.format(dateTimeFormat);
	}
	
	public static String getTodayTime() {
		return today.format(timeFormat);
	}
}
