package de.nexum.bnf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.nexum.util.FileIO;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class BnfSyntaxValidatorTest {
	
	@Test
	public void testValidateSentence() throws Exception {
		
		CharSequence bnfGrammarText = FileIO.readFile("/sentence.bnf", "UTF-8");
		
		BnfRule firstRule = new BnfGrammarParser().buildBnfGrammar(bnfGrammarText);
		Map<String, List<String>> values = 	new HashMap<String, List<String>>();
		
		boolean isValid = false;
		
		isValid = new BnfSyntaxValidator().checkSyntax("This is a test. ", firstRule, values);
		Assert.assertTrue(isValid == true);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("This is a test.", firstRule, values);
		Assert.assertTrue(isValid == true);
	}
	
	@Test
	public void testValidateTerminal() throws Exception {
		
		CharSequence bnfGrammarText = FileIO.readFile("/terminal.bnf", "UTF-8");
		
		BnfRule firstRule = new BnfGrammarParser().buildBnfGrammar(bnfGrammarText);
		Map<String, List<String>> values = 	new HashMap<String, List<String>>();
		
		boolean isValid = false;
		
		isValid = new BnfSyntaxValidator().checkSyntax("test", firstRule, values);
		Assert.assertTrue(isValid == true);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("test 123", firstRule, values);
		Assert.assertTrue(isValid == false);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("123 test", firstRule, values);
		Assert.assertTrue(isValid == false);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("123 test 456", firstRule, values);
		Assert.assertTrue(isValid == false);
	}
	
	@Test
	public void testValidateGroup() throws Exception {
		
		CharSequence bnfGrammarText = FileIO.readFile("/group.bnf", "UTF-8");
		
		BnfRule firstRule = new BnfGrammarParser().buildBnfGrammar(bnfGrammarText);
		Map<String, List<String>> values = 	new HashMap<String, List<String>>();
		
		boolean isValid = false;
		
		isValid = new BnfSyntaxValidator().checkSyntax("hello", firstRule, values);
		Assert.assertTrue(isValid == true);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("world", firstRule, values);
		Assert.assertTrue(isValid == true);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("hello 123", firstRule, values);
		Assert.assertTrue(isValid == false);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("123 hello", firstRule, values);
		Assert.assertTrue(isValid == false);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("123 hello 456", firstRule, values);
		Assert.assertTrue(isValid == false);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("world 123", firstRule, values);
		Assert.assertTrue(isValid == false);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("123 world", firstRule, values);
		Assert.assertTrue(isValid == false);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("123 world 456", firstRule, values);
		Assert.assertTrue(isValid == false);
	}
	
	@Test
	public void testValidateZeroOrMoreTimes() throws Exception {
		
		CharSequence bnfGrammarText = FileIO.readFile("/zero-or-more-times.bnf", "UTF-8");
		
		BnfRule firstRule = new BnfGrammarParser().buildBnfGrammar(bnfGrammarText);
		Map<String, List<String>> values = 	new HashMap<String, List<String>>();
		
		boolean isValid = false;
		
		isValid = new BnfSyntaxValidator().checkSyntax("hello", firstRule, values);
		Assert.assertTrue(isValid == true);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("hello hello", firstRule, values);
		Assert.assertTrue(isValid == true);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("hello hello    hello", firstRule, values);
		Assert.assertTrue(isValid == true);
	}
	
	@Test
	public void testValidateOnceOrNotAtAll() throws Exception {
		
		CharSequence bnfGrammarText = FileIO.readFile("/once-or-not-at-all.bnf", "UTF-8");
		
		BnfRule firstRule = new BnfGrammarParser().buildBnfGrammar(bnfGrammarText);
		Map<String, List<String>> values = 	new HashMap<String, List<String>>();
		
		boolean isValid = false;
		
		isValid = new BnfSyntaxValidator().checkSyntax("hello", firstRule, values);
		Assert.assertTrue(isValid == true);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("hello, world", firstRule, values);
		Assert.assertTrue(isValid == true);
		
		values.clear();
		
		isValid = new BnfSyntaxValidator().checkSyntax("hello, world, world", firstRule, values);
		Assert.assertTrue(isValid == false);
	}
	
}
