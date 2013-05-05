package jbehave.main;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.model.TableTransformers;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.ConsoleOutput;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.core.steps.ParameterConverters.ExamplesTableConverter;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML_TEMPLATE;
import static org.jbehave.core.reporters.Format.TXT;
import static org.jbehave.core.reporters.Format.XML_TEMPLATE;


public abstract class Basestories extends JUnitStories {

private final CrossReference xref = new CrossReference();

public Basestories() {
configuredEmbedder().embedderControls()
.doGenerateViewAfterStories(true)
.doIgnoreFailureInStories(false)
.doIgnoreFailureInView(true)
.doVerboseFailures(true)
.useThreads(2).useStoryTimeoutInSecs(60);
//configuredEmbedder().useEmbedderControls(new PropertyBasedEmbedderControls());
}

@Override
public Configuration configuration() {
Class<? extends Embeddable> embeddableClass = this.getClass();
Properties viewResources = new Properties();
viewResources.put("decorateNonHtml", "true");
//viewResources.put("reports", "ftl/jbehave-reports-with-totals.ftl");
//Start from default ParameterConverters instance
ParameterConverters parameterConverters = new ParameterConverters();
LocalizedKeywords keywords = new LocalizedKeywords();
//factory to allow parameter conversion and loading from external resources (used by StoryParser too)
ExamplesTableFactory examplesTableFactory = new ExamplesTableFactory(new LocalizedKeywords(), new LoadFromClasspath(embeddableClass), parameterConverters, new TableTransformers());
//add custom converters
parameterConverters.addConverters(new DateConverter(new SimpleDateFormat("yyyy-MM-dd")),
new ExamplesTableConverter(examplesTableFactory));
return new MostUsefulConfiguration()
.useStoryLoader(new LoadFromClasspath(embeddableClass))
.useStoryParser(new RegexStoryParser(examplesTableFactory))
.useKeywords(keywords)
.useDefaultStoryReporter(new ConsoleOutput(keywords))
.useStoryReporterBuilder(new StoryReporterBuilder()
.withCodeLocation(CodeLocations.codeLocationFromClass(embeddableClass))
.withDefaultFormats()
.withKeywords(keywords)
.withViewResources(viewResources)
.withFormats(CONSOLE, TXT, HTML_TEMPLATE, XML_TEMPLATE)
.withFailureTrace(true)
.withFailureTraceCompression(true)
.withCrossReference(xref))
.useParameterConverters(parameterConverters)
//use '%' instead of '$' to identify parameters
.useStepPatternParser(new RegexPrefixCapturingPatternParser(
"%"))
.useStepMonitor(xref.getStepMonitor());
}

//abstract protected List<Object> createSteps();

@Override
public InjectableStepsFactory stepsFactory() {
return new InstanceStepsFactory(configuration(), createSteps());
}

abstract protected List<Object> createSteps();

@Override
protected List<String> storyPaths() {
return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), "**/*.story", "**/failing_before*.story");

}

}