package by.epamtc.stanislavmelnikov.service.encryption;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordEncryption {
    public static String encryptPassword(String source) {
        String encrypted = DigestUtils.md5Hex(source);
        return encrypted;
    }
}
