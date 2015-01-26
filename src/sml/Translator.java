package sml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
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
					System.err.println("Missing or invalid label at line " + lineNumber);
					return false;
				}

				int existingIndex = labels.indexOf(label);
				if (existingIndex != -1) {
					System.err.println("Duplicate label " + label + " at lines " + existingIndex + " and " + (lineNumber + 1));
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

			System.err.println("File: IO error " + ioE.getMessage());
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
		try {
			switch (ins) {
			case "add":
				return new AddInstruction(label, params);
			case "sub":
				return new SubInstruction(label, params);
			case "mul":
				return new MulInstruction(label, params);
			case "div":
				return new DivInstruction(label, params);
			case "out":
				return new OutInstruction(label, params);
			case "lin":
				return new LinInstruction(label, params);
			case "bnz":
				return new BnzInstruction(label, params);
			}
		} catch (Exception e) {
			System.err.println("Error processing instruction " + ins + " at label " + label + ": " + e.getMessage());
			return null;
		}
		System.err.println("Unknown operation " + ins + " at label " + label);
		return null;
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