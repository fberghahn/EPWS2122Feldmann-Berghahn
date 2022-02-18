package com.example.MatchingActivities.data


class Event{
    var eventid: String ="generated"
    var creatorid: String? =null
    var Eventname: String? =null
    var lat: Double =0.0
    var lng: Double=0.0
    var image : String="null"
    var found : Boolean = false
    var kategorie : String? = null

    constructor(){}
    constructor(eventid: String,creatorid: String?,EventName: String?,lat: Double,lng: Double,image : String, kategorie: String?){
        this.eventid=eventid
        this.creatorid=creatorid
        this.Eventname=EventName
        this.lat=lat
        this.lng=lng
        this.image=image
        this.kategorie=kategorie
    }
    constructor(creatorid: String?,EventName: String?,lat: Double,lng: Double,image : String,kategorie: String?){

        this.creatorid=creatorid
        this.Eventname=EventName
        this.lat=lat
        this.lng=lng
        this.image=image
        this.kategorie=kategorie
    }

}

