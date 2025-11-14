package com.elpais.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ImageDownloader {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageDownloader.class);
	private ImageDownloader() {}

	public static Path download(String imageUrl, String folder, String slug) {
		try {
			if (imageUrl == null || imageUrl.isBlank()) return null;
			Path dir = Path.of(folder);
			Files.createDirectories(dir);
			String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
			String fileName = slug.replaceAll("[^a-zA-Z0-9-_]", "_") + "_" + ts + ".jpg";
			Path target = dir.resolve(fileName);
			try (InputStream in = URI.create(imageUrl).toURL().openStream()) {
				Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
			}
			LOGGER.info("Downloaded image from {} to {}", imageUrl, target);
			return target;
		} catch (IOException ex) {
			LOGGER.warn("Failed to download image from {}. Reason: {}", imageUrl, ex.getMessage());
			return null;
		}
	}
}


