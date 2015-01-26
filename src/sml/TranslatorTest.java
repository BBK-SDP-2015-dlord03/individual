package sml;

import static org.junit.Assert.*;

import java.util.ArrayList;

import junit.framework.Assert;

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

}
