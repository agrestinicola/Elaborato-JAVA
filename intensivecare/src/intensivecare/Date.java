package intensivecare;

import java.io.Serializable;

public final class Date implements Serializable{
	private final int day, month, year;

	public Date(int day, int month, int year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	public Date() {		
		this.day = SaveToFile.getTodayDay();
		this.month = SaveToFile.getTodayMonth();
		this.year = SaveToFile.getTodayYear();
	}
	
	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}
	
	@Override
	public String toString() {
		return String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year;
	}
	
	public static int getDayOfTheYear(int day, int month, int year) {
		int[] days = {31,28,31,30,31,30,31,31,30,31,30,31};
		if((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
			days[1] = 29;
		int sum = 0;
		for(int i = 0; i < month - 1; i++)
			sum += days[i];
		sum += day;
		
		return sum;
	}
	
	public static boolean checkDate(int day, int month, int year) {
		int[] days = {31,28,31,30,31,30,31,31,30,31,30,31};
		if((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
			days[1] = 29;
		return day <= days[month - 1];
	}
	
	//l'anno massimo consentito viene stabilito negli spinner
	public static boolean checkMax(int day, int month, int year) {
		if(year < SaveToFile.getTodayYear())
			return true;
		else if(year == SaveToFile.getTodayYear() && month == SaveToFile.getTodayMonth()) {
			if(day <= SaveToFile.getTodayDay())
				return true;
			else
				return false;
		} else if (month <= SaveToFile.getTodayMonth()) {
			return true;
		} else 
			return false;
	}
}
