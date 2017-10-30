package mpower.org.elearning_module.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.Helper;

public class SignUpActivity extends AppCompatActivity {

    boolean isDotProvider=false;
    @BindView(R.id.linear_layout_DOT)
    LinearLayout linearLayoutDOT;
    @BindView(R.id.et_dot_id)
    EditText editTextDOTId;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_phone)
    EditText etPhone;

    private String name,phone,dotId,password;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
       progressDialog=new ProgressDialog(this);
       progressDialog.setMessage("Signing Up...Please wait");
        if (getIntent().getExtras()!=null){
            isDotProvider=getIntent().getBooleanExtra(AppConstants.USER_TYPE,false);
        }
        if (isDotProvider){
            linearLayoutDOT.setVisibility(View.VISIBLE);
        }


    }

    public void signUp(View view) {
        if (checkDataValidation()){

        }else {

        }
        Helper.showToast(this,"Sign Up", Toast.LENGTH_LONG);
    }

    private boolean checkDataValidation() {
        name=etName.getText().toString();
        phone=etPhone.getText().toString();
        password=etPassword.getText().toString();
        dotId=editTextDOTId.getText().toString();

        if (name==null || name.equalsIgnoreCase("")){
            Helper.showToast(this,"Name can't be empty",Toast.LENGTH_LONG);
            return false;
        }else if (phone==null || phone.equalsIgnoreCase("") || phone.length()<11 || !TextUtils.isDigitsOnly(phone)){
            Helper.showToast(this,"Phone No. is not valid",Toast.LENGTH_LONG);
            return false;
        }else if (isDotProvider && dotId.isEmpty()){
            Helper.showToast(this,"DOT Id is not valid",Toast.LENGTH_LONG);
            return false;
        }else if (password==null || password.isEmpty()){
            Helper.showToast(this,"Password is not valid",Toast.LENGTH_LONG);
            return false;
        }

        return true;
    }

    private class SignUpTask extends AsyncTask<Void,Void,Boolean>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean){
                progressDialog.dismiss();
                Helper.showToast(SignUpActivity.this,"Successfully Signed Up,Now Log In",Toast.LENGTH_LONG);
                callLogin();
            }else {
                progressDialog.dismiss();
                //TODO have to handle the errors later
                Helper.showToast(SignUpActivity.this,"Failed,Try again later",Toast.LENGTH_LONG);
            }

        }
    }

    private void callLogin() {
        Intent intent=new Intent(this,LogInActivity.class);
        startActivity(intent);
        finish();
    }
}
