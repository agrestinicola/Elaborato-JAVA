package intensivecare;

import java.util.Queue;

/*
 * For example, to pick a number from 5 to 35 inclusively, 
 * the upper limit number will be 35-5+1=31 and 5 needs to be added to the result:
 * int pickedNumber = rand.nextInt(31) + 5; 
 * */

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;;

public final class VitalSignMonitor {
	//0 SBP 1 DBP 2 HR 3 T
	private int[] values = new int[4];
	private final int twoMinutes = 120 * 1000;
	private final int fiveMinutes = 180 * 1000;
	private final int threeMinutes = 300 * 1000;
	private final String healthCode;
	private final Timer timer = new Timer();
	private final Random rand = new Random();
	private Queue<String> vitalsQueue = new ArrayBlockingQueue<String>(120, true);
	
	public VitalSignMonitor(String healthCode) {
		this.healthCode = healthCode;
	}
	
	public void startMonitoring() {
		timer.scheduleAtFixedRate(hrTask, 0, fiveMinutes);
		timer.scheduleAtFixedRate(bpTask, 0, twoMinutes);
		timer.scheduleAtFixedRate(tTask, 0, threeMinutes);
	}
	
	public void stopMonitoring() {
		timer.cancel();
	}
	
	public int[] getValues() {
		return values;
	}

	public TimerTask bpTask = new TimerTask() {
		@Override
		public void run() {			
			//da 60 a 110
			values[1] = rand.nextInt(51) + 60;
			//da 80 a 180
			values[0] = rand.nextInt(101) + 80;
			
			SaveToFile.saveVitalSign(healthCode, "DBP", values[1]);
			SaveToFile.saveVitalSign(healthCode, "SBP", values[0]);
			addVitalsQueue("SBP: " + values[0] + "\n");
			addVitalsQueue("DBP: " + values[1] + "\n");
			VitalSignPage.updateVitals(healthCode, "DBP", values[1]);
			VitalSignPage.updateVitals(healthCode, "SBP", values[0]);
			VitalSignPage.updateVitalsArea(vitalsQueue, healthCode);
			
			if(values[1] < 70){
				AlarmPage alm = new AlarmPage(healthCode, "LIVELLO 2: IPOTENSIONE DIASTOLICA " + values[1] + "mmHg", 120);
				//System.out.println(healthCode + " LIVELLO 2: IPOTENSIONE DIASTOLICA " + values[1] + "mmHg");
			}
			else if(values[1] > 89){
				AlarmPage alm = new AlarmPage(healthCode, "LIVELLO 2: IPERTENSIONE DIASTOLICA " + values[1] + "mmHg", 120);
				//System.out.println(healthCode + " LIVELLO 2: IPERTENSIONE DIASTOLICA " + values[1] + "mmHg");
			}
			//else
				//System.out.println(healthCode + " Pressione diastolica: " + values[1] + "mmHg");
			
			if(values[0] < 100){
				AlarmPage alm = new AlarmPage(healthCode, "LIVELLO 2: IPOTENSIONE SISTOLICA " + values[0] + "mmHg", 120);
				//System.out.println(healthCode + " LIVELLO 2: IPOTENSIONE SISTOLICA " + values[0] + "mmHg");
			}
			else if(values[0] > 139){
				AlarmPage alm = new AlarmPage(healthCode, "LIVELLO 2: IPERTENSIONE SISTOLICA " + values[0] + "mmHg", 120);
				//System.out.println(healthCode + " LIVELLO 2: IPERTENSIONE SISTOLICA " + values[0] + "mmHg");
			}
			//else
				//System.out.println(healthCode + " Pressione sistolica: " + values[0] + "mmHg");
		}		
	};
	
	public TimerTask hrTask = new TimerTask() {
		@Override
		public void run() {
			//genero da 30 a 160
			values[2] = rand.nextInt(131) + 30;
			SaveToFile.saveVitalSign(healthCode, "HR", values[2]);
			addVitalsQueue("HR: " + values[2] + "\n");
			VitalSignPage.updateVitals(healthCode, "HR", values[2]);
			VitalSignPage.updateVitalsArea(vitalsQueue, healthCode);

			//tutto ok tra i 60 ed i 100, sopra i 150 ï¿½ flutter
			if(values[2] < 60){
				AlarmPage alm = new AlarmPage(healthCode, "LIVELLO 1: BRADICARDIA " + values[2] + "bpm", 180);
				//System.out.println(healthCode + " LIVELLO 1: BRADICARDIA " + values[2] + "bpm");
			}
			else if(values[2] > 100 && values[2] < 150){
				AlarmPage alm = new AlarmPage(healthCode, "LIVELLO 1: TACHICARDIA " + values[2] + "bpm", 180);
				//System.out.println(healthCode + " LIVELLO 1: TACHICARDIA " + values[2] + "bpm");
			}
			else if(values[2] > 150){
				AlarmPage alm = new AlarmPage(healthCode, "LIVELLO 3: FLUTTER " + values[2] + "bpm", 60);
				//System.out.println(healthCode + " LIVELLO 3: FLUTTER " + values[2] + "bpm");
			}
			//else
				//System.out.println(healthCode + " Frequenza cardiaca: " + values[2] + "bpm");
		}
	};
	
	public TimerTask tTask = new TimerTask() {
		@Override
		public synchronized void run() {
			//da 30 a 50
			//COUNTDOWN DELLA TEMPERATURA
			values[3] = rand.nextInt(21) + 30;
			
			SaveToFile.saveVitalSign(healthCode, "T", values[3]);
			addVitalsQueue("T: " + values[3] + "\n");
			VitalSignPage.updateVitals(healthCode, "T", values[3]);
			VitalSignPage.updateVitalsArea(vitalsQueue, healthCode);

			if(values[3] < 35){
				AlarmPage alm = new AlarmPage(healthCode, "LIVELLO 2: IPOTERMIA " + values[3] + "°C", 120);
				//System.out.println(healthCode + " LIVELLO 2: IPOTERMIA " + values[3] + "°C");
			}
			else if(values[3] > 39){
				AlarmPage alm = new AlarmPage(healthCode, "LIVELLO 2: IPERTERMIA " + values[3] + "°C", 120);
				//System.out.println(healthCode + " LIVELLO 2: IPERTERMIA " + values[3] + "°C");
			}
			//else
			//System.out.println(healthCode + " Temperatura: " + values[3] + "°C");
		}
	};
	
	public void addVitalsQueue(String insert) {
		if(vitalsQueue.size() == 120) {
			vitalsQueue.poll();
			vitalsQueue.add(insert);
			return;
		}
		vitalsQueue.add(insert);
	}
	
	public Queue<String> getVitalsQueue() {
		return vitalsQueue;
	}
}