package Facade;

import com.google.gson.Gson;
import RequestsAndResponses.LoginRequest;
import RequestsAndResponses.RegisterRequest;
import RequestsAndResponses.RegisterResult;

import java.io.*;
import java.net.*;
import java.util.zip.DataFormatException;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest req) throws DataFormatException {
        var path = "/user";
        RegisterResult res = this.makeRequest("POST", path, req, RegisterResult.class);
        return res;
    }

    public RegisterResult login(LoginRequest req) throws DataFormatException {
        var path = "/session";
        RegisterResult res = this.makeRequest("POST", path, req, RegisterResult.class);
        return res;
    }

    public RegisterResult clear(LoginRequest req) throws DataFormatException {
        var path = "/session";
        RegisterResult res = this.makeRequest("POST", path, req, RegisterResult.class);
        return res;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws DataFormatException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeBody(request, http);
            http.connect();
            if(!throwIfNotSuccessful(http)){
                return null;
            }
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new DataFormatException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private boolean throwIfNotSuccessful(HttpURLConnection http) throws IOException{
        var status = http.getResponseCode();
        switch (status){
            case 401, 403 -> {
                System.out.print("unable to complete request\n");
                return false;
            }
        }
        if (status != 200) {
            throw new IOException("failure: " + status);
        }
        return true;
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

}
