package com.endava.smartdesk.view.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.endava.smartdesk.R;
import com.endava.smartdesk.model.RegistrationData;
import com.endava.smartdesk.utils.UserDataMapper;
import com.endava.smartdesk.view.activities.RegistrationSuccessfulActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseRegistrationFragment extends Fragment {

    @BindView(R.id.register_button)
    Button mRegisterButton;

    @BindView(R.id.signature)
    ImageView mSignature;

    UserDataMapper mUserDataMapper;

    private Unbinder mUnbinder;
    private boolean isUserSigned;
    private SignatureFragment mSignatureFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserDataMapper = new UserDataMapper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return provideYourFragmentView(inflater, viewGroup, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);
        isUserSigned = false;
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRegisterButton.setOnClickListener(v -> finnishRegistration());
    }

    abstract void initViews();

    abstract View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    abstract RegistrationData getRegistrationData();

    abstract void saveItem(RegistrationData registrationData);

    public abstract boolean areAllFieldsFilled();

    public abstract void clearRegistrationFields();

    public abstract void fillFields(RegistrationData registrationData, boolean fillPartially);

    public void setUserSignedInToFalse() {
        isUserSigned = false;
    }

    private void onSignComplete(String imagePath) {
        mRegisterButton.setText("REGISTER");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        mSignature.setImageBitmap(bitmap);
        mSignatureFragment.setOnSignListener(null);
        isUserSigned = true;
    }

    private void finnishRegistration() {
        if (isUserSigned) {
            if (areAllFieldsFilled()) {
                saveItem(getRegistrationData());
                saveMiningData(getRegistrationData());
                startActivity(new Intent(getActivity(), RegistrationSuccessfulActivity.class));
                //Retrofit
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                try {
//                    Date arrivalDate = dateFormat.parse(mArrivalDate.getText().toString());
//                    Date departureDate = dateFormat.parse(mDepartureDate.getText().toString());
//                    GuestUserData userData = new GuestUserData(mFirstName.getText().toString(),
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
                Toast.makeText(getActivity(), "Please fill all the fields.", Toast.LENGTH_SHORT).show();
            }
        } else {
            mSignatureFragment = SignatureFragment.newInstance();
            mSignatureFragment.show(getActivity().getSupportFragmentManager(), "Signature Fragment");
            mSignatureFragment.setOnSignListener(this::onSignComplete);
        }
    }

    abstract void saveMiningData(RegistrationData registrationData);

    void setAutoCompleteViews(AutoCompleteTextView view, List<String> autoCompleteList) {
        if (view != null) {
            List<String> uniqueValueList = new ArrayList<>(new HashSet<>(autoCompleteList));

            ArrayAdapter<String> firstNameAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line, uniqueValueList);
            view.setThreshold(1);
            view.setAdapter(firstNameAdapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}