package toolbox.java7;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinExample {

    public static void main(String[] args) {

	final String text = "Eine Frage raubt mir den Verstand: Bin Ich verr�ckt oder sind es alle anderen hier";

	ForkJoinExample e = new ForkJoinExample();
	String result = e.toUpperCase(text);
	System.out.println(result);
    }

    /**
     * Die Methode wandelt einen �bergebenen String �ber den
     * {@link ForkJoinPool} in uppercase um
     *
     * @param text
     *            Text der umgewandelt werden soll
     * @return Der �bergebene Text in Gr��erschreibweise
     */
    public String toUpperCase(String text) {
	List<String> stringList = Arrays.asList(text.split("\\s"));
	return ForkJoinPool.commonPool().invoke(new UpperCaseTask(stringList));
    }
}

/**
 * Implementierung eines {@link RecursiveTask} der eine Liste von String bekommt
 * und diese rekursiv aufteilt und verarbeitet.
 */
class UpperCaseTask extends RecursiveTask<String> {
    /** H�lt die Liste an String die der Task toUppercase machen soll */
    private final List<String> m_StringList;

    /**
     * Konstruktor �bergibt die Liste an Strings
     *
     * @param stringList
     *            Liste mit Strings
     */
    public UpperCaseTask(List<String> stringList) {
	System.out.println("UpperCaseTask" + stringList);
	m_StringList = stringList;
    }

    /**
     * @see java.util.concurrent.RecursiveTask#compute()
     */
    @Override
    protected String compute() {
	if (m_StringList.size() == 1) {
	    // Liste hat nur ein Element => direkt zur�ckliefern
	    return m_StringList.get(0).toUpperCase();
	}

	// Liste in einen linken und rechten Teil umwandeln
	final List<String> left, right;
	if (m_StringList.size() % 2 == 0) {
	    final int partLength = m_StringList.size() / 2;
	    left = m_StringList.subList(0, partLength);
	    right = m_StringList.subList(partLength, m_StringList.size());
	}
	else {
	    left = Collections.singletonList(m_StringList.get(0));
	    right = m_StringList.subList(1, m_StringList.size());
	}

	// Neuen Task f�r den linken Teil erstellen und forken
	UpperCaseTask forkSubTask = new UpperCaseTask(left);
	forkSubTask.fork();

	// Neuen Task f�r den rechten Teil erstellen und direkt ausf�hren
	UpperCaseTask rightSubTask = new UpperCaseTask(right);
	String rightResult = rightSubTask.compute();

	// Auf das Ergebnis des linken Task warten und verketten
	String leftResult = forkSubTask.join();
	return leftResult + " " + rightResult;
    }
}