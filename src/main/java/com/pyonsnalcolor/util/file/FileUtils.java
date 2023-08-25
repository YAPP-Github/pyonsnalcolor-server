package com.pyonsnalcolor.util.file;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Configuration
public class FileUtils {
    private static String BASE_PATH;

    @Value("${file.base-path}")
    public void setNameStatic(String filePath) {
        BASE_PATH = filePath;
    }

    public static String uploadImage(MultipartFile image, String dirName) throws Exception {
        try {
            String dirPath = BASE_PATH + File.separator + dirName;
            String filePath = dirPath + File.separator + image.getOriginalFilename();

            File folder = new File(dirPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File destination = new File(filePath);
            image.transferTo(destination);

            return filePath;
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }
}
