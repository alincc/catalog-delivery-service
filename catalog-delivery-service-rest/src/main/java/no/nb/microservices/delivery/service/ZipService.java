package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.model.generic.ItemResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by andreasb on 14.07.15.
 */
@Service
public class ZipService implements IZipService {

    @Override
    public File zipIt(String outputZipPath, List<ItemResource> itemResources) {
        try {
            FileOutputStream fos = new FileOutputStream(outputZipPath);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ZipOutputStream zos = new ZipOutputStream(bos);
            for(ItemResource resource : itemResources) {
                ZipEntry ze = new ZipEntry(resource.getFilename());
                zos.putNextEntry(ze);
                zos.write(resource.getContent().getByteArray());
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
