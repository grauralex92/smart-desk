package com.endava.smartdesk.view.activities;

import android.animation.Animator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.endava.smartdesk.R;
import com.endava.smartdesk.database.DbInitialization;
import com.endava.smartdesk.database.SmartDeskDataBase;
import com.endava.smartdesk.database.model.DbRegistrationData;
import com.endava.smartdesk.database.model.DbUserData;
import com.endava.smartdesk.model.UserData;
import com.endava.smartdesk.networking.RetrofitClient;
import com.endava.smartdesk.utils.UserDataMapper;
import com.endava.smartdesk.view.fragments.SignatureFragment;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.endava.smartdesk.utils.KeyboardUtils.hideSoftKeyboard;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.main_view)
    ConstraintLayout mView;

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
    AutoCompleteTextView mFirstName;

    @BindView(R.id.last_name)
    AutoCompleteTextView mLastName;

    @BindView(R.id.company_name)
    AutoCompleteTextView mCompanyName;

    @BindView(R.id.email_address)
    EditText mEmailAddress;

    @BindView(R.id.badge_number)
    EditText mBadgeNumber;

    @BindView(R.id.purpose)
    EditText mPurpose;

    @BindView(R.id.arrival_date)
    EditText mArrivalDate;

    @BindView(R.id.departure_date)
    EditText mDepartureDate;

    @BindView(R.id.signature)
    ImageView mSignature;

    @BindView(R.id.authentication_code)
    EditText mRegistrationCode;

    @BindView(R.id.already_registered_menu_button)
    Button mAlreadyRegisteredMenuButton;

    @BindView(R.id.register_now_menu_button)
    Button mRegisterNowMenuButton;

    @BindView(R.id.register_button)
    Button mRegisterButton;

    @BindView(R.id.continue_button)
    Button mContinueButton;

    public static SmartDeskDataBase mDb;

    private float mScreenWidthInPixels;
    private boolean isUserSigned;
    private boolean isUserAlreadyRegistered;
    private SignatureFragment mSignatureFragment;
    private RetrofitClient retrofitClient;
    private UserDataMapper mUserDataMapper;
    private String mDateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);
        initDB();
        initViews();
        setupUI(mView);
        mUserDataMapper = new UserDataMapper();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAlreadyRegisteredMenuButton.setOnClickListener(v -> onSideMenuClick(true, null));
        mRegisterNowMenuButton.setOnClickListener(v -> onSideMenuClick(false, null));
        mRegisterButton.setOnClickListener(v -> finnishRegistration());
        mContinueButton.setOnClickListener(v -> continueRegistration());
        mArrivalDate.setOnClickListener(v -> getDate(mArrivalDate));
        mDepartureDate.setOnClickListener(v -> getDate(mDepartureDate));
        mLastName.setOnItemClickListener((parent, view, position, id) -> {
            String lastName = parent.getItemAtPosition(position).toString();
            DbUserData user = mDb.userDataModel().getUserDataByLastName(lastName);
            if (user != null) {
                if (user.firstName.equals(mFirstName.getText().toString())) {
                    fillFields(mUserDataMapper.convertToUserData(user), true);
                }
            }
        });
        mRegistrationCode.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard(WelcomeActivity.this);
                return true;
            }
            return false;
        });
    }

    public void setupUI(View view) {
        if (!(view instanceof EditText) && !(view instanceof Button)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard(WelcomeActivity.this);
                return false;
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void getDate(EditText dateField) {
        mDateAndTime = "";
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog picker = new DatePickerDialog(WelcomeActivity.this,
                (datePickerView, newYear, newMonth, newDay) -> {
                    mDateAndTime = newDay + "/" + (newMonth + 1) + "/" + newYear;
                    TimePickerDialog timePickerDialog = new TimePickerDialog(WelcomeActivity.this,
                            (timePickerView, newHour, newMinute) -> {
                                mDateAndTime = mDateAndTime + " " + newHour + ":" + newMinute;
                                dateField.setText(mDateAndTime);
                            }, hour, minute, false);
                    timePickerDialog.show();
                }, year, month, day);
        picker.show();
    }

    private String setDate(Date arrivalDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(arrivalDate);

        return c.get(Calendar.YEAR) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.DAY_OF_MONTH) + " "
                + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
    }

    private void initDB() {
        mDb = SmartDeskDataBase.getDatabase(getApplicationContext());
        if (mDb.userDataModel().getUserDataSize() == 0 && mDb.registrationDataDao().getRegistrationDataSize() == 0) {
            DbInitialization.populeDb(mDb);
        }
        List<DbRegistrationData> registrationDataList = mDb.registrationDataDao().getAllRegistrationData();
        for (int i = 0; i < registrationDataList.size(); i++) {
            Log.d("GABI", registrationDataList.get(i).email + " : " +
                    registrationDataList.get(i).registrationCode);
        }
    }

    void startAnimation(ConstraintLayout view, Animator.AnimatorListener animatorListener, int duration, int delay, float x) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> view.animate()
                .translationXBy(x)
                .setDuration(duration)
                .setListener(animatorListener)
                .start(), delay);
    }

    private void onSideMenuClick(boolean isSideMenuOnLeft, UserData userData) {
        int flag = (isSideMenuOnLeft) ? -1 : 1;
        int registerNowViewDelay = (isSideMenuOnLeft) ? 75 : 450;
        int alreadyRegisteredViewDelay = (isSideMenuOnLeft) ? 450 : 75;
        int leftInfoDelay = (isSideMenuOnLeft) ? 125 : 500;
        int rightInfoDelay = (isSideMenuOnLeft) ? 500 : 125;

        startAnimation(mLeftView, getSideViewAnimatorListener(), 1000, 0, -1 * flag * mScreenWidthInPixels / 3 * 2);
        startAnimation(mRegisterNowView, getRegisterNowViewAnimatorListener(flag * mScreenWidthInPixels / 9, userData),
                450, registerNowViewDelay, flag * mScreenWidthInPixels / 9);
        startAnimation(mAlreadyRegisteredView, getAlreadyRegisteredAnimatorListener(flag * mScreenWidthInPixels / 6),
                450, alreadyRegisteredViewDelay, flag * mScreenWidthInPixels / 6);
        startAnimation(mLeftInfo, getLeftInfoAnimatorListener(flag * mScreenWidthInPixels / 14),
                400, leftInfoDelay, flag * mScreenWidthInPixels / 14);
        startAnimation(mRightInfo, getRightInfoAnimatorListener(flag * mScreenWidthInPixels / 14),
                400, rightInfoDelay, flag * mScreenWidthInPixels / 14);
    }

    private void initViews() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        mScreenWidthInPixels = outMetrics.widthPixels;
        setViewsWidth(mLeftView);
        setViewsWidth(mRightView);
        setViewsWidth(mAlreadyRegisteredView);
        setViewsWidth(mRegisterNowView);

        setViewsVisibility();

        setAutoCompleteViews(mFirstName, mDb.firstNameMiningDao().getAllFirstNames());
        setAutoCompleteViews(mLastName, mDb.lastNameMiningDao().getAllLastNames());
        setAutoCompleteViews(mCompanyName, mDb.companyMiningDao().getAllCompanyNames());

        isUserSigned = false;
        isUserAlreadyRegistered = false;

        mArrivalDate.setInputType(InputType.TYPE_NULL);
        mDepartureDate.setInputType(InputType.TYPE_NULL);
    }

    private void setAutoCompleteViews(AutoCompleteTextView view, List<String> autoCompleteList) {
        List<String> uniqueValueList = new ArrayList<>(new HashSet<>(autoCompleteList));

        ArrayAdapter<String> firstNameAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, uniqueValueList);
        view.setThreshold(1);
        view.setAdapter(firstNameAdapter);
    }

    private void setViewsWidth(ConstraintLayout layout) {
        ConstraintLayout.LayoutParams viewParams = (ConstraintLayout.LayoutParams) layout.getLayoutParams();
        viewParams.width = (int) (mScreenWidthInPixels / 3);
        layout.setLayoutParams(viewParams);
    }

    private void continueRegistration() {
        String registrationCode = mRegistrationCode.getText().toString();
        if (registrationCode.isEmpty()) {
            Toast.makeText(this, "Please provide a valid registration code", Toast.LENGTH_SHORT).show();
        } else {
            DbRegistrationData registrationData = mDb.registrationDataDao().getRegistrationData(registrationCode);
            if (registrationData != null) {
                isUserAlreadyRegistered = true;
                DbUserData dbUserData = mDb.userDataModel().getUserDataByEmail(registrationData.email);
                onSideMenuClick(false, mUserDataMapper.convertToUserData(dbUserData));
            } else {
                Toast.makeText(this, "Please provide a valid registration code", Toast.LENGTH_SHORT).show();
            }
        }
        //Retrofit
//            RetrofitServiceApi retrofitServiceApi = retrofitClient.getRetrofitClient();
//            retrofitServiceApi.retrieveUserData("")
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(userData -> {
//                        isUserAlreadyRegistered = true;
//                        onSideMenuClick(false, userData);
//                    }, throwable -> Toast.makeText(this, "Please provide a valid authentication code", Toast.LENGTH_SHORT).show());
//        }
    }

    private void finnishRegistration() {
        if (isUserSigned) {
            if (areAllFieldsFilled()) {
                startActivity(new Intent(WelcomeActivity.this, RegistrationSuccessfulActivity.class));

                //Retrofit
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                try {
//                    Date arrivalDate = dateFormat.parse(mArrivalDate.getText().toString());
//                    Date departureDate = dateFormat.parse(mDepartureDate.getText().toString());
//                    UserData userData = new UserData(mFirstName.getText().toString(),
//                            mLastName.getText().toString(), mCompanyName.getText().toString(),
//                            mEmailAddress.getText().toString(), mBadgeNumber.getText().toString(),
//                            mPurpose.getText().toString(), arrivalDate.getTime(), departureDate.getTime());
//                    RetrofitServiceApi retrofitServiceApi = retrofitClient.getRetrofitClient();
//                    retrofitServiceApi.sendAuthenticationCode(userData)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(object -> startActivity(new Intent(WelcomeActivity.this, RegistrationSuccessfulActivity.class)),
//                                    throwable -> Toast.makeText(WelcomeActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
//                } catch (ParseException e) {
//                    Toast.makeText(WelcomeActivity.this, "Please entey a valid date.", Toast.LENGTH_SHORT).show();
//                }
            } else {
                Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
            }
        } else {
            mSignatureFragment = SignatureFragment.newInstance();
            mSignatureFragment.show(getSupportFragmentManager(), "Signature Fragment");
            mSignatureFragment.setOnSignListener(this::onSignComplete);
        }
    }

    private void clearRegistrationFields() {
        mFirstName.getText().clear();
        mLastName.getText().clear();
        mCompanyName.getText().clear();
        mEmailAddress.getText().clear();
        mBadgeNumber.getText().clear();
        mPurpose.getText().clear();
        mArrivalDate.getText().clear();
        mDepartureDate.getText().clear();
        mRegisterButton.setText(getResources().getString(R.string.register_button_text));
        mSignature.setImageDrawable(null);
        isUserSigned = false;
    }

    private void fillFields(UserData userData, boolean fillPartially) {
        if (userData != null) {
            mFirstName.setText(userData.getFirstName());
            mLastName.setText(userData.getLastName());
            mCompanyName.setText(userData.getCompanyName());
            mEmailAddress.setText(userData.getEmail());
            if (!fillPartially) {
                mBadgeNumber.setText(userData.getBadgeNumber());
                mPurpose.setText(userData.getPurpose());
                mArrivalDate.setText(setDate(userData.getArrivalDate()));
                mDepartureDate.setText(setDate(userData.getDepartureDate()));
            }
        } else {
            Toast.makeText(this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
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
        } else if ("".contentEquals(mBadgeNumber.getText())) {
            return false;
        } else if ("".contentEquals(mPurpose.getText())) {
            return false;
        } else if ("".contentEquals(mArrivalDate.getText())) {
            return false;
        } else return !"".contentEquals(mDepartureDate.getText());
    }

    private void clearAuthCodeField() {
        mRegistrationCode.getText().clear();
    }

    private void setViewsVisibility() {
        mRightInfo.animate().translationX(mScreenWidthInPixels / 14).start();
        mRightInfo.setVisibility(View.INVISIBLE);
        mRightView.setVisibility(View.INVISIBLE);
        mAlreadyRegisteredView.setVisibility(View.INVISIBLE);
    }


    Animator.AnimatorListener getSideViewAnimatorListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mRegisterNowMenuButton.setOnClickListener(null);
                mAlreadyRegisteredMenuButton.setOnClickListener(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRegisterNowMenuButton.setOnClickListener(v -> onSideMenuClick(false, null));
                mAlreadyRegisteredMenuButton.setOnClickListener(v -> onSideMenuClick(true, null));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    Animator.AnimatorListener getRegisterNowViewAnimatorListener(float x, UserData userData) {
        return new Animator.AnimatorListener() {
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
                        fillFields(userData, false);
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
        };
    }

    Animator.AnimatorListener getAlreadyRegisteredAnimatorListener(float x) {
        return new Animator.AnimatorListener() {
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
        };
    }

    Animator.AnimatorListener getRightInfoAnimatorListener(float x) {
        return new Animator.AnimatorListener() {
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
        };
    }

    Animator.AnimatorListener getLeftInfoAnimatorListener(float x) {
        return new Animator.AnimatorListener() {
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
        };
    }

    private void onSignComplete(String imagePath) {
        mRegisterButton.setText("REGISTER");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        mSignature.setImageBitmap(bitmap);
        mSignatureFragment.setOnSignListener(null);
        isUserSigned = true;
    }

    @Override
    protected void onDestroy() {
        mAlreadyRegisteredMenuButton.setOnClickListener(null);
        mRegisterNowMenuButton.setOnClickListener(null);
        mRegisterButton.setOnClickListener(null);
        super.onDestroy();
    }
}
