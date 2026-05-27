package com.example;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME;

/**
 * JUnit 5 Platform Suite that runs all Cucumber feature files.
 * Maven Surefire picks this class up via mvn test and executes the scenarios.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.example")
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty, summary"
)
public class RunCucumberTest {
}
