package com.endava.smartdesk.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.endava.smartdesk.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.form_fragment, container, false)
    }

}