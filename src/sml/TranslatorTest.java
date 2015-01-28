package sml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TranslatorTest {

	private Labels labels;
	private ArrayList<Instruction> program;

	@Before
	public void initialise() {
		labels = new Labels();
		program = new ArrayList<>();
	}

	@Test
	public void succeedFactorial() {
		Translator t = new Translator("code.sml");
		Machine m = new Machine();
		assertTrue(t.readAndTranslate(m.getLabels(), m.getProg()));
		assertEquals(7, m.getProg().size());
		m.execute();
		assertEquals(720, m.getRegisters().getRegister(21));
	}

	@Test
	public void succeedEmptyProgram() {
		Translator t = new Translator("empty.sml");
		Machine m = new Machine();
		assertTrue(t.readAndTranslate(m.getLabels(), m.getProg()));
		assertTrue(m.getProg().isEmpty());
		m.execute();
	}

	@Test
	public void rejectDuplicateLabel() {
		Translator t = new Translator("duplicate-label.sml");
		assertFalse(t.readAndTranslate(labels, program));
	}

	@Test
	public void rejectWrongParameterCount() {
		Translator t = new Translator("wrong-parameter-count.sml");
		assertFalse(t.readAndTranslate(labels, program));
	}

	@Test
	public void rejectWrongParameterType() {
		Translator t = new Translator("wrong-parameter-type.sml");
		assertFalse(t.readAndTranslate(labels, program));
	}

	@Test
	public void rejectInvalidInstruction() {
		Translator t = new Translator("wrong-instruction.sml");
		assertFalse(t.readAndTranslate(labels, program));
	}

	@Test
	public void failToReadMissingFile() {
		Translator t = new Translator("missing-file.sml");
		assertFalse(t.readAndTranslate(labels, program));
	}
	
	@Test
	public void failToCreateShortInstruction() {
		assertNull(createInstruction("x", "f0", null));
	}
	
	@Test
	public void failToCreateBadInstruction() {
		assertNull(createInstruction("mod", "f0", null));
	}
	
	@Test
	public void failToCreateNullInstruction() {
		assertNull(createInstruction(null, "f0", null));
	}
	
	@Test
	public void failToCreateEmptyInstruction() {
		assertNull(createInstruction("", "f0", null));
	}
	
	@Test
	public void succeedToCreateMulInstruction() {
		assertNotNull(createInstruction("mul", "f2", new String[] {"1", "2", "3"}));
	}
	
	/*
	 * Calls the static Translator.createInstruction() method by first making it non-private.
	 */
	private static Object createInstruction(String instruction, String label, String[] params) {
		try {
			Class<?>[] methodParams = new Class[] {String.class, String.class, String[].class};
			Method m = Translator.class.getDeclaredMethod("createInstruction", methodParams);
			m.setAccessible(true);
			return m.invoke(null, instruction, label, params);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
		
	}
	
}
