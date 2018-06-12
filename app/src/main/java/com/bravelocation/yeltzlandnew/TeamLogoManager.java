package com.bravelocation.yeltzlandnew;

import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class TeamLogoManager {
    public void LoadTeamImageIntoView(String teamName, ImageView view) {
        String imageUrl = String.format("https://bravelocation.com/teamlogos/%s.png", this.makeTeamFileName(teamName));
        Picasso.get().load(imageUrl).placeholder(R.drawable.blank_team).into(view);
    }

    private String makeTeamFileName(String teamName) {
        return teamName.replace(' ', '_').toLowerCase();
    }
}
