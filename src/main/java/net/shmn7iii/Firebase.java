package net.shmn7iii;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Firebase {
    private static Firebase firebase = new Firebase();
    private Firebase() {}
    public static Firebase getInstance() { return firebase; }

    public FirebaseApp firebaseapp;

    public void init (String credential_file_path) throws IOException {
        FileInputStream serviceAccount = new FileInputStream(credential_file_path);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("tap-f4f38.appspot.com")
                .build();
        this.firebaseapp = FirebaseApp.initializeApp(options);
    }

    public String getDownloadURL(String data){
        // get filename from Firebase URI
        String[] ary = data.split("/");
        String filename = ary[ary.length - 1];

        // get storage blob
        StorageClient storage = StorageClient.getInstance(this.firebaseapp);
        Bucket bucket = storage.bucket();
        Blob blob = bucket.get(filename);

        // get download url
        URL url = blob.signUrl(14, TimeUnit.DAYS);

        return url.toString();
    }

    public String addImage2Firebase(String imageURL) throws IOException {
        // get image byte array from url
        String[] ary = imageURL.split("\\.");
        String extension = ary[ary.length - 1];
        byte[] imageByte = getImageByteArray(imageURL,extension );

        // create file name
        Random random = new Random();
        String filename = "fromdicbot" + random.nextInt(10000000) + "." + extension;

        // get storage bucket
        StorageClient storage = StorageClient.getInstance(this.firebaseapp);
        Bucket bucket = storage.bucket();

        // create blob
        Blob blob = bucket.create("tmp/" + filename, imageByte, "image/" + extension);

        // return Firebase URI
        String URI = "gs://tap-f4f38.appspot.com/tmp/" + filename;

        return URI;
    }


    // https://qiita.com/narikei/items/bc15002f3cac5d975b34
    public byte[] getImageByteArray(String _url, String fileNameExtension) throws IOException {
        URL url = new URL(_url);
        BufferedImage readImage = ImageIO.read(url);
        ByteArrayOutputStream outPutStream = new ByteArrayOutputStream();
        ImageIO.write(readImage, fileNameExtension, outPutStream);
        return outPutStream.toByteArray();
    }
}
