package com.endava.smartdesk;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    //region Layouts
    @BindView(R.id.register_now_view)
    ConstraintLayout mRegisterNowView;

    @BindView(R.id.already_registered_view)
    ConstraintLayout mAlreadyRegisteredView;

    @BindView(R.id.leftView)
    ConstraintLayout mLeftView;

    @BindView(R.id.leftViewInfo)
    ConstraintLayout mLeftInfo;

    @BindView(R.id.rightView)
    ConstraintLayout mRightView;

    @BindView(R.id.rightViewInfo)
    ConstraintLayout mRightInfo;
    //endregion Layouts

    //region Registration fields
    @BindView(R.id.first_name)
    EditText mFirstName;

    @BindView(R.id.last_name)
    EditText mLastName;

    @BindView(R.id.company_name)
    EditText mCompanyName;

    @BindView(R.id.email_address)
    EditText mEmailAddress;

    @BindView(R.id.purpose)
    EditText mPurpose;

    @BindView(R.id.arrival_date)
    EditText mArrivalDate;

    @BindView(R.id.departure_date)
    EditText mDepartureDate;

    @BindView(R.id.signature)
    ImageView mSignature;
    //endregion Registration fields

    @BindView(R.id.authentication_code)
    EditText mAuthenticationCode;

    @BindView(R.id.already_registered_menu_button)
    Button mAlreadyRegisteredMenuButton;

    @BindView(R.id.register_now_menu_button)
    Button mRegisterNowMenuButton;

    @BindView(R.id.register_button)
    Button mRegisterButton;

    @BindView(R.id.continue_button)
    Button mContinueButton;

    SignatureFragment mSignatureFragment;

    private float mScreenWidthInDp;

    private boolean isUserSigned;
    private boolean isUserAlreadyRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAlreadyRegisteredMenuButton.setOnClickListener(v -> onAlreadyRegisteredButtonClick());
        mRegisterNowMenuButton.setOnClickListener(v -> onRegisterNowButtonClick());
        mRegisterButton.setOnClickListener(v -> finnishRegistration());
        mContinueButton.setOnClickListener(v -> continueRegistration());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAlreadyRegisteredMenuButton.setOnClickListener(null);
        mRegisterNowMenuButton.setOnClickListener(null);
        mRegisterButton.setOnClickListener(null);
    }

    private void continueRegistration() {
        if ("".contentEquals(mAuthenticationCode.getText()) || !isAuthenticationCodeValid()) {
            Toast.makeText(this, "Please provide a valid authentication code", Toast.LENGTH_SHORT).show();
        } else {
            isUserAlreadyRegistered = true;
            onRegisterNowButtonClick();
        }
    }

    private boolean isAuthenticationCodeValid() {
        Log.d("GABI", mAuthenticationCode.getText().toString());
        return mAuthenticationCode.getText().toString().equals("GABRIEL");
    }

    private void finnishRegistration() {
        if (isUserSigned) {
            if (areAllFieldsFilled()) {
                startActivity(new Intent(WelcomeActivity.this, RegistrationSuccessfulActivity.class));
            } else {
                Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
            }
        } else {
            mSignatureFragment = SignatureFragment.newInstance();
            mSignatureFragment.show(getSupportFragmentManager(), "Signature Fragment");
            mSignatureFragment.setOnSignListener(this::onSignComplete);
        }
    }

    private void onAlreadyRegisteredButtonClick() {
        moveSideView(mScreenWidthInDp / 3 * 2);
        moveRegisterNowView(0, -mScreenWidthInDp / 12 * 2);
        moveAlreadyRegisteredView(400, -mScreenWidthInDp / 12 * 2);
        moveLeftInfo(125, -140);
        moveRightInfo(500, -140);
    }

    private void onRegisterNowButtonClick() {
        moveSideView(-mScreenWidthInDp / 3 * 2);
        moveRegisterNowView(400, mScreenWidthInDp / 12 * 2);
        moveAlreadyRegisteredView(0, mScreenWidthInDp / 12 * 2);
        moveLeftInfo(500, 140);
        moveRightInfo(125, 140);
    }

    private void initViews() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float mScreenDensity = getResources().getDisplayMetrics().density;
        mScreenWidthInDp = outMetrics.widthPixels / mScreenDensity;

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mLeftView.getLayoutParams();
        params.width = (int) ((int) (mScreenWidthInDp / 3) * mScreenDensity);
        mLeftView.setLayoutParams(params);

        ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) mRightView.getLayoutParams();
        params1.width = (int) ((int) (mScreenWidthInDp / 3) * mScreenDensity);
        mRightView.setLayoutParams(params1);

        setViewsVisibility();
        isUserSigned = false;
        isUserAlreadyRegistered = false;
    }

    private void clearRegistrationFields() {
        mFirstName.getText().clear();
        mLastName.getText().clear();
        mCompanyName.getText().clear();
        mEmailAddress.getText().clear();
        mPurpose.getText().clear();
        mArrivalDate.getText().clear();
        mDepartureDate.getText().clear();
        mRegisterButton.setText(getResources().getString(R.string.get_signature));
        mSignature.setImageDrawable(null);
        isUserSigned = false;
    }

    private void fillFieldsWithDummyData() {
        mFirstName.setText("Gabriel");
        mLastName.setText("Blaj");
        mCompanyName.setText("Endava");
        mEmailAddress.setText("gabriel.blaj@endava.com");
        mPurpose.setText("Visiting");
        mArrivalDate.setText("17.06.2019");
        mDepartureDate.setText("27.06.2019");
    }

    private boolean areAllFieldsFilled() {
        if ("".contentEquals(mFirstName.getText())) {
            return false;
        } else if ("".contentEquals(mLastName.getText())) {
            return false;
        } else if ("".contentEquals(mCompanyName.getText())) {
            return false;
        } else if ("".contentEquals(mEmailAddress.getText())) {
            return false;
        } else if ("".contentEquals(mPurpose.getText())) {
            return false;
        } else if ("".contentEquals(mArrivalDate.getText())) {
            return false;
        } else if ("".contentEquals(mDepartureDate.getText())) {
            return false;
        } else {
            return true;
        }
    }

    private void clearAuthCodeField() {
        mAuthenticationCode.getText().clear();
    }

    private void setViewsVisibility() {
        mRightInfo.setVisibility(View.INVISIBLE);
        mRightView.setVisibility(View.INVISIBLE);
        mAlreadyRegisteredView.setVisibility(View.INVISIBLE);
        mRightInfo.animate().translationX(140).start();
    }

    private void moveRegisterNowView(int delay, float x) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> mRegisterNowView.animate()
                .translationXBy(x)
                .setDuration(400)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mRegisterNowView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (x < 0) {
                            mRegisterNowView.setVisibility(View.INVISIBLE);
                            clearRegistrationFields();
                        } else {
                            if (isUserAlreadyRegistered) {
                                fillFieldsWithDummyData();
                                isUserAlreadyRegistered = false;
                            }
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start(), delay);
    }

    private void moveAlreadyRegisteredView(int delay, float x) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> mAlreadyRegisteredView.animate()
                .translationXBy(x * 2)
                .setDuration(400)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mAlreadyRegisteredView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (x > 0) {
                            mAlreadyRegisteredView.setVisibility(View.INVISIBLE);
                            clearAuthCodeField();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start(), delay);
    }

    private void moveRightInfo(int delay, int x) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> mRightInfo.animate()
                .translationXBy(x)
                .setDuration(400)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mRightInfo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (x > 0) {
                            mRightInfo.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start(), delay);
    }

    private void moveLeftInfo(int delay, int x) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            mLeftInfo.animate()
                    .translationXBy(x)
                    .setDuration(400)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mLeftInfo.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (x < 0) {
                                mLeftInfo.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        }, delay);
    }

    private void moveSideView(float x) {
        mLeftView.animate()
                .translationXBy(x * 2)
                .setDuration(1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mRegisterNowMenuButton.setOnClickListener(null);
                        mAlreadyRegisteredMenuButton.setOnClickListener(null);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mRegisterNowMenuButton.setOnClickListener(v -> onRegisterNowButtonClick());
                        mAlreadyRegisteredMenuButton.setOnClickListener(v -> onAlreadyRegisteredButtonClick());
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    private void onSignComplete(String imagePath) {
        mRegisterButton.setText("REGISTER");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        mSignature.setImageBitmap(bitmap);
        mSignatureFragment.setOnSignListener(null);
        isUserSigned = true;
    }
}
