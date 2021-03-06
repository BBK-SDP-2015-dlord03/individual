package sml;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class InstructionTest {

	private Machine machine;
	private List<Instruction> program;

	@Before
	public void setup() {
		machine = new Machine();
		program = machine.getProg();
	}

	@Test(expected = IllegalArgumentException.class)
	public void failToCreateInstructionWithWrongCode() {
		try {
			new LinInstruction("f0", "mul");
		} catch (Exception e) {
			System.err.format("%s.%n", e.getMessage());
			throw e;
		}
	}

	@Test
	public void addTwoNumbers() {
		program.add(new LinInstruction("f0", 2, 3));
		program.add(new LinInstruction("f1", 3, 4));
		program.add(new AddInstruction("f2", 1, 2, 3));
		machine.execute();
		assertRegisterEquals(1, 7);
	}

	@Test
	public void subtractTwoNumbers() {
		program.add(new LinInstruction("f0", 2, 3));
		program.add(new LinInstruction("f1", 3, 4));
		program.add(new SubInstruction("f2", 1, 2, 3));
		machine.execute();
		assertRegisterEquals(1, -1);
	}

	@Test
	public void multiplyTwoNumbers() {
		program.add(new LinInstruction("f0", 2, 3));
		program.add(new LinInstruction("f1", 3, 4));
		program.add(new MulInstruction("f2", 1, 2, 3));
		machine.execute();
		assertRegisterEquals(1, 12);
	}

	@Test
	public void divideTwoNumbers() {
		program.add(new LinInstruction("f0", 2, 12));
		program.add(new LinInstruction("f1", 3, 4));
		program.add(new DivInstruction("f2", 1, 2, 3));
		machine.execute();
		assertRegisterEquals(1, 3);
	}

	@Test
	public void setRegisterValues() {
		program.add(new LinInstruction("f0", 26, 64));
		program.add(new LinInstruction("f0", 13, 32));
		machine.execute();
		assertRegisterEquals(26, 64);
		assertRegisterEquals(13, 32);
	}

	@Test
	public void setInvalidRegisterValue() {
		program.add(new LinInstruction("f0", 65, 1));
		machine.execute();
	}
	
	private void assertRegisterEquals(int register, int expected) {
		assertEquals(expected, machine.getRegisters().getRegister(register));
	}

}
