package sml;

/**
 * An SML instruction which when given three parameters s1, s2 and r will
 * multiply the contents of registers s1 and s2 and store the result in register
 * r.
 */
public class MulInstruction extends Instruction {

	private int result;
	private int op1;
	private int op2;

	public MulInstruction(String l, String op) {
		super(l, op);
	}

	public MulInstruction(String label, String... params) {
		super(label, params);
	}

	@Override
	public void execute(Machine m) {
		int value1 = getMachineRegister(m, op1);
		int value2 = getMachineRegister(m, op2);
		setMachineRegister(m, result, value1 * value2);
	}
	
	@Override
	public String toString() {
		return super.toString() + " register " + op1 + " * register " + op2 + " to register " + result;
	}

	@Override
	protected String getOpCode() {
		return "mul";
	}

	@Override
	protected void setParameters(String... params) {
		assertCorrectParameterCount(params, 3);
		this.result = parseIntParameter(params[0]);
		this.op1 = parseIntParameter(params[1]);
		this.op2 = parseIntParameter(params[2]);
	}

}
