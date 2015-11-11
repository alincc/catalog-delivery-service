package no.nb.microservices.delivery.core.print.factory;

import no.nb.microservices.delivery.core.print.service.*;
import no.nb.microservices.delivery.model.request.PrintFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PrintFormatFactory {

    private FormatAltoService altoPrintFormat;
    private FormatBookService bookPrintFormat;
    private FormatImageService imagePrintFormat;
    private FormatTextService textPrintFormat;

    @Autowired
    public PrintFormatFactory(FormatAltoService altoPrintFormat, FormatBookService bookPrintFormat, FormatImageService imagePrintFormat, FormatTextService textPrintFormat) {
        this.altoPrintFormat = altoPrintFormat;
        this.bookPrintFormat = bookPrintFormat;
        this.imagePrintFormat = imagePrintFormat;
        this.textPrintFormat = textPrintFormat;
    }

    public FormatService getPrintFormat(PrintFormat format) {

        List<String> images = Arrays.asList("jpg", "tif", "jp2");

        if (format == PrintFormat.PDF) {
            return bookPrintFormat;
        } else if (format == PrintFormat.ALTO) {
            return altoPrintFormat;
        } else if (format == PrintFormat.TXT) {
            return textPrintFormat;
        } else if (images.contains(format.toString())) {
            return imagePrintFormat;
        }

        return null;
    }
}
