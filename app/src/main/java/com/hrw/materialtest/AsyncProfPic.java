package com.hrw.materialtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;

/**
 * Created by Wu on 2014/12/20.
 */
public class AsyncProfPic {
    private static final String TAG = "AsyncProfPic";
    private GoogleApiClient mGoogleApiClient;
    private Context context;
    private Bitmap bitmap;
    private ImageView imageView;

    AsyncProfPic(GoogleApiClient mGoogleApiClient, Context context, Bitmap bitmap, ImageView imageView){
        this.mGoogleApiClient = mGoogleApiClient;
        this.context = context;
        this.bitmap = bitmap;
        this.imageView = imageView;
    }
    /**
     * Fetching user's information name, email, profile pic
     * */
    public void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + 400;

                new LoadProfileImage(bitmap, imageView, context).execute(personPhotoUrl);

            } else {
                Toast.makeText(context,"Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap;
        ImageView imageView;
        Context context;
        RoundedImageView roundedImageView;

        public LoadProfileImage(Bitmap bitmap1, ImageView imageView, Context context1) {
            this.bitmap = bitmap1;
            this.imageView = imageView;
            this.context = context1;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bitmap = result;
            roundedImageView = new RoundedImageView(context);
            imageView.setImageBitmap(roundedImageView.getCroppedBitmap(result,100));
        }
    }
}
