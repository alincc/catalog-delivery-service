package no.nb.microservices.delivery.service.order;

import no.nb.microservices.delivery.metadata.model.DeliveryFile;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by andreasb on 14.07.15.
 */
@Service
public class ZipService implements IZipService {

    @Override
    public File zipIt(String outputZipPath, List<DeliveryFile> deliveryFiles) {
        try {
            FileOutputStream fos = new FileOutputStream(outputZipPath);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ZipOutputStream zos = new ZipOutputStream(bos);
            for(DeliveryFile file : deliveryFiles) {
                ZipEntry ze = new ZipEntry(file.getFilename() + "." + file.getExtension());
                zos.putNextEntry(ze);
                zos.write(file.getContent().getByteArray());
                zos.closeEntry();
            }
            zos.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

        File zippedFile = new File(outputZipPath);
        return zippedFile;
    }
}
