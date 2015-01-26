package sml;

public class OutInstruction extends Instruction {

	private int op1;

	public OutInstruction(String l, String op) {
		super(l, op);
	}

	public OutInstruction(String label, int op1) {
		this(label, "out");
		this.op1 = op1;
	}

	@Override
	public void execute(Machine m) {
		System.out.println(m.getRegisters().getRegister(op1));
	}

	@Override
	public String toString() {
		return super.toString() + " register " + op1;
	}

	@Override
	protected String getOpCode() {
		return "out";
	}

	@Override
	protected void setParameters(String... params) {
		// TODO Auto-generated method stub
	}

}
