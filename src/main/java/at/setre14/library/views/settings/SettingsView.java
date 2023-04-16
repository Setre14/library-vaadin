package at.setre14.library.views.settings;

import at.setre14.library.calibre.CalibreImport;
import at.setre14.library.calibre.CalibreService;
import at.setre14.library.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@PageTitle("Settings")
@Route(value = "settings", layout = MainLayout.class)
@AnonymousAllowed
public class SettingsView extends VerticalLayout {

    private final CalibreService calibreService;

    private final FileBuffer buffer = new FileBuffer();
    private final Upload upload = new Upload(buffer);

    public SettingsView(
            CalibreService calibreService
    ) {
        this.calibreService = calibreService;

        upload.setAutoUpload(false);
        upload.setAcceptedFileTypes(".zip");
        upload.setMaxFiles(1);

        upload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        Button importCalibre = new Button("Import");
        importCalibre.addClickListener(e -> {
            upload.getElement().callJsFunction("uploadFiles");
        });

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();

            File extractDir = Paths.get("/wsp/sandbox/library-dev/").toFile();

            Path extracedFolder = Paths.get(extractDir.toString(), fileName.replace(".zip", ""));

            byte[] byteBuffer = new byte[1024];
            try (ZipInputStream zis = new ZipInputStream(buffer.getInputStream())) {
                ZipEntry zipEntry = zis.getNextEntry();
                while (zipEntry != null) {
                    File newFile = newFile(extractDir, zipEntry);
                    if (zipEntry.isDirectory()) {
                        if (!newFile.isDirectory() && !newFile.mkdirs()) {
                            throw new IOException("Failed to create directory " + newFile);
                        }
                    } else {
                        // fix for Windows-created archives
                        File parent = newFile.getParentFile();
                        if (!parent.isDirectory() && !parent.mkdirs()) {
                            throw new IOException("Failed to create directory " + parent);
                        }

                        // write file content
                        FileOutputStream fos = new FileOutputStream(newFile);
                        int len;
                        while ((len = zis.read(byteBuffer)) > 0) {
                            fos.write(byteBuffer, 0, len);
                        }
                        fos.close();
                    }
                    zipEntry = zis.getNextEntry();
                }

                zis.closeEntry();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//            Calibre calibre = new Calibre();

            CalibreImport calibreImport = null;
            try {


                calibreImport = this.calibreService.importDb(extracedFolder.toString());
                System.out.println(calibreImport);



                Files.walk(extracedFolder)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            // Do something with the file data
            // processFile(inputStream, fileName);
        });


        VerticalLayout content = new VerticalLayout(upload, importCalibre);
        content.setSpacing(false);
        content.setPadding(false);

        Details details = new Details("Import Calibre db", content);
        details.setOpened(true);

        add(details);
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

}
