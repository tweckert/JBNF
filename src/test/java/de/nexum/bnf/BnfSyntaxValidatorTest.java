package de.nexum.bnf;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.nexum.util.FileIO;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfSyntaxValidatorTest {

	private CharSequence bnfGrammarText;
	
	@Before
	public void setup() throws IOException {
		bnfGrammarText = FileIO.readFile("/test1.bnf", "UTF-8");
	}
	
	@Test
	public void testBuildBnfGrammar() throws BnfGrammarParseException, BnfSyntaxValidationException {
		
		BnfRule firstRule = new BnfGrammarParser(bnfGrammarText).buildBnfGrammar();
		
		boolean isValid = new BnfSyntaxValidator().checkSyntax("This is a test.", firstRule);
		//Assert.assertTrue(isValid);
	}
	
}
