package org.csstudio.opibuilder.adl2boy.translator;

import junit.framework.Test;
import junit.framework.TestSuite;

public class UiPluginTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(UiPluginTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(RelatedDisplay2ModelUiPluginTest.class);
		suite.addTestSuite(TranslatorUtilsUiPluginTest.class);
		suite.addTestSuite(AbstractADL2ModelUiPluginTest.class);
		suite.addTestSuite(Display2ModelUiPluginTest.class);
		//$JUnit-END$
		return suite;
	}

}
