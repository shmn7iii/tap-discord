package net.shmn7iii;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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
        String[] ary = data.split("/");
        String filename = ary[ary.length - 1];

        StorageClient storage = StorageClient.getInstance(this.firebaseapp);
        Bucket bucket = storage.bucket();
        Blob blob = bucket.get(filename);
        URL url = blob.signUrl(14, TimeUnit.DAYS);
        return url.toString();
    }
}
