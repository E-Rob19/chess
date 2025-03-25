package Facade;

import RequestsAndResponses.*;
import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.zip.DataFormatException;

public class ServerFacade {
    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest req) throws DataFormatException {
        var path = "/user";
        RegisterResult res = this.makeRequest("POST", path, req, RegisterResult.class, null);
        if (res != null) {
            authToken = res.authToken();
        }
        return res;
    }

    public RegisterResult login(LoginRequest req) throws DataFormatException {
        var path = "/session";
        RegisterResult res = this.makeRequest("POST", path, req, RegisterResult.class, null);
        if (res != null) {
            authToken = res.authToken();
        }
        return res;
    }

    public void clear() throws DataFormatException {
        var path = "/db";
        RegisterResult res = this.makeRequest("DELETE", path, null, null, authToken);
    }
    public String logout(LogoutRequest req) throws DataFormatException {
        var path = "/session";
        String res = this.makeRequest("DELETE", path, req, null, authToken);
        return res;
    }

    public CreateGameResponse createGame(CreateGameRequest req) throws DataFormatException {
        var path = "/game";
        CreateGameResponse res = this.makeRequest("POST", path, req, CreateGameResponse.class, authToken);
        return res;
    }

    public String joinGame(JoinRequest req) throws DataFormatException {
        var path = "/game";
        String res = this.makeRequest("PUT", path, req, null, authToken);
        return res;
    }

    public ListResponse listGames(LogoutRequest req) throws DataFormatException {
        var path = "/game";
        ListResponse res = this.makeRequest("GET", path, null, ListResponse.class, authToken);
        return res;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws DataFormatException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            if(authToken != null){
                http.addRequestProperty("Authorization", authToken);
            }
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
            case 400 -> {
                System.out.print("unable to complete request : 400\n");
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
