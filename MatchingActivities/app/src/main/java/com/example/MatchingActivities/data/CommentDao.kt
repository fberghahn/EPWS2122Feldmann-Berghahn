package com.example.MatchingActivities.data

import androidx.annotation.WorkerThread
import com.google.firebase.database.FirebaseDatabase

class CommentDao {


        private  val databaseRef = FirebaseDatabase.getInstance("https://matchingactivities-default-rtdb.europe-west1.firebasedatabase.app/").reference
        private var isInsertSuccess =false

        @Suppress("RedundantSuspendModifier")
        @WorkerThread
        suspend fun insert(comment: Comment): Boolean {


                val comment=Comment(databaseRef.push().key!!,comment.creatorid, comment.eventid!!, comment.comment, comment.Eventname)
                databaseRef.child("Comment").child(comment.commentid).setValue(comment).addOnCompleteListener{
                        isInsertSuccess=true

                }.addOnFailureListener{
                        isInsertSuccess=false

                }
                return isInsertSuccess

        }
        @Suppress("RedundantSuspendModifier")
        @WorkerThread
        suspend fun deleteAll() {
                databaseRef.child("Comment").removeValue()
        }
}