package raze.spring.inventory.routes;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class FileRouter extends RouteBuilder {
    private final FileProcessor fileProcessor;
    private final FileRemoveProcessor removeProcessor;

    public FileRouter(FileProcessor fileProcessor, FileRemoveProcessor removeProcessor) {
        this.fileProcessor = fileProcessor;
        this.removeProcessor = removeProcessor;
    }

    @Override
    public void configure() throws Exception {
        from("file:files/images/product?recursive=true&noop=true&flatten=true")
                .doTry()
                    .bean(fileProcessor)
                .doCatch(Exception.class)
                    .bean(removeProcessor)
                    .log("FAILED TO RESIZE IMAGE")
                .doCatch(IOException.class)
                    .log("FAILED TO REMOVE IMAGE FILE");

    }
}

//
@Component
@Slf4j
class FileProcessor {

    public File convertFile(@ExchangeProperties Map<String, String> properties,
                            @Headers Map<String, String> headers,
                            @Body File file ) throws IOException {
        BufferedImage bi = ImageIO.read(file);
        BufferedImage resizedImg = Scalr.resize(bi, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 32, 32, Scalr.OP_ANTIALIAS);
        File resizedImageFile = new File( headers.get("CamelFilePath").replace(".original.", ".small."));
        ImageIO.write(resizedImg, Files.getFileExtension(file.getName()) , resizedImageFile);
        return  resizedImageFile;
    }
}
@Component
@Slf4j
class FileRemoveProcessor {

    public File removeFile (@ExchangeProperties Map<String, String> properties,
                            @Headers Map<String, String> headers,
                            @Body File file) throws IOException {
        log.debug("BODY: {} \n HEADERS: {} \n PROPS: {}", file, headers, properties);
        log.debug("removing file path : {} , ", file.getPath());
        FileUtils.cleanDirectory(new File(headers.get("CamelFileParent")));
        return file;
    }

}
