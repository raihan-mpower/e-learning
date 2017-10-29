package mpower.org.elearning_module.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.Helper;

public class SignUpActivity extends AppCompatActivity {

    boolean isDotProvider=false;
    @BindView(R.id.linear_layout_DOT)
    LinearLayout linearLayoutDOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (getIntent().getExtras()!=null){
            isDotProvider=getIntent().getBooleanExtra(AppConstants.USER_TYPE,false);
        }
        if (isDotProvider){
            linearLayoutDOT.setVisibility(View.VISIBLE);
        }

    }

    public void signUp(View view) {
        Helper.showToast(this,"Sign Up", Toast.LENGTH_LONG);
    }
}
