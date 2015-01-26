package sml;

/**
 * This class ....
 * 
 * @author someone
 */

public class LinInstruction extends Instruction {
	
	private int register;
	private int value;

	public LinInstruction(String label, String opcode) {
		super(label, opcode);
	}

	public LinInstruction(String label, String... params) {
		super(label, params);
	}

	@Override
	public void execute(Machine m) {
		m.getRegisters().setRegister(register, value);
	}

	@Override
	public String toString() {
		return super.toString() + " register " + register + " value is " + value;
	}

	@Override
	protected String getOpCode() {
		return "lin";
	}

	@Override
	protected void setParameters(String... params) {
		assertCorrectParameterCount(params, 2);
		this.register = parseInt(params[0]);
		this.value = parseInt(params[1]);
	}

}
