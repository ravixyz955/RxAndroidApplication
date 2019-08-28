package com.example.user.rxandroidapplication.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {
    private static ProgressDialog progressDialog;

    public static void getSnackbar(CoordinatorLayout coordinatorLayout, String msg) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        View snackbar_view = snackbar.getView();
        final CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbar_view.getLayoutParams();
        params.setMargins(params.leftMargin + 10,
                params.topMargin,
                params.rightMargin + 10,
                params.bottomMargin + 50);

        snackbar_view.setLayoutParams(params);
        snackbar_view.setBackgroundColor(Color.parseColor("#000000"));
        TextView snackbar_text = (TextView) snackbar_view.findViewById(android.support.design.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbar_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            snackbar_text.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snackbar.show();
    }

    public static ProgressDialog createProgressDialoge(Context context, String title) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(title);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static void dissmisProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public static void showProgress() {
        if (progressDialog != null)
            progressDialog.show();
    }

    public static void showToast(Context context, String res) {
        Toast toast = Toast.makeText(context, res, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static String getLineStringJson(String coords) {
        JSONObject geoJson = new JSONObject();
        JSONArray geoJsonArray;
        try {
            geoJson.put("type", "LineString");
            geoJsonArray = new JSONArray(coords.replace("^\\[|\\]$", ""));
            geoJson.put("coordinates", geoJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return geoJson.toString();
    }

    public static String getNonLineStringJson(String coords) {
        JSONObject geoJson = new JSONObject();
        JSONArray geoJsonArray;
        try {
            geoJson.put("type", "Polygon");
            geoJsonArray = new JSONArray(coords.replace("^\\[|\\]$", "").replace("^\\[|\\]$", ""));
            geoJson.put("coordinates", geoJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return geoJson.toString();
    }

    public static StringBuilder readLogs() {
        StringBuilder logBuilder = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logBuilder.append(line + "\n");
            }
        } catch (IOException e) {
        }
        return logBuilder;
    }
}