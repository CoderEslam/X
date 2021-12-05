package com.doubleclick.x_course;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.doubleclick.x_course.About.AboutFragment;
import com.doubleclick.x_course.Account.AccountFragment;
import com.doubleclick.x_course.Admin.AdminActivity;
import com.doubleclick.x_course.Chat.ViewPagerActivity;
import com.doubleclick.x_course.CustomNavigation.MainFragment;
import com.doubleclick.x_course.CustomNavigation.Menu.DrawerAdapter;
import com.doubleclick.x_course.CustomNavigation.Menu.DrawerItem;
import com.doubleclick.x_course.CustomNavigation.Menu.SimpleItem;
import com.doubleclick.x_course.Help.HelpFragment;
import com.doubleclick.x_course.PyChat.models.User;
import com.doubleclick.x_course.ViewModel.GraphicDesignViewModel;
import com.doubleclick.x_course.ViewModel.MobileViewModel;
import com.doubleclick.x_course.ViewModel.UserViewModel;
import com.doubleclick.x_course.ViewModel.WebViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationDrawerActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_MAIN_COURSE_FRAGMENT = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_CHAT = 2;
    private static final int POS_HELP = 3;
    private static final int POS_ABOUT = 4;
    private static final int POS_ADMIN = 5;
    private static final int POS_LOGOUT = 6;
    private FirebaseAuth mAuth;
    private UserViewModel userViewModel;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;
    private Toolbar toolbar;
    private String UserId;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private LottieAnimationView animation_no_wifi;
    private MobileViewModel mobileViewModel;
    private WebViewModel webViewModel;
    private GraphicDesignViewModel graphicDesignViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        animation_no_wifi = findViewById(R.id.animation_no_wifi);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null && networkInfo != null && networkInfo.isConnected()) {
            try {
                userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
                userViewModel.LoadById(mAuth.getCurrentUser().getUid().toString());
                animation_no_wifi.setVisibility(View.GONE);
                toolbar = findViewById(R.id.toolbar);
                toolbar.setTitle("Courses");
                setSupportActionBar(toolbar);
                userViewModel.getUserLiveData().observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        try {
                            MainFragment mainFragment = new MainFragment();
                            if (user.getEmail() != null) {
                                mainFragment.newInstance(user.getEmail(), user.getId(), user.getUsername(), user.getImage());
                                UserId = user.getId();
                            }
                            TextView nameProfile = findViewById(R.id.profileName);
                            CircleImageView profileImage = findViewById(R.id.profileImage);
                            nameProfile.setText(user.getUsername());
                            try {
                                if (user.getImage().equals("default")) {
                                    Toast.makeText(NavigationDrawerActivity.this, "put an image profile", Toast.LENGTH_LONG).show();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new AccountFragment()).commit();
                                } else {
                                    Glide.with(NavigationDrawerActivity.this).load(user.getImage())
                                            .placeholder(R.drawable.account_circle_24)
                                            .into(profileImage);
                                }
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(NavigationDrawerActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });


                slidingRootNav = new SlidingRootNavBuilder(this)
                        .withToolbarMenuToggle(toolbar)
                        .withMenuOpened(false)
                        .withContentClickableWhenMenuOpened(false)
                        .withSavedState(savedInstanceState)
                        .withMenuLayout(R.layout.menu_left_drawer)
                        .inject();

                screenIcons = loadScreenIcons();
                screenTitles = loadScreenTitles();
                DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                        createItemFor(POS_MAIN_COURSE_FRAGMENT).setChecked(true), //setChecked(true) => to set selected
                        createItemFor(POS_ACCOUNT),
                        createItemFor(POS_CHAT),
                        createItemFor(POS_HELP),
                        createItemFor(POS_ABOUT),
                        //new SpaceItem(48), // for space between items.
                        createItemFor(POS_ADMIN),
                        createItemFor(POS_LOGOUT)));
                adapter.setListener(this);

                RecyclerView list = findViewById(R.id.list);
                list.setNestedScrollingEnabled(false);
                list.setLayoutManager(new LinearLayoutManager(this));
                list.setAdapter(adapter);
                //to selected it during opening
                adapter.setSelected(POS_MAIN_COURSE_FRAGMENT);
            } catch (NullPointerException e) {
                Log.e("NavigationDrawer: ", e.getMessage());
            }
        } else {
            animation_no_wifi.setVisibility(View.VISIBLE);
        }


    }

   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mobile:
                mobileViewModel = new ViewModelProvider(this).get(MobileViewModel.class);
                mobileViewModel.getMobileData().observe(this, new Observer<ArrayList<Diploma>>() {
                    @Override
                    public void onChanged(ArrayList<Diploma> diplomas) {
                        Toast.makeText(NavigationDrawerActivity.this, "" + diplomas.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            case R.id.web:
                webViewModel = new ViewModelProvider(this).get(WebViewModel.class);
                webViewModel.getWebData().observe(this, new Observer<ArrayList<Diploma>>() {
                    @Override
                    public void onChanged(ArrayList<Diploma> diplomas) {
                        Toast.makeText(NavigationDrawerActivity.this, "" + diplomas.toString(), Toast.LENGTH_LONG).show();

                    }
                });
                return true;
            case R.id.graphic:
                graphicDesignViewModel = new ViewModelProvider(this).get(GraphicDesignViewModel.class);
                graphicDesignViewModel.getGraphicDesign().observe(this, new Observer<ArrayList<Diploma>>() {
                    @Override
                    public void onChanged(ArrayList<Diploma> diplomas) {
                        Toast.makeText(NavigationDrawerActivity.this, "" + diplomas.toString(), Toast.LENGTH_LONG).show();

                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }*/

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }*/

    @Override
    public void onItemSelected(int position) {
        if (networkInfo != null && networkInfo.isConnected()) {
            slidingRootNav.closeMenu();
            switch (position) {
                case POS_MAIN_COURSE_FRAGMENT:
                    MainFragment mainFragment = new MainFragment();
                    showFragment(mainFragment);
                    toolbar.setTitle("Courses");
                    break;
                case POS_ACCOUNT:
                    if (UserId != null) {
                        showFragment(new AccountFragment());
                        toolbar.setTitle("Account");
                    } else {
                        Toast.makeText(NavigationDrawerActivity.this, "Login first", Toast.LENGTH_LONG).show();
                    }
                    break;
                case POS_CHAT:
//                    if (UserId != null) {
                        Intent intentChat = new Intent(NavigationDrawerActivity.this, ViewPagerActivity.class);
//                        Intent intentChat = new Intent(NavigationDrawerActivity.this, SplashActivity.class);
                        startActivity(intentChat);
//                    } else {
//                        Toast.makeText(NavigationDrawerActivity.this, "Login first", Toast.LENGTH_LONG).show();
//                    }
                    break;
                case POS_HELP:
                    showFragment(new HelpFragment());
                    toolbar.setTitle("Help");
                    break;
                case POS_ABOUT:
                    showFragment(new AboutFragment());
                    toolbar.setTitle("About");
                    break;
                case POS_ADMIN:
                    Intent intentAdmin = new Intent(NavigationDrawerActivity.this, AdminActivity.class);
                    startActivity(intentAdmin);
                    break;
                case POS_LOGOUT:
                    mAuth.signOut();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;

            }
//        Fragment selectedScreen = MainFragment.createFor(screenTitles[position]);
//        showFragment(selectedScreen);
        }

    }

    private void showFragment(Fragment fragment) {
        if (networkInfo != null && networkInfo.isConnected()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        if (networkInfo != null && networkInfo.isConnected()) {
            return new SimpleItem(screenIcons[position], screenTitles[position])
                    .withIconTint(color(R.color.whiterGray))
                    .withTextTint(color(R.color.whiterGray))
                    .withSelectedIconTint(color(R.color.darkBlue))
                    .withSelectedTextTint(color(R.color.darkBlue));
        } else {
            return null;
        }

    }

    private String[] loadScreenTitles() {
        if (networkInfo != null && networkInfo.isConnected()) {
            return getResources().getStringArray(R.array.ld_activityScreenTitles);
        } else {
            return null;
        }
    }

    private Drawable[] loadScreenIcons() {
        if (networkInfo != null && networkInfo.isConnected()) {
            TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
            Drawable[] icons = new Drawable[ta.length()];
            for (int i = 0; i < ta.length(); i++) {
                int id = ta.getResourceId(i, 0);
                if (id != 0) {
                    icons[i] = ContextCompat.getDrawable(this, id);
                }
            }
            ta.recycle();
            return icons;
        } else {
            return null;
        }

    }

    @ColorInt
    private int color(@ColorRes int res) {
        if (networkInfo != null && networkInfo.isConnected()) {
            return ContextCompat.getColor(this, res);
        } else {
            return getResources().getColor(R.color.black);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}