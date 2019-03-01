package fanrong.cwvwalled.http.model;

public class HomeAssertModel {

    public String title;

    public int imageResource;

    public String count;

    public String countCNY;

    public HomeAssertModel(String title, int imageResource, String count, String countCNY) {
        this.title = title;
        this.imageResource = imageResource;
        this.count = count;
        this.countCNY = countCNY;
    }

}
