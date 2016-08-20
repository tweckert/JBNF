package javax.bnf;

import javax.bnf.BnfGrammarParser;
import javax.bnf.BnfRule;
import javax.util.FileIO;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Thomas Weckert
 */
public class BnfGrammarParserTest {
	
	@Test
	public void testBuildBnfGrammar() throws Exception {
		
		CharSequence bnfGrammarText = FileIO.readFile("/sentence.bnf", "UTF-8");
		
		BnfRule firstRule = new BnfGrammarParser().buildBnfGrammar(bnfGrammarText);
		
		Assert.assertTrue("sentence".equals(firstRule.getSymbol()));
		Assert.assertTrue("sentence := word{' 'word}('. '|'.');".equals(firstRule.toString()));

		Assert.assertTrue("word".equals(firstRule.getNextRule().getSymbol()));
		Assert.assertTrue("word := letter{letter};".equals(firstRule.getNextRule().toString()));
		
		Assert.assertTrue("letter".equals(firstRule.getNextRule().getNextRule().getSymbol()));
		Assert.assertTrue("letter := 'a'|'b'|'c'|'d'|'e'|'f'|'g'|'h'|'i'|'j'|'k'|'l'|'m'|'n'|'o'|'p'|'q'|'r'|'s'|'t'|'u'|'v'|'w'|'x'|'y'|'z'|'A'|'B'|'C'|'D'|'E'|'F'|'G'|'H'|'I'|'J'|'K'|'L'|'M'|'N'|'O'|'P'|'Q'|'R'|'S'|'T'|'U'|'V'|'W'|'X'|'Y'|'Z';".equals(firstRule.getNextRule().getNextRule().toString()));
		
		Assert.assertTrue(firstRule.getNextRule().getNextRule().getNextRule() == null);
	}
	
}
