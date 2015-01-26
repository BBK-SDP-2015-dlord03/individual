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
		if (!op.equals(getOpCode()))
			throw new IllegalArgumentException("Invalid op code " + op);
		this.label = l;
		this.opcode = op;
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
	
}
