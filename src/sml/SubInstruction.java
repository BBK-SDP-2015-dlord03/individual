package sml;

/**
 * An SML instruction which when given three parameters s1, s2 and r will
 * subtract the contents of register s2 from the contents of s1 and store the
 * result in register r.
 */
public class SubInstruction extends Instruction {

	private int result;
	private int op1;
	private int op2;

	public SubInstruction(String l, String op) {
		super(l, op);
	}

	public SubInstruction(String label, int result, int op1, int op2) {
		this(label, "sub");
		this.result = result;
		this.op1 = op1;
		this.op2 = op2;
	}
	
	@Override
	public void execute(Machine m) {
		int value1 = m.getRegisters().getRegister(op1);
		int value2 = m.getRegisters().getRegister(op2);
		m.getRegisters().setRegister(result, value1 - value2);
	}
	
	@Override
	public String toString() {
		return super.toString() + " register " + op1 + " - register " + op2 + " to register " + result;
	}

}
