package zemberek.core.io;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import zemberek.core.logging.Log;

public class IOUtil {

  public static DataInputStream getDataInputStream(Path path) throws IOException {
    return new DataInputStream(new BufferedInputStream(Files.newInputStream(path)));
  }

  public static DataInputStream getDataInputStream(InputStream is) throws IOException {
    return new DataInputStream(new BufferedInputStream(is));
  }

  public static DataInputStream getDataInputStream(String resource) throws IOException {
    return new DataInputStream(new BufferedInputStream(IOUtil.class.getResourceAsStream(resource)));
  }


  public static DataOutputStream getDataOutputStream(Path path) throws IOException {
    return new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path)));
  }

  public static BufferedOutputStream geBufferedOutputStream(Path path) throws IOException {
    return new BufferedOutputStream(Files.newOutputStream(path));
  }

  public static DataInputStream getDataInputStream(Path path, int bufferSize) throws IOException {
    if (bufferSize <= 0) {
      throw new IllegalArgumentException("Buffer size must be positive. But it is :" + bufferSize);
    }
    return new DataInputStream(new BufferedInputStream(Files.newInputStream(path), bufferSize));
  }

  public static DataOutputStream getDataOutputStream(Path path, int bufferSize) throws IOException {
    if (bufferSize <= 0) {
      throw new IllegalArgumentException("Buffer size must be positive. But it is :" + bufferSize);
    }
    return new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path), bufferSize));
  }

  public static int readIntLe(DataInputStream dis) throws IOException {
    return Integer.reverseBytes(dis.readInt());
  }

  public static short readShortLe(DataInputStream dis) throws IOException {
    return Short.reverseBytes(dis.readShort());
  }

  public static void writeShortLe(DataOutputStream dos, short value) throws IOException {
    dos.writeShort(Short.reverseBytes(value));
  }

  public static void writeIntLe(DataOutputStream dos, int value) throws IOException {
    dos.writeInt(Integer.reverseBytes(value));
  }

  public static void checkFileArgument(Path path) {
    File f = path.toFile();
    if (!f.exists()) {
      throw new IllegalArgumentException("File does not exist = " + f.getAbsolutePath());
    }
    if (f.isDirectory()) {
      throw new IllegalArgumentException(
          "A file is expected. But path is a directory = " + f.getAbsolutePath());
    }
  }

  public static void checkDirectoryArgument(Path path) {
    File f = path.toFile();
    if (!f.exists()) {
      throw new IllegalArgumentException("Directory does not exist = " + f.getAbsolutePath());
    }
    if (!f.isDirectory()) {
      throw new IllegalArgumentException(
          "A directory is expected. But path is a file = " + f.getAbsolutePath());
    }
  }

  public static void deleteTempDir(Path tempDir) throws IOException {
    String tmpRoot = System.getProperty("java.io.tmpdir");
    if (!tempDir.toFile().getAbsolutePath().startsWith(tmpRoot)) {
      Log.info("Only directories within temporary system dir [%s] are allowed to be deleted recursively. But : %s",
          tmpRoot,
          tempDir.toFile().getAbsolutePath());
      return;
    }
    java.nio.file.Files.walk(tempDir, FileVisitOption.FOLLOW_LINKS)
        .sorted(Comparator.reverseOrder())
        .map(Path::toFile)
        .forEach(File::delete);
  }

}
