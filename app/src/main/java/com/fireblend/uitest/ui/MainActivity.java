package com.fireblend.uitest.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.fireblend.uitest.R;
import com.fireblend.uitest.entities.Post;
import com.fireblend.uitest.entities.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    GridView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (GridView) findViewById(R.id.lista_contactos);

        new APItask(this).execute();
    }

    List<Post> posts;

    private static class APItask extends AsyncTask<URL, Integer, String> {
        WeakReference<MainActivity> act;

        public APItask(MainActivity act) {
            this.act = new WeakReference<>(act);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL[] params) {
            String respuestaVer ="";

            URL endpoint = null;
            try {
                endpoint = new URL("https://jsonplaceholder.typicode.com/posts");

                HttpsURLConnection connection = null;

                connection = (HttpsURLConnection) endpoint.openConnection();

                if (connection.getResponseCode() == 200) {
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(connection.getInputStream(), "UTF-8");
                    String respuesta = null;

                    //asignar una respuesta a la variale

                    BufferedReader r = new BufferedReader(responseBodyReader);
                    StringBuilder builder = new StringBuilder();
                    String line;

                    while ((line = r.readLine()) != null) {

                        builder.append(line);
                    }
                    respuestaVer = builder.toString();
                    return respuestaVer;
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

           return respuestaVer;
        }

        @Override
        protected void onPostExecute(String respuesta) {
            super.onPostExecute(respuesta);
            act.get().setuplist(respuesta);

            //convertir respuesta a objetos y asignar el adapter con los objetos
        }


    }

    private void setuplist(String respuesta) {

        posts = new ArrayList();

        try {
            JSONArray listaPrincipal = new JSONArray(respuesta);
            for (int i = 0; i < listaPrincipal.length(); i++) {
                JSONObject jsonObject = listaPrincipal.getJSONObject(i);

                posts.add(new Post(jsonObject.getInt("userID"),
                        jsonObject.getInt("id"),
                        jsonObject.getString("title"),
                        jsonObject.getString("body")));

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error ", Toast.LENGTH_SHORT).show();
        }

        //Le asignamos a la lista un nuevo BaseAdapter, implementado a continuación
        list.setAdapter(new BaseAdapter() {
            @Override
            //Retorna el numero de elementos en la lista.
            public int getCount() {
                return posts.size();
            }

            @Override
            //Retorna el elemento que pertenece a la posición especificada.
            public Object getItem(int position) {
                return posts.get(position);
            }

            @Override
            //Devuelve un identificador único para cada elemento de la lista.
            //En nuestro caso, basta con devolver la posición del elemento en la lista.
            public long getItemId(int position) {
                return position;
            }

            @Override
            //Devuelve la vista que corresponde a cada elemento de la lista
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = getLayoutInflater();
                View row = inflater.inflate(R.layout.contact_item, parent, false);

                TextView userId, id, title, body;

                userId = (TextView) row.findViewById(R.id.userId);
                id = (TextView) row.findViewById(R.id.id);
                title = (TextView) row.findViewById(R.id.title);
                body = (TextView) row.findViewById(R.id.body);

                Button btn = (Button) row.findViewById(R.id.row_btn);

                //Basandonos en la posicion provista en este metodo, proveemos los datos
                //correctos para este elemento.
                final int pos = position;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Hola, " + posts.get(pos).userId, Toast.LENGTH_SHORT).show();
                    }
                });

                userId.setText(posts.get(position).userId + "");
                id.setText(posts.get(position).id + "");
                title.setText(posts.get(position).title);
                body.setText(posts.get(position).body);

                return row;
            }
        });

    }
}