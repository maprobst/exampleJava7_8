package toolbox.java7;

public class LiteralsExample {

    public static void main(String[] args) {
	LiteralsExample example = new LiteralsExample();
	example.binary();
	example.unterstriche();
    }

    private void binary() {
	System.out.println("Binary------------------------------------------------");
	// Bis einschlie�lich Java 6
	int wertOld = Integer.parseInt("1011", 2);

	// Ab Java 7 Binary mit Prefix 0b oder 0B gefolgt von der Nummer
	int wertNeu = 0b1011;

	System.out.printf("Der Wert in Java6 %1$d und in Java7 %2$d %n", wertOld, wertNeu);

	int decimal = 43;
	int hex = 0x2B;
	int octal = 053;
	int binary = 0b101011; // Neu sollte mit 0b oder 0B beginnen
	System.out.println("Der Wert von decimal ist : " + decimal);
	System.out.println("Der Wert von hex ist : " + hex);
	System.out.println("Der Wert von ocatl ist : " + octal);
	System.out.println("Der Wert von binary ist : " + binary);
    }

    private void unterstriche() {
	System.out.println("Unterstriche in Nummern------------------------------------------------");

	// An praktisch allen stellen in einem numerischen Literal Unterstriche
	// einzuf�gen, Beginn und Ende nicht
	// Der Compiler entfernt beim Parsen die Unterstriche
	long telefonNr = +49_721_6291_88L;
	long kreditkartenNr = 1234_5678_9012_3456L;
	float pi = 3.14_15F;
	long hexadecimalBytes = 0xFF_EC_DE_5E;
	long hexadecimalWords = 0xCAFE_BABE;
	long maxOfLong = 0x7fff_ffff_ffff_ffffL;
	byte byteInBinary = 0b0010_0101;
	long longInBinary = 0b11010010_01101001_10010100_10010010;
	int addition = 12_3 + 3_2_1;
	System.out.println("Der Compilerwert von +49_721_6291_88L ist " + telefonNr);
	System.out.println("Der Compilerwert von 1234_5678_9012_3456L ist " + kreditkartenNr);
	System.out.println("Der Compilerwert von 3.14_15F ist " + pi);
	System.out.println("Der Compilerwert von 0xFF_EC_DE_5E ist " + hexadecimalBytes);
	System.out.println("Der Compilerwert von 0xCAFE_BABE ist " + hexadecimalWords);
	System.out.println("Der Compilerwert von 0x7fff_ffff_ffff_ffffL ist " + maxOfLong);
	System.out.println("Der Compilerwert von 0b0010_0101 ist :" + byteInBinary);
	System.out.println("Der Compilerwert von 0b11010010_01101001_10010100_10010010 ist " + longInBinary);
	System.out.println("Der Compilerwert von 12_3 + 3_2_1 ist " + addition);
    }
}
