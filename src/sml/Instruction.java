package sml;

/**
 * This class is the superclass of the classes for machine instructions
 * 
 * @author someone
 */

public abstract class Instruction {

	protected String label;
	protected String opcode;

	// Constructor: an instruction with label l and opcode op
	// (op must be an operation of the language)

	public Instruction(String l, String op) {
		String opcode = getOpCode();
		if (!op.equals(opcode)) {
			throw new IllegalArgumentException("Invalid op code '" + op + "' (expected '" + opcode + "')");
		}
		this.label = l;
		this.opcode = opcode;
	}

	public Instruction(String l, String... params) {
		this.label = l;
		this.opcode = getOpCode();
		setParameters(params);
	}

	// = the representation "label: opcode" of this Instruction

	@Override
	public String toString() {
		return label + ": " + opcode;
	}

	// Execute this instruction on machine m.

	public abstract void execute(Machine m);

	// Return the opcode for this instruction

	protected abstract String getOpCode();

	// Assign the parameters. Throw an exception for wrong values.

	protected abstract void setParameters(String... params);
	
	// Safe way to get a machine register from machines with differing register counts.
	
	protected static int getMachineRegister(Machine m, int i) {
		try {
			return m.getRegisters().getRegister(i);
		} catch (IndexOutOfBoundsException e) {
			System.err.format("Invalid register number %d.%n", i);
			return 0;
		} catch (Exception e) {
			System.err.format("Error getting register %d: %s%n", i, e.getMessage());
			return 0;
		}

	}

	// Safe way to set a machine register from machines with differing register
	// counts.

	protected static void setMachineRegister(Machine m, int i, int v) {
		try {
			m.getRegisters().setRegister(i, v);
		} catch (IndexOutOfBoundsException e) {
			System.err.format("Invalid register number %d.%n", i);
		} catch (Exception e) {
			System.err.format("Error setting register %d to %d: %s%n", i, v, e.getMessage());
		}
	}

	// Utility helper method for checking parameter counts.
	
	protected static void assertCorrectParameterCount(String[] params, int expectedCount) {
		int actualCount = params.length;
		if (actualCount != expectedCount) {
			String message = String.format("Incorrect number of arguments (expected %d, received %d)", expectedCount, actualCount);
			throw new IllegalArgumentException(message);
		}
	}

	// Utility helper method for parsing integer parameters.
	
	protected static int parseIntParameter(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid integer parameter '" + string + "'");
		}
	}

}
