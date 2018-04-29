package com.example.alexuni.sit305ass2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class ShopActivity extends AppCompatActivity {

    JSONArray wd;
    JSONObject c;
    int i;
    private ListView listView;

    //private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    private ArrayList<String> data = new ArrayList<String>();
    //ArrayList<HashMap<String, String>> shopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        listView = findViewById(R.id.listView);




        loadJSON();
        try {
            loadItems();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String loadJSON() {
        String json = null;
        try {
            InputStream file = getAssets().open("gameData.json"); //JSON file
            int size = file.available();
            byte[] buffer = new byte[size];
            file.read(buffer);
            file.close();
            json = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void loadItems() throws JSONException {


        JSONObject gameData = new JSONObject(loadJSON());
        // Getting JSON Array node
        wd = gameData.getJSONArray("Weapons");
        for (int i = 0; i < wd.length(); i++) {
            c = wd.getJSONObject(i);

            String id = c.getString("ID");
            String name = c.getString("name");
            String price = c.getString("price");
        }


    }



   /* private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        public MyListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.image = (ImageView) convertView.findViewById(R.id.shopImage);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.button = (Button) convertView.findViewById(R.id.buyBtn);
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Button was clicked " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                convertView.setTag(viewHolder);
            } else {
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.name.setText(getItem(position));
            }

            return convertView;
        }
    }

    public class ViewHolder {
        ImageView image;
        TextView name;
        TextView price;
        Button button;
    }

    public void buyHandler(View v) throws JSONException {
        String price = c.getString("price");


    } */
}
