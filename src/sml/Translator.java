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
					System.err.println("Error whilst reading program: Missing or invalid label at line "
							+ lineNumber);
					return false;
				}

				int existingIndex = labels.indexOf(label);
				if (existingIndex != -1) {
					System.err.println("Error whilst reading program: Duplicate label " + label + " at lines "
							+ existingIndex + " and " + (lineNumber + 1));
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

			System.err.println("Error reading program: IO error " + ioE.getMessage());
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

	private Instruction createInstruction(String ins, String label, String[] params) {
		
		if (ins == null || ins.length() == 0) return null;
		
		String className = new StringBuilder()
			.append("sml.")
			.append(ins.substring(0, 1).toUpperCase())
			.append(ins.substring(1))
			.append("Instruction").toString();
		
		try {
			Class<?> instructionClass = Class.forName(className);
			Constructor<?> instructionConstructor = instructionClass.getDeclaredConstructor(new Class[] {String.class, String[].class});
			return (Instruction) instructionConstructor.newInstance(label, params);
		} catch (ClassNotFoundException e) {
			System.err.println("Error whilst creating instruction " + ins + " at label " + label + ": Unknown instruction");
			return null;
		} catch (IllegalArgumentException e) {
			System.err.println("Error whilst creating instruction " + ins + " at label " + label + ": " + e.getMessage());
			return null;
		} catch (InvocationTargetException e) {
			System.err.println("Error whilst creating instruction " + ins + " at label " + label + ": " + e.getTargetException().getMessage());
			return null;
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e) {
			System.err.println("Internal error whilst creating instruction " + ins + " at label " + label);
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

	private String[] split() {
		return line.trim().split("\\s");
	}
}