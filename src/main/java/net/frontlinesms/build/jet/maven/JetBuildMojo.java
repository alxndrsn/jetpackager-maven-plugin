package net.frontlinesms.build.jet.maven;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
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
	private static final String SPLASH_IMAGE_PATH = "resources/splash.jpg";
	private static final String ICON_PATH = "resources/icon.ico";
	private static final File RESOURCE_SPLASH_IMAGE = new File(COMPILE_WORKINGDIRECTORY, SPLASH_IMAGE_PATH);
	private static final File RESOURCE_PROGRAM_ICON = new File(COMPILE_WORKINGDIRECTORY, ICON_PATH);
	private static final String JPN_FILE_PATH = "temp/frontlinesms.jpn";

	private static final File PACK_WORKINGDIRECTORY = new File("target/jet-packager/pack");

//> INSTANCE VARIABLES
    /**
     * The Maven Project Object
     * @parameter default-value="${project}"
     * @readonly
     */
    private MavenProject project;
    /** The splash image used when the compiled program is starting up.
     * @parameter
     * @required */
    private File splashImage;
    /** The program icon used for the .exe file of the compiled program.
     * @parameter
     * @required */
    private File programIcon;
    /** The main java class, called to run the program.
     * @parameter
     * @required */
    private String javaMainClass;
    /** The vendor of the program.
     * @parameter
     * @required */
    private String programVendor;
    /** The year(s) copyright is asserted for.  This will default to the current year.  Couldn't really
     * do that with the standard default-value tag.
     * @parameter */
    private String copyrightYear;
    /** The name of the .exe file that will be generated.  This should not include the file extension.
     * @parameter
     * @required */
    private String programExecutableName;
    /** The contents of the installer package.
     * @parameter
     * @required */
	private List<FileSet> packageContents;
    /** Name of the directory in the Windows Start Menu to install shortcuts to.
     * @parameter
     * @required */
	private String startMenuProgramFolderRoot;
    /** Name of the subdirectory of Program Files to install the application to.
     * @parameter
     * @required */
	private String programFilesHome;

    /** Set to false if you do not want the compilation or associated initialisation to be performed
     * @parameter default-value=true */
	private boolean doCompile;
    /** Set to false if you do not want the packing or associated initialisation to be performed
     * @parameter default-value=true */
	private boolean doPack;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Starting JetBuild Mojo...");

		if(doCompile) {
			getLog().info("Initialising compilation resources...");
			initCompileResources();
			getLog().info("Compile resources initialised.");
			getLog().info("Calling Jet compiler...");
			doCompile();
			getLog().info("...compilation complete.");
		} else getLog().info("doCompile = false.  Not compiling.");

		if(doPack) {
			getLog().info("Initialising pack resources...");
			initPackResources();
			getLog().info("Pack resources initialised.");
			getLog().info("Calling Jet packer...");
			doPack();
			getLog().info("...packing complete.");
		} else getLog().info("doPack = false.  Not packing.");

		getLog().info("...JetBuild complete.");
	}

//> COMPILE METHODS
	private void doCompile() throws JetCompileException {
		JetCompiler compiler = new JetCompiler(COMPILE_WORKINGDIRECTORY);
		compiler.configureDefaults();
		try {
			int exitCode = compiler.doCompile(getCompileProfile());
			if(exitCode != 0) throw new JetCompileException("Compilation failed with status: " + exitCode);
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
			FileUtils.copyFile(this.splashImage, RESOURCE_SPLASH_IMAGE);
			FileUtils.copyFile(this.programIcon, RESOURCE_PROGRAM_ICON);
		} catch(IOException ex) {
			throw new JetCompileException("Problem copying compile resource.", ex);
		}
	}
	
	private JetCompileProfile getCompileProfile() {
		return new JetCompileProfile(COMPILE_WORKINGDIRECTORY,
				JPN_FILE_PATH, getJavaMainClass(),
				getOutputName(), SPLASH_IMAGE_PATH, ICON_PATH,
				getVersionInfoCompanyName(), getVersionInfoFileDescription(),
				getVersionInfoCopyrightYear(), getVersionInfoCopyrightOwner(),
				getVersionInfoProductName(), getVersionInfoNumber());
	}

//> COMPILE VARIABLE ACCESSORS
	private String getJavaMainClass() { return javaMainClass; }
	private String getOutputName() { return this.programExecutableName; }
	private String getVersionInfoCompanyName() { return this.programVendor; }
	private String getVersionInfoFileDescription() { return this.programExecutableName; }
	private String getVersionInfoCopyrightYear() { 
		if(this.copyrightYear != null) {
			return this.copyrightYear;
		} else {
			return Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		}
	}
	private String getVersionInfoCopyrightOwner() { return this.programVendor; }
	private String getVersionInfoProductName() { return this.programExecutableName; }
	private String getVersionInfoNumber() { return getNormalisedVersionNumber(); }

