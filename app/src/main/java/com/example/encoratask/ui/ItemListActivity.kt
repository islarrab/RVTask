package com.example.encoratask.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.encoratask.R
import com.example.encoratask.databinding.ActivityItemListBinding
import com.example.encoratask.ui.adapter.CharacterAdapter
import com.example.encoratask.viewmodel.CharacterListViewModel
import com.google.android.material.imageview.ShapeableImageView

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity(), CharacterAdapter.CharacterItemListener {
    private val viewModel: CharacterListViewModel by lazy {
        ViewModelProvider(this)[CharacterListViewModel::class.java]
    }

    private lateinit var binding: ActivityItemListBinding
    private lateinit var adapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = CharacterAdapter(this)
        binding.itemList.layoutManager = LinearLayoutManager(this)
        binding.itemList.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.loadNextPage()
        viewModel.characterListLiveData.observe(this, {
            if (!it.isNullOrEmpty()) adapter.setItems(ArrayList(it))
        })
    }

    override fun onClickedCharacter(view: View?, characterId: Int) {
        val intent = Intent(this, ItemDetailActivity::class.java).apply {
            putExtra(ItemDetailFragment.ARG_ITEM_ID, characterId)
        }

        val imageView = view?.findViewById<ShapeableImageView>(R.id.item_image)

        startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(this, imageView, "itemImage").toBundle()
        )
    }

    override fun onLastCharacterVisible() {
        viewModel.loadNextPage()
    }
}