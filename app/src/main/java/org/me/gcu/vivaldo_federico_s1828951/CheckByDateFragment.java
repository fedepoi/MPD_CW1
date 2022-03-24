package org.me.gcu.vivaldo_federico_s1828951;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CheckByDateFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        Log.e("date","alive");
        return inflater.inflate(R.layout.fragment_check_by_date, container, false);
    }
}
