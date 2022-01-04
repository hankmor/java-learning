package com.belonk.io.nio;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Predicate;

/**
 * Created by sun on 2022/1/4.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class TextFileRenameUtil {
	//~ Static fields/constants/initializer

	private static final TextFileRenameUtil INSTANCE = new TextFileRenameUtil();

	//~ Instance fields


	//~ Constructors

	private TextFileRenameUtil() {
	}

	//~ Methods

	public static TextFileRenameUtil getInstance() {
		return INSTANCE;
	}

	public void walkFileTree(String path, FilenameFilter filenameFilter, FileRenameStrategy renameStrategy) throws IOException {
		Files.walkFileTree(Paths.get(path), new TextFileVisitor(filenameFilter, renameStrategy));
	}

	public interface FileRenameStrategy {
		void rename(File file);
	}

	public abstract static class LineFileRenameStrategy implements FileRenameStrategy {
		private final Predicate<String> linePredicate;

		public LineFileRenameStrategy(Predicate<String> linePredicate) {
			this.linePredicate = linePredicate;
		}

		abstract void rename(File file, String line);

		@Override
		public void rename(File file) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line;
				while ((line = reader.readLine()) != null) {
					if (this.linePredicate.test(line)) {
						rename(file, line);
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static final class TextFileVisitor extends SimpleFileVisitor<Path> {
		private final FilenameFilter filenameFilter;
		private final FileRenameStrategy renameStrategy;

		private TextFileVisitor(FilenameFilter filenameFilter, FileRenameStrategy renameStrategy) {
			this.filenameFilter = filenameFilter;
			this.renameStrategy = renameStrategy;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			File f = file.toFile();
			if (filenameFilter.accept(f, f.getName())) {
				renameStrategy.rename(f);
			}
			return super.visitFile(file, attrs);
		}
	}

	public static void main(String[] args) throws IOException {
		String path = "/Users/sam/workspace/mine/samblog/source/_posts";
		TextFileRenameUtil renameUtil = TextFileRenameUtil.getInstance();
		renameUtil.walkFileTree(path, (dir, name) -> name.endsWith(".adoc"), new LineFileRenameStrategy((line) -> line.startsWith("date:")) {
			@Override
			void rename(File file, String line) {
				String dt = line.substring(5).trim().split(" ")[0];
				dt = dt.replaceAll("-", "").replaceAll("/", "");
				String srcName = file.getName();
				if (srcName.startsWith(dt)) {
					return;
				}
				File destFile = new File(file.getParentFile() + "/" + (dt + "-" + srcName));
				System.out.println("信息：" + file.getAbsolutePath() + " -> " + destFile.getName());
				if (!file.renameTo(destFile)) {
					System.err.println("重命名失败：" + srcName);
				}
			}
		});
	}
}