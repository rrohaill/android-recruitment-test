package dog.snow.androidrecruittest.Models;

import java.io.Serializable;

/**
 * Created by Rohail on 1/24/2018.
 */

public class DataModel implements Serializable {

    private String timestamp;

    private String id;

    private String icon;

    private String description;

    private String name;

    private String url;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ClassPojo [timestamp = " + timestamp + ", id = " + id + ", icon = " + icon + ", description = " + description + ", name = " + name + ", url = " + url + "]";
    }

}
