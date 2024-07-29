package com.example.assignment4;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DataFragment extends Fragment {

    private static final String[] FROM_COLUMNS = {DatabaseHelper.COLUMN_NAME};
    private static final int[] TO_VIEWS = {android.R.id.text1};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.list_view);
        Uri contentUri = MyContentProvider.CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getContext(),
                android.R.layout.simple_list_item_1,
                cursor,
                FROM_COLUMNS,
                TO_VIEWS,
                0
        );
        listView.setAdapter(adapter);
    }
}