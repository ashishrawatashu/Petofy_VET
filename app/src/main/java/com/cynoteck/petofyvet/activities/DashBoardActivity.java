package com.cynoteck.petofyvet.activities;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.cynoteck.petofyvet.R;
import com.cynoteck.petofyvet.fragments.AppointementFragment;
import com.cynoteck.petofyvet.fragments.HomeFragment;
import com.cynoteck.petofyvet.fragments.PetRegisterFragment;
import com.cynoteck.petofyvet.fragments.ProfileFragment;
import com.cynoteck.petofyvet.utils.Config;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout homeRL,profileRL,petregisterRL,appointmentRL;
    private ImageView icHome, icProfile, icPetRegister, icAppointment;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        init();
        SharedPreferences sharedPreferences = getSharedPreferences("userdetails", 0);
        Config.token = sharedPreferences.getString("token", "");
        Log.e("token",Config.token);


        if (savedInstanceState == null) {
            HomeFragment homeFragment = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_frame, homeFragment);
            ft.commit();
            icHome.setImageResource(R.drawable.home_green_icon);
        }

    }


    private void init() {
        homeRL = findViewById(R.id.homeRL);
        profileRL = findViewById(R.id.profileRL);
        petregisterRL=findViewById(R.id.petRegisterRL);
        appointmentRL=findViewById(R.id.appointmentRL);

        icHome=findViewById(R.id.icHome);
        icProfile = findViewById(R.id.icProfile);
        icPetRegister=findViewById(R.id.icPetRegister);
        icAppointment=findViewById(R.id.icAppointment);

        homeRL.setOnClickListener(this);
        profileRL.setOnClickListener(this);
        petregisterRL.setOnClickListener(this);
        appointmentRL.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.homeRL:
                icHome.setImageResource(R.drawable.home_green_icon);
                icProfile.setImageResource(R.drawable.profile_normal_icon);
                icPetRegister.setImageResource(R.drawable.pet_normal_icon);
                icAppointment.setImageResource(R.drawable.appointment_normal_icon);
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, homeFragment);
                ft.commit();

                break;

            case R.id.profileRL:
                icHome.setImageResource(R.drawable.home_normal_icon);
                icProfile.setImageResource(R.drawable.profile_green_icon);
                icPetRegister.setImageResource(R.drawable.pet_normal_icon);
                icAppointment.setImageResource(R.drawable.appointment_normal_icon);
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentTransaction ftProfile = getSupportFragmentManager().beginTransaction();
                ftProfile.replace(R.id.content_frame, profileFragment);
                ftProfile.commit();
                break;

            case R.id.petRegisterRL:
                icHome.setImageResource(R.drawable.home_normal_icon);
                icProfile.setImageResource(R.drawable.profile_normal_icon);
                icPetRegister.setImageResource(R.drawable.pet_green_icon);
                icAppointment.setImageResource(R.drawable.appointment_normal_icon);
                PetRegisterFragment petRegisterFragment = new PetRegisterFragment();
                FragmentTransaction ftPetRegister = getSupportFragmentManager().beginTransaction();
                ftPetRegister.replace(R.id.content_frame, petRegisterFragment);
                ftPetRegister.commit();
                break;

            case R.id.appointmentRL:
                icHome.setImageResource(R.drawable.home_normal_icon);
                icProfile.setImageResource(R.drawable.profile_normal_icon);
                icPetRegister.setImageResource(R.drawable.pet_normal_icon);
                icAppointment.setImageResource(R.drawable.appointment_green_icon);
                AppointementFragment appointementFragment = new AppointementFragment();
                FragmentTransaction ftAppointment = getSupportFragmentManager().beginTransaction();
                ftAppointment.replace(R.id.content_frame, appointementFragment);
                ftAppointment.commit();
                break;

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
/*        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count != 0) {
            HomeFragment homeFragment = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_frame, homeFragment);
            ft.addToBackStack(null);
            ft.commit();
            getSupportFragmentManager().popBackStack();
            icHome.setImageResource(R.drawable.home_green_icon);
            icProfile.setImageResource(R.drawable.profile_normal_icon);
            icPetRegister.setImageResource(R.drawable.pet_normal_icon);
            icAppointment.setImageResource(R.drawable.appointment_normal_icon);
            //additional code
        }
        else {*/
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finishAffinity();
                System.exit(0);
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

}
