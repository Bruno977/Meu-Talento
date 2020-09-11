package com.example.meutalento.BottomNavigation;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.meutalento.Home.HomeActivity;
import com.example.meutalento.New.NewActivity;
import com.example.meutalento.Profile.ProfileActivity;
import com.example.meutalento.R;
import com.example.meutalento.Search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationViewHelper {

    public static void enableNavigation(final Context context, BottomNavigationView view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){

                        case R.id.ic_home:
                            Intent intent1 = new Intent(context, HomeActivity.class);
                            context.startActivity(intent1);
                            break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent(context, SearchActivity.class);
                        context.startActivity(intent2);
                        break;

                    case  R.id.ic_add:
                        Intent intent3 = new Intent(context, NewActivity.class);
                        context.startActivity(intent3);
                        break;

                    case  R.id.ic_profile:
                        Intent intent4 = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent4);
                        break;
                }

                return false;
            }
        });
    }
}