package com.mars.phms.utils.ValidateCode;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;


public class ValidateCode {
    private BufferedImage image;
    private String code;
    private LocalDateTime expireTime;

    public ValidateCode(BufferedImage image, String code, int expireSecond) {
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireSecond);
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expireTime);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}