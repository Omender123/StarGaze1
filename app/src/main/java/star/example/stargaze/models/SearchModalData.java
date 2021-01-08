package star.example.stargaze.models;

public class SearchModalData {
    String ArtistName,ImageUrl;

    public SearchModalData(String artistName, String imageUrl) {
        ArtistName = artistName;
        ImageUrl = imageUrl;
    }

    public SearchModalData() {
    }

    public String getArtistName() {
        return ArtistName;
    }

    public void setArtistName(String artistName) {
        ArtistName = artistName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
