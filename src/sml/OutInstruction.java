package sml;

/**
 * An SML instruction which when given one parameter s1 will print the contents
 * of register s1 on the Java console (using <code>println</code>).
 */
public class OutInstruction extends Instruction {

	private int op1;

	public OutInstruction(String l, String op) {
		super(l, op);
	}

	public OutInstruction(String label, int op1) {
		this(label, "out");
		this.op1 = op1;
	}
	
	public OutInstruction(String label, String... params) {
		super(label, params);
	}

	@Override
	public void execute(Machine m) {
		System.out.println(getMachineRegister(m, op1));
	}

	@Override
	public String toString() {
		return super.toString() + " register " + op1;
	}

	@Override
	protected void setParameters(String... params) {
		assertCorrectParameterCount(params, 1);
		this.op1 = parseIntParameter(params[0]);
	}

}
