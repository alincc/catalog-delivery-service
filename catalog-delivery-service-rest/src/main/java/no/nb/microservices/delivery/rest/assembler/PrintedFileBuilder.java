package no.nb.microservices.delivery.rest.assembler;

import no.nb.microservices.delivery.model.metadata.PrintedFile;
import no.nb.microservices.delivery.model.metadata.PrintedResource;
import no.nb.microservices.delivery.model.request.PrintFormat;
import no.nb.microservices.delivery.model.request.PrintedResourceRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PrintedFileBuilder {

    PrintedFile printedFile;
    String compression = "zip";

    public PrintedFileBuilder() {
        printedFile = new PrintedFile();
    }

    public PrintedFileBuilder withFormat(PrintFormat format) {
        printedFile.setFormat(format);
        return this;
    }

    public PrintedFileBuilder withQuality(int quality) {
        printedFile.setQuality(quality);
        return this;
    }

    public PrintedFileBuilder withQuality(boolean high) {
        printedFile.setQuality(high ? 6 : 5);
        return this;
    }

    public PrintedFileBuilder addText(boolean text) {
        printedFile.setText(text);
        return this;
    }

    public PrintedFileBuilder addResource(String urn, String pages) {
        if (printedFile.getResources() == null) {
            printedFile.setResources(new ArrayList<>());
        }

        printedFile.getResources().add(new PrintedResource(urn, pages));

        return this;
    }

    public PrintedFileBuilder withResources(List<PrintedResource> resources) {
        printedFile.setResources(resources);
        return this;
    }

    public PrintedFileBuilder withResourceRequests(List<PrintedResourceRequest> resourcesRequests) {

        List<PrintedResource> resources = new ArrayList<>();
        for (PrintedResourceRequest printedResourceRequest : resourcesRequests) {
            PrintedResource printedResource = new PrintedResource();
            printedResource.setUrn(printedResourceRequest.getUrn());
            printedResource.setPages(printedResourceRequest.getPages());
            printedResource.setPageSelection(printedResourceRequest.getPageSelection());
            resources.add(printedResource);
        }

        printedFile.setResources(resources);

        return this;
    }

    public PrintedFileBuilder compressAs(String compression) {
        this.compression = compression;
        return this;
    }

    public PrintedFile build() {
        if (this.printedFile.getFormat() == PrintFormat.EPUB
                || this.printedFile.getFormat() == PrintFormat.PDF
                || this.printedFile.getFormat() == PrintFormat.TXT
                || ((this.printedFile.getFormat() == PrintFormat.JP2
                || this.printedFile.getFormat() == PrintFormat.JPG
                || this.printedFile.getFormat() == PrintFormat.TIF)
                && this.printedFile.getResources().size() == 1
                && StringUtils.isEmpty(this.printedFile.getResources().get(0).getPages()))) {
            this.printedFile.setExtension(this.printedFile.getFormat().toString().toLowerCase());
        } else {
            this.printedFile.setExtension(compression);
        }

        this.printedFile.setFilename(this.printedFile.getResources().get(0).getUrn() + "." + this.printedFile.getExtension());

        return printedFile;
    }
}
