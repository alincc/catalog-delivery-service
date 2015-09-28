package no.nb.microservices.delivery.core.print.factory;

import no.nb.microservices.delivery.core.print.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by andreasb on 23.09.15.
 */
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

    public FormatService getPrintFormat(String format) {

        List<String> images = Arrays.asList("jpg", "tif", "jp2");

        if ("pdf".equalsIgnoreCase(format)) {
            return bookPrintFormat;
        }
        else if ("alto".equalsIgnoreCase(format)) {
            return altoPrintFormat;
        }
        else if ("txt".equalsIgnoreCase(format)) {
            return textPrintFormat;
        }
        else if (images.contains(format)) {
            return imagePrintFormat;
        }

        return null;
    }
}
