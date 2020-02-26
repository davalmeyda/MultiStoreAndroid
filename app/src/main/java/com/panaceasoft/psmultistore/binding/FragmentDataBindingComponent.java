package com.panaceasoft.psmultistore.binding;

import androidx.fragment.app.Fragment;

/**
 * A Data Binding Component implementation for fragments.
 */
public class FragmentDataBindingComponent implements androidx.databinding.DataBindingComponent {
    private final FragmentBindingAdapters adapter;

    public FragmentDataBindingComponent(Fragment fragment) {
        this.adapter = new FragmentBindingAdapters(fragment);
    }

    public FragmentBindingAdapters getFragmentBindingAdapters() {
        return adapter;
    }
}
