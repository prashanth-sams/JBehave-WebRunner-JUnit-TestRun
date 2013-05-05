package jbehave.addition;

import java.util.ArrayList;
import java.util.List;

import jbehave.main.Basestories;
import jbehave.addition.steps.Additionsteps;

public class Additionstories extends Basestories {

	@Override
	protected List<Object> createSteps() {
		List<Object> steps = new ArrayList<Object>();
		steps.add(new Additionsteps());
		return steps;
	}

	@Override
	public void run() throws Throwable {
		super.run();
	}
}
