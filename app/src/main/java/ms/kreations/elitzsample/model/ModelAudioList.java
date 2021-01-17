package ms.kreations.elitzsample.model;

public class ModelAudioList {
    private String songTitle;
    private String url;

    public ModelAudioList(String songTitle, String url) {
        this.songTitle = songTitle;
        this.url = url;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
