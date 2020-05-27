package common.util;

import common.types.ByteNombreDto;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import static common.util.UtilFile.deleteFile;
import static common.util.UtilFile.readBytesFile;

public final class GenerarKeyStore {

    public static final String TYPE = "jceks";
    public static final String EXTENSION = "." + TYPE;

    private GenerarKeyStore() {
    }

    public static ByteBuffer generarKeyStore(String passwordEntry, List<ByteNombreDto> data) {
        try {
            KeyStore ks = KeyStore.getInstance(TYPE);

            char[] passArray = passwordEntry.toCharArray();
            try {
                ks.load(null, passArray);

                for (ByteNombreDto bytes : data) {
                    SecretKey secretKey = new SecretKeySpec(bytes.getData().array(), "AES");
                    KeyStore.SecretKeyEntry secret = new KeyStore.SecretKeyEntry(secretKey);
                    KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection(passArray);
                    ks.setEntry(bytes.getNombre(), secret, password);
                }

                String path = UtilFile.createFileTemp("keystore", EXTENSION);
                try (FileOutputStream fos = new FileOutputStream(path)) {
                    ks.store(fos, passArray);
                }
                ByteBuffer keyStoreData = readBytesFile(path);
                deleteFile(path);
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
            String path = UtilFile.createFileTemp("keystore", EXTENSION, dataKeyStore.array());
            FileInputStream fileInputStream = new FileInputStream(path);

            KeyStore ks = KeyStore.getInstance(TYPE);
            char[] passArray = passwordEntry.toCharArray();
            try {
                ks.load(fileInputStream, passArray);
                for (ByteNombreDto byteNombreDto : data) {
                    ks.deleteEntry(byteNombreDto.getNombre());
                }
                for (ByteNombreDto bytes : data) {
                    SecretKey secretKey = new SecretKeySpec(bytes.getData().array(), "AES");
                    KeyStore.SecretKeyEntry secret = new KeyStore.SecretKeyEntry(secretKey);
                    KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection(passArray);
                    ks.setEntry(bytes.getNombre(), secret, password);
                }

                try (FileOutputStream fos = new FileOutputStream(path)) {
                    ks.store(fos, passArray);
                }
                ByteBuffer keyStoreData = readBytesFile(path);
                deleteFile(path);
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
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ByteNombreDto> loadArchives(String passwordEntry, ByteBuffer dataKeyStore) {
        List<ByteNombreDto> listResponse = new ArrayList<>();
        try {
            String path = UtilFile.createFileTemp("keystore", EXTENSION, dataKeyStore.array());
            FileInputStream fileInputStream = new FileInputStream(path);

            KeyStore ks = KeyStore.getInstance(TYPE);
            char[] passArray = passwordEntry.toCharArray();
            try {
                ks.load(fileInputStream, passArray);
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
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return listResponse;
    }
}
