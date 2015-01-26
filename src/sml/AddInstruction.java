package sml;

/**
 * This class ....
 * 
 * @author someone
 */

public class AddInstruction extends Instruction {

	private int result;
	private int op1;
	private int op2;

	public AddInstruction(String label, String op) {
		super(label, op);
	}

	public AddInstruction(String label, String... params) {
		super(label, params);
	}

	@Override
	public void execute(Machine m) {
		int value1 = m.getRegisters().getRegister(op1);
		int value2 = m.getRegisters().getRegister(op2);
		m.getRegisters().setRegister(result, value1 + value2);
	}

	@Override
	public String toString() {
		return super.toString() + " register " + op1 + " + register " + op2 + " to register " + result;
	}

	@Override
	protected String getOpCode() {
		return "add";
	}

	@Override
	protected void setParameters(String... params) {
		assertCorrectParameterCount(params, 3);
		this.result = parseIntParameter(params[0]);
		this.op1 = parseIntParameter(params[1]);
		this.op2 = parseIntParameter(params[2]);
	}

}
