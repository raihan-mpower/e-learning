package mpower.org.elearning_module.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import mpower.org.elearning_module.R;
import mpower.org.elearning_module.utils.AppConstants;

public class UserTypeSelectionActivity extends AppCompatActivity {
    private boolean isDotProvider=false;
    private RadioGroup userTypeGroup;
    private String userType=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_selection);
        userTypeGroup=findViewById(R.id.user_group);
        int selectedRadioId=userTypeGroup.getCheckedRadioButtonId();
        if (selectedRadioId!=-1){
            RadioButton radioButton= findViewById(selectedRadioId);
            if (radioButton!=null){
                userType=radioButton.getText().toString();
                //Toast.makeText(this,"UserType "+userType,Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this,"Please Select User Type",Toast.LENGTH_LONG).show();
            return;
        }
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
}
