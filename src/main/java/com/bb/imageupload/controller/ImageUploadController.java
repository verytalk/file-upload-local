package com.bb.imageupload.controller;

import com.bb.imageupload.config.Configs;
import com.bb.imageupload.util.Base64Image;
import com.bb.imageupload.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Jason
 */
@RestController
public class ImageUploadController {

    @Autowired
    Configs configs;

    @Autowired
    FileUtils fileUtils;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/imageUpload")
    public Map imageUpload(@RequestParam("upload") MultipartFile file
            ,@RequestParam("filename") String filename,@RequestHeader("token") String token){
        String resultMsg;
        int resultCode = 401;
        Map<String,Object> root= new HashMap<>(16);
        String fileType = file.getContentType();
        String fileName = file.getOriginalFilename();
        logger.info("api is {}","/imageUpload");
        logger.info("file type is : {}",fileType);
        logger.info("file name is : {}",fileName);
        logger.info("file name is : {}",fileName);
        logger.info("file size is : {}",file.getSize());
        logger.info("rewrite filename is : {}",filename);
        logger.info("request token is : {}",token);

        if(!token.equals(configs.getToken())){
            resultMsg="token auth failed .";
            root.put("result_msg",resultMsg);
            return root;
        }

        int sizeUnit = 1000;
        if (file.getSize() / sizeUnit > configs.getMaxSize()){
            resultMsg="max allowed "+configs.getMaxSize();
            root.put("result_msg",resultMsg);
            return root;
        }
        String allowType = configs.getUploadType();
        boolean isAllowed = false;
        String[] strs = allowType.split(",");
        for(String s:strs){
            if(fileType.equals(s)){
                isAllowed = true;
            }
        }
        if(isAllowed == false){
            resultMsg="upload type is not allowed: "+ allowType;
            root.put("result_msg",resultMsg);
            return root;
        }

        final String localPath=configs.getUploadDir();
        if (fileUtils.upload(file, localPath, filename)) {
            resultMsg="upload success";
            resultCode = 200;
        }else{
            resultMsg="upload false";
        }
        root.put("result_msg",resultMsg);
        root.put("result_code",resultCode);
        return root;
    }

    @PostMapping("/imageUploadBase64")
    public Map imageUploadBase64(@RequestParam("data") String data
            ,@RequestParam("filename") String filename,@RequestHeader("token") String token){
        String resultMsg ="";
        int resultCode = 401;
        Map<String,Object> root= new HashMap<>(16);

        logger.info("rewrite filename is : {}",filename);
        logger.info("request token is : {}",token);

        if(!token.equals(configs.getToken())){
            resultMsg="token auth failed .";
            root.put("resultMsg",resultMsg);
            return root;
        }
        final String localPath=configs.getUploadDir();
        String imagesPath = localPath+"/"+filename;
        System.out.println(data);
        if (Base64Image.generateImage(data,imagesPath)) {
            resultMsg="upload success";
            resultCode = 200;
        }else{
            resultMsg="upload failed";
        }
        root.put("resultMsg",resultMsg);
        root.put("result_code",resultCode);
        return root;
    }

    @PostMapping(value = "/getFile",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@RequestParam("filename") String filename,@RequestHeader("token") String token) throws IOException {
        logger.info("api is {}","/getFile");
        logger.info("filename is {}",filename);
        logger.info("token is {}",token);
        String filePath = configs.getUploadDir()+"/"+filename;
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }

}
