package com.markerhub.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.context.annotation.Bean;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
class KaptchaConfig {
    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();  //主要用来设置长宽高
        properties.put("kaptcha.border", "no"); //是否有边框
        properties.put("kaptcha.textproducer.font.color", "black");  //文本的颜色
        properties.put("kaptcha.textproducer.char.space", "4");  //字符间的空行
        properties.put("kaptcha.image.height", "40");  //高度
        properties.put("kaptcha.image.width", "120");  //宽度
        properties.put("kaptcha.textproducer.font.size", "30"); //文字大小

        Config config = new Config(properties);  //里面可以看到可以配置图片的参数有哪些
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
