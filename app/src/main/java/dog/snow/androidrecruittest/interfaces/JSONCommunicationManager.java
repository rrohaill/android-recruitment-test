package dog.snow.androidrecruittest.interfaces;

import java.util.ArrayList;

public interface JSONCommunicationManager {
    public void onResponse(String response, JSONCommunicationManager jsonCommunicationManager);

    public void onProcessNext(ArrayList<Object> listObject);

    public void onPreRequest();

    public void onError(String s);
}
