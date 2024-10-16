package fiap.wtu_ancora.service;

import fiap.wtu_ancora.dto.EventDTO;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class EventHashService {
    private static final String SECRET_KEY = "WTU_MASTER_BLASTER";

    public String createSignedHash(EventDTO eventDTO) {
        try{
            String dataToHash = eventDTO.getId() + eventDTO.getTitle();

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secretKey);

            byte[] hashBytes = sha256_HMAC.doFinal(dataToHash.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(hashBytes);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Erro ao gerar hash assinado: " + e.getMessage(), e);
        }

    }

    public boolean validateSignedHash(EventDTO eventDTO, String signedHash) {
        String expectedHash = createSignedHash(eventDTO);

        return expectedHash.equals(signedHash);
    }
}
