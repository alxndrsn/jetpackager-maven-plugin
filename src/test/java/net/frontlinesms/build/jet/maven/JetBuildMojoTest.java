/**
 * 
 */
package net.frontlinesms.build.jet.maven;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.maven.project.MavenProject;

import junit.framework.TestCase;

import static org.mockito.Mockito.*;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class JetBuildMojoTest extends TestCase {
	public void testGetNormalisedVersionNumber() {
		testGetNormalisedVersionNumber("1", "1");
		testGetNormalisedVersionNumber("1.2", "1.2");
		testGetNormalisedVersionNumber("1.2.3", "1.2.3");
		testGetNormalisedVersionNumber("1.2.3.4", "1.2.3.4");
		testGetNormalisedVersionNumber("11.12.13.14", "11.12.13.14"); // i GUESS jet will be happy with this.  if not, please rewrite the test
		testGetNormalisedVersionNumber("01.2.3.4", "1.2.3.4");
		testGetNormalisedVersionNumber("1.00.3", "1.0.3");
		testGetNormalisedVersionNumber("1.2.03", "1.2.3");
		testGetNormalisedVersionNumber("1.0.2", "1.0.2");
		testGetNormalisedVersionNumber("1.20.03-beta", "1.20.3");
		testGetNormalisedVersionNumber("0.0.0.0", "0.0.0.0");
		testGetNormalisedVersionNumber("00000.00.0000.00", "0.0.0.0");
	}
	
	private void testGetNormalisedVersionNumber(String mavenVersion, String expectedNormalisedVersion) {
		JetBuildMojo mojo = new JetBuildMojo();
		MavenProject mockProject = mock(MavenProject.class);
		when(mockProject.getVersion()).thenReturn(mavenVersion);
		set(mojo, "project", mockProject);
		String actualNormalisedVersionNumber = mojo.getNormalisedVersionNumber();
		assertEquals("Failed to properly normalise '" + mavenVersion + "' - got '" + actualNormalisedVersionNumber + "'", expectedNormalisedVersion, actualNormalisedVersionNumber);
	}
	
	private void set(Object obj, String propertyName, Object value) {
		Field field;
		try {
			field = obj.getClass().getDeclaredField(propertyName);
			assert((field.getModifiers() & Modifier.STATIC) == 0) : "Should not use this for setting static field: " + propertyName;
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
