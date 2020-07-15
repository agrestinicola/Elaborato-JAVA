package intensivecare;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

public final class WeeklyReportPage extends JFrame{
	private final JTextArea report = new JTextArea();
	private final JButton selectFolderBtn = new JButton("Seleziona");
	private final SpinnerNumberModel spin1 = new SpinnerNumberModel(SaveToFile.getTodayDay(),1,31,1);
	private final SpinnerNumberModel spin2 = new SpinnerNumberModel(SaveToFile.getTodayMonth(),1,12,1);
	private final SpinnerNumberModel spin3 = new SpinnerNumberModel(SaveToFile.getTodayYear(),minYear(),SaveToFile.getTodayYear(),1);
	private final JSpinner daySpinner = new JSpinner(spin1);
	private final JSpinner monthSpinner = new JSpinner(spin2);
	private final JSpinner yearSpinner = new JSpinner(spin3);
	private final JPanel panel = new JPanel();
	private final Container frmContentPane = this.getContentPane();		
	private final JScrollPane scrollPane = new JScrollPane(report);
	private String[] fileList = new String[366];
	
	public WeeklyReportPage(String loggedUser) {
		super("REPORT SETTIMANALI: login come " + loggedUser + " (P)");
		
		selectFolderBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				report.setText("");
				if (Date.checkDate((int) daySpinner.getValue(), (int) monthSpinner.getValue(), (int) yearSpinner.getValue()) 
						&& Date.checkMax((int) daySpinner.getValue(), (int) monthSpinner.getValue(),(int) yearSpinner.getValue()))
					displayReport((int) daySpinner.getValue(), (int) monthSpinner.getValue(), (int) yearSpinner.getValue());
				else {
					JOptionPane.showMessageDialog(WeeklyReportPage.this,
				            "Data inserita non valida",
				            "Errore",
				            JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	
		addComponents();
		setup();
	}
	
	private int minYear() {
		File[] folders = new File("./logs").listFiles();
		return Integer.parseInt(folders[0].toString().substring(7));
	}
	
	private void displayReport(int day, int month, int year) {
		File[] files = new File("./logs/" + String.valueOf(year)).listFiles();
		int counter = 0;
		for(File file: files)
			if(file.isFile()) {
				fileList[counter] = file.toString().substring(12,17);
				counter++;
			}
		for(counter++; counter < 366; counter++)
			fileList[counter] = null;
		
		Arrays.sort(fileList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if(o1 == null || o2 == null)
					return 0;
				int diff = Integer.parseInt(o1.substring(3)) - Integer.parseInt(o2.substring(3));
				if (diff != 0)
					return diff;
				else {
					return Integer.parseInt(o1.substring(0, 2)) - Integer.parseInt(o2.substring(0, 2));		
				}
			}
		});

		int startIndex = Date.getDayOfTheYear(day, month, year) - 1; 
		for(int i = startIndex; i < startIndex + 7; i++) {
			try (Scanner input = new Scanner(Paths.get("./logs/" + year +  "/" + fileList[i] + ".log"))) {
				report.append("Data:" + fileList[i] + "\n");
			    while (input.hasNextLine()) {
			        report.append(input.nextLine() + "\n");
			    }
			} catch (IOException e) {
				System.out.println("Non ci sono ulteriori file log");
			    return;
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Selezionare anno nuovo");
				return;
			}
		}
	}

	private void setup() {
		this.setLocationRelativeTo(null);
		this.setSize(672, 376);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void addComponents() {
		frmContentPane.add(panel, null);
		
		panel.setLayout(null);
		
		daySpinner.setBounds(10, 11, 37, 20);
		daySpinner.setEditor(new JSpinner.NumberEditor(daySpinner, "#"));
		panel.add(daySpinner);
		
		monthSpinner.setBounds(57, 11, 37, 20);
		monthSpinner.setEditor(new JSpinner.NumberEditor(monthSpinner, "#"));
		panel.add(monthSpinner);
		
		yearSpinner.setBounds(105, 11, 54, 20);
		yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
		panel.add(yearSpinner);
		
		scrollPane.setBounds(10, 42, 646, 294);
		panel.add(scrollPane);
		report.setLineWrap(true);
		report.setWrapStyleWord(true);
		report.setEditable(false);

		selectFolderBtn.setBounds(169, 11, 90, 20);
		panel.add(selectFolderBtn);
	}
}
	
