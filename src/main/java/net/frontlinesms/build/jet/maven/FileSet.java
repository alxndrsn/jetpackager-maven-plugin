package net.frontlinesms.build.jet.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileSet extends org.apache.maven.model.FileSet {

	private static final char NORMAL_FILE_SEPERATOR = '/';
	private static final String NORMAL_FILE_SEPERATOR_STRING = "" + NORMAL_FILE_SEPERATOR;

	//> INSTANCE METHODS
    @SuppressWarnings("unchecked")
	public List<File> toFileList() throws IOException {
        File directory = new File(this.getDirectory());
        String includes = toString(this.getIncludes());
        String excludes = toString(this.getExcludes());
        return org.codehaus.plexus.util.FileUtils.getFiles(directory, includes, excludes);
    }
    
    /** This should be called on a file returned by {@link #toFileList()} to get its path
     * relative to {@link #getDirectory()}
     * @return path of the file relative to {@link #getDirectory()}
     */
    public String getRelativePath(File file) {
    	String directory = normaliseFilePath(this.getDirectory());
    	String path = normaliseFilePath(file.getPath());
		if(directory == null || directory.length() == 0) {
    		return path;
    	} else {
	    	if(!directory.endsWith(NORMAL_FILE_SEPERATOR_STRING)) {
	    		directory += NORMAL_FILE_SEPERATOR;
	    	}
	    	assert(path.startsWith(directory)) : "path '" + path + "' is not relative to this.getDirectory(): '" + directory + "'";
	    	return path.substring(directory.length());
    	}
    }

//> STATIC METHODS
    private static String toString(List<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
                if (sb.length() > 0)
                        sb.append(", ");
                sb.append(string);
        }
        return sb.toString();
    }
    
    /** @return the path with all instances of the Windows file separator ('\\') replaced with
     * the unix one ('/') */
    private static String normaliseFilePath(String path) {
		if(path != null) {
			path = path.replace('\\', NORMAL_FILE_SEPERATOR);
		}
		return path;
	}
}
