package attendance;
import jdk.swing.interop.SwingInterOpUtils;

import java.io.*;
import java.util.*;

/**
 * 
 * @author Yunseo Han
 * take member file, convert it to Map<Member, list<Integer> attenance, extra points>
 * take attendance file, convert it to list<List<>first name, last name, extra points>
 * iterate through attendance file and add to member map
 * convert member map to csv
 */
public class AttendanceRecorder {
	File memberDataCsv;
	ArrayList<File> attendanceCsvList;
	HashMap<Member, Member> memberMap;
	HashMap<Member, Member> rogueMemberMap;


	public AttendanceRecorder(String memberPath, String attendanceFolderPath, String resultsPath){
		// initialize
		this.memberDataCsv = new File(memberPath);
		this.attendanceCsvList = new ArrayList<>();
		this.folderFiles(attendanceFolderPath);
		memberMap = new HashMap<>();
		rogueMemberMap = new HashMap<>();

		// routine
		try {
			FileReader memberFr = new FileReader(memberDataCsv);
			makeMemberMap(memberFr);
			memberFr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("FILE DOESN'T EXIST: CHECK MEMBER PATH");
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (File f: attendanceCsvList) {
			try {
				FileReader attendanceFr = new FileReader(f);
				List<Member> attendanceList = makeAttendanceList(attendanceFr);
				this.takeAttendance(attendanceList);
				attendanceFr.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("FILE DOESN'T EXIST: CHECK ATTENDANCE PATH");
			} catch (IOException e){
				e.printStackTrace();
			}
		}

		this.writeResultsCSV(resultsPath);
	}


	private void folderFiles(String folderPath){
		File folder = new File(folderPath);
		try {
			for (File file : folder.listFiles()) {
				if (!file.isDirectory()) {
					File csvFile = new File(file.getAbsolutePath());
					this.attendanceCsvList.add(csvFile);
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("EMPTY FOLDER: CHECK PATH");
		}
	}
	
	
	/**
	 * Creates a HashMap from member data csv 
	 * @param memberFr file reader for the member data csv containing first name, last name, attendance, extra points
	 */
	public void makeMemberMap(FileReader memberFr) {
		String line;
		
		try {
			BufferedReader br = new BufferedReader(memberFr);
			br.readLine();		// remove title line

			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Member mbr = new Member(values[0].toLowerCase(), values[1].toLowerCase());

				try {
					int existingAttendance = Integer.parseInt(values[2]);		// existing attendance
					mbr.setAttendance(existingAttendance);
				} catch (ArrayIndexOutOfBoundsException e) {
					// if there are no extra points, do nothing
				}
				try {
					int existingExtraPoints = Integer.parseInt(values[3]);		// existing extra points
					mbr.setExtra(existingExtraPoints);
				} catch (ArrayIndexOutOfBoundsException e) {
					// if there are no extra points, do nothing
				}
				this.memberMap.put(mbr, mbr);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Creates a nested list of attendance names.
	 * @param attendanceFr file reader for attendance csv containing time stamp, first name, last name
	 * @return List containing Members who attended.
	 */
	public List<Member> makeAttendanceList(FileReader attendanceFr) {
		List<Member> attendanceList = new ArrayList<>();
		String line;
		try {
			BufferedReader br = new BufferedReader(attendanceFr);
			br.readLine();		// remove title line

			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Member attendee = new Member(values[1].toLowerCase(), values[2].toLowerCase());		// ignore values[0] since it contains time stamp

				try {
					String val = values[3];		// extra points
					attendee.setExtra(1);		// one extra point per day
				} catch (ArrayIndexOutOfBoundsException e) {
					// if there are no extra points, do nothing
				}
				attendanceList.add(attendee);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(attendanceList.size());
		return attendanceList;
	}


	public void takeAttendance(List<Member> attendanceList) {
		List<Member> attendedMembers = new ArrayList<>();
		List<Member> rogueAttendees = new ArrayList<>();
		
		for (Member attendee: attendanceList) {
			Member mbr = this.memberMap.get(attendee);

			if (mbr!=null) {										// attendee already in member database
				mbr.setAttendance(mbr.getAttendance()+1);
				mbr.setExtra(mbr.getExtra()+attendee.getExtra());
				attendedMembers.add(mbr);
			}
			else {
				Member rogueMbr = this.rogueMemberMap.get(attendee);
				if (rogueMbr!=null) {
					rogueMbr.setAttendance((rogueMbr.getAttendance()+1));
					rogueMbr.setExtra(rogueMbr.getExtra()+attendee.getExtra());
					rogueAttendees.add(rogueMbr);
				}
				else{
					this.rogueMemberMap.put(attendee, attendee);
					rogueAttendees.add(attendee);
				}
			}
		}
		
		System.out.println("\n\nAttended members: " + attendedMembers.size());
		this.printContent(attendedMembers);
		System.out.println("\n\nRogue attendees: " + rogueAttendees.size());
		this.printContent(rogueAttendees);
	}
	
	
	private void printContent(List<Member> list) {
		for (Member e: list) {
			System.out.println(e.toString());
		}
	}


	private void writeResultsCSV(String filePath){
		try {
			File resultsFile = new File(filePath);
			FileWriter csvWriter = new FileWriter(resultsFile);
			csvWriter.append("First Name,Last Name,Attendance,Extra Points,Total");
			csvWriter.append("\n");

			// Getting an iterator
			Iterator memberMapIterator = this.memberMap.entrySet().iterator();
			Iterator rogueMemberMapIterator = this.rogueMemberMap.entrySet().iterator();

			// Iterate through the hashmaps
			while (memberMapIterator.hasNext()) {
				Map.Entry mapElement = (Map.Entry)memberMapIterator.next();
				Member mbr = (Member) mapElement.getValue();
				csvWriter.append(mbr.toCSV());
				csvWriter.append("\n");
			}
			csvWriter.append("\n");
			while (rogueMemberMapIterator.hasNext()) {
				Map.Entry mapElement = (Map.Entry)rogueMemberMapIterator.next();
				Member mbr = (Member) mapElement.getValue();
				csvWriter.append(mbr.toCSV());
				csvWriter.append("\n");
			}

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		/**
		 * first argument: 	path to csv file that contains member data in the format
		 * 					[first name, last name, attendance points, extra points
		 * second argument:	path to a folder that contains multiple csv files from the google attendance form.
		 * 					attendance form is in the format [timestamp, first name, last name, additional questions]
		 * third argument:	path to where the results should go. must make sure to include the csv file name and the
		 * 					filename extension .csv
		 * 					ex) "/Users/yunseohan/Downloads/results.csv"
		 */
		AttendanceRecorder test = new AttendanceRecorder(
								"/Users/yunseohan/Downloads/Fall 2021 Member List - Sheet1.csv",
								"/Users/yunseohan/Downloads/attendencefolder",
								"/Users/yunseohan/Downloads/results.csv");
	
		
		// check attendance for duplicates?
	}

}
