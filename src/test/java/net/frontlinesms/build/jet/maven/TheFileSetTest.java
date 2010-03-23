/**
 * 
 */
package net.frontlinesms.build.jet.maven;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

/**
 * Unit tests for {@link FileSet}.
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class TheFileSetTest extends TestCase {
	/** Test {@link FileSet#toFileList()} 
	 * @throws IOException */
	public void testToFileList() throws IOException {
		FileSet fs = new FileSet();
		fs.setDirectory("c:/");
		List<File> files = fs.toFileList();
		for(File f : files) {
			System.out.println("> " + f.getPath());
		}
	}
	
	/** Test {@link FileSet#getRelativePath(File)()} 
	 * @throws IOException */
	public void testGetRelativePath() throws IOException {
		testGetRelativePath(null, "a/b.def", "a/b.def");
		testGetRelativePath("", "a/b.def", "a/b.def");
		testGetRelativePath("a/b/c", "a/b/c/d/e.xyz", "d/e.xyz");
		
		testGetRelativePath(null, "a\\b.def", "a/b.def");
		testGetRelativePath("", "a\\b.def", "a/b.def");
		testGetRelativePath("a\\b\\c", "a\\b\\c\\d\\e.xyz", "d/e.xyz");
		
		testGetRelativePath("src/assemble/resources/package", "src\\assemble\\resources\\package\\help\\contactmanager.htm", "help/contactmanager.htm");
		testGetRelativePath("src/assemble/resources/package/", "src\\assemble\\resources\\package\\help\\contactmanager.htm", "help/contactmanager.htm");

		testGetRelativePath("src\\assemble\\resources\\package", "src/assemble/resources/package/help/contactmanager.htm", "help/contactmanager.htm");
		testGetRelativePath("src\\assemble\\resources\\package\\", "src/assemble/resources/package/help/contactmanager.htm", "help/contactmanager.htm");
	}
	
	private void testGetRelativePath(String filesetDirectory, String originalPath, String expectedRelativePath) {
		FileSet fs = new FileSet();
		fs.setDirectory(filesetDirectory);
		
		String actualRelativePath = fs.getRelativePath(new File(originalPath));
		
		assertEquals(expectedRelativePath, actualRelativePath);
	}
	
	public static void main(String[] args) {
		System.out.println("File sep: " + File.separator);
	}
}
