package toolbox.java7;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ExceptionHandlingExample {

    public static void main(String[] args) {
	ExceptionHandlingExample handling = new ExceptionHandlingExample();

	try {
	    // Kein Exception-Handling der IOException notwendig, durch Rethrow
	    // einer anderen Exception
	    handling.rethrowVonExceptions();
	}
	catch (IllegalArgumentException e) {
	    System.out.printf("Neue Exception %1$s nach dem Rethrow %n", e.getClass());
	}

	// Verhalten des bisherigen Exception-Handling in Java 6
	try {
	    handling.multiCatchOld(ExceptionTyp.IO);
	}
	catch (Exception e) {
	    System.out.println("Multi-Catch mit IO Exception Java 6 implementierung");
	}
	try {
	    handling.multiCatchOld(ExceptionTyp.INDEXOUTOFBOUNDS);
	}
	catch (Exception e) {
	    System.out.println("Multi-Catch mit IO Exception Java 6 implementierung");
	}

	// Verhalten des bisherigen Exception-Handling in Java 7
	try {
	    handling.multiCatch(ExceptionTyp.IO);
	}
	catch (Exception e) {
	    System.out.println("Multi-Catch mit IO Exception Java 7 implementierung");
	}
	try {
	    handling.multiCatch(ExceptionTyp.INDEXOUTOFBOUNDS);
	}
	catch (Exception e) {
	    System.out.println("Multi-Catch mit IO Exception Java 7 implementierung");
	}
    }

    public void rethrowVonExceptions() throws IllegalArgumentException {
	// Java 6 -> public void rethrowVonExceptions() throws Throwable {
	try {
	    throwExction(ExceptionTyp.IO);
	}
	catch (Throwable ex) {
	    /**
	     * die Referenzvariable ex im catch -Block ist "effectively final",
	     * wird wie eine final -Variable behandelt und man darf an diese
	     * Variable nichts zuweisen. Compiler erkennt, dass es sich nicht
	     * mehr um einen Rethrow handelt.
	     */
	    System.out.printf("Urspr�ngliche Exception  %1$s %n", ex.getClass());
	    throw new IllegalArgumentException("Andere Exception");
	}
    }

    /**
     *
     * Schreibweise um mehrere Exceptions in Java 7 abfangen zu k�nnen.
     *
     * @param typ
     * @throws IOException
     * @throws IndexOutOfBoundsException
     */
    public void multiCatch(ExceptionTyp typ) throws IOException {
	try {
	    throwExction(typ);
	}
	catch (IOException | IndexOutOfBoundsException ex) {
	    /**
	     * die Referenzvariable ex im catch -Block ist "effectively final",
	     * wird wie eine final -Variable behandelt und man darf an diese
	     * Variable nichts zuweisen.
	     */
	    System.err.println(ex);
	    throw ex;
	}
    }

    /**
     * Schreibweise um mehrere Exceptions in Java 6 abfangen zu k�nnen.
     *
     * @param typ
     *            welche Exception soll geworfen werden
     * @throws IOException
     */
    public void multiCatchOld(ExceptionTyp typ) throws IOException {
	try {
	    throwExction(typ);
	}
	catch (IndexOutOfBoundsException ex) {
	    System.err.println(ex);
	    throw ex;
	}
	catch (IOException ex) {
	    System.err.println(ex);
	    throw ex;
	}
    }

    /**
     * Abh�ngig vom Enum wird eine Exception geworfen.
     *
     * @param typ
     *            welche Exception soll geworfen werden
     * @throws IOException
     */
    private void throwExction(ExceptionTyp typ) throws IOException {
	if (typ == ExceptionTyp.IO) {
	    // Wirft eine IOException, da File ein Verzeichnis ist
	    @SuppressWarnings({ "unused", "resource" })
	    PrintWriter out = new PrintWriter(new FileWriter("src"));
	}

	if (typ == ExceptionTyp.INDEXOUTOFBOUNDS) {
	    int size = 2;

	    List<Integer> list = new ArrayList<Integer>(size);
	    for (int i = 0; i < size; i++) {
		list.add(new Integer(i));
	    }

	    for (int i = 0; i < 5; i++) {
		// get(i) wirft beim 3 Durchlauf eine IndexOutOfBoundsException
		list.get(i);
	    }
	}
    }

    /**
     * Hilfsenum um jeweils andere Exception werfen zu lassen.
     */
    enum ExceptionTyp {
	IO,
	INDEXOUTOFBOUNDS
    }
}
