package mpower.org.elearning_module.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import mpower.org.elearning_module.BaseActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.utils.AppConstants;

public class UserTypeSelectionActivity extends BaseActivity {

    private boolean isDotProvider=false;


    @Override
    protected int getResourceLayout() {
        return R.layout.activity_user_type_selection;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {

    }

    public void dotSignUp(View view) {
        isDotProvider=true;
        Intent intent=new Intent(this,SignUpActivity.class);
        intent.putExtra(AppConstants.USER_TYPE,isDotProvider);
        startActivity(intent);
        finish();
    }

    public void otherSignUp(View view) {
        isDotProvider=false;
        Intent intent=new Intent(this,SignUpActivity.class);
        intent.putExtra(AppConstants.USER_TYPE,isDotProvider);
        startActivity(intent);
        finish();
    }

    public void callLogin(View view) {
        Intent intent=new Intent(this,LogInActivity.class);
        startActivity(intent);
        finish();
    }
}
