package mpower.org.elearning_module.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindView;
import mpower.org.elearning_module.BaseActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.fragments.BaseFragment;
import mpower.org.elearning_module.utils.AppConstants;

/**
 * Created by sabbir on 11/30/17.
 *
 * @author sabbir (sabbir@mpowe-social.com)
 */

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.btn_rate)
    Button btnRate;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.tv_thank_you)
    TextView tvThankYou;


    @Override
    protected int getResourceLayout() {
        return R.layout.feedback_fragment;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                tvThankYou.setVisibility(View.VISIBLE);



            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int r=0;
                try {
                    r =Math.round(ratingBar.getRating());
                }catch (Exception e){

                }
                finally {
                    sendResultBack(r);
                }
            }
        });
    }

    private void sendResultBack(int r) {
        Intent intent=new Intent();
        intent.putExtra(AppConstants.FEEDBACK_RATING,r);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }
}
