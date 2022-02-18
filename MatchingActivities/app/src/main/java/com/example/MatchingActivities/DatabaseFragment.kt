package com.example.MatchingActivities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MatchingActivities.adapter.ItemListAdapter
import com.example.MatchingActivities.data.Event
import com.example.MatchingActivities.databinding.DatabaseFragmentBinding
import com.google.android.gms.location.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList


class DatabaseFragment : Fragment() {
    companion object {
        fun newInstance() = DatabaseFragment()
    }
    private var _binding : DatabaseFragmentBinding?= null//??
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
//    private val itemViewModel: ItemViewModel by viewModels {
//        ItemViewModel.WordViewModelFactory()
//    }

    private var newLat :Double =0.0
    private var newLng :Double =0.0
    private lateinit var newTitle: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var myDataset : ArrayList<Event>
    private lateinit var myImageList : ArrayList<Bitmap>
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DatabaseFragmentBinding.inflate(inflater, container, false)//??
        val view = binding.root

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        return view

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
//        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDataset=ArrayList<Event>()

//        Firebase auth instance
        val firebaseapp : FirebaseApp = FirebaseApp.getInstance()
        mAuth = FirebaseAuth.getInstance(firebaseapp)


        //recyclerview über Listadapter erstellen
        val adapter = ItemListAdapter(this.requireContext(),myDataset)
        recyclerView = binding.recyclerview
        recyclerView.adapter=adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        loadDataFromServer()


        //Logout Button über Firebase
        binding.logoutButton.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this.requireActivity(), Login::class.java)
            startActivity(intent)
        }

        binding.fab.setOnClickListener() {
            val action = DatabaseFragmentDirections.actionDatabaseFragmentToAddCacheFragment()
            findNavController().navigate(action)
        }

    }





// Events von der Firebase DB laden

    private fun loadDataFromServer() {
        val databaseRef = FirebaseDatabase.getInstance("https://matchingactivities-default-rtdb.europe-west1.firebasedatabase.app").reference
        databaseRef.child("Cache").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                myDataset.clear()
//                myImageList.clear()
                for (postSnapshot in snapshot.children)
                {
                    val currentCache=postSnapshot.getValue(Event::class.java)
                    myDataset.add(currentCache!!)
//                    myImageList.add(getImage(currentCache.image))
                }
                myDataset.sortBy { cache: Event -> cache.found }
                Log.d("DatabaseRead", "Value is: $myDataset")
               recyclerView.adapter?.notifyDataSetChanged()
            }




            override fun onCancelled(error: DatabaseError) {
                Log.w("DatabaseRead", "Failed to read value.", error.toException())
//                Toast.makeText(
//                    context,
//                    "Data Could not load, try enabling wifi",
//                    Toast.LENGTH_LONG
//                ).show()

            }

        })
    }




}