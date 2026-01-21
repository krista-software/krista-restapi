/*
 * RestApi Extension for Krista
 * Copyright (C) 2024 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.development.api.rest.util;

import app.krista.ksdk.files.FileHandle;
import app.krista.ksdk.files.FileRepository;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Utility service for converting files between Krista's file system and Java file objects.
 * <p>
 * This class provides bidirectional file conversion capabilities:
 * <ul>
 *   <li>Upload Java files to Krista's media server</li>
 *   <li>Download Krista files to Java file objects</li>
 *   <li>Automatic compression of unsupported file formats into ZIP archives</li>
 * </ul>
 * Unsupported file formats (e.g., executable files, scripts) are automatically compressed
 * into ZIP archives before upload for security purposes.
 * </p>
 *
 * @see FileRepository
 * @see FileHandle
 */
@Service
public class KristaMediaClient {

    public  static final String ZIP_DIR = "/tmp/";
    @Inject
    private FileRepository fileRepository;

    private final List<String> unSupportedFileFormats = Arrays.asList("html", "php5", "pht", "phtml", "shtml", "asa", "cer", "asax", "swf", "xap", "jsp", "exe", "js");

    /**
     * Uploads a file to Krista's media server.
     * <p>
     * Takes a {@link java.io.File} object as input and returns a Krista file object.
     * If the file format is unsupported (e.g., executable files, scripts), the file is
     * automatically compressed into a ZIP archive before upload for security purposes.
     * </p>
     *
     * @param file the Java file object to be uploaded
     * @return the Krista file object representing the uploaded file
     * @throws IOException if an I/O error occurs during file upload or compression
     */
    public app.krista.model.base.File toKristaFile(File file) throws IOException {
        System.out.println("File Name " + file.getName());
        if (isUnsupportedFileFormat(file.getName())) {
            String zipFilePath = ZIP_DIR + file.getName().substring(0, file.getName().lastIndexOf(".")) + ".zip";
            compressFile(zipFilePath, file.getAbsolutePath());
            file = new File(zipFilePath);
        }
        try (final FileHandle fileHandle = fileRepository.createNewFileByName(file.getName())) {
            fileHandle.setContent(new FileInputStream(file));
            return fileHandle.getFile();
        }
    }

    /**
     * Downloads a file from Krista's media server.
     * <p>
     * Takes a Krista file object as input and returns a {@link java.io.File} object.
     * The file content is retrieved from Krista's file repository and written to a local file.
     * </p>
     *
     * @param file the Krista file object to be downloaded
     * @return the Java file object containing the downloaded file content
     * @throws IOException if an I/O error occurs during file download or conversion
     */
    public File toJavaFile(app.krista.model.base.File file) throws IOException {
        try (FileHandle fileHandle = fileRepository.getFile(file)) {
            InputStream content = fileHandle.getContent();
            final File input = new File(file.getFileName());
            return convertInputStreamToFile(content, input);
        }
    }

    /**
     * Checks if the file format is unsupported.
     *
     * @param fileName The name of the file.
     * @return True if the file format is unsupported, otherwise false.
     */
    private boolean isUnsupportedFileFormat(String fileName) {
        String fileExtension = getFileExtension(fileName);
        return unSupportedFileFormats.contains(fileExtension);
    }

    /**
     * Converts an input stream to a file.
     *
     * @param inputStream The input stream to be converted.
     * @param input The file to write the input stream content to.
     * @return The file with the content of the input stream.
     * @throws IOException If an I/O error occurs.
     */
    private File convertInputStreamToFile(InputStream inputStream, File input) throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(input))) {
            byte[] buffer = new byte[4096]; // Increasing buffer size for better performance with larger files
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return input;
    }


    /**
     * Retrieves the file extension from the file name.
     *
     * @param fileName the name of the file
     * @return the file extension without the dot (e.g., "txt", "pdf")
     * @throws IllegalArgumentException if the file name does not contain an extension
     */
    private String getFileExtension(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring((fileName.lastIndexOf(".") + 1));
        }
        throw new IllegalArgumentException("Unsupported file format");
    }

    /**
     * Compresses a file into a ZIP archive.
     * <p>
     * Creates a ZIP file at the specified path containing the file from the source path.
     * This method is used to compress unsupported file formats before uploading to Krista's media server.
     * </p>
     *
     * @param zipFilePath the path where the ZIP file will be created
     * @param dirPathToZip the path of the file to be compressed
     * @throws IOException if an I/O error occurs during compression
     */
    public static void compressFile(String zipFilePath, String dirPathToZip) throws IOException {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            File fileToZip = new File(dirPathToZip);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            zipOut.closeEntry();
        }
    }

}
