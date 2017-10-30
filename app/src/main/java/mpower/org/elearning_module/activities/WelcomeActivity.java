package mpower.org.elearning_module.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mpower.org.elearning_module.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

//individual onclick handler for buttons ,using just to not use button ids and other view referencing
    public void callLogin(View view) {
        Intent intent=new Intent(this,LogInActivity.class);
        startActivity(intent);
    }

    public void callSignUp(View view) {
        Intent intent=new Intent(this,UserTypeSelectionActivity.class);
        startActivity(intent);
    }
}
