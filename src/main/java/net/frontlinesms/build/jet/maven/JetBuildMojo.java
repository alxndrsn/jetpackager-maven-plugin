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
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Starting JetBuild Mojo...");
		
		getLog().info("Calling Jet compiler...");
		doCompile();
		getLog().info("...compilation complete.");
		
		getLog().info("Calling Jet packer...");
		doPack();
		getLog().info("...packing complete.");

		getLog().info("...JetBuild complete.");
	}

//> COMPILE METHODS
	private void doCompile() throws JetCompileException {
		JetCompiler compiler = new JetCompiler(new File("target/jet-packager/compile"));
		compiler.configureDefaults();
		try {
			compiler.doCompile(getCompileProfile());
		} catch (IOException ex) { throw new JetCompileException("Exception doing jet compile.", ex); }
	}

	private JetCompileProfile getCompileProfile() {
		// TODO Auto-generated method stub
		return null;
	}

//> PACKING METHODS
	private void doPack() throws JetPackException {
		JetPacker packer = new JetPacker(new File("target/jet-packager/compile"));
		packer.configureDefaults();
		try {
			packer.doPack(getPackProfile());
		} catch (IOException ex) { throw new JetPackException("Exception doing jet pack.", ex); }
	}

	private JetPackProfile getPackProfile() {
		// TODO Auto-generated method stub
		return null;
	}
}

abstract class JetBuildException extends MojoExecutionException {
	public JetBuildException(String message, Throwable cause) {
		super(message, cause);
	}
}

class JetCompileException extends JetBuildException {
	public JetCompileException(String message, Throwable cause) {
		super(message, cause);
	}
}

class JetPackException extends JetBuildException {
	public JetPackException(String message, Throwable cause) {
		super(message, cause);
	}
}