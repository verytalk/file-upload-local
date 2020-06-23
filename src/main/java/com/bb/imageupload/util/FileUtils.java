package com.bb.imageupload.util;

import com.bb.imageupload.config.Configs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


/**
 * @author Jason
 */
@Component
public class FileUtils {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    Configs configs;

    /**
     *
     * @param file
     * @param path
     * @param fileName
     * @return
     */
    public boolean upload(MultipartFile file, String path, String fileName) {
        String realPath = path + "/" + fileName;
        logger.info("upload file dir is :{}",realPath);
        File dest = new File(realPath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest);
            String objectName = fileName;
            String filePath = realPath;
            String contentType = file.getContentType();
            return true;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}



