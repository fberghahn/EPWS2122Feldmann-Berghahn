package com.example.MatchingActivities.data

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.MatchingActivities.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class EventDao (){
    private  val databaseRef = FirebaseDatabase.getInstance("https://matchingactivities-default-rtdb.europe-west1.firebasedatabase.app/").reference
    private var isInsertSuccess =false

    @WorkerThread
    fun getAlphabetizedWords(): ArrayList<Event> {
        // Read from the database
        val cacheList  = ArrayList<Event>()
        databaseRef.child("Cache").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                 cacheList.clear()
                for (postSnapshot in snapshot.children)
                {
                    val currentCache=postSnapshot.getValue(Event::class.java)
                    cacheList.add(currentCache!!)
                }
                Log.d("DatabaseRead", "Value is: $cacheList")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("DatabaseRead", "Failed to read value.", error.toException())

            }

        })
        return cacheList
    }

    @WorkerThread
     fun insert(cache : Event) {


        val cache=Event(databaseRef.push().key!!,cache.creatorid,cache.Eventname,cache.lat,cache.lng ,cache.image,cache.kategorie)
        databaseRef.child("Cache").child(cache.eventid).setValue(cache).addOnCompleteListener{
            isInsertSuccess=true

        }.addOnFailureListener{
            isInsertSuccess=false

        }

    }

    @WorkerThread
    fun update(cache : Event) {
        databaseRef.child("Cache").child(cache.eventid).setValue(cache).addOnCompleteListener{
            isInsertSuccess=true

        }.addOnFailureListener{
            isInsertSuccess=false

        }

    }
    @WorkerThread
    suspend fun setFoundTrue(eventId : String) {



        databaseRef.child("Cache").child(eventId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children)
                {
                    val currentCache=postSnapshot.getValue(Event::class.java)
                    if (currentCache?.eventid==eventId){
                        currentCache.found=true
                        insert(currentCache)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        databaseRef.removeValue()
    }

@WorkerThread
    suspend fun getImage(image: String?, context :Context): Bitmap {
        val storage = FirebaseStorage.getInstance().reference

        val pathReference = storage.child("images/")
        var useableImage: Bitmap = ContextCompat.getDrawable(context, R.drawable.image1)!!.toBitmap()
        pathReference.child(image!!).getBytes(1024*1024).addOnSuccessListener {
//            useableImage=BitmapFactory.decodeByteArray(it,0,it.size)
            useableImage= ContextCompat.getDrawable(context, R.drawable.image2)!!.toBitmap()

        }.addOnFailureListener {
            useableImage= ContextCompat.getDrawable(context, R.drawable.image2)!!.toBitmap()
        }
        return useableImage
    }
}