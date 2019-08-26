package com.endava.smartdesk.view.fragments;


import android.os.Bundle;
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
import com.endava.smartdesk.database.model.DbInnovationLabsUserData;
import com.endava.smartdesk.database.model.DbLastNameMiningData;
import com.endava.smartdesk.model.InnovationLabsUserData;
import com.endava.smartdesk.model.RegistrationData;
import com.endava.smartdesk.model.TShirtSize;
import com.endava.smartdesk.model.TransportationType;
import com.endava.smartdesk.utils.SpinnerUtils;

import butterknife.BindView;

import static com.endava.smartdesk.view.activities.WelcomeActivity.mDb;

public class InnovationLabsRegistrationFragment extends BaseRegistrationFragment {

    @BindView(R.id.first_name)
    AutoCompleteTextView mFirstName;

    @BindView(R.id.last_name)
    AutoCompleteTextView mLastName;

    @BindView(R.id.team_name)
    EditText mTeamName;

    @BindView(R.id.email_address)
    EditText mEmailAddress;

    @BindView(R.id.transportation)
    Spinner mTransportationSpinner;

    @BindView(R.id.tshirt_size)
    Spinner mTShirtSpinner;

    private SpinnerUtils mSpinnerUtils;

    @Override
    View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.innovation_labs_registration_layout, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSpinnerUtils = new SpinnerUtils();
        mSpinnerUtils.setTransportationSpinner(getActivity(), mTransportationSpinner);
        mSpinnerUtils.setTShirtSizeSpinner(getActivity(), mTShirtSpinner);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLastName.setOnItemClickListener((parent, view, position, id) ->
                autoCompleteFields(parent.getItemAtPosition(position).toString()));
    }

    @Override
    RegistrationData getRegistrationData() {
        InnovationLabsUserData userData = new InnovationLabsUserData();
        userData.setFirstName(mFirstName.getText().toString());
        userData.setLastName(mLastName.getText().toString());
        userData.setEmail(mEmailAddress.getText().toString());
        userData.setTeamName(mTeamName.getText().toString());
        userData.setTransportation(mTransportationSpinner.getSelectedItem().toString());
        userData.setTShirtSize(mTShirtSpinner.getSelectedItem().toString());
        return userData;
    }

    @Override
    void saveItem(RegistrationData registrationData) {
        mDb.innovationLabsUserDataModel().insert(mUserDataMapper
                .convertToDbInnovationLabsUserData((InnovationLabsUserData) registrationData));
    }

    @Override
    void initViews() {
        setAutoCompleteViews(mFirstName, mDb.firstNameMiningDao().getAllFirstNames());
        setAutoCompleteViews(mLastName, mDb.lastNameMiningDao().getAllLastNames());
    }

    private void autoCompleteFields(String lastName) {
        DbInnovationLabsUserData user = mDb.innovationLabsUserDataModel().getUserDataByLastName(lastName);
        if (user != null) {
            if (user.firstName.equals(mFirstName.getText().toString())) {
                fillFields(mUserDataMapper.convertToInnovationLabsUserData(user), true);
            }
        }
    }

    @Override
    public boolean areAllFieldsFilled() {
        if ("".contentEquals(mFirstName.getText())) {
            return false;
        } else if ("".contentEquals(mLastName.getText())) {
            return false;
        } else if ("".contentEquals(mTeamName.getText())) {
            return false;
        } else if ("".contentEquals(mEmailAddress.getText())) {
            return false;
        } else if (mTransportationSpinner.getSelectedItem().toString()
                .equals(TransportationType.TRANSPORTATION.getTransportationType())) {
            return false;
        } else return !mTShirtSpinner.getSelectedItem().toString()
                .equals(TShirtSize.T_SHIRT_SIZE.getTShirtSize());
    }

    @Override
    public void clearRegistrationFields() {
        mFirstName.getText().clear();
        mLastName.getText().clear();
        mTeamName.getText().clear();
        mEmailAddress.getText().clear();
        mTransportationSpinner.setSelection(0);
        mTShirtSpinner.setSelection(0);
        mRegisterButton.setText(getResources().getString(R.string.register_button_text));
        mSignature.setImageDrawable(null);
    }

    @Override
    public void fillFields(RegistrationData registrationData, boolean fillPartially) {
        InnovationLabsUserData innovationLabsUserData = (InnovationLabsUserData) registrationData;
        if (innovationLabsUserData != null) {
            mFirstName.setText(innovationLabsUserData.getFirstName());
            mLastName.setText(innovationLabsUserData.getLastName());
            mEmailAddress.setText(innovationLabsUserData.getEmail());
            if (!fillPartially) {
                mTeamName.setText(innovationLabsUserData.getTeamName());
                if (innovationLabsUserData.getTransportation().isEmpty()) {
                    mTransportationSpinner.setSelection(0);
                } else {
                    mTransportationSpinner.setSelection(TransportationType.valueOf(
                            innovationLabsUserData.getTransportation().toUpperCase()).ordinal());
                }
                if (innovationLabsUserData.getTShirtSize().isEmpty()) {
                    mTShirtSpinner.setSelection(0);
                } else {
                    mTShirtSpinner.setSelection(TShirtSize.valueOf(
                            innovationLabsUserData.getTShirtSize().toUpperCase()).ordinal());
                }
            }
        } else {
            Toast.makeText(getActivity(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void saveMiningData(RegistrationData registrationData) {
        InnovationLabsUserData userData = (InnovationLabsUserData) registrationData;
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
        mLastName.setOnItemClickListener(null);
    }
}
