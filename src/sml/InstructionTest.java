package sml;

import org.junit.Test;

public class InstructionTest {

	@Test(expected = IllegalArgumentException.class)
	public void failToCreateInstructionWithWrongCode() {
		Instruction instruction = new LinInstruction("f2", "mul");
	}

	@Test(expected = IllegalArgumentException.class)
	public void failToCreateInstructionWithWrongArguments() {
		Instruction instruction = new LinInstruction("f2", new String[] {"23"});
	}

}
