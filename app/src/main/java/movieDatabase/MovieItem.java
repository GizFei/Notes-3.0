package movieDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MovieItem {

    private UUID mUUID;
    private String mName;
    private boolean mHasSeen;
    private String mComment;

    public MovieItem(){
        this.mUUID = UUID.randomUUID();
        mName = "";
        mHasSeen = false;
        mComment = "No comments.";
    }

    public MovieItem(UUID UUID){
        this.mUUID = UUID;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isHasSeen() {
        return mHasSeen;
    }

    public void setHasSeen(boolean hasSeen) {
        mHasSeen = hasSeen;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public List<String> getTempMovieList(){
        List<String> movies = new ArrayList<>();
        movies.add("蚁人");
        movies.add("爱情公寓");
        movies.add("一出好戏");
        return  movies;
    }
}
