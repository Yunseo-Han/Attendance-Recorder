package attendance;
import java.io.*;
import java.util.*;

import attendance.Member;

/**
 * 
 * @author Yunseo Han
 * take member file, convert it to Map<Member, list<Integer> attenance, extra points>
 * take attendance file, convert it to list<List<>first name, last name, extra points>
 * iterate through attendance file and add to member map
 * convert member map to csv
 */
public class AttandanceRecorder {
	File attendanceCsv;
	File memberDataCsv;
	FileReader attendanceFr;
	FileReader memberFr;
	
	public AttandanceRecorder(String memberPath, String attendancePath){
		makeFiles(memberPath, attendancePath);
		makeFileReaders();
		
		Map<Member, List<Integer>> memberMap = makeMemberMap(memberFr);
		List<Member> attendanceList = makeAttendanceList(attendanceFr);
		this.takeAttendance(memberMap, attendanceList);
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
	 * Creates a nested list of attendance names. 
	 * @param attendanceFr file reader for attendance csv containing time stamp, first name, last name
	 * @return List containing Members who attended. 
	 */
	public List<Member> makeAttendanceList(FileReader attendanceFr) {
		List<Member> attendanceList = new ArrayList<>();
		String line;
		try (BufferedReader br = new BufferedReader(attendanceFr)) {
			br.readLine();		// remove title line
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Member mem = new Member(values[1], values[2]);	// ignore values[0] since it contains time stamp
				attendanceList.add(mem);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(attendanceList.size());
		return attendanceList;
	}
	
	
	/**
	 * Creates a HashMap from member data csv 
	 * @param memberFr file reader for the member data csv containing first name, last name, attendance, extra points 
	 * @return Map<Member, List<Integer>> containing all Members as keys and a list as values which contains their attendance and extra points 
	 */
	public Map<Member, List<Integer>> makeMemberMap(FileReader memberFr) {
		Map<Member, List<Integer>> memberMap = new HashMap<>();
		String line;
		
		try (BufferedReader br = new BufferedReader(memberFr)) {
			br.readLine();		// remove title line
			
			while ((line = br.readLine()) != null) {
				List<Integer> memberPoints = new ArrayList<>();
				String[] values = line.split(",");
				Member mbr = new Member(values[0], values[1]);
				try {
					memberPoints.add(Integer.valueOf(values[2]));	// attendance
					mbr.setAttendance(Integer.parseInt(values[2]));
				} catch (ArrayIndexOutOfBoundsException e) {
					memberPoints.add(0);							// if there are no attendance, add 0 to memberPoints
				}
				try {
					memberPoints.add(Integer.valueOf(values[3]));	// extra points
					mbr.setAttendance(Integer.parseInt(values[3]));
				} catch (ArrayIndexOutOfBoundsException e) { 
					memberPoints.add(0);							// if there are no extra points, add 0 to memberPoints
				}
				memberMap.put(mbr, memberPoints);
			}
		} catch (IOException e) { e.printStackTrace(); }
		
		System.out.println(memberMap.size());
		return memberMap;
	}
	
	
	public List<Member> takeAttendance(Map<Member, List<Integer>> memberMap, List<Member> attendanceList) {
		// check for duplicates
		// if name in attendance not found in members, print name
		List<Member> newMemberData = new ArrayList<>();
		List<Member> rogueAttendees = new ArrayList<>();
		
		for (Member e: attendanceList) {
			List<Integer> points = memberMap.get(e);
			if (points!=null) {
				memberMap.remove(e);
				e.setAttendance(points.get(0)+1);	
				e.setExtra(points.get(1));			// extra points don't do anything yet
				newMemberData.add(e);
			}
			else {
				rogueAttendees.add(e);
				memberMap.remove(e);
			}
		}
		
		System.out.println("\n\nAttendance taken:");
		this.printContent(newMemberData);
		newMemberData.addAll(memberMap.keySet());
		System.out.println("\n\nRogue attendees:");
		this.printContent(rogueAttendees);
		System.out.println("\n\nNew member data:");
		this.printContent(newMemberData);
		
		return newMemberData;
	}
	
	
	private void printContent(List<Member> list) {
		for (Member e: list) {
			System.out.println(e.toString());
		}
	}

	
	public static void main(String[] args) {
		AttandanceRecorder test = new AttandanceRecorder(
								"C:\\Users\\yunse_76cbckb\\Documents\\GitHub\\Attendance-Recorder\\testing member form - Sheet1.csv",
								"C:\\Users\\yunse_76cbckb\\Documents\\GitHub\\Attendance-Recorder\\attendance csv\\testing attendance form (Responses) - Form Responses 1.csv");
	
		
		// check attendance for duplicates?
	}

}
