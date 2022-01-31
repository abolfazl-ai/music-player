package com.example.compose.viewmodel

import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.ViewModel
import com.example.compose.local.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalMaterialApi
@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: Repository,
    val preferences: AppPreferences,
    val serviceController: ServiceController,
) : ViewModel() {

}