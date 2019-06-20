package com.endava.smartdesk;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WelcomeActivity extends AppCompatActivity {

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
    private RetrofitClient retrofitClient;

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
        mRegisterNowMenuButton.setOnClickListener(v -> onRegisterNowButtonClick(null));
        mRegisterButton.setOnClickListener(v -> finnishRegistration());
        mContinueButton.setOnClickListener(v -> continueRegistration());
    }

    @Override
    protected void onDestroy() {
        mAlreadyRegisteredMenuButton.setOnClickListener(null);
        mRegisterNowMenuButton.setOnClickListener(null);
        mRegisterButton.setOnClickListener(null);
        super.onDestroy();
    }

    private void continueRegistration() {
        if (mAuthenticationCode.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please provide a valid authentication code", Toast.LENGTH_SHORT).show();
        } else {
            RetrofitServiceApi retrofitServiceApi = retrofitClient.getRetrofitClient();
            retrofitServiceApi.retrieveUserData("")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userData -> {
                        isUserAlreadyRegistered = true;
                        onRegisterNowButtonClick(userData);
                    }, throwable -> Toast.makeText(this, "Please provide a valid authentication code", Toast.LENGTH_SHORT).show());
            ;
        }
    }

    private void finnishRegistration() {
        if (isUserSigned) {
            if (areAllFieldsFilled()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date arrivalDate = dateFormat.parse(mArrivalDate.getText().toString());
                    Date departureDate = dateFormat.parse(mDepartureDate.getText().toString());
                    UserData userData = new UserData(mFirstName.getText().toString(),
                            mLastName.getText().toString(), mCompanyName.getText().toString(),
                            mEmailAddress.getText().toString(), mPurpose.getText().toString(),
                            arrivalDate.getTime(),
                            departureDate.getTime());
                    RetrofitServiceApi retrofitServiceApi = retrofitClient.getRetrofitClient();
                    retrofitServiceApi.sendAuthenticationCode(userData)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object -> startActivity(new Intent(WelcomeActivity.this, RegistrationSuccessfulActivity.class)),
                                    throwable -> Toast.makeText(WelcomeActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                } catch (ParseException e) {
                    Toast.makeText(WelcomeActivity.this, "Please entey a valid date.", Toast.LENGTH_SHORT).show();
                }
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
        moveSideView(1000, mScreenWidthInDp / 3 * 2);
        moveRegisterNowView(null, 500, 0, -mScreenWidthInDp / 12 * 2);
        moveAlreadyRegisteredView(500, 500, -mScreenWidthInDp / 12 * 2);
        moveLeftInfo(400, 125, -140);
        moveRightInfo(400, 500, -140);
    }

    private void onRegisterNowButtonClick(UserData userData) {
        moveSideView(1000, -mScreenWidthInDp / 3 * 2);
        moveRegisterNowView(userData, 500, 500, mScreenWidthInDp / 12 * 2);
        moveAlreadyRegisteredView(500, 0, mScreenWidthInDp / 12 * 2);
        moveLeftInfo(400, 500, 140);
        moveRightInfo(400, 125, 140);
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
        mRegisterButton.setText(getResources().getString(R.string.register_button_text));
        mSignature.setImageDrawable(null);
        isUserSigned = false;
    }

    private void fillFields(UserData userData) {
        if (userData != null) {
            mFirstName.setText("Gabriel");
            mLastName.setText("Blaj");
            mCompanyName.setText("Endava");
            mEmailAddress.setText("gabriel.blaj@endava.com");
            mPurpose.setText("Visiting");
            mArrivalDate.setText("17.06.2019");
            mDepartureDate.setText("27.06.2019");
        } else {
            Toast.makeText(this, "Ooops! Something went wrong ...", Toast.LENGTH_SHORT).show();
        }
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

    private void moveRegisterNowView(UserData userData, int duration, int delay, float x) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> mRegisterNowView.animate()
                .translationXBy(x)
                .setDuration(duration)
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
                                fillFields(userData);
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

    private void moveAlreadyRegisteredView(int duration, int delay, float x) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> mAlreadyRegisteredView.animate()
                .translationXBy(x * 2)
                .setDuration(duration)
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

    private void moveRightInfo(int duration, int delay, int x) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> mRightInfo.animate()
                .translationXBy(x)
                .setDuration(duration)
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

    private void moveLeftInfo(int duration, int delay, int x) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            mLeftInfo.animate()
                    .translationXBy(x)
                    .setDuration(duration)
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

    private void moveSideView(int duration, float x) {
        mLeftView.animate()
                .translationXBy(x * 2)
                .setDuration(duration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mRegisterNowMenuButton.setOnClickListener(null);
                        mAlreadyRegisteredMenuButton.setOnClickListener(null);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mRegisterNowMenuButton.setOnClickListener(v -> onRegisterNowButtonClick(null));
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
