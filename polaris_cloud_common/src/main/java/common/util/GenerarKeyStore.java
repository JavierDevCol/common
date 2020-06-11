package common.util;

import common.types.ByteNombreDto;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

@Slf4j
public final class GenerarKeyStore {

    public static final String TYPE = "jceks";
    public static final String SECRET_KEY = "AES";
    public static final String EXTENSION = ".jceks";

    private GenerarKeyStore() {
    }

    public static ByteBuffer generarKeyStore(String passwordEntry, List<ByteNombreDto> data) {
        try {
            KeyStore ks = KeyStore.getInstance(TYPE);

            char[] passArray = passwordEntry.toCharArray();
            try {
                ks.load(null, passArray);

                for (ByteNombreDto bytes : data) {
                    SecretKey secretKey = new SecretKeySpec(bytes.getData().array(), SECRET_KEY);
                    KeyStore.SecretKeyEntry secret = new KeyStore.SecretKeyEntry(secretKey);
                    KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection(passArray);
                    ks.setEntry(bytes.getNombre(), secret, password);
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ks.store(outputStream, passArray);
                ByteBuffer keyStoreData = ByteBuffer.wrap(outputStream.toByteArray());
                return keyStoreData;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (CertificateException e) {
                e.printStackTrace();
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ByteBuffer generarKeyStore(String passwordEntry, List<ByteNombreDto> data, TipoKeyStore tipo) {
        try {
            KeyStore ks = KeyStore.getInstance(tipo.getTipo());

            char[] passArray = passwordEntry.toCharArray();
            try {
                ks.load(null, passArray);

                for (ByteNombreDto bytes : data) {
                    SecretKey secretKey = new SecretKeySpec(bytes.getData().array(), SECRET_KEY);
                    KeyStore.SecretKeyEntry secret = new KeyStore.SecretKeyEntry(secretKey);
                    KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection(passArray);
                    ks.setEntry(bytes.getNombre(), secret, password);
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ks.store(outputStream, passArray);
                ByteBuffer keyStoreData = ByteBuffer.wrap(outputStream.toByteArray());
                return keyStoreData;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (CertificateException e) {
                e.printStackTrace();
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ByteBuffer updateArchive(String passwordEntry, List<ByteNombreDto> data, ByteBuffer dataKeyStore) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(dataKeyStore.array());
            KeyStore ks = KeyStore.getInstance(TYPE);
            char[] passArray = passwordEntry.toCharArray();
            try {
                ks.load(inputStream, passArray);
                for (ByteNombreDto byteNombreDto : data) {
                    ks.deleteEntry(byteNombreDto.getNombre());
                }
                for (ByteNombreDto bytes : data) {
                    SecretKey secretKey = new SecretKeySpec(bytes.getData().array(), SECRET_KEY);
                    KeyStore.SecretKeyEntry secret = new KeyStore.SecretKeyEntry(secretKey);
                    KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection(passArray);
                    ks.setEntry(bytes.getNombre(), secret, password);
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ks.store(outputStream, passArray);
                ByteBuffer keyStoreData = ByteBuffer.wrap(outputStream.toByteArray());
                return keyStoreData;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (CertificateException e) {
                e.printStackTrace();
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ByteNombreDto> loadArchives(String passwordEntry, ByteBuffer dataKeyStore) {
        List<ByteNombreDto> listResponse = new ArrayList<>();
        try {
            String temp = UtilFile.createFileTemp("test", EXTENSION, dataKeyStore.array());
            log.info(String.format("la ubicacion del archivo temporal es = %s", temp));
            ByteArrayInputStream inputStream = new ByteArrayInputStream(dataKeyStore.array());
            KeyStore ks = KeyStore.getInstance(TYPE);
            char[] passArray = passwordEntry.toCharArray();
            try {
                ks.load(inputStream, passArray);
                Enumeration<String> allAlias = ks.aliases();
                while (allAlias.hasMoreElements()) {
                    String alias = allAlias.nextElement();
                    if (Objects.nonNull(alias)) {
                        Key key = ks.getKey(alias, passArray);
                        if (Objects.nonNull(key)) {
                            byte[] data = key.getEncoded();
                            listResponse.add(
                                    ByteNombreDto
                                            .builder()
                                            .nombre(alias)
                                            .data(ByteBuffer.wrap(data))
                                            .build()
                            );
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (CertificateException e) {
                e.printStackTrace();
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            }
        }
        catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return listResponse;
    }
}
