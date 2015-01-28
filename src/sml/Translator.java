package sml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 */
public class Translator {

	// word + line is the part of the current line that's not yet processed
	// word has no whitespace
	// If word and line are not empty, line begins with whitespace
	private String line = "";
	private Labels labels; // The labels of the program being translated
	private ArrayList<Instruction> program; // The program to be created
	private String fileName; // source file of SML code

	private static final String SRC = "src";

	public Translator(String fileName) {
		this.fileName = SRC + "/" + fileName;
	}

	// translate the small program in the file into lab (the labels) and
	// prog (the program)
	// return "no errors were detected"
	public boolean readAndTranslate(Labels lab, ArrayList<Instruction> prog) {

		try (Scanner sc = new Scanner(new File(fileName))) {
			// Scanner attached to the file chosen by the user
			labels = lab;
			labels.reset();
			program = prog;
			program.clear();

			int lineNumber = 1;
			// Each iteration processes line and reads the next line into line
			while (sc.hasNextLine()) {

				line = sc.nextLine();

				// Store the label in label
				String label = scan();

				if (label.length() == 0) {
					System.err.format("Error whilst reading program: Missing or invalid label at line %d\n", lineNumber);
					return false;
				}

				int existingIndex = labels.indexOf(label);
				if (existingIndex != -1) {
					System.err.format("Error whilst reading program: Duplicate label '%s' at lines %d and %d.\n", label, existingIndex + 1, lineNumber);
					return false;
				}

				Instruction ins = getInstruction(label);
				if (ins == null) {
					return false;
				}

				labels.addLabel(label);
				program.add(ins);

				lineNumber++;

			}
			
		} catch (IOException ioE) {

			System.err.format("Error reading program: IO error %s.\n", ioE.getMessage());
			return false;

		}

		return true;

	}

	// line should consist of an MML instruction, with its label already
	// removed. Translate line into an instruction with label label
	// and return the instruction
	public Instruction getInstruction(String label) {

		if (line.equals(""))
			return null;

		String ins = scan();
		String[] params = split();
		return createInstruction(ins, label, params);

	}

	/*
	 * Create an Instruction via reflection. Returns a valid
	 * <code>Instruction</code> object or <code>null</code> if errors are
	 * encountered. Any errors are reported to the standard error output stream.
	 */
	private static Instruction createInstruction(String ins, String label, String[] params) {
		
		String errorMessage = String.format("Error whilst creating instruction '%s' with label '%s'", ins, label);

		if (ins == null || ins.length() < 2) {
			System.err.println(String.format("%s: %s.", errorMessage, "Invalid instruction name"));
			return null;
		}
		
		String className = new StringBuilder()
			.append("sml.")
			.append(ins.substring(0, 1).toUpperCase())
			.append(ins.substring(1))
			.append("Instruction").toString();
		
		try {
			
			Class<?> instructionClass = Class.forName(className);
			Class<?>[] constructorParams = new Class[] {String.class, String[].class};
			Constructor<?> instructionConstructor = instructionClass.getDeclaredConstructor(constructorParams);
			Object instruction = instructionConstructor.newInstance(label, params);
			if (!(instruction instanceof Instruction)) {
				System.err.println(String.format("%s: %s.", errorMessage, "Invalid instruction class"));
				return null;
			}
			return (Instruction) instructionConstructor.newInstance(label, params);
			
		} catch (ClassNotFoundException e) {
			
			System.err.println(String.format("%s: %s.", errorMessage, "Unknown instruction"));
			return null;
			
		} catch (IllegalArgumentException e) {
			
			System.err.println(String.format("%s: %s.", errorMessage, e.getMessage()));
			return null;
			
		} catch (InvocationTargetException e) {
			
			System.err.println(String.format("%s: %s.", errorMessage, e.getTargetException().getMessage()));
			return null;
			
		} catch (ReflectiveOperationException e) {
			
			System.err.println(String.format("%s: %s.", errorMessage, e.getMessage()));
			return null;
		}
		
	}
	
	/*
	 * Return the first word of line and remove it from line. If there is no
	 * word, return ""
	 */
	private String scan() {
		
		line = line.trim();
		if (line.length() == 0)
			return "";

		int i = 0;
		while (i < line.length() && line.charAt(i) != ' ' && line.charAt(i) != '\t') {
			i = i + 1;
		}
		
		String word = line.substring(0, i);
		line = line.substring(i);
		return word;
	}

	/*
	 * Split a string into an array of strings delimiting on whitespace once any
	 * leading and trailing whitespace is removed.
	 */
	private String[] split() {
		return line.trim().split("\\s");
	}
	
}