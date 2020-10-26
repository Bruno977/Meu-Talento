package com.example.meutalento.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meutalento.Outros.BottomNavigationViewHelper;
import com.example.meutalento.Outros.FirebaseMethods;
import com.example.meutalento.Outros.GridImageAdapter;
import com.example.meutalento.Outros.UniversalImageLoader;
import com.example.meutalento.R;
import com.example.meutalento.models.Photo;
import com.example.meutalento.models.UserAccountSettings;
import com.example.meutalento.models.UserSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private static final int ACTIVITY_NUM = 3;
    private static final int NUM_GRID_COLUMNS = 2;
    private Context mContext = ProfileActivity.this;

    private ProgressBar mProgressBar;
    private ImageView profilePhoto;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;


    private TextView mDisplayName, mUsername, mDescription;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationView bottomNavigationView;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDisplayName = findViewById(R.id.display_name);
        mUsername = findViewById(R.id.username);
        mDescription = findViewById(R.id.description);
        mProfilePhoto = findViewById(R.id.profile_photo);
        mProgressBar = findViewById(R.id.profileProgressBar);
        gridView = findViewById(R.id.gridView);
        toolbar = findViewById(R.id.profileToolbar);
        profileMenu = findViewById(R.id.profileMenu);
        bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        mFirebaseMethods = new FirebaseMethods(this);

        setupBottomNavigationView();
//        setupActivityWidgets();
//        setProfileImage();
//
//        tempGridSetup();

        setupFirebaseAuth();
        setupGridView();

    }
    private void setupGridView(){

        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    photos.add(singleSnapshot.getValue(Photo.class));
                }
                //setup our image grid
                int gridWidth = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridWidth/NUM_GRID_COLUMNS;
                gridView.setColumnWidth(imageWidth);

                ArrayList<String> imgUrls = new ArrayList<String>();
                for(int i = 0; i < photos.size(); i++){
                    imgUrls.add(photos.get(i).getImage_path());
                }
                GridImageAdapter adapter = new GridImageAdapter(mContext,R.layout.layout_grid_imageview,
                        "", imgUrls);
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setProfileWidgets(UserSettings userSettings){

        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();

        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");

        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mProgressBar.setVisibility(View.GONE);
    }

//    private void tempGridSetup(){
//        ArrayList<String> imgURLs = new ArrayList<>();
//        imgURLs.add("https://pbs.twimg.com/profile_images/616076655547682816/6gMRtQyY.jpg");
//        imgURLs.add("https://i.redd.it/9bf67ygj710z.jpg");
//        imgURLs.add("https://c1.staticflickr.com/5/4276/34102458063_7be616b993_o.jpg");
//        imgURLs.add("http://i.imgur.com/EwZRpvQ.jpg");
//        imgURLs.add("http://i.imgur.com/JTb2pXP.jpg");
//        imgURLs.add("https://i.redd.it/59kjlxxf720z.jpg");
//        imgURLs.add("https://i.redd.it/pwduhknig00z.jpg");
//        imgURLs.add("https://i.redd.it/clusqsm4oxzy.jpg");
//        imgURLs.add("https://i.redd.it/svqvn7xs420z.jpg");
//        imgURLs.add("http://i.imgur.com/j4AfH6P.jpg");
//        imgURLs.add("https://i.redd.it/89cjkojkl10z.jpg");
//        imgURLs.add("https://i.redd.it/aw7pv8jq4zzy.jpg");
//
//        setupImageGrid(imgURLs);
//    }

//    private void setupImageGrid(ArrayList<String> imgURLs){
//        GridView gridView = (GridView) findViewById(R.id.gridView);
//
//        int gridWidth = getResources().getDisplayMetrics().widthPixels;
//        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
//        gridView.setColumnWidth(imageWidth);
//
//        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", imgURLs);
//        gridView.setAdapter(adapter);
//    }
//    private void setProfileImage(){
//        String imgURL = "www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf";
//        UniversalImageLoader.setImage(imgURL, profilePhoto, mProgressBar, "https://");
//    }
//    private void setupActivityWidgets(){
//        mProgressBar = findViewById(R.id.profileProgressBar);
//        mProgressBar.setVisibility(View.GONE);
//        profilePhoto = findViewById(R.id.profile_photo);
//
//    }
    private void setupBottomNavigationView(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(mContext, this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.profile_menu);
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile:
                Intent intent1 = new Intent(this, EditProfileActivity.class);
                startActivity(intent1);
                this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.sign_out:
                Intent intent2 = new Intent(this, SignOutActivity.class);
                startActivity(intent2);
                return true;
            default:
                return false;
        }
    }

     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));


                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
