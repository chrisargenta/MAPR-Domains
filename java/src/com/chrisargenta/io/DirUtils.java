package com.chrisargenta.io;

import java.io.File;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.chrisargenta.utils.UtilException;

public class DirUtils {
	public static String ensureDir(String dir) throws UtilException{
		File d = new File(dir);
		if (!d.exists()) d.mkdir();
		if (!d.canWrite()){
			throw new UtilException("Unable to write to directory: "+dir);
		}
		return d.getAbsolutePath();
	}
	
	public static String ensureNew(String dir) throws UtilException{
		File d = new File(dir);
		if (d.exists()) {
			throw new UtilException("Directory already exists: "+dir);
		}
		d.mkdir();
		if (!d.canWrite()){
			throw new UtilException("Unable to write to directory: "+dir);
		}
		return d.getAbsolutePath();
	}
	
	// snippit from Javo answer on http://stackoverflow.com/questions/15968883/how-to-zip-a-folder-itself-using-java
	public static void pack(final Path folder, final Path zipFilePath) throws IOException {
	    try (
	            FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
	            ZipOutputStream zos = new ZipOutputStream(fos)
	    ) {
	        Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
	            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	                zos.putNextEntry(new ZipEntry(folder.relativize(file).toString()));
	                Files.copy(file, zos);
	                zos.closeEntry();
	                return FileVisitResult.CONTINUE;
	            }

	            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
	                zos.putNextEntry(new ZipEntry(folder.relativize(dir).toString() + "/"));
	                zos.closeEntry();
	                return FileVisitResult.CONTINUE;
	            }
	        });
	    }
	}
}
