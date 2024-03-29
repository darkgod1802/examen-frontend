package bo.edu.ucb.darkgod.examen.Servicios;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Cifrado {
    private static final String CLAVE="SOFT-2019";
    public HashMap<String, byte[]> encryptBytes(byte[] plainTextBytes,String passwordString)
    {
        HashMap<String, byte[]> map = new HashMap<String, byte[]>();

        try
        {
            //Random salt for next step
            SecureRandom random = new SecureRandom();
            byte salt[] = new byte[256];
            random.nextBytes(salt);

            //PBKDF2 - derive the key from the password, don't use passwords directly
            char[] passwordChar = passwordString.toCharArray(); //Turn password into char[] array
            PBEKeySpec pbKeySpec = new PBEKeySpec(passwordChar, salt, 1324, 256); //1324 iterations
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = secretKeyFactory.generateSecret(pbKeySpec).getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            //Create initialization vector for AES
            SecureRandom ivRandom = new SecureRandom(); //not caching previous seeded instance of SecureRandom
            byte[] iv = new byte[16];
            ivRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            //Encrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plainTextBytes);

            map.put("salt", salt);
            map.put("iv", iv);
            map.put("encrypted", encrypted);
        }
        catch(Exception e)
        {
            Log.e("Cifrado", "encryption exception", e);
        }

        return map;
    }
    public byte[] decryptData(HashMap<String, byte[]> map, String passwordString)
    {
        byte[] decrypted = null;
        try
        {
            byte salt[] = map.get("salt");
            byte iv[] = map.get("iv");
            byte encrypted[] = map.get("encrypted");

            //regenerate key from password
            char[] passwordChar = passwordString.toCharArray();
            PBEKeySpec pbKeySpec = new PBEKeySpec(passwordChar, salt, 1324, 256);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = secretKeyFactory.generateSecret(pbKeySpec).getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            //Decrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            decrypted = cipher.doFinal(encrypted);
        }
        catch(Exception e)
        {
            Log.e("Cifrado", "decryption exception", e);
        }

        return decrypted;
    }
    public void guardarToken(String token, Context context){
        byte[] keyBytes = "token".getBytes();
        byte[] tokenBytes = token.getBytes();
        HashMap<String, byte[]> map = encryptBytes(tokenBytes, CLAVE);
        SharedPreferences.Editor editor = context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit();

        String keyBase64String = Base64.encodeToString(keyBytes, Base64.NO_WRAP);
        String valueBase64String = Base64.encodeToString(tokenBytes, Base64.NO_WRAP);
        editor.putString(keyBase64String, valueBase64String);
        editor.putString("token",token);
        editor.commit();
    }
    public String obtenerToken(Context context){
        byte[] keyBytes = "token".getBytes();
        String keyBase64String = Base64.encodeToString(keyBytes, Base64.NO_WRAP);
        SharedPreferences preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String base64EncryptedString = preferences.getString(keyBase64String, "default");
        byte[] encryptedBytes = Base64.decode(base64EncryptedString, Base64.NO_WRAP);
        String decryptedString="";
        try {
            decryptedString = new String(encryptedBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decryptedString;
    }
    public void borrarToken(Context context){
        Log.e("TOKENSEG","entre a borrarme");
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().clear().commit();
    }
}
