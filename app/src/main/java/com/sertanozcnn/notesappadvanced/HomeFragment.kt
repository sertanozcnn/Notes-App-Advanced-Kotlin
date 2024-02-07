package com.sertanozcnn.notesappadvanced

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sertanozcnn.notesappadvanced.adapter.NotesAdapter
import com.sertanozcnn.notesappadvanced.database.NotesDatabase
import com.sertanozcnn.notesappadvanced.databinding.FragmentHomeBinding
import com.sertanozcnn.notesappadvanced.entities.Notes
import kotlinx.coroutines.launch
import androidx.appcompat.widget.SearchView
import java.util.Locale

class HomeFragment : BaseFragment() {


    var arrNotes = ArrayList<Notes>()
    var notesAdapter:NotesAdapter = NotesAdapter(emptyList())
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the binding layout instead of the old layout
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }



    companion object {

        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        launch {
            context?.let {
                var notes = NotesDatabase.getDatabase(it).noteDao().getAllNotes()
                notesAdapter!!.setData(notes)
                arrNotes = notes as ArrayList<Notes>
                binding.recyclerView.adapter = notesAdapter
            }


        }

        notesAdapter!!.setOnClickListener(onClicked)

        binding.fabBtnCreateNote.setOnClickListener {
            replaceFragment(CreateNotesFragment.newInstance(),false)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return  true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var tempArr = ArrayList<Notes>()
                for(arr in arrNotes){
                    if (arr.title!!.toLowerCase(Locale.getDefault()).contains(newText.toString())){
                        tempArr.add(arr)
                    }
                }

                notesAdapter.setData(tempArr)
                notesAdapter.notifyDataSetChanged()
                return true

            }

        })

    }

    private val onClicked = object:NotesAdapter.OnItemClickListener{
        override fun onClicked(notesId: Int) {
            var fragment:Fragment
            var bundle = Bundle()
            bundle.putInt("noteId",notesId)
            fragment = CreateNotesFragment.newInstance()
            fragment.arguments = bundle
            replaceFragment(fragment,false)


        }

    }

    private fun replaceFragment(fragment: Fragment, istransition: Boolean) {
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()

        if (istransition) {
            fragmentTransition.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)

        }

        fragmentTransition.replace(R.id.frame_layout, fragment).addToBackStack(fragment.javaClass.simpleName).commit()


    }

}