package com.doubleclick.x_course.Repositorys;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.doubleclick.x_course.Model.Diploma;
import com.doubleclick.x_course.Model.Emails;
import com.doubleclick.x_course.Model.YouTubeDataModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;

public class LoadDataRepository {

    private DatabaseReference EmailsRef;
    private DatabaseReference AllPlayListsReferanc;
    private String name_Diplomat, numberOfDiploma, user_email,timestamp,NameDeveloper;
    private FirebaseAuth mAuth;
    private String UserId;
    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyB-c6ay-9BkRmdltEFr-zpSjNj6XPmvNuc";//here you should use your api key for testing purpose you can use this api also
//    private ArrayList<ArrayList<YouTubeDataModel>> YouTubeDataModelArrayLists=new ArrayList<>();


    public LoadDataRepository(OnItemClickListener listener, String name_Diplomat, String numberOfDiploma, String user_email,String NameDeveloper,String timestamp) {
        onItemClickListener = listener;
        this.name_Diplomat = name_Diplomat;
        this.numberOfDiploma = numberOfDiploma;
        this.user_email = user_email;
        this.timestamp = timestamp;
        this.NameDeveloper = NameDeveloper;
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid().toString();
    }


    public void loadUserData() {
        AllPlayListsReferanc = FirebaseDatabase.getInstance().getReference().child("AllPlayLists");
        AllPlayListsReferanc.keepSynced(true);
        EmailsRef = FirebaseDatabase.getInstance().getReference().child("Emails");
        EmailsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Emails email = dataSnapshot.getValue(Emails.class);
                    if (email.getUserId().equals(UserId)) {
                        AllPlayListsReferanc.child(NameDeveloper+":"+name_Diplomat+":"+numberOfDiploma+":"+timestamp).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Log.e("LoadDataRepository", name_Diplomat + numberOfDiploma + name_Diplomat);
                                        Diploma diploma = snapshot.getValue(Diploma.class);
                                        try {
                                            if (name_Diplomat.equals("Web")) {//email.getDiploma()
                                                if (email.getTrack().equals("FullStack")) {
                                                    if (!Objects.requireNonNull(diploma).getHTML().isEmpty() && !diploma.getHTML().equals("")) {
                                                        Log.e("LoadDataRepository", diploma.getHTML());
                                                        ConfigureURL(diploma.getHTML());
                                                    }
                                                    if (!diploma.getCSS().isEmpty() && !diploma.getCSS().equals("")) {
                                                        Log.e("LoadDataRepository", diploma.getCSS());
                                                        ConfigureURL(diploma.getCSS());
                                                    }
                                                    if (!diploma.getBootStrap().isEmpty() && !diploma.getBootStrap().equals("")) {
                                                        Log.e("LoadDataRepository", diploma.getBootStrap());
                                                        ConfigureURL(diploma.getBootStrap());
                                                    }
                                                    if (!diploma.getJavaScript().isEmpty() && !diploma.getJavaScript().equals("")) {
                                                        Log.e("LoadDataRepository", diploma.getJavaScript());
                                                        ConfigureURL(diploma.getJavaScript());
                                                    }
                                                    if (!diploma.getReact().isEmpty() && !diploma.getReact().equals("")) {
                                                        Log.e("LoadDataRepository", diploma.getReact());
                                                        ConfigureURL(diploma.getReact());
                                                    }
                                                    if (!diploma.getPhp().isEmpty() && !diploma.getPhp().equals("")) {
                                                        Log.e("LoadDataRepository", diploma.getPhp());
                                                        ConfigureURL(diploma.getPhp());
                                                    }
                                                    if (!diploma.getMySQL().isEmpty() && !diploma.getMySQL().equals("")) {
                                                        Log.e("LoadDataRepository", diploma.getMySQL());
                                                        ConfigureURL(diploma.getMySQL());
                                                    }
                                                    if (!diploma.getLaravel().isEmpty() && !diploma.getLaravel().equals("")) {
                                                        Log.e("LoadDataRepository", diploma.getLaravel());
                                                        ConfigureURL(diploma.getLaravel());
                                                    }
                                                } else if (email.getTrack().equals("FrontEnd")) {
                                                    if (!diploma.getHTML().isEmpty() && !diploma.getHTML().equals("")) {
                                                        ConfigureURL(diploma.getHTML());
                                                    }
                                                    if (!diploma.getCSS().isEmpty() && !diploma.getCSS().equals("")) {
                                                        ConfigureURL(diploma.getCSS());
                                                    }
                                                    if (!diploma.getBootStrap().isEmpty() && !diploma.getBootStrap().equals("")) {
                                                        ConfigureURL(diploma.getBootStrap());
                                                    }
                                                    if (!diploma.getJavaScript().isEmpty() && !diploma.getJavaScript().equals("")) {
                                                        ConfigureURL(diploma.getJavaScript());
                                                    }
                                                    if (!diploma.getReact().isEmpty() && !diploma.getReact().equals("")) {
                                                        ConfigureURL(diploma.getReact());
                                                    }
                                                }
                                            }
                                            if (name_Diplomat.equals("Mobile")) {//email.getDiploma()
                                                if (email.getTrack().equals("Java") && !Objects.requireNonNull(diploma).getJava().isEmpty()) {
                                                    ConfigureURL(diploma.getJava());
                                                }
                                                if (email.getTrack().equals("Flutter") && !Objects.requireNonNull(diploma).getFlutter().isEmpty()) {
                                                    ConfigureURL(diploma.getFlutter());
                                                }
                                                if (email.getTrack().equals("Kotlin") && !Objects.requireNonNull(diploma).getKotlin().isEmpty()) {
                                                    ConfigureURL(diploma.getKotlin());
                                                }
                                                if (!email.getTrack().equals("Ios") && !Objects.requireNonNull(diploma).getIos().isEmpty()) {
                                                    ConfigureURL(diploma.getIos());
                                                }
                                            }
                                            if (name_Diplomat.equals("graphicDesign")) {//email.getDiploma()
                                                if (email.getTrack().equals("graphicDesignDiploma")) {
                                                    if (email.getTrack().equals("Photoshop") && !Objects.requireNonNull(diploma).getPhotoshop().isEmpty()) {
                                                        ConfigureURL(diploma.getPhotoshop());
                                                    }
                                                    if (email.getTrack().equals("Illustrator") && !Objects.requireNonNull(diploma).getIllustrator().isEmpty()) {
                                                        ConfigureURL(diploma.getIllustrator());
                                                    }
                                                    if (email.getTrack().equals("InDesign") && !Objects.requireNonNull(diploma).getInDesign().isEmpty()) {
                                                        ConfigureURL(diploma.getInDesign());
                                                    }
                                                    if (email.getTrack().equals("PremierePro") && !Objects.requireNonNull(diploma).getPremierePro().isEmpty()) {
                                                        ConfigureURL(diploma.getPremierePro());
                                                    }
                                                    if (email.getTrack().equals("AfterEffect") && !Objects.requireNonNull(diploma).getAftereffects().isEmpty()) {
                                                        ConfigureURL(diploma.getAftereffects());
                                                    }
                                                    if (email.getTrack().equals("AdobeAudition") && !Objects.requireNonNull(diploma).getAdobeAudition().isEmpty()) {
                                                        ConfigureURL(diploma.getAdobeAudition());
                                                    }
                                                    if (email.getTrack().equals("AdobeXD") && !Objects.requireNonNull(diploma).getAdobeXD().isEmpty()) {
                                                        ConfigureURL(diploma.getAdobeXD());
                                                    }
                                                } else if (email.getTrack().equals("Photoshop") && !Objects.requireNonNull(diploma).getPhotoshop().isEmpty()) {
                                                    ConfigureURL(diploma.getPhotoshop());
                                                } else if (email.getTrack().equals("Illustrator") && !Objects.requireNonNull(diploma).getIllustrator().isEmpty()) {
                                                    ConfigureURL(diploma.getIllustrator());
                                                } else if (email.getTrack().equals("InDesign") && !Objects.requireNonNull(diploma).getInDesign().isEmpty()) {
                                                    ConfigureURL(diploma.getInDesign());
                                                } else if (email.getTrack().equals("PremierePro") && !Objects.requireNonNull(diploma).getPremierePro().isEmpty()) {
                                                    ConfigureURL(diploma.getPremierePro());
                                                } else if (email.getTrack().equals("AfterEffect") && !Objects.requireNonNull(diploma).getAftereffects().isEmpty()) {
                                                    ConfigureURL(diploma.getAftereffects());
                                                } else if (email.getTrack().equals("AdobeAudition") && !Objects.requireNonNull(diploma).getAdobeAudition().isEmpty()) {
                                                    ConfigureURL(diploma.getAdobeAudition());
                                                } else if (email.getTrack().equals("AdobeXD") && !Objects.requireNonNull(diploma).getAdobeXD().isEmpty()) {
                                                    ConfigureURL(diploma.getAdobeXD());
                                                }
                                            }
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                }

            }
        });
    }

    private void ConfigureURL(String track) {
        String PLAYLIST_GET_URLs;
        PLAYLIST_GET_URLs = "https://youtube.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=1000&playlistId=" + track + "&key=" + GOOGLE_YOUTUBE_API_KEY + "";
        new RequestApi().execute(PLAYLIST_GET_URLs);

    }

    private static ArrayList<YouTubeDataModel> mListData = new ArrayList<>();

    static OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(ArrayList<YouTubeDataModel> youTubeDataModels);
    }

    public class RequestApi extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {

            URL url = createUrl(urls[0]);
            try {
                String json = makeHttpRequest(url);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    mListData = parseVideoPlayListFromResponse(jsonObject);
//                    YouTubeDataModelArrayLists.add(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onItemClickListener.onItemClick(mListData); // get All ArrayList

            }

        }
    }


    public ArrayList<YouTubeDataModel> parseVideoPlayListFromResponse(JSONObject jsonObject) {
        ArrayList<YouTubeDataModel> mList = new ArrayList<>();

        if (jsonObject.has("items")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    if (json.has("id")) {
                        String video_id ;
                        if (json.has("kind")) {
                            if (json.getString("kind").equals("youtube#playlistItem")) { // for playList youtube#playlistItem
                                YouTubeDataModel youtubeObject = new YouTubeDataModel();
                                JSONObject jsonSnippet = json.getJSONObject("snippet");
                                JSONObject resourceId = jsonSnippet.getJSONObject("resourceId");
                                video_id = resourceId.getString("videoId");
                                String title = jsonSnippet.getString("title");
                                String description = jsonSnippet.getString("description");
                                String publishedAt = jsonSnippet.getString("publishedAt");
                                String thumbnail = "";
                                if (!jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url").equals("")) {
                                    thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");
                                }
                                youtubeObject.setTitle(title);
                                youtubeObject.setDescribtion(description);
                                youtubeObject.setPublishedAt(publishedAt);
                                youtubeObject.setThumnail(thumbnail);
                                youtubeObject.setVideo_id(video_id);
                                mList.add(youtubeObject);

                            }
                        }
                    }

                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }

        return mList;

    }


    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Main Fragment at 406", "Error Resopns Code");
            }
        } catch (IOException e) {
            Log.e("Main Fragment at 410", "Error Resopns Code" + e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
