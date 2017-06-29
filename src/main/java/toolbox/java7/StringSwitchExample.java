package toolbox.java7;

public class StringSwitchExample {

    public static void main(String[] args) {
	StringSwitchExample switchTest = new StringSwitchExample();
	String result = "";
	int value = 2;
	result = switchTest.getWochentagSwitchStatementString(switchTest
		.getWochentagSwitchStatementInt(value));
	System.out.printf("F�r den Integer-Wert %1$d des Wochentages gilt folgendes %2$s %n", value, result);
	value = 7;
	result = switchTest.getWochentagSwitchStatementString(switchTest
		.getWochentagSwitchStatementInt(value));
	System.out.printf("F�r den Integer-Wert %1$d des Wochentages gilt folgendes %2$s", value, result);
    }

    /**
     * Neue Verwendung des "switch-Statements" per String-Value
     *
     * @param tag
     *            Wochentag als String
     * @return Event des jeweiligen Wochentages
     */
    public String getWochentagSwitchStatementString(String tag) {
	/*
	 * Die Implementierung des neue String- switch eliminiert den Overhead
	 * indem sie erst �ber die HashCodes der String verzweigt und danach
	 * (bei Bedarf) noch den Vergleich per String.equals() nutzt. -> Der
	 * neue String- switch ist also nicht nur �bersichtlicher, sondern auch
	 * noch effizienter.
	 */
	String event;
	switch (tag) {
	case "Montag":
	    event = "Beginn der Arbeitswoche";
	    break;
	case "Dienstag":
	    event = "Motiviert";
	    break;
	case "Mittwoch":
	    event = "Bergfest";
	    break;
	case "Donnerstag":
	    event = "Genervt";
	    break;
	case "Freitag":
	    event = "Ende der Arbeitswoche";
	    break;
	case "Samstag":
	    event = "Wochenende";
	    break;
	case "Sonntag":
	    event = "Nichts tun";
	    break;
	default:
	    throw new IllegalArgumentException("Ung�ltiger Wochentag: " + tag);
	}
	return event;
    }

    /**
     * Bisherige Verwendung des "switch-Statements" per int-Value
     *
     * @param tag
     *            1-7 f�r den jeweiligen Wochentag
     * @return Wochentag als String
     */
    public String getWochentagSwitchStatementInt(int tag) {
	String tagString;
	switch (tag) {
	case 1:
	    tagString = "Montag";
	    break;
	case 2:
	    tagString = "Dienstag";
	    break;
	case 3:
	    tagString = "Mittwoch";
	    break;
	case 4:
	    tagString = "Donnerstag";
	    break;
	case 5:
	    tagString = "Freitag";
	    break;
	case 6:
	    tagString = "Samstag";
	    break;
	case 7:
	    tagString = "Sonntag";
	    break;
	default:
	    throw new IllegalArgumentException("Ung�ltiger Wochentag: " + tag);
	}
	return tagString;
    }

}
