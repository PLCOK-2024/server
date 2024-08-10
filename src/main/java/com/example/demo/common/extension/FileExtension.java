package com.example.demo.common.extension;

import com.example.demo.common.error.BusinessException;
import com.nimbusds.jose.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;

import static com.example.demo.common.error.ErrorCode.INVALID_TYPE_VALUE;

@Slf4j
public class FileExtension {
    public static String getExtension(MultipartFile file) {
        return StringUtils.getFilenameExtension(file.getOriginalFilename());
    }

    public static Pair<Integer, Integer> getImageSize(MultipartFile file) throws IOException {
        var bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null) {
            throw new BusinessException(String.format(
                    "지원하지 않는 파일 형식 입니다. name:%s type:%s",
                    file.getOriginalFilename(),
                    file.getContentType()
            ), INVALID_TYPE_VALUE);
        }

        return Pair.of(
                bufferedImage.getWidth(),
                bufferedImage.getHeight()
        );
    }
}
