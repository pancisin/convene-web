package com.pancisin.bookster.components.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StorageServiceImpl implements StorageService {

	@Value("${storage.path}")
	private String storagePath;

	public Long storeBinary(String binary, String filename) {
		String relative_path = storagePath + filename + ".jpg";

		File file = new File(relative_path);
		file.getParentFile().mkdirs();

		try (FileOutputStream imageOutFile = new FileOutputStream(file)) {
			String imageData = binary.replaceFirst("^data:image/[^;]*;base64,?", "");
			byte[] imageByteArray = Base64.getDecoder().decode(imageData);
			imageOutFile.write(imageByteArray);
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}

		return file.length();
	}

	public String storeText(String text, String filename, String extension) {
		String relative_path = storagePath + filename + "." + extension;

		File file = new File(relative_path);
		file.getParentFile().mkdirs();

		try (FileOutputStream textOutFile = new FileOutputStream(file)) {
			textOutFile.write(text.getBytes());
		} catch (FileNotFoundException e) {
			System.out.println("Text file not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}

		return relative_path;
	}

	public boolean isBinary(String context) {
		return Pattern.compile("^data:image/[^;]*;base64,?").matcher(context).find();
	}

	@Override
	public void init() {
		try {
			Files.createDirectory(Paths.get(storagePath));
		} catch (IOException e) {
		}
	}

	@Override
	public void store(MultipartFile file) {

	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(Paths.get(storagePath), 1).filter(path -> !path.equals(Paths.get(storagePath)))
					.map(path -> Paths.get(storagePath).relativize(path));
		} catch (IOException e) {
			return null;
//			throw new StorageException("Failed to read stored files", e);
		}
	}

	@Override
	public Path load(String filename) {
		return Paths.get(storagePath).resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				return null;
				// throw new Exception("Could not read file: " + filename);
			}
		} catch (MalformedURLException e) {
			return null;
			// throw new Exception("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(storagePath).toFile());
	}
}
