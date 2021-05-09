package in.co.itlabs.util;

public enum BloodGroup {
	A_Positive("A+"), A_Negative("A-"), B_Positive("B+"), B_Negative("B-"), O_Positive("O+"), O_Negative("O-"),
	AB_Positive("AB+"), AB_Negative("AB-");

	private String displayName;

	BloodGroup(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return this.displayName;
	}
}
