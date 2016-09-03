package com.mrsmyx.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cj on 1/3/16.
 */
public class CCNetwork  {

    protected String ip;
    protected int port;
    protected String urlStub="http://%s:%s/ccapi/";
    protected CCNetwork(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public CCNetwork(){}

    protected String compileUrl(String compiled){
        return String.format(urlStub,ip, port) + compiled;
    }


    public void doRequest(String urlString){
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.getInputStream().close();
            httpURLConnection.disconnect();
        }catch (IOException ex){

        }
    }

    public String getSimpleRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String stub = null;
        while((stub = br.readLine())!= null)
            sb.append(stub);
        br.close();
        httpURLConnection.disconnect();
        return sb.toString();
    }

    public List<String> getListRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        List<String> stringList = new ArrayList<>();
        String stub = null;
        while((stub = br.readLine())!= null)
            stringList.add(stub);
        br.close();
        httpURLConnection.disconnect();
        return stringList;
    }
}
