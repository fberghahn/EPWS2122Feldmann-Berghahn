package com.example.MatchingActivities.data

class Comment{
    var commentid: String ="generated"
    var creatorid: String? ="Noch nicht implementiert"
    var eventid : String? =null
    var comment: String? =null
    var Eventname: String?="null"

    constructor(){}
    constructor(commentid:String,creatorid: String?,eventid: String?, comment: String?, Eventname: String?){
        this.commentid=commentid
        this.creatorid=creatorid
        this.eventid=eventid
        this.comment=comment
        this.Eventname=Eventname

    }
    constructor(Eventname:String?,eventid: String?,comment: String?){
        this.Eventname=Eventname
        this.eventid=eventid
        this.comment=comment

    }

}
