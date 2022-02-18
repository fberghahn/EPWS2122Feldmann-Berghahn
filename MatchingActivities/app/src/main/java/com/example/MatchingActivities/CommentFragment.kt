package com.example.MatchingActivities

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MatchingActivities.adapter.CommentListAdapter
import com.example.MatchingActivities.data.Comment
import com.example.MatchingActivities.databinding.CommentFragmentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CommentFragment : Fragment() {
    companion object {
        fun newInstance() = CommentFragment()
    }
    private var _binding : CommentFragmentBinding?= null//??
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModel.WordViewModelFactory()
    }

    private lateinit var myDataset : ArrayList<Comment>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = CommentFragmentBinding.inflate(inflater, container, false)//??
        val view = binding.root

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val eventid = arguments?.let { it.getString("eventid")}

        myDataset=ArrayList<Comment>()


        val adapter = CommentListAdapter(myDataset)
        recyclerView = binding.recyclerviewComment
        recyclerView.adapter=adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        loadDataFromServer(eventid!!)




        binding.buttonSaveComment.setOnClickListener() {
           handleData()
        }

    }

    private fun handleData() {
        val name= arguments?.let { it.getString("title").toString() }
        val id = arguments?.let { it.getString("eventid")}
        if ( id!="-1" ) {

            val comment =  Comment(Eventname = name!!, eventid = id!!, comment = binding.editComment.text.toString())
            if (comment != null&&comment.eventid!="-1"&&comment.Eventname!="null") {
                itemViewModel.insertComment(comment)
                binding.editComment.text.clear()

            }
            else Toast.makeText(
                this.requireContext(),
                R.string.locationError,
                Toast.LENGTH_LONG
            ).show()

        }
        else Toast.makeText(
            this.requireContext(),
            R.string.locationError,
            Toast.LENGTH_LONG
        ).show()
    }


    //Firebase Datenbank Kommentare auslesen
    private fun loadDataFromServer(eventid:String) {
        val databaseRef = FirebaseDatabase.getInstance("https://matchingactivities-default-rtdb.europe-west1.firebasedatabase.app/").reference
        databaseRef.child("Comment").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                myDataset.clear()
                for (postSnapshot in snapshot.children)
                {
                    val currentComment=postSnapshot.getValue(Comment::class.java)
                    if (currentComment?.eventid==eventid)
                    myDataset.add(currentComment)
                }
                Log.d("DatabaseRead", "Value is: $myDataset")
                myDataset.reverse()
                recyclerView.adapter?.notifyDataSetChanged()


            }


            override fun onCancelled(error: DatabaseError) {
                Log.w("DatabaseRead", "Failed to read value.", error.toException())



            }

        })
    }

}