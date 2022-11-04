package com.mongodb.realmlocal.android

import androidx.lifecycle.*
import com.mongodb.realmlocal.Repo
import com.mongodb.realmlocal.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val realmRepo = Repo()

    val users: LiveData<List<User>> = liveData {
        emitSource(realmRepo.getUsersAsFlow().flowOn(Dispatchers.IO).asLiveData(Dispatchers.Main))
    }

    //val users: MutableLiveData<List<User>> = MutableLiveData()


    fun saveUserInfo(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            realmRepo.saveUserInfo(user)
        }
    }

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = realmRepo.getUsersAsList()
            withContext(Dispatchers.Main) {
              //  users.value = list
            }
        }
    }


}