//> PACKING METHODS
	private void doPack() throws JetPackException {
		JetPacker packer = new JetPacker(PACK_WORKINGDIRECTORY);
		packer.configureDefaults();
		try {
			int exitCode = packer.doPack(getPackProfile());
			if(exitCode != 0) throw new JetPackException("Pack failed with status: " + exitCode);
		} catch (IOException ex) { throw new JetPackException("Exception doing jet pack.", ex); }
	}

	private void initPackResources() throws JetPackException {
		// Copy compiled .exe to the working directory
		try {
			FileUtils.copyFile(
					new File(COMPILE_WORKINGDIRECTORY, getExecutableName() + ".exe"),
					new File(PACK_WORKINGDIRECTORY, getExecutableName() + ".exe"));
		} catch (IOException ex) { throw new JetPackException("Unable to copy executable file to pack working dir.", ex); }
		
		// Copy pack classpath to the working directory
		File cpDir = new File(PACK_WORKINGDIRECTORY, "classpath");
		if(!cpDir.mkdirs()) throw new JetPackException("Unable to create classpath directory at: " + cpDir.getAbsolutePath());
		try {
			copyArtifacts(cpDir, Artifact.SCOPE_COMPILE, Artifact.SCOPE_RUNTIME);
		} catch (IOException ex) { throw new JetPackException("Unable to copy artifacts to classpath directory.", ex); }
		
		// Copy pack resources to the working directory
		File resDir = new File(PACK_WORKINGDIRECTORY, "packageContent");
		for(File packContentFileOrFolder : getPackageContents()) {
			if(packContentFileOrFolder.isFile()) {
				try {
					// Copy files, maintaining the relative paths
					FileUtils.copyFile(packContentFileOrFolder, new File(resDir, packContentFileOrFolder.getPath()));
				} catch (IOException ex) { throw new JetPackException("Error copying packageContent file: " + packContentFileOrFolder.getAbsolutePath(), ex); }
			} else if(packContentFileOrFolder.isDirectory()) {
				try {
					FileUtils.copyDirectoryToDirectory(packContentFileOrFolder, new File(resDir, packContentFileOrFolder.getPath()));
				} catch (IOException ex) { throw new JetPackException("Error copying directory file: " + packContentFileOrFolder.getAbsolutePath(), ex); }
			} else throw new RuntimeException("Not sure what to do with file: " + packContentFileOrFolder.getAbsolutePath());
		}
	}

	private JetPackProfile getPackProfile() {
		return new JetPackProfile(PACK_WORKINGDIRECTORY, getProductName(),
				getProductVersion(), getProductVersionStandardised(),
				getProductDescription(), getProductVendor(),
				getExecutableName(), getStartMenuProgramFolderRoot(),
				getProgramFilesHome());
	}
	
	private List<File> getPackageContents() throws JetPackException { 
		try {
			return FileSet.toFileList(this.packageContents);
		} catch (IOException ex) {
			throw new JetPackException("Exception thrown getting package contents.", ex);
		}
	}
	private String getProductName() { return this.programExecutableName; }
	private String getProductVersion() { return this.project.getVersion(); }
	private String getProductVersionStandardised() { return this.getNormalisedVersionNumber(); }
	private String getProductDescription() { return this.programExecutableName; }
	private String getProductVendor() { return this.programVendor; }
	private String getExecutableName() { return this.programExecutableName; }
	private String getStartMenuProgramFolderRoot() { return this.startMenuProgramFolderRoot; }
	private String getProgramFilesHome() { return this.programFilesHome; }

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

	/**
	 * Convert the Maven Project's version number to one that is acceptable to Jet packer.
	 * This should be of the form W.X.Y.Z.
	 * Please note any other restrictions as and when they are discovered.
	 * @return
	 */
	String getNormalisedVersionNumber() {
		String version = this.project.getVersion();
		// remove all non-digits and points
		version = version.replaceAll("[^\\d\\.]", "");
		// remove all excess zeros from the start
		while(version.matches("^0\\d+\\..*")) {
			version = version.substring(1);
		}
		// remove all excess zeros from after dot characters
		version = version.replaceAll("\\.0+(\\d+)", "\\.$1");
		while(count(version, '.') < 3) version += ".0";
		if(version.length() > 0) {
				return version;
		} else throw new IllegalArgumentException("Unable to normalise version: " + this.project.getVersion());
	}
	
	private static int count(String haystack, char needle) {
		int count = 0;
		for(char c : haystack.toCharArray()) if(c == needle) ++count;
		return count;
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