package toolbox.java7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AutomaticResourceManagementExample {

    public static void main(String[] args) {
	AutomaticResourceManagementExample arm = new AutomaticResourceManagementExample();
	arm.autoClosableExcption();
    }

    private String readFirstLineFromFileOld(String path) throws IOException {
	BufferedReader br = new BufferedReader(new FileReader(path));
	try {
	    return br.readLine();
	}
	finally {
	    br.close();
	}
    }

    private String readFirstLineFromFile(String path) throws IOException {
	try (BufferedReader br = new BufferedReader(new FileReader(path))) {
	    return br.readLine();
	}
    }

    private String readFirstLineFromFileTwoRessources(String path) throws IOException
    {
	// Nebeneffekt!!!Bei beiden Ressourcen wird die close() -Methode
	// aufgerufen, obwohl die BufferedReader.close() implizit auch die
	// FileReader.close aufruft.
	try (FileReader fin = new FileReader(path);
		BufferedReader br = new BufferedReader(fin)) {
	    return br.readLine();
	}
    }

    private static class ResourceA implements AutoCloseable {
	public void read() throws Exception {
	    throw new Exception("Exception der Ressource A in der Read-Methode");
	}

	@Override
	public void close() throws Exception {
	    throw new Exception("Exception der Ressource A in der Close-Methode");
	}
    };

    public void autoClosableExcption() {
	try {
	    ResourceA a = new ResourceA();
	    a.read();
	}
	catch (Exception e) {
	    // Um die Suppressed Exception mit auszugeben
	    e.printStackTrace();
	}
    }
}
