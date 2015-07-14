package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.model.generic.ItemResource;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by andreasb on 14.07.15.
 */
public class ZipService implements IZipService {

    @Override
    public void zipIt(String outputZipPath, List<ItemResource> itemResources) {
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
    }
}
