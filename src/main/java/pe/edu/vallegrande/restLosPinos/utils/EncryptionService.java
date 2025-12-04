package pe.edu.vallegrande.restLosPinos.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public String encrypt(String plainText) {
        try {
            // Asegurar que la clave tenga exactamente 32 bytes para AES-256
            byte[] keyBytes = SECRET_KEY.getBytes();
            byte[] key32Bytes = new byte[32];
            
            if (keyBytes.length >= 32) {
                System.arraycopy(keyBytes, 0, key32Bytes, 0, 32);
            } else {
                System.arraycopy(keyBytes, 0, key32Bytes, 0, keyBytes.length);
                // Rellenar con ceros si es necesario
                for (int i = keyBytes.length; i < 32; i++) {
                    key32Bytes[i] = 0;
                }
            }
            
            SecretKeySpec secretKey = new SecretKeySpec(key32Bytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            // Asegurar que la clave tenga exactamente 32 bytes para AES-256
            byte[] keyBytes = SECRET_KEY.getBytes();
            byte[] key32Bytes = new byte[32];
            
            if (keyBytes.length >= 32) {
                System.arraycopy(keyBytes, 0, key32Bytes, 0, 32);
            } else {
                System.arraycopy(keyBytes, 0, key32Bytes, 0, keyBytes.length);
                // Rellenar con ceros si es necesario
                for (int i = keyBytes.length; i < 32; i++) {
                    key32Bytes[i] = 0;
                }
            }
            
            SecretKeySpec secretKey = new SecretKeySpec(key32Bytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}
