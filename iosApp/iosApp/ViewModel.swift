//
//  ViewModel.swift
//  iosApp
//
import Foundation
import Combine
import shared

// Generic Observable View Model, making it easier to control the lifecycle
// of multiple Flows.
class ObservableViewModel {
    private var jobs = Array<Closeable>() // List of Kotlin Coroutine Jobs

    func addObserver(observer: Closeable) {
        jobs.append(observer)
    }
    
    func stop() {
        jobs.forEach { job in job.close() }
    }
}

class IOSViewModel: ObservableViewModel, ObservableObject {
    @Published var name:String = ""
    @Published var twitterHandle: String = ""
    @Published var userList = [User]()
    
    var realmRepo = Repo()
    
    override init() {
        super.init()
    }
    
    deinit {
        super.stop()
    }
    
    func register() {
        realmRepo.doAppSignIn { _ in
            self.start()
        }

    }
        
    func saveUser(user: User) {
        realmRepo.saveUserInfo(user: user) { error in
            print("Error saving user")
        }
    }
    
    func start() {
        addObserver(observer: realmRepo.getUsersAsFlow().watch { list in
            self.userList = list as! [User]
        })
    }
}
