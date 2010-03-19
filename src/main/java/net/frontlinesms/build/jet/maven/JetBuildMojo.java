package net.frontlinesms.build.jet.maven;

import java.io.File;
import java.io.IOException;

import net.frontlinesms.build.jet.compile.JetCompileProfile;
import net.frontlinesms.build.jet.compile.JetCompiler;
import net.frontlinesms.build.jet.pack.JetPackProfile;
import net.frontlinesms.build.jet.pack.JetPacker;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
/**
 * 
 */

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 * @goal jetbuild
 */
public class JetBuildMojo extends AbstractMojo {
	private static final File COMPILE_WORKINGDIRECTORY = new File("target/jet-packager/compile");
	private static final File PACK_WORKINGDIRECTORY = new File("target/jet-packager/pack");
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Starting JetBuild Mojo...");


		getLog().info("Initialising compilation resources...");
		initCompileResources();
		getLog().info("Compile resources initialised.");
		getLog().info("Calling Jet compiler...");
		doCompile();
		getLog().info("...compilation complete.");

		getLog().info("Initialising pack resources...");
		initPackResources();
		getLog().info("Pack resources initialised.");
		getLog().info("Calling Jet packer...");
		doPack();
		getLog().info("...packing complete.");

		getLog().info("...JetBuild complete.");
	}

//> COMPILE METHODS
	private void doCompile() throws JetCompileException {
		JetCompiler compiler = new JetCompiler(COMPILE_WORKINGDIRECTORY);
		compiler.configureDefaults();
		try {
			compiler.doCompile(getCompileProfile());
		} catch (IOException ex) { throw new JetCompileException("Exception doing jet compile.", ex); }
	}

	private void initCompileResources() {
		// TODO Copy compile classpath to the working directory
		File cpDir = new File(COMPILE_WORKINGDIRECTORY, "classpath");

		// TODO Copy compile resources to the working directory
		File resDir = new File(PACK_WORKINGDIRECTORY, "resources");
	}
	
	private JetCompileProfile getCompileProfile() {
		// TODO Auto-generated method stub
		return null;
	}

//> PACKING METHODS
	private void doPack() throws JetPackException {
		JetPacker packer = new JetPacker(PACK_WORKINGDIRECTORY);
		packer.configureDefaults();
		try {
			packer.doPack(getPackProfile());
		} catch (IOException ex) { throw new JetPackException("Exception doing jet pack.", ex); }
	}

	private void initPackResources() {
		// TODO Copy compiled .exe to the working directory
		
		// TODO Copy pack classpath to the working directory
		File cpDir = new File(PACK_WORKINGDIRECTORY, "classpath");
		
		// TODO Copy pack resources to the working directory
		File resDir = new File(PACK_WORKINGDIRECTORY, "packageContent");
	}

	private JetPackProfile getPackProfile() {
		// TODO Auto-generated method stub
		return null;
	}
}

@SuppressWarnings("serial")
abstract class JetBuildException extends MojoExecutionException {
	public JetBuildException(String message, Throwable cause) {
		super(message, cause);
	}
}

@SuppressWarnings("serial")
class JetCompileException extends JetBuildException {
	public JetCompileException(String message, Throwable cause) {
		super(message, cause);
	}
}

@SuppressWarnings("serial")
class JetPackException extends JetBuildException {
	public JetPackException(String message, Throwable cause) {
		super(message, cause);
	}
}