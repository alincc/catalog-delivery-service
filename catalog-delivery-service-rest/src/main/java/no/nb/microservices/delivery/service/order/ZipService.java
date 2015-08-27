package no.nb.microservices.delivery.service.order;

import no.nb.microservices.delivery.metadata.model.DeliveryFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(ZipService.class);

    @Override
    public File zipIt(String outputZipPath, List<DeliveryFile> deliveryFiles) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputZipPath);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ZipOutputStream zos = new ZipOutputStream(bos);
        for(DeliveryFile file : deliveryFiles) {
            ZipEntry ze = new ZipEntry(file.getFilename());
            zos.putNextEntry(ze);
            zos.write(file.getContent().getByteArray());
            zos.closeEntry();
        }
        zos.close();

        return new File(outputZipPath);
    }
}
