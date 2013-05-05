package jbehave.addition.steps;


import static org.junit.Assert.assertEquals;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.jbehave.core.annotations.Then;

import jbehave.addition.Addition;

 
public class Additionsteps {
	
	private Addition addition;
	private int result;
	
	@Given ("I have a calculator")
	public void aCalculator(){

		addition = new Addition();
		
	}
	
	@When ("I add 2 and 3")
	public void addingTwoNumbes(){
		result = addition.add(2, 3);

	}
	
	@Then ("I get the result equal to 5")
	public void assertAdditionResult(){
	
		assertEquals(5, result);
	}

}