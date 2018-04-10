package edu.iastate.sdmay1809.shared;

import java.io.File;
import java.io.FileFilter;

public class DirectoryFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory();
	}

}
