package de.nexum.bnf;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.nexum.bnf.util.FileIO;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfParserTest {
	
	private CharSequence bnfGrammarText;
	
	@Before
	public void setup() throws IOException {
		bnfGrammarText = FileIO.readFile("/test1.bnf", "UTF-8");
	}
	
	@Test
	public void testBuildBnfGrammar() throws BnfGrammarParseException {
		
		BnfRule firstRule = new BnfGrammarParser(bnfGrammarText).buildBnfGrammar();
		
		Assert.assertTrue("sentence".equals(firstRule.getSymbol()));
		Assert.assertTrue("sentence := word{' 'word}('. '|'.');".equals(firstRule.toString()));

		Assert.assertTrue("word".equals(firstRule.getNextRule().getSymbol()));
		Assert.assertTrue("word := letter{letter};".equals(firstRule.getNextRule().toString()));
		
		Assert.assertTrue("letter".equals(firstRule.getNextRule().getNextRule().getSymbol()));
		Assert.assertTrue("letter := 'a'|'b'|'c'|'d'|'e'|'f'|'g'|'h'|'i'|'j'|'k'|'l'|'m'|'n'|'o'|'p'|'q'|'r'|'s'|'t'|'u'|'v'|'w'|'x'|'y'|'z'|'A'|'B'|'C'|'D'|'E'|'F'|'G'|'H'|'I'|'J'|'K'|'L'|'M'|'N'|'O'|'P'|'Q'|'R'|'S'|'T'|'U'|'V'|'W'|'X'|'Y'|'Z';".equals(firstRule.getNextRule().getNextRule().toString()));
		
		Assert.assertTrue(firstRule.getNextRule().getNextRule().getNextRule() == null);
	}
	
}
