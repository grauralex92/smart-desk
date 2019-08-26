package com.endava.smartdesk.view.fragments;


import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.endava.smartdesk.R;
import com.endava.smartdesk.database.model.DbFirstNameMiningData;
import com.endava.smartdesk.database.model.DbLastNameMiningData;
import com.endava.smartdesk.database.model.DbSummerPartyUserData;
import com.endava.smartdesk.model.RegistrationData;
import com.endava.smartdesk.model.SummerPartyUserData;
import com.endava.smartdesk.model.TransportationType;
import com.endava.smartdesk.utils.CalendarUtils;
import com.endava.smartdesk.utils.SpinnerUtils;

import butterknife.BindView;

import static com.endava.smartdesk.view.activities.WelcomeActivity.mDb;

public class SummerPartyRegistrationFragment extends BaseRegistrationFragment {

    @BindView(R.id.first_name)
    AutoCompleteTextView mFirstName;

    @BindView(R.id.last_name)
    AutoCompleteTextView mLastName;

    @BindView(R.id.email_address)
    EditText mEmailAddress;

    @BindView(R.id.transportation)
    Spinner mTransportationSpinner;

    @BindView(R.id.arrival_hour)
    EditText mArrivalHour;

    @BindView(R.id.departure_hour)
    EditText mDepartureHour;

    private SpinnerUtils mSpinnerUtils;
    private CalendarUtils mCalendarUtils;

    @Override
    View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.summer_party_registration_layout, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSpinnerUtils = new SpinnerUtils();
        mCalendarUtils = new CalendarUtils();
        mSpinnerUtils.setTransportationSpinner(getActivity(), mTransportationSpinner);
    }

    @Override
    public void onResume() {
        super.onResume();
        mArrivalHour.setOnClickListener(v -> mCalendarUtils.getTime(getActivity(), mArrivalHour));
        mDepartureHour.setOnClickListener(v -> mCalendarUtils.getTime(getActivity(), mDepartureHour));
        mLastName.setOnItemClickListener((parent, view, position, id) ->
                autoCompleteFields(parent.getItemAtPosition(position).toString()));
    }

    @Override
    RegistrationData getRegistrationData() {
        SummerPartyUserData userData = new SummerPartyUserData();
        userData.setFirstName(mFirstName.getText().toString());
        userData.setLastName(mLastName.getText().toString());
        userData.setEmail(mEmailAddress.getText().toString());
        userData.setTransportation(mTransportationSpinner.getSelectedItem().toString());
        userData.setArrivalHour(mCalendarUtils.getTimeFromString(mArrivalHour.getText().toString()));
        userData.setDepartureHour(mCalendarUtils.getTimeFromString(mDepartureHour.getText().toString()));
        return userData;
    }

    @Override
    void saveItem(RegistrationData registrationData) {
        mDb.summerPartyUserDataModel().insert(mUserDataMapper
                .convertToDbSummerPartyUserData((SummerPartyUserData) registrationData));
    }

    private void autoCompleteFields(String lastName) {
        DbSummerPartyUserData user = mDb.summerPartyUserDataModel().getUserDataByLastName(lastName);
        if (user != null) {
            if (user.firstName.equals(mFirstName.getText().toString())) {
                fillFields(mUserDataMapper.convertToSummerPartyUserData(user), true);
            }
        }
    }

    void initViews() {
        setAutoCompleteViews(mFirstName, mDb.firstNameMiningDao().getAllFirstNames());
        setAutoCompleteViews(mLastName, mDb.lastNameMiningDao().getAllLastNames());
        mArrivalHour.setInputType(InputType.TYPE_NULL);
        mDepartureHour.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public boolean areAllFieldsFilled() {
        if ("".contentEquals(mFirstName.getText())) {
            return false;
        } else if ("".contentEquals(mLastName.getText())) {
            return false;
        } else if ("".contentEquals(mEmailAddress.getText())) {
            return false;
        } else if (mTransportationSpinner.getSelectedItem().toString()
                .equals(TransportationType.TRANSPORTATION.getTransportationType())) {
            return false;
        } else if ("".contentEquals(mArrivalHour.getText())) {
            return false;
        } else return !"".contentEquals(mDepartureHour.getText());
    }

    @Override
    public void clearRegistrationFields() {
        mFirstName.getText().clear();
        mLastName.getText().clear();
        mEmailAddress.getText().clear();
        mTransportationSpinner.setSelection(0);
        mArrivalHour.getText().clear();
        mDepartureHour.getText().clear();
        mRegisterButton.setText(getResources().getString(R.string.register_button_text));
        mSignature.setImageDrawable(null);
    }

    @Override
    public void fillFields(RegistrationData registrationData, boolean fillPartially) {
        SummerPartyUserData summerPartyUserData = (SummerPartyUserData) registrationData;
        if (summerPartyUserData != null) {
            mFirstName.setText(summerPartyUserData.getFirstName());
            mLastName.setText(summerPartyUserData.getLastName());
            mEmailAddress.setText(summerPartyUserData.getEmail());
            if (!fillPartially) {
                if (summerPartyUserData.getTransportation().isEmpty()) {
                    mTransportationSpinner.setSelection(0);
                } else {
                    mTransportationSpinner.setSelection(TransportationType.valueOf(
                            summerPartyUserData.getTransportation().toUpperCase()).ordinal());
                }
                mArrivalHour.setText(mCalendarUtils.setTime(summerPartyUserData.getArrivalHour()));
                mDepartureHour.setText(mCalendarUtils.setTime(summerPartyUserData.getDepartureHour()));
            }
        } else {
            Toast.makeText(getActivity(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void saveMiningData(RegistrationData registrationData) {
        SummerPartyUserData userData = (SummerPartyUserData) registrationData;
        DbFirstNameMiningData dbFirstNameMiningData = new DbFirstNameMiningData();
        dbFirstNameMiningData.firstName = userData.getFirstName();
        mDb.firstNameMiningDao().insert(dbFirstNameMiningData);
        DbLastNameMiningData dbLastNameMiningData = new DbLastNameMiningData();
        dbLastNameMiningData.lastName = userData.getLastName();
        mDb.lastNameMiningDao().insert(dbLastNameMiningData);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSpinnerUtils = null;
        mCalendarUtils = null;
        mArrivalHour.setOnClickListener(null);
        mDepartureHour.setOnClickListener(null);
        mLastName.setOnItemClickListener(null);
    }
}
