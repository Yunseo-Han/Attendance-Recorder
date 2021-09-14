package attendence;
import java.io.*;
import java.util.*;
import attendence.MemberName;
import attendence.MemberAttended;

public class AttandanceRecorder {
	File attendanceCsv;
	File memberDataCsv;
	FileReader attendanceFr;
	FileReader memberFr;
	
	public AttandanceRecorder(String memberPath, String attendancePath){
		makeFiles(memberPath, attendancePath);
		makeFileReaders();
		
		makeAttendanceList(attendanceFr);
		makeMemberMap(memberFr);
	}
	
	private void makeFiles(String memberPath, String attendancePath) {
		this.attendanceCsv = new File(attendancePath);
		this.memberDataCsv = new File(memberPath);
	}
	
	private void makeFileReaders() {
		try {
			this.memberFr = new FileReader(memberDataCsv);
			this.attendanceFr = new FileReader(attendanceCsv);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Creates a nested list of attendance names. Inner list stores first name, last name, and extra points (if there are any).
	 * @param attendenceFr
	 * @return List that contains String Lists containing first name, last name, and extra points. 
	 */
	public List<List<String>> makeAttendanceList(FileReader attendenceFr) {
		List<List<String>> attendenceList = new ArrayList<>();
		String line;
		try (BufferedReader br = new BufferedReader(attendenceFr)) {
			br.readLine();		// remove title line
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				List<String> row = new ArrayList<>(Arrays.asList(values));
				row.remove(0);	// remove timestamp
				attendenceList.add(row);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(attendenceList.size());
		return attendenceList;
	}
	
	
	/**
	 * Creates a HashMap of member data. 
	 * @param memberFr
	 * @return
	 */
	public Map<List<String>, List<Integer>> makeMemberMap(FileReader memberFr) {
		Map<List<String>, List<Integer>> memberMap = new HashMap<>();
		String line;
		
		try (BufferedReader br = new BufferedReader(memberFr)) {
			br.readLine();		// remove title line
			
			while ((line = br.readLine()) != null) {
				List<String> memberName = new ArrayList<>();
				List<Integer> memberPoints = new ArrayList<>();
				String[] values = line.split(",");
				
				memberName.add(values[0]);						// first name
				memberName.add(values[1]);						// last name
				memberPoints.add(Integer.valueOf(values[2]));	// attendance
				try {
					memberPoints.add(Integer.valueOf(values[3]));	// extra points
				} catch (ArrayIndexOutOfBoundsException e) { /* do nothing */ }
				
				memberMap.put(memberName, memberPoints);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(memberMap.size());
		return memberMap;
	}
	
	
	public Map<List<String>, List<Integer>> takeAttendance(Map<List<String>, List<Integer>> memberMap, List<List<String>> attendenceList) {
		// check for duplicates
		// if name in attendence not found in members, print name
		
		for (List<String> e: attendenceList) {
			
		}
		
		return memberMap;
	}
	
	
	
	
	
	public static void main(String[] args) {
		AttandanceRecorder test = new AttandanceRecorder(
								"C:\\Users\\yunse_76cbckb\\Documents\\GitHub\\Attendance-Recorder\\testing member form - Sheet1.csv",
								"C:\\Users\\yunse_76cbckb\\Documents\\GitHub\\Attendance-Recorder\\attendance csv\\testing attendance form (Responses) - Form Responses 1.csv");
	
		// check attendence for duplicates?
	}

}
