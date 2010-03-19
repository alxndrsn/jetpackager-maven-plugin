package net.frontlinesms.build.jet.maven;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.frontlinesms.build.jet.compile.JetCompileProfile;
import net.frontlinesms.build.jet.compile.JetCompiler;
import net.frontlinesms.build.jet.pack.JetPackProfile;
import net.frontlinesms.build.jet.pack.JetPacker;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
/**
 * 
 */
import org.apache.maven.project.MavenProject;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 * @goal jetbuild
 */
public class JetBuildMojo extends AbstractMojo {
	private static final File COMPILE_WORKINGDIRECTORY = new File("target/jet-packager/compile");
	private static final File COMPILE_RESOURCEDIRECTORY = new File(COMPILE_WORKINGDIRECTORY, "resources");
	private static final File RESOURCE_SPLASH_IMAGE = new File(COMPILE_RESOURCEDIRECTORY, "splash.jpg");
	private static final File RESOURCE_PROGRAM_ICON = new File(COMPILE_RESOURCEDIRECTORY, "icon.ico");

	private static final File PACK_WORKINGDIRECTORY = new File("target/jet-packager/pack");

//> INSTANCE VARIABLES
    /**
     * The Maven Project Object
     * @parameter default-value="${project}"
     * @readonly
     */
    private MavenProject project;
    /** The splash image used when the compiled program is starting up.
     * @parameter */
    private File splashImage;
    /** The program icon used for the .exe file of the compiled program.
     * @parameter */
    private File programIcon;
	
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

	private void initCompileResources() throws JetCompileException {
		// Copy compile classpath to the working directory
		File cpDir = new File(COMPILE_WORKINGDIRECTORY, "classpath");
		if(!cpDir.mkdirs()) throw new JetCompileException("Unable to create classpath directory at: " + cpDir.getAbsolutePath());
		try {
			copyArtifacts(cpDir, Artifact.SCOPE_COMPILE, Artifact.SCOPE_PROVIDED);
		} catch (IOException ex) { throw new JetCompileException("Unable to copy artifacts to classpath directory.", ex); }

		// Copy compile resources to the working directory
		try {
			if(this.splashImage == null) throw new JetCompileException("splashImage not set.");
			FileUtils.copyFile(this.splashImage, RESOURCE_SPLASH_IMAGE);
			if(this.programIcon == null) throw new JetCompileException("programIcon not set.");
			FileUtils.copyFile(this.programIcon, RESOURCE_PROGRAM_ICON);
		} catch(IOException ex) {
			throw new JetCompileException("Problem copying compile resource.", ex);
		}
	}
	
	private JetCompileProfile getCompileProfile() {
		return new JetCompileProfile(COMPILE_WORKINGDIRECTORY,
				getJpnPath(), getJavaMainClass(),
				getOutputName(), getSplashImagePath(),
				getVersionInfoCompanyName(), getVersionInfoFileDescription(),
				getVersionInfoCopyrightYear(), getVersionInfoCopyrightOwner(),
				getVersionInfoProductName(), getVersionInfoNumber());
	}

	private String getJpnPath() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getJavaMainClass() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getOutputName() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getSplashImagePath() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getVersionInfoCompanyName() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getVersionInfoFileDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getVersionInfoCopyrightYear() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getVersionInfoCopyrightOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getVersionInfoProductName() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getVersionInfoNumber() {
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

	private void initPackResources() throws JetPackException {
		// TODO Copy compiled .exe to the working directory
//		FileUtils.copyFile(new File(COMPILE_WORKINGDIRECTORY, ), new File(PACK_WORKINGDIRECTORY, ))
		
		// Copy pack classpath to the working directory
		File cpDir = new File(PACK_WORKINGDIRECTORY, "classpath");
		if(!cpDir.mkdirs()) throw new JetPackException("Unable to create classpath directory at: " + cpDir.getAbsolutePath());
		try {
			copyArtifacts(cpDir, Artifact.SCOPE_COMPILE, Artifact.SCOPE_RUNTIME);
		} catch (IOException ex) { throw new JetPackException("Unable to copy artifacts to classpath directory.", ex); }
		
		// TODO Copy pack resources to the working directory
		File resDir = new File(PACK_WORKINGDIRECTORY, "packageContent");
	}

	private JetPackProfile getPackProfile() {
		return new JetPackProfile(PACK_WORKINGDIRECTORY, getProductName(),
				getProductVersion(), getProductVersionStandardised(),
				getProductDescription(), getProductVendor(),
				getExecutableName(), getStartMenuProgramFolderRoot(),
				getProgramFilesHome());
	}
	
	private String getProductName() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getProductVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getProductVersionStandardised() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getProductDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getProductVendor() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getExecutableName() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getStartMenuProgramFolderRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getProgramFilesHome() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	private void copyArtifacts(File targetDirectory, String...scopes) throws IOException {
		getLog().debug("Copying artifacts to directory: " + targetDirectory.getPath() + " with scopes: " + Arrays.deepToString(scopes));
		List<String> scopesArray = Arrays.asList(scopes);
		Set<Artifact> artifacts = project.getArtifacts();
		for(Artifact artifact : artifacts) {
			if(!scopesArray.contains(artifact.getScope())) {
				getLog().debug("Artifact out of scope (" + artifact.getScope() + "): " + artifact.toString());
			} else {
				getLog().debug("Copying artifact: " + artifact.toString());
				File artifactFile = artifact.getFile();
				FileUtils.copyFile(artifactFile, new File(targetDirectory, artifactFile.getName()));
			}
		}
	}
}

@SuppressWarnings("serial")
abstract class JetBuildException extends MojoExecutionException {
	public JetBuildException(String message) {
		super(message);
	}
	public JetBuildException(String message, Throwable cause) {
		super(message, cause);
	}
}

@SuppressWarnings("serial")
class JetCompileException extends JetBuildException {
	public JetCompileException(String message) {
		super(message);
	}
	public JetCompileException(String message, Throwable cause) {
		super(message, cause);
	}
}

@SuppressWarnings("serial")
class JetPackException extends JetBuildException {
	public JetPackException(String message) {
		super(message);
	}
	public JetPackException(String message, Throwable cause) {
		super(message, cause);
	}
}