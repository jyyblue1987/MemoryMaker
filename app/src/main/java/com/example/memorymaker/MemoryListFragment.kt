package com.example.memorymaker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*


private const val TAG = "MemoryListFragment"



class MemoryListFragment : Fragment()  {



    private var callbacks: Callbacks? = null

    private lateinit var memoryRecyclerView:RecyclerView
    private var adapter:MemoryAdapter? = MemoryAdapter(emptyList())

    private val memoryListViewModel: MemoryListViewModel by lazy {
        ViewModelProviders.of(this).get(MemoryListViewModel::class.java)
    }

    private val memoryDetailViewModel: MemoryDetailViewModel by lazy {
        ViewModelProviders.of(this).get(MemoryDetailViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_memory_list, container, false)

        memoryRecyclerView = view.findViewById(R.id.memory_recycler_view) as RecyclerView
        memoryRecyclerView.layoutManager = LinearLayoutManager(context)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        memoryListViewModel.memories.observe(
            viewLifecycleOwner,
            { memories ->
                Log.i(TAG, "Got Pictures ${memories.size}")
                updateUI(memories)
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.new_memory -> {
                val memory = Memory()
                memoryListViewModel.addMemory(memory)
                callbacks?.onMemorySelected(memory.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(memories: List<Memory>){
        adapter = MemoryAdapter(memories)
        memoryRecyclerView.adapter = adapter
    }

    private inner class MemoryHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var memory: Memory

        val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        val titleTextView: TextView = itemView.findViewById(R.id.memory_title)
        val dateTextView: TextView = itemView.findViewById(R.id.memory_date)
        val btnFavorite: ImageButton = itemView.findViewById(R.id.memory_favorite)
        val favoriteImageView: ImageButton = itemView.findViewById(R.id.memory_favorite)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(memory: Memory)
        {
            this.memory = memory
            titleTextView.text = this.memory.title
            dateTextView.text = this.memory.date.toString()
            favoriteImageView.visibility = if(memory.isFavorite){
                View.VISIBLE
            }else{
                View.GONE
            }
            val photoFile = memoryDetailViewModel.getPhotoFile(memory)

            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            thumbnail.setImageBitmap(bitmap)
        }

        override fun onClick(v: View?) {
            callbacks?.onMemorySelected(memory.id)
        }

    }

    private inner class MemoryAdapter(var memories: List<Memory>) : RecyclerView.Adapter<MemoryHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryHolder {
            val view = layoutInflater.inflate(R.layout.list_item_memory, parent, false)
            return MemoryHolder(view)
        }
        override fun getItemCount() = memories.size

        override fun onBindViewHolder(holder: MemoryHolder, position: Int) {
            val memory = memories[position]
            holder.bind(memory)
        }

    }


    companion object
    {

        fun newInstance(): MemoryListFragment {
            return MemoryListFragment()
        }

    }




}