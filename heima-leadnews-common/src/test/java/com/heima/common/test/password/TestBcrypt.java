package com.heima.common.test.password;

import com.heima.common.bcrypt.PasswordConfig;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBcrypt {

//    @Autowired
//    private BCryptPasswordEncoder encoder;

    @Test
    public void useBcrypt() {
        BCryptPasswordEncoder encoder = new PasswordConfig().passwordEncoder();
        String str = "123456";
//        String encode = encoder.encode(str);
        //        System.out.println(str + ",加密后内容=" + encode);
//      加密后的串
        String encoded = "$2a$10$bpmR99IjgEhXjS5o1yTsEefIt5EV5rrgIrYBonrQ7hh3Kst0YecdC";

//        通过加密后的串，使用自己的算法得到随机秘钥
//        使用encoder的加密方法，对传入的原始内容+随机秘钥 进行加密 得到一个新的加密串
//        比较2个加密串是否一样
        boolean b = encoder.matches(str, encoded);
        System.out.println(b);



    }
}
