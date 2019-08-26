package com.endava.smartdesk.view.activities;

import android.animation.Animator;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.endava.smartdesk.R;
import com.endava.smartdesk.database.DbInitialization;
import com.endava.smartdesk.database.SmartDeskDataBase;
import com.endava.smartdesk.database.model.DbRegistrationData;
import com.endava.smartdesk.model.GuestUserData;
import com.endava.smartdesk.model.InnovationLabsUserData;
import com.endava.smartdesk.model.RegistrationData;
import com.endava.smartdesk.model.RegistrationForm;
import com.endava.smartdesk.model.SummerPartyUserData;
import com.endava.smartdesk.networking.RetrofitClient;
import com.endava.smartdesk.utils.UserDataMapper;
import com.endava.smartdesk.view.fragments.BaseRegistrationFragment;
import com.facebook.stetho.Stetho;

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

    @BindView(R.id.authentication_code)
    EditText mRegistrationCode;

    @BindView(R.id.already_registered_menu_button)
    Button mAlreadyRegisteredMenuButton;

    @BindView(R.id.register_now_menu_button)
    Button mRegisterNowMenuButton;

    @BindView(R.id.continue_button)
    Button mContinueButton;

    @BindView(R.id.form_icon)
    ImageView mFormIcon;

    private NavHostFragment mNestedNav;
    public static SmartDeskDataBase mDb;

    private float mScreenWidthInPixels;
    private boolean isUserAlreadyRegistered;
    private RetrofitClient retrofitClient;
    private UserDataMapper mUserDataMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);
        initDB();
        initViews();
        setupKeyboard(mView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAlreadyRegisteredMenuButton.setOnClickListener(v -> onSideMenuClick(true, null));
        mRegisterNowMenuButton.setOnClickListener(v -> onSideMenuClick(false, null));
        mContinueButton.setOnClickListener(v -> continueRegistration());
        mRegistrationCode.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard(WelcomeActivity.this);
                return true;
            }
            return false;
        });
        mFormIcon.setOnClickListener(view -> displayFormDialog());
    }

    private void displayFormDialog() {
        int i = 0;
        String[] forms = new String[RegistrationForm.values().length];
        for (RegistrationForm form : RegistrationForm.values()) {
            forms[i] = form.getFormName();
            i++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this, R.style.AlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.registration_form_dialog_title));
        builder.setItems(forms, (dialog, which) ->
                changeForm(forms[which]));
        builder.show();
    }

    private void changeForm(String formName) {
        if (formName.equals(RegistrationForm.GUEST_FORM.getFormName())) {
            navigateToNewRegistrationForm(R.id.action_global_guestRegistrationFragment,
                    R.id.guestRegistrationFragment);
        } else if (formName.equals(RegistrationForm.INNOVATION_LABS_FORM.getFormName())) {
            navigateToNewRegistrationForm(R.id.action_global_innovationLabsRegistrationFragment,
                    R.id.innovationLabsRegistrationFragment);
        } else if (formName.equals(RegistrationForm.SUMMER_PARTY_FORM.getFormName())) {
            navigateToNewRegistrationForm(R.id.action_global_summerPartyRegistrationFragment,
                    R.id.summerPartyRegistrationFragment);
        }
    }

    private void navigateToNewRegistrationForm(int actionId, int fragmentID) {
        int currentFragment = mNestedNav.getNavController().getCurrentDestination().getId();
        if (currentFragment != fragmentID) {
            Navigation.findNavController(findViewById(R.id.registration_nav_host_fragment))
                    .navigate(actionId);
        }
    }

    public void setupKeyboard(View view) {
        if (!(view instanceof EditText) && !(view instanceof Button)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard(WelcomeActivity.this);
                return false;
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupKeyboard(innerView);
            }
        }
    }

    private void initDB() {
        mDb = SmartDeskDataBase.getDatabase(getApplicationContext());
        if (mDb.guestUserDataModel().getUsersNumber() == 0 && mDb.registrationDataDao().getRegistrationDataSize() == 0) {
            DbInitialization.populeDb(mDb);
        }
        List<DbRegistrationData> registrationDataList = mDb.registrationDataDao().getAllRegistrationData();
        for (int i = 0; i < registrationDataList.size(); i++) {
            Log.d("GABI", registrationDataList.get(i).formType + ": " +
                    registrationDataList.get(i).email + " - " + registrationDataList.get(i).registrationCode);
        }
    }

    void startViewAnimation(View view, Animator.AnimatorListener animatorListener, int duration, int delay, float x) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> view.animate()
                .translationXBy(x)
                .setDuration(duration)
                .setListener(animatorListener)
                .start(), delay);
    }

    private void onSideMenuClick(boolean isSideMenuOnLeft, RegistrationData registrationData) {
        int flag = (isSideMenuOnLeft) ? -1 : 1;
        int registerNowViewDelay = (isSideMenuOnLeft) ? 75 : 450;
        int alreadyRegisteredViewDelay = (isSideMenuOnLeft) ? 450 : 75;
        int leftInfoDelay = (isSideMenuOnLeft) ? 125 : 500;
        int rightInfoDelay = (isSideMenuOnLeft) ? 500 : 125;

        startViewAnimation(mLeftView, getSideViewAnimatorListener(), 1000, 0, -1 * flag * mScreenWidthInPixels / 3 * 2);
        startViewAnimation(mRegisterNowView, getRegisterNowViewAnimatorListener(flag, registrationData),
                450, registerNowViewDelay, flag * mScreenWidthInPixels / 9);
        startViewAnimation(mAlreadyRegisteredView, getAlreadyRegisteredAnimatorListener(flag),
                450, alreadyRegisteredViewDelay, flag * mScreenWidthInPixels / 6);
        startViewAnimation(mLeftInfo, getLeftInfoAnimatorListener(flag),
                400, leftInfoDelay, flag * mScreenWidthInPixels / 14);
        startViewAnimation(mRightInfo, getRightInfoAnimatorListener(flag),
                400, rightInfoDelay, flag * mScreenWidthInPixels / 14);
        startViewAnimation(mFormIcon, null, 400, leftInfoDelay, -flag * mScreenWidthInPixels / 14);
    }

    private void initViews() {
        mNestedNav = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.registration_nav_host_fragment);
        mUserDataMapper = new UserDataMapper();

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        mScreenWidthInPixels = outMetrics.widthPixels;
        setViewsWidth(mLeftView);
        setViewsWidth(mRightView);
        setViewsWidth(mAlreadyRegisteredView);
        setViewsWidth(mRegisterNowView);

        setViewsVisibility();
        isUserAlreadyRegistered = false;
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
                RegistrationData data = getUserDataByFormType(registrationData.formType, registrationData.email);
                navigateToFragmentByDataType(data);
                onSideMenuClick(false, data);
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

    private void navigateToFragmentByDataType(RegistrationData data) {
        if (data instanceof GuestUserData) {
            navigateToNewRegistrationForm(R.id.action_global_guestRegistrationFragment, R.id.guestRegistrationFragment);
        } else if (data instanceof SummerPartyUserData) {
            navigateToNewRegistrationForm(R.id.action_global_summerPartyRegistrationFragment, R.id.summerPartyRegistrationFragment);
        } else if (data instanceof InnovationLabsUserData) {
            navigateToNewRegistrationForm(R.id.action_global_innovationLabsRegistrationFragment, R.id.innovationLabsRegistrationFragment);
        }
    }

    private RegistrationData getUserDataByFormType(String formType, String email) {
        RegistrationData data = null;
        switch (formType) {
            case "Guest Registration Form":
                data = mUserDataMapper.convertToGuestUserData(mDb.guestUserDataModel().getUserDataByEmail(email));
                break;
            case "Summer Party Registration Form":
                data = mUserDataMapper.convertToSummerPartyUserData(mDb.summerPartyUserDataModel().getUserDataByEmail(email));
                break;
            case "Innovation Labs Registration Form":
                data = mUserDataMapper.convertToInnovationLabsUserData(mDb.innovationLabsUserDataModel().getUserDataByEmail(email));
                break;
        }
        return data;
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

    private BaseRegistrationFragment getRegistrationFragment() {
        return (BaseRegistrationFragment) mNestedNav.getChildFragmentManager().getFragments().get(0);
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

    Animator.AnimatorListener getRegisterNowViewAnimatorListener(float x, RegistrationData registrationData) {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mRegisterNowView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (x < 0) {
                    mRegisterNowView.setVisibility(View.INVISIBLE);
                    getRegistrationFragment().clearRegistrationFields();
                    getRegistrationFragment().setUserSignedInToFalse();
                } else {
                    if (isUserAlreadyRegistered) {
                        getRegistrationFragment().fillFields(registrationData, false);
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

    @Override
    protected void onDestroy() {
        mAlreadyRegisteredMenuButton.setOnClickListener(null);
        mRegisterNowMenuButton.setOnClickListener(null);
        super.onDestroy();
    }
}
