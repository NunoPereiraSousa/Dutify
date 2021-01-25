package com.example.dutify.RecyclerViewAdapterProjectsTeamMember;

public class TeamMember {
    private int  personId;
    private  String personName;
    private  String photoUrl;
    private  String teamColor;



    public  TeamMember (int personId, String personName, String photoUrl, String teamColor){
        this.personId = personId;
        this.personName = personName;
        this.photoUrl =  photoUrl;
        this.teamColor =  teamColor;
    }


    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(String teamColor) {
        this.teamColor = teamColor;
    }
}
