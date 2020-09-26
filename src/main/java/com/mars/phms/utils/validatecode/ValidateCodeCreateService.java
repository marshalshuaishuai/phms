package com.mars.phms.utils.validatecode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class ValidateCodeCreateService {
    Logger logger= LoggerFactory.getLogger(ValidateCodeCreateService.class);
    public ValidateCode createImageCode() {
        //图片宽度
        int width=80;
        //图片高度
        int height=32;
        //验证码长度
        int charLength=4;
        //过期时间 单位秒
        int expireTime=60;
        BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g=image.getGraphics();
        Random random=new Random();
        //设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman",Font.PLAIN,30));
        g.setColor(getRandColor(160, 200));
        for(int i=0;i<155;i++) {
            int x=random.nextInt(width);
            int y=random.nextInt(height);
            int x1=random.nextInt(12);
            int y1=random.nextInt(12);
            g.drawLine(x, y, x+x1, y+y1);
        }

        String sRand="";
        for(int i=0;i<charLength;i++) {
            String rand=String.valueOf(random.nextInt(10));
            sRand+=rand;
            g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
            g.drawString(rand, 13*i+13, 26);
        }
        g.dispose();
        return new ValidateCode(image, sRand, expireTime);
    }

    private Color getRandColor(int fc,int bc) {
        Random random=new Random();
        if(fc>255) {
            fc=255;
        }
        if(bc>255) {
            bc=255;
        }
        int r=fc+random.nextInt(bc-fc);
        int g=fc+random.nextInt(bc-fc);
        int b=fc+random.nextInt(bc-fc);
        return new Color(r,g,b);
    }
}