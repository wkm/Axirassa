
package axirassa.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.jaxen.JaxenException;
import org.junit.After;
import org.junit.Before;

import axirassa.ioc.AxirassaSecurityModule;
import axirassa.ioc.ExternalServicesMockingModule;
import axirassa.ioc.FlowsModule;
import axirassa.ioc.HibernateTestingModule;
import axirassa.ioc.LoggingModule;
import axirassa.ioc.MessagingModule;
import axirassa.ioc.PageTestingModule;
import axirassa.util.ElementUtil;

import com.formos.tapestry.testify.core.TapestryTester;
import com.formos.tapestry.testify.junit4.TapestryTest;
import com.formos.tapestry.xpath.TapestryXPath;

public class TapestryPageTest extends TapestryTest {
	private static final TapestryTester SHARED_TESTER = new TapestryTester("axirassa.webapp", FlowsModule.class,
	        MessagingModule.class, LoggingModule.class, ExternalServicesMockingModule.class,
	        HibernateTestingModule.class, AxirassaSecurityModule.class, PageTestingModule.class);


	public TapestryPageTest() {
		super(SHARED_TESTER);
		setSecurityManager();
	}


	private void setSecurityManager() {
		CustomSecurityManagerFactory factory = SHARED_TESTER.autobuild(CustomSecurityManagerFactory.class);
		SecurityUtils.setSecurityManager(factory.getWebSecurityManager());
	}


	private HibernateCleanupService cleanupService;


	@Before
	public void wipeAndCreate() {
		cleanupService = SHARED_TESTER.autobuild(HibernateCleanupService.class);
		cleanupService.wipeAndCreateDB();
	}


	@After
	public void stopSessions() {
		cleanupService.cleanup();
		cleanupService = null;
	}


	//
	// Test Helpers
	//

	public Document renderComponentTestPage(Class<?> componentClass) {
		return tester.renderPage("test/" + componentClass.getSimpleName().toLowerCase() + "testpage");
	}


	public Element getLabelFor(Document page, String text) throws JaxenException {
		return TapestryXPath.xpath("//*[@for='" + text + "']").selectSingleElement(page);
	}


	public Element getElementById(Document page, String id) throws JaxenException {
		return TapestryXPath.xpath("//*[@id='" + id + "']").selectSingleElement(page);
	}


	/**
	 * Calls {@link Element#getChildMarkup()} but trims leading and trailing
	 * whitespace.
	 */
	public String getElementText(Element element) {
		return element.getChildMarkup().trim();
	}


	public Document clickSubmitByValue(Document page, String value) throws JaxenException {
		return clickSubmitByValue(page, value, Collections.<String, String> emptyMap());
	}


	public Document clickSubmitByValue(Document page, String value, Map<String, String> values) throws JaxenException {
		Element button = TapestryXPath.xpath("//input[@value='" + value + "']").selectSingleElement(page);
		assertNotNull("could not find button with value: '" + value + "'", button);
		return tester.clickSubmit(button, values);
	}


	public void ensureNoErrors(Document page) throws JaxenException {
		// ensure no errors
		List<Element> errorNodes = TapestryXPath.xpath("//*[@class='t-error']").selectElements(page);
		assertEquals("tapestry error elements found: " + errorNodes, 0, errorNodes.size());

		// ensure no stack trace
		List<Element> stacktraceNodes = TapestryXPath.xpath("//*[@class='t-stack-trace']").selectElements(page);
		assertEquals("stack trace elements found: " + stacktraceNodes, 0, stacktraceNodes.size());
	}


	public void ensureErrorOnField(Document page, String id) throws JaxenException {
		Element fieldNode = getElementById(page, id);
		ensureElementHasClass(fieldNode, "t-error");

		// now try and find the error message
		Element previousNode = ElementUtil.getElementBefore(fieldNode);
		ensureElementHasClass(previousNode, "errorMsg");
	}


	public Element getErrorOnField(Document page, String id) throws JaxenException {
		Element fieldNode = getElementById(page, id);
		Element previousNode = ElementUtil.getElementBefore(fieldNode);
		return previousNode;
	}


	public void ensureNoErrorOnField(Document page, String id) throws JaxenException {
		Element fieldNode = getElementById(page, id);
		assertNotNull("could not find element with id: " + id, fieldNode);
		ensureElementDoesntHaveClass(fieldNode, "t-error");
	}


	public List<String> getElementClasses(Element element) {
		String classesString = element.getAttribute("class");
		if (classesString == null)
			return null;

		String[] classes = classesString.split(" +");

		List<String> classesList = new ArrayList<String>();
		Collections.addAll(classesList, classes);
		return classesList;
	}


	public void ensureElementDoesntHaveClass(Element element, String className) {
		List<String> classesList = getElementClasses(element);

		if (classesList == null)
			return;

		for (String individualClass : classesList)
			if (individualClass.equalsIgnoreCase(className))
				fail("Found " + className + " as class for: " + element);

		return;
	}


	public void ensureElementHasClass(Element element, String className) {
		List<String> classesList = getElementClasses(element);
		assertNotNull("no class attribute for element: " + element, classesList);

		for (String individualClass : classesList)
			if (individualClass.equalsIgnoreCase(className))
				return;

		fail("Missing " + className + " from classes: " + classesList);
	}
}
