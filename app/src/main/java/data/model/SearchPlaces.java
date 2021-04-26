package data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "searchplaces_table")
public class SearchPlaces {
    @PrimaryKey(autoGenerate = true)
    private long id;

    public long getId(){return id;}
    public void setId(long id){this.id = id;}

    public SearchPlaces(){
    }

    public static class SearchPlacesWithGooglePlaces{
        @Embedded public SearchPlaces searchPlaces;
        @Relation(
                parentColumn = "id",
                entityColumn = "searchPlacesId"
        )
        public List<GooglePlace> googlePlaces;
    }
}


