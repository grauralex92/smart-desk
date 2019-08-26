package com.endava.smartdesk.view.fragments;


import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.endava.smartdesk.R;
import com.endava.smartdesk.database.model.DbCompanyMiningData;
import com.endava.smartdesk.database.model.DbFirstNameMiningData;
import com.endava.smartdesk.database.model.DbGuestUserData;
import com.endava.smartdesk.database.model.DbLastNameMiningData;
import com.endava.smartdesk.model.GuestUserData;
import com.endava.smartdesk.model.RegistrationData;
import com.endava.smartdesk.utils.CalendarUtils;

import butterknife.BindView;

import static com.endava.smartdesk.view.activities.WelcomeActivity.mDb;

public class GuestRegistrationFragment extends BaseRegistrationFragment {

    @BindView(R.id.first_name)
    AutoCompleteTextView mFirstName;

    @BindView(R.id.last_name)
    AutoCompleteTextView mLastName;

    @BindView(R.id.email_address)
    EditText mEmailAddress;

    @BindView(R.id.company_name)
    AutoCompleteTextView mCompanyName;

    @BindView(R.id.badge_number)
    EditText mBadgeNumber;

    @BindView(R.id.purpose)
    EditText mPurpose;

    @BindView(R.id.arrival_date)
    EditText mArrivalDate;

    @BindView(R.id.departure_date)
    EditText mDepartureDate;

    private CalendarUtils mCalendarUtils;

    @Override
    View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guest_registration_layout, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCalendarUtils = new CalendarUtils();
        initViews();
    }

    void initViews() {
        setAutoCompleteViews(mFirstName, mDb.firstNameMiningDao().getAllFirstNames());
        setAutoCompleteViews(mLastName, mDb.lastNameMiningDao().getAllLastNames());
        setAutoCompleteViews(mCompanyName, mDb.companyMiningDao().getAllCompanyNames());
        mArrivalDate.setInputType(InputType.TYPE_NULL);
        mDepartureDate.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public void onResume() {
        super.onResume();
        mArrivalDate.setOnClickListener(v -> mCalendarUtils.getDateAndTime(getActivity(), mArrivalDate));
        mDepartureDate.setOnClickListener(v -> mCalendarUtils.getDateAndTime(getActivity(), mDepartureDate));
        mLastName.setOnItemClickListener((parent, view, position, id) ->
                autoCompleteFields(parent.getItemAtPosition(position).toString()));
    }

    @Override
    RegistrationData getRegistrationData() {
        GuestUserData userData = new GuestUserData();
        userData.setFirstName(mFirstName.getText().toString());
        userData.setLastName(mLastName.getText().toString());
        userData.setEmail(mEmailAddress.getText().toString());
        userData.setCompanyName(mCompanyName.getText().toString());
        userData.setBadgNumber(mBadgeNumber.getText().toString());
        userData.setPurpose(mPurpose.getText().toString());
        userData.setArrivalDate(mCalendarUtils.getDateAndTimeFromString(mArrivalDate.getText().toString()));
        userData.setDepartureDate(mCalendarUtils.getDateAndTimeFromString(mDepartureDate.getText().toString()));
        return userData;
    }

    @Override
    void saveItem(RegistrationData registrationData) {
        mDb.guestUserDataModel().insert(mUserDataMapper.convertToDbGuestUserData((GuestUserData) registrationData));
    }

    private void autoCompleteFields(String lastName) {
        DbGuestUserData user = mDb.guestUserDataModel().getUserDataByLastName(lastName);
        if (user != null) {
            if (user.firstName.equals(mFirstName.getText().toString())) {
                fillFields(mUserDataMapper.convertToGuestUserData(user), true);
            }
        }
    }

    public void fillFields(RegistrationData registrationData, boolean fillPartially) {
        GuestUserData guestUserData = (GuestUserData) registrationData;
        if (guestUserData != null) {
            mFirstName.setText(guestUserData.getFirstName());
            mLastName.setText(guestUserData.getLastName());
            mCompanyName.setText(guestUserData.getCompanyName());
            mEmailAddress.setText(guestUserData.getEmail());
            if (!fillPartially) {
                mBadgeNumber.setText(guestUserData.getBadgeNumber());
                mPurpose.setText(guestUserData.getPurpose());
                mArrivalDate.setText(mCalendarUtils.setDateAndTime(guestUserData.getArrivalDate()));
                mDepartureDate.setText(mCalendarUtils.setDateAndTime(guestUserData.getDepartureDate()));
            }
        } else {
            Toast.makeText(getActivity(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void saveMiningData(RegistrationData registrationData) {
        GuestUserData userData = (GuestUserData) registrationData;
        DbFirstNameMiningData dbFirstNameMiningData = new DbFirstNameMiningData();
        dbFirstNameMiningData.firstName = userData.getFirstName();
        mDb.firstNameMiningDao().insert(dbFirstNameMiningData);
        DbLastNameMiningData dbLastNameMiningData = new DbLastNameMiningData();
        dbLastNameMiningData.lastName = userData.getLastName();
        mDb.lastNameMiningDao().insert(dbLastNameMiningData);
        DbCompanyMiningData dbCompanyMiningData = new DbCompanyMiningData();
        dbCompanyMiningData.company = userData.getCompanyName();
        mDb.companyMiningDao().insert(dbCompanyMiningData);
    }

    public void clearRegistrationFields() {
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
    }

    public boolean areAllFieldsFilled() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCalendarUtils = null;
        mArrivalDate.setOnClickListener(null);
        mDepartureDate.setOnClickListener(null);
        mLastName.setOnItemClickListener(null);
    }
}
