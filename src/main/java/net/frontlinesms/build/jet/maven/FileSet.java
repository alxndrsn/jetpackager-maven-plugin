package net.frontlinesms.build.jet.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileSet extends org.apache.maven.model.FileSet {
	
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
    	String directory = this.getDirectory();
    	String path = file.getPath();
		if(directory == null || directory.length() == 0) {
    		return path;
    	} else {
	    	if(!directory.endsWith(File.separator)) {
	    		directory += File.separatorChar;
	    	}
	    	assert(path.startsWith(directory)) : "path '" + path + "' is not relative to this.getDirectory(): '" + directory + "'";
	    	return path.substring(directory.length());
    	}
    }
    
//> STATIC METHODS
	public static List<File> toFileList(List<FileSet> fileSets) throws IOException {
    	ArrayList<File> files = new ArrayList<File>();
    	for(FileSet fileSet : fileSets) {
    		files.addAll(fileSet.toFileList());
    	}
    	return files;
    }

    private static String toString(List<String> strings) {
            StringBuilder sb = new StringBuilder();
            for (String string : strings) {
                    if (sb.length() > 0)
                            sb.append(", ");
                    sb.append(string);
            }
            return sb.toString();
    }
}
