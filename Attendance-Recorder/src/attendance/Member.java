package attendance;

/**
 * 
 * @author Yunseo Han
 *
 */
public class Member {
	private String firstName;
	private String lastName;
	private int attendance;
	private int extra;


	public Member(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.attendance = 0;
		this.extra = 0;
	}


	@Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (o == this) {
        	return true;
        }
        Member obj = (Member) o;
        return (obj.getFirstName().equals(this.firstName) && obj.getLastName().equals(this.lastName));
    }



	@Override
	public int hashCode() {
		return (int) this.firstName.hashCode() * this.lastName.hashCode();
	}


	@Override
	public String toString() {
		return "[" + firstName + ", " + lastName + ", " + attendance + ", " + extra + "]";
	}
	
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}


	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}


	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	/**
	 * @return the attendance
	 */
	public int getAttendance() {
		return attendance;
	}


	/**
	 * @param attendance the attendance to set
	 */
	public void setAttendance(int attendance) {
		this.attendance = attendance;
	}


	/**
	 * @return the extra
	 */
	public int getExtra() {
		return extra;
	}


	/**
	 * @param extra the extra to set
	 */
	public void setExtra(int extra) {
		this.extra = extra;
	}
}
