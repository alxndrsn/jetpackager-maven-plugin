package net.frontlinesms.build.jet.maven;

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

	private void doPack() {
		// TODO Auto-generated method stub
		
	}

	private void doCompile() {
		// TODO Auto-generated method stub
		
	}
}
