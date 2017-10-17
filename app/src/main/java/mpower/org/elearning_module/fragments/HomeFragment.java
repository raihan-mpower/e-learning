package mpower.org.elearning_module.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import mpower.org.elearning_module.R;
import mpower.org.elearning_module.utils.Helper;

/**
 * Created by sabbir on 4/10/17.
 * @author Sabbir ,sabbir@mpower-social.com
 */

public class HomeFragment extends Fragment implements View.OnClickListener{
    private ImageView mProfileImage;
    private static final int PICK_PHOTO_FOR_AVATAR =99 ;

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("Home");
        //   ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_settings,container,false);


        //  optionSpinner = (Spinner) view.findViewById(R.id.spinner);
        mProfileImage=(ImageView) view.findViewById(R.id.profile_image);

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageForProfilePic();
              //  pickImageAnother();
            }
        });

        setProfileInfo();
        return view;
        // return inflater.inflate(R.layout.activity_settings,container,false);
    }

    private void pickImageForProfilePic() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity())
                .setTitle("Add Profile Photo")
                .setMessage("Pick an image for profile picture?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pickImageAnother();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (v.getId()){
            case R.id.buttonAnimalDetail:

                break;
        }
    }

    private void setProfileInfo() {
        SharedPreferences preferences=getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
        String name=preferences.getString("name","");
        String phone =preferences.getString("phone","");
        //String gps=preferences.getString("latitude","")+" , "+preferences.getString("longitude","");
        String add=preferences.getString("division","")+","+preferences.getString("upzilla","")+","+
                preferences.getString("district","")+","+preferences.getString("union","")+","+preferences.getString("village","");

        String gps=preferences.getString("location","");
        Log.i("gpsLoc",gps);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            /*try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                //  BitmapDrawable bitmapDrawable=BitmapDrawable.createFromStream(inputStream,"name");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                Bitmap newProfilePic = extras.getParcelable("data");
                mProfileImage.setImageBitmap(new Helper().getCroppedBitmap(newProfilePic));
            }
        }
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }


    /**
     *
     * defining types for the intent will result a cropped image whatever the user picked it kinda
     * improves performance because Bitmaps can be very large !
    * */
    public void pickImageAnother() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }


}
