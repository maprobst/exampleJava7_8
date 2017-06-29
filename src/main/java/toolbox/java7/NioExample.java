package toolbox.java7;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NioExample {

    public static void main(String[] args) {
	final NioExample nio = new NioExample();

	try {
	    nio.start();
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void start() throws IOException {

	// Freien Speicher f�r alle Dateisysteme anzeigen
	final FileSystem fs = FileSystems.getDefault();
	for (FileStore store : fs.getFileStores()) {
	    System.out.println(store + " useable: " + Long.valueOf(store.getUsableSpace()));
	}

	// Tempor�re Datei erstellen und wenn schon vorhanden l�schen
	final Path projectTempFile = Paths.get(".", "temp", "file.txt"); // .
									 // richtig
									 // aufl�sen!
	if (!Files.exists(projectTempFile.getParent())) {
	    Files.createDirectory(projectTempFile.getParent());
	}
	else {
	    Files.deleteIfExists(projectTempFile);
	}

	// Text in die Tempor�re Datei mit akt. Zeitstempel
	final Charset charset = Charset.forName("UTF-8");
	try (BufferedWriter writer = Files.newBufferedWriter(projectTempFile, charset)) {
	    writer.write(String.format("%1$tH:%1$tM:%1$tS Hallo World", new Date()));
	}

	// Datei verschieben und ggf. das Original ersetzen
	final Path projectTempFileNew = fs.getPath(projectTempFile.getParent().toString(), "file2.txt");
	Files.move(projectTempFile, projectTempFileNew, StandardCopyOption.REPLACE_EXISTING);

	// Durchlaufe das aktuelle Verzeichnis und gib alle Verzeichnisnamen aus
	FileVisitor<? super Path> visitor = new MyFileVisitor();
	Files.walkFileTree(Paths.get("."), visitor);

	// Link anlegen
	addLink(projectTempFileNew);

	// Watch Service erstellen und auf �nderung der tempor�ren Datei
	// registieren
	final WatchService watchService = fs.newWatchService();
	WatchKey key = projectTempFileNew.getParent().register(
		watchService,
		StandardWatchEventKinds.ENTRY_MODIFY,
		StandardWatchEventKinds.ENTRY_CREATE);

	ExecutorService executor = Executors.newSingleThreadExecutor();
	executor.submit(new WatchServiceTask(key));
	executor.shutdown();

	try {
	    executor.awaitTermination(5, TimeUnit.MINUTES);
	}
	catch (InterruptedException e) {
	    Thread.currentThread().interrupt();
	}
    }

    private void addLink(final Path projectTempFileNew) throws IOException {
	// Link anlegen
	Path hlink = Paths.get(URI.create("file:/C:/Java7/hlink.txt"));
	// Link L�schen falls schon existiert
	Files.deleteIfExists(hlink);
	// Anlegen des Links
	Files.createLink(hlink, projectTempFileNew);

	System.out
		.printf(
			"Beide Dateien sind %1$s %2$s",
			Files.isSameFile(projectTempFileNew, hlink) ? "gleich"
				: "nicht gleich",
			System.lineSeparator());
	System.out.println("Inhalt Ursprungsdatei"
		+ Files.readAllLines(projectTempFileNew)
		+ System.lineSeparator()
		+ "Inhalt Linkdatei"
		+ Files.readAllLines(hlink));

    }

}

class WatchServiceTask implements Runnable {

    /** Der Key f�r die Abfrage der Events */
    private final WatchKey m_WatchKey;

    public WatchServiceTask(WatchKey key) {
	m_WatchKey = key;
    }

    @Override
    public void run() {
	while (true) {

	    Path dir = (Path) m_WatchKey.watchable();

	    System.out.println("Horche 30 Sekunden auf �nderungen in Verzeichnis");
	    System.out.println(dir.normalize().toUri());

	    try {
		// 30 Sekunden warten
		Thread.sleep(TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
	    }
	    catch (InterruptedException e) {
		Thread.currentThread().interrupt();
	    }

	    List<WatchEvent<?>> events = m_WatchKey.pollEvents();
	    if (events.isEmpty()) {
		System.out.println("Keine �nderungen. WatchService wird beendet.");
		break;
	    }

	    for (WatchEvent<?> event : events) {

		Path file = dir.resolve(event.context().toString());

		try {
		    if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
			System.out.printf("Datei %1$s erstellt", file.getFileName().toString());
		    }
		    else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(event.kind())) {
			FileTime lastModifiedTime = Files.getLastModifiedTime(file);
			System.out
				.printf(

					"Datei %1$s wurde %3$d-mal ge�ndert (Zeitstempel %2$tH:%2$tM:%2$tS)",
					file.getFileName().toString(),
					new Date(lastModifiedTime.toMillis()),
					Integer.valueOf(event.count()));
		    }
		}
		catch (Throwable t) {
		    t.printStackTrace();
		}
	    }
	}
    }
}

/**
 * Visitor der alle Verzeichnisnamen ausgibt
 */
class MyFileVisitor extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
	System.out.println(dir.normalize().toUri());
	return super.preVisitDirectory(dir, attrs);
    }
}
