package sml;

public class BnzInstruction extends Instruction {

	private int op1;
	private String op2;

	public BnzInstruction(String l, String op) {
		super(l, op);
	}

	public BnzInstruction(String label, int op1, String op2) {
		this(label, "bnz");
		this.op1 = op1;
		this.op2 = op2;
	}

	@Override
	public void execute(Machine m) {
		int value1 = m.getRegisters().getRegister(op1);
		if (value1 != 1) {
			int pc = m.getLabels().indexOf(op2);
			if (pc != -1)
				m.setPc(pc);
		}
	}
	
	@Override
	public String toString() {
		return super.toString() + " if register " + op1 + " is not zero jump to " + op2;
	}

}
